package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.openprovenance.model.v1_1_a.OpmGraphDocument;
import org.openprovenance.model.v1_1_a.Process;

public class ProvenanceGraphProcessor {

	
	
	public static String OPMWebGraphWithAgentAtTheCenter(List<String> ls, String agent) {

		String first_string = "<v1:opmGraph xmlns:v1=\"http://openprovenance.org/model/v1.1.a\">";
		String last_string = "</v1:opmGraph>";
		
		String artifact_first_string = "<v1:artifacts>";
		String artifact_last_string = "</v1:artifacts>";
		
		String process_first_string = "<v1:processes>";
		String process_last_string = "</v1:processes>";

		String cdependecy_first_string = "<v1:causalDependencies>";
		String cdependecy_last_string = "</v1:causalDependencies>";	
		
		
		String agents = "";
		String processes = "";
		String artifacts = "";
		String edges = "";
		
		for (String p : ls) {
			processes = processes + getProcesses(p);
			artifacts = artifacts + getArtifacts(p);
			edges = edges + getCausalDependencies(p);
			agents = "<v1:agents><v1:agent id=\"" + agent + "\"><v1:account ref=\"" + "http://bitternut.cs.indiana.edu:33000/agent-1"/*getAccountRef(p)*/ + "\"/></v1:agent></v1:agents>"; 
		}
		
		String graph_full = first_string + 
			           process_first_string + 
			           processes + 
			           process_last_string +				
				       artifact_first_string + 
				       artifacts + 
				       artifact_last_string + 
                       agents + 
                       cdependecy_first_string +
                       edges +
                       cdependecy_last_string +
				       last_string;
		   
		    
//		System.out.println(graph_full);
		return graph_full;
		
	}
	

	public static String OPMWebGraphWithAgentAtTheStart(List<String> ls, String agent) {

		String first_string = "<v1:opmGraph xmlns:v1=\"http://openprovenance.org/model/v1.1.a\">";
		String last_string = "</v1:opmGraph>";
		
		String artifact_first_string = "<v1:artifacts>";
		String artifact_last_string = "</v1:artifacts>";
		
		String process_first_string = "<v1:processes>";
		String process_last_string = "</v1:processes>";

		String cdependecy_first_string = "<v1:causalDependencies>";
		String cdependecy_last_string = "</v1:causalDependencies>";	
		
		
		String agents = "";
		String processes = "";
		String artifacts = "";
		String edges = "";

		
        HashMap hash = new HashMap();
        String AccountRef = "";
 		for (String graph_string : ls) {
			OpmGraphDocument doc = null;
			try {
				doc = OpmGraphDocument.Factory.parse(graph_string);
			} catch (XmlException e) {
				e.printStackTrace();
			}		
			if (doc != null) {
			if (doc.getOpmGraph() != null) {	
			if (doc.getOpmGraph().getProcesses() != null) {
				Process [] process_array = doc.getOpmGraph().getProcesses().getProcessArray();
				if (process_array != null) {
	            if (process_array.length != 0) {
					hash.put(process_array[0].getId(), null);
	            }
				}
	            AccountRef = getAccountRef(graph_string);

			}		
			}
			}
		}

 		 ///////////////////////////////////////////////////////
 		 Set set = hash.keySet();
         Iterator it = set.iterator();   
         String cause = agent;
         while (it.hasNext()) {
            String effect = (String) it.next();

		String interaction1 = "<v1:wasTriggeredBy>";
		String interaction2 = "<v1:effect ref=" + "\"" + effect + "\"" + "/>";
		String interaction3 = "<v1:cause ref=" + "\"" + cause + "\"" + "/>";
		String interaction4 = "<v1:account ref=" + "\"" + AccountRef + "\"" + "/>";
		String interaction5 = "<v1:time noEarlierThan=\"2009-02-19T18:32:00.000-05:00\" noLaterThan=\"2009-02-19T18:32:00.000-05:00\"/>";
		String interaction6 = "</v1:wasTriggeredBy>";	

		     cause = effect;  
		edges = edges + interaction1 + interaction2 + interaction3 + interaction4 + interaction5+ interaction6;	
//		System.out.println(edges);
        }
         
         //////////////////////////////////////////////////////////////
         
		for (String p : ls) {
			
			processes = processes + getProcesses(p);
			artifacts = artifacts + getArtifacts(p);
			edges = edges + getCausalDependencies(p);
			agents = "<v1:agents><v1:agent id=\""+ agent + "\"><v1:account ref=\"" + "http://bitternut.cs.indiana.edu:33000/agent-1"/*getAccountRef(p)*/ +  "\"/></v1:agent></v1:agents>"; 
		}
		
		String graph_full = first_string + 
			           process_first_string + 
			           processes + 
			           process_last_string +				
				       artifact_first_string + 
				       artifacts + 
				       artifact_last_string + 
                       agents + 
                       cdependecy_first_string +
                       edges +
                       cdependecy_last_string +
				       last_string;
		   
		    
//		System.out.println(graph_full);
		return graph_full;
		
	}	
	
	
	public static String OPMWebGraphWithoutAgent(List<String> ls) {

		String first_string = "<v1:opmGraph xmlns:v1=\"http://openprovenance.org/model/v1.1.a\">";
		String last_string = "</v1:opmGraph>";
		
		String artifact_first_string = "<v1:artifacts>";
		String artifact_last_string = "</v1:artifacts>";
		
		String process_first_string = "<v1:processes>";
		String process_last_string = "</v1:processes>";

		String cdependecy_first_string = "<v1:causalDependencies>";
		String cdependecy_last_string = "</v1:causalDependencies>";	
		
		String agents = "<v1:agents/>";
		
		String processes = "";
		String artifacts = "";
		String edges = "";
		
		for (String p : ls) {
			processes = processes + getProcesses(p);
			artifacts = artifacts + getArtifacts(p);
			edges = edges + getCausalDependencies(p);
		}
		
		String graph_full = first_string + 
			           process_first_string + 
			           processes + 
			           process_last_string +				
				       artifact_first_string + 
				       artifacts + 
				       artifact_last_string + 
                       agents + 
                       cdependecy_first_string +
                       edges +
                       cdependecy_last_string +
				       last_string;
		   
		    
		System.out.println(graph_full);
		return graph_full;
		
	}

