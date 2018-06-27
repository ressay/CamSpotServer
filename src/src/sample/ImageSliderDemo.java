package sample;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class ImageSliderDemo extends Application {

    private List<String> list = new ArrayList<String>();
    int j = 0;
    double orgCliskSceneX, orgReleaseSceneX;
    Button lbutton, rButton;
    ImageView imageView;

    @Override
    public void start(Stage primaryStage) {
        // images in src folder.
        try {
            list.add("/home/masterubunto/Pictures/screen1.jpg");
            list.add("/home/masterubunto/Pictures/screen2.jpg");
            list.add("/home/masterubunto/Pictures/screen3.jpg");


            GridPane root = new GridPane();
            root.setAlignment(Pos.CENTER);

            lbutton = new Button("<");
            rButton = new Button(">");

            Image images[] = new Image[list.size()];
            for (int i = 0; i < list.size(); i++) {
                images[i] = new Image(new FileInputStream(list.get(i)));
            }

            imageView = new ImageView(images[j]);
            imageView.setCursor(Cursor.CLOSED_HAND);

            imageView.setOnMousePressed(circleOnMousePressedEventHandler);

            imageView.setOnMouseReleased(e -> {
                orgReleaseSceneX = e.getSceneX();
                if (orgCliskSceneX > orgReleaseSceneX) {
                    lbutton.fire();
                } else {
                    rButton.fire();
                }
            });

            rButton.setOnAction(e -> {
                j = j + 1;
                if (j == list.size()) {
                    j = 0;
                }
                imageView.setImage(images[j]);

            });
            lbutton.setOnAction(e -> {
                j = j - 1;
                if (j == 0 || j > list.size() + 1 || j == -1) {
                    j = list.size() - 1;
                }
                imageView.setImage(images[j]);

            });

            imageView.setFitHeight(100);
            imageView.setFitWidth(300);

            HBox hBox = new HBox();
            hBox.setSpacing(15);
            hBox.setAlignment(Pos.CENTER);
            // hBox.getChildren().addAll(lbutton, imageView, rButton);
            hBox.getChildren().addAll(imageView);

            root.add(hBox, 1, 1);
            Scene scene = new Scene(root, 800, 300);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    EventHandler<MouseEvent> circleOnMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent t) {
            orgCliskSceneX = t.getSceneX();
        }
    };

    public static void main(String[] args) {
        launch(args);
    }
}