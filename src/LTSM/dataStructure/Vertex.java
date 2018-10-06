package LTSM.dataStructure;

import java.util.ArrayList;
import java.util.List;

public class Vertex {
	String id;
	VertexType type;
	int ann_num;
	

	private List<Edge> inlinks;
	private List<Edge> outlinks;

	public Vertex(String id, VertexType type) {
		this.id = id;
		this.type = type;
		inlinks = new ArrayList<Edge>();
		outlinks = new ArrayList<Edge>();
	}
	
	public Vertex(String id, VertexType type, int ann_num) {
		this.id = id;
		this.type = type;
		this.ann_num = ann_num;
		inlinks = new ArrayList<Edge>();
		outlinks = new ArrayList<Edge>();
	}

	public List<Edge> getIn() {
		return inlinks;
	}

	public List<Edge> getOut() {
		return outlinks;
	}

	public String toString() {
		return id;
	}
	
	public VertexType getType(){
		return type;
	}
	
	public String getID(){
		return id;
	}
	
	public int getAnnotationNumber(){
		return ann_num;
	}
}
