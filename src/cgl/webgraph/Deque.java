package cgl.webgraph;

/*
 * <p>Title: Deque.java</p>
 * <p>Description: Deque class is used by BFS
 * </p>
 */

public class Deque {
  Deque() {
    m_Front = m_Back = null;
  }

  void push(Pair v) {
    m_Front = new DQnode(new Pair(v), m_Front);
    if (m_Back == null) {
      m_Back = m_Front;
    }
  }

  void enqueue(Pair v) {
    if (m_Front == null) {
      m_Front = m_Back = new DQnode(new Pair(v), null);
    }
    else {
      m_Back = m_Back.m_Next = new DQnode(new Pair(v), null);
    }
  }

  Pair remove() {
    Pair v = null;

    if (m_Front != null) {
      v = m_Front.m_Edge;
      m_Front = m_Front.m_Next;
      if (m_Front == null) {
        m_Back = null;
      }
    }
    return v;
  }

  boolean empty() {
    return m_Front == null;
  }

  class DQnode {
    DQnode(Pair v, DQnode nxt) {
      m_Edge = new Pair(v);
      m_Next = nxt;
    }

    Pair m_Edge;
    DQnode m_Next;
  }

  DQnode m_Front, m_Back;
};
