package Database;

import Network.NetworkFrame;

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



}
