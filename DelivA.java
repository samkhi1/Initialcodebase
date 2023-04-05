import java.io.*;

// Class DelivA does the work for deliverable DelivA of the Prog340

public class DelivA {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	
	public DelivA( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "_out.txt" );
		outputFile = new File( outputFileName );
		if ( outputFile.exists() ) {    // For retests
			outputFile.delete();
		}
		
		try {
			output = new PrintWriter(outputFile);			
		}
		catch (Exception x ) { 
			System.err.format("Exception: %s%n", x);
			System.exit(0);
		}
		//System.out.println( "DelivA:  To be implemented");
		//output.println( "DelivA:  To be implemented");
		
		//Report how many nodes are in the graph.
		System.out.println("There are " + g.getNodeList().size() + " nodes in the graph\n");

		output.println("There are " + g.getNodeList().size() + " nodes in the graph\n");
		
		//Report how many edges are in the graph.

		System.out.println("There are " + g.getEdgeList().size() + " edges in the graph\n");

		output.println("There are " + g.getEdgeList().size() + " edges in the graph\n");



		//List the node(s) that have the most outgoing edges, stating how many outgoing edges there are for that/those node(s). Identify the nodes by their mnemonics.

		for(Node n : g.getNodeList()) { // Iterate thru each Node in the ArrayList 

		  System.out.print("Node " + n.getAbbrev() + " has " + n.getOutgoingEdges().size() + " outgoing edges to nodes ");

		  output.print("Node " + n.getAbbrev() + " has " + n.getOutgoingEdges().size() + " outgoing edges to nodes ");

		  int commaCounter = 0;

		  for(Edge e : n.getOutgoingEdges()) { // Iterate thru each outgoing Edge

		   System.out.print(e.getHead().getAbbrev());

		   output.print(e.getHead().getAbbrev());

		   if(commaCounter < n.getOutgoingEdges().size() - 1) {

		    System.out.print(", ");

		    output.print(", ");

		    commaCounter++;

		   }else {

		    System.out.print(".");

		    output.print(".");

		   }

		  }

		  System.out.println();

		  output.println();

		 }

		 

		//List the longest (highest numerical value) and shortest (lowest numerical value) edges. Identify the edges by their mnemonics.

		int maxEdgeVal = 0;

		int minEdgeVal = Integer.MAX_VALUE;

		Edge maxEdge = null;

		Edge minEdge = null;



		for (Edge e : g.getEdgeList()) {

		    int edgeVal = e.getDist();

		    if (edgeVal > maxEdgeVal) {

		        maxEdgeVal = edgeVal;

		        maxEdge = e;

		    }

		    if (edgeVal < minEdgeVal) {

		        minEdgeVal = edgeVal;

		        minEdge = e;

		    }

		}



		System.out.println("The longest (highest numerical value) edge is " + maxEdge.getTail().getAbbrev() +"->"+ maxEdge.getHead().getAbbrev());

		output.println("The longest (highest numerical value) edge is " + maxEdge.getTail().getAbbrev() +"->"+ maxEdge.getHead().getAbbrev());



		System.out.println("The shortest (lowest numerical value) edge is " + minEdge.getTail().getAbbrev() +"->"+ minEdge.getHead().getAbbrev());

		output.println("The shortest (lowest numerical value) edge is " + minEdge.getTail().getAbbrev() +"->"+ minEdge.getHead().getAbbrev());
	}

}
