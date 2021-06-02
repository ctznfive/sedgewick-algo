import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

public class WordNet {
    private final Map<String, List<Integer>> nouns = new HashMap<>();
    private final List<String> synsets = new ArrayList<String>();
    private final Digraph digraph;
    private final SAP sap;
    private int size = 0;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();

        In file = new In(synsets);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] array = line.split(",");
            this.synsets.add(array[1]);

            String[] nouns = array[1].split(" ");
            for (String noun : nouns) {
                List<Integer> bag = this.nouns.get(noun);
                if (bag == null) {
                    bag = new ArrayList<Integer>();
                    this.nouns.put(noun, bag);
                }
                bag.add(size);
            }
            size++;
        }

        this.digraph = new Digraph(this.size);

        file = new In(hypernyms);
        while (file.hasNextLine()) {
            String line = file.readLine();
            String[] array = line.split(",");

            for (int i = 1; i < array.length; i++) {
                digraph.addEdge(Integer.parseInt(array[0]),
                                Integer.parseInt(array[i]));
            }
        }

        DirectedCycle cycle = new DirectedCycle(this.digraph);
        if (cycle.hasCycle())
            throw new IllegalArgumentException();

        int root = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (!this.digraph.adj(i).iterator().hasNext()) {
                if (root == 1)
                    throw new IllegalArgumentException();

                root++;
            }
        }

        this.sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();

        return nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        return sap.length(nouns.get(nounA),
                          nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();

        return synsets.get(sap.ancestor(nouns.get(nounA),
                                        nouns.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        StdOut.println(wordNet.nouns());
    }
}
