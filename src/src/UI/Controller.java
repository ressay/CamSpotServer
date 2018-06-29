package UI;

import Database.DbManager;
import Database.Tables.ReceivedFrame;
import Database.Tables.SpottedAnomaly;
import Network.AnomalyPredictor;
import Network.FrameStorer;
import Network.NetworkFrame;
import VideoUtils.FrameSequence;
import VideoUtils.IPFrameSequence;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


public class Controller {
/* it's an array containing all the ipaddress and their frames available in the database */

    public long timeBefore = 1000*60*60*5;
    protected Timeline timeline;
    protected List<Image> setimage;
    protected Iterator<Image> imageIterator;




    protected Thread updating_anomaly;

    protected NetworkFrame current_networkframe;




    /*map stuff*/

    double lat;
    double lon;
    int index = 0;
    ArrayList<SpottedAnomaly> Spottedanomalys;
    ArrayList<ReceivedFrame> Received_Frames;



    @FXML
    private Slider frameSlider;
    @FXML
    private Tooltip tooltip;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    public ListView<ReceivedIP> IP_Address_List;

    @FXML
    public ListView<SpottedAnomaly> anomaly_description;


    @FXML
    private Button video_stop;

    @FXML
    private Button video_start;

    @FXML
    private Button video_pause;
    @FXML
    private ImageView frame;
    @FXML
    public ListView<ReceivedIP> Table_anomaly;
    @FXML
    private WebView webmap;
    private WebEngine webEngine;

    @FXML
    private BorderPane frameslider2;

    @FXML
    private Slider slider2;

    @FXML
    private ImageView imageAnomaly;

    public AnomalyPredictor predictor = new AnomalyPredictor();

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
        assert frameslider2 != null : "fx:id=\"frameslider2\" was not injected: check your FXML file 'sample.fxml'.";
        assert imageAnomaly != null : "fx:id=\"imageAnomaly\" was not injected: check your FXML file 'sample.fxml'.";
        assert webmap != null : "fx:id=\"webmap\" was not injected: check your FXML file 'sample.fxml'.";
        assert slider2 != null : "fx:id=\"slider2\" was not injected: check your FXML file 'sample.fxml'.";
        {
            webEngine = webmap.getEngine();
        }

        /* set the example as default*/

//        try {
            centerImage();


//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

  /* web try*/
        final URL urlGoogleMaps = getClass().getResource("demo.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setOnAlert(e -> System.out.println(e.toString()));

        IP_Address_List.setOnMouseClicked(mouseEvent -> this.Display_Frame());
        loadIps();
        loadAnomalies();

        ListUpdaterThread.updateLists(this);
        FrameStorer.storeFrames(predictor);
    }

