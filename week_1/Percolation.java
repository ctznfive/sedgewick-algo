import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    private boolean[][] grid;
    private int numOpen;
    private WeightedQuickUnionUF unionData;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("n > 0");
        this.n = n;
        this.grid = new boolean[n][n];
        this.numOpen = 0;

        // the virtual-top / virtual-bottom trick
        this.unionData = new WeightedQuickUnionUF(n * n + 2);
        for (int i = 1; i <= n; i++) {
            unionData.union(i, 0);
            unionData.union(n * n + 1, n * n + 1 - i);
        }
    }

    private int xyTo1D(int row, int col) {
        return (row - 1) * n + col;
    }

    private void validIndices(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n)
            throw new IllegalArgumentException("error");
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        validIndices(row, col);
        int i = row - 1;
        int j = col - 1;
        // if the site is already open, then return
        if (grid[i][j]) return;

        grid[i][j] = true;
        numOpen++;

        int p = xyTo1D(row, col);
        if (i > 0 && grid[i - 1][j])
            unionData.union(p, p - n);
        if (i < n - 1 && grid[i + 1][j])
            unionData.union(p, p + n);
        if (j > 0 && grid[i][j - 1])
            unionData.union(p, p - 1);
        if (j < n - 1 && grid[i][j + 1])
            unionData.union(p, p + 1);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        validIndices(row, col);
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        validIndices(row, col);
        return grid[row - 1][col - 1] &&
                unionData.find(0) == unionData.find(xyTo1D(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpen;
    }

    // does the system percolate?
    public boolean percolates() {
        return unionData.find(0) == unionData.find(n * n + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 10;
        Percolation percolationModel = new Percolation(n);
        StdOut.println(percolationModel.isOpen(n, n));
        StdOut.println(percolationModel.isFull(n, n));
        StdOut.println(percolationModel.numberOfOpenSites());
        for (int i = 0; i < n; i++)
            percolationModel.open(i + 1, n);
        StdOut.println(percolationModel.isOpen(n, n));
        StdOut.println(percolationModel.isFull(n, n));
        StdOut.println(percolationModel.numberOfOpenSites());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                StdOut.print(percolationModel.grid[i][j] + " ");
            }
            StdOut.println();
        }
        StdOut.println("Percolate? - " + percolationModel.percolates());
    }

}
