import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * @author Herman Geng
 * @code This class provide static method for sorting an array using Merge sort.
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class MergeSort {
	private static final int cutOff = 7; //cutoff to insertion sort

	//This class should not be instantiated.
    private MergeSort() { }

	//Stable merge a[lo .. mid] with a[mid+1  hi] using aux[lo .. hi]
	//This kind of merge should check four boundaries of two sub-arrarys.
	private static void stableMerge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
		//assert a != null && aux != null;
		//assert lo >= 0 && hi <= a.length && lo <= mid && mid <= hi;

		//Precondition a[lo .. mid] and a[mid+1 .. hi] are sorted sub-arrays.
		assert HelperFunc.isSorted(a, lo, mid);
		assert HelperFunc.isSorted(a, mid + 1, hi);

		//If a[mid] <= a[mid+1], it means two sub-arrays are sorted already, return.
		//HelperFunc.less(a,b) returns true only when a < b, not a <= b.
        // if ( a[mid].compareTo(a[mid + 1]) <= 0 ) return;

		//Copy sub-array to aux[]. This array is initialed before recursion.
        //for ( int k = lo; k <= hi; k++ )
        //	aux[k] = a[k];

		int i = lo, j = mid + 1;
		for ( int k = lo; k <= hi; k++ ) {
            if (i > mid) aux[k] = a[j++];
            else if (j > hi) aux[k] = a[i++];
            else if (HelperFunc.less(a[i], a[j])) aux[k] = a[i++];
            else aux[k] = a[j++];
        }
		//assert HelperFunc.isSorted(a,lo,hi);
	}

	//This kind of merge checks only two boundaries of two sub-arrays while it's result might be unstable.
	private static void unStableMerge(Comparable[] a, Comparable[] aux, int lo, int mid, int hi) {
		assert HelperFunc.isSorted(a, lo, mid);
		assert HelperFunc.isSorted(a, mid + 1, hi);

		if ( a[mid].compareTo(a[mid + 1]) <= 0 ) return;

		//Copy first-part of a[] to aux[]
		for ( int i = lo; i <= mid; i++ )
			aux[i] = a[i];
		//Copy last-part of a[] conversely to aux[]
		for ( int i = mid + 1; i <= hi; i++ )
			aux[i] = a[hi - i + mid + 1];
		//Set two pointer in front and end of two arrays respectively.
		int i = lo;
		int j = hi;
		//When one side is over, its pointer moves to the largest element in the other side as a sentinel.
		//TODO explain why it's unstable
		for ( int k = lo; k <= hi; k++ )
			if ( HelperFunc.less(aux[i], aux[j]) ) a[k] = aux[i++];
			else a[k] = aux[j--];
	}

	/**
	 * Rearranges the array in ascending order, using the natural order with top-down recursion.
	 *
	 * @param a the array to be sorted.
	 */
	public static void topDownSort(Comparable[] a) {
		assert a != null;
		Stopwatch stopwatch = new Stopwatch();

		//Initial auxiliary array here in order to avoid any other initializations during recursion.
		//If initials in recursive topDownSort function, each call would pay extra time to initial a new array.
		Comparable[] aux = new Comparable[a.length];
        for (int i = 0; i < a.length; i++)
            aux[i] = a[i];
        topDownSort(a, aux, 0, a.length - 1);
		HelperFunc.isSorted(a);
		System.out.println(stopwatch.elapsedTime());
	}

	//Mergesort a[lo .. hi] using auxiliary array aux[lo .. hi]
	private static void topDownSort(Comparable[] a, Comparable[] aux, int lo, int hi) {
		assert lo >= 0 && hi <= a.length;
		//Switch to InsertionSort when sorting specified small-scale array.
		if ( lo + cutOff >= hi ) {
			InsertionSort.ordinarySort(a, lo, hi);
			return;
		}
		//Define mid in the form below
		//Because if defines mid as "(lo+hi)/2", this might excess the max limit of Integer when calculating (lo+hi)..
		int mid = lo + (hi - lo) / 2;
        topDownSort(aux, a, lo, mid);
        topDownSort(aux, a, mid + 1, hi);
        stableMerge(a, aux, lo, mid, hi);
	}

	public static void bottomUpSort(Comparable[] a) {
		Stopwatch stopwatch = new Stopwatch();
		int length = a.length;
		Comparable[] aux = new Comparable[length];

		for ( int len = 1; len < length; len *= 2 ) {
			//TODO Understand following codes.
			for ( int lo = 0; lo < length - len; lo += len + len ) {
				int mid = lo + len + 1;
				int hi = Math.min(lo + len + len - 1, length - 1);
				stableMerge(a, aux, lo, mid, hi);
			}
		}
		assert HelperFunc.isSorted(a);
		System.out.println(stopwatch.elapsedTime());
	}

	//public static void naturalMergeSort(Comparable[] a)

	private static void stableIndexMerge(Comparable[] a, int[] index, int[] aux, int lo, int mid, int hi) {
		assert a != null && aux != null & index != null;
		assert lo >= 0 && hi <= a.length && lo <= mid && mid <= hi;

		for ( int k = lo; k < hi; k++ )
			aux[k] = index[k];

		int i = lo, j = mid + 1;
		for ( int k = lo; k < hi; k++ ) {
			if ( i > mid ) index[k] = aux[j++];
			else if ( j > hi ) index[k] = aux[i++];
			else if ( HelperFunc.less(aux[i], aux[j]) ) index[k] = aux[i++];
			else index[k] = aux[j++];
		}
	}

	/**
	 * Returns a permutation that gives the elements in the array in ascending orders
	 *
	 * @param a the array
	 * @return a permutation {@code p[]} such that {@code a[p[0]]},{@code a[p[1]]},
	 * ...... , {@code a[p[n-1]]} are in ascending order
	 */
	public static int[] indexSort(Comparable[] a) {
		assert a != null;
		Stopwatch stopwatch = new Stopwatch();

		int length = a.length;
		int[] index = new int[length];
		for ( int i = 0; i < length; i++ )
			index[i] = i;
		int[] aux = new int[length];

		indexSort(a, index, aux, 0, length - 1);
		System.out.println(stopwatch.elapsedTime());
		return index;
	}

	private static void indexSort(Comparable[] a, int[] index, int[] aux, int lo, int hi) {
		assert lo >= 0 && hi <= a.length;
		if ( lo >= hi ) return;

		int mid = lo + (hi - lo) / 2;
		indexSort(a, index, aux, lo, mid);
		indexSort(a, index, aux, mid + 1, hi);
		stableIndexMerge(a, index, aux, lo, mid, hi);
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		assert in.exists() && !in.isEmpty();
		String[] a = in.readAllStrings();
		MergeSort.topDownSort(a);

		for ( String arg : a )
			System.out.println(arg);
	}
}
