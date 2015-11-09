
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;
import java.util.stream.Collectors;

public class WordNet {

    private final Map<Integer, List<String>> synsets;
    private final Set<String> nouns;
    private final Digraph G;
    private final SAP sap;

    public WordNet(String synsets, String hypernyms) {
        this.synsets = new HashMap();
        for (String s : new In(synsets).readAllLines()) {
            this.synsets.put(Integer.parseInt(s.substring(0, s.indexOf(","))),
                    Arrays.asList(s.substring(s.indexOf(",") + 1, s.indexOf(",", s.indexOf(",") + 1)).split(" ")));
        }
        nouns = new HashSet();
        this.synsets.forEach((k, l) -> l.forEach(nouns::add));

        String[] ha = new In(hypernyms).readAllLines();
        G = new Digraph(ha.length);
        for (String s : ha) {
            String[] a = s.split(",");
            for (int i = 1; i < a.length; i++) {
                G.addEdge(Integer.parseInt(a[0]), Integer.parseInt(a[i]));
            }
        }
        sap = new SAP(G);
    }

    public Iterable<String> nouns() {
        return nouns;
    }

    public boolean isNoun(String word) {
        return nouns.contains(word);
    }

    private List<Integer> getNoun(String noun) {
        return synsets.entrySet().stream().filter(e -> e.getValue().contains(noun)).map(e -> e.getKey()).collect(Collectors.toList());
    }

    public int distance(String nounA, String nounB) {
        return sap.length(getNoun(nounA), getNoun(nounB));
    }

    public String sap(String nounA, String nounB) {
        return synsets.get(sap.ancestor(getNoun(nounA), getNoun(nounB))).stream().reduce((s1, s2) -> s1 + " " + s2).get();
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets.txt", "hypernyms.txt");
        while (!StdIn.isEmpty()) {
            String n1 = StdIn.readString();
            String n2 = StdIn.readString();
            StdOut.println("Ancestor of " + n1 + " and " + n2 + " is (" + wordnet.sap(n1, n2) + ")");
        }
    }
}
