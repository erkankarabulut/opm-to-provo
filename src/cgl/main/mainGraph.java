package cgl.main;

/*
 * <p>Title: mainGraph.java</p>
 * <p>Description: Main class to create the web graph</p>
 * <p>authors: M Aktas & M Nacar
 */

import cgl.webgraph.Graph;
import cgl.cluster.PreferenceCluster;
import cgl.cluster.CombinationGenerator;
import cgl.cluster.PreferenceCluster;
import cgl.cluster.Weight;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.PrintStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.File;
import java.util.Vector;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.*;

public class mainGraph {
  public mainGraph() {

  }

  public static void main(String[] argv) {
    if (argv.length < 4) {
      System.out.println(
          "Usage: WebGraph -path <path> -datafile <file> -factor <dump_factor> -iter <pr_iteration>");
      return;
    }
    String file = "";
    String path = "";
    String pr_path = "";
    double dump_factor = 0.0;
    int iter = 0;
    for (int i = 0; i < argv.length; i++) {
      if ("-path".equals(argv[i])) {
        path = argv[i + 1];
      }
      if ("-datafile".equals(argv[i])) {
        file = argv[i + 1];
      }
      else if ("-factor".equals(argv[i])) {
        dump_factor = Double.parseDouble(argv[i + 1]);
      }
      else if ("-iter".equals(argv[i])) {
        iter = Integer.parseInt(argv[i + 1]);
      }
      else if ("-pr_path".equals(argv[i])) {
         pr_path = argv[i + 1];
      }


    }
    mainGraph graph = new mainGraph();
    graph.run(path, file, dump_factor, iter, pr_path);
  } // end main

  public void run(String path, String file, double dump_factor, int iter, String pr_path) {
    Graph map = new Graph();
    PrintWriter logFile = null;
    BufferedReader inp1 = null;
    BufferedReader inp2 = null;
    BufferedReader inp3 = null;
    String countfile = (path.concat("input/count_").concat(file)).concat(".txt");
    String vertexfile = (path.concat("input/vertex_").concat(file)).concat(
        ".txt");
    String edgefile = (path.concat("input/edge_").concat(file)).concat(".txt");

    String logfile = (path.concat("log/log_").concat(file)).concat(".txt");

    String nosink_countfile = (path.concat("input/count_").concat(file)).concat(
        "_nosink").concat(".txt");
    String nosink_vertexfile = (path.concat("input/vertex_").concat(file)).
        concat("_nosink").concat(".txt");
    String nosink_edgefile = (path.concat("input/edge_").concat(file)).concat(
        "_nosink").concat(".txt");

    String maxnode_countfile = (path.concat("input/count_").concat(file)).
        concat("_maxnode").concat(".txt");
    String maxnode_vertexfile = (path.concat("input/vertex_").concat(file)).
        concat("_maxnode").concat(".txt");
    String maxnode_edgefile = (path.concat("input/edge_").concat(file)).concat(
        "_maxnode").concat(".txt");

    String sourcenodefile = (path.concat("input/source_nodes_").concat(file)).
        concat(".txt");

    try {
      File f = new File(logfile);
      logFile = new PrintWriter(new FileWriter(f));
      inp1 = new BufferedReader(new FileReader(countfile));
      inp2 = new BufferedReader(new FileReader(vertexfile));
      inp3 = new BufferedReader(new FileReader(edgefile));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }
    //logFile.println("Step - 1 : Web Graph data is read into memory.");

    //logFile.println("\nStep - 2 : A matrix is consrtucted for the Web Graph and filled in with data.");
    map.fillGraph(inp1, inp2, inp3, logFile);
    System.out.println("[INFO] - The data file has " + map.nURLs() + " URLs, and "
                       + map.nEdges() + " edges connecting them.");

    logFile.println("[INFO] - The data file has " + map.nURLs() + " URLs, and "
                    + map.nEdges() + " edges connecting them.");

    logFile.println("[INFO] - Before adding magical node - Graph Report");
    System.out.println("[INFO] - Before adding magical node - Graph Report");
    map.getGraphReport(logFile);
    map.addMagicalNodeIntoGraph();
    logFile.println("[INFO] - Before removing danglink links - Graph Report");
    System.out.println("[INFO] - Before removing danglink links - Graph Report");
    map.getGraphReport(logFile);

    int loop_count = 0;
    while (map.getDangLinkVector().size() != 0) {
      map.removeDanglingLinks(map.getDangLinkVector());
      loop_count++;
    }

    System.out.println("[INFO] - After removing danglink links with " + loop_count + " iteration - Graph Report");
    logFile.println("[INFO] - After removing danglink links with " + loop_count +
                    " iteration - Graph Report");

    map.getGraphReport(logFile);

    System.out.println("[INFO] - The data file has " + map.nURLs() + " URLs, and "
                       + map.nEdges() + " edges connecting them.");

    logFile.println("[INFO] - The data file has " + map.nURLs() + " URLs, and "
                    + map.nEdges() + " edges connecting them.");

    map.dumpGraphAfterRemovingSinks(nosink_countfile, nosink_vertexfile,
                                    nosink_edgefile, logFile);
    //map.printSourceNodes(sourcenodefile, logFile);

    //logFile.println("\nHere is the Node List\n");
    //map.nodeList(logFile);
    //logFile.println("\nHere is the Edge List\n");
    //map.edgeList(logFile);

    int size = map.getSizeOfNodesInBiggestGraph(logFile);

    //map.dumpConnectedGraphWithMaxNode(maxnode_countfile, maxnode_vertexfile,
    //                                  maxnode_edgefile, logFile);

    //map.edgeList();
     //map.edgeList(logFile);
     //logFile.println();
     //map.dumpWebTree();
     //map.dumpWebTree(logFile);
     logFile.println(
         "\nStep - 3 : PageRank of every vertex is calculated from top to bottom");
      try {
      map.calculateAllPageRankResults(dump_factor, iter, pr_path);
      //map.calculatePlainPageRankResult(dump_factor, iter, pr_path);
      logFile.println("\nStep - 4 : All page rank calculations successfully completed. System is ready for the Search");
      System.out.println("[INFO] - All page rank calculations successfully completed. System is ready for the Search");
      // test the weight correlation
      // Vector vec = new Vector();
      // vec.add("mil"); vec.add("ame");
      // PreferenceCluster p = PreferenceCluster.instance();
      // Weight w = (Weight) p.getWeightObject(vec);
      // System.out.println("mil ame = " + w.get_mil_ame());
      // System.out.println("mil = " + w.get_mil());
      // System.out.println("gov = " + w.get_gov());
      // File f = (File) p.getEntryFromPR_ResultTable(vec);
      // Hashtable pr_table = map.readPRankResults(f);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    logFile.close();
  }
} // end class WebGraph
