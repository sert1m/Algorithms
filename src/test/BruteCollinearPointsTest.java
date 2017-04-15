package test;

import collinierpoints.BruteCollinearPoints;
import collinierpoints.Point;
import junit.framework.TestCase;

public class BruteCollinearPointsTest extends TestCase {
    
    public void testExceptions() {
        try {
            new BruteCollinearPoints(null);
            
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            
        }
        
        try {
            Point[] points = { null, null }; 
            new BruteCollinearPoints(points);
            
            fail("NullPointerException is expected");
        } catch (NullPointerException e) {
            
        }
    }
    
}
