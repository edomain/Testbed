package quaint;

import java.awt.font.NumericShaper;

/**
 * 2/28/16 8:46 PM.
 */
public class v3 implements Comparable {
    public float x;
    public float y;
    public float z;

    @Override
    public int compareTo(Object o) {
        v3 t = (v3)o;
        if(t.y > y) {
            return -1;
        } else if(t.y < y) {
            return 1;
        } else {
            return 0;
        }
    }

    public v3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public v3 add(v3 v) {
        return new v3(v.x +x, v.y+y, v.z+z);
    }

    public static v3 vectorBetweenPoints(v3 a, v3 b) {
        return new v3(b.x - a.x, b.y - a.y, b.z - a.z);
    }

    public float length() {
        return (float) (Math.sqrt(x * x + y * y+ z * z));
    }

    public void mul(float s) {
        x = x*s;
        y = y*s;
        z = z*s;
    }

    public v3 mull(float s) {
        return new v3(s*x, s*y, s*z);
    }

    public v3 normalv3() {
        return mull(1/length());
    }
    public void normalize() {
        mul(1 / length());
        //TODO(Stephen) : this is a note
        // if we try to normalize this vector as presented below, we
        // run into a situation where the length of the vector is changed
        // since when we do the firs divide, we modify x which modifies the
        // length since the length is based on the value of x, y and z
//        x = x/length();
//        y = y/length();
//        z = z/length();
    }

    public static v3 cross(v3 a, v3 b) {
        float x = a.y * b.z - b.y * a.z;
        float y = (a.x * b.z - b.x * a.z);
        float z = a.x * b.y - b.x * a.y;
        v3 result = new v3(x, y, z);
        return result;
    }

    public static float dot(v3 a, v3 b) {
        float alg = (float) (a.x * b.x + a.y * b.y + a.z * b.z);
        return alg;
    }

    public float[] getRepresentation() {
        return new float[]{x, y, z};
    }

    public float[] homogenousRep() {
        return new float[]{x, y, z, 1};
    }

    public v3 getPos(float[] pos) {
        return new v3(pos[0], pos[1], pos[2]);
    }
    public void setPos(float[] pos) {
        if(pos.length > 3) {
            x = pos[0] / pos[3];
            y = pos[1] / pos[3];
            z = pos[2] / pos[3];
        } else {
            x = pos[0];
            y = pos[1];
            z = pos[2];
        }
    }

}
