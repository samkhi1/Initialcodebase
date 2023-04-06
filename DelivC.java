import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DelivC<Graph> {

private final File inputFile;
private final File outputFile;
private final PrintWriter output;
private final Graph g;

public DelivC(File inputFile, Graph graph) {
    this.inputFile = inputFile;
    this.g = graph;

    // Create output file.
    String inputFileName = inputFile.toString();
    String outputFileName = inputFileName.split("\\.")[0].concat("_out.txt");
    this.outputFile = new File(outputFileName);

    if (outputFile.exists()) {
        outputFile.delete(); // For retests.
    }

    try {
        this.output = new PrintWriter(outputFile);
    } catch (Exception e) {
        System.err.format("Exception: %s%n", e);
        System.exit(0);
    }

    Graph shortestPath = IDSearch(this.g);

    if (shortestPath != null) {
        printGoal(shortestPath);
    }

    output.flush();
}

/**
 * Searches for the correct path using LCFS (Dijkstra's algorithm) until
 * the Goal node is found.
 *
 * @param graph - start path
 * @return the correct path - Graph
 */
private Graph IDSearch(Graph graph) {
    boolean[] hasAnyNeighbors = { true };
    int bound = 1;

    Node startNode = findNode(graph, "S");

    if (startNode.getOutgoingEdges().isEmpty()) {
        System.out.println("The start Node is not available and has no outgoing edges!");
        return null;
    }

    Graph path = new Graph();
    path.addNode(startNode);

    PriorityQueue<WeightedGraph> pathPQ = createPriorityQueuePath();

    while (hasAnyNeighbors[0]) {
        hasAnyNeighbors[0] = false;
        Graph res = depthBoundedSearch(path, bound, pathPQ, hasAnyNeighbors);
        if (res != null) {
            return res;
        }
        bound++;
    }
    return null;
}

/**
 * Uses recursive function to run all the Graph and find out the shortest path
 * to reach the goal node. Searches by LCFS until the bound is reached.
 *
 * @param path  - Graph (for finding the goal node),
 * @param bound - int (bound: increasing by 1 for testing each path),
 * @param queue - PriorityQueue<WeightedGraph> (store each path and pop out the
 *              smallest path length)
 * @return the next path - Graph
 */
private Graph depthBoundedSearch(Graph path, int bound, PriorityQueue<WeightedGraph> queue,
                                 boolean[] hasAnyNeighbors) {
    Node currNode = path.getNodeList().get(path.getNodeList().size() - 1);
    if (currNode.getVal().equals("G")) {
        return path;
    } else {
        List<Edge> outgoingEdges = currNode.getOutgoingEdges();
        List<Edge> validOutgoingEdges = new ArrayList<>();

        for (Edge e : outgoingEdges) {
            Node toNode = e.getToNode();
            if (!path.containsNode(toNode)) {
                validOutgoingEdges.add(e);
            }
        }

        if (!validOutgoingEdges.isEmpty()) {
            hasAnyNeighbors[0] = true;

            for (Edge e : validOutgoingEdges) {
                Node toNode = e.getToNode();
                Graph newPath = new Graph(path);
                newPath.addEdge(e);
                if (newPath.getTotalWeight() <= bound) {
                    addNewPathToPathPQ(queue, newPath, toNode);
                }
            }
        }
    }

    if (!queue.isEmpty() && queue.peek().length <= bound) {
        WeightedGraph gLPop = queue.poll();
        Graph currPath = gLPop.graph;
        Node tailNode = currPath.getNodeList().get(currPath.getNodeList().size() - 1);

        Graph res = depthBoundedSearch(currPath, bound, queue, hasAnyNeighbors);
        if (res != null) {
            return res;
        }
    }

    return null;
}
