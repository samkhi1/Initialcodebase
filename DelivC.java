package Workspace;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class DelivC {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph graph;

	public DelivC(File in, Graph graph) {
		inputFile = in;
		this.graph = graph;

		// Get output file name.
		String inputFileName = inputFile.toString();
		String outputFileName = inputFileName.split("\\.")[0].concat("_out.txt");
		outputFile = new File(outputFileName);
		if (outputFile.exists()) { // For retests
			outputFile.delete();
		}

		try {
			output = new PrintWriter(outputFile);
		} catch (Exception x) {
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		// Print out the final Goal path and lenght.
		printGoal(ID_search(this.graph));
		output.flush();
	}

	/**
	 * search for the correct path.
	 * 
	 * @param graph - start path
	 * @return the correct path - Graph
	 */
	
    private Graph ID_search(Graph graph) {
    // Find the start and goal nodes
    Node startNode = findNode(graph, "S");
    Node goalNode = findNode(graph, "G");
    if (startNode == null || goalNode == null) {
        // Display an error message if either start or goal node is missing
        System.out.println("Error: Start or goal node not found.");
        return null;
    }

    // Initialize distance values for all nodes
    for (Node node : graph.getNodeList()) {
        node.setDistance(Double.POSITIVE_INFINITY);
    }
    startNode.setDistance(0.0);

    // Initialize the priority queue with the start node
    PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(Node::getDistance));
    pq.offer(startNode);

    // Initialize a map to store the visited nodes
    Map<Node, Boolean> visited = new HashMap<>();

    // Loop until the priority queue is empty or the goal node is found
    while (!pq.isEmpty()) {
        Node currNode = pq.poll();

        // Check if the current node is the goal node
        if (currNode == goalNode) {
            // Return the path to the goal node
            return getPathToNode(graph, goalNode);
        }

        // Check if the current node has already been visited
        if (visited.getOrDefault(currNode, false)) {
            continue;
        }
        visited.put(currNode, true);

        // Visit all neighbors of the current node
        for (Edge edge : currNode.getOutgoingEdges()) {
            Node neighborNode = edge.getTargetNode();

            // Check if the neighbor node has already been visited
            if (visited.getOrDefault(neighborNode, false)) {
                continue;
            }

            // Compute the tentative distance to the neighbor node
            double tentativeDistance = currNode.getDistance() + edge.getWeight();

            // Check if the tentative distance is less than the current distance to the neighbor node
            if (tentativeDistance < neighborNode.getDistance()) {
                neighborNode.setDistance(tentativeDistance);
                neighborNode.setPreviousNode(currNode);

                // Update the priority queue with the new distance
                pq.offer(neighborNode);
            }
        }
    }

    // Return null if the goal node is not reachable from the start node
    return null;
}

	/**
	 * use recursive function for run all the Graph and find out the shortest path
	 * to reach the goal node. search by depth bound for the correct path.
	 * 
	 * @param path - Graph (for find out the goal node),
	 * @param bound	   - int (bound: increasing by 1 for testing each path),
	 * @param queue - PriorityQueue<GraphWithLength> (store each path and pop out the
	 *            smallest path length)
	 * @return the next path - Graph
	 */
	
	private Graph depth_bounded_search(Graph path, int bound, PriorityQueue<WeightedGraph> queue,
			boolean[] hasAnyNeighbors) {
    if (path.getNodeList().get(path.getNodeList().size() - 1).getVal().equals("G")) {
        return path;
    }
    for (Edge e : path.getNodeList().get(path.getNodeList().size() - 1).getOutgoingEdges()) {
        Node neighborNode = e.getToNode();
        if (!path.contains(neighborNode)) {
            Graph newPath = new Graph(path);
            newPath.addEdge(e);
            // Compute the distance of the new path
            double distance = newPath.getNodeList().stream()
                    .mapToDouble(Node::getDistanceFromStart)
                    .sum();
            // Add the new path to the priority queue based on its distance
            queue.add(new WeightedGraph(newPath, distance));
        }
    }
    if (queue.isEmpty()) {
        hasAnyNeighbors[0] = false;
        return null;
    }
    // Remove the path with the smallest distance from the start node
    WeightedGraph shortestPath = queue.poll();
    if (shortestPath.graph.getNodeList().get(shortestPath.graph.getNodeList().size() - 1).getVal().equals("G")) {
        return shortestPath.graph;
    }
    hasAnyNeighbors[0] = true;
    return depth_bounded_search(shortestPath.graph, bound, queue, hasAnyNeighbors);
}
	}

	/**
	 * create new path from a tail node of current path and add to pathPQ.
	 * 
	 * @param queue - PriorityQueue<GraphWithLength>,
	 * @param path - Graph
	 * @param node - Node
	 */
	private void addNewPathToPathPQ(PriorityQueue<WeightedGraph> queue, Graph path, Node node) {
		ArrayList<Edge> edges = node.getOutgoingEdges();
		ArrayList<Node> nodes = path.nodeList;
		for (Edge edge : edges) {
			if (!nodes.contains(edge.head)) {
				WeightedGraph newPath = new WeightedGraph(path, edge);
				queue.add(newPath);
			}
		}
	}

	/**
	 * create Priority Queue that store object of path and length. sorted by path's
	 * length. In case of a tie, break ties alphabetically.
	 * 
	 * @return a new Priority Queue - PriorityQueue<GraphWithLength>
	 */
	private PriorityQueue<WeightedGraph> createPriorityQueuePath() {
		return new PriorityQueue<WeightedGraph>(new Comparator<WeightedGraph>() {
			@Override
			public int compare(WeightedGraph g1, WeightedGraph g2) {
				int gInt = g1.length - g2.length;
				if (gInt == 0) {
					// In case of a tie, break ties alphabetically.
					return g1.abbrev.compareTo(g2.abbrev);
				}
				return gInt;
			}
		});
	}

	/**
	 * find the node from graph with a value string.
	 * 
	 * @param pathPQ - PriorityQueue<GraphWithLength>, path - Graph, n - Node
	 * 
	 * @return the found node - Node
	 */
	private Node findNode(Graph G, String s) {
		for (Node n : G.nodeList) {
			if (n.getVal().equals(s)) {
				return n;
			}
		}
		return null;
	}

	/**
	 * print out each depth of path and length.
	 * 
	 * @param instance of GraphWithLength class
	 */
	private void printEachDepth(WeightedGraph gl) {
		String str = "Depth " + gl.length + ":\n";
		for (Node n : gl.graph.nodeList) {
			str += n.abbrev;
		}
		str += "\n";
		System.out.println(str);
		output.println(str);
	}

	/**
	 * print out the final goal depth of path and length.
	 * 
	 * @param path - Graph
	 */
	public void printGoal(Node node) {
    if (node == null) {
        System.out.println("Goal node not found");
        return;
    }
    ArrayList<String> path = new ArrayList<>();
    Node current = node;
    while (current != null) {
        path.add(current.getState());
        current = current.getParent();
    }
    Collections.reverse(path);
    System.out.println("Path to the goal:");
    for (String state : path) {
        System.out.println(state);
    }
    System.out.println("Number of steps: " + (path.size() - 1));
}

