package LTSM.dataStructure;

public class Annotation {
	String parent_id;

	public Annotation(String id) {
		this.parent_id = id;
	}

	public String toString() {
		return parent_id;
	}
	
	
	public String getID(){
		return parent_id;
	}

}
