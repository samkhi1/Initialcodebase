// Edge between two nodes
public class Edge {
	
	int dist;
	Node tail;
	Node head;
	
	public Edge( Node tailNode, Node headNode, int dist ) {
		setDist( dist );
		setTail( tailNode );
		setHead( headNode );
	}
	
	public Node getSource() {
	    return tail;
	}

	public Node getDestination() {
	    return head;
	}

	public int getWeight() {
	    return dist;
	}
	
	public Node getTail() {
		return tail;
	}
	
	public Node getHead() {
		return head;
	}
	
	public int getDist() {
		return dist;
	}
	
	public void setTail( Node n ) {
		tail = n;
	}
	
	public void setHead( Node n ) {
		head = n;
	}
	
	public void setDist( int i ) {
		dist = i;
	}
}
