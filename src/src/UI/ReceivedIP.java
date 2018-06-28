package UI;

import VideoUtils.BasicFrameSequence;
import VideoUtils.Frame;
import VideoUtils.FrameSequence;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by ressay on 22/06/18.
 */
public class ReceivedIP
{
    FrameSequence sequence;
    String ipAddress;
    JSONObject obj;

    public String getIpAddress() {
        return ipAddress;
    }

    public ReceivedIP(FrameSequence sequence, String ipAddress,JSONObject obj1)
    {
        this.sequence = sequence;
        this.ipAddress = ipAddress;
        this.obj=obj1;
    }

    public ArrayList<Frame> getFrames()
    {
        return sequence.getFrames();
    }
    public ArrayList<Image> GetImages()  {return  sequence.GetImages();}
     public String toString() {
        return this.getIpAddress();
    }
}


