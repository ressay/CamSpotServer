package UI;

import VideoUtils.Frame;
import VideoUtils.FrameSequence;

import java.net.InetAddress;
import java.util.ArrayList;

/**
 * Created by ressay on 22/06/18.
 */
public class ReceivedIP
{
    FrameSequence sequence;
    String ipAddress;

    public String getIpAddress() {
        return ipAddress;
    }

    public ReceivedIP(FrameSequence sequence, String ipAddress)
    {
        this.sequence = sequence;
        this.ipAddress = ipAddress;
    }

    public ArrayList<Frame> getFrames()
    {
        return sequence.getFrames();
    }
}


