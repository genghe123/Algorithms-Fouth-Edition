import java.util.Comparator;

public class HelperFunc {
	public static boolean less(Comparable a, Comparable b) {
		return a.compareTo(b) < 0;
	}

	public static boolean less(Comparator comparator, Object a, Object b) {
		return comparator.compare(a, b) < 0;
	}

	public static void exch(Object[] array, int i, int j) {
		Object temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public static boolean isSorted(Comparable[] array) {
		return isSorted(array, 0, array.length - 1);
	}

	public static boolean isSorted(Comparable[] array, int lo, int hi) {
		for ( int i = lo; i < hi; i++ ) {
			if ( less(array[i + 1], array[i]) ) return false;
		}
		return true;
	}

	public static boolean isSorted(Object[] array, Comparator comparator) {
		return isSorted(array, comparator, 0, array.length - 1);
	}

	public static boolean isSorted(Object[] array, Comparator comparator, int lo, int hi) {
		for ( int i = lo; i < hi; i++ ) {
			if ( less(comparator, array[i + 1], array[i]) ) return false;
		}
		return true;
	}
}
