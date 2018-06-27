package UI;

import Database.Tables.ReceivedFrame;
import Network.NetworkFrame;
import VideoUtils.BasicFrameSequence;

import VideoUtils.Frame;
import VideoUtils.IPFrameSequence;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.simple.JSONObject;
import sample.GoogleApp;


import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
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


    protected Timeline timeline;
    protected List<Image> setimage;
    protected Iterator<Image> imageIterator;


    /*map stuff*/

    double lat;
    double lon;
    int index = 0;
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
    private ListView<ReceivedFrame> IP_Address_List;

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
    private ListView<String> Table_anomaly;
    @FXML
    private WebView webmap;
    private WebEngine webEngine;

    @FXML
    private BorderPane frameslider2;

    @FXML
    private Slider slider2;

    @FXML
    private ImageView imageAnomaly;
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
        assert Table_anomaly != null : "fx:id=\"Table_anomaly\" was not injected: check your FXML file 'sample.fxml'.";
        assert frameslider2 != null : "fx:id=\"frameslider2\" was not injected: check your FXML file 'sample.fxml'.";
        assert imageAnomaly != null : "fx:id=\"imageAnomaly\" was not injected: check your FXML file 'sample.fxml'.";
        assert webmap != null : "fx:id=\"webmap\" was not injected: check your FXML file 'sample.fxml'.";
        assert slider2 != null : "fx:id=\"slider2\" was not injected: check your FXML file 'sample.fxml'.";
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

                webEngine.executeScript("" + "window.lat = " + lat + ";" + "window.lon = " + lon + ";" + "document.goToLocation(window.lat, window.lon);");
            }
        });

    }
    /* methods */

    public void Example_set() throws FileNotFoundException {
        SampleLoader(3);
        this.frame.setImage(

                new Image(new FileInputStream("./CamSpotServer/src/src/VideoUtils/5cm.jpg")));


    }

    public void SampleLoader(int nbsamples)
    {


        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current relative path is: " + s);


        for(int i=1;i<4;i++)
        {
            for(int j=3;j<nbsamples+3;j++)
            {
                JSONObject object = new JSONObject();
                object.put("lat", new Double(3.456123+i));
                object.put("lon", new Double(1.556123+j));
             //   System.out.println(object.toJSONString());
              //  ReceivedFrame.FrameMetaData metaData = new ReceivedFrame.FrameMetaData(object.toJSONString());
              //  System.out.println("lat " + metaData.getLat() + " lon " + metaData.getLon());

                java.sql.Timestamp timeStamp = new Timestamp(System.currentTimeMillis());
                java.sql.Date date = new java.sql.Date(timeStamp.getTime());
                Time time = new Time(timeStamp.getTime());
                //System.out.println(date);
                //System.out.println(time);

                NetworkFrame frame = new NetworkFrame("" +
                        "./CamSpotServer/src/src/VideoUtils/screen"+i+".jpg",
                        "127.0.0."+j, 1, object.toJSONString());
                frame.getFrame().addFrameToDatabase();


                ReceivedFrame.UPDATE_Samples_anomalyset("127.0.0.3");

                IP_Address_List.getItems().add(frame.getFrame());

            }
        }




    }

    public void GetintoTableView()
    {


        ArrayList <String> reponse= ReceivedFrame.getIpAdress_having_anomaly
                (System.currentTimeMillis()-600*600*3,System.currentTimeMillis());
        if(reponse!=null)
        System.out.println("worked"+reponse.get(0));
        else
        System.out.println("hello i'm not working");

        this.Table_anomaly.getItems().addAll(reponse);



    }

    public void Display_Frame (){
    /* getting the selected ip address */


    centerImage();

    ReceivedFrame ipR=IP_Address_List.getSelectionModel().getSelectedItems().get(0);

        JOptionPane.showMessageDialog (
                null, "the IPAddress:"+ipR+" Has been Selected");
    System.out.println("IP selected");





 /* putting image into a list and convert it to IMAGE*/
      setimage = ReceivedFrame.getIPImage(ipR.getIp());
        /* displaying image one by one */
        //Collections.shuffle(setimage);
        imageIterator = setimage.iterator();

        this.setFrameSlider(frameSlider,setimage.size(),1,1);




         timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> {
                            centerImage();

                            if(imageIterator.hasNext())
                            {   centerImage();
                                 Image i=imageIterator.next();
                                frame.setImage(i);

                                imageAnomaly.setImage(i);
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


     this.anomaly_description.getItems().add(new Anomaly("Car_accident","happened at time:"+System.nanoTime()));


     GetintoTableView();


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


    public void setFrameSlider( Slider s,int max , int min , int pace)
    {

    s.setMax(max);
    s.setMin(min);
        s.setId("hello");
        s.setShowTickLabels(true);

        s.setMajorTickUnit(pace);
        s.setMinorTickCount(0);
        s.showTickLabelsProperty();
  //  frameSlider.setOrientation(Orientation.HORIZONTAL);

    }



    public  void Display_anomaly_frame ()
    {
           /* getting the selected ip address */


        centerImage();

        String ipR=Table_anomaly.getSelectionModel().getSelectedItems().get(0);

        JOptionPane.showMessageDialog (
                null, "the IPAddress:"+ipR+" Has been Selected");
        System.out.println("IP selected");



        IPFrameSequence anomaly= new IPFrameSequence(ipR,System.currentTimeMillis()-5000,System.currentTimeMillis()+5000);
        setimage=anomaly.GetImages();

 /*
        /* displaying image one by one */
        //Collections.shuffle(setimage);
        imageIterator = setimage.iterator();

        this.setFrameSlider(slider2,setimage.size(),1,1);




        timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> {
                            centerImage();

                            if(imageIterator.hasNext())
                            {   centerImage();
                                Image i=imageIterator.next();

                                imageAnomaly.setImage(i);
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








    }



}
