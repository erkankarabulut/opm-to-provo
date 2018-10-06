package pt.tumba.links;

import java.util.*;

import LTSM.dataStructure.Vertex;

/**
 *  Pagerank is a an algorithm that Google utilizes to rank its search results in presence of
 *  multiple resources matching a certain query. In very simple words Pagerank evaluates
 *  and ranks Web sites according to a computed value determined by the number of other
 *  sites linking to them. The way the Pagerank value is computed makes Pagerank
 *  somewhat immune to artificial linking efforts.
 *  
 *  The rank (a numerical measure of importance) of each web page is dependent on the
 *  importance conferred on it by other web pages that have links to it; each web page divides
 *  its importance equally amongst all of the web pages it references. The rank R(p) for any 
 *  web page p can be expressed via a simple summation given the set of web pages 
 *  B(p) that link to p and the outdegree function d+: V -> Z:
 *
 *   R(p) = sum(q in B(p), R(q)/d+(q)) 
 * 
 * Note that the right-hand side of the expression may contain R(p), because the rank 
 * of some R(q) may depend on R(p). For this reason, the rank of each web page is not
 * computed directly, but instead an iterative algorithm is applied using the formula:
 * 
 *   Ri+1(p) = sum(q in B(p), Ri(q)/d+(q))
 * 
 * @author Bruno Martins
 *
 */
public class PageRank {

	/** The value for the PageRank dampening factor */
	private double dampening = 0.85;

	/** The data structure containing the Web linkage graph */
	private WebGraph graph;

	/** A <code>Map</code> containing the PageRank values for each page */
	public Map scores;
	
	/** 
	 * Constructor for PageRank
	 * 
	 * @param graph The data structure containing the Web linkage graph
	 */
	public PageRank ( WebGraph graph ) {
		this.graph = graph;
		this.scores = new HashMap();
		int numLinks = graph.numNodes();
		Double faux = new Double(1/graph.numNodes());
		for(int i=0; i<numLinks; i++) { 
			scores.put(new Integer(i),faux);
		}
	}

	/**
	 * Sets the value for the PageRank dampening factor. The amount of PageRank that
	 * is transferred depends on a dampening factor which stands for â€œthe probability 
	 * that a random surfer will get boredâ€�. The dampening factor generally is set to 0.85.
	 * 
	 * @param damp The dampening factor
	 */
	public void setDampening(double damp) {
		this.dampening = damp;
	}
 
   /**
    * Returns the dampening factor used for the PageRank Algorithm. The amount of PageRank that
	* is transferred depends on a dampening factor which stands for â€œthe probability 
	* that a random surfer will get boredâ€�. The dampening factor generally is set to 0.85.
    * 
    * @return The dampening factor
    */
   public double getDampening() {
		return this.dampening;
	}

	/**
	 * Returns the PageRank value associated with a given link
	 * 
	 * @param link The url for the link
	 * @return The PageRank value associated with the given link
	 */
	public Double pageRank(String link) {
		return pageRank(graph.URLToIdentifyer(link));	
	}
	
	/**
	 * Returns the PageRank value associated with a given link identifyer.
	 * Identifyers are Integer numberes, used in <code>WebGraph</code> to
	 * represent the Web graph for efficiency reasons.
	 * 
	 * @param link An identifyer for the link
	 * @return The PageRank value associated with the given link
	 * @see WebGraph.IdentifyerToURL()
	 */
	private Double pageRank(Integer id) {
		return (Double)(scores.get(id));
	}

	/**
	 * Initializes the PageRank value associated with a given link.
	 * 
	 * @param link The url for the link
	 * @param value The PageRank value to assign
	 */
	public void initializePageRank(String link, double value) {
		Integer id = graph.URLToIdentifyer(link);
		if(id!=null) scores.put(id,new Double(value));	
	}
	
	/**
	 * Initializes PageRank value associated with a given link identifyer.
	 * Identifyers are Integer numberes, used in <code>WebGraph</code> to
	 * represent the Web graph for efficiency reasons.
	 * 
	 * @param link An identifyer for the link
	 * @param value The PageRank value to assign 
	 * @see WebGraph.IdentifyerToURL()
	 */
	public void initializePageRank(Integer id, double value) {
		if(id!=null) scores.put(id,new Double(value));	
	}

	/**
	 * Computes the PageRank value for all the nodes in the Web Graph.
	 * In this method, the number of iterations of the algorithm is set accordingly to
	 * the number of nodes in the Web graph.
	 *
	 */
	public void computePagerank() {
	  int n=graph.numNodes();
	  int iter=iter=((int)Math.abs(Math.log((double)n)/Math.log((double)10)))+1; 
  	  computePagerank(iter);
	}

