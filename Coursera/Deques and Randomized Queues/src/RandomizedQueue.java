import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private Item[] array;
	private int n;

	public RandomizedQueue() {
		array = (Item[]) new Object[2];
		n = 0;
	}

	public static void main(String[] args) {
		RandomizedQueue<String> queue = new RandomizedQueue<String>();
		while ( !StdIn.isEmpty() ) {
			String item = StdIn.readString();
			queue.enqueue(item);
		}

		for ( String item : queue )
			StdOut.println(item);
		StdOut.println();
		for ( String item : queue )
			StdOut.println(item);
	}

	public boolean isEmpty() {
		return n == 0;
	}

	public int size() {
		return n;
	}

	public void enqueue(Item item) {
		if ( item == null ) throw new java.lang.IllegalArgumentException();
		if ( n == array.length ) resize(2 * n);
		array[n++] = item;
	}

	public Item dequeue() {
		if ( isEmpty() ) throw new NoSuchElementException();
		int target = StdRandom.uniform(0, n);
		Item item = array[target];
		array[target] = array[--n];
		array[n] = null;
		if ( n > 0 && n == array.length / 4 ) resize(array.length / 2);
		return item;
	}

	public Item sample() {
		if ( isEmpty() ) throw new java.util.NoSuchElementException();
		return array[StdRandom.uniform(0, n)];
	}

	public Iterator<Item> iterator() {
		return new RandomIterator();
	}

	private void resize(int capacity) {
		assert capacity >= n;

		Item[] temp = (Item[]) new Object[capacity];
		for ( int i = 0; i < n; i++ )
			temp[i] = array[i];
		array = temp;
	}

	private class RandomIterator implements Iterator<Item> {
		private int count;
		private Item[] temp;

		public RandomIterator() {
			count = n;
			temp = (Item[]) new Object[count];
			for ( int i = 0; i < count; i++ ) {
				temp[i] = array[i];
			}
		}

		public boolean hasNext() {
			return count > 0;
		}

		public Item next() {
			if ( !hasNext() ) throw new NoSuchElementException();
			int target = StdRandom.uniform(0, count);
			Item item = temp[target];
			temp[target] = temp[--count];
			temp[count] = null;
			return item;
		}

		public void remove() {
			throw new java.lang.UnsupportedOperationException();
		}
	}
}
