
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private final WordNet wordnet;

    private class Pair {

        private final String s1, s2;

        public Pair(String s1, String s2) {
            this.s1 = s1;
            this.s2 = s2;
        }
    }

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {
        int[] costs = new int[nouns.length];
        for (int i = 0; i < nouns.length; i++) {
            for (int j = i + 1; j < nouns.length; j++) {
                int cost = wordnet.distance(nouns[i], nouns[j]);
                costs[i] += cost;
                costs[j] += cost;
            }
        }
        int min = 0;
        for (int i = 1; i < nouns.length; i++) {
            if (costs[i] > costs[min]) {
                min = i;
            }
        }
        return nouns[min];
    }

    public static void main(String[] args) {
        args = new String[]{"wordnet/synsets.txt", "wordnet/hypernyms.txt", "wordnet/outcast5.txt"};

        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
