package VideoUtils;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ressay on 22/06/18.
 */
public class BasicFrameSequence extends FrameSequence
{
    ArrayList<Frame> sequence;

    public BasicFrameSequence(ArrayList<Frame> sequence)
    {
        this.sequence = sequence;
    }

    public BasicFrameSequence(Frame... sequence)
    {
        this.sequence = new ArrayList<>();
        Collections.addAll(this.sequence, sequence);
    }

    @Override
    public ArrayList<Frame> getFrames()
    {
        return sequence;
    }
}
