package cytoscape.OPM_visualization.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.xmlbeans.XmlException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.openprovenance.model.v1_1_a.AccountRef;
import org.openprovenance.model.v1_1_a.Agent;
import org.openprovenance.model.v1_1_a.AgentRef;
import org.openprovenance.model.v1_1_a.Artifact;
import org.openprovenance.model.v1_1_a.ArtifactRef;
import org.openprovenance.model.v1_1_a.EmbeddedAnnotation;
import org.openprovenance.model.v1_1_a.OpmGraphDocument;
import org.openprovenance.model.v1_1_a.Process;
import org.openprovenance.model.v1_1_a.ProcessRef;
import org.openprovenance.model.v1_1_a.Property;
import org.openprovenance.model.v1_1_a.Used;
import org.openprovenance.model.v1_1_a.WasControlledBy;
import org.openprovenance.model.v1_1_a.WasDerivedFrom;
import org.openprovenance.model.v1_1_a.WasGeneratedBy;
import org.openprovenance.model.v1_1_a.WasTriggeredBy;

import cytoscape.CyEdge;
import cytoscape.CyNetwork;
import cytoscape.CyNode;
import cytoscape.Cytoscape;
import cytoscape.data.CyAttributes;
import cytoscape.data.Semantics;
import edu.iu.pti.opmPlugin.PluginDocument;

public class GraphBuilder {
	static String pathToConfigFile = "/plugins/config/pluginConfig.xml";

	public static String[] process_keyword = null;
	public static String[] process_name = null;

	public static String[] artifact_keyword = null;
	public static String[] artifact_name = null;

	public static String art_label[] = null;
	public static String pro_label[] = null;

