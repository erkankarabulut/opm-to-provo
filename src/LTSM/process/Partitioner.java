package LTSM.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.openprovenance.model.v1_1_a.Agent;
import org.openprovenance.model.v1_1_a.AgentRef;
import org.openprovenance.model.v1_1_a.Artifact;
import org.openprovenance.model.v1_1_a.ArtifactRef;
import org.openprovenance.model.v1_1_a.OpmGraphDocument;
import org.openprovenance.model.v1_1_a.Process;
import org.openprovenance.model.v1_1_a.ProcessRef;
import org.openprovenance.model.v1_1_a.Used;
import org.openprovenance.model.v1_1_a.WasControlledBy;
import org.openprovenance.model.v1_1_a.WasDerivedFrom;
import org.openprovenance.model.v1_1_a.WasGeneratedBy;
import org.openprovenance.model.v1_1_a.WasTriggeredBy;

import cytoscape.Cytoscape;

import Karma.query.KarmaAxis2Query;
import LTSM.dataStructure.Edge;
import LTSM.dataStructure.Graph;
import LTSM.dataStructure.Vertex;
import LTSM.dataStructure.VertexType;
//import pt.tumba.links.WebGraph;
//import cgl.webgraph.Graph;

public class Partitioner {
	LTSM.dataStructure.Graph D2IGraph = new LTSM.dataStructure.Graph();
	
	List<Set<Vertex>> partition = new ArrayList<Set<Vertex>>();
	
	pt.tumba.links.WebGraph webgraph = new pt.tumba.links.WebGraph();

	cgl.webgraph.Graph cglGraph = new cgl.webgraph.Graph();
    
	
	Map<String, Integer> timestamp = new HashMap<String, Integer>();
	Process[] processes = null;
	Artifact[] artifacts = null;
	Agent[] agents = null;

