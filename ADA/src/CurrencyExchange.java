// Benjamin Polglase
// 20118815
// Algorithm Design and Analysis S2 2023
// Software Assignment
// Solving Currency Exchange Problems

import java.util.ArrayList;
import java.util.Arrays;

public class CurrencyExchange {

    // Defining a static nested class named Edge to represent edges in the graph
    static class Edge {

        int source, destination;
        double weight;

        // Constructor to initialize Edge attributes
        Edge(int source, int destination, double weight) {
            this.source = source;
            this.destination = destination;
            this.weight = weight;
        }
    }

    // Bellman-Ford algorithm to detect arbitrage opportunities
    static void bellmanFord(Edge[] edges, int V, int source) {
        // Initialize an array to hold distances from source to all vertices
        double[] distance = new double[V];
        Arrays.fill(distance, Double.MAX_VALUE);
        distance[source] = 0;

        // Initialize an array to hold parent vertices for each vertex
        int[] parent = new int[V];
        Arrays.fill(parent, -1);

        // Relax edges repeatedly to find shortest paths
        for (int i = 1; i < V; i++) {
            for (Edge edge : edges) {
                if (distance[edge.source] + edge.weight < distance[edge.destination]) {
                    distance[edge.destination] = distance[edge.source] + edge.weight;
                    parent[edge.destination] = edge.source;
                }
            }
        }

        // Check for negative-weight cycles (arbitrage opportunities)
        boolean arbitrageDetected = false;
        for (Edge edge : edges) {
            if (distance[edge.source] + edge.weight < distance[edge.destination]) {
                arbitrageDetected = true;

                int start = edge.destination;
                ArrayList<Integer> cycle = new ArrayList<>();
                while (!cycle.contains(start)) {
                    cycle.add(start);
                    start = parent[start];
                }

                // Calculate exchange rate of the cycle
                double exchangeRate = 1.0;
                for (int i = 0; i < cycle.size() - 1; i++) {
                    int u = cycle.get(i);
                    int v = cycle.get(i + 1);
                    for (Edge e : edges) {
                        if (e.source == u && e.destination == v) {
                            exchangeRate *= Math.exp(-e.weight);
                            break;
                        }
                    }
                }

                System.out.println("Arbitrage Opportunity Detected!");
                System.out.println("Best Exchange Rate: " + exchangeRate);
                System.out.println("Arbitrage Cycle Path: " + cycle);

                break;
            }
        }

        // If no arbitrage is detected, just find the best exchange rate
        if (!arbitrageDetected) {
            double bestExchangeRate = 1.0;
            for (Edge edge : edges) {
                if (edge.weight < bestExchangeRate) {
                    bestExchangeRate = edge.weight;
                }
            }

            System.out.println("No Arbitrage Opportunity Detected");
            System.out.println("Best Exchange Rate: " + bestExchangeRate);
        }
    }


    static void findBestConversionRate(double[][] exchangeRates, int X, int Y) {
        int V = exchangeRates.length;
        Edge[] edges = new Edge[V * V];

        // Populate the edges array with exchange rate information
        int edgeIndex = 0;
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                edges[edgeIndex++] = new Edge(i, j, -Math.log(exchangeRates[i][j]));
            }
        }

        bellmanFord(edges, V, X);

        // Find and print the best exchange rate from X to Y
        double bestExchangeRate = Double.MAX_VALUE;
        ArrayList<Integer> bestCycle = new ArrayList<>();
        for (Edge edge : edges) {
            if (edge.source == X && edge.destination == Y && edge.weight < bestExchangeRate) {
                bestExchangeRate = edge.weight;
                bestCycle.clear();
                bestCycle.add(X);
                bestCycle.add(Y);
            }
        }

        if (bestExchangeRate != Double.MAX_VALUE) {
            System.out.println("\nBest Exchange Rate from " + X + " to " + Y + ": " + Math.exp(-bestExchangeRate));
            System.out.println("Exchange Path: " + bestCycle);
        } else {
            System.out.println("\nNo direct exchange rate found from " + X + " to " + Y);
        }
    }

    public static void main(String[] args) {

        //Example form assignment brief
        /*double[][] exchangeRates = {
            {1.0, 0.651, 0.581},
            {1.531, 1.0, 0.952},
            {1.711, 1.049, 1.0}
        };*/

        
        //This example is USD, Euro and GBP from xe.com
        /*double[][] exchangeRates = {
            {1.0, 0.950, 0.822},
            {1.052, 1.0, 0.865},
            {1.215, 1.155, 1.0}
        };*/

             
        // This example is CAN, AUS and NZD from xe.com
        double[][] exchangeRates = {
            {1.0, 1.160, 1.238},
            {0.861, 1.0, 1.067},
            {0.936, 0.807, 1.0}
        };

        int X = 0; // Currency USD
        int Y = 1; // Currency Euro

        findBestConversionRate(exchangeRates, X, Y);
    }
}
