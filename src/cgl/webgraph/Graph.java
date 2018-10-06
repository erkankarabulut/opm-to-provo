package cgl.webgraph;

/*
 * <p>Title: Graph.java</p>
 * <p>Description: Graph.java  --- mixed adjacency matrix / adjacency list implementation
 * </p>
 * <p>authors: M Aktas & M Nacar
 */

import LTSM.process.ARFF_Processor;
import cgl.pagerank.PageRank;
import cgl.cluster.PreferenceCluster;
import cgl.cluster.CombinationGenerator;
import cgl.cluster.PreferenceCluster;
import cgl.cluster.Weight;
//import cgl.linkanalyzer.Analyzer;

//import net.nutch.searcher.Hits;
//import net.nutch.searcher.Hit;

import cgl.library.Library;
import cgl.main.QuickSort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;

import java.util.*;

public class Graph {

  protected Vector edge_matrix_vector = null;
  protected Vertex[] m_URL = null;  
  protected Vertex[] m_URL_SORTED = null;    
  protected double[] m_PRank = null;
  protected double[] m_PR_weight = null;
  protected int m_Size = 0;
  protected int m_NEdges = 0;
  static final int UNVISITED = 0;

  public Vector tmpsubGraphEdgeVector = null;
  public Vector tmpsubGraphVertexVector = null;
  public Vector subGraphVertexVector = null;
  protected int maxdepth = 0;

  public Graph() {
    //subGraphEdgeVector = new Vector();
    subGraphVertexVector = new Vector();
    edge_matrix_vector = new Vector();
  }

  //this function is to fill the matrix with given input
  public void fillGraph(BufferedReader inp1, BufferedReader inp2,
                        BufferedReader inp3, PrintWriter log) {
    String lineIn = "";
    int miles;
    int src,
        dst;
    if (m_URL != null) {
      System.err.println("More than one initialization.  Abort!");
      System.exit( -1);
    }
    // first record
    Library library = new Library();
    lineIn = library.readLine(inp1);
    m_Size = atoi(lineIn);
    if (m_Size <= 0) {
      System.err.println("Zero entries or illegal data.  Abort!");
      System.exit( -1);
    }

    edge_matrix_vector = new Vector();

    m_URL = new Vertex[m_Size];
    
    m_URL_SORTED = new Vertex[m_Size];    

    m_PRank = new double[m_Size];

    m_PR_weight = new double[m_Size];

    for (src = 0; src < m_Size; src++) {
      lineIn = library.readLine(inp2);
      m_URL[src] = new Vertex(lineIn);
    }

    int index = 0;
    while (!library.eof(inp3)) {
//      if (index % 5000 == 0) {
//       System.out.println("[INFO] - POGRESS : (edges)mode(5000) " + index +
//                          "...");
//       log.println("[INFO] - POGRESS : (edges)mode(5000) " + index + "...");
//      }
      lineIn = library.readWord(inp3);
      if (lineIn == null || lineIn.length() == 0) {
        break;
      }
      src = findURL(lineIn);
      if (src < 0) {
//        log.print("Unknown 1st URL " + lineIn + ", discarding rest of record: ");
        lineIn = library.readLine(inp3);
//        log.println(lineIn);
        continue;
      }
      lineIn = library.readWord(inp3);
      dst = findURL(lineIn);
      if (dst < 0) {
//        log.print("Unknown URL " + lineIn + ", discarding rest of record: ");
        lineIn = library.readLine(inp3);
//        log.println(lineIn);
        continue;
      }
      if (src != dst) {
        m_URL[src].addChild(dst);
        m_URL[dst].addParent(src);
        Edge m_List = new Edge(src, dst);
        m_List.setFlag(1);
        if (!edge_matrix_vector.contains(m_List)) {
          edge_matrix_vector.add(index, m_List);
          index++;
          m_NEdges++;
        }
      }
    }
  }

  //this function is to fill the matrix with given input
  public void fillGraph(Vector url_vector, Vector edge_vector) {
    int miles;
    int src,
        dst;

    m_NEdges = 0;
    maxdepth = 0;
    m_Size = url_vector.size();

    if (m_Size <= 0) {
      System.err.println("Zero entries or illegal data.  Abort!");
      System.exit( -1);
    }

    m_URL = new Vertex[m_Size];

    edge_matrix_vector = null;

    edge_matrix_vector = new Vector();

    m_PRank = new double[m_Size];

    m_PR_weight = new double[m_Size];

    int count = 0;
    for (Iterator iter = url_vector.iterator(); iter.hasNext(); ) {
      m_URL[count] = new Vertex( ( (String) iter.next()).toString());
      count++;
    }
    int index = 0;
    String lineIn1 = "", lineIn2 = "";
    for (Iterator iter_ = edge_vector.iterator(); iter_.hasNext(); ) {
      String token = (String) iter_.next();
      StringTokenizer tokenizer = new StringTokenizer(token, " ");
      while (tokenizer.hasMoreElements()) {
        lineIn1 = (String) tokenizer.nextElement();
        lineIn2 = (String) tokenizer.nextElement();
      }
      src = findURL(lineIn1);
      if (src < 0) {
        continue;
      }
      dst = findURL(lineIn2);
      if (dst < 0) {
        continue;
      }
      if (src != dst) {
        m_URL[src].addChild(dst);
        m_URL[dst].addParent(src);
        Edge m_List = new Edge(src, dst);
        m_List.setFlag(1);
        if (!edge_matrix_vector.contains(m_List)) {
          edge_matrix_vector.add(index, m_List);
          index++;
          m_NEdges++;
        }
      }
    }
  }

