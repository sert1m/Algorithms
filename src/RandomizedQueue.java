import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
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
    
    private class RandomizedQueueIterator implements Iterator<Item> {
        
        private int index;
        private int []sequences;
        
        RandomizedQueueIterator() {
            sequences = StdRandom.permutation(RandomizedQueue.this.size());
            index = 0;
        }
        
        @Override
        public boolean hasNext() {
            return index < sequences.length;
        }

        @Override
        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException("no more elements");
            
            return getNext().item;
        }
        
        private Node getNext() {
            return getNode(sequences[index++]);
        }
    }
    
    private Node first;
    private Node last;
    private int size;
    
    public RandomizedQueue() {
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

    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null element");
        
        Node temp = new Node(item, null, last);
        if (first == null)
            first = temp;
        
        if (last != null)
            last.next = temp;
        
        last = temp;
        
        size++;
    }

    public Item dequeue() {
        Node node = getRandomNode();
        
        if (node.prev != null)
            node.prev.next = node.next;
        
        if (node.next != null)
            node.next.prev = node.prev;
        
        if (first == node)
            first = node.next;
        
        if (last == node)
            last = node.prev;
        
        size--;
        
        return node.item;
    }

    public Item sample() {
        return getRandomNode().item;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private Node getRandomNode() {
        if (isEmpty())
            throw new NoSuchElementException("RandomizedQueue is empty");
        return getNode(StdRandom.uniform(size));
    }
    
    private Node getNode(int index) {

        
        Node node = first;
        while (index-- > 0) {
            node = node.next;
        }
        
        return node;
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.sample();
    }
}