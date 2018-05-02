import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Herman Geng
 * @<code>MinPQ</code> class represents a priority queue of generic keys.
 * It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 * operations, along with methods for peeking at the minimum keys,
 * testing if the priority queue is empty, and iterating through the keys.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class MinPQ<Key> implements Iterable<Key> {
    private Key[] pq;   // store items at indices 1 to n
    private int n;      // number of items on priority queue
    private Comparator<Key> comparator; //optional comparator

    public MinPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        n = 0;
    }

    public MinPQ() {
        this(1);
    }

    public MinPQ(int initCapacity, Comparator<Key> comparator) {
        this(initCapacity);
        this.comparator = comparator;
    }

    public MinPQ(Key[] keys) {
        this(keys.length + 1);
        n = keys.length;
        for (int i = 0; i < n; i++)
            pq[i + 1] = keys[i];

        for (int i = n / 2; i >= 1; i--)
            sink(i);
        assert isMinHeap();
    }

    public static void main(String args[]) {
        MinPQ<String> pq = new MinPQ<>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMin() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }

    public boolean isEmpty() {
        return n == 0;
    }

    public int size() {
        return n;
    }

    public void insert(Key x) {
        if (n == pq.length - 1) resize(2 * pq.length);
        pq[++n] = x;
        swim(n);
        assert isMinHeap();
    }

    public Key min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    public Key delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        HelperFunc.exch(pq, 1, n);
        Key min = pq[n--];
        pq[n + 1] = null;
        sink(1);
        if (n > 0 && n <= (pq.length - 1) / 4) resize(pq.length / 2);
        assert isMinHeap();
        return min;
    }

    private void resize(int capacity) {
        assert capacity > n;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= n; i++)
            temp[i] = pq[i];
        pq = temp;
    }

    private void sink(int i) {
        for (int j = 2 * i; j <= n; ) {
            if (j < n && !less(j, j + 1)) j++;
            if (less(i, j)) break;
            HelperFunc.exch(pq, i, j);
            i = j;
        }
    }

    private void swim(int i) {
        while (i > 1 && !less(i / 2, i)) {
            HelperFunc.exch(pq, i / 2, i);
            i = i / 2;
        }
    }

    private boolean less(int i, int j) {
        if (comparator == null)
            return HelperFunc.less((Comparable<Key>) pq[i], (Comparable<Key>) pq[j]);
        else
            return HelperFunc.less(comparator, pq[i], pq[j]);
    }

    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    private boolean isMinHeap(int i) {
        if (i > n) return true;
        int left = 2 * i;
        int right = 2 * i + 1;
        if (left <= n && less(left, i)) return false;
        if (right <= n && less(right, i)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }

    @Override
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        private MinPQ<Key> copy;

        public HeapIterator() {
            if (comparator == null) copy = new MinPQ<>(size());
            else copy = new MinPQ<>(size(), comparator);
            for (int i = 0; i < n; i++)
                copy.insert(pq[i]);
        }

        @Override
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        @Override
        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
