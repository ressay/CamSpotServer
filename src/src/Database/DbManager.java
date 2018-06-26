package Database;

import Database.Tables.ReceivedFrame;
import Network.NetworkFrame;

import java.util.ArrayList;
import java.util.Date;

/**
 *
 * Created by ressay on 24/06/18.
 */
public class DbManager
{


    public static String dateTimeToString(Date date)
    {
        return date.toString();
    }

    public static void addFrame(NetworkFrame frame)
    {
        frame.getFrame().addFrameToDatabase();
    }

    public static ArrayList<ReceivedFrame> getFramesOfIp(String ip)
    {
        return ReceivedFrame.getIPFrames(ip);
    }

    public static ArrayList<String> getIpAdresses(long from, long to)
    {
        return ReceivedFrame.getIpsReceived(from,to);
    }



}
