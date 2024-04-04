import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {

    private Set<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        this.pointSet = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if ( p == null ) throw new IllegalArgumentException("Input point is null!");
        this.pointSet.add( p );
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if ( p == null ) throw new IllegalArgumentException("Input point is null!");
        return this.pointSet.contains( p );
    }

    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.05);
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        for (Point2D p : pointSet) {
            p.draw();
        }
        StdDraw.show();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if ( rect == null ) throw new IllegalArgumentException("Input point is null!");
        List<Point2D> result = new ArrayList<>();
        for (Point2D p : pointSet){
            if ( rect.contains(p) ) result.add(p);
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if ( p == null ) throw new IllegalArgumentException("Input point is null!");
        double distance = Double.POSITIVE_INFINITY;
        double currentD;
        Point2D result = null;
        for (Point2D point : pointSet){
            currentD = p.distanceSquaredTo(point);
            if ( currentD < distance) {
                distance = currentD;
                result = point;
            }
        }
        return result;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        PointSET set = new PointSET();
        Point2D p1 = new Point2D(0.654616, 0.365541);
        Point2D p2 = new Point2D(0.132481, 0.846148);
        Point2D p3 = new Point2D(0.654518, 0.123489);
        Point2D p4 = new Point2D(0.541876, 0.574516);
        Point2D p5 = new Point2D(0.941571, 0.764123);

        set.insert(p1);
        set.insert(p2);
        set.insert(p3);
        set.insert(p4);
        set.insert(p5);

        set.draw();

        System.out.println("check if set contains p1 " + set.contains(p1));

        System.out.println("check for a rectangle x: 0.4-0.7 y: 0.4-0.8 " + set.range(new RectHV(0.4, 0.4, 0.7, 0.9)));
    }
}