	public static String getArtifacts(String graph) {
	    int start1 = graph.lastIndexOf("<v1:artifacts>");
	    int end1 = graph.lastIndexOf("</v1:artifacts>");
	    String graph1 = "";
	    if (start1 == -1 || end1 == -1) {
		    graph1 = graph;
		    //System.out.println(graph1);
	    } else 
		    graph1 = graph.substring(start1, end1);
		
	    String new_graph = replace(graph1, "<v1:artifacts>", "");
	    
	    
//	    int start2 = graph1.lastIndexOf("<v1:artifact");
//	    int end2 = graph1.lastIndexOf(">");
//	    String graph2 = "";
//	    if (start2 == -1 || end2 == -1) {
//		    graph2 = graph1;
//		    //System.out.println(graph2);
//	    } else 
//		    graph2 = graph1.substring(start2, end2);	    

	    //System.out.println(new_graph);		
		return new_graph;
	}

	public static String getAccountRef(String graph) {
	    int start1 = graph.lastIndexOf("<v1:accounts>");
	    int end1 = graph.lastIndexOf("</v1:accounts>");
	    String graph1 = "";
	    if (start1 == -1 || end1 == -1) {
		    graph1 = graph;
	    } else 
		    graph1 = graph.substring(start1, end1);

	    String account_info = replace(graph1, "<v1:accounts><v1:account id=\"", "");	    
	    String account_ref = replace(account_info, "\"/>", "");	
	    
//	    System.out.println(account_ref);		
		return account_ref;
	}	
	
	public static String getProcesses(String graph) {
	    int start1 = graph.lastIndexOf("<v1:processes>");
	    int end1 = graph.lastIndexOf("</v1:processes>");
	    String graph1 = "";
	    if (start1 == -1 || end1 == -1) {
		    graph1 = graph;
		    //System.out.println(graph1);
	    } else 
		    graph1 = graph.substring(start1, end1);
		
	    String new_graph = replace(graph1, "<v1:processes>", "");

	    
//	    int start2 = graph1.lastIndexOf("<v1:process");
//	    int end2 = graph1.lastIndexOf(">");
//	    String graph2 = "";
//	    if (start2 == -1 || end2 == -1) {
//		    graph2 = graph1;
//		    //System.out.println(graph2);
//	    } else 
//		    graph2 = graph1.substring(start2, end2);	    

	    //System.out.println(new_graph);		
		return new_graph;
	}	

	public static String getCausalDependencies(String graph) {
	    int start1 = graph.lastIndexOf("<v1:causalDependencies>");
	    int end1 = graph.lastIndexOf("</v1:causalDependencies>");
	    String graph1 = "";
	    if (start1 == -1 || end1 == -1) {
		    graph1 = graph;
		    //System.out.println(graph1);
	    } else 
		    graph1 = graph.substring(start1, end1);

	    String new_graph = replace(graph1, "<v1:causalDependencies>", "");
	    
//	    int start2 = graph1.lastIndexOf("<v1:wasTriggeredBy>");
//	    int end2 = graph1.lastIndexOf(">");
//	    String graph2 = "";
//	    if (start2 == -1 || end2 == -1) {
//		    graph2 = graph1;
//		    //System.out.println(graph2);
//	    } else 
//		    graph2 = graph1.substring(start2, end2);	    

	    //System.out.println(new_graph);		
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
	
}
