package puzzle;

import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int n;
    private int[][] blocks;
    private int zeroX, zeroY;
    private Board previous; 
    
    public Board(int[][] blocks) {
        this(blocks, null);
    }
    
    public int dimension() {
        return n;
    }
    
    public int hamming() {
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
    
    public int manhattan() {
        int manhattan = 0;
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++)
                manhattan += manhattan(i, j);
        
        return manhattan;
    }
    
    public boolean isGoal() {
        return manhattan() == 0;
    }
    
    public Board twin() {
        Board board = new Board(copyBlocks());
        board.swapRandomPair();
        
        return board;
    }
    
    public boolean equals(Object y) {
        Board temp = (Board) y;
        if (null == temp)
            return false;
        
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
    
    public Iterable<Board> neighbors() {
        List<Board> neighbors = new LinkedList<>();
        
        for (Moves diraction : Moves.values()) {
            Board neighbor = createNeighbor(diraction);
            if (neighbor != null)
                neighbors.add(neighbor);
        }
        
        return neighbors;
    }
    
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
    
    private Board(int[][] blocks, Board previous) {
        this.blocks = blocks;
        this.n = blocks.length;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                if (blocks[i][j] == 0) {
                    updateZeroPosition(i, j);
                    break;
                }
        this.previous = previous;
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
    
    private int [][] copyBlocks() {
        int [][] blocks = new int [n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = this.blocks[i][j];
        
        return blocks;
    }
    
    private void swapRandomPair() {
        int i = StdRandom.uniform(0, n - 1);
        int j = StdRandom.uniform(0, n);
        
        swapPair(i, j, i + 1, j);
    }
    
    private void swapPair(int i, int j, int k, int l) {
        blocks[i][j] = (blocks[i][j] ^ blocks[k][l]) ^ (blocks[k][l] = blocks[i][j]);
        
        if (blocks[i][j] == 0) 
            updateZeroPosition(i, j);
        
        if (blocks[k][l] == 0)
            updateZeroPosition(k, l);
    }
    
    private Board createNeighbor(Moves direction) {
        int newZeroX = direction.moveX(zeroX);
        int newZeroY = direction.moveY(zeroY);
        
        if (newZeroX < 0 || newZeroX > n)
            return null;
        
        if (newZeroY < 0 || newZeroY > n)
            return null;
        
        Board board = new Board(copyBlocks(), this);
        board.swapPair(zeroX, zeroY, newZeroX, newZeroY);
        
        if (board.equals(previous))
            return null;
        
        return board;
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