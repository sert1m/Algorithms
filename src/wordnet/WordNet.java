package wordnet;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;

public class WordNet {

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Invalid file names");

        readSynsets(synsets);
        createGraph(hypernyms);
    };

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return new DFS(g.reverse(), findNounIndex()).getReachable();
    };

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("word is null");

        return distance("noun", word) < g.V();
    };

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("nounA or nounB is null");
        
        BreadthFirstDirectedPaths bfdp = new BreadthFirstDirectedPaths(g, findIndex(nounA));
        return bfdp.distTo(findIndex(nounB));
    };

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException("nounA or nounB is null");
        
        BreadthFirstDirectedPaths pathA = new BreadthFirstDirectedPaths(g, findIndex(nounA));
        BreadthFirstDirectedPaths pathB = new BreadthFirstDirectedPaths(g, findIndex(nounB));

        String sap = null;
        
        for (Integer a : pathA.pathTo(findNounIndex())) {
            for (Integer b : pathB.pathTo(findNounIndex())) {
                if (a.equals(b)) {
                    sap = synsets.get(pathA.distTo(a) + pathB.distTo(b)).synset;
                    return sap;
                }
            }
        }
        
        return sap;
    };
    
    private List<Synset> synsets;
    private Digraph g;
    
    private class Synset {
        final public int id;
        final public String synset;
        
        public Synset(int id, String synset) {
            this.id = id;
            this.synset = synset;
        }
    }
    
    private class DFS {
        
        public DFS(Digraph g, int v) {
            this.g = g;
            marked = new boolean[g.V()];
            reachableSyns = new LinkedList<String>();
            dfs(v);
        }
        
        public Iterable<String> getReachable() {
            return reachableSyns;
        }
        
        private boolean [] marked;
        private Digraph g;
        private List<String> reachableSyns;
        
        private void dfs(int v) {
            if (marked[v])
                return;
            
            marked[v] = true;
            reachableSyns.add(synsets.get(v).synset);
            for (Integer i : g.adj(v)) 
                dfs(i);
        }
    }
    
    private void readSynsets(String synsets) {
        this.synsets = new ArrayList<Synset>();
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String str = in.readLine();
            String [] data = str.split(",");
            if (data.length < 3)
                throw new IllegalArgumentException("Invalid length of synset");
            
            this.synsets.add(new Synset(Integer.valueOf(data[0]), data[1]));
        }
    };
    
    private void createGraph(String hypernyms) {
        g = new Digraph(synsets.size());
        
        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String str = in.readLine();
            String [] data = str.split(",");
            for (int i = 1; i < data.length; i++)
                g.addEdge(Integer.valueOf(data[0]), Integer.valueOf(data[i]));
        }
    }
    
    private int findNounIndex() {
        return findIndex("noun");
    };
    
    private int findIndex(String word) {
        int index = -1;
        
        for(Synset s : synsets) {
            if (s.synset.equals("noun")) {
                index = s.id;
                break;
            }
        }
        
        if (index == -1)
            throw new IllegalArgumentException();
        
        return index;
    }
    
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        Iterable<String> nouns = wordNet.nouns();
        for (String s : nouns)
            System.out.print(s + "\n");
        
        System.out.print(wordNet.sap("common_noun", "count_noun"));
    }
 }