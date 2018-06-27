package Database.Tables;

import Database.DbConnection;
import Network.NetworkFrame;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ressay on 25/06/18.
 */
public class ReceivedFrame
{

    private static final String frameReceivedTable = "Frame_received";

    public static String getFrameReceivedTable() {
        return frameReceivedTable;
    }

    public static class FrameMetaData
    {
        String desc;
        double lat;
        double lon;

        public FrameMetaData(String desc)
        {
            this.desc = desc;

            JSONParser parser = new JSONParser();
            try
            {
                JSONObject jsonObject =  (JSONObject) parser.parse(desc);

                lat = (double) jsonObject.get("lat");
                lon = (double) jsonObject.get("lon");
            } catch (ParseException e)
            {
                e.printStackTrace();
            }

        }

        public double getLat()
        {
            return lat;
        }

        public double getLon()
        {
            return lon;
        }

        @Override
        public String toString()
        {
            return desc;
        }
    }


    private String frameUrl;
    private String ip;
    private int accountId;
    private FrameMetaData metaData;
    private Timestamp timeStamp;

    public ReceivedFrame(String frameUrl, String ip, int accountId, String jsonDesc,long timeStamp)
    {
        this.frameUrl = frameUrl;
        this.ip = ip;
        this.accountId = accountId;
        this.metaData = new FrameMetaData(jsonDesc);
        this.timeStamp = new Timestamp(timeStamp);
    }

    public ReceivedFrame(String frameUrl, String ip, int accountId, String jsonDesc, Timestamp timeStamp)
    {
        this.frameUrl = frameUrl;
        this.ip = ip;
        this.accountId = accountId;
        this.metaData = new FrameMetaData(jsonDesc);
        this.timeStamp = timeStamp;
    }

    public void addFrameToDatabase()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO "+frameReceivedTable+"" +
                "(`ipAddress`, `MetaDataDescription`, `Image_url`, `Account_idAccount`)"+//", `Frame_receivedDate`)" +
                " VALUES" +
                "(INET_ATON(?),?,?,?)";
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,this.getIp());
            statement.setString(2,this.getMetaData().toString());
            statement.setString(3,this.getFrameUrl());
            statement.setInt(4,this.getAccountId());
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    static public void UPDATE_Samples_anomalyset(String ip)
    {

        Connection connection = DbConnection.getInstance().getConnection();
      /*  String query = "UPDATE "+frameReceivedTable+
                ""+"SET Spotted_anomaly_idSpotted_anomaly ="+id;*/

        String query =
                "UPDATE `Frame_received` SET `Spotted_anomaly_idSpotted_anomaly`=1 WHERE INET_NTOA(ipAddress) LIKE '"+ip+"'";
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);

            boolean rs =statement.execute();
            System.out.println("rs:update"+rs);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }



    }



    static  public  ArrayList<String> getIpAdress_having_anomaly(long from, long to)
    {

        ArrayList<String> ips = new ArrayList<>();
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT DISTINCT INET_NTOA(ipAddress) as ipAd " +
                "FROM `"+frameReceivedTable+ "` WHERE" +
                " (Frame_receivedDate >= ? AND Frame_receivedDate <= ?) AND " +
                "Spotted_anomaly_idSpotted_anomaly IS  NOT NULL ";
        System.out.println(query);
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            Timestamp f = new Timestamp(from);
            Timestamp t = new Timestamp(to);
            statement.setTimestamp(1,f);
            statement.setTimestamp(2,t);
            ResultSet set = statement.executeQuery();

            while (set.next())
            {
                String ipAd = set.getString("ipAd");
                ips.add(ipAd);
                System.out.println("anomaly:"+ipAd);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ips;

    }


    static public ArrayList<String> getIpsReceived(long from, long to)
    {
        ArrayList<String> ips = new ArrayList<>();
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT DISTINCT INET_NTOA(ipAddress) as ipAd " +
                "FROM `"+frameReceivedTable+ "` WHERE" +
                " (Frame_receivedDate >= ? AND Frame_receivedDate <= ?)";
        System.out.println(query);
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            Timestamp f = new Timestamp(from);
            Timestamp t = new Timestamp(to);
            statement.setTimestamp(1,f);
            statement.setTimestamp(2,t);
            ResultSet set = statement.executeQuery();

            while (set.next())
            {
                String ipAd = set.getString("ipAd");
                ips.add(ipAd);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return ips;
    }


    static public ArrayList<ReceivedFrame> getIPFrames(String ip)
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT "+frameReceivedTable+".*,INET_NTOA(ipAddress) as ipAd " +
                "FROM `"+frameReceivedTable+ "` WHERE INET_NTOA(ipAddress) = ?";
        System.out.println(query);
        ArrayList<ReceivedFrame> frames = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,ip);
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
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return frames;
    }



    static  public void Deleted_data_base (String query)
    {

        Connection connection = DbConnection.getInstance().getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }



    static public ArrayList<Image> getIPImage(String ip)
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "SELECT "+frameReceivedTable+".*,INET_NTOA(ipAddress) as ipAd " +
                "FROM `"+frameReceivedTable+ "` WHERE INET_NTOA(ipAddress) = ?";
        System.out.println(query);
        ArrayList<Image> frames = new ArrayList<>();
        try
        {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1,ip);
            ResultSet set = statement.executeQuery();

            while (set.next())
            {
                String frameUrl = set.getString("Image_url");
                String ipAd = set.getString("ipAd");
                int accountId = set.getInt("Account_idAccount");
                String json = set.getString("MetaDataDescription");
                Timestamp timeStamp = set.getTimestamp("Frame_receivedDate");
                try {

                    frameUrl=frameUrl.replaceFirst("ressay","masterubunto/Pictures");
                    frames.add(new Image(new FileInputStream(frameUrl)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return frames;
    }



    public String getFrameUrl()
    {
        return frameUrl;
    }

    public String getIp()
    {
        return ip;
    }

    public int getAccountId()
    {
        return accountId;
    }

    public FrameMetaData getMetaData()
    {
        return metaData;
    }

    public Timestamp getTimeStamp()
    {
        return timeStamp;
    }

    @Override
    public String toString()
    {
        return "ip: " + ip + ":dateTime: " + timeStamp;
    }
}
