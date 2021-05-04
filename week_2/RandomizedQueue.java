import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;


public class RandomizedQueue<Item> implements Iterable<Item> {

    // initial capacity of underlying resizing array
    private static final int INIT_CAPACITY = 8;

    private int size;
    private Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.items = (Item[]) new Object[INIT_CAPACITY];
        this.size = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() { return size == 0; }

    // return the number of items on the randomized queue
    public int size() { return size; }

    // resize the array
    private void resize(int capacity) {
        assert capacity >= size;

        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++)
            copy[i] = items[i];
        items = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();

        if (items.length == size)
            resize(2 * items.length);

        items[size++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        if (size == items.length / 4)
            resize(items.length / 2);

        int index = StdRandom.uniform(0, size);
        Item itemReturn = items[index];
        items[index] = items[--size];
        items[size] = null;
        return itemReturn;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty())
            throw new java.util.NoSuchElementException();

        return items[StdRandom.uniform(0, size)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item> {

        private Item[] arr;
        private int current;

        public RandomizedIterator() {
            this.arr = (Item[]) new Object[size];
            this.current = 0;

            for (int i = 0; i < size; i++)
                arr[i] = items[i];
            StdRandom.shuffle(arr);
        }

        public boolean hasNext() {
            return (current < arr.length);
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (current == arr.length)
                throw new java.util.NoSuchElementException();

            return arr[current++];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queueRandom = new RandomizedQueue<Integer>();
        queueRandom.enqueue(0);
        queueRandom.enqueue(1);
        StdOut.println(queueRandom.isEmpty());
        StdOut.println(queueRandom.size());
        StdOut.println(queueRandom.sample());
        queueRandom.dequeue();
        queueRandom.dequeue();
        StdOut.println(queueRandom.isEmpty());
        StdOut.println(queueRandom.size());

        int n = 5;
        for (int i = 0; i < n; i++)
            queueRandom.enqueue(i);
        for (int a : queueRandom) {
            for (int b : queueRandom)
                StdOut.print(a + "-" + b + " ");
            StdOut.println();
        }
    }

}
