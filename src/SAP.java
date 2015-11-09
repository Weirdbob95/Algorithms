
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class SAP {

    private final Digraph g;

    private class Node {

        private final Node parent;

        private Node(Node parent) {
            this.parent = parent;
        }

        private int dist() {
            return (parent == null) ? 0 : parent.dist() + 1;
        }
    }

    public SAP(Digraph g) {
        this.g = g;
    }

    private int search(Map<Integer, Node> vAnc, Map<Integer, Node> wAnc, Queue<Integer> toCheck, boolean returnAncestor) {
        while (!toCheck.isEmpty()) {
            int i = toCheck.poll();
            Node nv = vAnc.get(i);
            Node nw = wAnc.get(i);
            if (nv != null && nw != null) {
                if (returnAncestor) {
                    return i;
                } else {
                    return nv.dist() + nw.dist();
                }
            }
            if (nv != null) {
                for (int i2 : g.adj(i)) {
                    vAnc.put(i2, new Node(nv));
                    toCheck.add(i2);
                }
            } else {
                for (int i2 : g.adj(i)) {
                    wAnc.put(i2, new Node(nw));
                    toCheck.add(i2);
                }
            }
        }
        return -1;
    }

    public int length(int v, int w) {
        Map<Integer, Node> vAnc = new HashMap();
        Map<Integer, Node> wAnc = new HashMap();
        vAnc.put(v, new Node(null));
        wAnc.put(w, new Node(null));

        Queue<Integer> toCheck = new LinkedList();
        toCheck.add(v);
        toCheck.add(w);

        return search(vAnc, wAnc, toCheck, false);
    }

    public int ancestor(int v, int w) {
        Map<Integer, Node> vAnc = new HashMap();
        Map<Integer, Node> wAnc = new HashMap();
        vAnc.put(v, new Node(null));
        wAnc.put(w, new Node(null));

        Queue<Integer> toCheck = new LinkedList();
        toCheck.add(v);
        toCheck.add(w);

        return search(vAnc, wAnc, toCheck, true);
    }

    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        Map<Integer, Node> vAnc = new HashMap();
        Map<Integer, Node> wAnc = new HashMap();
        v.forEach(i -> vAnc.put(i, new Node(null)));
        w.forEach(i -> wAnc.put(i, new Node(null)));

        Queue<Integer> toCheck = new LinkedList();
        v.forEach(toCheck::add);
        w.forEach(toCheck::add);

        return search(vAnc, wAnc, toCheck, false);
    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        Map<Integer, Node> vAnc = new HashMap();
        Map<Integer, Node> wAnc = new HashMap();
        v.forEach(i -> vAnc.put(i, new Node(null)));
        w.forEach(i -> wAnc.put(i, new Node(null)));

        Queue<Integer> toCheck = new LinkedList();
        v.forEach(toCheck::add);
        w.forEach(toCheck::add);

        return search(vAnc, wAnc, toCheck, true);
    }

    public static void main(String[] args) {
        //args = new String[]{"wordnet/digraph1.txt"};

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
