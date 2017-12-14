package seamcarving;
/**
 * Solution for Algorithm pt2 
 * Course link: https://www.coursera.org/learn/algorithms-part2
 * Task specification: http://coursera.cs.princeton.edu/algs4/assignments/seam.html
 */


import java.awt.Color;

import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    
    private Picture picture;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("Invalid index");
        this.picture = new Picture(picture);
    }
    // current picture
    public Picture picture() {
        Picture temp = new Picture(width(), height());
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++)
                temp.set(i, j, picture.get(i, j));
        return temp;
    }
    // width of current picture
    public int width() {
        return picture.width();
    }
    // height of current picture
    public int height() {
        return picture.height();
    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x >= width() || y >= height() || x < 0 || y < 0) throw new IllegalArgumentException("Invalid index x = " + x + " y = " + y);
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000;
        
        return Math.sqrt(deltaSquare(picture.get(x - 1, y), picture.get(x + 1, y)) +
                         deltaSquare(picture.get(x, y - 1), picture.get(x, y + 1)));
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(getTransposeEnergy());
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        return findSeam(getEnergy());
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        validateSeam(seam, width(), height());
        
        Picture temp = new Picture(width(), height() - 1);
        int shift = 0;
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height() - 1; j++) {
                if (j == seam[i]) shift = 1;
                temp.set(i, j, picture.get(i, j + shift));
            }
            shift = 0;
        }
        
        picture = temp;
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        validateSeam(seam, height(), width());
        
        Picture temp = new Picture(width() - 1, height());
        int shift = 0;
        for (int i = 0; i < height(); i++) {
            for (int j = 0; j < width() - 1; j++) {
                if (j == seam[i]) shift = 1;
                temp.set(j, i, picture.get(j + shift, i));
            }
            shift = 0;
        }
        
        picture = temp;
    }
    
    private double deltaSquare(Color colorA, Color colorB) {
        double dr = colorA.getRed() - colorB.getRed();
        double dg = colorA.getGreen() - colorB.getGreen();
        double db = colorA.getBlue() - colorB.getBlue();
        
        return dr * dr + db * db + dg * dg;
    }
    
    private double [][] getEnergy() {
        double [][] energy = new double[height()][width()];
        
        for (int i = 0; i < energy.length; i++)
            for (int j = 0; j < energy[i].length; j++)
                energy[i][j] = energy(j, i);
        
        return energy;
    }
    
    private double [][] getTransposeEnergy() {
        double [][] energy = new double[width()][height()];
        
        for (int i = 0; i < energy.length; i++)
            for (int j = 0; j < energy[i].length; j++)
                energy[i][j] = energy(i, j);
        
        return energy;
    }
    
    private void validateSeam(int [] seam, int size, int maxValue) {
        if (seam == null) throw new IllegalArgumentException("Seam is null");
        if (seam.length != size) throw new IllegalArgumentException("Invalid seam length");
        
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= maxValue) 
                throw new IllegalArgumentException("Invalid seam index " + seam[i]);
            
            if (i + 1 < seam.length) {
                int diff = seam[i] - seam[i + 1];
                if (diff < -1 || diff > 1) 
                    throw new IllegalArgumentException("Invalid seams " + seam[i] + " " + seam[i + 1]);
            }
        }
    }
    
    private int [] findSeam(double [][] energy) {
        Graph g = createDigraph(energy);
        AcyclicSP acyclicSP = new AcyclicSP(g.getGraph(), g.getVirtualTop());
        Iterable<DirectedEdge> edges = acyclicSP.pathTo(g.getVirtualBottom());
        int i = 0;
        int [] path = new int[energy.length];
        for (DirectedEdge edge : edges) {
            if (edge.from() == g.getVirtualTop() || edge.from() == g.getVirtualBottom())
                continue;
            path[i++] = edge.from() % energy[0].length;
        }
        
        return path;
    }
    
    private Graph createDigraph(double [][] energy) {
        int height = energy.length;
        int width = energy[0].length;
        Graph g = new Graph(height * width);
        
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                // adding edge below
                int from = i * width + j;
                int to = (i + 1) * width + j;
                g.addEdge(from, to, energy[i + 1][j]);
                
                // checking for corner cases
                if (j - 1 >= 0)    
                    g.addEdge(from, to - 1, energy[i + 1][j - 1]);
                if (j + 1 < width) 
                    g.addEdge(from, to + 1, energy[i + 1][j + 1]);
            }
        }
        // Add virtual edges for top and bottom
        for (int i = 0; i < width; i++) 
            g.addEdge(g.getVirtualTop(), i, energy[0][i]);
        for (int i = 0; i < width; i++) 
            g.addEdge(width * (height - 1) + i, g.getVirtualBottom(), energy[height - 1][i]);
        
        return g;
    }
    
    // EdgeWeightedDigraph with two virtual vertexes
    private class Graph {
        private final EdgeWeightedDigraph g;
        private final int virtualTop;
        private final int virtualBottom;
        
        public Graph(int v) {
            g = new EdgeWeightedDigraph(v + 2);
            virtualTop = v;
            virtualBottom = v + 1;
        }
        
        public EdgeWeightedDigraph getGraph() { return g; }
        public int getVirtualTop() { return virtualTop; }
        public int getVirtualBottom() { return virtualBottom; }
        private void addEdge(int from, int to, double weight) {
            DirectedEdge edge = new DirectedEdge(from, to, weight);
            g.addEdge(edge);
        }
    }
    
//    @SuppressWarnings("unused")
//    private static void printEdge(int from, int to, double weight) {
//        System.out.print(from + "->" + to + " weight " + weight + "\n");
//    }
//    
//    @SuppressWarnings("unused")
//    private static void printSeam(int [] seam) {
//        System.out.print("seam {");
//        for (int i : seam)
//            System.out.print(i + ", ");
//        System.out.print("} \n");
//    }
//    
//    @SuppressWarnings("unused")
//    private static void printPicture(Picture picture) {
//        System.out.print("Picture\n");
//        for (int i = 0; i < picture.height(); i++) {
//            for (int j = 0; j < picture.width(); j++)
//                System.out.print("#"+Integer.toHexString(picture.get(j,i).getRGB()) + " ");
//            System.out.print("\n");
//        }
//    }
 }