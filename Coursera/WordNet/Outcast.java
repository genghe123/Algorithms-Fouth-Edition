package WordNet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException("Argument to Outcast Initializer is null");
        wordNet = wordnet;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);

        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }

    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException("Argument to outcast() method is null");

        int[] distance = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                int dist = wordNet.distance(nouns[i], nouns[j]);
                distance[i] += dist;
                distance[j] += dist;
            }
        }

        int maxDistance = 0;
        int maxIndex = 0;
        for (int i = 0; i < distance.length; i++) {
            if (distance[i] > maxDistance) {
                maxDistance = distance[i];
                maxIndex = i;
            }
        }
        return nouns[maxIndex];
    }
}
