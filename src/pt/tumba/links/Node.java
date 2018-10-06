package pt.tumba.links;

import java.io.Serializable;   
import java.util.HashSet;   
import java.util.Iterator;   
   
public class Node implements Serializable{//Define each node, has name, value, and links point to it.   
        String name; // The URL of the Node.   
        double value; // The rank value of the node.   
        int matchscore; // the score of the node match to the search result.   
        HashSet<Node> nodesin,nodesout; // Those nodes who points to this node. out means the back link   
        static final long serialVersionUID = 987654321;   
           
        public void setMatchScore(Integer i){   
            matchscore = i;   
        }   
           
        public Integer getMatchScore(){   
            return matchscore;   
        }   
           
        public int getOutnode(){    
            return nodesout.size();   
        }   
   
        public void setName(String str){   
            this.name = str;   
        }   
   
        public String getName(){   
            return this.name;   
        }   
   
        public void setValue(double i){   
            this.value = i;   
        }   
   
        public double getValue(){   
            return this.value;   
        }   
   
        public Node(String str, int out){   
            setName(str);   
            setValue(1);   
            nodesin = new HashSet<Node>();   
            nodesout = new HashSet<Node>();              
            matchscore = 0;   
        }   
   
        public void addNodein(Node n1){// add node, and need not delete node.   
            if(!nodesin.contains(n1)){   
                nodesin.add(n1);   
            }   
        }   
           
        public void addNodeout(Node n1){   
            if(!nodesout.contains(n1)){   
                nodesout.add(n1);   
            }   
        }   
   
        public Node[] getNodein(){   
            Iterator it = nodesin.iterator();   
            Node nodes[] = new Node[nodesin.size()];   
            int index = 0;   
               
            while(it.hasNext()){   
                nodes[index] = (Node)it.next();   
                index ++;   
            }   
               
            return nodes;   
        }   
                   
        public Node[] getNodesout() {   
            Iterator it = nodesout.iterator();   
            Node nodes[] = new Node[nodesout.size()];   
            int index = 0;   
               
            while(it.hasNext()){   
                nodes[index] = (Node)it.next();   
                index ++;   
            }   
               
            return nodes;   
        }   
   
        public int getNodeN(){   
            return nodesin.size();   
        }   
           
        public void printNode(){   
            Node[] nin = this.getNodein();   
            for(int j = 0; j < nin.length;j++){    
                System.out.println(this.getName() + " is pointed by " + nin[j].getName());   
            }   
               
            Node[] nout = this.getNodesout();   
            for(int i = 0; i < nout.length;i++){   
                System.out.println(this.getName() + " pointed to " + nout[i].getName());   
            }   
        }   
   
    } 
