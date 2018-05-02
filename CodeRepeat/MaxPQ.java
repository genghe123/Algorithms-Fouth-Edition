import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Herman Geng
 * @<code>MaxPQ</code> class represents a priority queue of generic keys.
 * It supports the usual <em>insert</em> and <em>delete-the-maximum</em>
 * operations, along with methods for peeking at the maximum keys,
 * testing if the priority queue is empty, and iterating through the keys.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class MaxPQ<Key> implements Iterable<Key> {
    private Key[] pq;
    private int n;
    private Comparator comparator;

    public MaxPQ(int initCapacity) {
        pq = (Key[]) new Object[initCapacity + 1];
        n = 0;
    }

    public MaxPQ() {
        this(1);
    }

    public MaxPQ(int initCapacity, Comparator<Key> comparator) {
        this(initCapacity);
        this.comparator = comparator;
    }

    public MaxPQ(Key[] keys) {
        this(keys.length + 1);
        n = keys.length;
        for (int i = 0; i < n; i++)
            pq[i + 1] = keys[i];

        for (int i = n / 2; i >= 1; i--)
            sink(i);
        assert isMaxHeap();
    }

    public static void main(String args[]) {
        edu.princeton.cs.algs4.MaxPQ<String> pq = new edu.princeton.cs.algs4.MaxPQ<String>();
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            if (!item.equals("-")) pq.insert(item);
            else if (!pq.isEmpty()) StdOut.print(pq.delMax() + " ");
        }
        StdOut.println("(" + pq.size() + " left on pq)");
    }

    public void insert(Key x) {
        if (n >= pq.length - 1) resize(2 * pq.length);
        pq[++n] = x;
        swim(n);
        assert isMaxHeap();
    }

    public Key max() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    public Key delMax() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        HelperFunc.exch(pq, 1, n);
        Key max = pq[n--];
        pq[n + 1] = null;
        sink(1);
        if (n > 0 && n <= (pq.length - 1) / 4) resize(pq.length / 2);
        assert isMaxHeap();
        return max;
    }

    private boolean less(int i, int j) {
        if (comparator == null)
            return HelperFunc.less((Comparable<Key>) pq[i], (Comparable<Key>) pq[j]);
        return HelperFunc.less(comparator, pq[i], pq[j]);
    }

    private boolean isEmpty() {
        return n == 0;
    }

    private int size() {
        return n;
    }

    private void resize(int capacity) {
        assert capacity > n;
        Key[] temp = (Key[]) new Object[capacity];
        for (int i = 1; i <= n; i++)
            temp[i] = pq[i];
        pq = temp;
    }

    private void sink(int i) {
        assert i <= n && i >= 1;
        for (int j = 2 * i; j <= n; ) {
            if (j < n && less(j, j + 1)) j++;
            if (!less(i, j)) break;
            HelperFunc.exch(pq, i, j);
            i = j;
        }
    }

    private void swim(int i) {
        assert i <= n && i >= 1;
        while (i > 1 && less(i / 2, i)) {
            HelperFunc.exch(pq, i, i / 2);
            i = i / 2;
        }
    }

    private boolean isMaxHeap() {
        return isMaxHeap(1);
    }

    private boolean isMaxHeap(int i) {
        if (i > n) return true;
        int left = 2 * i;
        int right = 2 * i + 1;
        if (left <= n && less(i, left)) return false;
        if (right <= n && less(i, right)) return false;
        return isMaxHeap(left) && isMaxHeap(right);

    }

    @Override
    public Iterator<Key> iterator() {
        return new HeapIterator();
    }

    private class HeapIterator implements Iterator<Key> {
        private MaxPQ<Key> copy;

        public HeapIterator() {
            copy = new MaxPQ<>(pq);
            if (comparator != null) copy.comparator = comparator;
        }

        @Override
        public boolean hasNext() {
            return !copy.isEmpty();
        }

        @Override
        public Key next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMax();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
