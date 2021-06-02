import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashSet;
import java.util.Set;

public class BoggleSolver {
    private static final int R = 26;

    private final Trie set;
    private Set<String> words;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        if (dictionary == null)
            throw new IllegalArgumentException();

        set = new Trie();
        for (String w : dictionary) {
            if (w.length() > 2) set.add(w);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        if (board == null)
            throw new IllegalArgumentException();

        int w = board.cols();
        int h = board.rows();

        boolean[][] called;
        words = new HashSet<String>();

        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                called = new boolean[w][h];
                addToTrie(board, "", called, i, j);
            }
        }

        return words;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        if (!set.has(word))
            return 0;

        switch (word.length()) {
            case 3:
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 7: return 5;
            default: return 11;
        }
    }

    private void addToTrie(BoggleBoard board, String pattern, boolean[][] scored, int i, int j) {
        String word = pattern + board.getLetter(j, i);
        if (!set.hasPrefix(word)) return;

        if (board.getLetter(j, i) == 'Q')
            word += "U";

        scored[i][j] = true;

        if (word.length() > 2 && !words.contains(word) && set.has(word))
            words.add(word);

        int h = board.rows();
        if (i > 0 && !scored[i - 1][j])
            addToTrie(board, word, scored, i - 1, j);
        if (i > 0 && j > 0 && !scored[i - 1][j - 1])
            addToTrie(board, word, scored, i - 1, j - 1);
        if (i > 0 && j < h - 1 && !scored[i - 1][j + 1])
            addToTrie(board, word, scored, i - 1, j + 1);
        if (i < board.cols() - 1 && !scored[i+1][j])
            addToTrie(board, word, scored, i + 1, j);
        if (i < board.cols() - 1 && j > 0 && !scored[i + 1][j - 1])
            addToTrie(board, word, scored, i + 1, j - 1);
        if (i < board.cols() - 1 && j < h - 1 && !scored[i + 1][j + 1])
            addToTrie(board, word, scored, i + 1, j + 1);
        if (j > 0 && !scored[i][j - 1])
            addToTrie(board, word, scored, i, j - 1);
        if (j < h - 1 && !scored[i][j + 1])
            addToTrie(board, word, scored, i, j + 1);

        scored[i][j] = false;
    }

    private static class Node {
        private Node[] next = new Node[R];
        private boolean isWord = false;
    }

    private static class Trie {
        private Node root;

        private Node get(Node x, String key, int d) {
            if (x == null) return null;
            if (d == key.length()) return x;
            int c = key.charAt(d) - 'A';
            return get(x.next[c], key, d+1);
        }

        public void add(String key) {
            root = add(root, key, 0);
        }

        private Node add(Node node, String key, int d) {
            if (node == null) node = new Node();
            if (d == key.length()) {
                node.isWord = true;
                return node;
            }

            int c = key.charAt(d) - 'A';
            node.next[c] = add(node.next[c], key, d + 1);
            return node;
        }

        public boolean has(String key) {
            return hasWord(key);
        }

        public boolean hasWord(String key) {
            Node node = get(root, key, 0);
            if (node == null) return false;
            return node.isWord;
        }

        public boolean hasPrefix(String key) {
            return get(root, key, 0) != null;
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
