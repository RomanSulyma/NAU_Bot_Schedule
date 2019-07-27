import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.io.*;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class User {
    private Bot bot = new Bot();

    private ReplyKeyboardMarkup replyKeyBoardMarkUp = new ReplyKeyboardMarkup();
    private long chat_id;
    private String lastMessage;
    private int trigger = 0;

    private String department;
    private String group;
    private String subgroup;
    private String day_of_week;

    private  ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
    private KeyboardRow keyboardFirstRow = new KeyboardRow();
    private  KeyboardRow keyboardSecondRow = new KeyboardRow();
    private KeyboardRow keyboardThirdRow = new KeyboardRow();

    public long getChat_id() {
        return chat_id;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public void choice() {
        switch (trigger) {
            case 1: {
                if (lastMessage.equals("НАЗАД")) {
                    clear();
                    trigger = 0;
                    break;
                }
                department = lastMessage;
                groupChoice();
                break;
            }
            case 2: {
                if (lastMessage.equals("НАЗАД")) {
                    clear();
                    trigger = 0;
                    break;
                }
                lastMessage = lastMessage.replaceAll("[^0-9]", "");
                group = lastMessage;
                subGroupChoice();
                break;
            }
            case 3: {
                if (lastMessage.equals("НАЗАД")) {
                    clear();
                    trigger = 0;
                    break;
                }
                if (lastMessage.equals("1") || lastMessage.equals("2")) {
                    subgroup = lastMessage;
                    keyboardScheduleMenu();
                    break;
                }
            }
            case 4: {
                day_of_week = lastMessage;
                if(lastMessage.equals("В меню"))
                {
                    trigger=0;
                    break;
                }
                keyboardScheduleMenu();
                DBresult();
                break;
            }
        }

            switch (lastMessage) {
                case "/start":
                case "НАЗАД": {
                    trigger = 0;
                    keyboardMainMenu();
                    break;
                }
                case "Расписание Звонков": {
                    keyboardCallSchedule();
                    break;
                }
                case "Расписание": {
                    departmentChoice();
                    break;
                }
                case "Расписание на сегодня": {
                    dayNow();
                    break;
                }
                case "Расписание на неделю": {
                    keyboardDates();
                    break;
                }
                case "Карта НАУ": {
                    bot.sendMsg("http://kit.nau.edu.ua/images/pic.jpg",chat_id,replyKeyBoardMarkUp);
                    break;
                }
                case "В меню": {
                    keyboardScheduleMenu();
                    break;
                }



            }
            if(lastMessage.contains("sendall"))
                sendALL(lastMessage);

        }



    private void keyboardDates ()
        {
            keyboardClear();
            keyboardFirstRow.add(new KeyboardButton("ПН1"));
            keyboardFirstRow.add(new KeyboardButton("ВТ1"));
            keyboardFirstRow.add(new KeyboardButton("СР1"));
            keyboardFirstRow.add(new KeyboardButton("ЧТ1"));
            keyboardFirstRow.add(new KeyboardButton("ПТ1"));

            keyboardSecondRow.add(new KeyboardButton("ПН2"));
            keyboardSecondRow.add(new KeyboardButton("ВТ2"));
            keyboardSecondRow.add(new KeyboardButton("СР2"));
            keyboardSecondRow.add(new KeyboardButton("ЧТ2"));
            keyboardSecondRow.add(new KeyboardButton("ПТ2"));

            keyboardThirdRow.add(new KeyboardButton("В меню"));

            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            keyboard.add(keyboardThirdRow);
            replyKeyBoardMarkUp.setKeyboard(keyboard);
            trigger = 4;
            bot.sendMsg("Выбери день недели",chat_id,replyKeyBoardMarkUp);

        }

    private void keyboardMainMenu ()
        {
            keyboardClear();
            keyboardFirstRow.add(new KeyboardButton("Расписание"));
            keyboardSecondRow.add(new KeyboardButton("Расписание Звонков"));
            keyboardThirdRow.add(new KeyboardButton("Карта НАУ"));
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            keyboard.add(keyboardThirdRow);
            replyKeyBoardMarkUp.setKeyboard(keyboard);
            bot.sendMsg("Выбери задачу",chat_id,replyKeyBoardMarkUp);
        }

    private void keyboardCallSchedule ()
        {
            String call = "";
            call = call + "1 Пара 8.00 - 9.20" + "\n";
            call = call + "2 Пара 9.40 - 11.00" + "\n";
            call = call + "3 Пара 11.20 - 12.40" + "\n";
            call = call + "4 Пара 13.00 - 14.20" + "\n";
            call = call + "5 Пара 14.40 - 16.00" + "\n";
            call = call + "6 Пара 16.20 - 17.40" + "\n";
            call = call + "7 Пара 18.00 - 19.20" + "\n";
            call = call + "8 Пара 19.40 - 21.00" + "\n";

            bot.sendMsg(call,chat_id,replyKeyBoardMarkUp);

        }

    private void keyboardScheduleMenu ()
        {
            keyboardClear();
            keyboardFirstRow.add(new KeyboardButton("НАЗАД"));
            keyboardSecondRow.add(new KeyboardButton("Расписание на сегодня"));
            keyboardThirdRow.add(new KeyboardButton("Расписание на неделю"));
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            keyboard.add(keyboardThirdRow);
            replyKeyBoardMarkUp.setKeyboard(keyboard);
            trigger = 0;
            bot.sendMsg("Выбери день",chat_id,replyKeyBoardMarkUp);

        }

    private void departmentChoice ()
        {
            keyboardClear();
            List<String> list = listChoice("http://rozklad.nau.edu.ua/timetable/group", "select[name=institute]");

            for (String s : list) {
                KeyboardRow k = new KeyboardRow();
                KeyboardButton b = new KeyboardButton(s);
                k.add(b);
                keyboard.add(k);
            }
            replyKeyBoardMarkUp.setKeyboard(keyboard);
            trigger = 1;
            bot.sendMsg("Введи департамент",chat_id,replyKeyBoardMarkUp);

        }

    private void groupChoice ()
        {
            keyboardClear();
            List<String> list = listChoice("http://rozklad.nau.edu.ua/timetable/group/" + lastMessage, "select[name=group]");

            if(list != null) {
                for (String s : list) {
                    KeyboardRow k = new KeyboardRow();
                    KeyboardButton b = new KeyboardButton(s);
                    k.add(b);
                    keyboard.add(k);
                }
                replyKeyBoardMarkUp.setKeyboard(keyboard);
                trigger = 2;
                bot.sendMsg("Введи номер группы", chat_id, replyKeyBoardMarkUp);
            }

        }

    private void subGroupChoice ()
        {
            keyboardClear();

            keyboardFirstRow.add(new KeyboardButton("НАЗАД"));
            keyboardSecondRow.add(new KeyboardButton("1"));
            keyboardThirdRow.add(new KeyboardButton("2"));
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            keyboard.add(keyboardThirdRow);
            replyKeyBoardMarkUp.setKeyboard(keyboard);
            trigger = 3;
            bot.sendMsg("Выбери подгруппу",chat_id,replyKeyBoardMarkUp);
        }

        private List<String> listChoice(String url, String param)
        {
            Document page;
            try {
                page = Jsoup.parse(new URL(url), 10000);
            } catch (IOException e) {
                trigger = 0;
                keyboardMainMenu();
                bot.sendMsg("Отсутствует расписание для данного департамента :(",chat_id,replyKeyBoardMarkUp);
                return null;
            }
            List<String> list = new ArrayList<String>();
            Element tableNAU = page.select(param).first();

                    Elements names = tableNAU.select("option");
                    for (Element el : names) {
                        list.add(el.text());
                    }
                    list.set(0, "НАЗАД");

            return list;
        }

    private void dayNow ()
        {
            int number_of_week;
            Date start = null;

            try {
                start = new SimpleDateFormat("dd/MM/yyyy").parse("01/09/2019"); //получим дату 1-го сентября 2019
            } catch (ParseException exc) {
                exc.printStackTrace();
            }

            long delay = System.currentTimeMillis() - start.getTime(); //получим разницу (в мс) между сегодня и 1-ым сентября
            long week = 1000 * 60 * 60 * 24 * 7; //кол-во миллисекунд в одной неделе
            delay %= week * 2; //найдем остаток от деления разницы на две недели

            if (delay <= week) number_of_week = 1; //если разница меньше либо равна одной неделе, то это первая неделя
            else number_of_week = 2; //иначе вторая

            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            switch (dayOfWeek) {
                case 2:
                    day_of_week = "ПН"+number_of_week;
                    break;
                case 3:
                    day_of_week = "ВТ"+number_of_week;
                    break;
                case 4:
                    day_of_week = "СР"+number_of_week;
                    break;
                case 5:
                    day_of_week = "ЧТ"+number_of_week;
                    break;
                case 6:
                    day_of_week = "ПТ"+number_of_week;
                    break;

                default:
                    bot.sendMsg("На сегодня нету пар :)",chat_id,replyKeyBoardMarkUp);
                    trigger = 0;
                    return;
            }
            trigger = 0;
            DBresult();

        }

    private void DBresult()
        {
            String result = "";
            DBWorker DB = new DBWorker();
            DB.connect();

            String s = "SELECT * FROM departmentTable where DEPARTMENT  LIKE '%" + department + "' AND GROUP_NUMBER = " + group + " AND SUBGROUP = " + subgroup + " AND DAY_WEEK LIKE '" + day_of_week + "';";
            System.out.println(s);
            ResultSet res = DB.execute(s);
            try {
                while (res.next()) {
                    if (!res.getString(6).isEmpty())
                        result = result + res.getInt(5) + " Пара: \n" + res.getString(6) + "\n" + "\n";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            bot.sendMsg(result,chat_id,replyKeyBoardMarkUp);
            DB.disconnect();
        }

        public void sendALL(String message)
        {
            if(chat_id == 342252437) {
                message = message.replace("sendall","");

                DBWorker DB = new DBWorker();
                DB.connect();
                String s = "SELECT chatid FROM users;";
                ResultSet res = DB.execute(s);
                try {
                    while (res.next()) {
                        bot.sendMsg(message, res.getLong(1), replyKeyBoardMarkUp);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                DB.disconnect();
            }
        }

    private void keyboardClear()
        {
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardSecondRow.clear();
            keyboardThirdRow.clear();
            replyKeyBoardMarkUp.setSelective(true);
            replyKeyBoardMarkUp.setResizeKeyboard(true);
            replyKeyBoardMarkUp.setOneTimeKeyboard(false);
        }

    private void clear()
        {
         department = "";
         group = "";
         subgroup = "";
         day_of_week = "";
        }

}
