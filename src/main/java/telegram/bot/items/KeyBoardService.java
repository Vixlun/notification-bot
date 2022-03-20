package telegram.bot.items;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class KeyBoardService {

    @Bean
    public List<List<InlineKeyboardButton>> getInlineKeyboard() {
        List<InlineKeyboardButton> firstLine = new ArrayList<>();
        List<InlineKeyboardButton> secondLine = new ArrayList<>();
        AtomicInteger count = new AtomicInteger();
        int midpoint = Math.round(Sector.values().length) / 2;

        Arrays.stream(Sector.values()).map(sector ->
                InlineKeyboardButton.builder().text(sector.name()).callbackData(sector.name()).build()).forEach(next -> {
            int index = count.getAndIncrement();
            if (index < midpoint) {
                firstLine.add(next);
            } else {
                secondLine.add(next);
            }
        });
        return List.of(firstLine, secondLine);
    }
}