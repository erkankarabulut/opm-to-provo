package cgl.cluster;
/*
 * <p>Title: PreferenceCluster.java</p>
 * <p>Description: Class for PreferenceCluster and
 * </p>related functions
 * <p>authors: M Aktas & M Nacar
 * </p>
 */

//import net.nutch.searcher.Hits;
//import net.nutch.searcher.Hit;
//
//import net.nutch.searcher.HitDetailer;
//import net.nutch.searcher.HitDetails;
//import net.nutch.searcher.IndexSearcher;
//import net.nutch.util.NutchConf;
//import net.nutch.searcher.DistributedSearch;

import cgl.cluster.HitScore;
import cgl.library.Library;
import java.io.*;
import java.util.*;


import java.util.Iterator;
import java.util.Vector;
import java.util.Hashtable;


public class PreferenceCluster{
  static public PreferenceCluster _instance;
  static private Hashtable PR_ResultTable= null;
  static private Hashtable weightDistributionTable = null;
//  static Hit[] hit_array = null;
//  static private HitDetailer detailer;
  static private String[] domains = {
    "gov",
    "edu",
    "net",
    "com",
    "mil",
    "org",
    "ame",
    "eur",
    "asi"};




  private PreferenceCluster()  throws IOException {
    //this(new File(NutchConf.get("searcher.dir", ".")));
    PR_ResultTable = new Hashtable();
    weightDistributionTable = new Hashtable();
    setWeightObjects();
   }

   static public PreferenceCluster instance() {
     if(null == _instance) {
       try {
         _instance = new PreferenceCluster();
       }
       catch (Exception ex) {
         ex.printStackTrace();
       }
     }
     return _instance;
   }
/*
  public PreferenceCluster(File dir) throws IOException {
    File servers = new File(dir, "search-servers.txt");
    if (servers.exists()) {
      init(new DistributedSearch.Client(servers));
    } else {
      init(new File(dir, "index"), new File(dir, "segments"));
    }
  }

  private void init(DistributedSearch.Client client) throws IOException {
   detailer = client;
  }

  private void init(File indexDir, File segmentsDir) {
      IndexSearcher indexSearcher;
      try {
        if (indexDir.exists()) {
          indexSearcher = new IndexSearcher(indexDir.toString());
        } else {
          indexSearcher = new IndexSearcher(segmentsDir.listFiles());
        }
        detailer = indexSearcher;
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
  }

  static public HitDetails getDetails(Hit hit) throws IOException {
    return detailer.getDetails(hit);
  }

  static public HitDetails[] getDetails(Hit[] hits) throws IOException {
    return detailer.getDetails(hits);
  }
*/

  static public Hashtable getWeightDistributionTable() {
    return weightDistributionTable;
  }

  /*
  static public void setEntryToPR_ResultTable(Vector vec, PR_Result result) {
    PR_ResultTable.put(vec, result);
  }
  */

  static public void setEntryToPR_ResultTable(Vector vec, File f) {
    PR_ResultTable.put(vec, f);
  }

  /*
  static public PR_Result getEntryFromPR_ResultTable(Vector vec) {
    return (PR_Result) PR_ResultTable.get(vec);
  }
  */

  static public File getEntryFromPR_ResultTable(Vector vec) {
    return (File) PR_ResultTable.get(vec);
  }

  static public Weight getWeightObject(Collection col) {
    Weight object = (Weight) weightDistributionTable.get(col);
    return object;
  }


  static private void setWeightObjects() {
    Vector[] array = combinationGenerator();
    for (int i = 0; i < array.length; i++) {
      Weight object = Weight.instance(array[i]);
      weightDistributionTable.put(array[i], object);
    }
  }


  public static int getTheIndexOfCombination(Collection collection) {
    int size = calculateCombination(domains);
    int result = 0;
    Collection col;
    Collection[] array = new Collection[size];
    //Vector vector = null;
    int[] indices;
    int count = 0;
    CombinationGenerator x = null;
    for (int j = 0; j <= domains.length; j++) {
      x = new CombinationGenerator (domains.length, j);
      StringBuffer combination;
      while (x.hasMore ()) {
        combination = new StringBuffer ();
        indices = x.getNext ();
        col = new TreeSet();
        for (int i = 0; i < indices.length; i++) {
          //combination.append (" ");

          //combination.append (domains[indices[i]]);
          col.add(domains[indices[i]]);
        }
//        System.out.println("col = " + col + " ?  collection = " +  collection + " count = " + count);
        if (col.equals(collection)) {
          System.out.println("count = " + count);
          result = count;
        }
        count++;
      }
    }
    return result;
  }


