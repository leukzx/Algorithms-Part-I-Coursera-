import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.LinkedList;

public class KdTree {

    private Node root;
    private int size;

    private static class Node implements Comparable<Node> {
        private Point2D point;      // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int level;


        public Node(Point2D point, int level, RectHV rect) {
            this.point = new Point2D(point.x(), point.y());
            this.level = level;
            this.rect = rect; // new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), rect.ymax());
        }

        public int compareTo(Node that) {
            double thatcoord;
            double thiscoord;
            double otherthatcoord;
            double otherthiscoord;

            if ((that.level & 1) == 0) {
                thatcoord = that.point.x();
                thiscoord = this.point.x();
                otherthatcoord = that.point.y();
                otherthiscoord = this.point.y();
            } else {
                thatcoord = that.point.y();
                thiscoord = this.point.y();
                otherthatcoord = that.point.x();
                otherthiscoord = this.point.x();
            }

            // What should I do if a point has the same x-coordinate
            // as the point in a node when inserting / searching in
            //  a 2d-tree? Go the right subtree as specified.
            if (thiscoord == thatcoord)
                if (otherthiscoord == otherthatcoord) return  0;
                else return 1;
            if (thiscoord < thatcoord) return -1;
            return  1;
        }

        public boolean equals(Object that) {
            if (that == this) return true;
            if (that == null) return false;
            if (that.getClass() != this.getClass()) return false;
            if (this.point.equals(that)) return true;
            return false;
        }

        public void draw() {
            double rad = StdDraw.getPenRadius();
            this.point.draw();

            StdDraw.setPenRadius();
            if ((this.level & 1) == 0) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(this.point.x(), this.rect.ymin(), this.point.x(), this.rect.ymax());
            } else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(this.rect.xmin(), this.point.y(), this.rect.xmax(), this.point.y());
            }
            StdDraw.setPenColor();
            StdDraw.setPenRadius(rad);
        }
    }

    // construct an empty set of points
    public KdTree() {
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return (size == 0);
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException("Argument is null.");
        Node inode = new Node(p, 0, null);
        root = insert(root, inode, null);
    }

    private Node insert(Node node, Node inode, Node pnode) {
        if (node == null) {
            if (pnode != null) {
                inode.level = pnode.level + 1;
                int cmp = inode.compareTo(pnode);
                if ((pnode.level & 1) == 0) {
                    if (cmp < 0)
                        inode.rect = new RectHV(pnode.rect.xmin(), pnode.rect.ymin(), pnode.point.x(), pnode.rect.ymax());
                    else if (cmp > 0)
                        inode.rect = new RectHV(pnode.point.x(), pnode.rect.ymin(), pnode.rect.xmax(), pnode.rect.ymax());
                } else {
                    if (cmp < 0)
                        inode.rect = new RectHV(pnode.rect.xmin(), pnode.rect.ymin(), pnode.rect.xmax(), pnode.point.y());
                    else if (cmp > 0)
                        inode.rect = new RectHV(pnode.rect.xmin(), pnode.point.y(), pnode.rect.xmax(), pnode.rect.ymax());
                }
            }
            else inode.rect = new RectHV(0, 0, 1, 1);
            this.size++;
            return inode;
        }

        int cmp = inode.compareTo(node);

        if (cmp < 0) node.lb = insert(node.lb, inode, node);
        else if (cmp > 0) node.rt = insert(node.rt, inode, node);

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException("Argument is null.");

        Node xnode = new Node(p, 0, new RectHV(0, 0, 1, 1));

        if (this.contains(root, xnode) != null) return true;
        else return false;
    }

    private Node contains(Node node, Node xnode) {
        if (node == null) return null;
        int cmp = xnode.compareTo(node);
        if (cmp < 0) return contains(node.lb, xnode);
        else if (cmp > 0) return contains(node.rt, xnode);
        else return node;
    }
    // draw all points to standard draw
    public void draw() {
        draw(root);
    }

    private void draw(Node node) {
        if (node == null) return;
        draw(node.lb);
        node.draw();
        draw(node.rt);
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new java.lang.NullPointerException("Argument is null.");
        LinkedList<Point2D> points = new LinkedList<Point2D>();
        range(this.root, rect, points);
        return points;
    }

    private void range(Node node, RectHV rect, LinkedList<Point2D> points) {
        if (node == null) return;
        if (rect.intersects(node.rect)) {
            range(node.lb, rect, points);
            range(node.rt, rect, points);
        }
        if (rect.contains(node.point)) points.add(node.point);
        return;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException("Argument is null.");
        Node snode = new Node(p, 0, null);

        if (!isEmpty()) return nearest(root, snode, Double.POSITIVE_INFINITY);
        return null;

    }

    private Point2D nearest(Node node, Node snode, double best) {
        double distance = snode.point.distanceSquaredTo(node.point);
        Point2D nearest = null;
        if (best > distance) {
            nearest = node.point;
            best = distance;
        }

        Point2D cand;
        int cmp = snode.compareTo(node);

        Node nodefirst;
        Node nodelast;
        if (cmp < 0) {
            nodefirst = node.lb;
            nodelast = node.rt;
        } else {
            nodefirst = node.rt;
            nodelast = node.lb;
        }
        if (nodefirst != null && best > nodefirst.rect.distanceSquaredTo(snode.point)) {
            cand = nearest(nodefirst, snode, best);
            if (cand != null) {
                nearest = cand;
                best = snode.point.distanceSquaredTo(nearest);
            }
        }
        if (nodelast != null &&  best > nodelast.rect.distanceSquaredTo(snode.point)) {
            cand = nearest(nodelast, snode, best);
            if (cand != null) {
                nearest = cand;
            }
        }

        return nearest;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
      
    }
}
