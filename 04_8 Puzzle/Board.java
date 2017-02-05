// import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.LinkedList;
import java.util.Arrays;

public class Board {
    private char[] board;
    private int dim;
    private LinkedList<Board> neighbors;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null)
            throw new
                java.lang.NullPointerException("A null argument is passed.");
        if (blocks[0].length != blocks[1].length)
            throw new java.lang.IllegalArgumentException(
                "Input blocks array is not square.");

        dim = blocks[0].length;
        board = new char[dim * dim];

        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                board[xyTo1D(i, j)] = (char) blocks[i][j];
    }

    private int xyTo1D(int row, int col) {
        if (row > dim)
            throw new IndexOutOfBoundsException("Row index out of bounds");
        if (col > dim)
            throw new IndexOutOfBoundsException("Column index out of bounds");
        return row * dim + col;
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // number of blocks out of place
    public int hamming() {
        int num = 0;
        for (int i = 0; i < board.length; i++) {
            if (i + 1 != board[i] && board[i] != 0)    num++;
        }
        return num;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int length = 0;
        for (int i = 0; i < board.length; i++) {
            if (i + 1 != board[i] && board[i] != 0) {
                length += Math.abs((board[i] - 1) / dim - i / dim)
                        + Math.abs((board[i] - 1) % dim - i % dim);
            }
        }
        return length;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < board.length; i++)
            if (i + 1 != board[i] && board[i] != 0) return false;
        return true;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        int[][] blocks = new int[dim][dim];
        for (int i = 0; i < dim; i++)
            for (int j = 0; j < dim; j++)
                blocks[i][j] = (int) board[xyTo1D(i, j)];

        // int x = StdRandom.uniform(dim);
        // int y;

        // while (blocks[x][y = StdRandom.uniform(dim)] == 0);

        // int xswap = StdRandom.uniform(dim);
        // int yswap;
        // while (blocks[xswap][yswap = StdRandom.uniform(dim)] == 0
        //         || blocks[xswap][yswap] == blocks[x][y]);

        int x = 0;
        int y = 0;
        if (blocks[x][y] == 0) y++;

        int xswap = 1;
        int yswap = 0;
        if (blocks[xswap][yswap] == 0) yswap++;

        int swap = blocks[x][y];
        blocks[x][y] = blocks[xswap][yswap];
        blocks[xswap][yswap] = swap;

        Board twin = new Board(blocks);
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.dim != that.dim) return false;
        for (int i = 0; i < board.length; i++)
            if (this.board[i] != that.board[i]) return false;
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> neighs = new LinkedList<Board>();

        int zpos = 0;
        while (board[zpos] != 0) zpos++;

        LinkedList<Integer> npos = new LinkedList<Integer>();
        if (zpos - dim >= 0) npos.add(zpos - dim);
        if (zpos + dim < dim * dim) npos.add(zpos + dim);
        if (zpos % dim != 0) npos.add(zpos - 1);
        if ((zpos + 1) % dim != 0) npos.add(zpos + 1);

        char[] nboard = Arrays.copyOf(board, board.length);
        int[][] blocks = new int[dim][dim];
        for (int pos : npos) {
            nboard[zpos] = board[pos];
            nboard[pos] = 0;
            for (int i = 0; i < dim; i++)
                for (int j = 0; j < dim; j++)
                    blocks[i][j] = (int) nboard[xyTo1D(i, j)];
            neighs.add(new Board(blocks));
            nboard[pos] = board[pos];
            nboard[zpos] = 0;
        }
        return neighs;
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        int cnt = 0;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", (int) board[cnt++]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // unit tests (not graded)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        StdOut.println(initial.toString());
        Board twin = initial.twin();

    }
}
