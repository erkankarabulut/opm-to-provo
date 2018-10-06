package pt.tumba.links;

//package pagerank;    
import java.io.*;   
 
public class pr {   
    
  static Node prn[];// all the nodes  
  static int d,zdy,cy;  
    
    
  public static void calc(Node ns[],double d){  
      double value = 0;  
      double values[] = new double[100];   
        
      for(int i=0;i<zdy;i++){  
          value = 0;  
          Node ns1[] = new Node[100];  
          ns1 = ns[i].getNodein();  
          int j = ns[i].getNodeN();  
          for(int k=0;k<j;k++){   
              value = ns1[k].getValue()/ns1[k].getOutnode() + value;  
          }  
            
          value = value * d + 1 - d;  
          values[i] = value;              
      }  
        
      for(int i=0;i<zdy;i++){  
          ns[i].setValue(values[i]);  
      }  
  }  
    
     
  /*  
   * This is the main calculate algorithm to calculate the PageRank  
   * */   
  public static void calc(Node ns[],double d, int times){   
      double value = 0;   
      double values[] = new double[1000];    
         
      for(int i=0;i<times;i++){   
          value = 0;   
          Node ns1[] = new Node[100];   
          ns1 = ns[i].getNodein();   
          int j = ns[i].getNodeN();   
          for(int k=0;k<j;k++){    
              value = ns1[k].getValue()/ns1[k].getOutnode() + value;   
          }   
             
          value = value * d + 1 - d;   
          values[i] = value;               
      }   
         
      for(int i=0;i<times;i++){   
          ns[i].setValue(values[i]);   
      }   
  }   
     
  
  public static void main(String[] args) throws Exception{  
      d = 0;  
      zdy = 4;  
      prn = new Node[4];  

//      System.out.println("Please input the nodes and link node which popint to it. ");  

//      BufferedReader buf;  
//      String str;  
//      buf=new BufferedReader(new InputStreamReader(System.in));  
//      str=buf.readLine();  
//
//      String sttr[] = new String[100];  
//      sttr = str.split(" ");  
//
//      zdy = sttr.length;  
//      for(int i=0;i<sttr.length;i++){  
//          prn[i] = new Node(sttr[i],0);    
//      }  
//        
//      System.out.println("please input the data ");  
        
//      for(int i=0;i < sttr.length;i++){  
//          str = buf.readLine();   
//          String sss[] = str.split(" ");  
//          int abc[] = new int[100];  
//            
//          for(int j=0;j<sss.length;j++){  
//              abc[j] = Integer.parseInt(sss[j]);  
//          }  
//            
//          for(int j=1;j<sss.length-1;j++){  
//              prn[abc[0]].addNode(prn[abc[j]]);  
//          }  
//            
//          prn[abc[0]].setOutnode(abc[sss.length-1]);  
//      }  
        
      
      Node n1 = new Node("A", 1);
      Node n2 = new Node("B", 1);
      Node n3 = new Node("C", 1);
      Node n4 = new Node("D", 1);
      
      n1.addNodeout(n2);
      n1.addNodein(n3);
      
      n2.addNodeout(n3);
      n2.addNodein(n1);
      
      n3.addNodeout(n1);
      n3.addNodein(n1);
      n3.addNodein(n4);
      
      n4.addNodeout(n3);
      
      prn[0] = n1;
      prn[1] = n2;
      prn[2] = n3;
      prn[3] = n4;
      
      int time = 10;
      for(int i = time;i>0;i--){  
          calc(prn, 1.0, 1);  
      }  
        
      for(int i=0;i<4;i++){  
          System.out.println(prn[i].getValue());  
      }  

  }   
}   
