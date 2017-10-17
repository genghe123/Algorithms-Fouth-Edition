import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;

/**
 * @author Herman Geng
 * @<code>InsertionSort</code> class provides static method for sorting an array using
 * different versions of insertion sort: ordinary, half-insertion with sentinel and binary-insertion.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class InsertionSort {
	//This class should not be instantiated.
	private InsertionSort() {
	}

	/**
	 * Rearranges the array in ascending order, using the natural order.
	 * Implements with ordinary insertion algorithms.
	 *
	 * @param array the array to be sorted.
	 */
	public static void ordinarySort(Comparable[] array) {
		ordinarySort(array, 0, array.length - 1);
	}

	/**
	 * Rearranges sub-array in ascending order, using the natural order.
	 * Implements with ordinary insertion algorithms.
	 *
	 * @param array the array to be sorted.
	 * @param lo    lower bound index of sub-array.
	 * @param hi    upper bound index of sub-array.
	 */
	@Contract( "null, _, _ -> fail" )
	public static void ordinarySort(Comparable[] array, int lo, int hi) {
		assert array != null;
		assert lo >= 0 && hi < array.length && lo <= hi;

		Stopwatch stopwatch = new Stopwatch();
		for ( int i = lo + 1; i <= hi; i++ ) {
			for ( int j = i; j > lo && HelperFunc.less(array[j], array[j - 1]); j-- )
				HelperFunc.exch(array, j, j - 1);
		}
		System.out.println(stopwatch.elapsedTime());
		assert HelperFunc.isSorted(array, lo, hi);
	}

	/**
	 * Rearranges the array in ascending order, using a comparator.
	 * Implements with ordinary insertion algorithms.
	 *
	 * @param array      the array to be sorted.
	 * @param comparator the comparator specifying the order
	 */
	public static void ordinarySort(Object[] array, Comparator comparator) {
		ordinarySort(array, 0, array.length - 1, comparator);
	}

	/**
	 * Rearranges the array in ascending order, using a comparator.
	 * Implements with ordinary insertion algorithms.
	 *
	 * @param array      the array to be sorted.
	 * @param lo         lower bound index of sub-array.
	 * @param hi         upper bound index of sub-array.
	 * @param comparator the comparator specifying the order
	 */
	public static void ordinarySort(Object[] array, int lo, int hi, Comparator comparator) {
		assert array != null;
		assert lo >= 0 && hi < array.length && lo <= hi;

		Stopwatch stopwatch = new Stopwatch();
		for ( int i = lo + 1; i <= hi; i++ ) {
			for ( int j = i; j > lo && HelperFunc.less(comparator, array[j], array[j - 1]); j-- )
				HelperFunc.exch(array, j, j - 1);
		}
		assert HelperFunc.isSorted(array, comparator, lo, hi);
		System.out.println(stopwatch.elapsedTime());
	}

	/**
	 * Rearranges the array in ascending order, using natural order.
	 * Implements with sentinel insertion algorithms.
	 *
	 * @param array the array to be sorted.
	 */
	public static void sentinelSort(Comparable[] array) {
		sentinelSort(array, 0, array.length - 1);
	}

	public static void sentinelSort(Comparable[] array, int lo, int hi) {
		assert array != null;
		assert lo >= 0 && hi < array.length && lo <= hi;

		Stopwatch stopwatch = new Stopwatch();
		//If exchanges equals zero, it means the array is sorted already.
		//If not, this operation puts smallest element in position to serve as sentinel.
		int exchanges = 0;
		for ( int i = hi; i > lo; i-- ) {
			if ( HelperFunc.less(array[i], array[i - 1]) ) {
				HelperFunc.exch(array, i, i - 1);
				exchanges++;
			}
		}
		if ( exchanges == 0 ) return;

		//Insertion sort with half-exchanges.
		// Shifting elements to right from end to front in the array instead of exchanging them
		//Stock array[i] as temporary variable
		for ( int i = lo + 1; i <= hi; i++ ) {
			Comparable temp = array[i];
			int j = i;

			//Use while statement cause array[lo] must be a smallest element as a sentinel
			while ( HelperFunc.less(temp, array[j - 1]) ) {
				array[j] = array[j - 1];
				j--;
			}
			array[j] = temp;
		}
		assert HelperFunc.isSorted(array, lo, hi);
		System.out.println(stopwatch.elapsedTime());
	}

	/**
	 * Rearranges the array in ascending order, using natural order.
	 * Implements with binary find-insertion algorithms.
	 *
	 * @param array the array to be sorted.
	 */
	public static void binarySort(Comparable[] array) {
		binarySort(array, 0, array.length - 1);
	}

	public static void binarySort(Comparable[] array, int lo, int hi) {
		assert array != null;
		assert lo >= 0 && hi < array.length && lo <= hi;

		Stopwatch stopwatch = new Stopwatch();

		int exchanges = 0;
		for ( int i = hi; i > lo; i-- ) {
			if ( HelperFunc.less(array[i], array[i - 1]) ) {
				HelperFunc.exch(array, i, i - 1);
				exchanges++;
			}
		}
		if ( exchanges == 0 ) return;

		for ( int i = lo + 1; i <= hi; i++ ) {
			Comparable temp = array[i];
			int low = lo, high = i;

			//Use while statement cause array[lo] must be smallest element as a sentinel
			while ( low < high ) {
				//Define mid in the form below
				//Because if defines mid as "(lo+hi)/2", this might excess the max limit of Integer when calculating (lo+hi)..
				int mid = low + (high - low) / 2;
				if ( HelperFunc.less(temp, array[mid]) ) high = mid - 1;
				else low = mid + 1;
			}

			for ( int j = i; j > low; --j )
				array[j] = array[j - 1];
			array[low] = temp;
		}
		assert HelperFunc.isSorted(array, lo, hi);
		System.out.println(stopwatch.elapsedTime());
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		assert in.exists() && !in.isEmpty();
		String[] a = in.readAllStrings();
		InsertionSort.ordinarySort(a);
		InsertionSort.sentinelSort(a);
		InsertionSort.binarySort(a);

		for ( String arg : a )
			System.out.print(arg + "\n");
	}
}
