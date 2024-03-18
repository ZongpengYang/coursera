import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque <Item> implements Iterable<Item> {

    private Node<Item> front;
    private Node<Item> end;
    private int size;

    private class Node<Item> {
        Node<Item> prev;
        Node<Item> next;
        Item data;
    }

    // construct an empty deque
    public Deque(){
        size = 0;
        front = null;
        end = null;
    }

    // is the deque empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the deque
    public int size(){
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Adding null entry not permitted!");
        Node<Item> oldFront = front;
        front = new Node<Item>();
        front.data = item;
        if (isEmpty()) end = front;
        else {
            front.next = oldFront;
            oldFront.prev = front;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Adding null entry not permitted!");
        Node<Item> oldLast = end;
        end = new Node<Item>();
        end.data = item;
        end.next = null;
        if (isEmpty()) front = end;
        else {
            end.prev = oldLast;
            oldLast.next = end;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst(){
        if(isEmpty()) throw new NoSuchElementException("No element to remove from front!");
        Node<Item> result = front;
        front = front.next;
        size--;
        return result.data;
    }

    // remove and return the item from the back
    public Item removeLast(){
        if(isEmpty()) throw new NoSuchElementException("No element to remove from end!");
        Node<Item> result = end;
        end = end.prev;
        size--;
        return result.data;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator(front);
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Node<Item> start) {
            current = start;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException("No next element in the iterator!");
            Item result = current.data;
            current = current.next;
            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Not supported!");
        }
    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<String> deque = new Deque<>();
        try {
            deque.removeFirst();
        } catch (NoSuchElementException e){
            System.out.println("Exception caught(removeFirst): "+ e.getMessage());
        }
        try {
            deque.removeLast();
        } catch (NoSuchElementException e){
            System.out.println("Exception caught(removeLast): "+ e.getMessage());
        }
        try {
            deque.addFirst(null);
        } catch (IllegalArgumentException e){
            System.out.println("Exception caught(addFirst): "+ e.getMessage());
        }
        try {
            deque.addLast(null);
        } catch (IllegalArgumentException e){
            System.out.println("Exception caught(addLast): "+ e.getMessage());
        }
        System.out.println("Adding elements in order of aaa-bbb-ccc-ddd");
        deque.addFirst("bbb");
        deque.addFirst("aaa");
        deque.addLast("ccc");
        deque.addLast("ddd");
        System.out.println("Testing Iterator... Order should be same as above");
        for(String item : deque){
            System.out.println(item);
        }
        try {
            deque.iterator().remove();
        } catch (UnsupportedOperationException e){
            System.out.println("Exception caught(remove iterator): "+ e.getMessage());
        }

        System.out.println("Removing first element... expected: aaa, actual: "+ deque.removeFirst());
        System.out.println("Removing last element... expected: ddd, actual: "+ deque.removeLast());
        System.out.println("Removing last element... expected: ccc, actual: "+ deque.removeLast());
        System.out.println("Removing first element... expected: bbb, actual: "+ deque.removeFirst());
    }
}
