package WordNet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class WordNet {
    private HashMap<String, ArrayList<Integer>> nounMap;
    private ArrayList<String> idList;
    private Digraph digraph;
    private SAP sap;
    private int num = 0;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        In inSynsets = new In(synsets);
        In inHypernyms = new In(hypernyms);

        int id;
        nounMap = new HashMap<String, ArrayList<Integer>>();
        idList = new ArrayList<String>();

        while (!inSynsets.isEmpty()) {
            num++;
            String[] synsetLine = inSynsets.readLine().split(",");
            id = Integer.parseInt(synsetLine[0]);
            idList.add(synsetLine[1]);

            String[] nounSet = synsetLine[1].split(" ");
            for (String nounName : nounSet) {

                ArrayList<Integer> ids = nounMap.get(nounName);
                if (ids == null) {
                    ids = new ArrayList<Integer>(1);
                    ids.add(id);
                    nounMap.put(nounName, ids);
                } else {
                    ids.add(id);
                }
            }
        }

        digraph = new Digraph(num);
        while (!inHypernyms.isEmpty()) {
            String[] hypernymsLine = inHypernyms.readLine().split(",");
            id = Integer.parseInt(hypernymsLine[0]);
            for (int i = 1; i < hypernymsLine.length; i++) {
                digraph.addEdge(id, Integer.parseInt(hypernymsLine[i]));
            }
        }

        if (new DirectedCycle(digraph).hasCycle())
            throw new IllegalArgumentException("Digraph transferred has a circle!");

        int rootNum = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.indegree(i) > 0 && digraph.outdegree(i) == 0)
                rootNum++;
        }
        if (rootNum > 1)
            throw new IllegalArgumentException("Digraph transferred has multi-roots!");

        sap = new SAP(digraph);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        ArrayList<String> list = new ArrayList<String>(num);
        Iterator<Map.Entry<String, ArrayList<Integer>>> iterator = nounMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, ArrayList<Integer>> entry = iterator.next();
            list.add(entry.getKey());
        }
        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }

    // is the nounMap a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return this.nounMap.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        return idList.get(sap.ancestor(nounMap.get(nounA), nounMap.get(nounB)));
    }

}
