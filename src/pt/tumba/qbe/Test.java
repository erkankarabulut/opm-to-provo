package pt.tumba.qbe;

import java.util.*;
import pt.tumba.links.*;
import pt.tumba.links.PageRank.*;

/**
 * @author bmartins
 *
 */
public class Test {

	/**
	 *  A list of stop sites for the related pages algorithm
	 */
	private static String[] stopSiteList = {
		"www.sapo.pt",
		"tsf.sapo.pt",
		"exclusivos.sapo.pt",
		"www.clix.pt",
		"images.clix.pt",
		"pagewizard.clix.pt",
		"www.publico.pt",
		"ultimahora.publico.pt",
		"www.fccn.pt",
		"www.infocid.pt",
		"www.fct.mct.pt",
		"www.miau.pt",
		"www.sic.pt",
		"www.dn.pt",
		"www.negocios.pt",
		"www.diariodigital.pt",
		"www.expresso.pt",
		"www.abola.pt",
		"www.iol.pt"
	};

	/**
	 *  The HashMap that stores the dictionary of stop sites
	 */
	private static Map stopSites;

	/**
	 *  Static code to initialize the HashMap with the dictionary of stop sites
	 */
	{
		stopSites = new HashMap();
		for (int i = stopSiteList.length - 1; i >= 0; i--) stopSites.put(stopSiteList[i], null);
		stopSiteList = null;
	}
	
	private Map documentTitles;
	
	public WebGraph graph;
	
	private Map documentSnippets;

	private List relatedPages;
	
	public Test () {
		documentTitles = new HashMap();
		documentSnippets = new HashMap();
		graph = new WebGraph();
		relatedPages = new Vector();
	}

	/**
	 *  Check if the supplied URL is the url for a site listed in the stop list
	 */
	public static boolean isStopSite(String s) {
		if (s == null) return false;
		String site = s.toLowerCase();
		if (site.startsWith("http://")) site = site.substring(7);
		if (site.endsWith("/")) site = site.substring(0,site.length()-1);
		return stopSites.containsKey(site);
	}

	/**
	 *  Check if the supplied URL references a document in a site listed in the stop list
	 */
	public static boolean isStopURL(String s) {
		if (s == null) return false;
		String site = s.toLowerCase();
		if (site.startsWith("http://")) site = site.substring(7);
		int i = site.indexOf("/"); 
		if (i!=-1) site = site.substring(0,i-1);
		return stopSites.containsKey(site);
	}

	public void addDocument ( String url, String title, String snippet ) {
		documentTitles.put(url,title);
		documentSnippets.put(new Integer(url.hashCode()),snippet);
		graph.addLink(url);
	}

	public void addLinkage ( String fromURL, String toURL ) {
		if(documentTitles.containsKey(fromURL) && documentTitles.containsKey(toURL)) {
			graph.addLink(fromURL,toURL, new Double(1.0));
		}
	}

	public void addLinkage ( String fromURL, Iterator toURLs ) {
		if(documentTitles.containsKey(fromURL)) {
			while(toURLs.hasNext()) {
				String toURL = toURLs.next().toString();		
				if(	documentTitles.containsKey(toURL)) {
					graph.addLink(fromURL,toURL,new Double(1.0));
				}
			}
		}
	}
	
	public void computeRelated ( String URL ) {
			Amsler amsler = new Amsler(graph);
			amsler.computeAmsler(URL);
			Map map = amsler.amsler(URL);
			Iterator it = map.keySet().iterator();
			while (it.hasNext()) {
				String url = graph.IdentifyerToURL((Integer)(it.next()));
				if(!isStopSite("")) {}
			}
	}

	public static void main ( String args[] ) throws Exception {
		
		Test test = new Test();
		test.addDocument("A","A","test");
		test.addDocument("B","B","test");
		test.addDocument("C","C","test");
		test.addDocument("D","D","test");

		test.addLinkage("A", "B");
		test.addLinkage("B", "C");
		test.addLinkage("D", "C");
		test.addLinkage("C", "A");

		PageRank pr = new PageRank(test.graph);
		pr.computePagerank();
		

		System.out.println(test.graph.numNodes());
//		Set set = hm.entrySet();		
//		// Get an iterator 
//		Iterator i = set.iterator(); 
//		// Display elements 
//		while(i.hasNext()) { 
//		Map.Entry me = (Map.Entry)i.next(); 
//		System.out.print(me.getKey() + ": "); 
//		System.out.println(me.getValue()); 
//		}
		
	}

}
