import java.sql.*;
import java.util.TimeZone;

import static javax.management.Query.value;

public class DBWorker {

    private static final String USER = "wtpbskgebkbagn";
    private final String PASS = "e9939d6bc247e26f749d09d02a37e75970c6c39f09b67411b0a70cd8f11776fe";
    private static String url = "jdbc:postgresql://ec2-54-217-234-157.eu-west-1.compute.amazonaws.com:5432/d8ra2j3ikmj92d?user=wtpbskgebkbagn&password=e9939d6bc247e26f749d09d02a37e75970c6c39f09b67411b0a70cd8f11776fe&?ssl=true&sslmode=require";
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
            PreparedStatement stat = conn.prepareStatement("insert into users (chatid) values (?);");
            stat.setInt(1,(int)chat_id);
            stat.executeUpdate();
            stat.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
