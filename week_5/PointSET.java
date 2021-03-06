import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {

    private final SET<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        points = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points)
            point.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();

        Queue<Point2D> queueResult = new Queue<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p))
                queueResult.enqueue(p);
        }
        return queueResult;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (points.isEmpty())
            return null;

        Point2D pointNearest = null;
        double distanceNearest = Double.POSITIVE_INFINITY;

        for (Point2D point : points) {
            double temp = point.distanceSquaredTo(p);
            if (temp < distanceNearest) {
                pointNearest = point;
                distanceNearest = temp;
            }
        }
        return pointNearest;
    }
}
