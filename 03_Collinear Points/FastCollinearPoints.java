import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;


public class FastCollinearPoints {

    private LinkedList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null)
            throw new java.lang.NullPointerException("The argument to the constructor is null.");
        segments = new LinkedList<LineSegment>();

        Point[] parr = Arrays.copyOf(points, points.length);

        for (int k = 0; k < parr.length; k++) {
            Point base = parr[k];

            if (base == null)
                throw new java.lang.NullPointerException("Point in the array is null.");

            if (k > 2) Arrays.sort(parr, 0, k, base.slopeOrder());
            Arrays.sort(parr, k + 1, parr.length, base.slopeOrder());

            double slope = 0.0;

            ArrayList<Point> segList = new ArrayList<Point>();
            segList.add(base);

            for (int i = k + 1; i < parr.length; i++) {
                if (base.compareTo(parr[i]) == 0)
                    throw new java.lang.IllegalArgumentException(
                    "The argument to the constructor contains a repeated point.");

                double newslope = base.slopeTo(parr[i]);
                if (newslope == slope) segList.add(parr[i]);
                if (slope != newslope || i == parr.length - 1) {
                    if (segList.size() > 3) {
                        if (Arrays.binarySearch(parr, 0, k, segList.get(1), base.slopeOrder()) < 0) {
                            segList.sort(null);
                            Point first = segList.get(0);
                            Point last = segList.get(segList.size()-1);
                            segments.add(new LineSegment(first, last));
                        }
                    }
                    segList = new ArrayList<Point>();
                    segList.add(base);
                    slope = newslope;
                    segList.add(parr[i]);
                }
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        LineSegment[] segs = new LineSegment[segments.size()];
        segments.toArray(segs);
        return segs;
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
        StdOut.println(collinear.numberOfSegments());

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
