package telegram.bot.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import telegram.bot.items.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class NotificationBot extends AbstractTelegramBot {
    private final String START_COMMAND              = "/start";
    private final String FINISH_COMMAND             = "/finish";
    private final String HELP_COMMAND               = "/help";

    @Autowired
    KeyBoardService keyBoardService;

    public void sendToClientsNotification(String atm, Sector sector) {
        activeUser.entrySet().stream().filter(entry -> entry.getValue() == sector)
                .forEach(user -> sendMessage(new SendMessage(user.getKey(), atm)));
    }

    @Override
    public void initActionForBadRequest() {
        actionForBadRequest = (error -> {
            if (error.getErrorType() == ErrorType.COMMAND_NOT_FOUND) {
                SendMessage message = SendMessage.builder()
                        .text(botResourceBundle.getString("bot.message.commandNotFound"))
                        .replyToMessageId(error.getRequest().getMessage().getMessageId())
                        .chatId(String.valueOf(error.getRequest().getMessage().getChatId()))
                        .build();
                sendMessage(message);
            } else {
                log.error("Internal error. Nothing to send");
            }
        });
    }

    @Override
    public void initActionForUserText() {
        actionForTextRequest = (messageContext -> {
            SendMessage message = new SendMessage();
            message.setText(botResourceBundle.getString("bot.massage.notActive"));
            message.setReplyMarkup(createReplyKeyBoard(Arrays.asList(START_COMMAND, FINISH_COMMAND, HELP_COMMAND),
                    Boolean.TRUE, Boolean.TRUE));
            message.setChatId(String.valueOf(messageContext.getChatId()));
            sendMessage(message);
        });
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()) {
            Sector sector = Sector.valueOf(update.getCallbackQuery().getData());
            String chatId = String.valueOf(update.getCallbackQuery().getMessage().getChatId());
            log.info("{} subscribed for notification in '{}' district", update.getCallbackQuery().getFrom(), sector.name());
            activeUser.put(chatId, sector);
            SendMessage message = SendMessage.builder()
                    .text(botResourceBundle.getString("bot.command.activated.message"))
                    .chatId(chatId)
                    .build();
            sendMessage(message);
        } else {
            super.onUpdateReceived(update);
        }
    }

    @Override
    public void configureBotCommandsMap() {
        commandsMap = Stream.of(
                TelegramBotCommand.builder()
                        .name(START_COMMAND)
                        .description(botResourceBundle.getString("bot.command.start.description"))
                        .action(msgContext -> {
                            log.debug("User = '{}' starting work with bot", msgContext.getUserLogin());
                            SendMessage message = SendMessage.builder()
                                    .text(botResourceBundle.getString("bot.command.start.message"))
                                    .chatId(String.valueOf(msgContext.getChatId()))
                                    .replyMarkup(InlineKeyboardMarkup.builder()
                                            .keyboard(keyBoardService.getInlineKeyboard()).build())
                                    .build();
                            sendMessage(message);
                        })
                        .build(),
                TelegramBotCommand.builder()
                        .name(HELP_COMMAND)
                        .description(botResourceBundle.getString("bot.command.help.description"))
                        .action(msgContext -> {
                            SendMessage message = SendMessage.builder()
                                    .text(botResourceBundle.getString("bot.command.help.message"))
                                    .chatId(msgContext.getChatId())
                                    .replyMarkup(createReplyKeyBoard(Arrays.asList(START_COMMAND, FINISH_COMMAND, HELP_COMMAND),
                                        Boolean.TRUE, Boolean.TRUE))
                                    .build();
                            sendMessage(message);
                        })
                        .build(),
                TelegramBotCommand.builder()
                        .name(FINISH_COMMAND)
                        .description(botResourceBundle.getString("bot.command.finish.description"))
                        .action(msgContext -> {
                            log.debug("User = '{}' finishing work with bot", msgContext.getUserLogin());
                            activeUser.remove(String.valueOf(msgContext.getChatId()));
                            SendMessage message = SendMessage.builder()
                                    .text(botResourceBundle.getString("bot.command.finish.message"))
                                    .chatId(msgContext.getChatId())
                                    .replyMarkup(createReplyKeyBoard(Arrays.asList(START_COMMAND, HELP_COMMAND),
                                            Boolean.TRUE, Boolean.TRUE))
                                    .build();
                            sendMessage(message);
                        })
                        .build())
                .collect(Collectors.toMap(TelegramBotCommand::getName, Function.identity()));
    }

    @Override
    public void initBotResourceBundle() {
        botResourceBundle = ResourceBundle.getBundle("notify-bot");
    }
}
