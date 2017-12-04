package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private static final int NOT_FOUND = -1;
    private final Digraph G;
    private final CommonAncestorSearcher searcher;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
        searcher = new CommonAncestorSearcher(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!isVertexValid(v) || !isVertexValid(w))
            throw new IllegalArgumentException("Invalid vertex v = " + v + " w = " + w);
        
        int length = NOT_FOUND;
        int ancestor = searcher.getAncestor(v, w);
        if (ancestor != NOT_FOUND)
            length = searcher.getLength(ancestor);

        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!isVertexValid(v) || !isVertexValid(w))
            throw new IllegalArgumentException("Invalid vertex v = " + v + " w = " + w);

        return searcher.getAncestor(v, w);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null value received");

        int length = NOT_FOUND;
        int ancestor = searcher.getAncestor(v, w);
        if (ancestor != NOT_FOUND)
            length = searcher.getLength(ancestor);

        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null value received");

        return searcher.getAncestor(v, w);
    }
    
    private enum MarkType {
        UNMARKED,
        MARK_A,
        MARK_B;
    };
    
    private boolean isVertexValid(int v)
    {
        return ((v > 0) || (v < G.V() - 1));
    }
    
    private class CommonAncestorSearcher {
        private final Digraph G;
        private MarkType[] marked;
        private int[] distToA;
        private int[] distToB;
        
        CommonAncestorSearcher(Digraph G) {
            this.G = G;
            
            int size = G.V();
            marked = new MarkType[size];
            distToA = new int[size];
            distToB = new int[size];
        }
        
        public int getAncestor(int a, int b) {
            searcher.reset(G.V());

            Queue<Integer> queueA = createQueueAndSetDist(a, distToA);
            Queue<Integer> queueB = createQueueAndSetDist(b, distToB);
            
            return getAnchestor(queueA, queueB);
        }
        
        public int getAncestor(Iterable<Integer> a, Iterable<Integer> b) {
            searcher.reset(G.V());

            Queue<Integer> queueA = createQueueAndSetDist(a, distToA);
            Queue<Integer> queueB = createQueueAndSetDist(b, distToB);
            
            return getAnchestor(queueA, queueB);
        }
        
        public int getLength(int ancestor) {
            return distToA[ancestor] + distToB[ancestor];
        }
        
        public void reset(int size) {
            for (int i = 0; i < marked.length; i++) {
                marked[i] = null;
                distToA[i] = 0;
                distToB[i] = 0;
            }
        }
        
        
        private Queue<Integer> createQueueAndSetDist(int v, int[]dist) {
            Queue<Integer> queue = new Queue<>();
            
            queue.enqueue(v);
            dist[v] = 0;
            
            return queue;
        }
        
        private Queue<Integer> createQueueAndSetDist(Iterable<Integer> v, int[] dist) {
            Queue<Integer> queue = new Queue<>();
            
            for (int i : v) {
                queue.enqueue(i);
                dist[i] = 0;
            }
            
            return queue;
        }
        
        private int getAnchestor(Queue<Integer> queueA, Queue<Integer> queueB) {
            int ancestor = NOT_FOUND;
            while (!queueA.isEmpty() || !queueB.isEmpty()) {
                ancestor = searchAnchestor(queueA, MarkType.MARK_A, distToA);
                if (ancestor != NOT_FOUND)
                    break;
                
                ancestor = searchAnchestor(queueB, MarkType.MARK_B, distToB);
                if (ancestor != NOT_FOUND)
                    break;
            }
            
            return ancestor;
        }
        
        private int searchAnchestor(Queue<Integer> queue, MarkType mark, int[] distTo)
        {
            if (queue.isEmpty())
                return NOT_FOUND;
            
            int level = distTo[queue.peek()];
            while (!queue.isEmpty() && level == distTo[queue.peek()])
            {
                int c = queue.dequeue();
                if (marked[c] == null) {
                    for (int i : G.adj(c)) {
                        distTo[i] = distTo[c] + 1;
                        queue.enqueue(i);
                    }
                    marked[c] = mark;
                }
                else if (marked[c] != mark)
                    return c;
                else
                    continue;
            }
            
            return NOT_FOUND;
        }
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