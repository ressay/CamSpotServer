package UI;

import Database.DbManager;
import Database.Tables.SpottedAnomaly;
import com.sun.org.apache.regexp.internal.RE;
import javafx.application.Platform;
import javafx.scene.control.ListView;

import java.util.ArrayList;

/**
 * Created by ressay on 28/06/18.
 */
public class ListUpdaterThread
{
    public static void updateLists(Controller controller)
    {
        ListView<ReceivedIP> listIps = controller.IP_Address_List;
        ListView<ReceivedIP> anomalies = controller.Table_anomaly;
        new Thread(() -> {
            while (true)
            {
                ArrayList<String> ips = DbManager.getIpAdresses(
                        System.currentTimeMillis() - controller.timeBefore, System.currentTimeMillis()
                );
                ArrayList<SpottedAnomaly> anomalies1 = SpottedAnomaly.getAnomalies();
//                System.out.println(ips.size() + " and " + listIps.getItems().size());
                if (ips.size() != listIps.getItems().size())
                {
//                    System.out.println("something");
                    Platform.runLater(controller::loadIps);
                }
//                System.out.println(anomalies1.size() + " " + anomalies.getItems().size());
                if(anomalies1.size() != anomalies.getItems().size())
                {
//                    System.out.println("something");
                    Platform.runLater(controller::loadAnomalies);
                }

                try
                {
                    Thread.sleep(800);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
