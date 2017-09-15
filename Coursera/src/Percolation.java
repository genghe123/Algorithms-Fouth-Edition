import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
    private final int grid;
    private final WeightedQuickUnionUF backWash;
    private final WeightedQuickUnionUF sites;
    private boolean[] status;
    private int numofOpenSites= 0;

    public Percolation(int n)
    {
        if (n <= 0) throw new java.lang.IllegalArgumentException();
        grid = n;

        backWash = new WeightedQuickUnionUF(grid * grid +2);
        sites = new WeightedQuickUnionUF(grid * grid +1);
        status = new  boolean[grid * grid +1];

        for (int i = 1; i <= grid; i++)
            backWash.union(0,i);

        int virtualbottom = grid * grid +1;
        for (int i = grid * grid - grid +1; i < virtualbottom; i++)
            backWash.union(virtualbottom,i);
    }

    public void open(int row,int col)
    {
        if (!isOpen(row, col))
        {
            int coordination = xyTo1D(row,col);
            status[coordination] = true;
            numofOpenSites++;

            if (row == 1)
            {
                backWash.union(0,coordination);
                sites.union(0,coordination);
            }
            if (row == grid)
            {
                backWash.union(grid * grid +1,coordination);
            }
            if (row > 1 && isOpen(row-1,col))
            {
                backWash.union(xyTo1D(row-1,col),coordination);
                sites.union(xyTo1D(row-1,col),coordination);

            }
            if (row < grid && isOpen(row+1,col))
            {
                backWash.union(coordination, xyTo1D(row+1,col));
                sites.union(coordination, xyTo1D(row+1,col));
            }
            if (col > 1 && isOpen(row,col-1))
            {
                backWash.union(coordination, xyTo1D(row,col-1));
                sites.union(coordination, xyTo1D(row,col-1));
            }
            if (col < grid && isOpen(row,col+1))
            {
                backWash.union(xyTo1D(row,col+1),coordination);
                sites.union(xyTo1D(row,col+1),coordination);
            }
        }
    }

    public boolean isOpen(int row,int col)    { return status[xyTo1D(row, col)]; }

    public boolean isFull(int row,int col)
    { return sites.connected(0, xyTo1D(row, col)); }

    public int numberOfOpenSites()    { return numofOpenSites; }

    public boolean percolates()
    {
        return (grid != 1)?backWash.connected(0, grid * grid +1): sites.connected(0,1);
    }

    private int xyTo1D(int row, int col)
    {
        if (Math.max(row, col) > grid || Math.min(row, col) < 1) throw new IllegalArgumentException();
        return (row-1)*grid+col;
    }

    public static void main(String[] args)
    {
        int n=Integer.parseInt(args[0]);
        Percolation percolation=new Percolation(n);
        while (!percolation.percolates())
        {
            percolation.open(StdRandom.uniform(1,n+1),StdRandom.uniform(1,n+1));
        }
        StdOut.println(percolation.numberOfOpenSites());
    }
}