    public void loadAnomalies()
    {
        ArrayList<SpottedAnomaly> anomalies = SpottedAnomaly.getAnomalies();
        this.anomaly_description.getItems().addAll(anomalies);
        ArrayList<ReceivedIP> rIps = new ArrayList<>();
        for(SpottedAnomaly anomaly : anomalies) {
            rIps.add(new ReceivedIP(anomaly.sequence(), anomaly.getFrames_cool().getIp(), null));
            String lat = (String.valueOf(anomaly.getFrames_cool().getMetaData().getLat()));
            String lon = (String.valueOf(anomaly.getFrames_cool().getMetaData().getLon()));
            webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    webEngine.executeScript("" + "window.lat = " + lat + ";" +
                            "window.lon = " + lon + ";" + "document.addmarker(window.lat, window.lon);");
                }
            });

        }


        Table_anomaly.getItems().clear();
        Table_anomaly.getItems().addAll(rIps);

        this.anomaly_description.getItems().clear();
        this.anomaly_description.getItems().addAll(SpottedAnomaly.getAnomalies());
    }

    public void loadIps()
    {
        ArrayList<String> ips = DbManager.getIpAdresses(
                System.currentTimeMillis()- timeBefore,System.currentTimeMillis()
        );
        ArrayList<ReceivedIP> rIps = new ArrayList<>();
        for(String ip : ips)
        {
            rIps.add(new ReceivedIP(new IPFrameSequence(ip,System.currentTimeMillis()-timeBefore
            ,System.currentTimeMillis()),ip,null));
        }
        IP_Address_List.getItems().clear();
        IP_Address_List.getItems().addAll(rIps);
//        ArrayLisst
//        IP_Address_List.getItems().add(frame.getFrame());


        this.anomaly_description.getItems().clear();
        this.anomaly_description.getItems().addAll(SpottedAnomaly.getAnomalies());

    }

    /* methods */

    public void Display_Frame()
    {
    /* getting the selected ip address */
    centerImage();
    ReceivedIP ipR=IP_Address_List.getSelectionModel().getSelectedItems().get(0);
//    JOptionPane.showMessageDialog (null, "the IPAddress:"+ipR+" Has been Selected");

    /* putting image into a list and convert it to IMAGE*/
        setimage = ReceivedFrame.getIPImage(ipR.getIpAddress());
        /* displaying image one by one */
        //Collections.shuffle(setimage);
        imageIterator = setimage.iterator();


        System.out.println("nb image: selected ip"+ipR.GetImages().size());

        this.setFrameSlider(frameSlider,setimage.size(),1,1);




         timeline = new Timeline(
                new KeyFrame(
                        Duration.ZERO,
                        e -> {
                            centerImage();

                            if(imageIterator.hasNext())
                            {   centerImage();
                                 Image i=imageIterator.next();
                                 for (int j=0; j<50; j++)

                                 {
                                   frameSlider.increment();


                                 }
                                 frameSlider.increment();

                                frame.setImage(i);


                            }


                        }
                ),
                new KeyFrame(Duration.millis(10))
        );
        timeline.setCycleCount(setimage.size());

        timeline.play();







    }

    public void Updating_Anomaly_Table()
    {




        ReceivedFrame.Deleted_data_base("SET FOREIGN_KEY_CHECKS=0;");
//       ReceivedFrame.Deleted_data_base("ALTER TABLE Frame_received MODIFY  CONSTRAINT `fk_Frame_received_Spotted_anomaly1` DISABLE");
        ReceivedFrame.Deleted_data_base("DELETE FROM `Spotted_anomaly` WHERE 1");
        ReceivedFrame.Deleted_data_base("SET FOREIGN_KEY_CHECKS=1;");


        this.Spottedanomalys=new ArrayList<>();

        Runnable runnable = () ->
        {
            while(true)
            {
                if(this.Spottedanomalys!=null) {
                    if (this.Spottedanomalys.size() != SpottedAnomaly.getSpottedAnomaly().size()) {
                        {
                            //System.out.println("update");

                            // this.Table_anomaly.getItems().addAll(this.Spottedanomalys);
                            this.Spottedanomalys = SpottedAnomaly.getSpottedAnomaly();
                          ///  this.Table_anomaly.getItems().addAll(SpottedAnomaly.getAnomalies());
                            this.anomaly_description.getItems().addAll(this.Spottedanomalys);
                        }
                    }
                    else
                        this.Spottedanomalys=SpottedAnomaly.getSpottedAnomaly();

                }
                //  if(this.Spottedanomalys!=null)
                // System.out.println("updating:"+this.Spottedanomalys.size());

            }


        };


        Thread ui= new Thread(runnable);
        this.updating_anomaly=ui;


        this.updating_anomaly.start();


        if(this.Spottedanomalys!=null)
           // this.Table_anomaly.getItems().addAll(this.Spottedanomalys);
                this.anomaly_description.getItems().addAll(this.Spottedanomalys);
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

        ReceivedIP receivedIP = Table_anomaly.getSelectionModel().getSelectedItems().get(0);
        String ipR= receivedIP.toString();


//        JOptionPane.showMessageDialog (null, "the IPAddress:"+ipR+" Has been Selected");



        FrameSequence anomaly = receivedIP.sequence;
//        IPFrameSequence anomaly= new IPFrameSequence(ipR,System.currentTimeMillis()-5000,System.currentTimeMillis()+5000);
        setimage=anomaly.GetImages();


        /* displaying image one by one */

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
                new KeyFrame(Duration.millis(101))
        );
        timeline.setCycleCount(setimage.size());

        timeline.play();

      String lat="36.7525";
      String lon ="13.0420";


        System.out.println("lat:"+ReceivedFrame.getIPFrames(ipR).get(0).getMetaData().getLat()+
                "\nlon:"+ReceivedFrame.getIPFrames(ipR).get(0).getMetaData().getLon());

        lat= String.valueOf(ReceivedFrame.getIPFrames(ipR).get(0).getMetaData().getLat());
        lon= String.valueOf(ReceivedFrame.getIPFrames(ipR).get(0).getMetaData().getLon());


        System.out.println("lat2:"+lat+":lon:"+lon);



        webEngine.executeScript(""
                + "window.lat = " + lat
                + ";" +
                "window.lon = " + lon +
                ";" + "document.goToLocation(window.lat, window.lon);");






    }



}
