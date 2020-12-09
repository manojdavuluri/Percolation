import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
  private boolean[][] sites;
  private int openedSiteCount;
  private boolean[] connectToBottom;
  private final int n;
  private final WeightedQuickUnionUF uf;
  private boolean percolated;

  // creates n-by-n grid, with all sites initially blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }
    this.n = n;
    this.sites = new boolean[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        this.sites[i][j] = false;
      }
    }
    int ufLength = n * n + 1;
    this.connectToBottom = new boolean[ufLength];
    for (int i = 0; i < ufLength; i++) {
      this.connectToBottom[i] = false;
    }
    this.openedSiteCount = 0;
    uf = new WeightedQuickUnionUF(ufLength);
    this.percolated = false;
  }

  private void validateIndices(int row, int col) {
    if (row < 1 || row > this.n || col < 1 || col > this.n) {
      throw new IllegalArgumentException();
    }
  }

  // opens the site (row, col) if it is not open already
  public void open(int row, int col) {
    this.validateIndices(row, col);
    if (this.isOpen(row, col)) {
      return;
    }
    this.sites[row - 1][col - 1] = true;
    this.openedSiteCount++;
    int nowUnionIndex = this.getRealUnionIndex(row, col);
    boolean isBottom = false;
    if (row - 1 >= 1 && this.isOpen(row - 1, col)) {
      int realUnionIndex = this.getRealUnionIndex(row - 1, col);
      if (connectToBottom[this.uf.find(realUnionIndex)]) {
        isBottom = true;
      }
      uf.union(nowUnionIndex, realUnionIndex);
    }
    if (row + 1 <= this.n && this.isOpen(row + 1, col)) {
      int realUnionIndex = this.getRealUnionIndex(row + 1, col);
      if (connectToBottom[this.uf.find(realUnionIndex)]) {
        isBottom = true;
      }
      uf.union(nowUnionIndex, realUnionIndex);
    }
    if (col - 1 >= 1 && this.isOpen(row, col - 1)) {
      int realUnionIndex = this.getRealUnionIndex(row, col - 1);
      if (connectToBottom[this.uf.find(realUnionIndex)]) {
        isBottom = true;
      }
      uf.union(nowUnionIndex, realUnionIndex);
    }
    if (col + 1 <= this.n && this.isOpen(row, col + 1)) {
      int realUnionIndex = this.getRealUnionIndex(row, col + 1);
      if (connectToBottom[this.uf.find(realUnionIndex)]) {
        isBottom = true;
      }
      uf.union(nowUnionIndex, realUnionIndex);
    }
    if (row == 1) {
      if (connectToBottom[this.uf.find(0)]) {
        isBottom = true;
      }
      uf.union(nowUnionIndex, 0);
    }
    int componentIndex = this.uf.find(nowUnionIndex);
    if (row == this.n || isBottom) {
      this.connectToBottom[componentIndex] = true;
    }
    if (!this.percolates()) {
      if (this.connectToBottom[componentIndex] && this.uf.connected(0, componentIndex)) {
        this.percolated = true;
      }
    }
  }

  private int getRealUnionIndex(int row, int col) {
    return this.n * (row - 1) + col;
  }

  // is the site (row, col) open?
  public boolean isOpen(int row, int col) {
    this.validateIndices(row, col);
    return this.sites[row - 1][col - 1];
  }

  // is the site (row, col) full?
  public boolean isFull(int row, int col) {
    this.validateIndices(row, col);
    int ufIndex = this.getRealUnionIndex(row, col);
    return this.uf.connected(ufIndex, 0);
  }

  // returns the number of open sites
  public int numberOfOpenSites() {
    return this.openedSiteCount;
  }

  // does the system percolate?
  public boolean percolates() {
    return this.percolated;
  }

  // test client (optional)
  public static void main(String[] args) {
    int n = Integer.parseInt(args[0]);
    Percolation percolation = new Percolation(n);
    for (int i = 0; i < n * n; i++) {
      int row, col;
      do {
        row = StdRandom.uniform(n) + 1;
        col = StdRandom.uniform(n) + 1;
      } while (percolation.isOpen(row, col));
      System.out.println(row + ", " + col);
      percolation.open(row, col);
      if (percolation.percolates()) {
        System.out.println("percolated");
      }
    }
    System.out.println("numberOfOpenSites: " + percolation.numberOfOpenSites());
  }
}