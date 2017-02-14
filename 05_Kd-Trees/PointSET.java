import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import java.util.LinkedList;
import java.util.TreeSet;

public class PointSET {

    private TreeSet<Point2D> points;

   
    // construct an empty set of points
    public PointSET() {
        points = new TreeSet<Point2D>();
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
            throw new java.lang.NullPointerException("Argument is null.");
        points.add(p);
    }
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) 
            throw new java.lang.NullPointerException("Argument is null.");
        return points.contains(p);
    }
    
    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) 
            point.draw();
    }
    
    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) 
            throw new java.lang.NullPointerException("Argument is null.");

        LinkedList<Point2D> rpoints = new LinkedList<Point2D>();
        for (Point2D point : points) {
            if (rect.contains(point)) rpoints.add(point);
        }
        return rpoints;
    }
    
    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) 
            throw new java.lang.NullPointerException("Argument is null.");
        
        if (isEmpty()) return null;
        
        double dist = Double.POSITIVE_INFINITY;
        double ndist =  Double.POSITIVE_INFINITY;
        Point2D npoint = new Point2D(0, 0);

        for (Point2D point : points) {
            dist = p.distanceSquaredTo(point);
            if (dist < ndist) {
                ndist = dist;
                npoint = new Point2D(point.x(), point.y());
            }
        }
        return npoint;
    }
    
    // unit testing of the methods (optional)
    // public static void main(String[] args){

    // }
}