import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] treshold;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        treshold = new double[trials];
        this.trials = trials;

        for (int i = 0; i < treshold.length; i++) {
            treshold[i] = computeTreshold(n);
        }
    }
    private double computeTreshold(int n) {
        int i, j;
        Percolation p = new Percolation(n);
        while (!p.percolates()) {
            i = StdRandom.uniform(n)+1;
            j = StdRandom.uniform(n)+1;
            if (!p.isOpen(i, j)) {
                p.open(i, j);
            }
        }
        return ((double)p.numberOfOpenSites())/(n*n);
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(treshold);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(treshold);
    }


    // low endpoint of 95% confidence interval
    public double confidenceLo(){
        return mean() - (1.96*stddev())/(Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi(){
        return mean() + (1.96*stddev())/(Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args){
        PercolationStats stats = new PercolationStats(100, 50);
        System.out.println("mean = " + stats.mean());
        System.out.println("stddev = " + stats.stddev());
        System.out.println("[95% confidence interval = " + stats.confidenceLo() + ", " + stats.confidenceHi()+"]");
    }

}
