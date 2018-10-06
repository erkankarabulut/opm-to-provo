package cgl.main;

/*
 * <p>Title: test.java</p>
 * <p>Description: Test class to see if the memory is enough for a large matrix</p>
 * <p>authors: M Aktas & M Nacar
 */

public class Test {
  protected static double[][] m_PR = null; //Array of PageRank
  protected static String[][] m_URL = null; //Array of PageRank
  protected int m_Size = 500000;
  protected static Test[] pagerank_result_set;

  public Test() {
    m_PR = new double[m_Size][1];
    m_URL = new String[m_Size][1];
    for (int i = 0; i < m_PR.length; i++) {
      m_PR[i][0] = 0.026666;
      m_URL[i][0] = "http:\\xxxxxxxxxxxxxxxxxxxxxxxxx";
    }
  }

  public static void main(String[] args) {

    pagerank_result_set = new Test[445];
    for (int i = 0; i < pagerank_result_set.length; i++) {
      pagerank_result_set[i] = new Test();
    }
    int test = -1;
    try {
      System.in.mark(1);
      test = System.in.read();
      if (test >= 0) {
        System.in.reset();
      }
    }
    catch (java.io.IOException e) {}

  }

}