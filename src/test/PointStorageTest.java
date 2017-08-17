package test;

import java.util.List;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import junit.framework.TestCase;
import kdtrees.PointStorage;

public abstract class PointStorageTest extends TestCase {
    protected abstract PointStorage getPointStorageToTest();
    
    PointStorage storage0, storage1, storage2;
    
    protected void setUp() throws Exception {
        storage0 = getPointStorageToTest();
        storage1 = getPointStorageToTest();
        storage2 = getPointStorageToTest();
        
        storage1.insert(new Point2D(0.0, 0.5));
        storage1.insert(new Point2D(0.5, 1.0));
        storage1.insert(new Point2D(0.5, 0.0));
        storage1.insert(new Point2D(1.0, 0.5));
        
        storage2.insert(new Point2D(0.206107, 0.095492));
        storage2.insert(new Point2D(0.975528, 0.654508));
        storage2.insert(new Point2D(0.024472, 0.345492));
        storage2.insert(new Point2D(0.793893, 0.095492));
        storage2.insert(new Point2D(0.793893, 0.904508));
        storage2.insert(new Point2D(0.975528, 0.345492));
        storage2.insert(new Point2D(0.206107, 0.904508));
        storage2.insert(new Point2D(0.500000, 0.000000));
        storage2.insert(new Point2D(0.024472, 0.654508));
        storage2.insert(new Point2D(0.500000, 1.000000));
    }
    
    protected void tearDown() throws Exception {
        
    }
    
    public void testIsEmpty() {
        assertEquals(true, storage0.isEmpty());
        assertEquals(false, storage1.isEmpty());
        assertEquals(false, storage2.isEmpty());
    }
    
    public void testSize() {
        assertEquals(0, storage0.size());
        assertEquals(4, storage1.size());
        assertEquals(10, storage2.size());
    }
    
    public void testContains() {
        assertEquals(false, storage0.contains(new Point2D(0.0, 0.0)));
        assertEquals(false, storage1.contains(new Point2D(0.0, 0.0)));
        assertEquals(true, storage1.contains(new Point2D(0.0, 0.5)));
        assertEquals(true, storage2.contains(new Point2D(0.500000, 1.000000)));
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
        assertEquals(point, storage2.nearest(point));
    }
    
    public void testTest0() {
        PointStorage storage = getPointStorageToTest();
        Point2D p = new Point2D(0.75, 0.5625);
        
        storage.insert(new Point2D(0.8125, 0.25));
        storage.insert(p);
        assertEquals(p, storage.nearest(new Point2D(0, 0.75)));
        
        storage.insert(new Point2D(0.625, 0.0625));
        assertEquals(3, storage.size());
        assertEquals(p, storage.nearest(new Point2D(0.25, 0.4375)));
        
        p = new Point2D(0.4375, 0.3125);
        storage.insert(p);
        assertEquals(p, storage.nearest(new Point2D(0.125, 0.9375)));
    }
    
    public void testTest2() {
        PointStorage circle = createCircle100();
        
        assertEquals(100, circle.size());
        assertEquals(new Point2D(0.135516, 0.157726), circle.nearest(new Point2D(0.291015625, 0.294921875)));
        assertEquals(new Point2D(0.135516, 0.157726), circle.nearest(new Point2D(0.3046875, 0.30859375)));
        assertEquals(new Point2D(0.842274, 0.864484), circle.nearest(new Point2D(0.947265625, 1.0)));
        assertEquals(new Point2D(0.975528, 0.654508), circle.nearest(new Point2D(0.7734375, 0.583984375)));
    }
    
