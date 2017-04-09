import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class DequeIterator implements Iterator<Item> {
        
        Iterator<Item> iter;
        
        public DequeIterator() {
            iter = Deque.this.list.iterator();
        }
        
        @Override
        public boolean hasNext() {
            return iter.hasNext();
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("no more elements");
            
            return iter.next();
        }
        
    }
    
    private LinkedList<Item> list;
    private int size;
    
    public Deque() {
        list = new LinkedList<>();
        size = 0;
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null element");
        
        list.addFirst(item);
        size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null element");
        
        list.addLast(item);
        size++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");
        
        size--;
        return list.removeFirst();
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");
        
        size--;
        return list.removeLast();
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
}