	public void build(OpmGraphDocument doc, CyNetwork cyNetwork,
			CyAttributes cyNodeAttrs, CyAttributes cyEdgeAttrs)
			throws IOException, XmlException, ParserConfigurationException,
			JDOMException {
		Process[] processes = new Process[0];
		if (doc.getOpmGraph().getProcesses() != null) {
			processes = doc.getOpmGraph().getProcesses().getProcessArray();
		}

		Artifact[] artifacts = new Artifact[0];
		if (doc.getOpmGraph().getArtifacts() != null) {
			artifacts = doc.getOpmGraph().getArtifacts().getArtifactArray();
		}

		Agent[] agents = new Agent[0];
		if (doc.getOpmGraph().getAgents() != null) {
			agents = doc.getOpmGraph().getAgents().getAgentArray();
		}
		CyNode[] node_processes = new CyNode[processes.length];
		CyNode[] node_artifacts = new CyNode[artifacts.length];
		CyNode[] node_agents = new CyNode[agents.length];

		for (int i = 0; i < processes.length; i++) {
			if (processes[i].getId() == null)
				processes[i].setId("new_process " + i);
			node_processes[i] = Cytoscape.getCyNode(processes[i].getId(), true);

//			if (OPM_visualization.useAutomaticLoading)
//				cyNodeAttrs.setAttribute(node_processes[i].getIdentifier(),
//						"detailLevel", "coarse");
		}

		for (int i = 0; i < artifacts.length; i++) {
			if (artifacts[i].getId() == null)
				artifacts[i].setId("new_artifact " + i);
			node_artifacts[i] = Cytoscape.getCyNode(artifacts[i].getId(), true);

//			if (OPM_visualization.useAutomaticLoading)
//				cyNodeAttrs.setAttribute(node_artifacts[i].getIdentifier(),
//						"detailLevel", "coarse");
		}

		for (int i = 0; i < agents.length; i++) {
			if (agents[i].getId() == null)
				agents[i].setId("new_agent " + i);
			node_agents[i] = Cytoscape.getCyNode(agents[i].getId(), true);

//			if (OPM_visualization.useAutomaticLoading)
//				cyNodeAttrs.setAttribute(node_agents[i].getIdentifier(),
//						"detailLevel", "coarse");
		}

		for (int i = 0; i < node_processes.length; i++) {
			cyNetwork.addNode(node_processes[i]);
		}

		for (int i = 0; i < node_artifacts.length; i++) {
			cyNetwork.addNode(node_artifacts[i]);
		}

		for (int i = 0; i < node_agents.length; i++) {
			cyNetwork.addNode(node_agents[i]);
		}

		// CyAttributes cyNodeAttrs = Cytoscape.getNodeAttributes();
		// CyAttributes cyEdgeAttrs = Cytoscape.getEdgeAttributes();
		String attributeName;
		String AttributeValue;

		{
			/*
			 * Load annotation configuration
			 */
			StringBuffer bf2 = new StringBuffer();
			String proFilePath = System.getProperty("user.dir")
					+ pathToConfigFile;
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(proFilePath));

			InputStreamReader isr = new InputStreamReader(in);
			BufferedReader reader = new BufferedReader(isr);

			String line2;
			while ((line2 = reader.readLine()) != null) {
				// System.out.println(line2);
				bf2.append(line2);
			}

			reader.close();

			PluginDocument doc_p = PluginDocument.Factory.parse(bf2.toString());

			edu.iu.pti.opmPlugin.PluginDocument.Plugin.ImportAttributes.Process.Attribute[] process_import_attr = doc_p
					.getPlugin().getImportAttributes().getProcess()
					.getAttributeArray();
			edu.iu.pti.opmPlugin.PluginDocument.Plugin.ImportAttributes.Artifact.Attribute[] artifact_import_attr = doc_p
					.getPlugin().getImportAttributes().getArtifact()
					.getAttributeArray();

			// the process label based on the priority
			DocumentBuilderFactory dbf1 = DocumentBuilderFactory.newInstance();
			DocumentBuilder db1 = dbf1.newDocumentBuilder();
			SAXBuilder builder1 = new SAXBuilder();
			Reader input1 = new StringReader(doc_p.getPlugin()
					.getVisualAttributes().getNodeLabel().getProcessLabel()
					.xmlText());
			Document tmp_doc1 = null;
			tmp_doc1 = builder1.build(input1);
			Element root1 = tmp_doc1.getRootElement();
			java.util.List<Element> ls1 = root1.getChildren();

			pro_label = new String[ls1.size()];

			for (int i = 0; i < ls1.size(); i++) {
				pro_label[i] = ls1.get(i).getValue();
				System.out.println("pro_label[" + i + "] : " + pro_label[i]);
			}

			DocumentBuilderFactory dbf2 = DocumentBuilderFactory.newInstance();
			DocumentBuilder db2 = dbf2.newDocumentBuilder();
			SAXBuilder builder2 = new SAXBuilder();
			Reader input2 = new StringReader(doc_p.getPlugin()
					.getVisualAttributes().getNodeLabel().getArtifactLabel()
					.xmlText());
			Document tmp_doc2 = null;
			tmp_doc2 = builder2.build(input2);
			Element root2 = tmp_doc2.getRootElement();
			java.util.List<Element> ls2 = root2.getChildren();

			art_label = new String[ls2.size()];

			for (int i = 0; i < ls2.size(); i++) {
				art_label[i] = ls2.get(i).getValue();
				System.out.println("art_label[" + i + "] : " + art_label[i]);
			}

			process_keyword = new String[process_import_attr.length];
			process_name = new String[process_import_attr.length];

			artifact_keyword = new String[artifact_import_attr.length];
			artifact_name = new String[artifact_import_attr.length];

			for (int i = 0; i < process_import_attr.length; i++) {
				process_keyword[i] = process_import_attr[i].getAttributeName()
						.xmlText().split("<|>")[2];
				process_name[i] = process_import_attr[i].getImportName()
						.xmlText().split("<|>")[2];
			}

			for (int i = 0; i < artifact_import_attr.length; i++) {
				artifact_keyword[i] = artifact_import_attr[i]
						.getAttributeName().xmlText().split("<|>")[2];
				artifact_name[i] = artifact_import_attr[i].getImportName()
						.xmlText().split("<|>")[2];
			}
		}
		/*
		 * Add the attribute of Processes
		 */
		for (int i = 0; i < processes.length; i++) {
			Process process = processes[i];
			// println("Process#" + i);
			// println("Process ID:" + process.getId());

			attributeName = "NodeType";
			AttributeValue = "PROCESS";
			cyNodeAttrs.setAttribute(node_processes[i].getIdentifier(),
					attributeName, AttributeValue);

			AccountRef[] accounts = process.getAccountArray();

			for (int j = 0; j < accounts.length; j++) {
				// println("AccountRef :" + accounts[j].getRef());
				attributeName = "Account";
				AttributeValue = accounts[j].getRef();
				cyNodeAttrs.setAttribute(node_processes[i].getIdentifier(),
						attributeName, AttributeValue);
			}

			EmbeddedAnnotation[] annotation = process.getAnnotationArray();

			// during first pass, set the existing label attribute with
			// the highest priority
			int label_index = pro_label.length - 1;

			for (int j = 0; j < annotation.length; j++) {
				// println("Annotation ID:" + annotation[j].getId());

				Property[] properties = annotation[j].getPropertyArray();
				for (int k = 0; k < properties.length; k++) {
					for (int pt = 0; pt < process_keyword.length; pt++) {
						if (properties[k].getUri()
								.contains(process_keyword[pt])) {
							attributeName = process_name[pt];
							String[] tmp = properties[k].getValue().toString()
									.split("<|>");

							// check if this value is empty
							if (tmp.length < 3)
								continue;

							AttributeValue = tmp[2];
							// AttributeValue = properties[k].getValue()
							// .toString().split("<|>")[2];
							cyNodeAttrs.setAttribute(node_processes[i]
									.getIdentifier(), attributeName,
									AttributeValue);

						}
					}

					// test and update the label index
					for (int l = 0; l < pro_label.length; l++) {
						if (properties[k].getUri().toString().contains(
								pro_label[l])) {
							label_index = l < label_index ? l : label_index;
						}
					}
				}

			}

			for (int j = 0; j < annotation.length; j++) {

				Property[] properties = annotation[j].getPropertyArray();
				for (int k = 0; k < properties.length; k++) {
					// based on the label index set before, import the
					// node label
					if (cyNodeAttrs.getAttribute(node_processes[i]
							.getIdentifier(), "node_label") == null
							&& properties[k].getUri().toString().contains(
									pro_label[label_index])) {

						String[] tmp = properties[k].getValue().toString()
								.split("<|>");

						if (tmp.length < 3)
							continue;

						AttributeValue = tmp[2];

						cyNodeAttrs.setAttribute(node_processes[i]
								.getIdentifier(), "node_label", AttributeValue);
					}

				}

			}

			if (cyNodeAttrs.getStringAttribute(node_processes[i]
					.getIdentifier(), "node_label") == null
					|| cyNodeAttrs.getStringAttribute(
							node_processes[i].getIdentifier(), "node_label")
							.trim().equals("")) {
				cyNodeAttrs.setAttribute(node_processes[i].getIdentifier(),
						"node_label", node_processes[i].getIdentifier());
			}

		}

