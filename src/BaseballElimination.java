
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import static java.lang.Double.POSITIVE_INFINITY;
import java.util.LinkedHashMap;
import java.util.Map;

public class BaseballElimination {

    private final Map<String, Integer> teams;
    private final int[] wins, losses, remaining;
    private final int[][] against;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        int n = in.readInt();
        teams = new LinkedHashMap();
        wins = new int[n];
        losses = new int[n];
        remaining = new int[n];
        against = new int[n][n];
        for (int i = 0; i < n; i++) {
            teams.put(in.readString(), i);
            wins[i] = in.readInt();
            losses[i] = in.readInt();
            remaining[i] = in.readInt();
            for (int j = 0; j < n; j++) {
                against[i][j] = in.readInt();
            }
        }
    }

    public int numberOfTeams() {
        return teams.size();
    }

    public Iterable<String> teams() {
        return teams.keySet();
    }

    public int wins(String team) {
        return wins[teams.get(team)];
    }

    public int losses(String team) {
        return losses[teams.get(team)];
    }

    public int remaining(String team) {
        return remaining[teams.get(team)];
    }

    public int against(String team1, String team2) {
        return against[teams.get(team1)][teams.get(team2)];
    }

    public boolean isEliminated(String team) {
        int t = teams.get(team);
        int n = teams.size();
        FlowNetwork f = new FlowNetwork((n * n + n) / 2 + 1);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (i != t && j != t) {
                    int pos = n + i * i - i + j + 1;
                    FlowEdge e = new FlowEdge(0, pos, against[i][j]);
                    f.addEdge(e);
                    f.addEdge(new FlowEdge(pos, i + 1, POSITIVE_INFINITY));
                    f.addEdge(new FlowEdge(pos, j + 1, POSITIVE_INFINITY));
                    pos++;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            f.addEdge(new FlowEdge(i + 1, n * n, wins[t] + remaining[t] - wins[i]));
        }

        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
