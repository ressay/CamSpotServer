package Database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbQuerier
{

    private DbConnection dbConnection;


    public DbQuerier(DbConnection dbConnection)
    {
        this.dbConnection = dbConnection;
    }


    public ResultSet executeSelectQuery(String query)
    {
        ResultSet resultSet = null;
        try
        {
            Statement command = this.dbConnection.getConnection().createStatement();
            resultSet = command.executeQuery(query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return resultSet;
    }

    public int executeUpdateQuery(String query)
    {
        int updated = -1;
        try
        {
            Statement command = this.dbConnection.getConnection().createStatement();
            updated = command.executeUpdate(query);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return updated;
    }

    public static String createInsertQuery(String tableName, String... values)
    {
        String query = "INSERT INTO " + tableName + " VALUES(";
        for (String value : values)
        {
            query += value + ",";
        }
        query = query.substring(0, query.length() - 1);
        query += ")";
        return query;
    }

}
