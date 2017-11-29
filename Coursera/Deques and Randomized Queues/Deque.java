import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int size = 0;

    public Deque() {
        first = null;
        last = null;
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<String>();

        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            switch (item) {
                case "+":
                    deque.addFirst(StdIn.readString());
                    break;
                case "++":
                    deque.addLast(StdIn.readString());
                    break;
                case "-":
                    StdOut.print(deque.removeFirst() + " ");
                    break;
                case "--":
                    StdOut.print(deque.removeLast() + " ");
                    break;
                case "Show":
                    for (String s : deque) StdOut.println(s);
                    break;
            }
        }
        StdOut.println("(" + deque.size() + " left on deque)");
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.nextNode = oldFirst;
        if (oldFirst == null) {
            last = first;
        } else {
            oldFirst.previousmNode = first;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null) throw new java.lang.IllegalArgumentException();
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previousmNode = oldLast;
        if (oldLast == null) {
            first = last;
        } else {
            oldLast.nextNode = last;
        }
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = first.item;
        first = first.nextNode;
        if (first == null) {
            last = null;
        } else {
            first.previousmNode = null;
        }
        size--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        Item item = last.item;
        last = last.previousmNode;
        if (last == null) {
            first = null;
        } else {
            last.nextNode = null;
        }
        size--;
        return item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node {
        Item item;
        Node nextNode;
        Node previousmNode;
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = first;

        public boolean hasNext() {
            return current == null;
        }

        public void remove() {
            throw new java.lang.UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = current.item;
            current = current.nextNode;
            return item;
        }

    }
}
