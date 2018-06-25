package sample;

import Database.DbManager;
import Network.NetworkFrame;
import UI.ReceivedIP;
import VideoUtils.BasicFrameSequence;
import VideoUtils.Frame;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.simple.JSONObject;

import java.net.InetAddress;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("../UI/sample.fxml"));
        primaryStage.setTitle("Server!!");
        primaryStage.setScene(new Scene(root, 600, 575));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
