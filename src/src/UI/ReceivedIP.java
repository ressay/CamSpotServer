package UI;

import VideoUtils.BasicFrameSequence;
import VideoUtils.Frame;
import VideoUtils.FrameSequence;
import javafx.scene.image.Image;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by ressay on 22/06/18.
 */
public class ReceivedIP
{
    BasicFrameSequence sequence;
    String ipAddress;

    public String getIpAddress() {
        return ipAddress;
    }

    public ReceivedIP(BasicFrameSequence sequence, String ipAddress)
    {
        this.sequence = sequence;
        this.ipAddress = ipAddress;
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