/**
 * create this class for content a path and it length and the final abbrev of
 * node of path.
 * 
 * To store in the Priority Queue then sorted by the length.
 * 
 */
class WeightedGraph {
	Graph graph;
	int length;
	String abbrev;

	/**
	 * Constructor for build a object to store in Priority Queue.
	 * 
	 * @param path - Graph, e - Edge
	 */
	public WeightedGraph(Graph path, Edge e) {
		this.graph = createGraph(path, e);
		this.length = totalEdgeDistanceInGraph(graph);
		this.abbrev = e.getHead().getAbbrev();
	}

	/**
	 * create a new path by cloning form old path and add a new edge and node to the
	 * end.
	 * 
	 * @param path - Graph, e - Edge
	 * 
	 * @return a new path - Graph
	 */
	private Graph createGraph(Graph path, Edge e) {
		Graph newG = new Graph();
		newG.edgeList.addAll(path.getEdgeList());
		newG.nodeList.addAll(path.getNodeList());
		newG.addEdge(e);
		newG.addNode(e.head);
		return newG;
	}

	/**
	 * Calculate the length of path by sum all the distance of each edge.
	 * 
	 * @param path - Graph
	 * 
	 * @return sum - int
	 */
	private int totalEdgeDistanceInGraph(Graph path) {
		int sum = 0;
		for (Edge e : path.edgeList) {
			sum += e.getDist();
		}
		return sum;
	}

	@Override
	public String toString() {
		return "GraphWithLength [graph=" + graph + ", length=" + length + ", abbrev=" + abbrev + "]\n\n";
	}

}
