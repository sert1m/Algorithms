import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF unionFind;
    private boolean[] siteStatus;
    private final int virtualTopSite;
    private final int virtualBottomSite;
    private final int n;
    private final int size;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        size = n * n;

        // Represent grid as array with two virtual nodes on top and bottom.
        unionFind = new WeightedQuickUnionUF(size + 2);

        // Initialize status array
        siteStatus = new boolean[size + 2];
        for (int i = 0; i < siteStatus.length; i++)
            siteStatus[i] = false;

        // Store virtual indexes
        virtualTopSite = size;
        virtualBottomSite = virtualTopSite + 1;

        // Open Virtual Sites at the beginning
        siteStatus[virtualTopSite] = true;
        siteStatus[virtualBottomSite] = true;
    }

    public void open(int row, int col) {
        if (isOpen(row, col))
            return;

        int openedSite = convert(row, col);
        siteStatus[openedSite] = true;

        if (col != 1)
            connect(openedSite, openedSite - 1);
        if (col != n)
            connect(openedSite, openedSite + 1);
        if (row != 1)
            connect(openedSite, openedSite - n);
        if (row != n)
            connect(openedSite, openedSite + n);

        if (row == 1)
            connect(openedSite, virtualTopSite);
        if (row == n)
            connect(openedSite, virtualBottomSite);
    }

    public boolean isOpen(int row, int col) {
        return siteStatus[convert(row, col)];
    }

    public boolean isFull(int row, int col) {
        int site = convert(row, col);
        
        return unionFind.connected(site, virtualTopSite);
    }

    public int numberOfOpenSites() {
        int numberOfOpenedSites = 0;

        for (int i = 0; i < size; i++)
            if (siteStatus[i])
                numberOfOpenedSites++;

        return numberOfOpenedSites;
    }

    public boolean percolates() {
        return unionFind.connected(virtualTopSite, virtualBottomSite);
    }

    private void connect(int i, int j) {
        if (siteStatus[i] && siteStatus[j])
            unionFind.union(i, j);
    }

    private int convert(int row, int col) {
        if ((row <= 0 || row > n) || (col <= 0 || col > n))
            throw new IllegalArgumentException();

        return (row - 1) * n + (col - 1);
    }
}