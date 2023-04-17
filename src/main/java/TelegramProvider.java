import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.List;

public class TelegramProvider extends TelegramLongPollingBot {
    private static final String BotName = "Many_tries_after";
    private static final String Token = "6209908973:AAEf0_mTWrGA1p4Ajy31fenwpcjEt-rwwL4";
    private final Bot bot;

    public TelegramProvider(Bot newBot) {
        super();
        bot = newBot;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String message_text = update.getMessage().getText();
            long chat_id = update.getMessage().getChatId();

            String answer = null;
            try {
                answer = getAnswer(message_text, chat_id);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SendMessage message = new SendMessage().setChatId(chat_id).setText(answer);
            try {
                System.out.println(message);
                execute(message);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            List<Long> ids = bot.getChatIds();
            if (ids != null){
                for (long id : ids){
                    String ans = null;
                    try {
                        ans = getUpdate(chat_id, id);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    SendMessage mes = new SendMessage().setChatId(id).setText(ans);
                    try {
                        System.out.println(mes);
                        execute(mes);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                bot.noUpadete();
            }
        }
    }

    @Override
    public String getBotUsername() {
        return BotName;
    }

    @Override
    public String getBotToken() {
        return Token;
    }

    public String getAnswer(String question, long chat_id) throws IOException {
        return bot.takeAnswer(question, chat_id);
    }

    public String getUpdate(long chat_id, long update_id) throws IOException {
        return bot.takeUpdate(chat_id, update_id);
    }
}
