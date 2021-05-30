import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

    public FastCollinearPoints(Point[] points) {
        if (points == null || hasNull(points) || hasRepeated(points))
            throw new IllegalArgumentException();

        int n = points.length;
        Point[] pointsCopy = Arrays.copyOf(points, n);

        for (int i = 0; i < n; i++) {
            Point p = points[i];
            Arrays.sort(pointsCopy, p.slopeOrder());

            int stop = 0;
            while (stop < n) {
                int repeated = 0;
                double currentSlope = p.slopeTo(pointsCopy[stop]);
                while (stop < n && currentSlope == p.slopeTo(pointsCopy[stop])) {
                    repeated++;
                    stop++;
                }

                if (repeated >= 3) {
                    Point[] pointsCollinear = new Point[repeated + 1];
                    int from = stop - repeated;
                    for (int to = 0; to < repeated; to++) {
                        pointsCollinear[to] = pointsCopy[from];
                        from++;
                    }
                    pointsCollinear[repeated] = p;

                    Point pointHead = pointsCollinear[0];
                    Point pointTail = pointsCollinear[0];
                    for (int k = 0; k < pointsCollinear.length; k++) {
                        if (pointsCollinear[k].compareTo(pointHead) < 0)
                            pointHead = pointsCollinear[k];
                        if (pointsCollinear[k].compareTo(pointTail) > 0)
                            pointTail = pointsCollinear[k];
                    }

                    if (p.compareTo(pointHead) == 0)
                        lineSegments.add(new LineSegment(pointHead, pointTail));
                }
            }
        }
    }

    public int numberOfSegments() {
        return lineSegments.size();
    }

    public LineSegment[] segments() {
        int n = lineSegments.size();
        return lineSegments.toArray(new LineSegment[n]);
    }

    private static boolean hasNull(Point[] points) {
        for (Point x : points)
            if (x == null) return true;
        return false;
    }

    private static boolean hasRepeated(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
