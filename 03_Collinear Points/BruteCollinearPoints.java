import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment[] segs;
    public BruteCollinearPoints(Point[] points) { // finds all line segments containing 4 points
        if (points == null)
            throw new java.lang.NullPointerException("The argument to the constructor is null.");
        segs = new LineSegment[0];

        for (Point first : points) {
            if (first == null)
                throw new java.lang.NullPointerException("Point in the array is null.");
            for (Point second : points) {
                if (second == first) continue;
                if (second.compareTo(first) == 0)
                    throw new java.lang.IllegalArgumentException(
                    "The argument to the constructor contains a repeated point.");
                if (second.compareTo(first) < 0) continue;
                double slope = first.slopeTo(second);
                for (Point third : points) {
                    if (third == second || third == first) continue;
                    if (third.compareTo(second) == 0 || third.compareTo(first) == 0)
                        throw new java.lang.IllegalArgumentException(
                        "The argument to the constructor contains a repeated point.");
                    if (slope != first.slopeTo(third)) continue;
                    if (third.compareTo(second) < 0) continue;
                    for (Point fourth : points) {
                        if (fourth == third || fourth == second
                                || fourth == first) continue;
                        if (fourth.compareTo(third) == 0
                                || fourth.compareTo(second) == 0
                                || fourth.compareTo(first) == 0)
                            throw new java.lang.IllegalArgumentException(
                            "The argument to the constructor contains a repeated point.");
                        if (slope != first.slopeTo(fourth)) continue;
                        if (fourth.compareTo(third) < 0) continue;

                        LineSegment[] copy = new LineSegment[segs.length + 1];
                        for (int i = 0; i < segs.length; i++) copy[i] = segs[i];
                        copy[segs.length] = new LineSegment(first, fourth);
                        segs = copy;
                    }
                }
            }
        }
    }
    public int numberOfSegments() { // the number of line segments
        return segs.length;
    }
    public LineSegment[] segments() { // the line segments
        return Arrays.copyOf(segs, segs.length);
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
        StdOut.println(collinear.numberOfSegments());

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
