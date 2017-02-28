package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;

/**
 * 3/4/16 6:43 PM.
 */
public class ImageLoader {
    public static final String TAG = "ImageLoader";
    public static Texture loadImage(String fileName) {
        Texture texture = new Texture();
        int[] imageData;
        BufferedImage image = null;
        try {
            image = ImageIO.read(ImageLoader.class.getResourceAsStream("../obj/" + fileName));
            imageData = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
            texture.width = image.getWidth();
            texture.height = image.getHeight();
            texture.data = imageData;
            return texture;
        } catch (IOException ex) {
            System.err.println(TAG + fileName +  " not found");
        }

        return texture;
    }
}
