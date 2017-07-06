package test;

import junit.framework.TestCase;
import puzzle.Board;
import puzzle.Solver;

public class SolverTest  extends TestCase {
    protected void setUp() throws Exception {

    }

    protected void tearDown() throws Exception {
        
    }
    
    public void testGoal() {
        int [][] blocks = {{1, 2, 3},{4, 5, 6},{7, 8, 0}};
        Board board = new Board(blocks);
        Solver solv = new Solver(board);
        
        assertEquals(true, solv.isSolvable());
    }
    
    public void testSolvable0() {
        int [][] blocks = {{1, 0}, {3, 2}};
        testSolvable(blocks, 1);
    }
    
    public void testSolvable1() {
        int [][] blocks = {{0, 1, 3},{4, 2, 5},{7, 8, 6}};
        testSolvable(blocks, 4);
    }
    
    public void testSolvable2() {
        int [][] blocks = {{1, 6, 4}, {7, 0, 8}, {2, 3, 5}};
        testSolvable(blocks, 20);
    }
    
    public void testSolvable3() {
        int [][] blocks = {{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 0}, {13, 14, 15, 12}};
        testSolvable(blocks, 1);
    }
    
    public void testUnsolvable0() {
        int [][] blocks = {{3, 2, 4, 8}, {1, 6, 0, 12}, {5, 10, 7, 11}, {9, 13, 14, 15}};
        testUnsolvable(blocks);
    }
    
    public void testUnsolvable1() {
        int [][] blocks = {{1, 0}, {2, 3}};
        testUnsolvable(blocks);
    }
    
    private void testSolvable(int [][] blocks, int moves) {
        Board board = new Board(blocks);
        Solver solv = new Solver(board);
        
        assertEquals(true, solv.isSolvable());
        assertEquals(moves, solv.moves());
    }
    
    private void testUnsolvable(int [][] blocks) {
        Board board = new Board(blocks);
        Solver solv = new Solver(board);
        
        assertEquals(false, solv.isSolvable());
        assertEquals(-1, solv.moves());
    }
}
