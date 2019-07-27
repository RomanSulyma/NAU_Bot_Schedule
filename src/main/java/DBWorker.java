import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class DBWorker {

    private Connection conn;

    private static Connection getConnection() throws URISyntaxException, SQLException {
        URI dbUri = new URI(System.getenv("DATABASE_URL"));

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        return DriverManager.getConnection(dbUrl, username, password);
    }
    private Statement stat;

    public void connect()
    {
        try {
            conn = getConnection();
        } catch (SQLException | URISyntaxException e) {
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