		/*
		 * Add artifacts' attributes
		 */
		for (int i = 0; i < artifacts.length; i++) {

			Artifact artifact = artifacts[i];
			// println("Process#" + i);
			// println("Process ID:" + process.getId());

			attributeName = "NodeType";
			AttributeValue = "ARTIFACT";
			cyNodeAttrs.setAttribute(node_artifacts[i].getIdentifier(),
					attributeName, AttributeValue);

			// AccountRef[] accounts = artifact.getAccountArray();

			EmbeddedAnnotation[] annotation = artifact.getAnnotationArray();

			int art_nodeLabel_index = art_label.length;
			for (int j = 0; j < annotation.length; j++) {
				// println("Annotation ID:" + annotation[j].getId());

				Property[] properties = annotation[j].getPropertyArray();
				for (int k = 0; k < properties.length; k++) {

					for (int l = 0; l < art_label.length; l++) {
						if (properties[k].getValue().toString().contains(
								art_label[l])) {
							art_nodeLabel_index = l < art_nodeLabel_index ? l
									: art_nodeLabel_index;
						}
					}

					//
					// if (properties[k].getValue().toString().contains(
					// "size")) {
					// attributeName = "size";
					//
					// AttributeValue = properties[k].getValue()
					// .toString().split("<|>")[2];
					// cyNodeAttrs.setAttribute(node_artifacts[i]
					// .getIdentifier(), attributeName,
					// AttributeValue);
					//
					// attributeName = "size_uri";
					// AttributeValue = properties[k].getUri();
					// cyNodeAttrs.setAttribute(node_artifacts[i]
					// .getIdentifier(), attributeName,
					// AttributeValue);
					// } else
					for (int pt = 0; pt < artifact_keyword.length; pt++) {
						if (properties[k].getValue().toString().contains(
								artifact_keyword[pt])) {
							cyNodeAttrs.setAttribute(node_artifacts[i]
									.getIdentifier(), artifact_name[pt],
									properties[k].getValue().toString().split(
											"<|>")[2]);
						}
					}

					if (properties[k].getValue().toString().contains(
							"dataBlocks")) {

						attributeName = "dataBlocks_uri";
						AttributeValue = properties[k].getUri();
						cyNodeAttrs
								.setAttribute(
										node_artifacts[i].getIdentifier(),
										attributeName, AttributeValue);

						String[] datablock = properties[k].getValue()
								.toString().split("<|>");

						for (int l = 0; l < datablock.length - 1; l++) {
							for (int m = 0; m < art_label.length; m++) {
								if (datablock[l].equals(art_label[m])) {
									art_nodeLabel_index = m < art_nodeLabel_index ? m
											: art_nodeLabel_index;
									// cyNodeAttrs.setAttribute(
									// node_artifacts[i]
									// .getIdentifier(),
									// "node_label", datablock[l + 1]);
								}
							}
							for (int pt = 0; pt < artifact_keyword.length; pt++) {
								if (datablock[l].equals(artifact_keyword[pt])) {
									cyNodeAttrs
											.setAttribute(node_artifacts[i]
													.getIdentifier(),
													artifact_name[pt],
													datablock[l + 1]);
								}
							}

						}

					}

				}
			}

			// set the node_label
			for (int j = 0; j < annotation.length; j++) {
				// println("Annotation ID:" + annotation[j].getId());

				Property[] properties = annotation[j].getPropertyArray();
				for (int k = 0; k < properties.length; k++) {

					if (art_nodeLabel_index == art_label.length
							|| properties[k].getValue().toString().contains(
									art_label[art_nodeLabel_index])) {
						attributeName = "node_label";

						String[] tmp = properties[k].getValue().toString()
								.split("<|>");
						if (tmp.length < 3)
							continue;

						AttributeValue = tmp[2];
						cyNodeAttrs
								.setAttribute(
										node_artifacts[i].getIdentifier(),
										attributeName, AttributeValue);
					}

					if (properties[k].getValue().toString().contains(
							"dataBlocks")) {

						String[] datablock = properties[k].getValue()
								.toString().split("<|>");

						for (int l = 0; l < datablock.length - 1; l++) {
							if (art_nodeLabel_index == art_label.length
									|| datablock[l]
											.equals(art_label[art_nodeLabel_index])) {
								cyNodeAttrs.setAttribute(node_artifacts[i]
										.getIdentifier(), "node_label",
										datablock[l + 1]);
							}

						}

					}
				}

			}

			if (cyNodeAttrs.getStringAttribute(node_artifacts[i]
					.getIdentifier(), "node_label") == null
					|| cyNodeAttrs.getStringAttribute(
							node_artifacts[i].getIdentifier(), "node_label")
							.trim().equals("")) {
				cyNodeAttrs.setAttribute(node_artifacts[i].getIdentifier(),
						"node_label", node_artifacts[i].getIdentifier());
			}
		}

