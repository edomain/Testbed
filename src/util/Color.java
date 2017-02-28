package util;

public class Color {
    public static int white = 0xFFFFFFFF;
    public static int red = 0x00FF0000;
    public static int green = 0x0000FF00;
    public static int blue = 0x000000FF;

    public static int color(float r, float g, float b) {
        return compositeColor(createColor(r), createColor(g),
                createColor(b));
    }

    public static int decomposeAndInterpolate(int color, float intensity) {
        int r = (color>>16) & 255;
        int g = (color>>8) & 255;
        int b = color & 255;

        r = (int)(intensity*r);
        g = (int)(intensity*g);
        b = (int)(intensity*b);
        int color_res = compositeColor(r, g, b);
        return color_res;
    }

    public static int compositeColor(int r, int g, int b) {
        int color = (r << 16) | (g << 8) | b;
        return color;
    }
    public static int createColor(float v)  {
        return (int)(v*255.0f);
    }
    public static int random() {
        int r =  createColor((float)Math.random());
        int g =  createColor((float)Math.random());
        int b =  createColor((float)Math.random());

        return compositeColor(r, g, b);
    }
}