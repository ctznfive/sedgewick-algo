import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // provides a 95% confidence interval for the percolation threshold
    private static final double CONFIDENCE_95 = 1.96;
    private final double[] fractions;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("error");
        this.fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation trial = new Percolation(n);
            while (!trial.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                if (!trial.isOpen(row, col))
                    trial.open(row, col);
            }
            fractions[i] = trial.numberOfOpenSites() / (double) (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (fractions.length == 1) return Double.NaN;
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_95 * stddev() / Math.sqrt(fractions.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_95 * stddev() / Math.sqrt(fractions.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats stats = new PercolationStats(n, t);
        StdOut.println("mean = " + stats.mean());
        StdOut.println("stddev = " + stats.stddev());
        StdOut.println("95% confidence interval = [" +
                               stats.confidenceLo() + ", " + stats.confidenceHi() + "]");
    }

}
