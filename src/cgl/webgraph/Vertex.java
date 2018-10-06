package cgl.webgraph;

/*
 * <p>Title: Vertex.java</p>
 * <p>Description: Vertex class is for the verteces in the Matrix
 * </p>
 */

import java.util.Vector;

public class Vertex {
  String m_Name;
  int m_Flag;
  int m_StartNode;
  int m_outlinkNumber;
  int m_depth;
  Vector parent_index;
  Vector child_index;
  double m_Pagerank;

  //Constructors
  public Vertex(String n) {
    m_Name = n;
    m_Flag = 0;
    parent_index = new Vector();
    child_index = new Vector();
  }

  //Accessor methods
  public void setName(String n) {
    m_Name = n;
  }

  public String getName() {
    return m_Name;
  }

  public String toString() {
    return m_Name;
  }

  public void setFlag(int n) {
    m_Flag = n;
  }

  public int getFlag() {
    return m_Flag;
  }

  public void setDepth(int n) {
    m_depth = n;
  }

  public int getDepth() {
    return m_depth;
  }

  public void setStartNode(int n) {
    m_StartNode = n;
  }

  public int getStartNode() {
    return m_StartNode;
  }

  void incFlag() {
    ++m_Flag;
  }

  void decFlag() {
    --m_Flag;
  }

  public void setOutlinknumber(int l) {
    m_outlinkNumber = l;
  }

  public int getOutlinknumber() {
    return m_outlinkNumber;
  }

  public void addParent(int i) {
    parent_index.add(new Integer(i));
  }

  public void removeParent(int i) {
    parent_index.remove(new Integer(i));
  }

  public Vector getParentIndexVector() {
    return parent_index;
  }

  public void addChild(int i) {
    child_index.add(new Integer(i));
  }

  public void removeChild(int i) {
    child_index.remove(new Integer(i));
  }

  public Vector getChildIndexVector() {
    return child_index;
  }

  public void setPageRank(double p) {
    m_Pagerank = p;
  }

  public double getPageRank() {
    return m_Pagerank;
  }
}
