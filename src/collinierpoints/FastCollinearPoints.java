package collinierpoints;



import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
    // Coursera doesn`t allow to make hierarchy of classes and to add necessary methods :(
    // Thats why there are a lot of duplications and some tricky things
    private class ComparableLineSegment extends LineSegment implements Comparable<ComparableLineSegment>  {

        private final Point p, q;
        
        public ComparableLineSegment(Point p, Point q) {
            super(p, q);
            this.p = p;
            this.q = q;
        }

        @Override
        public int compareTo(ComparableLineSegment o) {
            int ret = 0;
            
            if (this == o)
                return ret;
            
            ret = p.compareTo(o.p);
            if (ret == 0)
                ret = q.compareTo(o.q);
            
            return ret;
        }
        
    }
    
    private Set<LineSegment> segments;
    
    public FastCollinearPoints(Point[] points) {
        validatePoints(points);
        
        segments = new TreeSet<>();
        searchForSegments(points);
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
        
        for (int i = 0; i < points.length; i++) {
            Point[] temp = points.clone();
            Arrays.sort(temp, points[i].slopeOrder());
            
            List<Point> segment = new LinkedList<>();
            double slope = points[i].slopeTo(temp[0]);
            segment.add(points[i]);
            segment.add(temp[0]);
            for (int j = 1; j < temp.length; j++) {
                double tempSlope = points[i].slopeTo(temp[j]);
                if (Double.compare(slope, tempSlope) == 0)
                    segment.add(temp[j]);
                else {
                    if (segment.size() >= 4)
                        segments.add(new ComparableLineSegment(Collections.min(segment), Collections.max(segment)));
                    
                    slope = tempSlope;
                    segment.clear();
                    segment.add(points[i]);
                    segment.add(temp[j]);
                }
            }
            
            if (segment.size() >= 4)
                segments.add(new ComparableLineSegment(Collections.min(segment), Collections.max(segment)));
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
 }