package wordnet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {
    private final List<String[]> synsetList;
    private final Map<String, Bag<Integer>> nouns;
    private final SAP sap;
    private final Digraph g;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("Invalid file names");
        
        this.synsetList = new ArrayList<>();
        this.nouns = new HashMap<>();
        
        readSynsets(synsets);
        g = new Digraph(this.synsetList.size());
        createGraph(hypernyms);
        sap = new SAP(g);
    };

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    };

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("word is null");

        return nouns.containsKey(word);
    };

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException(nounA + " or " + nounB + " is not a noun");
        
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    };

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException(nounA + " or " + nounB + " is not a noun");
        
        int ancestor = sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return synsetList.get(ancestor)[0];
    };
    
    private void readSynsets(String synsets) {
        In in = new In(synsets);
        while (!in.isEmpty()) {
            String str = in.readLine();
            String [] data = str.split(",");
            if (data.length < 3)
                throw new IllegalArgumentException("Invalid length of synset");
            
            this.synsetList.add(data[1].trim().split(" "));
            
            for (String noun : data[1].split(" ")) {
                Bag<Integer> ids = nouns.get(noun);
                if (ids == null)
                {
                    ids = new Bag<Integer>();
                    ids.add(this.synsetList.size() - 1);
                    nouns.put(noun, ids);
                    continue;
                }

                ids.add(this.synsetList.size() - 1);
            }
        }
    };
    
    private void createGraph(String hypernyms) {
        In in = new In(hypernyms);
        while (!in.isEmpty()) {
            String str = in.readLine();
            String [] data = str.split(",");
            for (int i = 1; i < data.length; i++)
                g.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[i]));
        }
        
        if (!isRootedDAG(g)) {
            throw new java.lang.IllegalArgumentException("Not rooted DAG!");
        }
    }
    
    private boolean isRootedDAG(Digraph G) {
        DirectedCycle diCycle = new DirectedCycle(G);
        if (diCycle.hasCycle()) {
           return false;
        }
        
        int roots = 0;
        for (int vertex = 0; vertex < G.V(); vertex++) {
           if (!G.adj(vertex).iterator().hasNext()) roots++;
        }
        
        return (roots == 1);
    }
    
    public static void main(String[] args) {
        WordNet wn = new WordNet(args[0], args[1]);
        for (String s : wn.nouns()) {
           StdOut.println(s);
        }
        while (!StdIn.isEmpty()) {
           String nounA = StdIn.readLine();
           String nounB = StdIn.readLine();
           int distance   = wn.distance(nounA, nounB);
           String ancestor = wn.sap(nounA, nounB);
           StdOut.println("length = " + distance);
           StdOut.println("ancestor = " + ancestor);
        }
    }
 }