package screens;

import core.Screen;
import quaint.v2;
import quaint.v3;
import util.Color;
import util.Matrix3x3;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Comparator;

/**
 * 3/8/16 6:13 PM.
 */
public class TransformsScreen extends Screen {

    float[] pointMatrix;
    float[] pointMatrix1;
    float[] pointMatrix2;
    float[][] resultantMatrix;
    v3 tri[];

    @Override
    public void init() {

        tri = new v3[]{new v3(375, 390, -1), new v3(425, 390, 0), new v3(400, 310, 0)};
        drawTriangle(tri);
        pointMatrix = tri[0].getRepresentation();
        pointMatrix1 = tri[1].getRepresentation();
        pointMatrix2 = tri[2].getRepresentation();

        Matrix3x3.setPoints(pointMatrix, pointMatrix1, pointMatrix2, 1);
    }

    @Override
    public void updateAndRender() {
        bitmap.clear();
//        resultantMatrix = Matrix3x3.doMultiply();
//        tri[0].setPos(resultantMatrix[0]);
//        tri[1].setPos(resultantMatrix[1]);
//        tri[2].setPos(resultantMatrix[2]);
//        drawTriangle(tri);
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    private void drawTriangle(v3[] vertices) {
        Arrays.sort(vertices, new ysort<v3>());
        int yMin = (int) vertices[0].y;
        int yMax = (int) vertices[2].y;
        Arrays.sort(vertices, new xsort<v3>());
        int xMin = (int) vertices[0].x;
        int xMax = (int) vertices[2].x;

        v3 v1 = v3.vectorBetweenPoints(vertices[0], vertices[1]);
        v3 v2 = v3.vectorBetweenPoints(vertices[0], vertices[2]);

        float dot12 = v3.dot(v1, v2);
        float dot11 = v3.dot(v1, v1);
        float dot22 = v3.dot(v2, v2);

        float denominator = 1.0f / (dot22 * dot11 - dot12 * dot12);

        v3 k0 = v3.vectorBetweenPoints(v2.mull(dot12), v1.mull(dot22));
        v3 k1 = v3.vectorBetweenPoints(v1.mull(dot12), v2.mull(dot11));
        k0.mul(denominator);
        k1.mul(denominator);

        float u, v;
        for (int y = yMin; y <= yMax; y++) {
            for (int x = xMin; x <= xMax; x++) {
                v3 P = new v3(x, y, 0);

                v3 w = v3.vectorBetweenPoints(vertices[0], P);
                v = v3.dot(w, k0);
                u = v3.dot(w, k1);
                float q = 1 - u - v;

                if (u >= 0 && v >= 0 && u + v < 1) {
                    int color = Color.color(q, v, u);
                    bitmap.setPixel(x, y, color);
                }
            }
        }
    }


    static class xsort<T> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            if (o1 instanceof v3) {
                v3 a = (v3) o1;
                v3 b = (v3) o2;
                if (a.x == b.x) {
                    return 0;
                } else if (a.x > b.x) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        }
    }


    static class ysort<T> implements Comparator<T> {
        @Override
        public int compare(T o1, T o2) {
            if (o1 instanceof v3) {
                v3 a = (v3) o1;
                v3 b = (v3) o2;
                if (a.y == b.y) {
                    return 0;
                } else if (a.y > b.y) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return 0;
        }
    }
}
