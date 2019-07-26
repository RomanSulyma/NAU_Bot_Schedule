import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static List<User> list = new ArrayList<>();

    public void onUpdateReceived(Update update) {

        long chat_id = update.getMessage().getChatId();
        String lastMessage = update.getMessage().getText();

        User user = null;
        DBWorker DB = new DBWorker();
        boolean trigger = false;

        DB.connect();
        ResultSet res = DB.execute("SELECT chatid FROM users where  (chatid = "+ chat_id +");");
        try {
            if(res.next() == false) {
                res.next();
                DB.insert(chat_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.disconnect();

        for(User userList : list)
        {
            if(userList.getChat_id() == chat_id)
            {
                user = userList;
                userList.setLastMessage(lastMessage);
                trigger = true;
            }
        }
        if(trigger == false)
        {
            user = new User();
            user.setChat_id(chat_id);
            user.setLastMessage(lastMessage);
            list.add(user);
        }

        user.choice();
    }

    public synchronized void sendMsg(String s,long chat_id, ReplyKeyboardMarkup replyKeyBoardMarkUp) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chat_id);
        sendMessage.setText(s);
        sendMessage.setReplyMarkup(replyKeyBoardMarkUp);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String getBotUsername() {
        return "schedule NAU";
    }

    public String getBotToken() {
        return "808303911:AAFXt6oXms88zTbyz4GYKomXHdLmIcltI0s";
    }


}