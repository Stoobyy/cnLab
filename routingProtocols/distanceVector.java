import java.util.Scanner;

public class distanceVector {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int INF = 999;

        System.out.print("Enter number of nodes: ");
        int n = sc.nextInt();

        int[][] cost = new int[n][n];
        int[][] dist = new int[n][n];
        int[][] nextHop = new int[n][n];

        System.out.println("Enter cost matrix (999 for ∞):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = sc.nextInt();
                dist[i][j] = cost[i][j];
                if (i != j && cost[i][j] != INF)
                    nextHop[i][j] = j; // direct neighbor
                else
                    nextHop[i][j] = -1; // unknown
            }
        }

        boolean changed;
        do {
            changed = false;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        if (dist[i][k] != INF && dist[k][j] != INF) {
                            if (dist[i][j] > dist[i][k] + dist[k][j]) {
                                dist[i][j] = dist[i][k] + dist[k][j];
                                nextHop[i][j] = nextHop[i][k];
                                changed = true;
                            }
                        }
                    }
                }
            }
        } while (changed);

        // print routing tables
        for (int i = 0; i < n; i++) {
            System.out.println("\nRouting table for node " + i + ":");
            System.out.println("Dest\tCost\tNextHop");
            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                String nh = (nextHop[i][j] == -1) ? "-" : String.valueOf(nextHop[i][j]);
                if (dist[i][j] == INF)
                    System.out.println(j + "\t∞\t" + nh);
                else
                    System.out.println(j + "\t" + dist[i][j] + "\t" + nh);
            }
        }
    }
}
