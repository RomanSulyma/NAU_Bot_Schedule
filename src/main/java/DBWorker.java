import java.sql.*;
import java.util.TimeZone;

public class DBWorker {

    private static final String USER = "wtpbskgebkbagn";
    private final String PASS = "e9939d6bc247e26f749d09d02a37e75970c6c39f09b67411b0a70cd8f11776fe";
    private static String url = "jdbc:postgres://wtpbskgebkbagn:e9939d6bc247e26f749d09d02a37e75970c6c39f09b67411b0a70cd8f11776fe@ec2-54-217-234-157.eu-west-1.compute.amazonaws.com:5432/d8ra2j3ikmj92d";
//jdbc:mysql://localhost:3306/mydbtest?serverTimezone=" + TimeZone.getDefault().getID()+"&useSSL=false
    private Connection conn;
    private Statement stat;

    public void connect()
    {
        try {
            conn = DriverManager.getConnection(url);
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
