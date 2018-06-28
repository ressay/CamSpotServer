package Database;

import Database.Tables.SpottedAnomaly;

import java.util.ArrayList;

/**
 * Created by masterubunto on 27/06/18.
 */
public class Thread extends java.lang.Thread
{

        public ArrayList<SpottedAnomaly> list_anomalies;

    @Override
    public void run() {
        while(true)
        {
            if( list_anomalies.size()!= SpottedAnomaly.getSpottedAnomaly().size())
                System.out.println("cool" +
                    "");


        }
    }
}
