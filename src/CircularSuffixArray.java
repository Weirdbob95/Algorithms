
import java.util.ArrayList;
import java.util.Collections;

public class CircularSuffixArray {

    private final int[] a;

    public CircularSuffixArray(String s) {
        int l = s.length();
        ArrayList<Pair> al = new ArrayList(l);
        for (int i = 0; i < l; i++) {
            al.add(new Pair(shift(s, i), i));
        }
        Collections.sort(al);
        a = new int[l];
        for (int i = 0; i < l; i++) {
            a[i] = al.get(i).i;
        }
    }

    public int index(int i) {
        return a[i];
    }

    public int length() {
        return a.length;
    }

    private String shift(String s, int i) {
        return s.substring(i) + s.substring(0, i);
    }

    private static class Pair implements Comparable<Pair> {

        String s;
        int i;

        public Pair(String s, int i) {
            this.s = s;
            this.i = i;
        }

        @Override
        public int compareTo(Pair o) {
            return s.compareTo(o.s);
        }
    }
}
