import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentDeque<T> implements Iterable<T> {
    private Node<T> currentFirst = null;
    private Node<T> currentLast = null;
    private volatile int currentSize = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public boolean isEmpty() {
        try {
            lock.lock();
            return currentSize == 0;
        } finally {
            lock.unlock();
        }
    }

    public int size() {
        return currentSize;
    }

    // add the item to the front
    public void addFirst(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<T> newNode = new Node(item);
        newNode.next = null;
        try {
            lock.lock();
            newNode.previous = currentFirst;
            if (isEmpty()) {
                currentFirst = newNode;
                currentLast = currentFirst;
            } else {
                currentFirst.next = newNode;
                currentFirst = currentFirst.next;
            }
            currentSize++;
        } finally {
            lock.unlock();
        }
    }

    // add the item to the back
    public void addLast(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<T> newNode = new Node(item);
        newNode.next = null;
        try {
            lock.lock();
            newNode.previous = currentLast;
            if (isEmpty()) {
                currentLast = newNode;
                currentFirst = currentLast;
            } else {
                currentLast.previous = newNode;
                currentLast = currentLast.previous;
            }
            currentSize++;
        } finally {
            lock.unlock();
        }
    }

    // remove and return the item from the front
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T value;
        try {
            lock.lock();
            value = currentFirst.value;
            if (currentFirst.previous == null) {
                currentFirst = null;
                currentLast = null;
            } else {
                currentFirst = currentFirst.previous;
                currentFirst.next = null;
            }
            currentSize--;
            return value;
        } finally {
            lock.unlock();
        }
    }

    // remove and return the item from the back
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        T value;
        try {
            value = currentLast.value;
            if (currentLast.next == null) {
                currentLast = null;
                currentFirst = null;
            } else {
                currentLast = currentLast.next;
                currentLast.previous = null;
            }
            currentSize--;
            return value;
        } finally {
            lock.unlock();
        }
    }

    // return an iterator over items in order from front to back
    // todo: make concurrent
    public Iterator<T> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<T> {
        private Node<T> currentNode;
        T currentValue;

        DequeIterator() {
            try {
                lock.lock();
                currentNode = currentFirst;
                currentValue = (currentNode == null) ? null : currentNode.value;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public boolean hasNext() {
            return currentNode != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            try {
                lock.lock();
                currentNode =  currentFirst.previous;
                return currentValue;
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private class Node<Item> {
        private Node previous = null;
        private Node next = null;
        private final Item value;

        private Node(final Item value) {
            this.value = value;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        basic_correctness_test();
        basic_multithreaded_test();
        multithreaded_size_test();
    }

    private static void basic_correctness_test() {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(1);
        deque.addFirst(2);
        assert deque.removeFirst() == 2;  // 2-1 ==> 2
        deque.addLast(4);
        deque.addFirst(5);
        assert  deque.removeLast() == 4;  // 5-1-4  ==> 4
        assert deque.removeFirst() == 5; // 5-1 ==> 5
        assert  deque.removeLast() == 1 ;  // 1 ==> 1

        Iterator<Integer> iterator = deque.iterator();
        assert deque.isEmpty() && deque.size() == 0;
        assert !iterator.hasNext();
    }

    private static void basic_multithreaded_test() {
        Deque<Integer> deque = new Deque<>();
        AtomicInteger dequeEmptyExceptions = new AtomicInteger();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                deque.addFirst(i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                try {
                    deque.removeLast();

                // the deque should probably use semaphores instead of throwing exceptions like this
                } catch (NoSuchElementException e) {
                    dequeEmptyExceptions.incrementAndGet();
                }
            }
        });

        try {
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
            assert !deque.isEmpty();
            assert deque.size() == 50 + dequeEmptyExceptions.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void multithreaded_size_test() {
        Deque<Integer> deque = new Deque<>();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                deque.addFirst(i);
                deque.removeLast();
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 50; i++) {
                assert deque.isEmpty();
            }
        });

        try {
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}