package quaint;

/**
 * 2/28/16 12:23 PM.
 */
public class v2 implements Comparable {
    static double cos60 = Math.cos(Math.PI / 3);
    static double sin60 = Math.sin(Math.PI / 3);

    public int x;
    public int y;
    float length;
    v2 unitV;

    public v2(int x, int y) {
        this.x = x;
        this.y = y;
        length = length();
    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y);
    }

    public float getNormal() {
        return (1 / length);
    }

    public void initUnitVector() {
        int nx = Math.round(getNormal() * x);
        int ny = Math.round(getNormal() * y);
        unitV = new v2(nx, ny);
    }

    public v2 getUnitVector() {
        return unitV;
    }

    public v2 add(v2 a) {
        return new v2(a.x + x, a.y + y);
    }

    public void add(int x, int y) {
        this.x += x;
        this.y += y;
    }

    public v2 multiply(float a) {
        return new v2((int) (a * x), (int) (a * y));
    }

    public v2 rotate(float angle) {
        float rad = (float) Math.toRadians(angle);
        float[] vec = new float[]{x, y};
        float[] rotMatrix = new float[4];
        rotMatrix[0] = (float) Math.cos(rad);
        rotMatrix[1] = -1 * (float) Math.sin(rad);
        rotMatrix[2] = (float) Math.sin(rad);
        rotMatrix[3] = rotMatrix[0];

        float[] res = new float[vec.length];
        for (int i = 0; i < rotMatrix.length; i++) {
            for (int j = 0; j < vec.length; j++) {
                float a = rotMatrix[i++] * vec[j++];
                float b = rotMatrix[i] * vec[j];
                res[i / 2] = (a + b);
            }
        }

        return new v2((int) res[0],
                (int) res[1]);
    }

    public v2 multiply(double a, double b) {
        return new v2((int) (a * x), (int) (b * y));
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }

    public static v2 vectorBetweenPoints(v2 a, v2 b) {
        return new v2(b.x - a.x, b.y - a.y);
    }

    // returns the angle between two vectors
    public static float dot(v2 a, v2 b) {
        float algebraicDot = a.x * b.x + a.y * b.y;
        float lenProduct = a.length() * b.length();
        float result = algebraicDot / lenProduct;
        return  result;
    }

    public void normalize() {
        x = (int)(x/length);
        y = (int)(y/length);
    }

    public void mul(float s) {
        x *= s; y *= s;
    }

    public v2 mull(float s) {
       return new v2((int)(x*s), (int)(y*s));
    }

    @Override
    public int compareTo(Object o) {
        v2 t = (v2)o;
        if(t.y > y) {
            return -1;
        } else if(t.y < y) {
            return 1;
        } else {
            return 0;
        }
    }
}
