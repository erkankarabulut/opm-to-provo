package LTSM.dataStructure;

public class Edge {
	String id;
	
	private Vertex source;
	private Vertex destination;
	private int annotation_number;
	
	public Edge(String id, Vertex source,Vertex destination){
		this.source = source;
		this.destination = destination;
	}

	public Edge(String id, Vertex source,Vertex destination, int ann_num){
		this.source = source;
		this.destination = destination;
		this.annotation_number = ann_num;
	}	
	
	public Vertex getSource(){
		return source;
	}
	
	public Vertex getDestination(){
		return destination;
	}
	
	public void setSource(Vertex source){
		this.source = source;
	}
	
	public void setDestination(Vertex destination){
		this.destination = destination;
	}
	
	public String toString(){
		return id;
	}
	
	public int getAnnotationNumber(){
		return annotation_number;
	}
	
	
}
