package kdtrees;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET implements PointStorage {
    
    private Set<Point2D> points;
    public PointSET() {
        points = new TreeSet<>();
    }
    public boolean isEmpty() {
        return points.isEmpty();
    }
    public int size() {
        return points.size();
    }
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }
    public void draw() {
        for (Point2D point : points)
            point.draw();
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        
        List<Point2D> range = new LinkedList<>();
        
        for (Point2D point : points)
            if (rect.contains(point))
                range.add(point);
        
        return range;
    }
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        
        Point2D nearest = null;
        double distance = Double.MAX_VALUE;
        for(Point2D point : points) {
            double d = p.distanceTo(point);
            if (d < distance) {
                nearest = point;
                distance = d;
            }
        }
        
        return nearest;
    }
 }
