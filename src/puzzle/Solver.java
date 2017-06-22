package puzzle;

import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    
    AStarAlgorithm aStarAlgorithm;
    Board solution = null;
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        this.aStarAlgorithm = new AStarAlgorithm(initial);
        aStarAlgorithm.run();
    }
    // is the initial board solvable?
    public boolean isSolvable() {
        return solution != null;
        
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution != null ? solution.getMoves() : -1 ;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (aStarAlgorithm.solution == null)
            return null;
        
        List<Board> solutions = new LinkedList<>();
        Board board = aStarAlgorithm.solution;
        while (board != null) {
            solutions.add(0, board);
            board = board.getPrevious();
        }
        
        return solutions;
    }
    
    private class AStarAlgorithm {
        Board solution;
        MinPQ<Board> queue, twinQueue;
        
        AStarAlgorithm(Board board) {
            queue.insert(board);
            twinQueue.insert(board.twin());
        }
        
        void run() {
            Board board, twin;
            while (true) {
                board = queue.delMin();
                if (!board.isGoal())
                    insertNeighbors(board, queue);
                else
                    break;
                    
                twin = twinQueue.delMin();
                if (!twin.isGoal())
                    insertNeighbors(twin, twinQueue);
                else
                    break;
            }
            
            if (board.isGoal()) {
                solution = board;
            }
        }
        
        void insertNeighbors(Board board, MinPQ<Board> queue) {
            Iterable<Board> neighbors = board.neighbors();
            for (Board b : neighbors)
                queue.insert(b);
        }
    }
}
