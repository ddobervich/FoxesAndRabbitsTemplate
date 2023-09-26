package Graph;

public class Point implements Comparable<Point> {
    public Float x, y;

    Point(Float _x, Float _y) {
        x = _x;
        y = _y;
    }

    public int compareTo(Point o) {
        return ((x.compareTo(o.x) == 0) && (y.compareTo(o.y) == 0))?0:1;
    }
}
