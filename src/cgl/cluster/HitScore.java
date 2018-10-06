package cgl.cluster;
/*
 * <p>Title: HitScore.java</p>
 * <p>Description: HitScore Class
 * </p>
 * <p>authors: M Aktas & M Nacar
 */

//import net.nutch.searcher.Hit;

public class HitScore {

    double PR;
//    Hit hit;
//
//    public void setHits (Hit h) {
//      hit = h;
//    }

    public void setPR(double pr) {
      PR = pr;
    }

    public double getPR() {
      return PR;
    }

//    public double getScore() {
//      return hit.getScore() * PR;
//    }
//
//    public Hit getHit() {
//      return hit;
//    }

  public static void main(String[] args) {
  }
}