		/*
		 * Add the attribute of Agents
		 */
		for (int i = 0; i < agents.length; i++) {
			Agent agent = agents[i];
			// println("Process#" + i);
			// println("Process ID:" + process.getId());

			attributeName = "NodeType";
			AttributeValue = "AGENT";
			cyNodeAttrs.setAttribute(node_agents[i].getIdentifier(),
					attributeName, AttributeValue);

			AccountRef[] accounts = agent.getAccountArray();

			for (int j = 0; j < accounts.length; j++) {
				// println("AccountRef :" + accounts[j].getRef());
				attributeName = "Account";
				AttributeValue = accounts[j].getRef();
				cyNodeAttrs.setAttribute(node_agents[i].getIdentifier(),
						attributeName, AttributeValue);
			}

			EmbeddedAnnotation[] annotation = agent.getAnnotationArray();

			cyNodeAttrs.setAttribute(node_agents[i].getIdentifier(),
					"node_label", node_agents[i].getIdentifier().toString());
		}

		WasDerivedFrom[] wdf = doc.getOpmGraph().getCausalDependencies()
				.getWasDerivedFromArray();
		WasGeneratedBy[] wgb = doc.getOpmGraph().getCausalDependencies()
				.getWasGeneratedByArray();
		WasTriggeredBy[] wtb = doc.getOpmGraph().getCausalDependencies()
				.getWasTriggeredByArray();
		Used[] used = doc.getOpmGraph().getCausalDependencies().getUsedArray();
		WasControlledBy[] wcb = doc.getOpmGraph().getCausalDependencies()
				.getWasControlledByArray();

