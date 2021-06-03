import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException();

        int len = s.length();
        Node[] nodes = new Node[len];

        for (int i = 0; i < len; i++) {
            nodes[i] = new Node(s, i);
        }
        Arrays.sort(nodes);

        index = new int[len];
        for (int i = 0; i < len; i++) {
            index[i] = nodes[i].getBias();
        }
    }

    private class Node implements Comparable<Node> {
        private final String s;
        private final int bias;

        public Node(String s, int bias) {
            this.s = s;
            this.bias = bias;
        }

        public int compareTo(Node that) {
            int len = s.length();
            for (int i = 0; i < len; i++) {
                char charThis = s.charAt((this.bias + i) % len);
                char charThat = s.charAt((that.bias + i) % len);

                if (charThis < charThat) return -1;
                if (charThis > charThat) return  1;
            }
            return 0;
        }

        public int getBias() {
            return bias;
        }
    }

    // length of s
    public int length() {
        return index.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > index.length - 1)
            throw new IllegalArgumentException();

        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        if (new CircularSuffixArray(args[0]).length() > 0)
            StdOut.println("Done!");
    }
}
