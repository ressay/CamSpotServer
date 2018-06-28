package Network;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by ressay on 28/06/18.
 */
public class FrameStorer
{
    static public void storeFrames(AnomalyPredictor predictor)
    {
        FrameReceiver frameReceiver = new FrameReceiver(new Server());
        frameReceiver.initFrameReceiver((networkFrame, nounouFrame) ->
        {
            ByteArrayInputStream bis = new ByteArrayInputStream(nounouFrame.getArray());
//            System.out.println(bis);
            try
            {
                BufferedImage bImage2 = ImageIO.read(bis);
                networkFrame.getFrame().setIp(networkFrame.getFrame().getIp().substring(1));
                String directory = "imgs/"+networkFrame.getFrame().getIp()+"/";
                new File(directory).mkdirs();
                String file = directory+"output"+nounouFrame.getId()+".jpg";
                ImageIO.write(bImage2, "jpg", new File(file) );
                networkFrame.getFrame().setFrameUrl(file);
                System.out.println(networkFrame.getFrame().getIp());
                networkFrame.getFrame().addFrameToDatabase();
                predictor.detectAnomaly(networkFrame.getFrame());
            } catch (IOException e)
            {
                e.printStackTrace();
            }
            System.out.println("image created");
//                System.out.println("frame received from "+networkFrame.getFrame().getIp());
        });
    }
}
