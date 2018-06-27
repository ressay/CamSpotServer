package Network;

import Database.Tables.ReceivedFrame;
import com.example.noureddinebensebia.hackathonapp.NounouFrame;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;

public class NetworkTest {

    public static void main(String[] args) {
        FrameReceiver frameReceiver = new FrameReceiver(new Server());
        frameReceiver.initFrameReceiver(new FrameReceiver.OnFrameReceived() {
            @Override
            public void onFrameReceived(NetworkFrame networkFrame) {
                System.out.println("frame received from "+networkFrame.getFrame().getIp());
            }
        });
    }

}