		CyEdge[] edge_wdf = new CyEdge[wdf.length];
		CyEdge[] edge_wgb = new CyEdge[wgb.length];
		CyEdge[] edge_wtb = new CyEdge[wtb.length];
		CyEdge[] edge_used = new CyEdge[used.length];
		CyEdge[] edge_wcb = new CyEdge[wcb.length];

		for (int i = 0; i < edge_wdf.length; i++) {
			int index_art_1 = searchArtifact(artifacts, wdf[i].getEffect());
			int index_art_2 = searchArtifact(artifacts, wdf[i].getCause());

			CyNode node_1, node_2;

			// IF not defined in the processes or artifacts
			if (index_art_1 < 0) {
				node_1 = Cytoscape.getCyNode(wdf[i].getEffect().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "node_label",
						node_1.getIdentifier());
			} else {
				node_1 = node_artifacts[index_art_1];
			}
			if (index_art_2 < 0) {
				node_2 = Cytoscape.getCyNode(wdf[i].getCause().getRef()
						.toString(), true);

				cyNodeAttrs.setAttribute(node_2.getIdentifier(), "node_label",
						node_2.getIdentifier());

			} else {
				node_2 = node_artifacts[index_art_2];
			}

			edge_wdf[i] = Cytoscape.getCyEdge(node_1, node_2,
					Semantics.INTERACTION, "wasDerivedFrom", true);

			// this is not necessarily to have a time attribute
			if (wdf[i].getTime() == null)
				continue;

			cyEdgeAttrs.setAttribute(edge_wdf[i].getIdentifier(),
					"NoEarlierThan", wdf[i].getTime().getNoEarlierThan()
							.getTime().toString());
			cyEdgeAttrs.setAttribute(edge_wdf[i].getIdentifier(),
					"NoLaterThan", wdf[i].getTime().getNoLaterThan().getTime()
							.toString());
			cyEdgeAttrs.setAttribute(edge_wdf[i].getIdentifier(), "AccountRef",
					wdf[i].getAccountArray()[0].getRef());

			// Add NoLaterThan as the time information for the nodes
			String new_time = wdf[i].getTime().getNoEarlierThan().toString();
			if (cyNodeAttrs.getStringAttribute(node_1.getIdentifier(), "Time") == null
					|| cyNodeAttrs.getStringAttribute(node_1.getIdentifier(),
							"Time").compareTo(new_time) > 0)
				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "Time",
						new_time);
		}

		for (int i = 0; i < edge_wgb.length; i++) {
			int index_art = searchArtifact(artifacts, wgb[i].getEffect());
			int index_pro = searchProcess(processes, wgb[i].getCause());

			CyNode node_1, node_2;

			// IF not defined in the processes or artifacts
			if (index_art < 0) {
				node_1 = Cytoscape.getCyNode(wgb[i].getEffect().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "node_label",
						node_1.getIdentifier());
			} else {
				node_1 = node_artifacts[index_art];
			}
			if (index_pro < 0) {
				node_2 = Cytoscape.getCyNode(wgb[i].getCause().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_2.getIdentifier(), "node_label",
						node_2.getIdentifier());
			} else {
				node_2 = node_processes[index_pro];
			}

			edge_wgb[i] = Cytoscape.getCyEdge(node_1, node_2,
					Semantics.INTERACTION, "wasGeneratedBy", true);

			// this is not necessarily to have a time attribute
			if (wgb[i].getTime() == null)
				continue;

			cyEdgeAttrs.setAttribute(edge_wgb[i].getIdentifier(),
					"NoEarlierThan", wgb[i].getTime().getNoEarlierThan()
							.getTime().toString());
			cyEdgeAttrs.setAttribute(edge_wgb[i].getIdentifier(),
					"NoLaterThan", wgb[i].getTime().getNoLaterThan().getTime()
							.toString());
			cyEdgeAttrs.setAttribute(edge_wgb[i].getIdentifier(), "AccountRef",
					wgb[i].getAccountArray()[0].getRef());

			// Add NoLaterThan as the time information for the nodes
			String new_time = wgb[i].getTime().getNoEarlierThan().toString();
			if (cyNodeAttrs.getStringAttribute(node_1.getIdentifier(), "Time") == null
					|| cyNodeAttrs.getStringAttribute(node_1.getIdentifier(),
							"Time").compareTo(new_time) > 0)

				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "Time",
						new_time);
		}

		for (int i = 0; i < edge_wtb.length; i++) {
			int index_pro_1 = searchProcess(processes, wtb[i].getEffect());
			int index_pro_2 = searchProcess(processes, wtb[i].getCause());

			CyNode node_1, node_2;

			// IF not defined in the processes or artifacts
			if (index_pro_1 < 0) {
				node_1 = Cytoscape.getCyNode(wtb[i].getEffect().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "node_label",
						node_1.getIdentifier());
			} else {
				node_1 = node_processes[index_pro_1];
			}
			if (index_pro_2 < 0) {
				node_2 = Cytoscape.getCyNode(wtb[i].getCause().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_2.getIdentifier(), "node_label",
						node_2.getIdentifier());
			} else {
				node_2 = node_processes[index_pro_2];
			}

			edge_wtb[i] = Cytoscape.getCyEdge(node_1, node_2,
					Semantics.INTERACTION, "wasTriggeredBy", true);

			// this is not necessarily to have a time attribute
			if (wtb[i].getTime() == null)
				continue;

			cyEdgeAttrs.setAttribute(edge_wtb[i].getIdentifier(),
					"NoEarlierThan", wtb[i].getTime().getNoEarlierThan()
							.getTime().toString());
			cyEdgeAttrs.setAttribute(edge_wtb[i].getIdentifier(),
					"NoLaterThan", wtb[i].getTime().getNoLaterThan().getTime()
							.toString());
			cyEdgeAttrs.setAttribute(edge_wtb[i].getIdentifier(), "AccountRef",
					wtb[i].getAccountArray()[0].getRef());

			// Add NoLaterThan as the time information for the nodes
			String new_time = wtb[i].getTime().getNoEarlierThan().toString();
			if (cyNodeAttrs.getStringAttribute(node_1.getIdentifier(), "Time") == null
					|| cyNodeAttrs.getStringAttribute(node_1.getIdentifier(),
							"Time").compareTo(new_time) > 0)

				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "Time",
						new_time);
		}

		for (int i = 0; i < edge_used.length; i++) {
			int index_pro = searchProcess(processes, used[i].getEffect());
			int index_art = searchArtifact(artifacts, used[i].getCause());

			CyNode node_1, node_2;

			// IF not defined in the processes or artifacts
			if (index_pro < 0) {
				node_1 = Cytoscape.getCyNode(used[i].getEffect().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "node_label",
						node_1.getIdentifier());
			} else {
				node_1 = node_processes[index_pro];
			}
			if (index_art < 0) {
				node_2 = Cytoscape.getCyNode(used[i].getCause().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_2.getIdentifier(), "node_label",
						node_2.getIdentifier());
			} else {
				node_2 = node_artifacts[index_art];
			}

			edge_used[i] = Cytoscape.getCyEdge(node_1, node_2,
					Semantics.INTERACTION, "used", true);

			// this is not necessarily to have a time attribute
			if (used[i].getTime() == null)
				continue;

			cyEdgeAttrs.setAttribute(edge_used[i].getIdentifier(),
					"NoEarlierThan", used[i].getTime().getNoEarlierThan()
							.getTime().toString());
			cyEdgeAttrs.setAttribute(edge_used[i].getIdentifier(),
					"NoLaterThan", used[i].getTime().getNoLaterThan().getTime()
							.toString());
			cyEdgeAttrs.setAttribute(edge_used[i].getIdentifier(),
					"AccountRef", used[i].getAccountArray()[0].getRef());

			// Add NoLaterThan as the time information for the nodes
			String new_time = used[i].getTime().getNoEarlierThan().toString();
			if (cyNodeAttrs.getStringAttribute(node_1.getIdentifier(), "Time") == null
					|| cyNodeAttrs.getStringAttribute(node_1.getIdentifier(),
							"Time").compareTo(new_time) > 0)

				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "Time",
						new_time);

			if (cyNodeAttrs.getStringAttribute(node_2.getIdentifier(), "Time") == null
					|| cyNodeAttrs.getStringAttribute(node_2.getIdentifier(),
							"Time").compareTo(new_time) > 0)

				cyNodeAttrs.setAttribute(node_2.getIdentifier(), "Time",
						new_time);
		}

		for (int i = 0; i < edge_wcb.length; i++) {
			int index_pro = searchProcess(processes, wcb[i].getEffect());
			int index_agent = searchAgent(agents, wcb[i].getCause());

			CyNode node_1, node_2;

			// IF not defined in the processes or agents
			if (index_pro < 0) {
				node_1 = Cytoscape.getCyNode(wcb[i].getEffect().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_1.getIdentifier(), "node_label",
						node_1.getIdentifier());
			} else {
				node_1 = node_processes[index_pro];
			}
			if (index_agent < 0) {
				node_2 = Cytoscape.getCyNode(wcb[i].getCause().getRef()
						.toString(), true);
				cyNodeAttrs.setAttribute(node_2.getIdentifier(), "node_label",
						node_2.getIdentifier());
			} else {
				node_2 = node_agents[index_agent];
			}

			edge_wcb[i] = Cytoscape.getCyEdge(node_1, node_2,
					Semantics.INTERACTION, "wasControlledBy", true);
			// cyEdgeAttrs.setAttribute(edge_wcb[i].getIdentifier(),
			// "NoEarlierThan", wcb[i].getStartTime()
			// .getNoEarlierThan().toString());
			// cyEdgeAttrs.setAttribute(edge_wcb[i].getIdentifier(),
			// "NoLaterThan", wcb[i].getEndTime().getNoLaterThan()
			// .toString());
			// cyEdgeAttrs.setAttribute(edge_wcb[i].getIdentifier(),
			// "AccountRef", wcb[i].getAccountArray()[0].getRef());

			// Add NoEarlierThan as the time information for the nodes
			// String new_time =
			// wcb[i].getStartTime().getNoEarlierThan()
			// .toString();
			// if
			// (cyNodeAttrs.getStringAttribute(node_1.getIdentifier(),
			// "Time") == null
			// || cyNodeAttrs.getStringAttribute(
			// node_1.getIdentifier(), "Time").compareTo(
			// new_time) > 0)
			//
			// cyNodeAttrs.setAttribute(node_1.getIdentifier(),
			// "Time", new_time);
			//
			// if
			// (cyNodeAttrs.getStringAttribute(node_2.getIdentifier(),
			// "Time") == null
			// || cyNodeAttrs.getStringAttribute(
			// node_2.getIdentifier(), "Time").compareTo(
			// new_time) > 0)
			//
			// cyNodeAttrs.setAttribute(node_2.getIdentifier(),
			// "Time", new_time);
		}

		for (int i = 0; i < edge_wdf.length; i++) {
			cyNetwork.addEdge(edge_wdf[i]);
		}

		for (int i = 0; i < edge_wtb.length; i++) {
			cyNetwork.addEdge(edge_wtb[i]);
		}

		for (int i = 0; i < edge_wgb.length; i++) {
			cyNetwork.addEdge(edge_wgb[i]);
		}

		for (int i = 0; i < edge_used.length; i++) {
			cyNetwork.addEdge(edge_used[i]);
		}

		for (int i = 0; i < edge_wcb.length; i++) {
			cyNetwork.addEdge(edge_wcb[i]);
		}
	}

	public int searchArtifact(Artifact[] artifacts, ArtifactRef ref) {
		for (int i = 0; i < artifacts.length; i++) {
			if (artifacts[i].getId().equalsIgnoreCase(ref.getRef().toString()))
				return i;
		}

		return -1;
	}

	public int searchAgent(Agent[] agents, AgentRef ref) {
		for (int i = 0; i < agents.length; i++) {
			if (agents[i].getId().equalsIgnoreCase(ref.getRef().toString()))
				return i;
		}

		return -1;
	}

	public int searchProcess(Process[] processes, ProcessRef ref) {
		for (int i = 0; i < processes.length; i++) {
			if (processes[i].getId().equalsIgnoreCase(ref.getRef().toString()))
				return i;
		}

		return -1;
	}
}
