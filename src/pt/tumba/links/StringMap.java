package pt.tumba.links;

public class StringMap {   
    private String source,destination;   
   
    public StringMap(String res, String des){   
        this.setDestination(des);   
        this.setResource(res);   
    }   
       
    public String getResource() {   
        return source;   
    }   
   
    public void setResource(String resource) {   
        this.source = resource;   
    }   
   
    public String getDestination() {   
        return destination;   
    }   
   
    public void setDestination(String destination) {   
        this.destination = destination;   
    }   
       
       
   
}
