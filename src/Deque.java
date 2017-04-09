import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node prev;
        
        Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
            if (next != null)
                next.prev = this;
            if (prev != null) 
                prev.next = this;
        }
    }
    
    private class DequeIterator implements Iterator<Item> {
        
        private Node current;
        
        DequeIterator(Node first) {
            current = first;
        }
        
        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (current == null)
                throw new NoSuchElementException("no more elements");
            
            Node temp = current;
            current = current.next;
            
            return temp.item;
        }
        
    }
    
    private Node first;
    private Node last;
    private int size;
    
    public Deque() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null element");
        
        Node temp = new Node(item, first, null);
        if (last == null)
            last = temp;
        first = temp;
        
        size++;
    }

    public void addLast(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null element");
        
        Node temp = new Node(item, null, last);
        if (last != null)
            last.next = temp;
        else
            first = temp;
        
        last = temp;
        
        size++;
    }

    public Item removeFirst() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");
        
        Node node = first;
        
        first = first.next;
        if (first != null)
            first.prev = null;
        else
            last = null;
        
        size--;
        
        return node.item;
    }

    public Item removeLast() {
        if (isEmpty())
            throw new NoSuchElementException("Deque is empty");
        
        Node node = last;
        
        last = last.prev;
        if (last != null)
            last.next = null;
        else
            first = null;
        
        size--;
        
        return node.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator(first);
    }
}