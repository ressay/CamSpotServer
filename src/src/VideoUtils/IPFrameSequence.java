package VideoUtils;

import Database.DbManager;
import Database.Tables.ReceivedFrame;

import java.util.ArrayList;

/**
 * this class gets a sequence of frames received by a given ip starting from a given time
 * Created by ressay on 25/06/18.
 */
public class IPFrameSequence extends FrameSequence
{
    private String ip;
    private long from;
    private long to;
    ArrayList<ReceivedFrame> frames;

    public IPFrameSequence(String ip, long from, long to)
    {
        this.ip = ip;
        this.from = from;
        this.to = to;
        frames = DbManager.getFramesOfIp(ip);
        generateFrameSequence();
    }

    private void generateFrameSequence()
    {
        ArrayList<Frame> videoFrames = new ArrayList<>();
        for (ReceivedFrame frame : frames)
        {
            if(from <= frame.getTimeStamp().getTime()
                    && frame.getTimeStamp().getTime() <= to)
            videoFrames.add(new Frame(frame.getFrameUrl()));
        }
    }

    @Override
    public ArrayList<Frame> getFrames()
    {
        return null;
    }
}
