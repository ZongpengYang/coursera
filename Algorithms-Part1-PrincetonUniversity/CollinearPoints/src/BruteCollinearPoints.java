import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.NoSuchElementException;

public class BruteCollinearPoints {

    private final LineSegment[] foundSegments;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points){
        if ( points == null ) throw new IllegalArgumentException("Input is null!!");
        int len = points.length;
        LineSegment[] segmentsHolder = new LineSegment[ len ];
        for ( Point p : points ){
            if ( p == null ) throw new IllegalArgumentException("Input contains null point!");
        }
        Point[] pointsCopy = new Point[len];
        arrayCopy( points, pointsCopy );

        Arrays.sort(pointsCopy);
        checkDuplicate(pointsCopy);
        int count = 0;
        // According to requirement, only exact 4 collinear points will be supplied.
        // Hence, 2 points cannot be in 2 different line segment at both time
        // only the most outer loop is needed
        boolean found = false;
        for ( int i = 0; i < len; i++ ){
            for ( int j = i + 1; j < len; j++ ){
                if ( found ) found = false;
                for ( int k = j + 1; k < len; k++ ){
                    if ( found ) break;
                    for ( int g = k + 1; g < len; g++ ){
                        double slopeij = pointsCopy[i].slopeTo(pointsCopy[j]);
                        double slopeik = pointsCopy[i].slopeTo(pointsCopy[k]);
                        double slopeig = pointsCopy[i].slopeTo(pointsCopy[g]);
                        if ( slopeij == slopeik && slopeik == slopeig ) {
                            segmentsHolder[count++] = new LineSegment( pointsCopy[i], pointsCopy[g] );
                            found = true;
                            break;
                        }
                    }
                }
            }
        }
        foundSegments = new LineSegment[count];
        for ( int i = 0; i < count; i++ ){
            foundSegments[i] = segmentsHolder[i];
        }
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
    public int numberOfSegments(){
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
