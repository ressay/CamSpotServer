import Database.DbManager;
import Database.Tables.ReceivedFrame;
import Network.NetworkFrame;
import org.json.simple.JSONObject;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by ressay on 25/06/18.
 */
public class testMain
{


        public static void main(String[] args) {
        JSONObject object = new JSONObject();
        object.put("lat",new Double(3.456123));
        object.put("lon",new Double(1.556123));
        System.out.println(object.toJSONString());
        ReceivedFrame.FrameMetaData metaData = new ReceivedFrame.FrameMetaData(object.toJSONString());
        System.out.println("lat " + metaData.getLat() + " lon " + metaData.getLon());

        java.sql.Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
        java.sql.Date date = new java.sql.Date(timeStamp.getTime());
        Time time = new Time(timeStamp.getTime());
        System.out.println(date);
        System.out.println(time);

        NetworkFrame frame = new NetworkFrame("/home/masterubunto/CampSpot/CamSpotServer/src/src/VideoUtils/screen3.jpg",
                "127.0.0.2",1,object.toJSONString());
//        DbManager.addFrame(frame);
        ArrayList<ReceivedFrame> frames = ReceivedFrame.getIPFrames("127.0.0.1");
        for(ReceivedFrame f : frames)
        {
            System.out.println(f);
        }

        ArrayList<String> ips = ReceivedFrame.getIpsReceived(System.currentTimeMillis()-1000*3600*9,
                System.currentTimeMillis());
        for(String ip : ips)
        {
            System.out.println(ip);
        }

        ReceivedFrame.Deleted_data_base("select * from "+"/'"+
                ReceivedFrame.getFrameReceivedTable()
        +"/'");

//        launch(args);
    }
}