  public static Vector[] combinationGenerator() {
    int size = calculateCombination(domains);
    Vector[] array = new Vector[size];
    Vector vector = null;
    int[] indices;
    int count = 0;
    CombinationGenerator x = null;
    for (int j = 0; j <= domains.length; j++) {
      x = new CombinationGenerator (domains.length, j);
      StringBuffer combination;
      while (x.hasMore ()) {
        combination = new StringBuffer ();
        indices = x.getNext ();
        vector = new Vector();
        for (int i = 0; i < indices.length; i++) {
          combination.append (" ");
          combination.append (domains[indices[i]]);
          vector.add(domains[indices[i]]);
        }
        //System.out.println (combination.toString());
        array[count] = vector;
        count++;
      }
    }
    //System.out.println("count = " + count);
    return array;
  }

  static public int calculateCombination(String[] array) {
    int[] indices;
    int count = 0;
    CombinationGenerator x = null;
    for (int j = 0; j <= array.length; j++) {
      x = new CombinationGenerator (array.length, j);
      StringBuffer combination;
      while (x.hasMore ()) {
        combination = new StringBuffer ();
        indices = x.getNext ();
        for (int i = 0; i < indices.length; i++) {
          combination.append (array[indices[i]]);
        }
        count++;
      }
    }
    return count;
  }


  public boolean writePRintoFile() {
    try {
      String path = "data/output/";
      String filename = "output_pr_result_table.txt";
      String file = path.concat(filename);
      PrintWriter out = null;
      File f = new File(file);
      out = new PrintWriter(new FileWriter(f));
      int size = PR_ResultTable.size();
      out.println(size);
//UPDATED_BY_AKTAS      
      Set s = PR_ResultTable.keySet();
      Iterator it = s.iterator();
      while (it.hasNext()) {
        Vector vec = (Vector) it.next();
        String combination = getCombination(vec);
        out.println(combination);
        File file_desc = (File) PR_ResultTable.get(vec);
        out.println(file_desc.getName());
      }
      out.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return false;
    }
    return true;
  }

  public Hashtable readPRfromFile() {
    try {
      String path = "C:/Documents and Settings/manacar.FUJI/jbproject/nuc/WebModule1/data/output/";
      String filename = "output_pr_result_table.txt";
      String file = path.concat(filename);
      BufferedReader in = null;
      String lineIn1 = "", lineIn2 = "";
      in = new BufferedReader(new FileReader(file));
      Library library = new Library();
      String lineIn = library.readLine(in);
      int size = atoi(lineIn);
      if (size <= 0) {
        System.err.println("There are no pagerank results in the database.Abort!");
        System.exit( -1);
      }
      Collection col = null;
      File file_desc = null;

      for (int i = 0; i < size; i++) {
        lineIn1 = library.readLine(in);
        lineIn2 = library.readLine(in);
        col = getCombinationCollection(lineIn1);
        file_desc = new File(path.concat(lineIn2));
        PR_ResultTable.put(col, file_desc);
      }
      in.close();

    }
    catch (Exception ex) {
      ex.printStackTrace();
      //return false;
    }
    return PR_ResultTable;
  }


  public int atoi(String number) {
    int rtnVal = 0;
    try {
      rtnVal = Integer.parseInt(number.trim());
    }
    catch (NumberFormatException e) {
    }
      return rtnVal;
  } // end atoi


