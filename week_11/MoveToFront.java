import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.Iterator;
import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> chain = initChain();
        while (!BinaryStdIn.isEmpty()) {
            char idx = BinaryStdIn.readChar();

            int i = 0;
            for (Iterator<Character> iter = chain.iterator(); iter.hasNext(); i++) {
                char next = iter.next();
                if (next == idx) {
                    iter.remove();
                    break;
                }
            }

            BinaryStdOut.write(i, 8);
            chain.addFirst(idx);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> chain = initChain();
        while (!BinaryStdIn.isEmpty()) {
            int idx = BinaryStdIn.readChar();
            char next = ' ';

            int i = 0;
            for (Iterator<Character> iter = chain.iterator(); iter.hasNext(); i++) {
                next = iter.next();
                if (i == idx) {
                    iter.remove();
                    break;
                }
            }

            BinaryStdOut.write(next);
            chain.addFirst(next);
        }
        BinaryStdOut.close();
    }

    private static LinkedList<Character> initChain() {
        LinkedList<Character> chain = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            chain.add((char) i);
        }
        return chain;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if      (args[0].equals("-")) encode();
        else if (args[0].equals("+")) decode();
        else throw new IllegalArgumentException();

    }
}
