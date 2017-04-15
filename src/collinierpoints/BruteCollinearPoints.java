package collinierpoints;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
    private List<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        validatePoints(points);
        
        segments = new ArrayList<>();
        Point[] temp = points.clone();
        
        Arrays.sort(temp);
        searchForSegments(temp);
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[numberOfSegments()]);
    }

    private void validatePoints(Point[] points) {
        if (points == null)
            throw new NullPointerException();

        for (int i = 0; i < points.length; i++) {
            
            if (points[i] == null)
                throw new NullPointerException();
            
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null)
                    throw new NullPointerException();
                
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }
    }

    private void searchForSegments(Point[] points) {
        Point[] temp = new Point[4];
        
        for (int i = 0; i < points.length; i++) {
            temp[0] = points[i];
            for (int j = i + 1; j < points.length; j++) {
                temp[1] = points[j];
                for (int k = j + 1; k < points.length; k++) {
                    temp[2] = points[k];
                    for (int l = k + 1; l < points.length; l++) {
                        temp[3] = points[l];
                        checkCollinearPoints(temp);
                    }
                }
            }
        }
    }

    private void checkCollinearPoints(Point[] points) {
        double slope = points[0].slopeTo(points[1]);
        if (Double.compare(slope, points[0].slopeTo(points[2])) == 0 && 
            Double.compare(slope, points[0].slopeTo(points[3])) == 0) {
            segments.add(new LineSegment(points[0], points[3]));
        }
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
