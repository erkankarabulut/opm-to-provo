package cgl.pagerank;

/*
 * <p>Title: PageRank.java</p>
 * <p>Description: The PageRank class is an abstract class designed to give a general
 * framework for the implementation of all different PageRank calculation methods.
 * </p>
 * <p>authors: M Aktas & M Nacar
 */

import cgl.webgraph.Graph;
import cgl.webgraph.Edge;
import cgl.webgraph.Vertex;
import java.util.Hashtable;
import java.util.Vector;

public abstract class PageRank {

  /**
       * This method returns the PageRank Class that is designed to apply particular
   * method for a PageRank calculation. So, it is a PRank calculation method factory
   */
  public static PageRank methodFactory(String method) throws Exception {
    if (method.equalsIgnoreCase("plain")) {
      return new Plain();
    }
    else if (method.equalsIgnoreCase("weighted")) {
      return new Weighted();
    }
    else {
      throw new Exception("No relevant method was found.");
    }
  }

  /**
   * This method calculates pagerank. It is to be implemented by sub classes.
   */
  public abstract double[] calculatePageRank(Vector edge_matrix_vector,
                                             Vertex[] vertex_vector,
                                             double[] pagerank, double d,
                                             double[] weight);

  /**
   * This method calculates pagerank. It is to be implemented by sub classes.
   */
  public abstract Hashtable returnPageRankResultSet(Vector edge_matrix_vector,
      Vertex[] vertex_vector,
      double[] pagerank, double d, double[] weight);

} // end of PageRank class
