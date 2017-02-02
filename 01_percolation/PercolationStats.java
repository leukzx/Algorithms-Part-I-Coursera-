import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdOut;

public class PercolationStats {
    private int gridSize = 0;
    private int nTrials = 0;
    private double[] thresholds;
    private double mean = 0;
    private double stddev = 0;
    private double confidenceLo = 0;
    private double confidenceHi = 0;
    
    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) 
            throw new IllegalArgumentException("n < 1 || trials < 1 !!!");
        gridSize = n;
        nTrials = trials;
        thresholds = new double[trials];
        
        int x, y;
        
        for (int i = 0; i < nTrials; i++) {
            Percolation p = new Percolation(gridSize);
            while (!p.percolates()) {
                x = StdRandom.uniform(gridSize) + 1;
                y = StdRandom.uniform(gridSize) + 1;
                p.open(x, y);
            }
            thresholds[i] = 
                ((double) p.numberOfOpenSites() / (gridSize* gridSize));
        }
        mean = StdStats.mean(thresholds);
        for (int i = 0; i < nTrials; i++) {
            stddev += Math.pow(thresholds[i] - mean, 2);
        }
        stddev /=  (nTrials -1);
        stddev = StdStats.stddev(thresholds);
        
        confidenceLo = mean - 1.96 * stddev / Math.sqrt(nTrials);
        confidenceHi = mean + 1.96 * stddev / Math.sqrt(nTrials);
    }
    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }
    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    } 
    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }
    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }
    // test client
    public static void main(String[] args) {
        int n = 0;
        int t = 0;
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            t = Integer.parseInt(args[1]);
        }
        PercolationStats pS = new PercolationStats(n, t);
        StdOut.print("Sample mean of percolation threshold: ");
        StdOut.println(pS.mean());
        StdOut.print("Sample standard deviation of percolation threshold: ");
        StdOut.println(pS.stddev());
        StdOut.print("Low  endpoint of 95% confidence interval: ");
        StdOut.println(pS.confidenceLo());
        StdOut.print("High endpoint of 95% confidence interval: ");
        StdOut.println(pS.confidenceHi());
    }        
}