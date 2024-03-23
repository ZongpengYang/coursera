import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] foundSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points){
        if ( points == null ) throw new IllegalArgumentException("Input is null!!");
        int len = points.length;
        List<LineSegment> segmentsHolder = new ArrayList<>();
        for ( Point p : points ){
            if ( p == null ) throw new IllegalArgumentException("Input contains null point!");
        }

        Point[] pointsCopy = new Point[len];
        arrayCopy( points, pointsCopy);
        Arrays.sort(pointsCopy);
        checkDuplicate(pointsCopy);

        Point[] sortedArr = new Point[len];
        int count = 0;

        for (int i = 0; i < len; i++){
            arrayCopy(pointsCopy, sortedArr);
            Point p = pointsCopy[i];
            Arrays.sort( sortedArr, p.slopeOrder());
            double prevSlope = p.slopeTo(sortedArr[1]);
            if ( prevSlope == Double.NEGATIVE_INFINITY )
                throw new IllegalArgumentException("Duplicate point found!");
            int start = 1;
            for ( int j = 2; j < len; j++){
                if ( p.slopeTo(sortedArr[j]) != prevSlope){
                    if ( j - start > 2 && pointsCopy[i].compareTo(sortedArr[start]) < 0)
                        segmentsHolder.add(new LineSegment( pointsCopy[i], sortedArr[j-1] ));
                    start = j;
                    prevSlope = p.slopeTo(sortedArr[j]);
                }
            }
            // edge case when collinear is sorted to the end of array
            if ( len - start > 2 && pointsCopy[i].compareTo(sortedArr[start]) < 0 )
                segmentsHolder.add(new LineSegment( pointsCopy[i], sortedArr[len-1] ));
        }

        foundSegments = segmentsHolder.toArray(new LineSegment[]{});
    }

    private void checkDuplicate (Point[] sortedPoints){
        for (int i = 1; i < sortedPoints.length; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i - 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    private void arrayCopy( Point[] points, Point[] target){
        for (int i = 0; i < points.length; i++){
            target[i] = points[i];
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return foundSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return foundSegments;
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
