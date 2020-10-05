import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node currentFirst = null;
    private Node currentLast = null;
    private int currentSize = 0;

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public int size() {
        return currentSize;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(currentFirst, null, item);
        if (isEmpty()) {
            currentFirst = newNode;
            currentLast = currentFirst;
        } else {
            currentFirst.next = newNode;
            currentFirst = currentFirst.next;
        }
        currentSize++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node newNode = new Node(null, currentLast, item);
        if (isEmpty()) {
            currentLast = newNode;
            currentFirst = currentLast;
        } else {
            currentLast.previous = newNode;
            currentLast = currentLast.previous;
        }
        currentSize++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (currentSize == 0) {
            throw new NoSuchElementException();
        }
        Item value = currentFirst.value;
        if (currentFirst.previous == null) {
            currentFirst = null;
            currentLast = null;
        } else {
            currentFirst = currentFirst.previous;
            currentFirst.next = null;
        }
        currentSize--;
        return value;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (currentSize == 0) {
            throw new NoSuchElementException();
        }
        Item value = currentLast.value;
        if (currentLast.next == null) {
            currentLast = null;
            currentFirst = null;
        } else {
            currentLast = currentLast.next;
            currentLast.previous = null;
        }
        currentSize--;
        return value;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node currentNode = currentFirst;

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Item value = currentNode.value;
            currentNode = currentNode.previous;
            return value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node {
        private Node previous;
        private Node next;
        private final Item value;

        private Node(Node previous, Node next, Item value) {
            this.previous = previous;
            this.next = next;
            this.value = value;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        System.out.println(deque.removeFirst());  // 2-1 ==> 2
        deque.addLast(4);
        deque.addFirst(5);
        System.out.println(deque.removeLast());  // 5-1-4  ==> 4
        System.out.println(deque.removeFirst()); // 5-1 ==> 5
        System.out.println(deque.removeLast());  // 1 ==> 1

        Iterator<Integer> iterator = deque.iterator();
        assert deque.isEmpty() && deque.size() == 0;
        assert !iterator.hasNext();
    }
}
