package Network;

import Database.Tables.ReceivedFrame;
import com.example.noureddinebensebia.hackathonapp.NounouFrame;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FrameReceiver {
    private Server server;
    public static int id;
    public static String path;
    public NetworkFrame nt;

    public FrameReceiver(Server server) {
        this.server = server;
    }

    public void initFrameReceiver (OnFrameReceived onFrameReceived)
    {
        Server server= new Server();
        server.initServer(5000, new Server.OnMessageReceivedListener() {
            @Override
            public void onMessageReceived(Object o,String ip) {
                if (o!=null)
                {
                    NounouFrame nounouFrame = (NounouFrame) o;
                    JSONObject object = new JSONObject();
                    object.put("lat",((NounouFrame) o).getLat());
                    object.put("lon",((NounouFrame) o).getLon());
                    try (FileOutputStream fos = new FileOutputStream("nounouf.jpg")) {
                        fos.write(nounouFrame.getArray());
                        onFrameReceived.onFrameReceived(
                                new NetworkFrame("frame"+nounouFrame.getId(),ip,
                                1,object.toJSONString()
                        ),((NounouFrame) o));
                        //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    public interface OnFrameReceived {
        public void onFrameReceived (NetworkFrame networkFrame,NounouFrame nounouFrame);
    }
}
