import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
  private static final double CONFIDENCE_95 = 1.96;
  private final double mean;
  private final double stddev;
  private final double confidenceLo;
  private final double confidenceHi;

  // perform independent trials on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    double[] testResults = new double[trials];
    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);
      int totalCount = n * n;
      for (int k = 0; k < totalCount; k++) {
        int row, col;
        do {
          row = StdRandom.uniform(n) + 1;
          col = StdRandom.uniform(n) + 1;
        } while (percolation.isOpen(row, col));
        percolation.open(row, col);
        if (percolation.percolates()) {
          break;
        }
      }
      testResults[i] = (double) percolation.numberOfOpenSites() / totalCount;
    }
    this.mean = StdStats.mean(testResults);
    this.stddev = StdStats.stddev(testResults);
    this.confidenceLo = this.mean - (PercolationStats.CONFIDENCE_95 * this.stddev / Math.sqrt(trials));
    this.confidenceHi = this.mean + (PercolationStats.CONFIDENCE_95 * this.stddev / Math.sqrt(trials));
  }

  // sample mean of percolation threshold
  public double mean() {
    return this.mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return this.stddev;
  }

  // low endpoint of 95% confidence interval
  public double confidenceLo() {
    return this.confidenceLo;
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return this.confidenceHi;
  }

  // test client (see below)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);
    PercolationStats percolationStats = new PercolationStats(n, trials);
    System.out.println("mean                    = " + percolationStats.mean());
    System.out.println("stddev                  = " + percolationStats.stddev());
    System.out.println(
        "95% confidence interval = [" + percolationStats.confidenceLo() + ", " + percolationStats.confidenceHi() + "]");
  }
}