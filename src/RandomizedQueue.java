import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
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
            
            return list.get(sequences[index++]);
        }
    }
    
    private List<Item> list; 
    
    public RandomizedQueue() {
        list = new ArrayList<>();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return list.size();
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new NullPointerException("Adding null element");
        
        list.add(item);
    }

    public Item dequeue() {
        return list.remove(getRandom());
    }

    public Item sample() {
        return list.get(getRandom());
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private int getRandom() {
        if (isEmpty())
            throw new NoSuchElementException("Empty");
        return StdRandom.uniform(list.size());
    }
    
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        queue.sample();
    }
}