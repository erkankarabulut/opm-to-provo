package pt.tumba.links;

import java.util.Comparator;   

public class NodeMatchScoreCompare implements Comparator {   
   
    /* first compare their matched score with the search thing,  
       if it is the same, them compare the pagerank value. */   
    public int compare(Object o1, Object o2) {   
        Node n1 = (Node)o1;   
        Node n2 = (Node)o2;   
           
        if(n1.getMatchScore() >   
            n2.getMatchScore()){   
            return -1;   
        }else if(n1.getMatchScore() ==   
            n2.getMatchScore()){   
               
            return n1.getValue() >= n2.getValue()?-1:1;    
            /*if(n1.getValue() >= n2.getValue()){  
                return -1;  
            }else{  
                return 1;  
            }*/   
        }   
           
        return 1;   
    }   
   
} 