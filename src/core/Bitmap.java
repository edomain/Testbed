package core;

import java.util.Arrays;

/**
 * 1/2/16
 **/
public class Bitmap {
    //TODO(Stephen): This is a note: for square screen sizes, java has a problem with
    // displaying the bitmap such that it does not clip y. In essence, our y is clipped
    // premarturely if our screen is square. Spent hours trying to debug this
    private int[] components;

    public int width, height;
    private int pixelHeight;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        pixelHeight = 1;
        components = new int[width * height];
    }

    public void clear() {
        clear(0x0);  // clear to black
    }

    public void clear(int clearColor) {
        Arrays.fill(components, clearColor);
    }

    /**
     * Takes Normalized color values between 0.0f and 1.0f
     */
    public void clear(float r, float g, float b) {
        int clearColor = ((int) (r * 255) << 16 |
                (int) (g * 255) << 8 |
                (int) (b * 255.0f));
        clear(clearColor);
    }

    public void setPixel(int x, int y, int colorValue) {
        //TODO(Stephen): Remove this check maybe?
        //TODO(Stephen): Let's implement clipping. We're using a one dimensional array
        // as an image and thus if an object is outside the screen it goes over to the next
        // row rather than clipping. This is an obvious bug. It's not always unwanted
        for (int py = y; py <= (y + pixelHeight); py++) {
            if ((x + py * width) < (width * height)) {
                if (x + py * width > 0) {
                    components[x + py * width] = colorValue;
                }
            }
        }
    }

    public void copyBuffers(int[] dest) {
        // y is up
        for (int a = 0, y = height - 1; y >= 0 && a < height; y--, a++) {
            for (int x = 0; x < width; x++) {
                dest[y * width + x] = components[a * width + x];
            }

        }
    }

    public int getPixel(int x, int y) {
        return components[y * width + x];
    }

    public void setPixelHeight(int height) {
        pixelHeight = height;
    }

    public void resetPixelHeight() {
        pixelHeight = 1;
    }
}
