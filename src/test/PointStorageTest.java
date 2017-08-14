package test;

import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import junit.framework.TestCase;
import kdtrees.PointStorage;

public abstract class PointStorageTest extends TestCase {
    protected abstract PointStorage getPointStorageToTest();
    
    PointStorage storage0, storage1;
    
    protected void setUp() throws Exception {
        storage0 = getPointStorageToTest();
        storage1 = getPointStorageToTest();
        
        storage1.insert(new Point2D(0.0, 0.5));
        storage1.insert(new Point2D(0.5, 1.0));
        storage1.insert(new Point2D(0.5, 0.0));
        storage1.insert(new Point2D(1.0, 0.5));
    }
    
    protected void tearDown() throws Exception {
        
    }
    
    public void testIsEmpty() {
        assertEquals(true, storage0.isEmpty());
        assertEquals(false, storage1.isEmpty());
    }
    
    public void testSize() {
        assertEquals(0, storage0.size());
        assertEquals(4, storage1.size());
    }
    
    public void testContains() {
        assertEquals(false, storage0.contains(new Point2D(0.0, 0.0)));
        assertEquals(false, storage1.contains(new Point2D(0.0, 0.0)));
        assertEquals(true, storage1.contains(new Point2D(0.0, 0.5)));
    }
    
    public void testRange() {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        List<Point2D> points = (List<Point2D>) storage0.range(rect);
        assertEquals(0, points.size());
        
        points = (List<Point2D>) storage1.range(rect);
        assertEquals(4, points.size());
        
        rect = new RectHV(0.5, 0.5, 1.1, 1.1);
        points = (List<Point2D>) storage1.range(rect);
        assertEquals(2, points.size());
    }
    
    public void testNearst() {
        Point2D point = new Point2D(0.5, 1.0);
        
        assertEquals(null, storage0.nearest(point));
        assertEquals(point, storage1.nearest(point));
    }
}
