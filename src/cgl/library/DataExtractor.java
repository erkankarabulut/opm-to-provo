package cgl.library;
/*
 * <p>Title: DataExtractor.java</p>
 * <p>Description: DataExtractor Class
 * <p>authors: M Aktas & M Nacar
 */

import java.util.Vector;

import cgl.webgraph.*;
import java.io.*;
import java.util.*;
import java.nio.channels.*;
//import net.nutch.io.*;
//import net.nutch.pagedb.*;
//import net.nutch.linkdb.*;
//import net.nutch.db.*;

public class DataExtractor {

  public int count = 0;

  public DataExtractor() {
    //linkVector = new Vector();
  }
/*
  public int removeLinksANDdumpData(WebDBReader reader, int start, int boundary, int depth,
                                    String out1, String out2, String out3) {
    PrintWriter printwriter1 = null;
    PrintWriter printwriter2 = null;
    PrintWriter printwriter3 = null;

    try {
      File file1 = new File(out1);
      printwriter1 = new PrintWriter(new FileWriter(file1));
      File file2 = new File(out2);
      printwriter2 = new PrintWriter(new FileWriter(file2));
      File file3 = new File(out3);
      printwriter3 = new PrintWriter(new FileWriter(file3));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }


    Hashtable table = new Hashtable();
    Vector vec = null;
    int count = 0;
    int index = 0;
    /////////////////////////////////////////
    Page page = null;
    MD5Hash md5hash = null;
    String url = "";
    Link link = null, in_link = null, in_in_link = null;
    Link[] link_array = null, in_link_array = null, in_in_link_array = null;
    /////////////////////////////////////////


    Enumeration en = reader.links();
    int loopcount = 0;
    while (en.hasMoreElements() && loopcount < start) {
      try {
        Link l = (Link) en.nextElement();
        loopcount++;
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    while (en.hasMoreElements() && count < boundary) {
      try {
        link = (Link) en.nextElement();
        page = reader.getPage(link.getURL().toString());
        md5hash = page.getMD5();
        link_array = reader.getLinks(md5hash);
        ////////////////////////////////////////////////////////////////////////
        int i, j;
        if (link_array.length != 0) {
          vec = new Vector();
          for (i = 0; i < link_array.length; i++) {

            in_link = link_array[i];
            page = reader.getPage(in_link.getURL().toString());
            md5hash = page.getMD5();
            in_link_array = reader.getLinks(md5hash);
            url = in_link.getURL().toString();
            if (in_link_array.length != 0 && url.startsWith("http")) {
              if (!vec.contains(in_link) && in_link != link) {
                vec.add(in_link);
                count++;
              }
            }
          }
          url = link.getURL().toString();
          if (url.startsWith("http")) {
            if (index % 50 == 0) {
              System.out.println("[INFO] - Total " + index +
                                 " seed pages covered.");
            }
            table.put(link, vec);
            count++;
            index++;
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    System.out.println("[INFO] - 1)  Total " + count +
        " pages (parent and child) with one or more children is read into a vector.");

    int parent_count = 0;
    int child_count = 0;

    //UPDATED_BY_AKTAS
    Set s = table.keySet();
    Iterator it = s.iterator();
    while (it.hasNext()) {
    //Enumeration enum = table.keys();
    //while (enum.hasMoreElements()) {
      try {
        link = (Link) it.next(); //enum.nextElement();
        Vector vector = (Vector) table.get(link);
        page = reader.getPage(link.getURL().toString());
        md5hash = page.getMD5();
        Link[] url_links = reader.getLinks(md5hash);
        printwriter2.println(link.getURL().toString());
        parent_count++;
        for (Iterator iter = vector.iterator(); iter.hasNext(); ) {
          child_count++;
          Link l = (Link) iter.next();
          if (l != link) {
            printwriter2.println(l.getURL().toString());
            printwriter3.print(link.getURL().toString());
            printwriter3.print(" ");
            printwriter3.println(l.getURL().toString());
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    int total_count = parent_count + child_count;
    System.out.println("[INFO] - 2)  Total of " + total_count + " pages with "
                       + parent_count + " parent pages and " + child_count
                       + " child pages written into vertex_raw.txt.");
    printwriter1.println(total_count);
    printwriter1.close();
    printwriter2.close();
    printwriter3.close();

    //int s = 0;
    return 0;

  }
*/
/*
  public int removeLinksANDdumpData(WebDBReader reader, int start, int boundary, int depth,
                                    String out1, String out2, String out3) {
    PrintWriter printwriter1 = null;
    PrintWriter printwriter2 = null;
    PrintWriter printwriter3 = null;

    try {
      File file1 = new File(out1);
      printwriter1 = new PrintWriter(new FileWriter(file1));
      File file2 = new File(out2);
      printwriter2 = new PrintWriter(new FileWriter(file2));
      File file3 = new File(out3);
      printwriter3 = new PrintWriter(new FileWriter(file3));
    }
    catch (IOException e) {
      System.err.println("Log file open failed: " + e);
      System.exit( -1);
    }


    Hashtable table = new Hashtable();
    Vector vec = null;
    int count = 0;
    int index = 0;
    /////////////////////////////////////////
    Page page = null;
    MD5Hash md5hash = null;
    String url = "";
    Link link = null, in_link = null, in_in_link = null;
    Link[] link_array = null, in_link_array = null, in_in_link_array = null;
    /////////////////////////////////////////


    Enumeration en = reader.links();
    int loopcount = 0;
    while (en.hasMoreElements() && loopcount < start) {
      try {
        Link l = (Link) en.nextElement();
        loopcount++;
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    while (en.hasMoreElements() && count < boundary) {
      try {
        link = (Link) en.nextElement();
        page = reader.getPage(link.getURL().toString());
        md5hash = page.getMD5();
        link_array = reader.getLinks(md5hash);
        ////////////////////////////////////////////////////////////////////////
        int i, j;
        if (link_array.length != 0) {
          vec = new Vector();
          for (i = 0; i < link_array.length; i++) {
            in_link = link_array[i];
            page = reader.getPage(in_link.getURL().toString());
            md5hash = page.getMD5();
            in_link_array = reader.getLinks(md5hash);
            url = in_link.getURL().toString();
            if (in_link_array.length != 0 && url.startsWith("http")) {
              if (!vec.contains(in_link) && in_link != link) {
                vec.add(in_link);
                count++;
              }
            }
          }
          ////////////////////////////////////////////////////////////////////////
          url = link.getURL().toString();
          if (url.startsWith("http")) {
            if (index % 50 == 0) {
              System.out.println("[INFO] - Total " + index +
                                 " seed pages covered.");
            }
            table.put(link, vec);
            count++;
            index++;
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    System.out.println("[INFO] - 1)  Total " + count +
        " pages (parent and child) with one or more children is read into a vector.");

    int parent_count = 0;
    int child_count = 0;
    Enumeration enum = table.keys();
    while (enum.hasMoreElements()) {
      try {
        link = (Link) enum.nextElement();
        Vector vector = (Vector) table.get(link);
        page = reader.getPage(link.getURL().toString());
        md5hash = page.getMD5();
        Link[] url_links = reader.getLinks(md5hash);
        printwriter2.println(link.getURL().toString());
        parent_count++;
        for (Iterator iter = vector.iterator(); iter.hasNext(); ) {
          child_count++;
          Link l = (Link) iter.next();
          if (l != link) {
            printwriter2.println(l.getURL().toString());
            printwriter3.print(link.getURL().toString());
            printwriter3.print(" ");
            printwriter3.println(l.getURL().toString());
          }
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    int total_count = parent_count + child_count;
    System.out.println("[INFO] - 2)  Total of " + total_count + " pages with "
                       + parent_count + " parent pages and " + child_count
                       + " child pages written into vertex_raw.txt.");
    printwriter1.println(total_count);
    printwriter1.close();
    printwriter2.close();
    printwriter3.close();

    int s = 0;
    return s;

  }
*/

  public static void main(String[] args) {
    DataExtractor extractor = new DataExtractor();
  }

}
