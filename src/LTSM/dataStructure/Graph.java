package LTSM.dataStructure;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
	private Map<String, Edge> edges;
	private Map<String, Vertex> nodes;

	public Graph() {
		edges = new HashMap<String, Edge>();
		nodes = new HashMap<String, Vertex>();
	}

	public void addVertex(String nodeRef, VertexType type) {
		nodes.put(nodeRef, new Vertex(nodeRef, type));
	}
	
	public void addVertex(String nodeRef, VertexType type, int ann_num) {
		nodes.put(nodeRef, new Vertex(nodeRef, type, ann_num));
	}

	public void addEdge(String edgeRef, String sourceRef, String destinationRef) {
		Vertex source = nodes.get(sourceRef);
		Vertex destination = nodes.get(destinationRef);

		Edge edge = new Edge(edgeRef, source, destination);
		edges.put(edgeRef, edge);
		source.getOut().add(edge);
		destination.getIn().add(edge);
	}

	public void getVertex(String nodeRef) {
		nodes.get(nodeRef);
	}

	public void getEdge(String edgeRef) {
		edges.get(edgeRef);
	}

	public Collection<Vertex> getAllVertex() {
		return nodes.values();
	}

	public Collection<Edge> getAllEdges() {
		return edges.values();
	}
	
	public int getVertexNumber(){
		return nodes.values().size();
	}
	
	public int getEdgesNumber(){
		return edges.values().size();
	}
}
