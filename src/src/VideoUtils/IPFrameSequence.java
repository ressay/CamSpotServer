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
    private long time;
    private int timeLaps;
    private int timeLimit;
    ArrayList<ReceivedFrame> frames;

    public IPFrameSequence(String ip, long time, int timeLaps,int timeLimit)
    {
        this.ip = ip;
        this.time = time;
        this.timeLaps = timeLaps;
        this.timeLimit = timeLimit;
        frames = DbManager.getFramesOfIp(ip);
        generateFrameSequence();
    }

    private void generateFrameSequence()
    {

    }

    @Override
    public ArrayList<Frame> getFrames()
    {
        return null;
    }
}
