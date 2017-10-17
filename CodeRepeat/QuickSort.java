import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;
import org.jetbrains.annotations.Contract;

/**
 * The {@code Quick} class provides static methods for sorting an
 * array and selecting the ith smallest element in an array using quicksort.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 *
 * @author Herman Geng
 */
public class QuickSort {
	//This class should not be instantiated.
	private QuickSort() {
	}

	/**
	 * Rearranges the array in ascending order, using the natural order.
	 *
	 * @param a the array to be sorted.
	 */
	@Contract( "null -> fail" )
	public static void ordinarySort(Comparable[] a) {
		assert a != null;

		Stopwatch stopwatch = new Stopwatch();
		StdRandom.shuffle(a);
		ordinarySort(a, 0, a.length - 1);
		assert HelperFunc.isSorted(a);
		System.out.println(stopwatch.elapsedTime());
	}

	@Contract( "null, _, _ -> fail" )
	private static void ordinarySort(Comparable[] a, int lo, int hi) {
		assert a != null;
		assert lo >= 0 && hi < a.length;

		if ( lo >= hi ) return;
		int j = partition(a, lo, hi);
		ordinarySort(a, lo, j - 1);
		ordinarySort(a, j + 1, hi);

		HelperFunc.isSorted(a);
	}

	//Partition the sub-array a[lo .. hi] so that a[lo .. j-1] <= a[j] <= a[j+1 .. hi]
	//and return the index j.
	private static int partition(Comparable[] a, int lo, int hi) {
		//a[lo] is a probe.
		int i = lo + 1, j = hi;
		while ( true ) {
			while ( i <= j && HelperFunc.less(a[i], a[lo]) ) {
				i++;
				break;
			}
			while ( i <= j && HelperFunc.less(a[lo], a[j]) ) {
				j--;
				break;
			}

			if ( i >= j ) break;
			HelperFunc.exch(a, i, j);
		}
		//Put the probe a[lo] at a[j].Pay attention to the position exchanged with probe is "j".
		//Statement when looping out is "j <= i".
		//Position before "i" (not include itself) means no more than probe. Position after "j" (not include iteself) means no less than probe.
		//So a[j] <= a[lo](probe) <= a[i].
		HelperFunc.exch(a, lo, j);
		return j;
	}

	/**
	 * Rearranges the array so that {@code a[k]} contains the kth smallest key;
	 * {@code a[0]} through {@code a[k-1]} are less than (or equals to) {@code a[k]}; and
	 * {@code a[k+1]} through {@code a[n-1]} are greater than (or equal to) {@code a[k]}.
	 *
	 * @param a the array
	 * @param k the rank of the key
	 * @return the key of rank {@code k}
	 */
	@Contract( "null, _ -> fail" )
	public static Comparable select(Comparable[] a, int k) {
		assert a != null;
		if ( k < 0 || k >= a.length ) throw new IndexOutOfBoundsException("Selected elements out of bounds.");

		StdRandom.shuffle(a);
		int lo = 0, hi = a.length - 1;
		//TODO Understand codes below
		while ( lo < hi ) {
			int i = partition(a, lo, hi);
			if ( i > k ) hi = i - 1;
			else if ( i < k ) lo = i + 1;
			else return a[i];
		}
		return a[lo];
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		assert in.exists() && !in.isEmpty();
		String[] a = in.readAllStrings();
		QuickSort.ordinarySort(a);

		for ( String arg : a )
			System.out.println(arg);
	}
}