  public Hashtable readPRankResults(File f) {
    Hashtable table = new Hashtable();
    try {
      //System.out.println("readPRankResults file = " + f);
      BufferedReader inp = new BufferedReader(new FileReader(f));
      String lineIn = "", lineIn1 = "", lineIn2 = "";
      Library library = new Library();
      lineIn = library.readLine(inp);
      while (library.eof()) {
        lineIn1 = library.readLine(inp);
        lineIn2 = library.readLine(inp);
        table.put(lineIn1, lineIn2);
      }
      inp.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return table;
  }

  public Vector getCombinationVector(String combination) {
    Vector vec = new Vector();
    Iterator iter = vec.iterator();
    StringTokenizer str = new StringTokenizer(combination, " ");
    while (str.hasMoreTokens()) {
         vec.addElement(str.nextToken());
    }
    return vec;
  }

  public Collection getCombinationCollection(String combination) {
    Collection col = new TreeSet();
    StringTokenizer str = new StringTokenizer(combination, " ");
    while (str.hasMoreTokens()) {
         col.add(str.nextToken());
    }
    return col;
  }

  public String getCombination(Vector vec) {
    Iterator iter = vec.iterator();
    String combination = "";
    if (iter.hasNext()) {
      String object = ( (Object) iter.next()).toString();
      combination = object;
    }
    while(iter.hasNext()) {
      String object = ((Object)iter.next()).toString();
      combination = combination.concat(" ");
      combination = combination.concat(object);
    }
    return combination;
  }

  public int getMax( float[] hs) throws Exception {
    int index = 0;
    float max = 0.0f;
    for (int i=0; i < hs.length; i++) {
      if (max < hs[i]) {
          max = hs[i];
          index = i;
      }
    }
    return index;
  }
/*
  public Hit[] getPRHitList(Hit[] hits, int index, int plain) throws Exception {
       Vector pr = new Vector();
       float[] product = new float[hits.length];
       for (int i = 0; i < hits.length; i++) {
           HitDetails detail = getDetails(hits[i]);
           String url = detail.getValue("url");
           Vector pref = detail.getPagerank(index);
//           System.out.println("PR Pref = " + pref);
           product[i] = hits[i].getScore() * Float.parseFloat((String) pref.elementAt(plain));
//           System.out.println("Product = " + product[i]);
        }
  // Takes maximum top 10 Hits of HitList
      int length = (int)Math.min(hits.length, 10);
      Hit[] top = new Hit[length];
      for (int i = 0; i < length; i++) {
        int indice = getMax(product);
        System.out.println("Max. Index of " + indice);
        top[i] = hits[indice];
        product[indice] = 0.0f;
      }
      return top; //hit_array;
  }
*/
  /*
  public Hit[] Combine( Hit[] first, Hit[] second, Hit[] third) throws Exception {
    Vector temp = new Vector();
    for ( int i =0; i < 10; i++) {
      if ( i < first.length )
        if( !temp.contains(first[i]) )
          temp.add(first[i]);
      if ( i < second.length )
        if( !temp.contains(second[i]))
          temp.add(second[i]);
      if ( i < third.length)
        if( !temp.contains(third[i]) )
          temp.add(third[i]);
    }
//System.out.println("VECTOR DONE.........");
    Random rand = new Random();
    Hit[] last = new Hit[temp.size()];
    for ( int i = 0; i < temp.size(); i++) {
      last[i] = (Hit) temp.elementAt(i);
  //  System.out.println("Normal array " + i + " = " + last[i].getIndexNo() );
    }
    for (int i = 0; i < last.length; i++) {
        int randomPosition = rand.nextInt(last.length);
        Hit tmp = last[i];
        last[i] = last[randomPosition];
        last[randomPosition] = tmp;
    }
    HitDetails[] det = this.getDetails(last);
    for (int i = 0; i < det.length; i++) {
  //    System.out.println("details = " + det[i].getValue("url"));
    }
 //System.out.println("Combined Hit list size=" + last.length);
    return last;
  }
*/
 /*
  public Hit[] getPRList(Hit[] hits, int index) throws Exception {
      Vector pr = new Vector();
      for (int i = 0; i < hits.length; i++) {
          HitDetails detail = getDetails(hits[i]);
          String url = detail.getValue("url");
          Vector pref = detail.getPagerank(index);
          if ( pref != null)
             pr.add(hits[i]);
      }
      Hit[] hs = new Hit[pr.size()];
      for (int i=0; i < pr.size(); i++)
        hs[i] = (Hit) pr.elementAt(i);
      return hs;
  }
*/
/*
  public HitScore[] getPRList(Hit[] hits, float[] scores, Hashtable hashtable) throws Exception {
    Vector temp = new Vector();
    Collection vec = new TreeSet();
    PreferenceCluster p = PreferenceCluster.instance();
    File f = (File) hashtable.get(vec);
    Hashtable table = readPRankResults(f);
    String url ="";
    String value = "";
    for (int i = 0; i < hits.length; i++) {
      try {
        HitDetails detail = getDetails(hits[i]);
        url = detail.getValue("url");
    //    System.out.println("URL..." + url);
      }
      catch (Exception ex) {}
//System.out.println("Hit array " + hits.length + "Score array " + scores.length);
      value = (String) table.get(url);
      if (value != null) {
        HitScore hs = new HitScore();
        hs.setHits(hits[i]);
        hs.setScore(scores[i]);
        temp.add(hs);
      }
    }
    HitScore[] hs = new HitScore[temp.size()];
    int length = (int)Math.min(temp.size(), 10);     // Max 10 Hits taken
    for (int i=0; i < length; i++)
      hs[i] = (HitScore) temp.elementAt(i);
    return hs;
  }
*/

  public static void main(String [] args) {
  try {
    PreferenceCluster p = PreferenceCluster.instance();
    p.readPRfromFile();
    System.out.println("size = " + PR_ResultTable.size());

  }
  catch (Exception ex) {
    ex.printStackTrace();
  }

}
}

