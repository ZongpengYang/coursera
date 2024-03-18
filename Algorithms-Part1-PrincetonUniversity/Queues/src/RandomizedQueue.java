import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node<Item> front;
    private int size;

    private class Node<Item>{
        Node<Item> next;
        Item data;
    }
    // construct an empty randomized queue
    public RandomizedQueue(){
        size=0;
        front=null;
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return size;
    }

    // add the item
    public void enqueue(Item item){
        if (item == null) throw new IllegalArgumentException("Adding null entry not permitted!");
        Node<Item> oldFront = front;
        front = new Node<Item>();
        front.data = item;
        front.next = oldFront;
        size++;
    }

    // remove and return a random item
    public Item dequeue(){
        if(isEmpty()) throw new NoSuchElementException("No element to remove from front!");
        int randomIndex = StdRandom.uniformInt(size);
        Node<Item> current = front;
        Node<Item> prev = null;
        for (int i=0; i<randomIndex; i++){
            prev = current;
            current = current.next;
        }
        if (prev==null) front = current.next;
        else prev.next = current.next;
        size--;
        return current.data;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        if(isEmpty()) throw new NoSuchElementException("No element to remove from front!");
        int randomIndex = StdRandom.uniformInt(size);
        Node<Item> current = front;
        for (int i=0; i<randomIndex; i++){
            current = current.next;
        }
        return current.data;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new RandomizedQuqueIterator(front);
    }

    private class RandomizedQuqueIterator implements Iterator<Item> {
        private Node<Item> current;
        private int[] indexHolder;
        private int counter;

        public RandomizedQuqueIterator(Node<Item> start) {
            current = start;
            indexHolder = new int[size];
            for (int i=0; i<size; i++){
                indexHolder[i] = i;
            }
            StdRandom.shuffle(indexHolder);
            counter=0;
        }

        @Override
        public boolean hasNext() {
            return counter != size;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No next element in the iterator!");
            Node<Item> current = front;
            for (int i=0; i<indexHolder[counter]; i++){
                current = current.next;
            }
            counter++;
            return current.data;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported!");
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<String> deque = new RandomizedQueue<>();
        try {
            deque.sample();
        } catch (NoSuchElementException e){
            System.out.println("Exception caught(sample): "+ e.getMessage());
        }
        try {
            deque.dequeue();
        } catch (NoSuchElementException e){
            System.out.println("Exception caught(dequeue): "+ e.getMessage());
        }
        try {
            deque.enqueue(null);
        } catch (IllegalArgumentException e){
            System.out.println("Exception caught(enqueue): "+ e.getMessage());
        }
        System.out.println("Adding elements in order of aaa-bbb-ccc-ddd");
        deque.enqueue("bbb");
        deque.enqueue("aaa");
        deque.enqueue("ccc");
        deque.enqueue("ddd");
        System.out.println("Testing sample... Order should be random");
        deque.sample();
        deque.sample();
        deque.sample();
        deque.sample();
        System.out.println("Testing Iterator... Order should be random");
        for(String item : deque){
            System.out.println(item);
        }

        System.out.println("Removing element... "+ deque.dequeue());
        System.out.println("Removing element... "+ deque.dequeue());
        System.out.println("Removing element... "+ deque.dequeue());
        System.out.println("Removing element... "+ deque.dequeue());

        try {
            deque.iterator().remove();
        } catch (UnsupportedOperationException e){
            System.out.println("Exception caught(remove iterator): "+ e.getMessage());
        }
    }

}
