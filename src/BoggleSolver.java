
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import java.util.*;

public class BoggleSolver {

    public static void main(String[] args) {
        args = new String[]{"dictionary-algs4.txt", "board-q.txt"};
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);

        long t = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            solver.getAllValidWords(board);
        }
        System.out.println(System.currentTimeMillis() - t);
    }

    private final Node root;

    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (String s : dictionary) {
            if (s.length() >= 3) {
                if (s.contains("Q")) {
                    if (s.equals(s.replaceAll("QU", "Q"))) {
                        continue;
                    }
                }
                root.addWord(s.replaceAll("QU", "Q"));
            }
        }
    }

    public Iterable<String> getAllValidWords(BoggleBoard board) {
        List<String> words = new LinkedList();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                r(board, words, new LinkedList(), root, new Pair(i, j));
            }
        }
        return new TreeSet(words);
    }

    private void r(BoggleBoard board, List<String> words, List<Pair> positions, Node n, Pair pos) {
        if (pos.a < 0 || pos.a >= board.rows() || pos.b < 0 || pos.b >= board.cols()) {
            return;
        }
        if (positions.contains(pos)) {
            return;
        }
        if (!n.hasSub(board.getLetter(pos.a, pos.b))) {
            return;
        }
        n = n.getSub(board.getLetter(pos.a, pos.b));
        positions.add(pos);
        if (n.isWord) {
            words.add(positions.stream().map(p -> "" + board.getLetter(p.a, p.b)).reduce(String::concat).get().replaceAll("Q", "QU"));
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                r(board, words, positions, n, new Pair(pos.a + i, pos.b + j));
            }
        }
        positions.remove(positions.size() - 1);
    }

    public int scoreOf(String word) {
        if (root.contains(word.replaceAll("QU", "Q"))) {
            switch (word.length()) {
                case 0:
                case 1:
                case 2:
                    return 0;
                case 3:
                case 4:
                    return 1;
                case 5:
                    return 2;
                case 6:
                    return 3;
                case 7:
                    return 5;
                default:
                    return 11;
            }
        } else {
            return 0;
        }
    }

    private static class Node {

        boolean isWord;
        Node[] subNodes = new Node[26];
        //Map<Character, Node> subNodes = new HashMap();

        void addWord(String s) {
            if (s.length() == 0) {
                isWord = true;
                return;
            }
            char h = s.charAt(0);
            getSub(h).addWord(s.substring(1));
        }

        boolean contains(String s) {
            if (s.length() == 0) {
                return isWord;
            }
            char h = s.charAt(0);
            return hasSub(h) && getSub(h).contains(s.substring(1));
        }

        Node getSub(char c) {
            if (!hasSub(c)) {
                return subNodes[c - 'A'] = new Node();
            }
            return subNodes[c - 'A'];
        }

        boolean hasSub(char c) {
            return subNodes[c - 'A'] != null;
        }
    }

    private static class Pair {

        int a, b;

        public Pair(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public boolean equals(Object o) {
            return ((Pair) o).a == a && ((Pair) o).b == b;
        }
    }
}
