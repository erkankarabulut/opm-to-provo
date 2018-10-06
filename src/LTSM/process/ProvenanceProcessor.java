package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import LTSM.dataStructure.Graph;
import LTSM.dataStructure.Vertex;
import LTSM.dataStructure.VertexType;
import pt.tumba.links.WebGraph;
import pt.tumba.links.PageRank;


import Karma.query.KarmaAxis2Query;

import org.apache.xmlbeans.XmlException;
import org.openprovenance.model.v1_1_a.Agent;
import org.openprovenance.model.v1_1_a.Artifact;
import org.openprovenance.model.v1_1_a.OpmGraphDocument;
//import Karma.query.JDBCquery;
//import cytoscape.Cytoscape;
import org.openprovenance.model.v1_1_a.Account;
import org.openprovenance.model.v1_1_a.AccountRef;
import org.openprovenance.model.v1_1_a.Process;
import org.openprovenance.model.v1_1_a.Used;
import org.openprovenance.model.v1_1_a.WasControlledBy;
import org.openprovenance.model.v1_1_a.WasDerivedFrom;
import org.openprovenance.model.v1_1_a.WasGeneratedBy;
import org.openprovenance.model.v1_1_a.WasTriggeredBy;

import cgl.main.mainpr;

public class ProvenanceProcessor {
	
	public static HashMap string_graph_ls = new HashMap();
	
	public static Connection conn = null;
	
	public ProvenanceProcessor() {
/*		
		String url = "jdbc:mysql://10.1.32.229:3306/karma";
		String user = "root";
		String password = "12345678";
		try {
			conn = DriverManager.getConnection(url, user, password);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
//				if (conn != null)
//					conn.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
*/	
	}



