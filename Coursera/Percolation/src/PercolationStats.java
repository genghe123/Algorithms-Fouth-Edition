import edu.princeton.cs.algs4.Accumulator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats
{
    private final Accumulator accumulator;

    public PercolationStats(int n,int trials)
    {
        if (trials<=0) throw new java.lang.IllegalArgumentException();

        accumulator=new Accumulator();
        Percolation percolation;

        for (int i=1;i<=trials;i++)
        {
            percolation=new Percolation(n);
            while (!percolation.percolates())
            {
                percolation.open(StdRandom.uniform(1,n+1),StdRandom.uniform(1,n+1));
            }
            accumulator.addDataValue((double)percolation.numberOfOpenSites()/n/n);
        }
    }

    public double mean()
    {
        StdStats.mean(new int[] {1});
        return accumulator.mean();
    }

    public double stddev()
    {
        StdStats.stddev(new int[] {1});
        return accumulator.stddev();
    }

    public double confidenceLo() {return mean()-1.96*stddev()/Math.sqrt(accumulator.count());}

    public double confidenceHi() {return mean()+1.96*stddev()/Math.sqrt(accumulator.count());}

    public static void main(String[] args)
    {
        int n=Integer.parseInt(args[0]);
        int trials=Integer.parseInt(args[1]);
        PercolationStats percolationStats=new PercolationStats(n,trials);
        System.out.printf("mean                     = %f\n",percolationStats.mean());
        System.out.printf("stddev                   = %f\n",percolationStats.stddev());
        System.out.printf("95%% confidence interval = [%f,%f]\n",
                percolationStats.confidenceLo(),percolationStats.confidenceHi());
    }
}
