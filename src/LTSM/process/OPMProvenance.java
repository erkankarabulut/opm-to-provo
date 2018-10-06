package LTSM.process;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.xmlbeans.XmlException;
import org.openprovenance.model.v1_1_a.AccountRef;
import org.openprovenance.model.v1_1_a.OpmGraphDocument;
import org.openprovenance.model.v1_1_a.Process;

import Karma.query.KarmaAxis2Query;

public class OPMProvenance {

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
	
	public static String getFullConnectedGraphWithoutAgentInString(String opmgraph, String agent) {
		OpmGraphDocument doc = null;
		try {
			doc = OpmGraphDocument.Factory.parse(opmgraph);
		} catch (XmlException e) {
			e.printStackTrace();
		}		
		
		String graph = null, new_graph = null;
		Process[] processes = null;
		String [] effect_cause = new String[2];
		String accountref = null;

        if (doc != null && doc.getOpmGraph() != null) {	
		if (doc.getOpmGraph().getProcesses() != null) {
			processes = doc.getOpmGraph().getProcesses().getProcessArray();
			if (processes[0] != null && processes[0].getAccountArray() != null) {
		        AccountRef[] acc_array = processes[0].getAccountArray();
                accountref = acc_array[0].getRef();
			}
			if (processes[0] != null && processes[1] != null) {
			    effect_cause[0] = processes[0].getId(); 
                effect_cause[1] = processes[1].getId(); 		
			}
		}	

		////////step-1: We connect agent node to the workflow node
		String interaction0 = "<v1:causalDependencies>";
		
		////////step-2: We connect workflow node to the first process
		String interaction1 = "<v1:wasTriggeredBy>";
		String interaction2 = "<v1:effect ref=" + "\"" + effect_cause[1] + "\"" + "/>";
		String interaction3 = "<v1:cause ref=" + "\"" + effect_cause[0] + "\"" + "/>";
		String interaction4 = null;
		if (accountref != null) {
		    interaction4 = "<v1:account ref=" + "\"" + accountref + "\"" + "/>";
		}
		
		String interaction5 = "<v1:time noEarlierThan=\"2009-02-19T18:32:00.000-05:00\" noLaterThan=\"2009-02-19T18:32:00.000-05:00\"/>";
		String interaction6 = "</v1:wasTriggeredBy>";	
		
		String interaction = null;
		if (accountref != null) {	
		    interaction = interaction0 + interaction1 + interaction2 + interaction3 + interaction4 + interaction5+ interaction6;
		} else {
			interaction = interaction0 + interaction1 + interaction2 + interaction3 + interaction5+ interaction6;			
		}
		
		graph = opmgraph;//(String) string_graph_ls.get(graph_ref);////getOPMGraphInStringFromKarma(graph_ref);
		new_graph = replace(graph, interaction0, interaction);
		
//	    String agents = null;
//		if (accountref != null) {
//	        agents = "<v1:agents><v1:agent id=\"" + agent + "\">" + agent_interaction_4 + "</v1:agent></v1:agents>";		
//		} else {
//	        agents = "<v1:agents><v1:agent id=\"" + agent + "\">" + "</v1:agent></v1:agents>";					
//		}
//	    new_graph = replace(new_graph, "<v1:agents/>", agents);		
//        } else {
//        	new_graph = graph;
       }
		
		return new_graph;
		
	}
	
	
	public static String getFullConnectedGraphWithAgentInString(String opmgraph, String agent) {
		OpmGraphDocument doc = null;
		try {
			doc = OpmGraphDocument.Factory.parse(opmgraph);
		} catch (XmlException e) {
			e.printStackTrace();
		}		
		
		String graph = null, new_graph = null;
		Process[] processes = null;
		String [] effect_cause = new String[2];
		String accountref = null;

        if (doc != null && doc.getOpmGraph() != null) {	
		if (doc.getOpmGraph().getProcesses() != null) {
			processes = doc.getOpmGraph().getProcesses().getProcessArray();
			if (processes[0] != null && processes[0].getAccountArray() != null) {
		        AccountRef[] acc_array = processes[0].getAccountArray();
		        if (acc_array != null && acc_array.length != 0 ) {
		        	if (acc_array[0] != null) {
                       accountref = acc_array[0].getRef();
		        	}
		        }
			}
			if (processes[0] != null && processes[1] != null) {
			    effect_cause[0] = processes[0].getId(); 
                effect_cause[1] = processes[1].getId(); 		
			}
		}	

		////////step-1: We connect agent node to the workflow node
		String interaction0 = "<v1:causalDependencies>";
		
		String agent_interaction_1 = "<v1:wasTriggeredBy>";
		String agent_interaction_2 = "<v1:effect ref=" + "\"" + effect_cause[0] + "\"" + "/>";
		String agent_interaction_3 = "<v1:cause ref=" + "\"" + agent + "\"" + "/>";
		
		String agent_interaction_4 = null;
		if (accountref != null) {
   		     agent_interaction_4 = "<v1:account ref=" + "\"" + accountref + "\"" + "/>";
		}
		String agent_interaction_5 = "</v1:wasTriggeredBy>";	
		String agent_interaction = agent_interaction_1 + agent_interaction_2 + agent_interaction_3 + agent_interaction_4 + agent_interaction_5;

		////////step-2: We connect workflow node to the first process
		String interaction1 = "<v1:wasTriggeredBy>";
		String interaction2 = "<v1:effect ref=" + "\"" + effect_cause[1] + "\"" + "/>";
		String interaction3 = "<v1:cause ref=" + "\"" + effect_cause[0] + "\"" + "/>";
		String interaction4 = null;
		if (accountref != null) {
		    interaction4 = "<v1:account ref=" + "\"" + accountref + "\"" + "/>";
		}
		
		String interaction5 = "<v1:time noEarlierThan=\"2009-02-19T18:32:00.000-05:00\" noLaterThan=\"2009-02-19T18:32:00.000-05:00\"/>";
		String interaction6 = "</v1:wasTriggeredBy>";	
		
		String interaction = null;
		if (accountref != null) {	
		    interaction = interaction0 + agent_interaction +interaction1 + interaction2 + interaction3 + interaction4 + interaction5+ interaction6;
		} else {
			interaction = interaction0 + agent_interaction +interaction1 + interaction2 + interaction3 + interaction5+ interaction6;			
		}
		
		graph = opmgraph;//(String) string_graph_ls.get(graph_ref);////getOPMGraphInStringFromKarma(graph_ref);
		new_graph = replace(graph, interaction0, interaction);
		
	    String agents = null;
		if (accountref != null) {
	        agents = "<v1:agents><v1:agent id=\"" + agent + "\">" + agent_interaction_4 + "</v1:agent></v1:agents>";		
		} else {
	        agents = "<v1:agents><v1:agent id=\"" + agent + "\">" + "</v1:agent></v1:agents>";					
		}
	    new_graph = replace(new_graph, "<v1:agents/>", agents);		
        } else {
        	new_graph = graph;
        }
		
		return new_graph;
		
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

	public static void retrieveAndWriteGraphsIntoFiles(String index_file_name, String workflow_type){
		List<String> ls = new ArrayList<String>();	
		try {
			FileReader fr = new FileReader(index_file_name);
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
			e.printStackTrace();
		}				

			try {
			int i = 0;	
			for (String id : ls) {
				String f_name = "C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\" + workflow_type + "\\" + i + ".txt";
				
				String graph = getOPMGraphFromKarma(id);
				System.out.println(graph);
		
				try {

					FileWriter fw = new FileWriter(f_name, false);
					BufferedWriter bw = new BufferedWriter(fw);
					
                    bw.write(graph);
					//bw.newLine();
			        bw.close();
			        fw.close();					

				} catch (Exception e) {
					e.printStackTrace();
				}
		        i++;
		        


			}
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	public static void readAndConvertGraphsToCompleteFiles(String path, String agent, String workflow_type){
		  String graph = "";
		  String new_graph = "";
		  String file;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles(); 
		 
		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
		 
		   if (listOfFiles[i].isFile()) 
		   {
		   file = listOfFiles[i].getName();
			try {
				String f_name = path + "\\" + file;

				try {

					FileReader freader = new FileReader(f_name);
					BufferedReader breader = new BufferedReader(freader);
					
					String myreadline;
					while (breader.ready()) {
						myreadline = breader.readLine();
						String[] sa1 = myreadline.split("\t");				
						graph = sa1[0];
					}

					breader.close();
					breader.close();				

				} catch (Exception e) {
					e.printStackTrace();
				}
        
				new_graph = getFullConnectedGraphWithAgentInString(graph, agent);

				try {


						String f__name = "C:\\maktas\\workspace\\LTSM\\fileDB\\data_files\\" + workflow_type + "\\" + i + ".txt";
						
						//String graph = getOPMGraphFromKarma(id);
						//System.out.println(graph);
				
						try {

							FileWriter fw = new FileWriter(f__name, false);
							BufferedWriter bw = new BufferedWriter(fw);
							
		                    bw.write(new_graph);
							//bw.newLine();
					        bw.close();
					        fw.close();					

						} catch (Exception e) {
							e.printStackTrace();
						}
					
					} catch (Exception e) {
						e.printStackTrace();
					}				

			} catch (Exception e) {
				e.printStackTrace();
			}
		      }
		  }				
	}	
	
	public static String OPMWebGraphWithAgentAtTheCenter(String path, String agent) {

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
		
			  String graph = "";
			  String new_graph = "";
			  String file;
			  File folder = new File(path);
			  File[] listOfFiles = folder.listFiles(); 
			 
			  for (int i = 0; i < listOfFiles.length; i++) 
			  {
			 
			   if (listOfFiles[i].isFile()) 
			   {
			   file = listOfFiles[i].getName();

					String f_name = path + "\\" + file;

					try {

						FileReader freader = new FileReader(f_name);
						BufferedReader breader = new BufferedReader(freader);
						
						String myreadline;
						while (breader.ready()) {
							myreadline = breader.readLine();
							String[] sa1 = myreadline.split("\t");				
							graph = sa1[0];
						}

						breader.close();
						breader.close();				

					} catch (Exception e) {
						e.printStackTrace();
					}
					
	                //fully connected graph is directly read from the folder 
					//new_graph = getFullConnectedGraphWithAgentInString(graph, agent);

					processes = processes + getProcesses(graph);
					artifacts = artifacts + getArtifacts(graph);
					edges = edges + getCausalDependencies(graph);
					//agents = "<v1:agents><v1:agent id=\"" + agent + "\"><v1:account ref=\"" + getAccountRef(graph) + "\"/></v1:agent></v1:agents>"; 
					agents = "<v1:agents><v1:agent id=\"" + agent + "\">" + "</v1:agent></v1:agents>"; 

			   
			  }				
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
		   
		    
		return graph_full;
		
	}
	
	public static void writeOPMGraphTOFile(String graph_full, String opm_file_name) {
		try {
			FileWriter fw_v = new FileWriter(opm_file_name, false);
			BufferedWriter bw_v = new BufferedWriter(fw_v);
  		    bw_v.write(graph_full);   			
		    bw_v.flush();
		    bw_v.close();
		    fw_v.close();
	} catch (Exception e) {
		e.printStackTrace();
	}		
				
	}	
	

	public static String OPMWebGraphWithAgentAtTheStart(String path, String agent) {

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

		
        HashMap hash_processes = new HashMap();
        HashMap hash_graphs = new HashMap();
        
        String AccountRef = "";
		//////////////////////////////////
        String graph = "";
		  String new_graph = "";
		  String file;
		  File folder = new File(path);
		  File[] listOfFiles = folder.listFiles(); 
		 
		  for (int i = 0; i < listOfFiles.length; i++) 
		  {
		 
		   if (listOfFiles[i].isFile()) 
		   {
		   file = listOfFiles[i].getName();

				String f_name = path + "\\" + file;
                /////////////////////////////////////////////////////// 
				try {

					FileReader freader = new FileReader(f_name);
					BufferedReader breader = new BufferedReader(freader);
					
					String myreadline;
					while (breader.ready()) {
						myreadline = breader.readLine();
						String[] sa1 = myreadline.split("\t");				
						graph = sa1[0];
						new_graph = getFullConnectedGraphWithoutAgentInString(graph, agent);
						hash_graphs.put(new_graph, null);
					}
		            AccountRef = getAccountRef(graph);
					breader.close();
					breader.close();				

				} catch (Exception e) {
					e.printStackTrace();
				}
                /////////////////////////////////////////////////////// 
      
	 			OpmGraphDocument doc = null;
				try {
					doc = OpmGraphDocument.Factory.parse(graph);
				} catch (XmlException e) {
					e.printStackTrace();
				}		
				if (doc != null) {
				if (doc.getOpmGraph() != null && doc.getOpmGraph().getProcesses() != null) {	
				if (doc.getOpmGraph().getProcesses().getProcessArray() != null) {
					Process [] process_array = doc.getOpmGraph().getProcesses().getProcessArray();
					if (process_array != null) {
		            if (process_array.length != 0) {
		            	hash_processes.put(process_array[0].getId(), null);
		            }
					}

				}		
				}
				}
		   }
		  }
		  
		
		  
 		//////////////////////////////////
 		 Set set = hash_processes.keySet();
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
        }


  		//////////////////////////////////
 		 Set g_set = hash_graphs.keySet();
         Iterator g_it = g_set.iterator();   
         while (g_it.hasNext()) {
            graph = (String) g_it.next();         
 		    processes = /*processes +*/ getProcesses(graph);
	        artifacts = /*artifacts +*/ getArtifacts(graph);
		    edges = edges + getCausalDependencies(graph);
			agents = "<v1:agents><v1:agent id=\""+ agent + "\"><v1:account ref=\"" + AccountRef + "\"/></v1:agent></v1:agents>"; 
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
    
	public static void main(String args[]) {
	    
		OPMProvenance opm_creator = new OPMProvenance();

		
		/*
        opm_creator.retrieveAndWriteGraphsIntoFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\index_files\\index_10GB_ncfs.txt", "ncfs");
        opm_creator.retrieveAndWriteGraphsIntoFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\index_files\\index_10GB_gene2life.txt", "gene2life");
        opm_creator.retrieveAndWriteGraphsIntoFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\index_files\\index_10GB_animation.txt", "animation");
        opm_creator.retrieveAndWriteGraphsIntoFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\index_files\\index_10GB_motif.txt", "motif");
          */

        opm_creator.readAndConvertGraphsToCompleteFiles(".\\fileDB\\org_data_files\\nam_1000", "Agent_001", "nam_1000");
        //opm_creator.readAndConvertGraphsToCompleteFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\scoop_1000", "Agent_001", "scoop_1000");
		//opm_creator.readAndConvertGraphsToCompleteFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\ncfs_1000", "Agent_001", "ncfs_1000");
        //opm_creator.readAndConvertGraphsToCompleteFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\gene2life_1000", "Agent_001", "gene2life_1000");

		//opm_creator.readAndConvertGraphsToCompleteFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\motif\\", "Agent_001", "motif");
       
		String opmgraph = null;

  	    opmgraph = opm_creator.OPMWebGraphWithAgentAtTheCenter(".\\fileDB\\data_files\\nam_1000_test", "Agent_001");
  	
        opm_creator.writeOPMGraphTOFile(opmgraph, ".\\fileDB\\OPM_files\\OPM_10GB_nam_1000_test_atCenter.xml");
        /*
	    opmgraph = opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\data_files\\scoop_1000", "Agent_001");
        opm_creator.writeOPMGraphTOFile(opmgraph, "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\OPM_10GB_scoop_1000_atCenter.xml");

	    opmgraph = opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\data_files\\ncfs_1000", "Agent_001");
        opm_creator.writeOPMGraphTOFile(opmgraph, "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\OPM_10GB_ncfs_1000_atCenter.xml");
        
	    opmgraph = opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\data_files\\gene2life_1000", "Agent_001");
        opm_creator.writeOPMGraphTOFile(opmgraph, "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\OPM_10GB_gene2life_1000_atCenter.xml");
*/
		//opm_creator.readAndConvertGraphsToCompleteFiles("C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\animation_1000", "Agent_001", "animation");
		//opmgraph = opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\data_files\\animation_1000", "Agent_001");
		//opm_creator.writeOPMGraphTOFile(opmgraph, "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\OPM_10GB_animation_1000_atCenter.xml");
		
		
//		String opmgraph = opm_creator.OPMWebGraphWithAgentAtTheStart("C:\\maktas\\workspace\\LTSM\\fileDB\\org_data_files\\nam_100", "Agent_001");
//        opm_creator.writeOPMGraphTOFile(opmgraph, "C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\OPM_10GB_nam_100_atCenter.xml");

        
//        opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\ncfs\\OPM_10GB_ncfs.xml", "Agent_001", "ncfs");
//        opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\animation\\OPM_10GB_animation.xml", "Agent_001", "animation");
//        opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\gene2life\\OPM_10GB_gene2life.xml", "Agent_001", "gene2life");
//        opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\scoop\\OPM_10GB_scoop.xml", "Agent_001", "scoop");
//        opm_creator.OPMWebGraphWithAgentAtTheCenter("C:\\maktas\\workspace\\LTSM\\fileDB\\OPM_files\\motif\\OPM_10GB_motif.xml", "Agent_001", "motif");
        

	}
	
	
	
	
	
}
