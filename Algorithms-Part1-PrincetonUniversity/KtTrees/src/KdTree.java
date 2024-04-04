import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.List;
import java.util.ArrayList;

public class KdTree {
    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node left;
        private Node right;

        private Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        this.root = null;
        this.size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if ( p == null ) throw new IllegalArgumentException("Input point is null!");
        if (root == null)
            root = new Node(p, new RectHV(p.x(), 0, p.x(), 1));
        else
            insertPoint(p);
        size++;
    }

    private void insertPoint(Point2D p){
        boolean isVertical = true;
        boolean isLeft = true;

        Node prev = this.root;
        Node current = this.root;
        RectHV division = new RectHV(0, 0, 1, 1 );
        while (current != null){
            if (current.p.equals(p)) return;
            prev = current;

            if (isVertical) isLeft = p.x() < current.p.x();
            else isLeft = p.y() < current.p.y();

            if (isLeft) current = current.left;
            else current = current.right;

            division = updateDiv( division, isVertical, isLeft, prev);

            isVertical = !isVertical;
        }

        double threshold = isVertical? prev.p.y() : prev.p.x();
        RectHV rect = null;
        if (isVertical){
            if (isLeft) rect = new RectHV(p.x(), Math.max(0, division.ymin()), p.x(), threshold);
            else rect = new RectHV(p.x(), threshold, p.x(), Math.min(1, division.ymax()));
        } else {
            if (isLeft) rect = new RectHV(Math.max(0, division.xmin()), p.y(), threshold, p.y());
            else rect = new RectHV(threshold, p.y(), Math.min(1, division.xmax()), p.y());
        }
        Node node = new Node(p, rect);
        if (isLeft) prev.left = node;
        else prev.right = node;
    }

    private RectHV updateDiv(RectHV div, boolean isVertical, boolean isLeft, Node current) {
        RectHV result = null;
        if (isVertical){
            if (isLeft) result = new RectHV(div.xmin(), div.ymin(), current.p.x(), div.ymax());
            else result = new RectHV(current.p.x(), div.ymin(), div.xmax(), div.ymax());
        } else {
            if (isLeft) result = new RectHV(div.xmin(), div.ymin(), div.xmax(), current.p.y());
            else result = new RectHV(div.xmin(), current.p.y(), div.xmax(), div.ymax());
        }
        return result;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if ( p == null ) throw new IllegalArgumentException("Input point is null!");
        Node current = root;
        boolean isVertical = true;

        while ( current != null ){
            if ( current.p.equals(p) ) return true;
            if (isVertical) {
                if ( p.x() < current.p.x() ) current = current.left;
                else current = current.right;
            } else {
                if ( p.y() < current.p.y() ) current = current.left;
                else current = current.right;
            }
            isVertical = !isVertical;
        }
        return false;
    }

    // draw all points to standard draw
    public void draw() {
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        draw(root, true);
        StdDraw.show();
    }

    private void draw( Node node, boolean isVertical){
        if ( node == null ) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.02);
        node.p.draw();
        draw( node.left, !isVertical);
        draw( node.right, !isVertical);
        StdDraw.setPenRadius(0.005);
        StdDraw.setPenColor(isVertical? StdDraw.RED : StdDraw.BLUE);
        node.rect.draw();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if ( rect == null ) throw new IllegalArgumentException("Input point is null!");
        List<Point2D> result = new ArrayList<>();
        rangeX(rect, root, result);;
        return result;
    }

    private void rangeX(RectHV rect, Node node, List<Point2D> points){
        if ( node == null ) return;
        if (rect.contains(node.p)) points.add(node.p);
        if ( rect.xmax() < node.p.x() ){
            rangeY( rect, node.left, points );
        } else if ( rect.xmin() > node.p.x() ){
            rangeY( rect, node.right, points );
        } else {
            rangeY( rect, node.left, points );
            rangeY( rect, node.right, points );
        }
    }

    private void rangeY(RectHV rect, Node node, List<Point2D> points){
        if ( node == null ) return;
        if (rect.contains(node.p)) points.add(node.p);
        if ( rect.ymax() < node.p.y() ){
            rangeX( rect, node.left, points );
        } else if ( rect.ymin() > node.p.y() ){
            rangeX( rect, node.right, points );
        } else {
            rangeX( rect, node.left, points );
            rangeX( rect, node.right, points );
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if ( p == null ) throw new IllegalArgumentException("Input point is null!");
        if ( size == 0 ) return null;
        NearestP result = nearestX(p, root, new NearestP(root.p, root.p.distanceSquaredTo(p)));
        return result.p;
    }

    private static class NearestP {
        Point2D p;
        double distance;

        NearestP(Point2D p, double distance) {
            this.p = p;
            this.distance = distance;
        }
    }

    private NearestP nearestX(Point2D p, Node node, NearestP targetP){
        if ( node == null ) return targetP;
        double dist = node.p.distanceSquaredTo( p );
        if ( dist < targetP.distance )
            targetP = new NearestP( node.p, dist );
        if ( p.x() < node.p.x() ){
            targetP = nearestY( p, node.left, targetP);
            if ( node.rect.distanceSquaredTo(targetP.p) > targetP.distance ) return targetP;
            targetP = nearestY( p, node.right, targetP);
        } else {
            targetP = nearestY( p, node.right, targetP);
            if ( node.rect.distanceSquaredTo(targetP.p) > targetP.distance ) return targetP;
            targetP = nearestY( p, node.left, targetP);
        }
        return targetP;
    }

    private NearestP nearestY(Point2D p, Node node, NearestP targetP){
        if ( node == null ) return targetP;
        double dist = node.p.distanceSquaredTo( p );
        if ( dist < targetP.distance )
            targetP = new NearestP( node.p, dist );
        if ( p.y() < node.p.y() ){
            targetP = nearestX( p, node.left, targetP);
            if ( node.rect.distanceSquaredTo(targetP.p) > targetP.distance ) return targetP;
            targetP = nearestX( p, node.right, targetP);
        } else {
            targetP = nearestX( p, node.right, targetP);
            if ( node.rect.distanceSquaredTo(targetP.p) > targetP.distance ) return targetP;
            targetP = nearestX( p, node.left, targetP);
        }
        return targetP;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

        KdTree set = new KdTree();
        set.insert(new Point2D(0.372, 0.497));
        set.insert(new Point2D(0.564, 0.413));
        set.insert(new Point2D(0.226, 0.577));
        set.insert(new Point2D(0.144, 0.179));
        set.insert(new Point2D(0.083, 0.510));
        set.insert(new Point2D(0.320, 0.708));
        set.insert(new Point2D(0.417, 0.362));
        set.insert(new Point2D(0.862, 0.825));
        set.insert(new Point2D(0.785, 0.725));
        set.insert(new Point2D(0.499, 0.208));

        System.out.println("check nearest point of p(should be)" + set.nearest(new Point2D(0.785, 0.429)) +
                " with a distance of " + set.nearest(new Point2D(0.785, 0.429)).distanceSquaredTo(new Point2D(0.785, 0.429)));
        //set.draw();

        System.out.println("check if set contains 0.372, 0.497: " + set.contains(new Point2D(0.372, 0.497)));

        System.out.println("check for a rectangle x: 0.4-0.7 y: 0.4-0.8 " + set.range(new RectHV(0.4, 0.4, 0.7, 0.9)));

        KdTree set2 = new KdTree();
        set2.insert(new Point2D(0.25, 1.0));
        set2.insert(new Point2D(0.5, 0.25));
        set2.insert(new Point2D(0.25, 1.0));
        set2.insert(new Point2D(1.0, 1.0));
        set2.insert(new Point2D(0.0, 0.75));
        set2.insert(new Point2D(0.75, 0.5));
        set2.insert(new Point2D(0.5, 1.0));
        set2.insert(new Point2D(1.0, 0.0));
        set2.insert(new Point2D(0.25, 0.0));
        set2.insert(new Point2D(0.75, 1.0));
        set2.draw();
        System.out.println("check for a rectangle [0.125, 0.375] x [0.875, 1.0] ((0.25, 1.0))" + set2.range(new RectHV(0.125, 0.875, 0.375, 1)));
    }
}
