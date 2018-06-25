package Network;

import Database.Tables.ReceivedFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Created by ressay on 24/06/18.
 */
public class NetworkFrame
{
    private ReceivedFrame frame;

    public NetworkFrame(String frameUrl, String ip, int accountId, String jsonDesc)
    {
        this.frame = new ReceivedFrame(frameUrl,ip,accountId,jsonDesc,System.currentTimeMillis());
    }


    public ReceivedFrame getFrame()
    {
        return frame;
    }
}
