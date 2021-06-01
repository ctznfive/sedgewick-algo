import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.MinPQ;

import java.util.Comparator;

public class Solver {

    private final Stack<Board> solution;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();

        solution = new Stack<>();
        Comparator<SearchNode> comparator = new NodeComparator();

        SearchNode nodeMain = new SearchNode();
        SearchNode nodeTwin = new SearchNode();

        nodeMain.board = initial;
        nodeTwin.board = initial.twin();

        nodeMain.priority = nodeMain.board.manhattan();
        nodeTwin.priority = nodeTwin.board.manhattan();

        MinPQ<SearchNode> pqMain = new MinPQ<>(comparator);
        MinPQ<SearchNode> pqTwin = new MinPQ<>(comparator);

        pqMain.insert(nodeMain);
        pqTwin.insert(nodeTwin);

        while (!pqMain.min().board.isGoal() && !pqTwin.min().board.isGoal()) {
            addNeighbours(pqMain);
            addNeighbours(pqTwin);
        }

        solvable = pqMain.min().board.isGoal();
        if (solvable) {
            SearchNode node = pqMain.min();
            solution.push(node.board);

            while (node.prev != null) {
                node = node.prev;
                solution.push(node.board);
            }
        }
    }

    private class NodeComparator implements Comparator<SearchNode> {
        public int compare(SearchNode searchNode, SearchNode t1) {
            return searchNode.priority - t1.priority;
        }
    }

    private class SearchNode {
        private Board board;
        private SearchNode prev;
        private int moves;
        private int priority;

        public SearchNode() { }

        public SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }
    }

    private void addNeighbours(MinPQ<SearchNode> pq) {
        SearchNode nodeMin = pq.delMin();
        for (Board neighbour : nodeMin.board.neighbors()) {
            if (nodeMin.prev != null && neighbour.equals(nodeMin.prev.board))
                continue;

            SearchNode nodeNeighbour = new SearchNode(neighbour,
                                                     nodeMin,
                                                     nodeMin.moves + 1);
            int priority = nodeNeighbour.moves + nodeNeighbour.board.manhattan();
            nodeNeighbour.setPriority(priority);

            pq.insert(nodeNeighbour);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solution == null)
            return -1;
        return solution.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solution == null)
            return null;
        if (!solvable)
            return null;

        return solution;
    }

    // test client (see below)
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
