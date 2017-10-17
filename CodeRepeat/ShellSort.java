import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Shell;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * @author Herman Geng
 * @<code>ShellSort</code> class provides static methods for sorting an array using ShellSort with Knuth's increment sequence
 * Prototype of this code comes from <i>Algorithms 4th Edition</i>
 */
public class ShellSort {
	//This class should not be instantiated.
	private ShellSort() {
	}

	public static void sort(Comparable[] array) {
		sort(array, 1, array.length);
	}

	public static void sort(Comparable[] array, int lo, int hi) {
		assert array != null;
		assert lo >= 1 && hi <= array.length && lo <= hi;

		Stopwatch stopwatch = new Stopwatch();
		int h = 1;
		//Fixed ascending series comes from combination of (9*4^k-9*2^k+1) and (4^k-3*2^k+1).
		//int[] fixedh = new int[] { 1,5,9,41,109,209,505,929,2161,3905,8929,16001,36289,64769,14605,260609 };
		while ( h < (hi - lo) / 3 ) h = 3 * h + 1;

		while ( h >= 1 ) {
			//h-sort the array
			//todo
			h = h / 3;
		}
		System.out.println(stopwatch.elapsedTime());
		assert HelperFunc.isSorted(array, lo, hi);
	}

	public static void main(String[] args) {
		In in = new In(args[0]);
		assert in.exists() && !in.isEmpty();
		String[] a = in.readAllStrings();
		Shell.sort(a);
	}
}
