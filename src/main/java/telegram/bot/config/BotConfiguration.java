package telegram.bot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import telegram.bot.items.AbstractTelegramBot;

import javax.annotation.PostConstruct;

@Slf4j
@Configuration
public class BotConfiguration {
    private final AbstractTelegramBot telegramBot;

    @Value("${bot.username}")
    private String theBotUserName;
    @Value("${bot.token}")
    private String theBotToken;

    @Autowired
    public BotConfiguration(AbstractTelegramBot ownTelegramBot) {
        this.telegramBot = ownTelegramBot;
    }

    @PostConstruct
    void initMethod() {
        log.info("Start registering telegram api");
        try {
            TelegramBotsApi telegramApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBot.setBotToken(theBotToken);
            telegramBot.setBotUserName(theBotUserName);
            telegramApi.registerBot(telegramBot);
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

}
