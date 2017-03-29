import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

	private WeightedQuickUnionUF unionFind;
	private boolean siteStatus[];
	private final int virtualTopSite;
	private final int virtualBottomSite;
	private final int n;
	private final int size;
	
	
	public Percolation(int n) {               // create n-by-n grid, with all sites blocked
		if (n <= 0)
			throw new IllegalArgumentException();

		this.n = n;
		size = n * n;
		
		// Represent grid as array with two virtual nodes on top and bottom. 
		unionFind = new WeightedQuickUnionUF(size + 2);
		
		// Initialize status array
		siteStatus = new boolean [size + 2];
		for (int i = 0; i < siteStatus.length; i++)
			siteStatus[i] = false;

		// Store virtual indexes
		virtualTopSite = size;
		virtualBottomSite = virtualTopSite + 1;
		
		// Open Virtual Sites at the beginning
		siteStatus[virtualTopSite] = true;
		siteStatus[virtualBottomSite] = true;
	}
	
	public void open(int row, int col) {     // open site (row, col) if it is not open already
		if (isOpen(row, col))
			return;
			
		int openedSite = (row - 1) * n  + (col - 1);
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
	
	public boolean isOpen(int row, int col) {// is site (row, col) open?
		if ((row <= 0 || row > n) ||
			(col <= 0 || col > n))
			throw new IllegalArgumentException();
		
		return siteStatus[(row - 1) * n + (col - 1)];
	}
	
	public boolean isFull(int row, int col) {// is site (row, col) full?
		
		return !isOpen(row, col);
	}
	
	public int numberOfOpenSites() {         // number of open sites
		int numberOfOpenedSites = 0;
		
		for (int i = 0; i < size; i++)
			if (siteStatus[i])
				numberOfOpenedSites++;
		
		return numberOfOpenedSites;
	}
	
	public boolean percolates() {            // does the system percolate?
		
		return unionFind.connected(virtualTopSite, virtualBottomSite);
	}
	
	private void connect(int i, int j)
	{
		try {
			if (siteStatus[i] && siteStatus[j])
				unionFind.union(i, j);
		}
		catch (Exception e)	{
			// its OK, nothing to worry about
		}
	}
}