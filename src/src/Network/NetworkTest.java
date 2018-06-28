package Network;

import Database.Tables.ReceivedFrame;
import com.example.noureddinebensebia.hackathonapp.NounouFrame;

import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Base64;

import static java.lang.System.out;

public class NetworkTest {

    public static void main(String[] args) {


        FrameReceiver frameReceiver = new FrameReceiver(new Server());
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        out.println("Current relative path is: " + s);
        frameReceiver.initFrameReceiver(new FrameReceiver.OnFrameReceived() {
            @Override
            public void onFrameReceived(NetworkFrame networkFrame, NounouFrame nounouFrame) {
                int id = nounouFrame.getId();
                try (FileOutputStream fos = new FileOutputStream(s+"/pathname"+id+".jpg")) {

                    fos.write(nounouFrame.getArray());

                    //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
