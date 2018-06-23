package UI;

import VideoUtils.BasicFrameSequence;
import VideoUtils.Frame;
import com.sun.org.apache.regexp.internal.RE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.util.Duration;


import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

/*public class Controller implements Initializable
{
    // have something like this in your window
    ListView<ReceivedIP> list;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        // something like this to get frames
        list=new ListView<>();
        list.getSelectionModel().getSelectedItems().get(0).getFrames();

        // something like this to create dummy data for test


    }
}
*/
public class Controller {
/* it's an array containing all the ipaddress and their frames available in the database */

    protected SetOfReceivedIP DataBase_IPadrress;

    List<Image> setimage = new LinkedList<>();
    Iterator<Image> imageIterator;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> IP_Address_List;

    @FXML
    private ListView<?> anomaly_description;

    @FXML
    private Button video_stop;

    @FXML
    private Button video_start;

    @FXML
    private ImageView frame;

    @FXML
    void initialize() {
        assert IP_Address_List != null : "fx:id=\"IP_Address_List\" was not injected: check your FXML file 'sample.fxml'.";
        assert anomaly_description != null : "fx:id=\"anomaly_description\" was not injected: check your FXML file 'sample.fxml'.";
        assert video_stop != null : "fx:id=\"video_stop\" was not injected: check your FXML file 'sample.fxml'.";
        assert video_start != null : "fx:id=\"video_start\" was not injected: check your FXML file 'sample.fxml'.";
        assert frame != null : "fx:id=\"frame\" was not injected: check your FXML file 'sample.fxml'.";

        /* set the example as default*/

        try {
            Example_set();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
/* methods */

    public void Example_set() throws FileNotFoundException {
        /*image sequence*/

       BasicFrameSequence sequence = new BasicFrameSequence(
            new Frame("/home/masterubunto/Pictures/screen1.jpg"),
            new Frame("/home/masterubunto/Pictures/screen2.jpg"),
            new Frame("/home/masterubunto/Pictures/screen3.jpg")
    );


    /* put the full path if it doesnt work
        BasicFrameSequence sequence = new BasicFrameSequence(
                new Frame("../VideoUtils/screen1.jpg"),
                new Frame("../VideoUtils/screen2.jpg"),
                new Frame("../VideoUtils/screen3.jpg")
        );*/




        ReceivedIP receivedIP = new ReceivedIP(sequence,"127.0.0.1");

      /* well because it's still an example  i ll add it to the database (frame_received class) but when we ll do the connection then well use GET*/
        this.DataBase_IPadrress=new SetOfReceivedIP();
        this.DataBase_IPadrress.add(receivedIP);

        /* i choose to display the ip address only */
        IP_Address_List.getItems().add(receivedIP.getIpAddress());

//        ImageView iv = new ImageView(getClass().getResource("").toExternalForm());

        /*change the path my relative path doesnt work (choose any picture) */


        frame.setImage(new Image(new FileInputStream(
                "/home/masterubunto/Pictures/5cm.jpg")));


    }



    public void Display_Frame (){
    /* getting the selected ip address */
    String ipR=IP_Address_List.getSelectionModel().getSelectedItems().get(0);

        JOptionPane.showMessageDialog (
                null, "the IPAddress:"+ipR+" Has been Selected");
    System.out.println("IP selected");

    /* find the frames related to it in DATABASE_IPADRESS */


    ReceivedIP foundIp=DataBase_IPadrress.Look_up_Frame(ipR);


    /* looking for its frames */

    ArrayList<Frame> IpFrames=foundIp.getFrames();


    /* display the frames on the ImageView*/



    /*WORKING IN PROGRESS HERE*/


    /*displaying the first image*/


 /* putting image into a list and convert it to IMAGE*/

        try {
            for (int i=0; i<3; i++) {

                System.out.println("sequence:"+i);
               // frame.setImage(new Image(new FileInputStream(IpFrames.get(i).getFrameUrl())));
                setimage.add(new Image(new FileInputStream(
                        IpFrames.get(i).getFrameUrl())));


            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        /* displaying image one by one */
        Collections.shuffle(setimage);
        imageIterator = setimage.iterator();

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> {
                            frame.setImage(imageIterator.next());
                            System.out.println(
                                    "Displaying " + frame.getImage().impl_getUrl()
                            );
                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(setimage.size());
        timeline.setOnFinished(event -> {
            Collections.shuffle(setimage);
            imageIterator = setimage.iterator();
            timeline.playFromStart();
        });
        timeline.play();



    }





}