    private PointStorage createCircle100() {
        PointStorage circle = getPointStorageToTest();
        
        circle.insert(new Point2D(0.740877, 0.938153));
        circle.insert(new Point2D(0.181288, 0.114743));
        circle.insert(new Point2D(0.157726, 0.864484));
        circle.insert(new Point2D(0.684062, 0.035112));
        circle.insert(new Point2D(0.975528, 0.345492));
        circle.insert(new Point2D(0.684062, 0.964888));
        circle.insert(new Point2D(0.938153, 0.259123));
        circle.insert(new Point2D(0.315938, 0.964888));
        circle.insert(new Point2D(0.793893, 0.904508));
        circle.insert(new Point2D(0.938153, 0.740877));
        circle.insert(new Point2D(0.047586, 0.287110));
        circle.insert(new Point2D(0.952414, 0.712890));
        circle.insert(new Point2D(0.114743, 0.818712));
        circle.insert(new Point2D(0.922164, 0.767913));
        circle.insert(new Point2D(0.008856, 0.406309));
        circle.insert(new Point2D(0.999013, 0.531395));
        circle.insert(new Point2D(0.008856, 0.593691));
        circle.insert(new Point2D(0.624345, 0.984292));
        circle.insert(new Point2D(0.345492, 0.975528));
        circle.insert(new Point2D(0.206107, 0.904508));
        circle.insert(new Point2D(0.000000, 0.500000));
        circle.insert(new Point2D(0.500000, 0.000000));
        circle.insert(new Point2D(0.181288, 0.885257));
        circle.insert(new Point2D(0.562667, 0.003943));
        circle.insert(new Point2D(0.654508, 0.975528));
        circle.insert(new Point2D(0.003943, 0.437333));
        circle.insert(new Point2D(0.468605, 0.999013));
        circle.insert(new Point2D(0.061847, 0.740877));
        circle.insert(new Point2D(0.468605, 0.000987));
        circle.insert(new Point2D(0.922164, 0.232087));
        circle.insert(new Point2D(0.818712, 0.885257));
        circle.insert(new Point2D(0.712890, 0.952414));
        circle.insert(new Point2D(0.593691, 0.008856));
        circle.insert(new Point2D(0.015708, 0.624345));
        circle.insert(new Point2D(0.406309, 0.991144));
        circle.insert(new Point2D(0.035112, 0.315938));
        circle.insert(new Point2D(0.740877, 0.061847));
        circle.insert(new Point2D(0.000987, 0.468605));
        circle.insert(new Point2D(0.095492, 0.206107));
        circle.insert(new Point2D(0.885257, 0.181288));
        circle.insert(new Point2D(0.767913, 0.077836));
        circle.insert(new Point2D(0.003943, 0.562667));
        circle.insert(new Point2D(0.984292, 0.375655));
        circle.insert(new Point2D(0.035112, 0.684062));
        circle.insert(new Point2D(0.259123, 0.061847));
        circle.insert(new Point2D(0.232087, 0.922164));
        circle.insert(new Point2D(0.996057, 0.562667));
        circle.insert(new Point2D(0.077836, 0.232087));
        circle.insert(new Point2D(0.593691, 0.991144));
        circle.insert(new Point2D(0.375655, 0.015708));
        circle.insert(new Point2D(0.000987, 0.531395));
        circle.insert(new Point2D(0.375655, 0.984292));
        circle.insert(new Point2D(0.077836, 0.767913));
        circle.insert(new Point2D(0.114743, 0.181288));
        circle.insert(new Point2D(0.904508, 0.206107));
        circle.insert(new Point2D(0.024472, 0.654508));
        circle.insert(new Point2D(0.206107, 0.095492));
        circle.insert(new Point2D(0.562667, 0.996057));
        circle.insert(new Point2D(0.287110, 0.952414));
        circle.insert(new Point2D(0.406309, 0.008856));
        circle.insert(new Point2D(0.315938, 0.035112));
        circle.insert(new Point2D(0.975528, 0.654508));
        circle.insert(new Point2D(0.991144, 0.593691));
        circle.insert(new Point2D(0.437333, 0.003943));
        circle.insert(new Point2D(0.842274, 0.864484));
        circle.insert(new Point2D(0.345492, 0.024472));
        circle.insert(new Point2D(0.287110, 0.047586));
        circle.insert(new Point2D(0.232087, 0.077836));
        circle.insert(new Point2D(0.624345, 0.015708));
        circle.insert(new Point2D(0.904508, 0.793893));
        circle.insert(new Point2D(0.015708, 0.375655));
        circle.insert(new Point2D(0.964888, 0.684062));
        circle.insert(new Point2D(0.999013, 0.468605));
        circle.insert(new Point2D(0.500000, 1.000000));
        circle.insert(new Point2D(0.135516, 0.842274));
        circle.insert(new Point2D(0.095492, 0.793893));
        circle.insert(new Point2D(0.991144, 0.406309));
        circle.insert(new Point2D(0.654508, 0.024472));
        circle.insert(new Point2D(0.767913, 0.922164));
        circle.insert(new Point2D(0.984292, 0.624345));
        circle.insert(new Point2D(0.531395, 0.999013));
        circle.insert(new Point2D(0.996057, 0.437333));
        circle.insert(new Point2D(0.818712, 0.114743));
        circle.insert(new Point2D(0.864484, 0.157726));
        circle.insert(new Point2D(0.964888, 0.315938));
        circle.insert(new Point2D(1.000000, 0.500000));
        circle.insert(new Point2D(0.531395, 0.000987));
        circle.insert(new Point2D(0.842274, 0.135516));
        circle.insert(new Point2D(0.259123, 0.938153));
        circle.insert(new Point2D(0.061847, 0.259123));
        circle.insert(new Point2D(0.885257, 0.818712));
        circle.insert(new Point2D(0.135516, 0.157726));
        circle.insert(new Point2D(0.024472, 0.345492));
        circle.insert(new Point2D(0.437333, 0.996057));
        circle.insert(new Point2D(0.793893, 0.095492));
        circle.insert(new Point2D(0.864484, 0.842274));
        circle.insert(new Point2D(0.952414, 0.287110));
        circle.insert(new Point2D(0.047586, 0.712890));
        circle.insert(new Point2D(0.157726, 0.135516));
        circle.insert(new Point2D(0.712890, 0.047586));
        
        return circle;
    }
}
