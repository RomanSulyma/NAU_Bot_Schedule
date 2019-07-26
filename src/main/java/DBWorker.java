import java.sql.*;
import java.util.TimeZone;

public class DBWorker {
    private static final String USER = "root";
    private final String PASS = "Romka132";
    private static String url = "jdbc:mysql://localhost:3306/mydbtest?serverTimezone=" + TimeZone.getDefault().getID()+"&useSSL=false";

    private Connection conn;
    private Statement stat;

    public void connect()
    {
        try {
            conn = DriverManager.getConnection(url,USER,PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect()
    {
        try {
            stat.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public ResultSet execute(String s)
    {
        stat = null;
        try {
            stat = conn.createStatement();
            ResultSet res = stat.executeQuery(s);
            return  res;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;


    }
    public void insert(long chat_id)
    {
        try {
            stat = conn.createStatement();
            stat.execute("insert into users (chatid) value ("+chat_id+");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
