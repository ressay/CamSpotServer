package Database.Tables;

import Database.DbConnection;

import java.sql.*;
import java.util.ArrayList;

public class SpottedAnomaly {
    private static final String SpottedAnomalyTable = "Spotted_anomaly";
    //--------------------------------------Attributs------------------------------//
    private int idSpottedAnomaly;
    private String AnomalyDesc;
    private Timestamp DateAnomaly;
    //----------------------------------Constractor-------------------------------//
    SpottedAnomaly(int idSpottedAnomaly,String AnomalyDesc, Timestamp DateAnomaly)
    {
        this.AnomalyDesc=AnomalyDesc;
        this.DateAnomaly=DateAnomaly;
        this.idSpottedAnomaly=idSpottedAnomaly;

    }
    SpottedAnomaly(int idSpottedAnomaly,String AnomalyDesc, long DateAnomaly)
    {
        this.AnomalyDesc=AnomalyDesc;
        this.DateAnomaly=new Timestamp(DateAnomaly);
        this.idSpottedAnomaly=idSpottedAnomaly;
    }
    //-----------------------------------Methods---------------------------------//
    //Gets//////////////
    /**************************************/
    public int getId()
    {
        return this.idSpottedAnomaly;
    }
    public String getDesc()
    {
        return this.AnomalyDesc;
    }
    public Timestamp getDate()
    {
        return this.DateAnomaly;
    }
    public long getDateLong()
    {
        return this.DateAnomaly.getTime();
    }
    /**********************************/
    //Sets////////////////
    /*********************************/
    public void setId(int id)
    {
        this.idSpottedAnomaly=id;
    }
    public void setDesc(String desc)
    {
        this.AnomalyDesc=desc;
    }
    public void setDate(Timestamp dat)
    {
        this.DateAnomaly=dat;
    }
    public void setDate(long dat)
    {
        this.DateAnomaly=new Timestamp(dat);
    }
    /*********************************/
    //base methods//
    /********************************/
    public ArrayList<ReceivedFrame>  getFrams()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "select * from Frame_received where Spotted_anomaly_idSpotted_anomaly="+this.getId();
        PreparedStatement statement = null;
        ArrayList<ReceivedFrame> frames = new ArrayList<>();
        try {
            statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {
                String frameUrl = set.getString("Image_url");
                String ipAd = set.getString("ipAd");
                int accountId = set.getInt("Account_idAccount");
                String json = set.getString("MetaDataDescription");
                Timestamp timeStamp = set.getTimestamp("Frame_receivedDate");
                frames.add(new ReceivedFrame(frameUrl,ipAd,accountId,json,timeStamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return frames;
    }

    public ArrayList<ReceivedFrame>  getFrams_cool()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "select * from Frame_received" +
                " where Spotted_anomaly_idSpotted_anomaly="+this.getId();
        PreparedStatement statement = null;
        ArrayList<ReceivedFrame> frames = new ArrayList<>();
        try {
            statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {
                String frameUrl = set.getString("Image_url");
                String ipAd = set.getString("ipAd");
                int accountId = set.getInt("Account_idAccount");
                String json = set.getString("MetaDataDescription");
                Timestamp timeStamp = set.getTimestamp("Frame_receivedDate");
                frames.add(new ReceivedFrame(frameUrl,ipAd,accountId,json,timeStamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return frames;
    }




    public void addAnomalyToDatabase()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO "+SpottedAnomalyTable+" " +
                "(`idSpotted_anomaly`, `Anomaly_description`, `Date_anomaly`)"+
                " VALUES" + "(?,?,?)";
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,String.valueOf(this.getId()));
            statement.setString(2,this.getDesc());
            statement.setString(3,this.getDate().toString());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    public void getAnomalyFromDatabase(int id) //using its id
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query ="Select * from "+SpottedAnomalyTable+" where idSpotted_anomaly="+id;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
           // statement.setInt(1,id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                this.setId(set.getInt("idSpotted_anomaly"));
                this.setDate(set.getTimestamp("Date_anomaly"));
                this.setDesc(set.getString("Anomaly_description"));
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
    public void updateFramesWithAnomaly(int idFrame)
    {
        //updating frame having id=idFrame
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "UPDATE Frame_received set Spotted_anomaly_idSpotted_anomaly="+this.getId()+" where idFrame="+idFrame;
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }


    }
}
