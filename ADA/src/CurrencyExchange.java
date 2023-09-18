import java.util.*;

public class CurrencyExchange {

    static class Edge {
        int source, destination;
        double weight;

        Edge(int source, int destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    static boolean bellmanFord(double[][] graph) {
        int V = graph.length;
        double[] distance = new double[V];
        int[] predecessor = new int[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[0] = 0;

        for (int i = 0; i < V - 1; i++) {
            for (int u = 0; u < V; u++) {
                for (int v = 0; v < V; v++) {
                    if (graph[u][v] != 0 && distance[u] != Double.MAX_VALUE && distance[u] + graph[u][v] < distance[v]) {
                        distance[v] = distance[u] + graph[u][v];
                        predecessor[v] = u;
                    }
                }
            }
        }

        for (int u = 0; u < V; u++) {
            for (int v = 0; v < V; v++) {
                if (graph[u][v] != 0 && distance[u] != Double.MAX_VALUE && distance[u] + graph[u][v] < distance[v]) {
                    System.out.println("Arbitrage Opportunity Detected!");
                    printArbitrageCycle(predecessor, u);
                    return true;
                }
            }
        }

        System.out.println("No arbitrage opportunity detected.");
        return false;
    }

    static void printArbitrageCycle(int[] predecessor, int start) {
        int current = start;
        Set<Integer> visited = new HashSet<>();
        while (!visited.contains(current)) {
            visited.add(current);
            current = predecessor[current];
        }
        System.out.println("Arbitrage Cycle: ");
        printPath(predecessor, current);
        System.out.print(current);
    }

    static void printPath(int[] predecessor, int current) {
        if (predecessor[current] != -1) {
            printPath(predecessor, predecessor[current]);
        }
        System.out.print(current + " -> ");
    }

    public static void main(String[] args) {
        int n = 3; // Number of currencies
        double[][] exchangeRates = {
            {0, 0.651, 0.581},
            {1.531, 1, 0.952},
            {1.711, 1.049, 1}
        };

        bellmanFord(exchangeRates);
    }
}
