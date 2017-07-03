package puzzle;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private final Node solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        solution = initial.isGoal() ? new Node(initial) : initAndRun(initial);
    }
    // is the initial board solvable?
    public boolean isSolvable() {
        return solution != null;
        
    }
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return solution != null ? solution.getMoves() : -1;
    }
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solution == null)
            return null;
        
        List<Board> solutions = new LinkedList<>();
        Node node = solution;
        while (node != null) {
            solutions.add(0, node.getBoard());
            node = node.getParent();
        }
        
        return solutions;
    }
    
    private Node initAndRun(Board board) {
        final int initialSize = 10; 
        MinPQ<Node> queue = new MinPQ<>(initialSize, new NodeManhattanComparator());
        queue.insert(new Node(board));

        MinPQ<Node> twinQueue = new MinPQ<>(initialSize, new NodeManhattanComparator());
        twinQueue.insert(new Node(board.twin()));
        
        return run(queue, twinQueue);
    }
    
    private Node run(MinPQ<Node> queue, MinPQ<Node> twinQueue) {
        Node board = null;
        Node twin = null;
        do {
            board = nextNode(queue);
            twin = nextNode(twinQueue);
        } while (!board.getBoard().isGoal() && !twin.getBoard().isGoal());
        
        return board.getBoard().isGoal() ? board : null;
    }
    
    private Node nextNode(MinPQ<Node> queue) {
        Node node = queue.delMin();
        node.insertNeighbors(queue);
        
        return node;
    }
    
    private class NodeManhattanComparator implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            int diff = o1.getPriority() - o2.getPriority();
            return diff == 0 ? 0 : diff < 0 ? -1 : 1;
        }
        
    }
    
    // Should use previous Board but coursera doesn`t allow to change public API
    private class Node {
        private final Board board;
        private final Node parent;
        private final int moves;
        
        Node(Board current) {
            this(current, null, 0);
        }
        
        Node(Board current, Node parent, int moves) {
            this.board = current;
            this.parent = parent;
            this.moves = moves;
        }
        
        Board getBoard() {
            return board;
        }
        
        Node getParent() {
            return parent;
        }
        
        int getMoves() {
            return moves;
        }
        
        int getPriority() {
            return board.manhattan() + moves;
        }
        
        void insertNeighbors(MinPQ<Node> queue) {
            Iterable<Board> neighbors = board.neighbors();
            for (Board b : neighbors) {
                if (parent != null) {
                    if (parent.getBoard().equals(b))
                        continue;
                }
                queue.insert(new Node(b, this, moves + 1));
            }
        }
    }
}
