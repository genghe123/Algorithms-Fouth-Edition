package Eight_Puzzle;

import java.util.ArrayList;
import java.util.Arrays;

public class Board {
    private int n;
    private int blankPosition;
    private int[] tiles;
    private int hamming;
    private int manhattan;

    public Board(int[][] blocks) {
        if (blocks == null) throw new java.lang.IllegalArgumentException("Null tiles");
        n = blocks.length;
        if (n <= 0) throw new java.lang.IllegalArgumentException();
        tiles = new int[n * n];

        InitialParameters(blocks);
    }

    /**
     * Return a new Board copied from given Board object
     *
     * @param that specified Board to be copied
     */
    private Board(Board that) {
        if (that == null) throw new java.lang.NullPointerException();
        this.n = that.n;
        this.blankPosition = that.blankPosition;
        this.hamming = that.hamming;
        this.manhattan = that.manhattan;
        this.tiles = Arrays.copyOf(that.tiles, that.tiles.length);
    }

    private void InitialParameters(int[][] blocks) {
        for (int i = 0; i < n; i++)
            if (blocks[i].length != n) throw new java.lang.IllegalArgumentException("Not a n*n matrix");
            else for (int j = 0; j < n; j++)
                if (blocks[i][j] < 0 || blocks[i][j] >= n * n)
                    throw new java.lang.IllegalArgumentException("Value at " + blocks[i][j] + " is over limit");
                else {
                    int position = i * n + j;
                    int value = blocks[i][j];
                    tiles[position] = value;

                    if (value == 0) blankPosition = position;
                    else if (value != position + 1) {
                        hamming++;
                        manhattan += Math.abs(i - (value - 1) / n) + Math.abs(j - (value - 1) % n);
                    }
                }
    }

    private int hammingNumber(int position) {
        return tiles[position] != 0 && tiles[position] != position + 1 ? 1 : 0;
    }

    private int manhattanNumber(int position) {
        if (tiles[position] == 0) return 0;
        int i = position / n, j = position % n;
        return Math.abs(i - (tiles[position] - 1) / n) + Math.abs(j - (tiles[position] - 1) % n);
    }

    private void recalculateHammingNumber() {
        hamming = 0;
        for (int i = 0; i < n * n; i++)
            hamming += hammingNumber(i);
    }

    private void recalculateManhattanNumber() {
        manhattan = 0;
        for (int i = 0; i < n * n; i++)
            manhattan += manhattanNumber(i);
    }

    public int dimension() {
        return n;
    }

    public int hamming() {
        return hamming;
    }

    public int manhattan() {
        return manhattan;
    }

    public boolean isGoal() {
        return hamming == 0;
    }

    public Board twin() {
        //Test need check that Board is immutable by testing whether methods
        //return the same value, regardless of order in which called
        //So we fix the positions exchanges
        if (blankPosition == 0 || blankPosition == 1) return twin(2, 3);
        else return twin(0, 1);
    }

    private Board twin(int p, int q) {
        //if p or q is out of range,return null
        //if p equals q, return null,
        if (p >= n * n || p < 0 || q >= n * n || q < 0 || p == q) return null;

        Board that = new Board(this);

        that.tiles[p] = this.tiles[q];
        that.tiles[q] = this.tiles[p];

        if (p == this.blankPosition || q == this.blankPosition) {
            if (p == this.blankPosition) that.blankPosition = q;
            else that.blankPosition = p;
            that.recalculateHammingNumber();
            that.recalculateManhattanNumber();
        } else {
            that.hamming = this.hamming - this.hammingNumber(p) - this.hammingNumber(q)
                    + that.hammingNumber(p) + that.hammingNumber(q);
            that.manhattan = this.hamming - this.manhattanNumber(p) - this.manhattanNumber(q)
                    + that.manhattanNumber(p) + that.manhattanNumber(q);
        }

        return that;
    }

    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (this.n != that.n || this.manhattan != that.manhattan || this.hamming != that.hamming) return false;
        for (int i = 0; i < n * n; i++)
            if (Integer.compare(this.tiles[i], that.tiles[i]) != 0) return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        ArrayList<Board> boards = new ArrayList<>(4);
        int[] neighborPositions =
                {blankPosition - n, blankPosition + n,
                        (blankPosition % n - 1) < 0 ? -1 : blankPosition - 1,
                        (blankPosition % n + 1) >= n ? -1 : blankPosition + 1};
        for (int neighborPosition : neighborPositions) {
            Board neighbor = twin(blankPosition, neighborPosition);
            if (neighbor != null) boards.add(neighbor);
        }
        return boards;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i * n + j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
}
