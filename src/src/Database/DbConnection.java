package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by ressay on 24/06/18.
 */

public class DbConnection {

    private String connectionURL;
    private String username;
    private String password;
    private String DBname;
    private Connection connection;
    private static DbConnection _instance = null;

    public DbConnection(String connectionURL, String username, String password, String DBname) {
        this.connectionURL = connectionURL;
        this.username = username;
        this.password = password;
        this.DBname = DBname;
        this.setConnection();
    }

    private void setConnection()
    {
        try
        {
//            System.out.println(connectionURL+DBname);
            this.connection = DriverManager.getConnection(this.connectionURL+this.DBname, username, password);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public Connection getConnection()
    {
        return connection;
    }

    private static void initDatabase()
    {
        _instance = new DbConnection("jdbc:mysql://localhost:3306/",
                "root",
                "hassina",
                "ServerCam");
    }

    public static DbConnection getInstance()
    {
        if(_instance == null)
            initDatabase();
        return _instance;
    }
}