	public static void loadOPMGraphFromKarma(String graph_ref) {

		//OpmGraphDocument doc = null;
		try {
			String serviceURL = "http://10.1.32.229:8080/axis2/services/KarmaService";
			KarmaAxis2Query axis2Tester = new KarmaAxis2Query(serviceURL);

			String graph = null;
			String graph_response = axis2Tester.getWorkflowGraphWithoutAnnotation(graph_ref);
			int start = graph_response.lastIndexOf("<v1:opmGraph");
			int end = graph_response.lastIndexOf("<");
				if (start == -1 || end == -1) {
					graph = graph_response;
					System.out.println("Invalid: " + graph_ref);
				} else {
					graph = graph_response.substring(start, end);
				}


			//doc = OpmGraphDocument.Factory.parse(graph);
            
            //////////////////////////////////////////
			string_graph_ls.put(graph_ref, graph);
            //////////////////////////////////////////
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		//return doc;
	}
	
	public static String getOPMGraphFromKarma(String graph_ref) {
		String graph = null;
		try {
			String serviceURL = "http://10.1.32.229:8080/axis2/services/KarmaService";
			KarmaAxis2Query axis2Tester = new KarmaAxis2Query(serviceURL);
			String graph_response = axis2Tester.getWorkflowGraphWithoutAnnotation(graph_ref);
			int start = graph_response.lastIndexOf("<v1:opmGraph");
			int end = graph_response.lastIndexOf("<");
				if (start == -1 || end == -1) {
					graph = graph_response;
					System.out.println("Invalid: " + graph_ref);
				} else {
					graph = graph_response.substring(start, end);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;
	}
	

//    public static List<String> completeOPMGraphWithoutAgents(List<String> ls) {
//    	List<String> graph_ls = new ArrayList<String>();
//    	
//		for (String p : ls) {
//			String ref = getAccountRef(p); 
//			String new_graph = getFullConnectedGraphWithoutAgentInString(ref);
//			graph_ls.add(new_graph);
//		}   	
//    	return graph_ls;
//    }
	
    
    public static List<String> listOfcompleteOPMGraphs(List<String> ls, String agent) {
    	List<String> graph_ls = new ArrayList<String>();
    	
		for (String p : ls) {
			String ref = p;//getAccountRef(p); 
			String new_graph = getFullConnectedGraphWithAgentInString(ref, agent);
			graph_ls.add(new_graph);
		}   	
    	return graph_ls;
    }	

	public static String getAccountRef(String graph) {
	    String account_ref = "", account_info = "";		
	    int start1 = graph.lastIndexOf("<v1:accounts>");
	    int end1 = graph.lastIndexOf("</v1:accounts>");
	    String graph1 = "";
	    if (start1 == -1 || end1 == -1) {
		    graph1 = graph;
		    account_ref = null;
	    } else {
		    graph1 = graph.substring(start1, end1);
	       account_info = replace(graph1, "<v1:accounts><v1:account id=\"", "");	    
	       account_ref = replace(account_info, "\"/>", "");
	    }
		return account_ref;
	}	   
    
	public static void writeOPMGraphFromDBTOFile(String file_name) {
		
		List <String> ls = getIndecesOFGraphsFromIndexFile("C:\\maktas\\workspace\\LTSM\\fileDB\\index_files\\index_" + file_name);
		
		try {
            String v_name =  "C:\\maktas\\workspace\\LTSM\\fileDB\\data_files\\" + file_name; 
			FileWriter fw_v = new FileWriter(v_name, false);
			BufferedWriter bw_v = new BufferedWriter(fw_v);

			for (String p : ls) {
				String graph = getOPMGraphFromKarma(p);
				bw_v.write(graph+ "\n");   			
			} 
		
		bw_v.flush();
		bw_v.close();
		fw_v.close();
	} catch (Exception e) {
		e.printStackTrace();
	}		
				
	}

	public static void createWEBGRAPHFileFromOPMXMLFile(String file_name_with_path, String pagerank_data_folder_path, String workflow_name, int workflow_number) {

		String opmgraph = "";
		try {
			
			//String new_file_name = file_name_with_path;
			String new_file_name = ".\\provenance_xml\\nam_data_without_annotation.xml";
			FileReader fr = new FileReader(new_file_name);
			BufferedReader br = new BufferedReader(fr);

			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				String[] sa1 = myreadline.split("\t");				
				opmgraph = sa1[0];
				System.out.println(opmgraph);
			}

			br.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}		
		
		
		OpmGraphDocument doc = null;
		try {
			doc = OpmGraphDocument.Factory.parse(opmgraph);
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Vector vertexVector = new Vector();
		Vector edgeVector = new Vector();
		

		if (doc.getOpmGraph().getProcesses() != null) {
			Process[] processes = doc.getOpmGraph().getProcesses().getProcessArray();
			for (Process p : processes) {
				vertexVector.add(p.getId());
			}
		}

		if (doc.getOpmGraph().getArtifacts() != null) {
			Artifact[] artifacts = doc.getOpmGraph().getArtifacts().getArtifactArray();
			for (Artifact a : artifacts) {
				vertexVector.add(a.getId());
			}
		}

		if (doc.getOpmGraph().getAgents() != null) {
			Agent[] agents = doc.getOpmGraph().getAgents().getAgentArray();
			for (Agent a : agents) {
				vertexVector.add(a.getId());
			}
		}

		WasGeneratedBy[] wgb = doc.getOpmGraph().getCausalDependencies()
				.getWasGeneratedByArray();
		for (WasGeneratedBy gb : wgb) {
			String edge = gb.getEffect().getRef() + " " + gb.getCause().getRef();
			edgeVector.add(edge);
					
		}		

		WasTriggeredBy[] wtb = doc.getOpmGraph().getCausalDependencies()
				.getWasTriggeredByArray();
		
		for (WasTriggeredBy tb : wtb) {
			String edge = tb.getEffect().getRef() + " " + tb.getCause().getRef();
			edgeVector.add(edge);

		}

		Used[] used = doc.getOpmGraph().getCausalDependencies().getUsedArray();
		for (Used u : used) {
			String edge = u.getEffect().getRef() + " " + u.getCause().getRef();
			edgeVector.add(edge);
			
		}
		
		WasControlledBy[] wcb = doc.getOpmGraph().getCausalDependencies()
				.getWasControlledByArray();
		for (WasControlledBy cb : wcb) {
			String edge = cb.getEffect().getRef() + " " + cb.getCause().getRef();
			edgeVector.add(edge);			
		}
		
		WasDerivedFrom[] wdf = doc.getOpmGraph().getCausalDependencies()
				.getWasDerivedFromArray();
		for (WasDerivedFrom df : wdf) {
			String edge = df.getEffect().getRef() + " " + df.getCause().getRef();
			edgeVector.add(edge);				
		}
		
		//WebGraph creation is already done above.

		File folder1 = new File(pagerank_data_folder_path + "\\"+ workflow_name + "\\" + workflow_number);
		if (folder1.exists() != true) {
			folder1.mkdir();
		}
		File folder2 = new File(pagerank_data_folder_path + "\\"+ workflow_name + "\\" + workflow_number+ "\\"+ "input");
		if (folder2.exists() != true) {
			folder2.mkdir();
		}		
		
		try {
            String v_name = pagerank_data_folder_path + "\\"+ workflow_name + "\\"+ workflow_number + "\\"+ "input" + "\\" + "vertex.txt"; 
			//String v_name =  "C:\\maktas\\workspace\\LTSM\\data\\input\\vertex_" + file_name + ".txt"; 
            FileWriter fw_v = new FileWriter(v_name, false);
			BufferedWriter bw_v = new BufferedWriter(fw_v);

            String e_name=pagerank_data_folder_path + "\\"+ workflow_name + "\\"+ workflow_number + "\\"+ "input" + "\\" + "edge.txt";
			//String e_name =  "C:\\maktas\\workspace\\LTSM\\data\\input\\edge_" + file_name + ".txt"; 
			FileWriter fw_e = new FileWriter(e_name, false);
			BufferedWriter bw_e = new BufferedWriter(fw_e);	
			
            String c_name=pagerank_data_folder_path + "\\"+ workflow_name + "\\"+ workflow_number + "\\"+ "input" + "\\" + "count.txt";
			//String c_name =  "C:\\maktas\\workspace\\LTSM\\data\\input\\count_" + file_name + ".txt"; 
			FileWriter fw_c = new FileWriter(c_name, false);
			BufferedWriter bw_c = new BufferedWriter(fw_c);	
			int size = vertexVector.size();
			bw_c.write(size+ "\n");  
			
			
		Iterator<String> itr = vertexVector.iterator();
		while (itr.hasNext()) {
			String v = itr.next();
			bw_v.write(v+ "\n");             

		}	

		Iterator<String> edge_itr = edgeVector.iterator();
		while (edge_itr.hasNext()) {
			String v = edge_itr.next();
            bw_e.write(v+ "\n");  

		}	

		
		bw_v.flush();
		bw_v.close();
		fw_v.close();
		bw_e.flush();
		bw_e.close();
		fw_e.close();
		bw_c.flush();
		bw_c.close();
		fw_c.close();		
	} catch (Exception e) {
		e.printStackTrace();
	}			
		
		
	}


	public static String getOPMGraphInStringFromOPMXMLFile(String file_name_with_path) {

		String opmgraph = "";
		try {
			
			String new_file_name = file_name_with_path;
			FileReader fr = new FileReader(new_file_name);
			BufferedReader br = new BufferedReader(fr);

			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				String[] sa1 = myreadline.split("\t");				
				opmgraph = sa1[0];
//				System.out.println(opmgraph);
			}

			br.close();
			fr.close();

		} catch (Exception e) {
			e.printStackTrace();
		}		

		return opmgraph;
	}	
	
	
	
/*  
	public static String getOPMGraphInStringFromKarma(String graph_ref) {
		String graph = null;

		OpmGraphDocument doc = null;
		try {
			String serviceURL = "http://10.1.32.229:8080/axis2/services/KarmaService";
			KarmaAxis2Query axis2Tester = new KarmaAxis2Query(serviceURL);

			String graph_response = axis2Tester.getWorkflowGraphWithoutAnnotation(graph_ref);
			int start = graph_response.lastIndexOf("<v1:opmGraph");
			int end = graph_response.lastIndexOf("<");
				if (start == -1 || end == -1) {
					graph = graph_response;
					System.out.println("Invalid: " + graph_ref);
				} else {
					graph = graph_response.substring(start, end);
				}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return graph;
	}
	*/

	public static void loadGraphIntoMemory(List<String> ls) {
		for (String p : ls) {
			loadOPMGraphFromKarma(p);
		}  		
	}
	
//	public static String getFullConnectedGraphWithoutAgentInString(String graph_ref) {
//
//		String opmgraph = (String) string_graph_ls.get(graph_ref);
//		OpmGraphDocument doc = null;
//		try {
//			doc = OpmGraphDocument.Factory.parse(opmgraph);
//		} catch (XmlException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		Process[] processes = null;
//		String [] effect_cause = new String[2];
//		String accountref = null;
//
//		if (doc.getOpmGraph().getProcesses() != null) {
//			processes = doc.getOpmGraph().getProcesses().getProcessArray();
//            AccountRef[] acc_array = processes[0].getAccountArray();
//            accountref = acc_array[0].getRef();
//            effect_cause[0] = processes[0].getId(); 
//            effect_cause[1] = processes[1].getId(); 		
//		}
//		
//		String interaction0 = "<v1:causalDependencies>";
//		
//		String interaction1 = "<v1:wasTriggeredBy>";
//		String interaction2 = "<v1:effect ref=" + "\"" + effect_cause[1] + "\"" + "/>";
//		String interaction3 = "<v1:cause ref=" + "\"" + effect_cause[0] + "\"" + "/>";
//		String interaction4 = "<v1:account ref=" + "\"" + accountref + "\"" + "/>";
//		String interaction5 = "<v1:time noEarlierThan=\"2009-02-19T18:32:00.000-05:00\" noLaterThan=\"2009-02-19T18:32:00.000-05:00\"/>";
//		String interaction6 = "</v1:wasTriggeredBy>";	
//		
//		String interaction = interaction0 + interaction1 + interaction2 + interaction3 + interaction4 + interaction5+ interaction6;
//	
//		String graph = (String) string_graph_ls.get(graph_ref);//getOPMGraphInStringFromKarma(graph_ref);
//		String new_graph = replace(graph, interaction0, interaction);
//		return new_graph;
//		
//	}	

	public static String getFullConnectedGraphWithAgentInString(String graph_ref, String agent) {

		//OpmGraphDocument doc = (OpmGraphDocument) opm_graph_ls.get(graph_ref);
		String opmgraph = (String) string_graph_ls.get(graph_ref);
		OpmGraphDocument doc = null;
		try {
			doc = OpmGraphDocument.Factory.parse(opmgraph);
		} catch (XmlException e) {
			e.printStackTrace();
		}		
		
		Process[] processes = null;
		String [] effect_cause = new String[2];
		//String accountref = null;

		if (doc.getOpmGraph().getProcesses() != null) {
			processes = doc.getOpmGraph().getProcesses().getProcessArray();
            //AccountRef[] acc_array = processes[0].getAccountArray();
            //accountref = acc_array[0].getRef();
            effect_cause[0] = processes[0].getId(); 
            effect_cause[1] = processes[1].getId(); 		
		}
		
		String interaction0 = "<v1:causalDependencies>";
		
		String agent_interaction_1 = "<v1:wasTriggeredBy>";
		String agent_interaction_2 = "<v1:effect ref=" + "\"" + effect_cause[0] + "\"" + "/>";
		String agent_interaction_3 = "<v1:cause ref=" + "\"" + agent + "\"" + "/>";
		String agent_interaction_4 = "<v1:account ref=" + "\"" + graph_ref + "\"" + "/>";
		//String agent_interaction_5 = "<v1:time noEarlierThan=\"2009-02-19T18:32:00.000-05:00\" noLaterThan=\"2009-02-19T18:32:00.000-05:00\"/>";
		String agent_interaction_6 = "</v1:wasTriggeredBy>";	
		
		String agent_interaction = agent_interaction_1 + agent_interaction_2 + agent_interaction_3 + agent_interaction_4 + /*agent_interaction_5+*/ agent_interaction_6;
		
		String interaction1 = "<v1:wasTriggeredBy>";
		String interaction2 = "<v1:effect ref=" + "\"" + effect_cause[1] + "\"" + "/>";
		String interaction3 = "<v1:cause ref=" + "\"" + effect_cause[0] + "\"" + "/>";
		String interaction4 = "<v1:account ref=" + "\"" + graph_ref + "\"" + "/>";
		String interaction5 = "<v1:time noEarlierThan=\"2009-02-19T18:32:00.000-05:00\" noLaterThan=\"2009-02-19T18:32:00.000-05:00\"/>";
		String interaction6 = "</v1:wasTriggeredBy>";	
		
		String interaction = interaction0 + agent_interaction +interaction1 + interaction2 + interaction3 + interaction4 + interaction5+ interaction6;
		
		String graph = (String) string_graph_ls.get(graph_ref);////getOPMGraphInStringFromKarma(graph_ref);
		String new_graph = replace(graph, interaction0, interaction);
		
	    String agents = "<v1:agents><v1:agent id=\"" + agent + "\">" + agent_interaction_4 + "</v1:agent></v1:agents>";		
	    new_graph = replace(new_graph, "<v1:agents/>", agents);		
		
		return new_graph;
		
	}

    public static String replace(String str, String pattern, String replace) {
	    int s = 0;
	    int e = 0;
	    StringBuffer result = new StringBuffer();

	    while ((e = str.indexOf(pattern, s)) >= 0) {
	        result.append(str.substring(s, e));
	        result.append(replace);
	        s = e+pattern.length();
	    }
	    result.append(str.substring(s));
//	    System.out.println(result.toString());
	    return result.toString();
	}
	
	
	public static void createOPMGraphFile(String file_name, String OPMGraphInString){
		try {
            file_name =  file_name; 
			FileWriter fw = new FileWriter(file_name, true);
			BufferedWriter bw = new BufferedWriter(fw);

			bw.append(OPMGraphInString);
			bw.flush();
			bw.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> getIndecesOFGraphsFromIndexFile(String file_name){
		List<String> ls = new ArrayList<String>();
		try {
			FileReader fr = new FileReader(file_name);
			BufferedReader br = new BufferedReader(fr);

			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				String[] sa1 = myreadline.split("\t");				
				ls.add(sa1[1]);
			}

			br.close();
			fr.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ls;
	}
	
	
/*	
	public static List<String> getGraphsFromIndexFile(String file_name){
		List<String> ls = new ArrayList<String>();
		List<String> graph_ls = new ArrayList<String>();		
		try {
			//file_name = "C:\\maktas\\workspace\\LTSM\\test_arff\\index\\index_arff_provenance_10GB_nam.txt";
			FileReader fr = new FileReader(file_name);
			BufferedReader br = new BufferedReader(fr);

			String myreadline;
			while (br.ready()) {
				myreadline = br.readLine();
				String[] sa1 = myreadline.split("\t");				
				ls.add(sa1[1]);
			}

			br.close();
			fr.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				

			try {
			for (String id : ls) {
				String graph = "";
				String graph_response = (String) string_graph_ls.get(id);//getOPMGraphInStringFromKarma(id);
				graph_ls.add(graph_response);
				int start = graph_response.lastIndexOf("<v1:opmGraph");
				int end = graph_response.lastIndexOf("<");

				if (start == -1 || end == -1) {
					graph = graph_response;
					System.out.println("Invalid: " + id);
					continue;
				} else {
					graph = graph_response.substring(start, end);
				}
				//System.out.println(graph);	
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
		return graph_ls;
	}
*/
	
	public static List<String> getIndecesOfGraphsFromKARMADB(String sql){
		List<String> ls = new ArrayList<String>();
		Statement stmt = null;
		ResultSet rs = null;
		if (true) {
			String driver = "com.mysql.jdbc.Driver";
			try {
				Class.forName(driver);
				//conn = getKarmaDBConnection();
				//System.out.println(conn);
				stmt = conn.createStatement();
				//sql = "select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/nam-wrf-4-75%';";
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
				ls.add(rs.getString(1));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return ls;
	}
	
	
	public static List<String> getGraphsFromKARMADB(String sql){
		List<String> ls = new ArrayList<String>();
		List<String> graph_ls = new ArrayList<String>();		
		//Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		if (true) {
			String driver = "com.mysql.jdbc.Driver";
			try {
				Class.forName(driver);
				//conn = getKarmaDBConnection();
				//System.out.println(conn);
				stmt = conn.createStatement();
				//sql = "select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/nam-wrf-4-75%';";
				rs = stmt.executeQuery(sql);

				while (rs.next()) {
				ls.add(rs.getString(1));
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (rs != null)
						rs.close();
					if (stmt != null)
						stmt.close();
//					if (conn != null)
//						conn.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}			
			try {
			for (String id : ls) {
				String graph = "";
				String graph_response = (String) string_graph_ls.get(id);//getOPMGraphInStringFromKarma(id);
				graph_ls.add(graph_response);
				int start = graph_response.lastIndexOf("<v1:opmGraph");
				int end = graph_response.lastIndexOf("<");

				if (start == -1 || end == -1) {
					graph = graph_response;
					System.out.println("Invalid: " + id);
					continue;
				} else {
					graph = graph_response.substring(start, end);
				}
//				System.out.println(graph);	
			}
			} catch (Exception e) {
				e.printStackTrace();
			}
		return graph_ls;
	}	
	
	
	public static void pageRankCGL(){
		List<String> graph_ls = new ArrayList<String>();

		graph_ls = getGraphsFromKARMADB("select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/nam-wrf-4-75%';");

		try {
			int i = 0;
			for (String id : graph_ls) {
				i = i+1;
				String graph_response = (String) string_graph_ls.get(id);//getOPMGraphInStringFromKarma(id);
				String graph;
				int start = graph_response.lastIndexOf("<v1:opmGraph");
				int end = graph_response.lastIndexOf("<");
				if (start == -1 || end == -1) {
					graph = graph_response;
					System.out.println("Invalid: " + id);
					continue;
				} else {
					graph = graph_response.substring(start, end);
				}
			System.out.println(" graph = " + graph);
			
			OpmGraphDocument doc = OpmGraphDocument.Factory.parse(graph);
			
			Partitioner p = new Partitioner(doc);
			cgl.webgraph.Graph cglGraph = p.getCGLGraph();
		    System.out.println("[INFO] - The data file " + id + " has " + cglGraph.nURLs() + " nodes, and " + cglGraph.nEdges() + " edges connecting them.");			
		    //System.out.println("[INFO] - 2 - Before adding magical node - Graph Report");
		    //cglGraph.getGraphReport();		    
		    cglGraph.addMagicalNodeIntoGraph();
		    //cglGraph.getGraphReport();
		    int loop_count = 0;
		    while (cglGraph.getDangLinkVector().size() != 0) {
		    	cglGraph.removeDanglingLinks(cglGraph.getDangLinkVector());
		      loop_count++;
		    }
		    //System.out.println("[INFO] - 3 - After adding magical node - Graph Report");		    
		    //cglGraph.getGraphReport();		
		      try {
		    	  cglGraph.calculatePlainPageRankResult(0.85, 20, "data/ouput", (new Integer(i)).toString());
		          //System.out.println("[INFO] - All page rank calculations successfully completed.");
		    	  //cglGraph.calculateAllPageRankResults(0.85, 20, "data/ouput");
		        }
		        catch (Exception ex) {
		          ex.printStackTrace();
		        }		
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

   public static void createTemporalPartitionsFromKARMADB() {
  	    List<String> ls = new ArrayList<String>();
		ls = getGraphsFromKARMADB("select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/gene2life-7-8%';");
		
		try {

			FileWriter fw = new FileWriter("provenance_testing.txt", true);
			BufferedWriter bw = new BufferedWriter(fw);
			
			Date date = new Date();
			bw.append("#Date:" + date.toString());
			bw.newLine();
			String graph1="";

			int i = 0;
			for (String id : ls) {
				i = i+1; 
				System.out.println("get workflow: " + i + " " +id);

				//OpmGraphDocument doc = (OpmGraphDocument) opm_graph_ls.get(id);
				String opmgraph = (String) string_graph_ls.get(id);
				OpmGraphDocument doc = OpmGraphDocument.Factory.parse(opmgraph);				
				Partitioner p = new Partitioner(doc);
				Representor rp = new Representor();
				String attr = rp.getTopoRepresentation(p);

				bw.append(id + "," + attr);
				bw.newLine();
				bw.flush();
			}
				
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	   
   }
	
	

   
   public List<String> createCompleteOPMGraph_FromKarmaDB(ProvenanceProcessor processor) {
	   
//     processor.createTemporalPartitionsFromKARMADB();
//    processor.pageRankCGL();
//     String graph = processor.getOPMGraphInStringFromKarma("http://bitternut.cs.indiana.edu:33000/nam-wrf-4-757");
//      String graph = getFullConnectedGraphWithoutAgentInString("http://bitternut.cs.indiana.edu:33000/nam-wrf-4-757");       
//      String graph = getFullConnectedGraphWithAgentInString("http://bitternut.cs.indiana.edu:33000/nam-wrf-4-757");       

//      System.out.println(graph);
//      createOPMGraphFile("data014.xml", graph);
      //graph_ls.add(graph);

//      List<String> graph_indices_ls = processor.getIndecesOfGraphsFromKARMADB("select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/nam-wrf-4-75%';");
//      List<String> graph_indices_ls = processor.getIndecesOfGraphsFromKARMADB("select distinct entity_uri from exe_entity where entity_uri like 'http://bitternut.cs.indiana.edu:33000/nam-wrf-4%';");
      List<String> graph_indices_ls = processor.getIndecesOFGraphsFromIndexFile("C:\\maktas\\workspace\\LTSM\\fileDB\\index_files\\index_10GB_nam.txt");
      System.out.println("Step-1: All indices are loaded.");
      processor.loadGraphIntoMemory(graph_indices_ls);
      System.out.println("Step-2: All graphs are loaded.");
      
      List<String> new_graph_ls = processor.listOfcompleteOPMGraphs(graph_indices_ls, "Agent_001");
      System.out.println("Step-3: All graphs are reprocessed to be complete OPM Graphs.");
      
      return new_graph_ls;
       
   }   
   
   public List<String> createCompleteOPMGraph_FromFileDB(ProvenanceProcessor processor, String path) {
	   
       
 	   File folder = new File(path);
	   File[] listOfFiles = folder.listFiles(); 
	   List<String> graph_indices_ls = new ArrayList<String>();	   

		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
		   if (listOfFiles[i].isFile()) 
		   {
		     String file = listOfFiles[i].getName();
			 String file_name_with_path = path + "\\" + file;
	         String opmgraph = processor.getOPMGraphInStringFromOPMXMLFile(file_name_with_path);              
	         //////////////////////////////////////////
	         String index = processor.getAccountRef(opmgraph);
	         if (index == null) {
	        	 index = file_name_with_path;
	         }
			 string_graph_ls.put(index, opmgraph);
			 graph_indices_ls.add(index);
	         //////////////////////////////////////////
		   }
		  }	   
	   
       System.out.println("Step-2: All graphs are loaded.");
       
       List<String> new_graph_ls = processor.listOfcompleteOPMGraphs(graph_indices_ls, "Agent_001");
       
       System.out.println("Step-3: All graphs are reprocessed to be complete OPM Graphs.");
       
       return new_graph_ls;
   }
   
   
   public void createIndividualWebGraphsFromOPMFiles_calculatePageRank_createARFF(ProvenanceProcessor processor) {
 
          String path = "C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\nam_1000";
          //String path = "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\combined";
		  String pagerank_data_folder_path = "C:\\maktas\\workspace\\LTSM\\fileDB\\pagerank_data";
		  String workflow_name = "nam_100";
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles(); 
		  
		  
		   //int maximum_node_size = 22;
		   FileWriter fw = null;;
		   BufferedWriter bw = null;;
		try {
			fw = new FileWriter("C:\\maktas\\workspace\\LTSM\\fileDB\\pagerank_data\\nam_1000.arff", false);
			bw = new BufferedWriter(fw);	
			bw.append("@RELATION provenance");
			bw.newLine();
			bw.newLine();
			
			/*
			for (int i = 1; i <= maximum_node_size; i++) {
				bw.append("@ATTRIBUTE PR_" + i + "\t"
						+ "NUMERIC");
				bw.newLine();
			}
			*/
			bw.append("@ATTRIBUTE Node_ID" + "\t" + "STRING");	
			bw.newLine();
			bw.append("@ATTRIBUTE PageRank" + "\t" + "NUMERIC");			
			
			bw.newLine();
			bw.append("@DATA");
			bw.newLine();			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
			
			FileWriter fw_index = null;
			BufferedWriter bw_index = null;;
			try {
				fw_index = new FileWriter("C:\\maktas\\workspace\\LTSM\\fileDB\\pagerank_data\\index_nam_1000_arff.txt", false);
				bw_index = new BufferedWriter(fw_index); 
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		  
		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
		   if (listOfFiles[i].isFile()) 
		   {
 		 String file = listOfFiles[i].getName();
			 String file_name_with_path = path + "\\" + file;
	         processor.createWEBGRAPHFileFromOPMXMLFile(file_name_with_path, pagerank_data_folder_path, workflow_name, i);
		     String output_folder = pagerank_data_folder_path + "\\" + workflow_name + "\\" + i + "\\" + "output\\";
  	  	 //////////////////////////
		     File folder1 = new File(output_folder);
			 if (folder1.exists() != true) {
				folder1.mkdir();
			 }    
			String new_path = pagerank_data_folder_path + "\\" + workflow_name + "\\" + i + "\\";
			mainpr graph = new mainpr();  		   
			graph.run1(new_path, workflow_name, i, 0.85, 40, output_folder, bw, bw_index/*, maximum_node_size*/);  		   
		    
		   }
		  }
      
			try {
				bw.flush();
				bw.close();
				fw.close();
				bw_index.flush();
				bw_index.close();
				fw_index.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}         
   
   }
   
   
   public static void main(String args[]) {
	    
        ProvenanceProcessor processor = new ProvenanceProcessor();
        ProvenanceGraphProcessor g_processor = new ProvenanceGraphProcessor();
        //String path = "C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\nam_1000";
        //String path = "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\combined";
		String pagerank_data_folder_path = "G:\\MyData\\LTSM\\fileDB\\pagerank_data";
		String workflow_name = "nam_100_test";
		int i = 0;
		  
        processor.createWEBGRAPHFileFromOPMXMLFile("", pagerank_data_folder_path, workflow_name, i);

/*
        String path = ".\\fileDB\\org_data_files\\combined12";
        List<String> new_graph_ls = processor.createCompleteOPMGraph_FromFileDB(processor, path);
        String graph_full = g_processor.OPMWebGraphWithAgentAtTheCenter(new_graph_ls, "Agent_001");
//         String graph_full = g_processor.OPMWebGraphWithAgentAtTheStart(new_graph_ls, "Agent_001");
        System.out.println("Step-4: All graphs are combined into one BIG OPM Graph.");
//         processor.createOPMGraphFile("C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\OPM_10GB_nam_agent_at_start.xml", graph_full);
        String combined_graph = ".\\fileDB\\OPM_files\\combined\\combined_12_agent_at_center.xml";
        processor.createOPMGraphFile(combined_graph, graph_full);
        System.out.println("Step-5: OPM Graph is written into a file. All graphs are combined into one BIG OPM Graph. " + combined_graph);
        
      
        processor.createIndividualWebGraphsFromOPMFiles_calculatePageRank_createARFF(processor);
  */       
        
        
//        processor.writeOPMGraphFromDBTOFile("10GB_animation.txt");
//        processor.writeOPMGraphFromDBTOFile("10GB_gene2life.txt");
//        processor.writeOPMGraphFromDBTOFile("10GB_motif.txt");
//        processor.writeOPMGraphFromDBTOFile("10GB_nam.txt");
//        processor.writeOPMGraphFromDBTOFile("10GB_ncfs.txt");
//        processor.writeOPMGraphFromDBTOFile("10GB_scoop.txt");

        
	}
}
