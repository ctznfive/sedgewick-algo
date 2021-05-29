import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null || hasNull(points) || hasRepeated(points))
            throw new IllegalArgumentException();

        /* Brute force */
        for (int i = 0; i < points.length; i++)
        {
            for (int j = i + 1; j < points.length; j++)
            {
                double slopePQ = points[i].slopeTo(points[j]);
                for (int k = j + 1; k < points.length; k++)
                {
                    double slopeQR = points[j].slopeTo(points[k]);
                    if (slopePQ == slopeQR)
                    {
                        for (int m = k + 1; m < points.length; m++)
                        {
                            double slopeRS = points[k].slopeTo(points[m]);
                            if (slopeQR == slopeRS)
                            {
                                Point[] pointsForSorting = { points[i],
                                                             points[j],
                                                             points[k],
                                                             points[m] };
                                Arrays.sort(pointsForSorting);
                                lineSegments.add(new LineSegment(pointsForSorting[0],
                                                                 pointsForSorting[3]));
                            }
                        }
                    }
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}