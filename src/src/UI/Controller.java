package UI;

import Database.Tables.ReceivedFrame;
import Database.Tables.SpottedAnomaly;
import Network.FrameReceiver;
import Network.NetworkFrame;
import Network.Server;
import VideoUtils.IPFrameSequence;
import com.example.noureddinebensebia.hackathonapp.NounouFrame;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import org.json.simple.JSONObject;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.System.*;


public class Controller {
/* it's an array containing all the ipaddress and their frames available in the database */


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
    private ListView<String> IP_Address_List;

    @FXML
    private ListView<SpottedAnomaly> anomaly_description;


    @FXML
    private Button video_stop;

    @FXML
    private Button video_start;

    @FXML
    private Button video_pause;
    @FXML
    private ImageView frame;
    @FXML
    private ListView<SpottedAnomaly> Table_anomaly;
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
            NetworkFrameReceiver();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

  /* web try*/
        final URL urlGoogleMaps = getClass().getResource("demo.html");
        webEngine.load(urlGoogleMaps.toExternalForm());
        webEngine.setOnAlert(new EventHandler<WebEvent<String>>() {
            @Override
            public void handle(WebEvent<String> e) {
                out.println(e.toString());
            }
        });






    }
    /* methods */

    public void Example_set() throws FileNotFoundException
    {

        //        SampleLoader(3);


        ArrayList<String> list_ip=ReceivedFrame.getIpsReceived(currentTimeMillis()-12000000,currentTimeMillis());


        System.out.println("ip:"+list_ip.size());

            this.IP_Address_List.getItems().addAll(list_ip);



        this.frame.setImage(new Image(new FileInputStream("./CamSpotServer/src/src/VideoUtils/5cm.jpg")));


    }







    public void NetworkFrameReceiver()
    {


        FrameReceiver frameReceiver = new FrameReceiver(new Server());
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        out.println("Current relative path is: " + s);
        frameReceiver.initFrameReceiver(new FrameReceiver.OnFrameReceived() {

            @Override
            public void onFrameReceived(NetworkFrame networkFrame, NounouFrame nounouFrame) {

               String path = networkFrame.getFrame().getIp().substring(1,networkFrame.getFrame().getIp().length());


                System.out.println("path"+path);




                       ;
                current_networkframe=new NetworkFrame(
                        networkFrame.getFrame().getFrameUrl(),path,networkFrame.getFrame().getAccountId(),networkFrame.getFrame().getMetaData().toString());





                int id = nounouFrame.getId();
                frameReceiver.id=id;

                current_networkframe.getFrame().addFrameToDatabase();
                try (FileOutputStream fos = new FileOutputStream(
                        "./CamSpotServer/src/src/VideoUtils/pathnamem"+id+".jpg")) {

                    fos.write(nounouFrame.getArray());

                    frameReceiver.path="./CamSpotServer/src/src/VideoUtils/pathnamem"+id+".jpg";

                    //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                current_networkframe.getFrame().setFrameUrl(frameReceiver.path);
                current_networkframe.getFrame().addFrameToDatabase();
                IP_Address_List.getItems().add(current_networkframe.getFrame().getIp());
            }
        });






















    }






/*simuler des examples aléatoires*/
    public void SampleLoader(int nbsamples)
    {


        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        out.println("Current relative path is: " + s);

        ArrayList<ReceivedFrame> receivedFrames= new ArrayList<>();


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

                java.sql.Timestamp timeStamp = new Timestamp(currentTimeMillis());
                java.sql.Date date = new java.sql.Date(timeStamp.getTime());
                Time time = new Time(timeStamp.getTime());
                //System.out.println(date);
                //System.out.println(time);

                NetworkFrame frame = new NetworkFrame("" +
                        "./CamSpotServer/src/src/VideoUtils/pathnamem"+i+".jpg",
                        "127.0.0."+j, 1, object.toJSONString());
                frame.getFrame().addFrameToDatabase();
                receivedFrames.add(frame.getFrame());





                IP_Address_List.getItems().add(frame.getFrame().getIp());

            }


        }

        this.Received_Frames=receivedFrames;
        //* i dont know if we need it*//
    ArrayList <String> ips_secondes=ReceivedFrame.getIpsReceived(currentTimeMillis()-8000,currentTimeMillis());



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
                            this.Table_anomaly.getItems().addAll(this.Spottedanomalys);
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
        this.Table_anomaly.getItems().addAll(this.Spottedanomalys);







    }

    public  void Button_addAnomaly()
    {

        SpottedAnomaly sp = new SpottedAnomaly(2, "weird person detected", currentTimeMillis());
        sp.addAnomalyToDatabase();

         SpottedAnomaly sp2 = new SpottedAnomaly(50, "weird person detected", currentTimeMillis());
        sp2.addAnomalyToDatabase();


    }

    public void Display_Frame ()
    {
    /* getting the selected ip address */


            centerImage();
            String ipR=IP_Address_List.getSelectionModel().getSelectedItems().get(0);

            /* putting image into a list and convert it to IMAGE*/

             IPFrameSequence  selected_ip = new IPFrameSequence(ipR,
                System.currentTimeMillis()-12000,System.currentTimeMillis());
             setimage =selected_ip.GetImages();
            /* displaying image one by one */

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
                                    frameSlider.increment();


                                }


                            }
                    ),
                    new KeyFrame(Duration.seconds(1))
            );
            timeline.setCycleCount(setimage.size());

            timeline.play();




            Updating_Anomaly_Table();











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
            out.println("restart:timeline");
        }
        else {
            out.println("restart:nulltimeline");

            timeline.playFromStart();
        }
    }

    public void ChangingSlide()
    {
        out.println("changed:"+frameSlider.getValue());
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



    public  void Display_anomaly_frame()
    {
           /* getting the selected ip address */
          centerImage();

        SpottedAnomaly ipR=Table_anomaly.getSelectionModel().getSelectedItems().get(0);

        ArrayList<ReceivedFrame> Receveidedframes =ipR.getFrams();

        for( int i=0;i< Receveidedframes.size();i++)
        {
            try
            {
                setimage.add(new Image(new FileInputStream(Receveidedframes.get(i).getFrameUrl())));
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }





        JOptionPane.showMessageDialog (null, "the IPAddress:"+ipR+" Has been Selected");





        for( int i=0;i< Receveidedframes.size();i++)
        {

            IPFrameSequence anomaly =
                    new IPFrameSequence(Receveidedframes.get(i).getIp(), currentTimeMillis() - 5000, currentTimeMillis() + 5000);
            setimage = anomaly.GetImages();
        }

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
                                slider2.increment();


                                imageAnomaly.setImage(i);
                                //  frameSlider.increment();
                            }


                        }
                ),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(setimage.size());

        timeline.play();



       /*map setting*/
        for (int i=0;i<Receveidedframes.size();i++)
        {
            System.out.println("spotted anomaly");
            webEngine.executeScript("window.lat = " +Receveidedframes.get(i).getMetaData().getLat()
                    + ";"
                    + "window.lon =" + Receveidedframes.get(i).getMetaData().getLon() + ";" + "document.goToLocation(window.lat, window.lon);");



        }









    }



}
