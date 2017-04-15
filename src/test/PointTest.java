package test;

import java.util.Comparator;

import collinierpoints.Point;
import junit.framework.TestCase;

public class PointTest extends TestCase {
    Point p1, p2, p3, p4, p5, p6, p7;
    
    protected void setUp() throws Exception {
        p1 = new Point(10, 20);
        p2 = new Point(10, 30);
        p3 = new Point(5, 20);
        p4 = new Point(20, 20);
        p5 = new Point(10, 20);
        p6 = new Point(285, 221);
        p7 = new Point(272, 221);
    }

    protected void tearDown() throws Exception {
        
    }
    
    public void testCompareTo() {
        assertEquals(0, p1.compareTo(p1));
        assertEquals(0, p1.compareTo(p5));
        assertEquals(-10, p1.compareTo(p2));
        assertEquals(5, p1.compareTo(p3));
        assertEquals(-10, p1.compareTo(p4));
    }
    
    public void testComparator() {
        Comparator<Point> comparartor = p1.slopeOrder();
        
        assertEquals(0, comparartor.compare(p2, p2));
        assertEquals(1, comparartor.compare(p2, p3));
        assertEquals(-1, comparartor.compare(p3, p2));
        assertEquals(1, comparartor.compare(p2, p4));
        assertEquals(1, comparartor.compare(p2, p5));
        assertEquals(0, comparartor.compare(p3, p4));
        assertEquals(1, comparartor.compare(p4, p5));
    }
    
    public void testSlope() {
        assertEquals(+0.0, p6.slopeTo(p7));
    }
}
