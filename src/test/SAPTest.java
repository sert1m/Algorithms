package test;

import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.Digraph;
import junit.framework.TestCase;
import wordnet.SAP;

public class SAPTest  extends TestCase {
    protected static Digraph digraph1, digraph2;
    protected SAP sap1, sap2;
   
    protected void setUp() throws Exception {
        sap1 = new SAP(digraph1);
        sap2 = new SAP(digraph2);
    }

    protected void tearDown() throws Exception {
        
    }
    
    public void testSingleAnchestor() {
        assertEquals(2, sap1.length(1, 4));
        assertEquals(2, sap1.ancestor(1, 4));
        assertEquals(4, sap1.length(0, 6));
        assertEquals(2, sap1.ancestor(0, 6));
        assertEquals(2, sap1.length(3, 5));
        assertEquals(4, sap1.ancestor(3, 5));
        
        assertEquals(4, sap2.length(3, 11));
        assertEquals(1, sap2.ancestor(3, 11));
        assertEquals(3, sap2.length(9, 12));
        assertEquals(5, sap2.ancestor(9, 12));
        assertEquals(4, sap2.length(7, 2));
        assertEquals(0, sap2.ancestor(7, 2));
        
        assertEquals(-1, sap2.length(1, 6));
        assertEquals(-1, sap2.ancestor(1, 6));
    }
    
    public void testMultipleAnchestor() {
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2 = new ArrayList<>();

        list1.add(0);
        list1.add(3);
        
        list2.add(8);
        
        assertEquals(2, sap1.ancestor(list1, list2));
        assertEquals(4, sap1.length(list1, list2));
        
        list2.add(5);
        assertEquals(4, sap1.ancestor(list1, list2));
        assertEquals(2, sap1.length(list1, list2));
    }

    static
    {
        digraph1 = new Digraph(11);
        digraph1.addEdge(0, 1);
        digraph1.addEdge(1, 2);
        digraph1.addEdge(3, 4);
        digraph1.addEdge(4, 2);
        digraph1.addEdge(5, 4);
        digraph1.addEdge(6, 5);
        digraph1.addEdge(6, 7);
        digraph1.addEdge(8, 7);
        digraph1.addEdge(9, 8);
        digraph1.addEdge(10, 9);
        digraph1.addEdge(0, 10);
        digraph1.addEdge(7, 2);
        
        digraph2 = new Digraph(13);
        digraph2.addEdge(7, 3);
        digraph2.addEdge(8, 3);
        digraph2.addEdge(3, 1);
        digraph2.addEdge(4, 1);
        digraph2.addEdge(5, 1);
        digraph2.addEdge(9, 5);
        digraph2.addEdge(10, 5);
        digraph2.addEdge(11, 10);
        digraph2.addEdge(12, 10);
        digraph2.addEdge(1, 0);
        digraph2.addEdge(2, 0);
    }
}