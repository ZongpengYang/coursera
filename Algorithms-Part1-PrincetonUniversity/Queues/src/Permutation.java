import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args){
        if (args.length==0) return;
        Integer count = Integer.parseInt(args[0]);
        if (count==0) return;
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        int counter = 1;
        while(!StdIn.isEmpty()){
            if (counter <= count) queue.enqueue(StdIn.readString());
            else if (!StdRandom.bernoulli((double) count /counter)){
                queue.dequeue();
                queue.enqueue(StdIn.readString());
            }
            counter++;
        }
        for (String s : queue){
            System.out.println(s);
        }
    }
}
