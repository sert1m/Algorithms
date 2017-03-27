import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	
	int n;
	int trials;
	double percolationThreshold[];
	
	public PercolationStats(int n, int trials) {   // perform trials independent experiments on an n-by-n grid
		if (n <= 0 || trials <= 0)
			throw new IllegalArgumentException();
		
		this.n = n;
		this.trials = trials;
		
		percolationThreshold = new double[trials];
		
		perform();
	}
	public double mean() {                         // sample mean of percolation threshold
		
		return StdStats.mean(percolationThreshold);
	}
	public double stddev() {                       // sample standard deviation of percolation threshold
		
		return StdStats.stddev(percolationThreshold);
	}
	public double confidenceLo() {                 // low  endpoint of 95% confidence interval
		
		return mean() - (1.96 * stddev() / Math.sqrt(trials));
	}
	public double confidenceHi() {                 // high endpoint of 95% confidence interval
		
		return mean() + (1.96 * stddev() / Math.sqrt(trials));
	}
	
	private int performTest() {
		int site = 0;
		Percolation percolation = new Percolation(n);
		
		while (site < n * n) {
			int row = StdRandom.uniform(1, n + 1);
			int col = StdRandom.uniform(1, n + 1);
			
			if (percolation.isFull(row, col)) {
				percolation.open(row, col);
				site++;
				
				if (percolation.percolates())
					break;
			}
		}
		
		return site;
	}
	
	private void perform() {
		for (int i = 0; i < percolationThreshold.length; i++)
			percolationThreshold[i] = (double)performTest() / (n * n);
	}
	
	public static void main(String[] args) {       // test client (described below)
	   
		int n = Integer.parseInt(args[0]);
		int trials = Integer.parseInt(args[1]);
		
		PercolationStats pStats = new PercolationStats(n, trials);
		
		System.out.print("mean                    = " + pStats.mean() + "\n");
		System.out.print("stddev                  = " + pStats.stddev() + "\n");
		System.out.print("95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]\n");
	   
	}
}