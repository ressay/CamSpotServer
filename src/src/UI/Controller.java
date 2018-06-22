package UI;

import VideoUtils.BasicFrameSequence;
import VideoUtils.Frame;
import com.sun.org.apache.regexp.internal.RE;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable
{
    // have something like this in your window
    ListView<ReceivedIP> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // something like this to get frames
//        list.getSelectionModel().getSelectedItems().get(0).getFrames();

        // something like this to create dummy data for test

//        BasicFrameSequence sequence = new BasicFrameSequence(
//                new Frame("frame1"),
//                new Frame("frame2"),
//                new Frame("frame3")
//        );
//        ReceivedIP receivedIP = new ReceivedIP(sequence,"address");
//        list.getItems().addAll(receivedIP);
    }
}
