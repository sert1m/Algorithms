package test;

import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;
import puzzle.Board;

public class BoardTest extends TestCase {

    Board board0, board1, board2, board3, board4, board5, board6, board7;
    
    protected void setUp() throws Exception {
        int [][] blocks0 = {{1, 2, 3},{4, 5, 6},{7, 8, 0}};
        int [][] blocks1 = {{8, 1, 3},{4, 0, 2},{7, 6, 5}};
        int [][] blocks2 = {{8, 1, 3},{0, 4, 2},{7, 6, 5}};
        int [][] blocks3 = {{8, 1, 3},{4, 2, 0},{7, 6, 5}};
        int [][] blocks4 = {{8, 0, 3},{4, 1, 2},{7, 6, 5}};
        int [][] blocks5 = {{8, 1, 3},{4, 6, 2},{7, 0, 5}};
        int [][] blocks6 = {{0, 1, 3},{8, 4, 2},{7, 6, 5}};
        int [][] blocks7 = {{8, 1, 3},{7, 4, 2},{0, 6, 5}};


        board0 = new Board(blocks0);
        board1 = new Board(blocks1);
        board2 = new Board(blocks2);
        board3 = new Board(blocks3);
        board4 = new Board(blocks4);
        board5 = new Board(blocks5);
        board6 = new Board(blocks6);
        board7 = new Board(blocks7);
    }

    protected void tearDown() throws Exception {
        
    }
    
    public void testHamming() {
        assertEquals(0, board0.hamming());
        assertEquals(5, board1.hamming());
    }
    
    public void testManhattan() {
        assertEquals(0, board0.manhattan());
        assertEquals(10, board1.manhattan());
    }
    
    public void testIsGoal() {
        assertEquals(true, board0.isGoal());
        assertEquals(false, board1.isGoal());
    }
    
    public void testTwin() {
        // Some improves?
        assertEquals(false, board0.equals(board0.twin()));
    }
    
    public void testEqusls() {
        assertEquals(false, board0.equals(board1));
        assertEquals(true, board0.equals(board0));
    }
    
    public void testNeighbours() {
        List<Board> neighbors = (List<Board>) board1.neighbors();
        List<Board> boards = new LinkedList<>();
        boards.add(board2);
        boards.add(board3);
        boards.add(board4);
        boards.add(board5);
        
        assertEquals(4, boards.size());
        assertEquals(true, boards.containsAll(neighbors));

        Board board = null;
        for (Board b : neighbors)
            if (b.equals(board2)) {
                board = b;
                break;
            }
        
        neighbors = (List<Board>) board.neighbors();
        boards.clear();
        boards.add(board1);
        boards.add(board6);
        boards.add(board7);
        assertEquals(3, boards.size());
        assertEquals(true, boards.containsAll(neighbors));
    }
}
