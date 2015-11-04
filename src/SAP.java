
import edu.princeton.cs.algs4.Digraph;
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

    public int length(int v, int w) {
        Map<Integer, Node> vAnc = new HashMap();
        Map<Integer, Node> wAnc = new HashMap();
        vAnc.put(v, new Node(null));
        wAnc.put(w, new Node(null));

        Queue<Integer> toCheck = new LinkedList();
        toCheck.add(v);
        toCheck.add(w);

        while (!toCheck.isEmpty()) {
            int i = toCheck.poll();
            Node nv = vAnc.get(i);
            Node nw = wAnc.get(i);
            if (nv != null && nw != null) {
                return nv.dist() + nw.dist();
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

    public int ancestor(int v, int w) {
        Map<Integer, Node> vAnc = new HashMap();
        Map<Integer, Node> wAnc = new HashMap();
        vAnc.put(v, new Node(null));
        wAnc.put(w, new Node(null));

        Queue<Integer> toCheck = new LinkedList();
        toCheck.add(v);
        toCheck.add(w);

        while (!toCheck.isEmpty()) {
            int i = toCheck.poll();
            Node nv = vAnc.get(i);
            Node nw = wAnc.get(i);
            if (nv != null && nw != null) {
                return i;
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

    public int length(Iterable<Integer> v, Iterable<Integer> w) {

    }

    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

    }

    public static void main(String[] args) {

    }
}
