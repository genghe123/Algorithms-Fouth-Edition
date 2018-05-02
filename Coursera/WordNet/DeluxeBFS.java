package WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

public class DeluxeBFS {
    private int[] distTo;

    public DeluxeBFS(Digraph G, Iterable<Integer> v) {
        if (G == null || v == null) throw new IllegalArgumentException();
        distTo = new int[G.V()];
        for (int i = 0; i < G.V(); i++) {
            distTo[i] = Integer.MAX_VALUE;
        }

        Queue<Integer> queue = new Queue<>();

        for (int i : v) {
            if (i < 0 || i >= G.V()) throw new IllegalArgumentException();
            queue.enqueue(i);
            distTo[i] = 0;
        }

        while (!queue.isEmpty()) {
            int s = queue.dequeue();
            for (int n : G.adj(s)) {
                if (distTo[n] == Integer.MAX_VALUE) {
                    distTo[n] = distTo[s] + 1;
                    queue.enqueue(n);
                }
            }
        }
    }

    public int distTo(int v) {
        if (v < 0 || v >= distTo.length) throw new IllegalArgumentException();
        return distTo[v];
    }
}
