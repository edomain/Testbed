package screens;

import core.Screen;
import quaint.Point;
import quaint.v2;
import util.Color;
import util.Plotter;

import static java.lang.Math.*;

import java.awt.event.KeyEvent;
import java.util.Arrays;

/**
 * 2/28/16 12:22 PM.
 */
public class LineScreen extends Screen {

    Point[] triangle = new Point[3];
    int numberOfIterations = 3;
    float root3 = (float) sqrt(3) / 2;

    @Override
    public void init() {
        v2 t0[] = {new v2(10, 10), new v2(100, 30), new v2(190, 160)};
//        Plotter.baryTriangle(bitmap, t0, Color.red);
        bitmap.setPixelHeight(5);
        int[] yBuffer = new int[bitmap.width];
        Arrays.fill(yBuffer, Integer.MIN_VALUE);
        rasterize(new v2(20, 34), new v2(744, 400), yBuffer, Color.red );
        rasterize(new v2(120, 434), new v2(444, 400), yBuffer, Color.green);
        rasterize(new v2(330, 463), new v2(594, 200), yBuffer, Color.blue);
        bitmap.resetPixelHeight();
    }

    public void rasterize(v2 p0, v2 p1, int[] yBuffer, int color) {
        if (p0.x > p1.x) {
            v2 temp = p0;
            p0 = p1;
            p1 = temp;
        }

        for (int x = p0.x; x < p1.x; x++) {
            float t = (x - p0.x) / (float) (p1.x - p0.x);
            int y = (int) ((1 - t) * p0.y + t * p1.y);
            if (yBuffer[x] < y) {
                yBuffer[x] = y;
                bitmap.setPixel(x, 0, color);
            }

        }
    }

    @Override
    public void updateAndRender() {
    }

    //TODO(Stephen): This is a crappy koch snowflake, we can do better
    public void drawKoch(int depth, int iterations, v2[] t) {
        if (depth >= iterations) {
            return;
        }
        for (int k = 0; k < iterations; k++) {
            v2[] tt = new v2[3];
            for (int i = 0; i < 3; i++) {
                v2 a = t[i];
                v2 b = t[(i + 1) % 3];
                drawLine(a, b, 0xFF0000);
                v2 vab = v2.vectorBetweenPoints(a, b);
                v2 c = a.add(vab.multiply(1 / 3.0f));
                v2 e = a.add(vab.multiply(2 / 3.0f));
                v2 l = v2.vectorBetweenPoints(c, e);
                l = c.add(l.rotate(60));
                tt[0] = c;
                tt[1] = l;
                tt[2] = e;
                drawKoch(depth + 1, iterations, tt);
            }
        }
    }

    public void drawLine(v2 a, v2 b, int color) {
    }


    @Override
    public void keyPressed(KeyEvent e) {

    }
}



