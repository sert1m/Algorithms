package wordnet;

import java.util.Arrays;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class SAP {
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = G;
        searcher = new CommonAncestorSearcher(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!is_vertex_valid(v) || !is_vertex_valid(w))
            throw new IllegalArgumentException("Invalid vertex v = " + v + " w = " + w);
        
        int length = NOT_FOUND;
        int ancestor = searcher.getAncestor(v, w);
        if (ancestor != NOT_FOUND)
            length = searcher.get_length(ancestor);

        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!is_vertex_valid(v) || !is_vertex_valid(w))
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
            length = searcher.get_length(ancestor);

        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new IllegalArgumentException("Null value received");

        return searcher.getAncestor(v, w);
    }
    
    private Digraph G;
    private CommonAncestorSearcher searcher;
    private static final int NOT_FOUND = -1;
    private enum MarkType {
        UNMARKED,
        MARK_A,
        MARK_B;
    };
    
    private boolean is_vertex_valid(int v)
    {
        return ((v > 0) || (v < G.E() - 1));
    }
    
    private class CommonAncestorSearcher {
        
        CommonAncestorSearcher(Digraph G) {
            this.G = G;
            
            int size = G.E();
            marked = new MarkType[size];
            distToA = new int[size];
            distToB = new int[size];
        }
        
        public int getAncestor(int a, int b) {
            searcher.reset(G.E());

            Queue<Integer> queueA = createQueueAndSetDist(a, distToA, MarkType.MARK_A);
            Queue<Integer> queueB = createQueueAndSetDist(b, distToB, MarkType.MARK_B);
            
            return getAnchestor(queueA, queueB);
        }
        
        public int getAncestor(Iterable<Integer> a, Iterable<Integer> b) {
            searcher.reset(G.E());

            Queue<Integer> queueA = createQueueAndSetDist(a, distToA, MarkType.MARK_A);
            Queue<Integer> queueB = createQueueAndSetDist(b, distToB, MarkType.MARK_B);
            
            return getAnchestor(queueA, queueB);
        }
        
        public int get_length(int ancestor) {
            return distToA[ancestor] + distToB[ancestor];
        }
        
        public void reset(int size) {
            Arrays.fill(marked, null);
            Arrays.fill(distToA, 0);
            Arrays.fill(distToB, 0);
        }
        
        private Digraph G;
        private MarkType[] marked;
        private int[] distToA;
        private int[] distToB;
        
        private Queue<Integer> createQueueAndSetDist(int v, int[]dist, MarkType type) {
            Queue<Integer> queue = new Queue<>();
            
            queue.enqueue(v);
            dist[v] = 0;
            marked[v] = type;
            
            return queue;
        }
        
        private Queue<Integer> createQueueAndSetDist(Iterable<Integer> v, int[] dist, MarkType type) {
            Queue<Integer> queue = new Queue<>();
            
            for (Integer i : v) {
                queue.enqueue(i);
                dist[i] = 0;
                marked[i] = type;
            }
            
            return queue;
        }
        private int getAnchestor(Queue<Integer> queueA, Queue<Integer> queueB) {
            int ancestor = NOT_FOUND;
            while(!queueA.isEmpty() && !queueB.isEmpty()) {
                ancestor = search_anchestor(queueA, MarkType.MARK_A, distToA);
                if (ancestor != NOT_FOUND)
                    break;
                
                ancestor = search_anchestor(queueB, MarkType.MARK_B, distToB);
                if (ancestor != NOT_FOUND)
                    break;
            }
            
            return ancestor;
        }
        
        private int search_anchestor(Queue<Integer> queue, MarkType mark, int[] distTo)
        {
            if (queue.isEmpty())
                return NOT_FOUND;
            
            int level = distTo[queue.peek()];
            while (level == distTo[queue.peek()])
            {
                int c = queue.dequeue();
                for (int i : G.adj(c)) {
                    if (marked[i] == null) {
                        distTo[i] = distTo[c] + 1;
                        marked[i] = mark;
                        queue.enqueue(i);
                    }
                    else if (marked[i] != mark)
                    {
                        distTo[i] = distTo[c] + 1;
                        return i;
                    }
                }
            }
            
            return NOT_FOUND;
        }
    }
 }