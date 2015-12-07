
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MoveToFront {

    public static void encode() {
        List<Integer> in = new LinkedList();
        while (!BinaryStdIn.isEmpty()) {
            in.add(BinaryStdIn.readInt(8));
        }
        e(in).forEach(i -> BinaryStdOut.write(i, 8));
        BinaryStdOut.flush();
    }

    public static void decode() {
        List<Integer> in = new LinkedList();
        while (!BinaryStdIn.isEmpty()) {
            in.add(BinaryStdIn.readInt(8));
        }
        d(in).forEach(i -> BinaryStdOut.write(i, 8));
        BinaryStdOut.flush();
    }

    public static void main(String[] args) {
        if (args[0].equals("-")) {
            encode();
        } else {
            decode();
        }
        //p(100, 100, 100, 200, 200, 200, 100, 150, 250, 50, 100, 150, 250);
    }

    private static void p(Integer... a) {
        List<Integer> l = Arrays.asList(a);
        System.out.println(l);
        System.out.println(e(l));
        System.out.println(d(e(l)));
    }

    private static List<Integer> d(List<Integer> in) {
        List<Integer> out = new LinkedList();
        List<Integer> l = new ArrayList(256);
        for (int i = 0; i < 256; i++) {
            l.add(i);
        }
        for (int i : in) {
            int c = l.get(i);
            l.remove((int) i);
            l.add(0, c);
            out.add(c);
        }
        return out;
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