	public Partitioner(OpmGraphDocument doc) {
		/*
		 * Travel through all nodes and edges in the xml file, and assign the
		 * nodes without incident edges or only have edges coming into them
		 * timestamps 0;
		 */
		
		Vector vertexVector = new Vector();
		Vector edgeVector = new Vector();
		

		if (doc.getOpmGraph().getProcesses() != null) {
			processes = doc.getOpmGraph().getProcesses().getProcessArray();
			for (Process p : processes) {
				D2IGraph.addVertex(p.getId(), VertexType.PROCESS);
				String vertex = p.getId();// + " " + VertexType.PROCESS;
				vertexVector.add(vertex);
			}
		}

		if (doc.getOpmGraph().getArtifacts() != null) {
			artifacts = doc.getOpmGraph().getArtifacts().getArtifactArray();
			for (Artifact a : artifacts) {
				D2IGraph.addVertex(a.getId(), VertexType.ARTIFACT);
				String vertex = a.getId();// + " " + VertexType.ARTIFACT;
				vertexVector.add(vertex);
			}
		}

		if (doc.getOpmGraph().getAgents() != null) {
			agents = doc.getOpmGraph().getAgents().getAgentArray();
			for (Agent a : agents) {
				D2IGraph.addVertex(a.getId(), VertexType.AGENT);
				String vertex = a.getId();// + " " + VertexType.AGENT;
				vertexVector.add(vertex);
			}
		}

		WasGeneratedBy[] wgb = doc.getOpmGraph().getCausalDependencies()
				.getWasGeneratedByArray();
		for (WasGeneratedBy gb : wgb) {
			String ID = gb.getEffect().getRef()+"_WasGeneratedBy_"+ gb.getCause().getRef();
			gb.setId(ID);
			D2IGraph.addEdge(gb.getId(), gb.getEffect().getRef(), gb.getCause()
					.getRef());
			webgraph.addLink(gb.getEffect().getRef(), gb.getCause().getRef(), 1.0);
			String edge = gb.getEffect().getRef() + " " + gb.getCause().getRef();// + " " + "WasGeneratedBy";
			edgeVector.add(edge);
					
		}		
		//System.out.println("graph edges number = " + D2IGraph.getEdgesNumber());

		WasTriggeredBy[] wtb = doc.getOpmGraph().getCausalDependencies()
				.getWasTriggeredByArray();
		
		for (WasTriggeredBy tb : wtb) {
			String ID = tb.getEffect().getRef()+"_WasTriggeredBy_"+ tb.getCause().getRef();		
			tb.setId(ID);
			D2IGraph.addEdge(tb.getId(), tb.getEffect().getRef(), tb.getCause()
					.getRef());
			webgraph.addLink(tb.getEffect().getRef(), tb.getCause().getRef(), 1.0);			
			String edge = tb.getEffect().getRef() + " " + tb.getCause().getRef();// + " " + "WasTriggeredBy";
			edgeVector.add(edge);

		}
		//System.out.println("graph edges number = " + D2IGraph.getEdgesNumber());
	

		Used[] used = doc.getOpmGraph().getCausalDependencies().getUsedArray();
		for (Used u : used) {
			String ID = u.getEffect().getRef()+"_WasUsedBy_"+ u.getCause().getRef();		
			u.setId(ID);
			D2IGraph.addEdge(u.getId(), u.getEffect().getRef(), u.getCause()
					.getRef());
			webgraph.addLink(u.getEffect().getRef(), u.getCause().getRef(), 1.0);	
			String edge = u.getEffect().getRef() + " " + u.getCause().getRef();// + " " + "WasUsedBy";
			edgeVector.add(edge);
			
		}
		//System.out.println("graph edges number = " + D2IGraph.getEdgesNumber());

		
		WasControlledBy[] wcb = doc.getOpmGraph().getCausalDependencies()
				.getWasControlledByArray();
		for (WasControlledBy cb : wcb) {
			String ID = cb.getEffect().getRef()+"_WasControlledBy_"+ cb.getCause().getRef();		
			cb.setId(ID);
			D2IGraph.addEdge(cb.getId(), cb.getEffect().getRef(), cb.getCause()
					.getRef());
			webgraph.addLink(cb.getEffect().getRef(), cb.getCause().getRef(), 1.0);	
			String edge = cb.getEffect().getRef() + " " + cb.getCause().getRef();// + " " + "WasControlledBy";
			edgeVector.add(edge);			
		}
		//System.out.println("graph edges number = " + D2IGraph.getEdgesNumber());

		
		WasDerivedFrom[] wdf = doc.getOpmGraph().getCausalDependencies()
				.getWasDerivedFromArray();
		for (WasDerivedFrom df : wdf) {
			String ID = df.getEffect().getRef()+"_WasDerivedFrom_"+ df.getCause().getRef();		
			df.setId(ID);
			D2IGraph.addEdge(df.getId(), df.getEffect().getRef(), df.getCause()
					.getRef());
			webgraph.addLink(df.getEffect().getRef(), df.getCause().getRef(), 1.0);	
			String edge = df.getEffect().getRef() + " " + df.getCause().getRef();// + " " + "WasDerivedFrom";
			edgeVector.add(edge);				
		}
		//System.out.println("graph edges number = " + D2IGraph.getEdgesNumber());
		
		//WebGraph creation is already done above.
		//CGL Graph creation
		cglGraph.fillGraph(vertexVector, edgeVector);
		
		Iterator<Vertex> itr = D2IGraph.getAllVertex().iterator();
		while (itr.hasNext()) {
			Vertex v = itr.next();

			if (v.getOut().isEmpty()) {
				timestamp.put(v.toString(), 0);
				calculate(v);
			}
		}	
		
		//partition is done on the graph
		partition();
	}
	
