package LTSM.process;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.openprovenance.model.v1_1_a.OpmGraphDocument;

import cytoscape.Cytoscape;

import Karma.query.KarmaAxis2Query;
import LTSM.dataStructure.Vertex;
import LTSM.dataStructure.VertexType;

public class Representor {
	public String getTopoRepresentation(Partitioner pt) {

		/*
		 * <nodes type, number of nodes, average in-degree of nodes, average
		 * out-degree nodes >
		 */
		List<Set<Vertex>> partition = pt.getPartition();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < partition.size(); i++) {
			Set<Vertex> set = partition.get(i);
			Iterator<Vertex> itr_v = set.iterator();

			if(i!=0)
				sb.append(",");
			
			Vertex v = itr_v.next();
			if (v.getType() == VertexType.PROCESS)
				sb.append("PROCESS");
			else if (v.getType() == VertexType.ARTIFACT)
				sb.append("ARTIFACT");
			else if (v.getType() == VertexType.AGENT)
				sb.append("AGENT");

			sb.append("," + set.size());

			int inlinks = v.getIn().size();
			int outlinks = v.getOut().size();
			while (itr_v.hasNext()) {
				v = itr_v.next();
				inlinks += v.getIn().size();
				outlinks += v.getOut().size();
			}

			double avginlinks = inlinks / (double) set.size();
			double avgoutlinks = outlinks / (double) set.size();

			sb.append("," + avginlinks);
			sb.append("," + avgoutlinks);
		}
		return sb.toString();
	}

	public static void main(String args[]) {
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

			Representor rp = new Representor();
			String attr = rp.getTopoRepresentation(p);

			System.out.println(graph);
			System.out.println(p.toString());
			System.out.println(attr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
