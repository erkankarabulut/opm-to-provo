package cgl.webgraph;

/*
 * <p>Title: Pair.java</p>
 * <p>Description: Pair class is for the pair in the Matrix
 * </p>
 */

public class Pair {

  int m_First, m_Second;

  Pair(int v0, int v1) {
    set(v0, v1);
  }

  Pair(Pair v) {
    set(v.get1(), v.get2());
  }

  void set(int v0, int v1) {
    m_First = v0;
    m_Second = v1;
  }

  int get1() {
    return m_First;
  }

  int get2() {
    return m_Second;
  }

}
