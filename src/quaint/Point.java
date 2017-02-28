package quaint;

import java.util.Comparator;

/**
 * 2/28/16 10:43 PM.
 */
public class Point implements Comparable {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Object o) {
        Point p2 = (Point)o;
        if(this.y < p2.y) {
            return -1;
        } else if(this.y > p2.y) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "x: " + x + " y: " + y;
    }
}
