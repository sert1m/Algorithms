package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int[] dist = new int[nouns.length];
        
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++)
                addToAll(dist, wordnet.distance(nouns[i], nouns[j]));
        }
        
        return nouns[getMax(dist)];
    }
    
    private WordNet wordnet;
    
    private void addToAll(int[] array, int number) {
        for (int i = 0; i < array.length; i++) 
            array[i] += number;
    }
    
    private int getMax(int[] array) {
        int maxAt = 0;
        for (int i = 0; i < array.length; i++) {
            maxAt = array[i] > array[maxAt] ? i : maxAt;
        }
        
        return maxAt;
    }
    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
 }