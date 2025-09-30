import java.util.*;

public class linkState {
    static final int INF = 999;

    static void dijkstra(int src, int[][] cost, int n) {
        int[] dist = new int[n];
        int[] parent = new int[n];
        boolean[] visited = new boolean[n];

        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        dist[src] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = -1, min = INF;
            for (int i = 0; i < n; i++) {
                if (!visited[i] && dist[i] < min) {
                    min = dist[i];
                    u = i;
                }
            }
            if (u == -1) break;
            visited[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visited[v] && cost[u][v] != INF && dist[u] + cost[u][v] < dist[v]) {
                    dist[v] = dist[u] + cost[u][v];
                    parent[v] = u;
                }
            }
        }

        // Print routing table for this source
        System.out.println("\nRouting table for node " + src + ":");
        System.out.println("Dest\tCost\tPath");
        for (int i = 0; i < n; i++) {
            if (i == src) continue;
            if (dist[i] == INF) {
                System.out.println(i + "\t∞\t-");
            } else {
                System.out.print(i + "\t" + dist[i] + "\t");
                printPath(i, parent);
                System.out.println();
            }
        }
    }

    static void printPath(int node, int[] parent) {
        if (node == -1) return;
        printPath(parent[node], parent);
        System.out.print(node + " ");
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of nodes: ");
        int n = sc.nextInt();

        int[][] cost = new int[n][n];
        System.out.println("Enter cost matrix (999 for ∞):");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = sc.nextInt();
            }
        }

        for (int src = 0; src < n; src++) {
            dijkstra(src, cost, n);
        }
    }
}
