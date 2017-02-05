import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;

public class Solver {
    private LinkedList<Board> solution;
    private int moves;

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int priority;
        private int moves;
        private int manhattan;
        private int hamming;
        private SearchNode prev;

        public SearchNode(Board newboard, SearchNode prevnode) {
            board = newboard;
            manhattan = board.manhattan();
            hamming = board.hamming();
            if (prevnode != null) moves = prevnode.moves +  1;
            else moves = 0;
            priority = moves + manhattan;
            prev = prevnode;
        }

        public int compareTo(SearchNode that) {
            if (this.priority < that.priority) return -1;
            if (this.priority > that.priority) return 1;
            // if (this.priority == that.priority) {
            //     if (this.moves + this.hamming < that.moves + that.hamming)
            //         return -1;
            //     if (this.moves + this.hamming > that.moves + that.hamming)
            //         return 1;
            // }
            return 0;
        }

        public Board board() {
            return board;
        }

        public SearchNode prev() {
            return prev;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        moves = -1;

        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        SearchNode node = new SearchNode(initial, null);

        MinPQ<SearchNode> twinpq = new MinPQ<SearchNode>();
        SearchNode twinnode = new SearchNode(initial.twin(), null);

        pq.insert(node);
        twinpq.insert(twinnode);

        node = pq.delMin();
        if (node.board().isGoal()) {
            solution = new LinkedList<Board>();
            solution.add(node.board());
            moves = 0;
            return;
        }

        twinnode = twinpq.delMin();
        if (twinnode.board().isGoal()) return;

        for (Board neighbor : node.board().neighbors())
            pq.insert(new SearchNode(neighbor, node));
        for (Board neighbor : twinnode.board().neighbors())
            twinpq.insert(new SearchNode(neighbor, twinnode));

        do {
            node = pq.delMin();
            if (node.board().isGoal()) break;

            twinnode = twinpq.delMin();

            if (twinnode.board().isGoal()) {
                node = null;
                break;
            }

            for (Board neighbor : node.board().neighbors())
                if (!neighbor.equals(node.prev.board()))
                    pq.insert(new SearchNode(neighbor, node));
            for (Board neighbor : twinnode.board().neighbors())
                if (!neighbor.equals(twinnode.prev.board()))
                    twinpq.insert(new SearchNode(neighbor, twinnode));
        } while (true);

        moves = -1;
        if (node != null) solution = new LinkedList<Board>();
        while (node != null) {
            solution.addFirst(node.board());
            moves++;
            node = node.prev;
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return (moves != -1);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        LinkedList<Board> boards = new LinkedList<Board>(solution);
        return boards;
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

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