	/**
	 * Computes the PageRank value for all the nodes in the Web Graph.
	 * The formula of Sergey Brin and Lawrence Page (founders of Google) can be
	 * found in their <a href="http://www-db.stanford.edu/~backrub/google.html">original document</a>.
	 * Essentially:
	 * 
     * The amount of PageRank that is transferred depends on a dampening factor
     *  which stands for â€œthe probability that a random surfer will get boredâ€�. The dampening
     *  factor generally is set to 0.85. 
     *
     * The more outgoing links a web page has, the less PageRank of that page will
     *  be transferred to each of the pages it links to. Very simple: devide the real PageRank
     *  by the number of outgoing links and multiply it with the dampening factor to calculate
     *  the amount of PageRank that is transferred.
     * 
     * Do this for all pages that link to your page and you know your own PageRank.
     *
	 * @param iter The number of iterations for the algorithm
	 * 
	 */
	public void computePagerank(int iter) {
	  int n=graph.numNodes();
	  double aux = (1-dampening)/n;
	  while((iter--)>0) {     
		  Map newScore = new HashMap();
		  for (int j=0;j<n;j++) { 
			Map inlinks = graph.inLinks(new Integer(j));
			Iterator it = inlinks.keySet().iterator();
			Double weight2 = new Double(0);
			while (it.hasNext()) {
				Integer link = (Integer)(it.next());
				Double weight = (Double)(inlinks.get(link));
				if(weight!=null && weight.doubleValue()>0) {
					int numLinks = 0;
					Map outlinks = graph.outLinks(link);
					Iterator it2 = inlinks.keySet().iterator();
					while (it.hasNext()) {
						Integer l = (Integer)(it.next());
						Double w = (Double)(outlinks.get(l));
						if(w!=null && w.doubleValue()>0) numLinks++;
					}
					
					//System.out.println("outlink = " + scores.get(link));
					//System.out.println("inlink = " + numLinks);	
					//double nl = 0.0;
					//if (numLinks == 0) numLinks =0;
					weight2 = new Double(weight2.doubleValue() + (((Double)(scores.get(link))).doubleValue()/numLinks));
					//System.out.println("PR  = " + new Double(weight2.doubleValue()) + "   " + ((Double)(scores.get(link))).doubleValue() + " / " + numLinks);
					
					
				}
			}
			weight2 = new Double(aux + dampening*weight2.doubleValue());
			//if (inlinks.size() == 0) weight2 = 1.0;
			newScore.put(new Integer(j),weight2);
		  }     
		  for (int j=0;j<n;j++) {
		  	scores.put(new Integer(j),(Double)(newScore.get(new Integer(j))));
		  }
	  }
	}
	
	public static void main(String[] args) {

		WebGraph webgraph = new WebGraph();
		webgraph.addLink("Page A", "Page B", 1.0);
		webgraph.addLink("Page B", "Page C", 1.0);
		webgraph.addLink("Page D", "Page C", 1.0);
		webgraph.addLink("Page C", "Page A", 1.0);
		webgraph.addLink("Page A", "Page C", 1.0);
		PageRank pr = new PageRank(webgraph);
		pr.computePagerank(20);

		
		Map scMap = (Map) pr.scores;
		Iterator it = scMap.keySet().iterator();
		while (it.hasNext()) {
			Integer sc = (Integer)(it.next());
			Double scr = (Double) pr.pageRank(sc);
			System.out.println(webgraph.IdentifyerToURL(sc) + " -> " + scr);
		}
		
/*		
		while (itr.hasNext()) {
			Vertex v = itr.next();
			System.out.println("------------------------------");
			if (v.getID() != null) {
			    System.out.println(v.getID() + " : " + pr.pageRank(v.getID()));
			    System.out.println("-------inlinks-----------");					
			    
			    //int id = wg1.URLToIdentifyer(v.getID());
			    //wg1.outlinks(v.getID());
				Map auxMap1 = (Map) wg1.inLinks(v.getID());
				Iterator it = auxMap1.keySet().iterator();
				while (it.hasNext()) {
					Integer link2 = (Integer)(it.next());
					String url = wg1.IdentifyerToURL(link2);
					System.out.println(url + " -> " + v.getID());
				}

			    System.out.println("-------outlinks-----------");					
		    
			    //int id = wg1.URLToIdentifyer(v.getID());
			    //wg1.outlinks(v.getID());
				Map auxMap = (Map) wg1.outLinks(wg1.URLToIdentifyer(v.getID()));
				Iterator it2 = auxMap.keySet().iterator();
				while (it2.hasNext()) {
					Integer link2 = (Integer)(it2.next());
					String url = wg1.IdentifyerToURL(link2);
					System.out.println(v.getID() + " -> " + url);
				}
			    
			}
		

	    PageRank pr = new PageRank(webgraph);
	    pr.computePagerank(20);
*/	
	}
	

}
