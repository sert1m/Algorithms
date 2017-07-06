package puzzle;

import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private int[][] blocks;
    private int n, zeroX, zeroY;
    private int hamming, manhattan;
    
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        this.n = blocks.length;
        this.blocks = copyBlocks(blocks, n);
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] == 0) {
                    updateZeroPosition(i, j);
                    break;
                }
        hamming = manhattan = -1;
    }
    
    // board dimension n
    public int dimension() {
        return n;
    }
    // number of blocks out of place
    public int hamming() {
        if (hamming == -1) 
            hamming = countHamming();
        
        return hamming;
    }
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        if (manhattan == -1)
            manhattan = countManhattan();
        
        return manhattan;
    }
    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board board = new Board(copyBlocks(this.blocks, n));
        board.swapRandomPair();
        
        return board;
    }
    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null)
            return false;
        
        if (y.getClass() != this.getClass())
            return false;
        
        Board temp = (Board) y;
        if (this == temp)
            return true;
        
        if (temp.n != n)
            return false;
        
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] != temp.blocks[i][j])
                    return false;
                
        return true;
    }
    // all neighboring boards
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new LinkedList<>();
        
        for (Moves diraction : Moves.values()) {
            Board neighbor = createNeighbor(diraction);
            if (neighbor != null)
                neighbors.add(neighbor);
        }
        
        return neighbors;
    }
    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(n).append("\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) 
                buffer.append(blocks[i][j]).append("\n");
            buffer.append("\n");
        }
        
        return buffer.toString();
    }

    private int countHamming() {
        int outOfPlace = 0;
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0)
                    continue;
                
                if (blocks[i][j] != getRequiredNumber(i, j))
                    outOfPlace++;
            }
        return outOfPlace;
    }

    private int countManhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++)
                manhattan += manhattan(i, j);
        
        return manhattan;
    }
    
    private void updateZeroPosition(int i, int j) {
        zeroX = i;
        zeroY = j;
    }
    
    private int getRequiredNumber(int i, int j) {
        if (i == (n - 1) && j == (n - 1))
            return 0;
        
        return i * n + j + 1;
    }
    
    private int manhattan(int i, int j) {
        if (blocks[i][j] == 0)
            return 0;
        
        // on the first place should be 1, so -1 to the number
        int x = (blocks[i][j] - 1) / n;
        int y = (blocks[i][j] - 1) % n;
        
        return Math.abs(x - i) + Math.abs(y - j); 
    }
    
    private void swapRandomPair() {
        
        int i = 0; 
        int j = 0;
        
        do {
            i = StdRandom.uniform(0, n - 1);
            j = StdRandom.uniform(0, n);
        } while (blocks[i][j] == 0 || blocks[i + 1][j] == 0);
        
        swapPair(i, j, i + 1, j);
    }
    
    private void swapPair(int i, int j, int x, int y) {
        int temp = blocks[i][j];
        blocks[i][j] = blocks[x][y];
        blocks[x][y] = temp;
        
        if (blocks[i][j] == 0) 
            updateZeroPosition(i, j);
        
        if (blocks[x][y] == 0)
            updateZeroPosition(x, y);
        
        // Need to update scores
        hamming = countHamming();
        manhattan = countManhattan();
    }
    
    private Board createNeighbor(Moves direction) {
        int newZeroX = direction.moveX(zeroX);
        int newZeroY = direction.moveY(zeroY);
        
        if (newZeroX < 0 || newZeroX >= n)
            return null;
        
        if (newZeroY < 0 || newZeroY >= n)
            return null;
        
        Board board = new Board(copyBlocks(this.blocks, n));
        board.swapPair(zeroX, zeroY, newZeroX, newZeroY);
        
        return board;
    }
    
    private static int [][] copyBlocks(int [][] blocks, int n) {
        int [][] temp = new int [n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                temp[i][j] = blocks[i][j];
        
        return temp;
    }
    
    private enum Moves {
        
        LEFT(0, -1), 
        RIGHT(0, 1), 
        UP(-1, 0), 
        DOWN(1, 0);

        private int stepX, stepY;
        
        Moves(int stepX, int stepY) {
            this.stepX = stepX;
            this.stepY = stepY;
        }
        
        public int moveX(int x) {
            return x + stepX;
        }

        public int moveY(int y) {
            return y + stepY;
        }
        
    };
}