package cgl.pagerank;

/*
 * <p>Title: Plain.java</p>
 * <p>Description: The Plain class does the calculation of the PageRank
 * Result sets based on given matrices. This class does not
 * include the notion of the weighted links. It is a multhreaded class
 * to speed-up the calculations.
 * </p>
 * <p>authors: M Aktas & M Nacar
 */

import cgl.webgraph.Graph;
import cgl.webgraph.Edge;
import cgl.webgraph.Vertex;
import java.util.Hashtable;
import java.util.Vector;
import java.util.*;
import java.io.*;

public class Plain
    extends PageRank {

  static Vector edge_matrix_vector;
  static double X[];
  static double Y[];
  int nnz, n;

  /**
   * Constructors.
   */
  public Plain() {
  }

  /**
   * This function calculates the PageRank Vector. Returns Hashtable where key
   * is vertex and entry is PRank of that vertex.
   */
  public Hashtable returnPageRankResultSet(Vector matrix_vector,
                                           Vertex[] vertex_vector, double[] PR,
                                           double d, double[] weight) {
    Hashtable table = new Hashtable();
    nnz = matrix_vector.size();
    n = vertex_vector.length;
    Plain p = new Plain();
    edge_matrix_vector = matrix_vector;
    //initilize pr array
    X = PR;
    Y = new double[n];
    for (int j = 0; j < Y.length; j++) {
      Y[j] = 0.0;
    }
    //create Threads for each column of the EdgeMatrix
    try {
      p.matrix_vector_multiplication(vertex_vector);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    for (int i = 0; i < nnz; i++) {
      double x = (1 - d) + d * Y[i];
      table.put(vertex_vector[i], Double.valueOf(String.valueOf(x)));
    }
    return table;
  }

  /**
       * This function calculates the PageRank Vector. Returns double [n][1] vector.
   */
  public double[] calculatePageRank(Vector matrix_vector,
                                    Vertex[] vertex_vector, double[] PR,
                                    double d, double[] weight) {
    nnz = matrix_vector.size();
    n = vertex_vector.length;
    Plain p = new Plain();
    edge_matrix_vector = matrix_vector;
    //initilize pr array
    X = PR;
    Y = new double[n];
    for (int j = 0; j < Y.length; j++) {
      Y[j] = 0.0;
    }
    //create Threads for each column of the EdgeMatrix
    //System.out.println("Before PageRank Calculation = vertex_vector[5] = " + vertex_vector[5].getName() + " PR = " + X[5]);
    try {
      p.matrix_vector_multiplication(vertex_vector);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    for (int i = 0; i < n; i++) {
      Y[i] = (1 - d) + d * Y[i];
      //System.out.println("Pagerank Calculation = " + vertex_vector[i].getName() + " PR = " + Y[i]);
    }
    return Y;
  }

  public void matrix_vector_multiplication(Vertex[] m_URL) {
    int nnz = edge_matrix_vector.size();
    for (int k = 0; k < nnz; k++) {
      Edge edge = (Edge) edge_matrix_vector.get(k);
      int row = edge.get1();
      int col = edge.get2();
      if (row != -1) {
        Vertex parent = m_URL[edge.get1()];

        int outlinknumber = parent.getChildIndexVector().size();
        Y[col] = Y[col] + edge.getFlag() * (X[row] / outlinknumber);
        //System.out.println("row [" + row + "]" + " col [" + col + "]" + " edge.flag = " + edge.getFlag() + " parentoutlinknumber = " + outlinknumber + " edge.getFlag() * (X[row] / outlinknumber) = " + edge.getFlag() * (X[row] / outlinknumber) + " Y[col] = " + Y[col]);
      }
    }
  }

  public void testtub(String path, String file, double dump_factor,
                      int iteration) {
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
    map.fillGraph(inp1, inp2, inp3, logFile);
    logFile.println("[INFO] - The data file has " + map.nURLs() + " URLs, and "
                    + map.nEdges() + " edges connecting them.");

    logFile.println("[INFO] - Before removing danglink links - Graph Report");
    map.getGraphReport(logFile);
    int iter = 0;
    while (map.getDangLinkVector().size() != 0) {
      map.removeDanglingLinks(map.getDangLinkVector());
      iter++;
    }
    logFile.println("[INFO] - After removing danglink links with " + iter +
                    " iteration - Graph Report");
    map.getGraphReport(logFile);

    logFile.println("[INFO] - The data file has " + map.nURLs() + " URLs, and "
                    + map.nEdges() + " edges connecting them.");

    try {
      map.calculatePlainPageRankResult(dump_factor, iteration, "", "");
      logFile.println("[INFO] - Plain page rank calculations successfully completed. System is ready for the Search");
      System.out.println("[INFO] - Plain page rank calculations successfully completed. System is ready for the Search");
      print_output(logFile);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    logFile.close();
  }

  void print_output(PrintWriter logFile) {
    logFile.println("[INFO] - OUTPUT ARRAY");
    for (int i = 0; i < Y.length; i++) {
      logFile.println("Y[" + i + "] = " + Y[i]);
    }
  }

  public static void main(String[] argv) {
    Plain p = new Plain();
    p.testtub("data/", "testub_plain", 0.85, 1);
  }

} //end of Plain class