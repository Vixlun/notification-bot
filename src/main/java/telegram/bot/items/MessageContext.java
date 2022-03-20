package telegram.bot.items;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

@Getter
@Setter
public class MessageContext {
    private Sector state;
    private final String chatId;
    private final String userLogin;
    private final Update request;

    public MessageContext(String chatId, String userLogin, Update request) {
        this.chatId = chatId;
        this.userLogin = userLogin;
        this.request = request;
    }
}
