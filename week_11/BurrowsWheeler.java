import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int len = s.length();

        int start = 0;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            int csa = circularSuffixArray.index(i);
            if (csa == 0) start = i;
            chars[i] = s.charAt((csa + len - 1) % len);
        }
        BinaryStdOut.write(start);

        for (char ch : chars) {
            BinaryStdOut.write(ch);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int start = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        char[] chars = s.toCharArray();

        int[] num = new int[257];
        int[] next = new int[chars.length];
        char[] sort = new char[chars.length];

        for (char ch : chars) {
            num[ch + 1]++;
        }

        for (int i = 0; i < 256; i++) {
            num[i + 1] += num[i];
        }

        for (int i = 0; i < chars.length; i++) {
            sort[num[chars[i]]] = chars[i];
            next[num[chars[i]]] = i;
            num[chars[i]]++;
        }

        int idx = start;
        for (int i = 0; i < chars.length; i++) {
            BinaryStdOut.write(sort[idx]);
            idx = next[idx];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException();
    }
}