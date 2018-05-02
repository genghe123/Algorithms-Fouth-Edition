import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * @author Herman Geng
 * @<codeHeapSort</code> class provides static method for sorting an array using
 * Max or Min heap.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class HeapSort {
    //This class should not be instantiated/
    private HeapSort() {
    }

    /**
     * Rearranges the array in asceding order, using the natrual order.
     *
     * @param pq the array to be sorted.
     */
    public static void sort(Comparable[] pq) {
        int n = pq.length;
        for (int i = n / 2; i >= 1; i--)
            sink(pq, i, n);
        //TODO Understand
        while (n > 1) {
            exch(pq, 1, n--);
            sink(pq, 1, n);
        }
    }

    private static void sink(Comparable[] pq, int i, int n) {
        assert i <= n && i >= 0;
        for (int j = 2 * i; j <= n; i = j) {
            if (j < n && less(pq, j, j + 1)) j++;
            if (!less(pq, i, j)) break;
            exch(pq, i, j);
        }
        assert HelperFunc.isSorted(pq);
    }

    private static boolean less(Comparable[] pq, int i, int j) {
        return HelperFunc.less(pq[i - 1], pq[j - 1]);
    }

    private static void exch(Object[] pq, int i, int j) {
        Object swap = pq[i - 1];
        pq[i - 1] = pq[j - 1];
        pq[j - 1] = swap;
    }

    private static void show(Comparable[] a) {
        for (int i = 0; i < a.length; i++)
            System.out.println(a[i]);
    }

    public static void main(String[] args) {
        In file = new In(args[0]);
        int[] a = file.readAllInts();
        Integer[] b = new Integer[a.length];
        for (int i = 0; i < a.length; i++)
            b[i] = a[i];
        Stopwatch stopwatch = new Stopwatch();
        HeapSort.sort(b);
        show(b);
        System.out.println(stopwatch.elapsedTime());
    }

}
