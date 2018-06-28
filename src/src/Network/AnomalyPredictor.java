package Network;

import Database.Tables.ReceivedFrame;
import Database.Tables.SpottedAnomaly;
import UI.Anomaly;

/**
 * Created by ressay on 28/06/18.
 */
public class AnomalyPredictor
{
    int i = 0;
    public boolean detectAnomaly(ReceivedFrame frame)
    {
        i++;
        if(i >= 100)
        {
            i = 0;
            frameContainsAnomaly(frame);
            return true;
        }
        return false;
    }

    private void frameContainsAnomaly(ReceivedFrame frame)
    {
        SpottedAnomaly anomaly = new SpottedAnomaly("missing person",System.currentTimeMillis());
        int id = anomaly.addAnomalyToDatabase();
        frame.setAnomaly(id);
    }
}
