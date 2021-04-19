/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomWord {

    public static void main(String[] args) {
        int i = 1;
        String champion = "";
        while (!StdIn.isEmpty()) {
            String str = StdIn.readString();
            double p = 1 / (double) i;
            if (StdRandom.bernoulli(p)) {
                champion = str;
            }
            i++;
        }
        StdOut.println(champion);
    }

}
