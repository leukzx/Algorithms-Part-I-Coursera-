import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int nElements = 0;

    private class RandomizedQueueIterator implements Iterator<Item>
    {
        private int id = 0;
        private int[] ids = new int[nElements];

        public RandomizedQueueIterator() {
            for (int i = 0; i < nElements; i++) ids[i] = i;
            StdRandom.shuffle(ids);
        }

        public boolean hasNext() {  return id < nElements;  }
        public void remove() {  /* not supported */
            throw new java.lang.UnsupportedOperationException("The remove() method in the iterator is not supported.");
        }
        public Item next()
        {
            if (id == nElements)
                throw new java.util.NoSuchElementException("There are no more items to return");
            Item item = items[ids[id++]];
            return item;
        }
    }

    public RandomizedQueue() {  // construct an empty randomized queue
        items = (Item[]) new Object[1];
    }

    public boolean isEmpty() { // is the queue empty?
        return nElements == 0;
    }

    public int size() { // return the number of items on the queue
        return nElements;
    }

    public void enqueue(Item item) { // add the item
        if (item == null)
            throw new java.lang.NullPointerException("Attempt to add a null item");
        if (nElements == items.length) resize(2 * items.length);
        items[nElements] = item;
        nElements++;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < nElements; i++) copy[i] = items[i];
        items = copy;
    }

    public Item dequeue() { // remove and return a random item
        if (isEmpty())
            throw new java.util.NoSuchElementException("Attempt to remove an item from an empty deque");
        int rNum = 0;
        if (nElements > 1) rNum = StdRandom.uniform(nElements);
        nElements--;
        Item item = items[rNum];
        items[rNum] = items[nElements];
        items[nElements] = null;
        if (nElements > 0 && nElements == items.length / 4)
            resize(items.length / 2);
        return item;
    }

    public Item sample() { // return (but do not remove) a random item
        if (isEmpty())
            throw new java.util.NoSuchElementException("Attempt to remove an item from an empty deque");
        return items[StdRandom.uniform(nElements)];
    }

    public Iterator<Item> iterator() { // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }

    public static void main(String[] args) { // unit testing (optional)

    }
}
