package percolation;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private int n;
    private double[] percolationThreshold;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();

        this.n = n;
        percolationThreshold = new double[trials];

        perform();
    }

    public double mean() {
        return StdStats.mean(percolationThreshold);
    }

    public double stddev() {
        return StdStats.stddev(percolationThreshold);
    }

    public double confidenceLo() {
        return mean() - (1.96 * stddev() / Math.sqrt(percolationThreshold.length));
    }

    public double confidenceHi() {
        return mean() + (1.96 * stddev() / Math.sqrt(percolationThreshold.length));
    }

    private int performTest() {
        int site = 0;
        Percolation percolation = new Percolation(n);

        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, n + 1);
            int col = StdRandom.uniform(1, n + 1);
            
            if (!percolation.isOpen(row, col)) {
                percolation.open(row, col);
                site++;
            }
        }

        return site;
    }

    private void perform() {
        for (int i = 0; i < percolationThreshold.length; i++)
            percolationThreshold[i] = (double) performTest() / (double) (n * n);
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats pStats = new PercolationStats(n, trials);

        System.out.print("mean                    = " + pStats.mean() + "\n");
        System.out.print("stddev                  = " + pStats.stddev() + "\n");
        System.out.print("95% confidence interval = [" + pStats.confidenceLo() + ", " + pStats.confidenceHi() + "]\n");

    }
}