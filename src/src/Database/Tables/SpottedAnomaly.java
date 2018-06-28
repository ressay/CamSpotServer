package Database.Tables;

import Database.DbConnection;
import VideoUtils.FrameSequence;
import VideoUtils.IPFrameSequence;

import java.sql.*;
import java.util.ArrayList;

public class SpottedAnomaly {
    private static final String SpottedAnomalyTable = "Spotted_anomaly";
    //--------------------------------------Attributs------------------------------//
    private int idSpottedAnomaly;
    private String AnomalyDesc;
    private Timestamp DateAnomaly;
    //----------------------------------Constractor-------------------------------//
    public SpottedAnomaly(int idSpotted_anomaly, String AnomalyDesc, Timestamp DateAnomaly)
    {
        this.AnomalyDesc=AnomalyDesc;
        this.DateAnomaly=DateAnomaly;

    }
    public SpottedAnomaly(String AnomalyDesc, long DateAnomaly)
    {
        this.AnomalyDesc=AnomalyDesc;
        this.DateAnomaly=new Timestamp(DateAnomaly);
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

    public FrameSequence sequence()
    {
        ReceivedFrame frame = getFrames_cool();
        long timeFrame = frame.getTimeStamp().getTime();
        return new IPFrameSequence(frame.getIp(),timeFrame-2*1000,timeFrame+2*1000);
    }

    public static ArrayList<SpottedAnomaly>  getSpottedAnomaly()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "select * from "+SpottedAnomaly.SpottedAnomalyTable +";" ;
        PreparedStatement statement = null;
        ArrayList<SpottedAnomaly> frames = new ArrayList<>();
        try {
            statement = connection.prepareStatement(query);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {

                frames.add(
                        new SpottedAnomaly(set.getInt("idSpotted_anomaly"),
                                set.getString("Anomaly_description"),
                                set.getTimestamp("Date_anomaly")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return frames;
    }




    public ArrayList<ReceivedFrame>  getFrames(long from, long to)
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "select *, INET_NTOA(ipAddress) as ipAd from Frame_received " +
                "where (Spotted_anomaly_idSpotted_anomaly=? AND " +
                "(Frame_receivedDate >= ? AND Frame_receivedDate <= ?))";
        PreparedStatement statement;
        ArrayList<ReceivedFrame> frames = new ArrayList<>();
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1,this.getId());
            statement.setTimestamp(2,new Timestamp(from));
            statement.setTimestamp(3,new Timestamp(to));
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

    public ReceivedFrame  getFrames_cool()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "select *, INET_NTOA(ipAddress) as ipAd from Frame_received" +
                " where Spotted_anomaly_idSpotted_anomaly = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1,idSpottedAnomaly);
            ResultSet set = statement.executeQuery();
            while (set.next())
            {
                String frameUrl = set.getString("Image_url");
                String ipAd = set.getString("ipAd");
                int accountId = set.getInt("Account_idAccount");
                String json = set.getString("MetaDataDescription");
                Timestamp timeStamp = set.getTimestamp("Frame_receivedDate");
                return new ReceivedFrame(frameUrl,ipAd,accountId,json,timeStamp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    public int addAnomalyToDatabase()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query = "INSERT INTO "+SpottedAnomalyTable+" " +
                "(`Anomaly_description`)"+
                " VALUES" + "(?)";
        try
        {
            PreparedStatement statement = connection.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
//            statement.setString(1,String.valueOf(this.getId()));
            statement.setString(1,this.getDesc());
            statement.executeUpdate();
            ResultSet set = statement.getGeneratedKeys();
            if(set.next())
                return set.getInt(1);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return -1;
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


    static public ArrayList<SpottedAnomaly> getAnomalies()
    {
        Connection connection = DbConnection.getInstance().getConnection();
        String query ="Select * from "+SpottedAnomalyTable;
        ArrayList<SpottedAnomaly> anomalies = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            // statement.setInt(1,id);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                SpottedAnomaly anomaly = new SpottedAnomaly(set.getString("Anomaly_description"),0);
                anomaly.setId(set.getInt("idSpotted_anomaly"));
                anomalies.add(anomaly);
            }
            return anomalies;
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return anomalies;
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

    @Override
    public String toString()
    {
        return "SpottedAnomaly{" +
                "idSpottedAnomaly=" + idSpottedAnomaly +
                ", AnomalyDesc='" + AnomalyDesc + '\'' +
                '}';
    }
}
