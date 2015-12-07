
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class BurrowsWheeler {

    public static void encode() {
        String in = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(in);
        for (int i = 0; i < in.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
                break;
            }
        }
        for (int i = 0; i < in.length(); i++) {
            BinaryStdOut.write(in.charAt((csa.index(i) - 1 + in.length()) % in.length()));
        }
        BinaryStdOut.flush();
    }

    public static void decode() {
        BinaryStdOut.write(d(BinaryStdIn.readInt(), BinaryStdIn.readString().toCharArray()));
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else {
            decode();
        }
//        String s = "";
//        for (int i = 0; i < 1000; i++) {
//            s += i;
//        }
//        p(s);
    }

    private static void p(String in) {
        int pos = 0;
        char[] t = new char[in.length()];
        {
            CircularSuffixArray csa = new CircularSuffixArray(in);
            for (int i = 0; i < in.length(); i++) {
                if (csa.index(i) == 0) {
                    pos = i;
                    break;
                }
            }
            for (int i = 0; i < in.length(); i++) {
                t[i] = in.charAt((csa.index(i) - 1 + in.length()) % in.length());
            }
        }
        {
            List<Integer> bin = new ArrayList(t.length);
            for (char c : t) {
                bin.add((int) c);
            }
            System.out.println(e(bin).stream().map(i -> "" + i).reduce((s1, s2) -> s1 + " " + s2).get());
        }
        {
            System.out.println(d(pos, t));
        }
    }

    private static String d(int pos, char[] t) {
        int[] count = new int[256];
        for (char c : t) {
            count[(int) c]++;
        }
        int p = 0;
        for (int i = 1; i < 256; i++) {
            int np = count[i];
            count[i] = p + count[i - 1];
            p = np;
        }
        int[] next = new int[t.length];
        for (int i = 0; i < t.length; i++) {
            char c = t[i];
            int j = count[(int) c]++;
            next[j] = i;
        }
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < t.length; i++) {
            for (int j = 0;; j++) {
                if (count[j] > pos) {
                    s.append((char) j);
                    pos = next[pos];
                    break;
                }
            }
        }
        return s.toString();
    }

    private static List<Integer> e(List<Integer> in) {
        List<Integer> out = new LinkedList();
        List<Integer> l = new ArrayList(256);
        for (int i = 0; i < 256; i++) {
            l.add(i);
        }
        for (int c : in) {
            int i = l.indexOf(c);
            l.remove((int) i);
            l.add(0, c);
            out.add(i);
        }
        return out;
    }
}