	public Graph getGraph(){
		return D2IGraph;
	}

	
	public pt.tumba.links.WebGraph getWebGraph(){
		return webgraph;
	}
	
	
	public cgl.webgraph.Graph getCGLGraph(){
		return cglGraph;
	}	
	/*
	 * Check all edges coming out of current node. If the effect of any of these
	 * edges has a timestamp that is not larger than the timestamp of current
	 * node, assign (the timestamp of current node + 1) to it and do step 3) on
	 * it;
	 */
	public void calculate(Vertex v) {
		List<Edge> edges = v.getIn();
		int ts = timestamp.get(v.toString());

		for (Edge e : edges) {
			if (!timestamp.containsKey(e.getSource().toString())
					|| timestamp.get(e.getSource().toString()) < ts + 1) {
				timestamp.put(e.getSource().toString(), ts + 1);
				calculate(e.getSource());
			}
		}
	}

	/*
	 * Group nodes into subsets based on their timestamps and node types, and
	 * then attach edges to their effects. Sort these subsets based the binary
	 * relation “happened before” defined in 3.3, so subset a happened before
	 * subset b will appear before subset b.
	 */
	private void partition() {
		Map<Integer, Set<Vertex>> tmp = new HashMap<Integer, Set<Vertex>>();

		Iterator<Vertex> itr = D2IGraph.getAllVertex().iterator();
		while (itr.hasNext()) {
			Vertex v = itr.next();

			Integer time = timestamp.get(v.toString());
			if (tmp.containsKey(time)) {
				tmp.get(time).add(v);
			} else {
				Set<Vertex> set = new HashSet<Vertex>();
				set.add(v);
				tmp.put(time, set);
			}
		}

		for (int i = 0; tmp.containsKey(i); i++) {
			Iterator<Vertex> itr_v = tmp.get(i).iterator();
			Set<Vertex> pset = new HashSet<Vertex>();
			Set<Vertex> atset = new HashSet<Vertex>();
			Set<Vertex> agset = new HashSet<Vertex>();

			while (itr_v.hasNext()) {
				Vertex v = itr_v.next();
				if (v.getType() == VertexType.PROCESS)
					pset.add(v);
				else if (v.getType() == VertexType.ARTIFACT)
					atset.add(v);
				else if (v.getType() == VertexType.AGENT)
					agset.add(v);
			}

			if (!pset.isEmpty())
				partition.add(pset);
			if (!atset.isEmpty())
				partition.add(atset);
			if (!agset.isEmpty())
				partition.add(agset);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < partition.size(); i++) {
			Iterator<Vertex> itr_v = partition.get(i).iterator();
			Vertex v = itr_v.next();
			if (v.getType() == VertexType.PROCESS)
				sb.append("\nPROCESS:\n" + v.toString() + "\n");
			else if (v.getType() == VertexType.ARTIFACT)
				sb.append("\nARTIFACT:\n" + v.toString() + "\n");
			else if (v.getType() == VertexType.AGENT)
				sb.append("\nAGENT:\n" + v.toString() + "\n");

			while (itr_v.hasNext()) {
				v = itr_v.next();
				sb.append(v.toString() + "\n");
			}
		}

		return sb.toString();
	}
	
	public List<Set<Vertex>> getPartition(){
		return partition;
	}

	public static void main(String[] args) {
		//String serviceURL = "http://bitternut.cs.indiana.edu:31085/axis2/services/KarmaService";
		String serviceURL = "http://10.1.32.229:8080/axis2/services/KarmaService";
		
		try {
			KarmaAxis2Query axis2Tester = new KarmaAxis2Query(serviceURL);
			String id = "http://bitternut.cs.indiana.edu:33000/nam-wrf-4-278";
			String graph_response = axis2Tester
					.getWorkflowGraphWithoutAnnotation(id);
			String graph;
			/*
			 * Re-organize the result xml into a opm xml
			 */
			int start = graph_response.lastIndexOf("<v1:opmGraph");
			int end = graph_response.lastIndexOf("<");

			if (start == -1 || end == -1) {
				graph = graph_response;

				JOptionPane.showMessageDialog(Cytoscape.getDesktop(),
						"An invalid OPM graph returned");
			} else
				graph = graph_response.substring(start, end);

			OpmGraphDocument doc = OpmGraphDocument.Factory.parse(graph);
			Partitioner p = new Partitioner(doc);
			
			System.out.println(graph);
			System.out.println(p.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
