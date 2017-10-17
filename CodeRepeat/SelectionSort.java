import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Comparator;

/**
 * @author Herman Geng
 * @code This class provide static method for sorting an array using selection sort.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class SelectionSort {
	//This class should not be instantiated.
	private SelectionSort() {
	}

	/**
	 * Rearranges the array in ascending order, using the natural order.
	 *
	 * @param array the array to be sorted.
	 */
	public static void sort(Comparable[] array) {
		assert array != null;

		Stopwatch stopwatch = new Stopwatch();
		int length = array.length;
		for ( int i = 0; i < length; i++ ) {
			int min = i;
			for ( int j = i + 1; j < length; j++ ) {
				if ( HelperFunc.less(array[j], array[min]) ) min = j;
			}
			HelperFunc.exch(array, min, i);
			assert HelperFunc.isSorted(array, 0, i);
		}
		System.out.println(stopwatch.elapsedTime());
		assert HelperFunc.isSorted(array);
	}

	/**
	 * Rearranges the array in ascending order, using a comparator.
	 *
	 * @param array      the array to be sorted.
	 * @param comparator the comparator specifying the order
	 */
	public static void sort(Object[] array, Comparator comparator) {
		assert array != null;
		assert comparator != null;

		Stopwatch stopwatch = new Stopwatch();
		int length = array.length;
		for ( int i = 0; i < length; i++ ) {
			int min = i;
			for ( int j = i + 1; j < length; j++ ) {
				if ( HelperFunc.less(comparator, array[j], array[min]) ) min = j;
			}
			HelperFunc.exch(array, min, i);
			assert HelperFunc.isSorted(array, comparator, 0, i);
		}
		System.out.println(stopwatch.elapsedTime());
		assert HelperFunc.isSorted(array, comparator);
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		assert in.exists() && !in.isEmpty();
		String[] a = in.readAllStrings();
		SelectionSort.sort(a);
		for ( String arg : a ) {
			System.out.print(arg + "\n");
		}
	}
}
