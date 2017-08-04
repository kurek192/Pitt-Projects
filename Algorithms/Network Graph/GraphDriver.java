//WILLIAM KUREK
//CS 1501 SUMMER 2017
//ASSIGNMENT 5

import java.io.*;
import java.util.*;

public final class GraphDriver {

    private static Graph graph;

    public static void main(String[] args) {

        Scanner fScan = null;
        try {
            fScan = new Scanner(new File(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("Input file error!");
            System.exit(0);
        }

        int V = Integer.parseInt(fScan.nextLine());

        fScan.nextLine();

        graph = new Graph(V);

        while (fScan.hasNextLine()) {

            String line = fScan.nextLine();
            String[] edgeRep = line.split(" ");

            // grab the data for each edge by file line
            int v = Integer.parseInt(edgeRep[0]);
            int w = Integer.parseInt(edgeRep[1]);
            double weight = (double) Integer.parseInt(edgeRep[2]);

            // initial edge representation
            graph.addEdge(v, w, weight);
            // bidirectional edge representation
            graph.addEdge(w, v, weight);

        }

        Scanner s = new Scanner(System.in);

        while (true) {

            System.out.println();

            System.out.println("Enter a command (H for list of commands): ");

            String[] input = s.nextLine().split(" ");

            if (input[0].equals("R")) {
                System.out.println();
                System.out.println("COMMAND R:");
                System.out.println("-----------------\n");
                graph.getReport();
            }
            if (input[0].equals("M")) {
                System.out.println();
                System.out.println("COMMAND M:");
                System.out.println("-----------------\n");
                graph.MST();
            }

            if (input[0].equals("S")) {
                System.out.println();
                System.out.println("COMMAND S " + Integer.parseInt(input[1]) + " " + Integer.parseInt(input[2]) + ":");
                System.out.println("-----------------\n");
                graph.getShortestPath(Integer.parseInt(input[1]), Integer.parseInt(input[2]));
            }
            if (input[0].equals("P")) {
                System.out.println();
                System.out.println("COMMAND P " + Integer.parseInt(input[1]) + " " + Integer.parseInt(input[2]) + " " + Integer.parseInt(input[3]) + ":");
                System.out.println("-----------------\n");
                int numPaths = graph.getDistinctPaths(Integer.parseInt(input[1]), Integer.parseInt(input[2]), Integer.parseInt(input[3]));
                System.out.println("Total Paths: " + numPaths);
            }

            if (input[0].equals("D")) {
                System.out.println();
                System.out.println("COMMAND D " + Integer.parseInt(input[1]) + ":");
                System.out.println("-----------------\n");
                graph.nodeDown(Integer.parseInt(input[1]));
            }
            if (input[0].equals("U")) {
                System.out.println();
                System.out.println("COMMAND U " + Integer.parseInt(input[1]) + ":");
                System.out.println("-----------------\n");
                graph.nodeUp(Integer.parseInt(input[1]));
            }
            if (input[0].equals("C")) {
                System.out.println();
                System.out.println("COMMAND C " + Integer.parseInt(input[1]) + " " + Integer.parseInt(input[2]) + " " + Integer.parseInt(input[3]) + ":");
                System.out.println("-----------------\n");
                graph.changeWeight(Integer.parseInt(input[1]), Integer.parseInt(input[2]), Integer.parseInt(input[3]));

            }
            if (input[0].equals("I")) { //EXTRA CREDIT INSERT COMMAND (INPUT MUST BE PERFECT/VALID OR ERROR WILL OCCUR)
                System.out.println();
                System.out.println("COMMAND I:");
                System.out.println("-----------------\n");
                graph.addVertex();
                System.out.println("How many new edges are associated with the new vertex?: ");
                int newEdges = s.nextInt();

                for (int i = 0; i < newEdges; i++) {
                    System.out.println("Please enter associated vertex " + (i + 1) + ": ");
                    int edge = s.nextInt();
                    System.out.println("Please enter new edge weight: ");
                    int weight = s.nextInt();
                    graph.addEdge(graph.V() - 1, edge, weight);
                    graph.addEdge(edge, graph.V() - 1, weight);
                }
            }
            if (input[0].equals("H")) {
                System.out.println();
                System.out.println(
                        "NETWORK GRAPH MAIN MENU:\n"
                        + "-------------------------------------------------\n"
                        + "R:        Report\n"
                        + "M:        Minimum Spanning Tree\n"
                        + "S i j:    Shortest Path between vertices i and j\n"
                        + "P i j x:  Distinct Paths between vertices i and j\n"
                        + "D i:      Turn Node <i> off\n"
                        + "U i:      Turn Node <i> on\n"
                        + "C i j x:  Change weight of edge i -> j to x\n"
                        + "I:        Insert new vertex\n"
                        + "Q:        Exit the program.\n"
                );
            }

            if (input[0].equals("Q")) {
                System.exit(0);
            }
        }
    }

}
