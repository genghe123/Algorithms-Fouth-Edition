package Eight_Puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private Node last = null;

    public Solver(Board initial) {
        if (initial == null) throw new java.lang.IllegalArgumentException();
        Node root = new Node(null, null, 0, false);

        MinPQ<Node> minManhattan = new MinPQ<>();
        minManhattan.insert(new Node(root, initial, 0, false));
        minManhattan.insert(new Node(root, initial.twin(), 0, true));
        while (!minManhattan.isEmpty()) {
            Node node = minManhattan.delMin();

            if (!node.isGoal()) {
                for (Board neighborBoard : node.board.neighbors()) {
                    if (!neighborBoard.equals(node.preNode.board))
                        minManhattan.insert(new Node(node, neighborBoard, node.moves + 1, node.isTwin));
                }
            } else {
                if (!node.isTwin)
                    last = node;
                break;
            }
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

    public boolean isSolvable() {
        return last != null;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        return last.moves;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> stack = new Stack<>();
        Node tempLast = last;
        while (tempLast.preNode != null) {
            stack.push(tempLast.board);
            tempLast = tempLast.preNode;
        }
        assert stack.size() == moves() + 1;
        return stack;
    }

    private class Node implements Comparable<Node> {
        private final Node preNode;
        private final Board board;
        private final int moves;
        private final boolean isTwin;

        private final int manhattan;

        private Node(Node preNode, Board board, int moves, boolean isTwin) {
            this.preNode = preNode;
            this.board = board;
            this.moves = moves;
            this.isTwin = isTwin;
            if (this.board == null)
                this.manhattan = 0;
            else this.manhattan = board.manhattan();
        }

        private boolean isGoal() {
            return board.manhattan() == 0;
        }

        @Override
        public int compareTo(Solver.Node that) {
            int compare = Integer.compare(this.manhattan + this.moves, that.manhattan + that.moves);
            if (compare != 0) return compare;
            else return Integer.compare(this.manhattan, that.manhattan);
        }
    }
}
