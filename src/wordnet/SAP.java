package wordnet;

import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int NOT_FOUND = -1;
    private final Digraph G;
    private int lastAncestor, lastLength;
    private int lastV, lastW;
    private Iterable<Integer> lastVI, lastWI;
    
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
        lastVI = null;
        lastWI = null;
        lastV = NOT_FOUND;
        lastW = NOT_FOUND;
        lastAncestor = NOT_FOUND;
        lastLength = NOT_FOUND;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isVertexValid(v) || !isVertexValid(w))
            throw new IllegalArgumentException("Invalid vertex v = " + v + " w = " + w);
        
        if (!isCached(v, w))
            countAnchestorAndLength(v,w);

        return lastLength;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!isVertexValid(v) || !isVertexValid(w))
            throw new IllegalArgumentException("Invalid vertex v = " + v + " w = " + w);
        
        if (!isCached(v, w))
            countAnchestorAndLength(v,w);
            
        return lastAncestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null value received");

        if (!isCached(v, w))
            countAnchestorAndLength(v,w);

        return lastLength;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null value received");
        
        if (!isCached(v, w))
            countAnchestorAndLength(v,w);

        return lastAncestor;
    }
    
    private boolean isVertexValid(int v) {
        return ((v > 0) || (v < G.V() - 1));
    }
    
    private boolean isCached(int v, int w) {
        return (lastV == v && lastW == w) || (lastV == w && lastW == v); 
    }
    
    private boolean isCached(Iterable<Integer> v, Iterable<Integer> w) {
        return (lastVI == v && lastWI == w) || (lastVI == w && lastWI == v);
    }
    
    private void countAnchestorAndLength(int v, int w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        
        countAnchestorAndLength(bfsV, bfsW);
        lastV = v;
        lastW = w;
    }
    
    private void countAnchestorAndLength(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        
        countAnchestorAndLength(bfsV, bfsW);
        lastVI = v;
        lastWI = w;
    }
    
    private void countAnchestorAndLength(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW) {
        List<Integer> ancestors = new LinkedList<>();
        for (int i = 0; i < G.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i))
                ancestors.add(i);
        }
        
        if (ancestors.isEmpty()) {
            lastLength = NOT_FOUND;
            lastAncestor = NOT_FOUND;
            return;
        }
        
        lastLength = Integer.MAX_VALUE;
        for (int i : ancestors) {
            int length = getLength(bfsV, bfsW, i);
            if (length < lastLength) {
                lastLength = length;
                lastAncestor = i;
            }
        }
        
    }
    
    private int getLength(BreadthFirstDirectedPaths bfsV, BreadthFirstDirectedPaths bfsW, int v) {
        return bfsV.distTo(v) + bfsW.distTo(v);
    }
    
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while(!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            StdOut.printf("length = %d, anchestor = %d\n", sap.length(v, w), sap.ancestor(v, w));
        }
    }
 }