import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.Iterator;

public class Permutation {
   public static void main(String[] args) {
       RandomizedQueue<String> rq = new RandomizedQueue<String>();
       int k = Integer.parseInt(args[0]);
       while (!StdIn.isEmpty())
           rq.enqueue(StdIn.readString());

       Iterator<String> rqit = rq.iterator();
       for (int i = 0; i < k && rqit.hasNext(); i++)
            StdOut.println(rqit.next());
   }
}
