package cgl.webgraph;

/*
 * <p>Title: Edge.java</p>
 * <p>Description: Edge class is for the Links connecting the Verteces
 * </p>
 */

public class Edge {
  int m_Src;
  int m_Dest;
  int m_flag;
  int row;
  int col;

  // Constructors
  Edge(int src, int dest , Edge next ,
       int flag) {
    m_Src = src;
    m_Dest = dest;
    m_flag = flag;
  }

  Edge(int src, int dest) {
    m_Src = src;
    m_Dest = dest;
  }

  Edge(int src, int dest, Edge next) {
    m_Src = src;
    m_Dest = dest;
  }

  // accessor functions
  public int get_row() {
    return row;
  }

  public void set_row(int i) {
    row = i;
  }

  public int get_col() {
    return col;
  }

  public void set_col(int j) {
    col = j;
  }

  public int get1() {
    return m_Src;
  }

  public int get2() {
    return m_Dest;
  }

  public int getFlag() {
    return m_flag;
  }

  public void setFlag(int f) {
    m_flag = f;
  }

}
