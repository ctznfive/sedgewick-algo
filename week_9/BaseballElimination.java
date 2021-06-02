import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {

    private final String[] names;
    private final Object[] obj;
    private final boolean[] e;
    private final int[][] g;
    private final int[] w;
    private final int[] r;
    private final int[] ls;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        In file = new In(filename);
        int len = Integer.parseInt(file.readLine());

        names = new String[len];
        obj = new Object[len];
        e = new boolean[len];
        g = new int[len][len];
        w = new int[len];
        r = new int[len];
        ls = new int[len];

        for (int i = 0; i < len; i++) {
            String str = file.readLine();
            String[] line = str.trim().split(" +");
            names[i] = line[0];

            w[i]  = Integer.parseInt(line[1]);
            ls[i] = Integer.parseInt(line[2]);
            r[i]  = Integer.parseInt(line[3]);

            for (int j = 0; j < len; j++) {
                g[i][j] = Integer.parseInt(line[j + 4]);
            }
        }

        String winsTeam = "";
        int wins = 0;
        for (int i = 0; i < len; i++) {
            if (w[i] > wins) {
                winsTeam = names[i];
                wins = w[i];
            }
        }

        for (int i = 0; i < len; i++) {
            if (w[i] + r[i] < wins) {
                e[i] = true;
                Queue<String> queue = new Queue<String>();
                queue.enqueue(winsTeam);
                obj[i] = queue;
            }
        }

        if (len > 1) {
            for (int i = 0; i < len; i++) {
                if (e[i]) continue;

                int lenM = (len - 2) * (len - 1) / 2;
                int lenV = len + lenM + 1;

                FlowNetwork network = new FlowNetwork(lenV);
                int dx = 1;
                for (int j = 0; j < len - 1; j++) {
                    for (int k = j + 1; k < len; k++) {
                        if (i == j || i == k)
                            continue;
                        FlowEdge numGames = new FlowEdge(0, dx, g[j][k]);
                        network.addEdge(numGames);
                        dx++;
                    }
                }

                dx = 1;
                for (int j = 0; j < len - 2; j++) {
                    for (int k = j + 1; k < len - 1; k++) {
                        FlowEdge team1 = new FlowEdge(dx, j + lenM + 1, Double.POSITIVE_INFINITY);
                        FlowEdge team2 = new FlowEdge(dx, k + lenM + 1, Double.POSITIVE_INFINITY);
                        network.addEdge(team1);
                        network.addEdge(team2);
                        dx++;
                    }
                }

                int numWins = w[i] + r[i];
                int kx = 1;
                for (int j = 0; j < len; j++) {
                    if (i == j) {
                        kx = 0;
                        continue;
                    }

                    FlowEdge flowEdge;
                    if (numWins - w[j] >= 0)
                        flowEdge = new FlowEdge(j + lenM + kx, lenV - 1, numWins - w[j]);
                    else
                        flowEdge = new FlowEdge(j + lenM + kx, lenV - 1, 0);
                    network.addEdge(flowEdge);
                }

                FordFulkerson fulkerson = new FordFulkerson(network, 0, lenV - 1);
                Iterable<FlowEdge> edges = network.adj(0);
                for (FlowEdge edge : edges) {
                    if (edge.capacity() != edge.flow())
                        e[i] = true;
                }

                if (e[i]) {
                    Queue<String> cert = new Queue<String>();
                    kx = 1;
                    for (int j = 0; j < len; j++) {
                        if (j == i) {
                            kx = 0;
                            continue;
                        }

                        if (fulkerson.inCut(j + kx + lenM))
                            cert.enqueue(names[j]);
                    }
                    obj[i] = cert;
                }
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return names.length;
    }

    // all teams
    public Iterable<String> teams() {
        Queue<String> queue = new Queue<String>();
        for (String team : names) {
            queue.enqueue(team);
        }

        return queue;
    }

    // number of wins for given team
    public int wins(String team) {
        if (team == null)
            throw new IllegalArgumentException();

        int id = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(team)) id = i;
        }

        return w[id];
    }

    // number of losses for given team
    public int losses(String team) {
        if (team == null)
            throw new IllegalArgumentException();

        int id = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(team)) id = i;
        }

        return ls[id];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null)
            throw new IllegalArgumentException();

        int id = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(team)) id = i;
        }

        return r[id];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || team2 == null)
            throw new IllegalArgumentException();

        int idTeam1 = 0;
        int idTeam2 = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(team1)) idTeam1 = i;
            if (names[i].equals(team2)) idTeam2 = i;
        }

        return g[idTeam1][idTeam2];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null)
            throw new IllegalArgumentException();

        int id = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(team)) id = i;
        }

        return e[id];
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null)
            throw new IllegalArgumentException();

        int id = 0;
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(team)) id = i;
        }

        Queue<String> certificate = (Queue<String>) obj[id];
        return certificate;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                System.out.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    System.out.print(t + " ");
                }
                System.out.println("}");
            }
            else {
                System.out.println(team + " is not eliminated");
            }
        }
    }
}
