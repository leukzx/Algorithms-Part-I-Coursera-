import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int gridSize;
    private boolean[] openSites;
    private WeightedQuickUnionUF connections;
    private WeightedQuickUnionUF connectionsTop;
    private int numberOfOpen = 0;
    private int virtTop = 0;
    private int virtBott = 0;
    
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n < 1) throw new IllegalArgumentException("n < 1 !!!");
        gridSize = n;
        openSites = new boolean[gridSize * gridSize + 2];
        for (int i = 0; i <= gridSize * gridSize; i++) 
            openSites[i] = false;

        connections = 
            new WeightedQuickUnionUF(gridSize * gridSize + 2);
        virtTop = gridSize * gridSize;
        virtBott = gridSize * gridSize + 1;
        
        connectionsTop = 
            new WeightedQuickUnionUF(gridSize * gridSize + 1);
    }
 
    // map from a 2-dimensional (row, column) pair to a 1-dimensional index
    private int xyTo1D(int row, int col) {
        if (row < 1 || row > gridSize) 
            throw new IndexOutOfBoundsException("row index out of bounds");
        if (col < 1 || col > gridSize) 
            throw new IndexOutOfBoundsException("column index out of bounds");
        
        return (row - 1) * gridSize + (col - 1);
    }
    
    // open site (row, col) if it is not open already
    public    void open(int row, int col) {
        int id = xyTo1D(row, col);        
        int idN = 0;
        if (openSites[id]) return;
        
        openSites[id] = true;
        numberOfOpen++;
        
        // top neighbour
        if (row > 1) {
            idN = xyTo1D(row - 1, col);
            if (openSites[idN]) {
                connections.union(idN, id);
                connectionsTop.union(idN, id);
            }
        } 
        else if (row == 1) {
            connections.union(virtTop, id);
            connectionsTop.union(virtTop, id);
        }
        
        // bottom neighbour
        if (row < gridSize) {
            idN = xyTo1D(row + 1, col);
            if (openSites[idN]) {
                connections.union(idN, id);
                connectionsTop.union(idN, id);
            }
        } 
        else if (row == gridSize) connections.union(virtBott, id);
        // left neighbour
        if (col > 1) {
            idN = xyTo1D(row, col - 1);
            if (openSites[idN]) {
                connections.union(idN, id);
                connectionsTop.union(idN, id);
            }
        }
        // right neighbour
        if (col < gridSize) {
            idN = xyTo1D(row, col + 1);
            if (openSites[idN]) {
                connections.union(idN, id);
                connectionsTop.union(idN, id);
            }
        }
    }
    
    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        int id = xyTo1D(row, col);
        return openSites[id];
    }
    
    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        return connectionsTop.connected(virtTop, xyTo1D(row, col));    
    }
    
    // number of open sites
    public     int numberOfOpenSites() {
        return numberOfOpen;
    }       
    // does the system percolate
    public boolean percolates() {
        return connections.connected(virtTop, virtBott);
    }              

    public static void main(String[] args) {   // test client (optional) 
    }
}