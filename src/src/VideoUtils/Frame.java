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

    public Frame(String frameUrl){
        this.frameUrl = frameUrl;
        try
        {
            frameImage= new Image(new FileInputStream(this.frameUrl));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    public String getFrameUrl() {
        return frameUrl;
    }

    public Image getFrameImage() {
        return frameImage;
    }
}
