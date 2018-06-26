package UI;

import VideoUtils.BasicFrameSequence;

import VideoUtils.Frame;
import com.sun.org.apache.regexp.internal.RE;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import sample.GoogleApp;


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

    /* for any example */


    Timeline timeline;
    List<Image> setimage ;
    Iterator<Image> imageIterator;


    /*map stuff*/
    GoogleApp.MyBrowser myBrowser;
    double lat;
    double lon;
    int index =0;
    final TextField latitude = new TextField("" + 35.857908 * 1.00007);
    final TextField longitude = new TextField("" + 10.598997 * 1.00007);
    @FXML
    private Slider frameSlider;
    @FXML
    private Tooltip tooltip;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<ReceivedIP> IP_Address_List;

    @FXML
    private ListView<Anomaly> anomaly_description;



    @FXML
    private Button video_stop;

    @FXML
    private Button video_start;

    @FXML
    private Button video_pause;
    @FXML
    private ImageView frame;
    @FXML
    private TableView<?> Table_anomaly;

    @FXML
    private WebView webmap;
    WebEngine webEngine;



    @FXML
    void initialize() {
        assert IP_Address_List != null : "fx:id=\"IP_Address_List\" was not injected: check your FXML file 'sample.fxml'.";
        assert anomaly_description != null : "fx:id=\"anomaly_description\" was not injected: check your FXML file 'sample.fxml'.";
        assert video_stop != null : "fx:id=\"video_stop\" was not injected: check your FXML file 'sample.fxml'.";
        assert video_start != null : "fx:id=\"video_start\" was not injected: check your FXML file 'sample.fxml'.";
        assert frame != null : "fx:id=\"frame\" was not injected: check your FXML file 'sample.fxml'.";
        assert video_pause != null : "fx:id=\"video_pause\" was not injected: check your FXML file 'sample.fxml'.";
        assert frameSlider != null : "fx:id=\"frameSlider\" was not injected: check your FXML file 'sample.fxml'.";
        assert Table_anomaly != null : "fx:id=\"Table_anomaly\" was not injected: check your FXML file 'sample.fxml'.";
        assert webmap != null : "fx:id=\"webmap\" was not injected: check your FXML file 'sample.fxml'.";

        {
            webEngine = webmap.getEngine();
        }

        /* set the example as default*/

        try {
            centerImage();
            Example_set();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

  /* web try*/
        final URL urlGoogleMaps = getClass().getResource("demo.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> e) {
                System.out.println(e.toString());
            }
        });

        Button update = new Button("Update");
        update.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent arg0) {
                lat = Double.parseDouble(latitude.getText());
                lon = Double.parseDouble(longitude.getText());

                System.out.printf("%.2f %.2f%n", lat, lon);

                webEngine.executeScript("" +
                        "window.lat = " + lat + ";" +
                        "window.lon = " + lon + ";" +
                        "document.goToLocation(window.lat, window.lon);"
                );
            }
        });

    }
    /* methods */

    public void Example_set() throws FileNotFoundException {
        /*image sequence*/

       BasicFrameSequence sequence = new BasicFrameSequence(
            new Frame("/home/masterubunto/CampSpot/CamSpotServer/src/src/VideoUtils/screen1.jpg"),
            new Frame("/home/masterubunto/CampSpot/CamSpotServer/src/src/VideoUtils/screen2.jpg"),
            new Frame("/home/masterubunto/CampSpot/CamSpotServer/src/src/VideoUtils/screen3.jpg")
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


         IP_Address_List.getItems().add(receivedIP);

   /*change the path my relative path doesnt work (choose any picture) */


        frame.setImage(new Image(new FileInputStream(
                "/home/masterubunto/CampSpot/CamSpotServer/src/src/VideoUtils/5cm.jpg")));


    }



    public void Display_Frame (){
    /* getting the selected ip address */


    centerImage();

    ReceivedIP ipR=IP_Address_List.getSelectionModel().getSelectedItems().get(0);

        JOptionPane.showMessageDialog (
                null, "the IPAddress:"+ipR+" Has been Selected");
    System.out.println("IP selected");





 /* putting image into a list and convert it to IMAGE*/
      setimage = ipR.GetImages();
        /* displaying image one by one */
        //Collections.shuffle(setimage);
        imageIterator = setimage.iterator();

        this.setFrameSlider(setimage.size(),1,1);



         timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> {
                            centerImage();

                            if(imageIterator.hasNext())
                            {   centerImage();
                                frame.setImage(imageIterator.next());
                              //  frameSlider.increment();
                            }


                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(setimage.size());
        /*this part enable a looping on the same sequence of images ,"we don't need it now"*/
     /*   timeline.setOnFinished(event -> {
            Collections.shuffle(setimage);
            imageIterator = setimage.iterator();
            timeline.playFromStart();
        });
       */
        timeline.play();


     this.anomaly_description.getItems().add(new Anomaly("",""));


    }




    public void centerImage() {
        Image img = frame.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = frame.getFitWidth() / img.getWidth();
            double ratioY = frame.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            frame.setX((frame.getFitWidth() - w) / 2);
            frame.setY((frame.getFitHeight() - h) / 2);

        }
    }


    public void ButtonPause()
    {

     if(timeline!=null)
         if(imageIterator.hasNext())
         timeline.play();

    }


    public void ButtonStop()
    {

        if(timeline!=null)
            timeline.stop();


    }
    public void ButtonRestart()
    {
        imageIterator = setimage.iterator();
        if(timeline!=null)
        {  timeline.playFromStart();
            System.out.println("restart:timeline");
        }
        else {
            System.out.println("restart:nulltimeline");

            timeline.playFromStart();
        }
    }

    public void ChangingSlide()
    {
        System.out.println("changed:"+frameSlider.getValue());
        if(setimage!=null) {
            centerImage();
            frame.setImage(setimage.get(((int) frameSlider.getValue() - 1)));
        }
    }


    public void setFrameSlider(int max , int min , int pace)
    {

    frameSlider.setMax(max);
    frameSlider.setMin(min);
    frameSlider.setId("hello");
    frameSlider.setShowTickLabels(true);

    frameSlider.setMajorTickUnit(pace);
    frameSlider.setMinorTickCount(0);
    frameSlider.showTickLabelsProperty();
  //  frameSlider.setOrientation(Orientation.HORIZONTAL);

    }



}
