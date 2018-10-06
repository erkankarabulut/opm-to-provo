package cgl.main;

import cgl.webgraph.Vertex;

public class QuickSort{
	
	  public QuickSort() {
		  
	  }
	
	  public static Vertex[] re_sort(Vertex array[])
	  {
		  Vertex [] re_sorted_array = new Vertex[array.length];
		  for (int i=0; i<array.length;i++) {
			  re_sorted_array[array.length-1-i] = array[i];
		  }
		  return re_sorted_array;	  
	  }

	  public static void quick_srt(Vertex array[],int low, int n){
	  int lo = low;
	  int hi = n;
	  if (lo >= n) {
	  return;
	  }
	  double mid = array[(lo + hi) / 2].getPageRank();
	  while (lo < hi) {
	  while (lo<hi && array[lo].getPageRank() < mid) {
	  lo++;
	  }
	  while (lo<hi && array[hi].getPageRank() >= mid) {
	  hi--;
	  }
	  if (lo < hi) {
	  Vertex T = array[lo];
	  array[lo] = array[hi];
	  array[hi] = T;
	  }
	  }
	  if (hi < lo) {
	  int T = hi;
	  hi = lo;
	  lo = T;
	  }
	  quick_srt(array, low, lo);
	  quick_srt(array, lo == low ? lo+1 : lo, n);
	  }	
	
	  public static void main(String a[]){
	  int i;
	  
	  Vertex[] m_URL = new Vertex[10];
	  
	  for (i=0; i<10; i++) {
	        Vertex v = new Vertex("File_".concat((new Integer(i)).toString()));  v.setPageRank(1-i*0.1);  m_URL[i] = v;
	  }
	  

	  System.out.println(" Quick Sort\n\n");
	  System.out.println("Values Before the sort:\n");
	  for(i = 0; i < m_URL.length; i++)
	  System.out.print( m_URL[i].getPageRank()+"  ");
	  System.out.println();
	  quick_srt(m_URL,0,m_URL.length-1);
	  System.out.print("Values after the sort:\n");
	  for(i = 0; i <m_URL.length; i++)
	  System.out.print(m_URL[i].getPageRank()+"  ");
	  System.out.println();
  
	  }


	}
