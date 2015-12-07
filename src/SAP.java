
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class SAP {

    private final Digraph g;

    public SAP(Digraph g) {
        this.g = new Digraph(g);
    }

    public int length(int v, int w) {
        return search(Arrays.asList(v), Arrays.asList(w), false);
    }

    public int ancestor(int v, int w) {
        return search(Arrays.asList(v), Arrays.asList(w), true);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        return search(toList(v), toList(w), false);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        return search(toList(v), toList(w), true);
    }

    private int search(List<Integer> v, List<Integer> w, boolean retA) {
        int ancestor = -1, length = -1;
        Map<Integer, Integer> vc = new HashMap(), wc = new HashMap();

        for (int depth = 0; (length == -1 || depth < length) && (!v.isEmpty() || !w.isEmpty()); depth++) {
            List<Integer> toCheck = new LinkedList(v);
            v = new LinkedList();
            for (int n : toCheck) {
                if (!vc.containsKey(n)) {
                    if (wc.containsKey(n)) {
                        int l = wc.get(n) + depth;
                        if (length == -1 || l < length) {
                            length = l;
                            ancestor = n;
                        }
                    }
                    vc.put(n, depth);
                    g.adj(n).forEach(v::add);
                }
            }
            toCheck = new LinkedList(w);
            w = new LinkedList();
            for (int n : toCheck) {
                if (!wc.containsKey(n)) {
                    if (vc.containsKey(n)) {
                        int l = vc.get(n) + depth;
                        if (length == -1 || l < length) {
                            length = l;
                            ancestor = n;
                        }
                    }
                    wc.put(n, depth);
                    g.adj(n).forEach(w::add);
                }
            }
        }
        return retA ? ancestor : length;
    }

    private static <T> List<T> toList(Iterable<T> i) {
        List<T> r = new LinkedList();
        i.forEach(r::add);
        return r;
    }

    public static void main(String[] args) {
        args = new String[]{"wordnet/digraph4.txt"};

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
