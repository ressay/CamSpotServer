package VideoUtils;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by ressay on 22/06/18.
 */
public class Frame
{
    String frameUrl;
    Image frameImage;

    public Frame(String frameUrl) throws FileNotFoundException {
        this.frameUrl = frameUrl;
        frameImage= new Image(new FileInputStream(this.frameUrl));
    }

    public String getFrameUrl() {
        return frameUrl;
    }

    public Image getFrameImage() {
        return frameImage;
    }
}