  public void getLinkAnalysis() {
    Hashtable table = new Hashtable();
    table.put("gov", new Vector());
    table.put("edu", new Vector());
    table.put("mil", new Vector());
    table.put("net", new Vector());
    table.put("com", new Vector());
    table.put("org", new Vector());
    table.put("ame", new Vector());
    table.put("eur", new Vector());
    table.put("asi", new Vector());
    table.put("gov_ame", new Vector());
    table.put("edu_ame", new Vector());
    table.put("mil_ame", new Vector());
    table.put("net_ame", new Vector());
    table.put("com_ame", new Vector());
    table.put("org_ame", new Vector());
    table.put("gov_eur", new Vector());
    table.put("edu_eur", new Vector());
    table.put("mil_eur", new Vector());
    table.put("net_eur", new Vector());
    table.put("com_eur", new Vector());
    table.put("org_eur", new Vector());
    table.put("gov_asi", new Vector());
    table.put("edu_asi", new Vector());
    table.put("mil_asi", new Vector());
    table.put("net_asi", new Vector());
    table.put("com_asi", new Vector());
    table.put("org_asi", new Vector());
    //Analyzer analizer = new Analyzer();
    for (int i = 0; i < m_URL.length; i++) {
      String url = m_URL[i].getName();
      String domain = "";
      try {
        domain = "edu";//analizer.getDomain(url);
        Vector vec = (Vector) table.get(domain);
        vec.add(new Integer(i));
        table.remove(domain);
        table.put(domain, vec);
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    
    Set s = table.keySet();
    Iterator it = s.iterator();
    
    
    
    
    //Enumeration enum = table.keys();
    System.out.print("[INFO] - ");
//    while (enum.hasMoreElements()) {
    while (it.hasNext()) {  
      String dom = (String) it.next();//enum.nextElement();
      
      if ( ( (Vector) table.get(dom)).size() != 0) {
        System.out.print("| dom = " + dom + " " +
                         ( (Vector) table.get(dom)).size());
      }
    }
    System.out.println();
  }

  public void addMagicalNodeIntoGraph() {
    //this is link vector
    Vector link_vector = new Vector();
    for (int i = 0; i < m_URL.length; i++) {
      link_vector.add(m_URL[i].getName());
    }
    //this is edge vector  String edge = "url1 url2"
    Vector edge_vector = new Vector();
    for (Iterator iter = edge_matrix_vector.iterator(); iter.hasNext(); ) {
      Edge edge = (Edge) iter.next();
      if (edge.get1() == -1 || edge.get2() == -1) {
        continue;
      }
      String url_1 = m_URL[edge.get1()].getName();
      String url_2 = m_URL[edge.get2()].getName();
      String lineIn = url_1 + " " + url_2;
      if (edge_vector.contains(lineIn)) {
        continue;
      }
      edge_vector.add(lineIn);
    }
    link_vector.add("http://www.magicalnode.com");
    for (int i = 0; i < m_URL.length; i++) {
      int parentnumber = m_URL[i].getParentIndexVector().size();
      int childnumber = m_URL[i].getChildIndexVector().size();
      if (parentnumber == 0 && childnumber == 0) {
        continue;
      }
      else if (parentnumber == 0) {
        //this is source node
        String url_1 = "http://www.magicalnode.com";
        String url_2 = m_URL[i].getName();
        String lineIn = url_1 + " " + url_2;
        if (edge_vector.contains(lineIn)) {
          continue;
        }
        edge_vector.add(lineIn);

      }
      else if (childnumber == 0) {
        //this is source
        String url_1 = m_URL[i].getName();
        String url_2 = "http://www.magicalnode.com";
        String lineIn = url_1 + " " + url_2;
        if (edge_vector.contains(lineIn)) {
          continue;
        }
        edge_vector.add(lineIn);
      }
    }
    fillGraph(link_vector, edge_vector);
  }

  public void getGraphReport(PrintWriter log) {
    int sink_count = 0;
    int source_count = 0;
    for (int i = 0; i < m_URL.length; i++) {
      int parentnumber = m_URL[i].getParentIndexVector().size();
      int childnumber = m_URL[i].getChildIndexVector().size();
      System.out.println("m_URL[" + i + "] = " + m_URL[i].getName() + " inlink # = " +
              parentnumber + " outlink # = " + childnumber);      
      log.println("m_URL[" + i + "] = " + m_URL[i].getName() + " inlink # = " +
                  parentnumber + " outlink # = " + childnumber);
      if (parentnumber == 0) {
        source_count++;
      }
      if (childnumber == 0) {
        sink_count++;
      }
    }
    System.out.println("[INFO] - There are total " + source_count +
                       " sources and " + sink_count +
                       " danglink links in this graph.");
    log.println("[INFO] - There are total " + source_count + " sources and " +
                sink_count + " danglink links in this graph.");
  }

  
  public void getGraphReport() {
	    int sink_count = 0;
	    int source_count = 0;
	    for (int i = 0; i < m_URL.length; i++) {
	      int parentnumber = m_URL[i].getParentIndexVector().size();
	      int childnumber = m_URL[i].getChildIndexVector().size();
	      //System.out.println("m_URL[" + i + "] = " + m_URL[i].getName() + " inlink # = " +
	      //        parentnumber + " outlink # = " + childnumber);      
	      if (parentnumber == 0) {
	        source_count++;
	      }
	      if (childnumber == 0) {
	        sink_count++;
	      }
	    }
	    System.out.println("[INFO] - There are total " + source_count +
	                       " sources and " + sink_count +
	                       " danglink links in this graph.");
  }
  
  public Vector getDangLinkVector() {
    Vector danglink_vec = new Vector();
    for (int i = 0; i < m_URL.length; i++) {
      int childnumber = m_URL[i].getChildIndexVector().size();
      if (childnumber == 0) {
        danglink_vec.add(new Integer(i));
      }
    }
    return danglink_vec;
  }

  public void removeDanglingLinks(Vector danglink_vec) {
    //this is link vector
    Vector link_vector = new Vector();
    for (int i = 0; i < m_URL.length; i++) {
      if ( (!danglink_vec.contains(new Integer(i))) /*&&
                    (!sourcelink_vec.contains(new Integer(i)))*/) {
        link_vector.add(m_URL[i].getName());
      }
    }

    //this is edge vector  String edge = "url1 url2"
    Vector edge_vector = new Vector();
    for (Iterator iter = edge_matrix_vector.iterator(); iter.hasNext(); ) {
      Edge edge = (Edge) iter.next();
      if (edge.get1() == -1 || edge.get2() == -1) {
        continue;
      }
      String url_1 = m_URL[edge.get1()].getName();
      String url_2 = m_URL[edge.get2()].getName();
      String lineIn = url_1 + " " + url_2;
      if (edge_vector.contains(lineIn)) {
        continue;
      }
      edge_vector.add(lineIn);
    }
    fillGraph(link_vector, edge_vector);
  }

  public void createMagicNodeNode(String html, String edgelist, PrintWriter log) {
    Vector sourcelink_vec = new Vector();
    Vector danglink_vec = new Vector();
    for (int i = 0; i < m_URL.length; i++) {
      int parentnumber = m_URL[i].getParentIndexVector().size();
      int childnumber = m_URL[i].getChildIndexVector().size();
      if (parentnumber == 0) {
        sourcelink_vec.add(new Integer(i));
      }
      if (childnumber == 0) {
        danglink_vec.add(new Integer(i));
      }
    }
    PrintWriter printwriter1 = null, printwriter2 = null;
    try {
      File file1 = new File(html);
      printwriter1 = new PrintWriter(new FileWriter(file1));
      File file2 = new File(edgelist);
      printwriter2 = new PrintWriter(new FileWriter(file2));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }
    String magicalnode = "http://magicnode.com";
    for (Iterator iter = danglink_vec.iterator(); iter.hasNext(); ) {
      Integer item = (Integer) iter.next();
      String v = m_URL[item.intValue()].getName();
      printwriter2.println(v.concat(" ").concat(magicalnode));
    }
    for (Iterator iter = sourcelink_vec.iterator(); iter.hasNext(); ) {
      Integer item = (Integer) iter.next();
      String v = m_URL[item.intValue()].getName();
      printwriter1.println(v);
      printwriter2.println(magicalnode.concat(" ").concat(v));
    }
    System.out.println("[INFO] - Magical node html content is written into " +
                       html + " its edge list is written into " + edgelist);
    log.println("[INFO] - Magical node html content is written into " + html +
                " its edge list is written into " + edgelist);
    printwriter1.close();
    printwriter2.close();
  }

  public void printSourceNodes(String out, PrintWriter log) {
    Vector sourcelink_vec = new Vector();
    for (int i = 0; i < m_URL.length; i++) {
      int parentnumber = m_URL[i].getParentIndexVector().size();
      int childnumber = m_URL[i].getChildIndexVector().size();
      if (parentnumber == 0) {
        sourcelink_vec.add(new Integer(i));
      }
    }
    PrintWriter printwriter = null;
    try {
      File file = new File(out);
      printwriter = new PrintWriter(new FileWriter(file));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }
    for (Iterator iter = sourcelink_vec.iterator(); iter.hasNext(); ) {
      Integer item = (Integer) iter.next();
      String v = m_URL[item.intValue()].getName();
      printwriter.println(v);
    }

    //System.out.println("[INFO] - Total of " + sourcelink_vec.size() + " URLs written into source_vertex.txt files.");
    log.println("[INFO] - Total of " + sourcelink_vec.size() +
                " URLs written into source_vertex.txt files.");
    printwriter.close();
  }

  public int nURLs() {
    return m_Size;
  }

  public int nEdges() {
    return m_NEdges;
  }

  public boolean validURL(String V) {
    return findURL(V) >= 0;
  }

  void clearFlags() { // Clear traversal flags
    for (int k = 0; k < m_Size; k++) {
      m_URL[k].setFlag(UNVISITED);
    }
  }

  public void nodeList() {
    for (int k = 0; k < m_Size; k++) {
      System.out.print(m_URL[k].getName() + ", ");
    }
    System.out.println();
  }

  public void nodeList(PrintWriter out) { // file
    for (int k = 0; k < m_Size; k++) {
      out.print(m_URL[k].getName() + ", ");
    }
    out.println();
  }

  public Hashtable edgeListTable() {
    Hashtable table = new Hashtable();
    Vector edge_vec = new Vector();
    for (int src = 0; src < m_Size; src++) {
      Vector vector = m_URL[src].child_index;
      for (Iterator iter = vector.iterator(); iter.hasNext(); ) {
        Integer item = (Integer) iter.next();
        Edge edge = new Edge(src, item.intValue());
        edge_vec.add(edge);
      }
      table.put(m_URL[src].getName(), edge_vec);
    }
    return table;
  }

  public void edgeList() {
    int sum = 0, leg;
    int index = edge_matrix_vector.size();
    for (int k = 0; k < index; k++) {
      if ( ( (Edge) edge_matrix_vector.get(k)) == null) {
        break;
      }
      else if ( ( (Edge) edge_matrix_vector.get(k)).get1() < 0) {
        System.out.println("Start node : " +
                           m_URL[ ( (Edge) edge_matrix_vector.get(k)).get2()].
                           getName());
      }
      else {
        System.out.println(m_URL[ ( (Edge) edge_matrix_vector.get(k)).get1()].
                           getName() + " to "
                           +
                           m_URL[ ( (Edge) edge_matrix_vector.get(k)).get2()].
                           getName());
      }
    }

  }

  public void edgeList(PrintWriter out) { // file
    int sum = 0, leg;
    int index = edge_matrix_vector.size();
    for (int k = 0; k < index; k++) {
      if ( ( (Edge) edge_matrix_vector.get(k)) == null) {
        break;
      }
      else if ( ( (Edge) edge_matrix_vector.get(k)).get1() < 0) {
        out.println("Start node : " +
                    m_URL[ ( (Edge) edge_matrix_vector.get(k)).get2()].getName());
      }
      else {
        out.println(m_URL[ ( (Edge) edge_matrix_vector.get(k)).get1()].getName() +
                    " to "
                    +
                    m_URL[ ( (Edge) edge_matrix_vector.get(k)).get2()].getName());
      }
    }
  }

  public String getCombination(Collection col) {
    Iterator iter = col.iterator();
    String combination = "";
    while (iter.hasNext()) {
      String object = ( (Object) iter.next()).toString();
      combination = combination.concat(" ");
      combination = combination.concat(object);
    }
    return combination;
  }

  public void createFileDBFromKarmaDB(String workflow_name){
	  //read first index file	  
	  //create index file
	  //retrieve data from KarkamDB
	  //write data to data file 
  }
  
  public void calculatePlainPageRankResult(double dump_factor, int iteration,
                                           String path, String fname) {
    PageRank pr = null;
    try {
      pr = PageRank.methodFactory("plain");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    int n = m_PRank.length;
    for (int j = 0; j < n; j++) {
      m_PRank[j] = 1.0;
    }

    for (int i = 0; i < iteration; i++) {
      m_PRank = pr.calculatePageRank(edge_matrix_vector, m_URL, m_PRank,
                                     dump_factor, null);
    }
    try {
      String filename = (path.concat("output_plain_pr_")).concat(fname).concat(".txt");
      File f = new File(filename);
      PrintWriter out = new PrintWriter(new FileWriter(f));
      for (int i = 0; i < m_Size; i++) {
        m_URL[i].setPageRank(m_PRank[i]);
      }
      dumpPRankResults(out);
      out.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  
  public void calculatePlainPageRankResult(double dump_factor, int iteration,
          String path, String fname, BufferedWriter bw, BufferedWriter bw_index/*, int size*/) {
		PageRank pr = null;
		try {
		pr = PageRank.methodFactory("plain");
		}
		catch (Exception ex) {
		ex.printStackTrace();
		}
		
		int n = m_PRank.length;
		for (int j = 0; j < n; j++) {
		m_PRank[j] = 1.0;
		}
		
		for (int i = 0; i < iteration; i++) {
		m_PRank = pr.calculatePageRank(edge_matrix_vector, m_URL, m_PRank,
		    dump_factor, null);
		}
		try {
		String filename = (path.concat("output_plain_pr_")).concat(fname).concat(".txt");
		File f = new File(filename);
		PrintWriter out = new PrintWriter(new FileWriter(f));
		for (int i = 0; i < m_Size; i++) {
		m_URL[i].setPageRank(m_PRank[i]);
		}
		dumpPRankResults(out, bw, bw_index/*, size*/);
		out.close();
		}
		catch (Exception ex) {
		ex.printStackTrace();
		}
}  
  

  
  public void calculateAllPageRankResults(double dump_factor, 
		                                  int iteration,
                                          String path) {
    //Analyzer link_analyzer = new Analyzer();
    PreferenceCluster p = PreferenceCluster.instance();
    Hashtable w_table = p.getWeightDistributionTable();
    Hashtable result_table = new Hashtable();
    
    //UPDATED_BY_AKTAS
    Set s = w_table.keySet();
    Iterator it = s.iterator();
    
    //Enumeration enum = w_table.keys();
    Collection col = null;
    PageRank pr = null;
    String filename = path.concat("pr_result_set.txt");
    PrintWriter out = null;
    Collection col_ = new TreeSet();
    Vector tmp = new Vector();
    for (int i = 0; i < 10; i++) {
      tmp.add(i, new Vector());
    }

    Vector vec = null;
    while (it.hasNext()) {
//    while (enum.hasMoreElements()) {
//    enum.nextElement();
      col = (Collection) it.next(); 
      int size = col.size();
      vec = (Vector) tmp.get(size);
      tmp.remove(size);
      vec.add(col);
      tmp.add(size, vec);
    }
//    System.out.println("collection size = " + tmp.size());
    Iterator iter_ = tmp.iterator();
    int count = 0;
    while (iter_.hasNext()) {
      Vector vector = (Vector) iter_.next();
      for (int r = 0; r < vector.size(); r++) {
        col = (Collection) vector.get(r);
        if (col.size() == 0) {
          try {
            pr = PageRank.methodFactory("plain");
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
          int n = m_PRank.length;
          for (int j = 0; j < n; j++) {
            m_PRank[j] = 1.0;
          }
          for (int i = 0; i < iteration; i++) {
            m_PRank = pr.calculatePageRank(edge_matrix_vector, 
            		                       m_URL, m_PRank,
                                           dump_factor, null);
          }      
          
        }
        else if (col.size() != 0) {
          Weight w = p.getWeightObject(col);
          for (int i = 0; i < m_Size; i++) {
            String url = m_URL[i].getName();
            String domain = "";
            try {

//UPDATED_BY_AKTAS            	
//              domain = link_analyzer.getDomain(url);

            
            }
            catch (Exception ex) {
              ex.printStackTrace();
            }
            double weight = w.getWeight(domain);
            if (weight != -1.0) {
              m_PR_weight[i] = weight;
            }
          }
          try {
            pr = PageRank.methodFactory("weighted");
          }
          catch (Exception ex) {
            ex.printStackTrace();
          }
          int n = m_PRank.length;
          for (int j = 0; j < n; j++) {
            m_PRank[j] = 1.0;
          }
          for (int i = 0; i < iteration; i++) {
            m_PRank = pr.calculatePageRank(edge_matrix_vector, m_URL, m_PRank,
                                           dump_factor, m_PR_weight);
           //System.out.println("mURL = " + m_URL[i] + " m_PRank = " + m_PRank[i]);
          }
                    
        }
        File f = new File(filename);
        count++;
        Hashtable table = new Hashtable();
        for (int i = 0; i < m_Size; i++) {
          m_URL[i].setPageRank(m_PRank[i]);
          table.put(m_URL[i].getName(), new Double(m_PRank[i]));
        }
        result_table.put(col, table);
      }
    }
    
    //System.out.println("result table = " + result_table.size());
    try {

      out = new PrintWriter(new FileWriter(filename));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    for (int k = 0; k < m_URL.length; k++) {
      Enumeration enumeration = result_table.elements();
      String name = m_URL[k].getName();
      //System.out.println(name);
      out.println(name);
      while (enumeration.hasMoreElements()) {
        Hashtable t = (Hashtable) enumeration.nextElement();
        double value = ( (Double) t.get(name)).doubleValue();
        out.print(value + " ");
        //System.out.println(value + " ");

      }
      out.println();
    }
    out.close();

    /*
      int size = (int)(m_URL.length / 1500);
      PrintWriter[] out_array = new PrintWriter[size];
      int index = m_URL.length%1500;
      for (int s = 0; s < m_URL.length; s = s + 1500) {
        filename = path.concat("pr_result_set");
        filename = filename.concat("_").concat(Integer.toString(s)).concat(
        ".txt");
        try {
          out_array[index] = new PrintWriter(new FileWriter(filename));
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
      for (int k = s*1500 ; k < (s+1)*1500 & k < m_URL.length; k++) {
        Enumeration enumeration = result_table.elements();
        String name = m_URL[k].getName();
        out_array[index].println(name);
        while (enumeration.hasMoreElements()) {
          Hashtable t = (Hashtable) enumeration.nextElement();
          double value = ( (Double) t.get(name)).doubleValue();
          out_array[index].print(value + " ");
        }
        out.println();
      }
      out_array[index].close();
           }
     */
    //p.writePRintoFile(path);
  }

  //BFS UTILITY FUNCTION
  //this method applies breath first search starting from a given URL
  public int getSizeOfNodesInBiggestGraph(PrintWriter log) {
    int first;
    int size;
    int max = 0;

    clearFlags();
    for (first = 0; first < m_Size; first++) {

      if (m_URL[first].getFlag() == UNVISITED) {
        if (first % 5000 == 0) {
          //System.out.println("[INFO] - breadthfirst call made starting from page index " + first + ".");
          log.println(
              "[INFO] - breadthfirst call made starting from page index " +
              first + ".");
        }
        breadth1st(first);
        size = tmpsubGraphVertexVector.size();
        if (size > max) {
          max = size;
          //subGraphEdgeVector = tmpsubGraphEdgeVector;
          subGraphVertexVector = tmpsubGraphVertexVector;
          System.out.println("[INFO] - max connected graph size is " + max +
                             " with depth of " + maxdepth);
          log.println("[INFO] - max connected graph size is " + max +
                      " with depth of " + maxdepth);
        }
      }
    }
    //Vector vec = (Vector) subGraphTable.get(m_URL[max]);
    return max;
  }

  //this function is companying the function above for BFS
  void breadth1st(int first) {
    //int size = 0;
    //Vector vector = new Vector();
    tmpsubGraphVertexVector = new Vector();
    tmpsubGraphEdgeVector = new Vector();
    Pair edge = new Pair(0, 0);
    Deque work = new Deque();
    Edge current;
    edge.set( -1, first);
    work.enqueue(edge);
    int depth = 0;
    maxdepth = 0;
    m_URL[first].setDepth(depth);
    while (!work.empty()) {
      edge = work.remove();
      int src = edge.get1();
      int dst = edge.get2();
      if (m_URL[dst].getFlag() != UNVISITED) {
        continue;
      }
      if (edge.get1() == -1) {
        Edge m_List = new Edge( -1, dst);
        Vector tmp = new Vector();
        int i = 0;
        tmp.add(i, m_List);
        i = 1;
        for (Iterator iter = edge_matrix_vector.iterator(); iter.hasNext(); ) {
          Edge item = (Edge) iter.next();
          tmp.add(i, item);
          i++;
        }
        edge_matrix_vector = tmp;
      }
      m_URL[dst].setFlag(1);
      //current = m_URL[dst].list();
      Vector vec = m_URL[dst].getChildIndexVector();
      src = dst;
      if (!tmpsubGraphVertexVector.contains(m_URL[src])) {
        tmpsubGraphVertexVector.add(m_URL[src]);
      }
      for (Iterator iter = vec.iterator(); iter.hasNext(); ) {
        Integer item = (Integer) iter.next();
        dst = item.intValue();
        int flag = m_URL[dst].getFlag();
        edge.set(src, dst);
        if (flag == UNVISITED) {
          m_URL[dst].setDepth(m_URL[src].getDepth() + 1);
          work.enqueue(edge);
          if (!tmpsubGraphVertexVector.contains(m_URL[dst])) {
            tmpsubGraphVertexVector.add(m_URL[dst]);
          }
        }
      }
      if (m_URL[src].getDepth() > maxdepth) {
        maxdepth = m_URL[src].getDepth();
      }
    }
  }

  public void dumpGraphAfterRemovingSinks(String out1, String out2, String out3,
                                          PrintWriter log) {
    PrintWriter printwriter1 = null;
    PrintWriter printwriter2 = null;
    PrintWriter printwriter3 = null;

    try {
      File file1 = new File(out1);
      printwriter1 = new PrintWriter(new FileWriter(file1));
      File file2 = new File(out2);
      printwriter2 = new PrintWriter(new FileWriter(file2));
      File file3 = new File(out3);
      printwriter3 = new PrintWriter(new FileWriter(file3));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }

    for (int i = 0; i < m_URL.length; i++) {
      String v = m_URL[i].getName();
      printwriter2.println(v);
    }

    Iterator it = edge_matrix_vector.iterator();
    while (it.hasNext()) {
      Edge edge = (Edge) it.next();
      String src_node = m_URL[edge.get1()].getName();
      String dst_node = m_URL[edge.get2()].getName();
      printwriter3.print(src_node);
      printwriter3.print(" ");
      printwriter3.println(dst_node);
    }
    log.println("[INFO] - Total of " + m_URL.length + " pages and " +
                edge_matrix_vector.size() +
        " edges written after removing danglinks into .._nosink.txt files.");
    printwriter1.println(m_URL.length);
    printwriter1.close();
    printwriter2.close();
    printwriter3.close();

  }

  public void dumpConnectedGraphWithMaxNode(String out1, String out2,
                                            String out3, PrintWriter log) {
    PrintWriter printwriter1 = null;
    PrintWriter printwriter2 = null;
    PrintWriter printwriter3 = null;

    try {
      File file1 = new File(out1);
      printwriter1 = new PrintWriter(new FileWriter(file1));
      File file2 = new File(out2);
      printwriter2 = new PrintWriter(new FileWriter(file2));
      File file3 = new File(out3);
      printwriter3 = new PrintWriter(new FileWriter(file3));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }
    //this is link vector
    int count = 0;
    Iterator iterator = subGraphVertexVector.iterator();
    Vector link_vector = new Vector();
    while (iterator.hasNext()) {
      Vertex src = (Vertex) iterator.next();
      link_vector.add(src.getName());
      count++;
    }
    Vector edge_vector = new Vector();
    for (Iterator iter = edge_matrix_vector.iterator(); iter.hasNext(); ) {
      Edge edge = (Edge) iter.next();
      if (edge.get1() == -1 || edge.get2() == -1) {
        continue;
      }
      String url_1 = m_URL[edge.get1()].getName();
      String url_2 = m_URL[edge.get2()].getName();
      String lineIn = url_1 + " " + url_2;
      if (edge_vector.contains(lineIn)) {
        continue;
      }
      edge_vector.add(lineIn);
    }

    fillGraph(link_vector, edge_vector);

    for (int i = 0; i < m_URL.length; i++) {
      String v = m_URL[i].getName();
      printwriter2.println(v);
    }

    Iterator it = edge_matrix_vector.iterator();
    while (it.hasNext()) {
      Edge edge = (Edge) it.next();
      String src_node = m_URL[edge.get1()].getName();
      String dst_node = m_URL[edge.get2()].getName();
      printwriter3.print(src_node);
      printwriter3.print(" ");
      printwriter3.println(dst_node);
    }
    log.println("[INFO] - Total of " + count + " pages and " +
                edge_matrix_vector.size() +
                " edges written into connected graph .txt files.");
    printwriter1.println(count);
    printwriter1.close();
    printwriter2.close();
    printwriter3.close();

  }

  //UTILITY FUNCTIONS
  public void dumpPRankResults(File f, String combination) {
    try {
      PrintWriter out = new PrintWriter(new FileWriter(f));
      out.println("PAGERANK RESULTS for " + combination);
      for (int src = 0; src < m_Size; src++) {
        Vertex vertex = m_URL[src];
        out.println(vertex.getName());
        out.println(vertex.getPageRank());
      }
      out.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public void dumpPRankResults(PrintWriter out, BufferedWriter bw, BufferedWriter bw_index/*, int size*/) {
	m_URL_SORTED = m_URL;	
	QuickSort qs = new QuickSort();
	//System.out.println(m_URL_SORTED.length);
	qs.quick_srt(m_URL_SORTED,0,m_URL_SORTED.length-1);
	m_URL_SORTED = qs.re_sort(m_URL_SORTED);
	//if (m_Size >= size) {
    try {	
    for (int src = 0; src < m_Size; src++) {
      Vertex vertex = m_URL_SORTED[src];
      if (!vertex.getName().equals("http://www.magicalnode.com")){
      out.println(vertex.getName() + " " + vertex.getPageRank());

		//bw.append((new Double(m_URL_SORTED[src].getPageRank())).toString());
		bw.append(vertex.getName() + ", " + vertex.getPageRank());
	    bw.newLine();
	    
	    bw_index.append(m_URL_SORTED[src].getName());	
	    bw_index.newLine();
//	    if (src < m_Size -1) {
//	    	bw.append(",");
//	    } 	
      }
    }
    
//    for (int i = m_Size-1; i < size; i++) {
//     	bw.append("0");
//  	    if (i < size -1) {
//  	    	bw.append(",");
//  	    } 		
//      }

    
//    bw.newLine();
	} catch (IOException e) {
		e.printStackTrace();
	}    
	//}
  }

  public void dumpPRankResults(PrintWriter out) {
	    out.println("PAGERANK RESULTS");  
		m_URL_SORTED = m_URL;	
		QuickSort qs = new QuickSort();
		System.out.println(m_URL_SORTED.length);
		qs.quick_srt(m_URL_SORTED,0,m_URL_SORTED.length-1);
		m_URL_SORTED = qs.re_sort(m_URL_SORTED);
	    for (int src = 0; src < m_Size; src++) {
	      Vertex vertex = m_URL_SORTED[src];
	      out.println("Node" + "[" + vertex.getName() + "]" +
	                  " PR = (" + vertex.getPageRank() + ")" +
	                  " ");    
	    }
	  }

  
  public void dump(PrintStream out) { // Presumably to System.out
    for (int src = 0; src < m_Size; src++) {
      out.print("Node" + "[" + m_URL[src].getName() + "]"
                + ", edges to:  ");
      Vector vec = m_URL[src].getChildIndexVector();
      for (Iterator iter = vec.iterator(); iter.hasNext(); ) {
        Integer item = (Integer) iter.next();
        int dest = item.intValue();
        out.print("(" + m_URL[dest].getName() + ")");
      }
      out.println();
    }
  }

  public void dumpWebTree() {
    for (int src = 0; src < m_Size; src++) {
      System.out.print("Node" + "[" + m_URL[src].getName() + "]"
                       + ", edges to:  ");
      Vector vector = m_URL[src].getChildIndexVector();
      Iterator iter = vector.iterator();
      while (iter.hasNext()) {
        Integer item = (Integer) iter.next();
        int dest = item.intValue();
        System.out.print("(" + m_URL[dest].getName() + ")");
      }
      System.out.println();
    }
  }

  public void dumpWebTree(PrintWriter out) {
    for (int src = 0; src < m_Size; src++) {
      out.print("Node" + "[" + m_URL[src].getName() + "]"
                + ", edges to:  ");
      Vector vec = m_URL[src].getChildIndexVector();
      for (Iterator iter = vec.iterator(); iter.hasNext(); ) {
        Integer item = (Integer) iter.next();
        int dest = item.intValue();
        out.print("(" + m_URL[dest].getName() + ")");
      }
      out.println();
    }
  }

  public String firstURL() {
    return m_URL[0].getName();
  }

  public int findURL(String name) {
    for (int k = 0; k < m_Size; k++) {
      if (name.compareToIgnoreCase(m_URL[k].getName()) == 0) {
        return k;
      }
    }
    return -1;
  }

  static int atoi(String number) {
    int rtnVal = 0;

    try {
      rtnVal = Integer.parseInt(number.trim());
    }
    catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return rtnVal;
  }

  //Maing funtion for test purposes
  public static void main(String[] args) {
    Graph graph = new Graph();
    String filename1 = "data/input/count_url.txt";
    String filename2 = "data/input/vertex_url.txt";
    String filename3 = "data/input/edge_url.txt";
    PrintWriter out = null;
    BufferedReader d1, d2, d3;
    try {
      d1 = new BufferedReader(new FileReader(filename1));
      d2 = new BufferedReader(new FileReader(filename2));
      d3 = new BufferedReader(new FileReader(filename3));
      graph.fillGraph(d1, d2, d3, out);
      System.out.println("first url = " + graph.firstURL());
      int length = graph.m_URL.length;
      Vertex[] cvertex = new Vertex[length];
      cvertex = graph.m_URL;
      for (int i = 0; i < cvertex.length; i++) {
        System.out.println("url = " + cvertex[i].getName());
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  class Triplet {
    int row;
    int col;
    double value;

    Triplet(int i, int j, double v) {
      row = i;
      col = j;
      value = v;
    }

    int get_row() {
      return row;
    }

    void set_row(int i) {
      row = i;
    }

    int get_col() {
      return col;
    }

    void set_col(int j) {
      col = j;
    }

    double get_value() {
      return value;
    }

    void set_value(double v) {
      value = v;
    }
  }

}
