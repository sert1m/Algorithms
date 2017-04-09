import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private WeightedQuickUnionUF unionFind;
    private WeightedQuickUnionUF unionFindFull;
    private boolean[] siteStatus;
    private final int virtualTopSite;
    private final int virtualBottomSite;
    private final int n;
    private final int size;
    private int numberOfOpenedSites;

    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        size = n * n;

        // Represent grid as array with two virtual nodes on top and bottom.
        unionFind = new WeightedQuickUnionUF(size + 2);
        unionFindFull = new WeightedQuickUnionUF(size + 1);

        // Initialize status array
        siteStatus = new boolean[size + 2];
        for (int i = 0; i < siteStatus.length; i++)
            setBlocked(i);

        // Store virtual indexes
        virtualTopSite = size;
        virtualBottomSite = size + 1;

        // Open Virtual Sites at the beginning
        setOpen(virtualTopSite);
        setOpen(virtualBottomSite);
        // Reset
        numberOfOpenedSites = 0;
    }

    public void open(int row, int col) {
        int site = convert(row, col);
        
        if (isOpen(site))
            return;
        setOpen(site);

        if (col != 1)
            connect(site, site - 1);
        if (col != n)
            connect(site, site + 1);
        
        if (row != 1)
            connect(site, site - n);
        else
            connect(site, virtualTopSite);
            
        if (row != n)
            connect(site, site + n);
        else
            connect(site, virtualBottomSite);
    }

    public boolean isOpen(int row, int col) {
        return isOpen(convert(row, col));
    }

    public boolean isFull(int row, int col) {
        return unionFindFull.connected(convert(row, col), virtualTopSite);
    }

    public int numberOfOpenSites() {
        return numberOfOpenedSites;
    }

    public boolean percolates() {
        return unionFind.connected(virtualTopSite, virtualBottomSite);
    }

    private void connect(int i, int j) {
        if (isOpen(i) && isOpen(j)) {
            unionFind.union(i, j);
            if (i != virtualBottomSite && j != virtualBottomSite)
                unionFindFull.union(i, j);
        }
    }

    private int convert(int row, int col) {
        if ((row <= 0 || row > n) || (col <= 0 || col > n))
            throw new IndexOutOfBoundsException();

        return (row - 1) * n + (col - 1);
    }

    private boolean isOpen(int i) {
        return siteStatus[i];
    }

    private boolean isBlocked(int i) {
        return !siteStatus[i];
    }

    private void setOpen(int i) {
        siteStatus[i] = true;
        numberOfOpenedSites++;
    }

    private void setBlocked(int i) {
        siteStatus[i] = false;
    }
}