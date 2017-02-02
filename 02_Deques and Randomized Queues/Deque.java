import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
    private class Node
    {
        private Item item;
        private Node next;
        private Node prev;

    }

    private class DequeIterator implements Iterator<Item>
    {
        private Node current = first;

        public boolean hasNext() {  return current != null;  }
        public void remove() {  /* not supported */
            throw new java.lang.UnsupportedOperationException("The remove() method in the iterator is not supported.");
        }
        public Item next()
        {
            if (current == null)
                throw new java.util.NoSuchElementException("There are no more items to return");
            Item item = current.item;
            current   = current.prev;
            return item;
        }
    }

    private Node first, last;
    private int nElements = 0;

    public Deque() { // construct an empty deque
        first = null;
        last = null;
    }

    public boolean isEmpty() { // is the deque empty?
        return ((first == null) || (last == null));
    }

    public int size() { // return the number of items on the deque
        return nElements;
    }

    public void addFirst(Item item) { // add the item to the front
        if (item == null)
            throw new java.lang.NullPointerException("Attempt to add a null item");
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.next = null;
        newFirst.prev = first;
        if (!isEmpty())
            first.next = newFirst;
        else
            last = newFirst;
        first = newFirst;
        nElements++;
    }

    public void addLast(Item item) { // add the item to the end
        if (item == null)
            throw new java.lang.NullPointerException("Attempt to add a null item");
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.next = oldLast;
        last.prev = null;
        if (!isEmpty())
            oldLast.prev = last;
        else
            first = last;
        nElements++;
    }

    public Item removeFirst() { // remove and return the item from the front
        if (isEmpty())
            throw new java.util.NoSuchElementException("Attempt to remove an item from an empty deque");
        Item item = first.item;
        if (first == last) {
            first = null;
            last = null;
        } else {
            first = first.prev;
            first.next = null;
        }
        nElements--;
        return item;
    }

    public Item removeLast() { // remove and return the item from the end
        if (isEmpty())
            throw new java.util.NoSuchElementException("Attempt to remove an item from an empty deque");
        Item item = last.item;
        last = last.next;
        if (last == null)
            first = null;
        if (!isEmpty())
            last.prev = null;
        nElements--;
        return item;
    }

    public Iterator<Item> iterator() { // return an iterator over items in order from front to end
        return new DequeIterator();
    }



    public static void main(String[] args) { // unit testing (optional)

    }
}
