package cgl.library;
/*
 * <p>Title: RDFCreator.java</p>
 * <p>Description: RDF Content File Creator
 * <p>authors: M Aktas & M Nacar
 */

import java.io.*;
import java.util.*;

public class RDFCreator {
  public RDFCreator() {
  }

  public void createRDFSection0(PrintWriter out) {
    out.println("<RDF xmlns:r=" + "\"http://www.w3.org/TR/RDF/\"" + "\n     " +
                "xmlns:d=" + "\"http://purl.org/dc/elements/1.0/\"" + "\n     " +
                "xmlns=" + "\"http://directory.mozilla.org/rdf\"" + ">" +
                "\n\n" +
                "<Topic r:id=" + "\"Top\"" + ">" + "\n  " +
                "<tag catid=" + "\"1\"" + "/>" + "\n  " +
                "<d:Title>Top</d:Title>" + "\n" +
                "</Topic>" + "\n");
  }

  public void createRDFSection1(PrintWriter out, int total) {
    out.println("<Topic r:id=" + "\"" + "Top/URL" + "\"" + ">" + "\n  " +
                "<tag catid=" + "\"" + total + "\"" + "/>" + "\n  " +
                "<d:Title>URL</d:Title>");
  }

  public void createRDFSection2(PrintWriter out, String url) {
    out.println("  " + "<link r:resource=" + "\"" + url + "\"" + "/>");
  }

  public void createRDFSection3(PrintWriter out) {
    out.println("</Topic>" + "\n");
  }

  public void createRDFSection4(PrintWriter out, String url) {
    out.println("<ExternalPage about=" + "\"" + url + "\"" + ">" +
                "</ExternalPage>");
  }

  public void createRDFSection5(PrintWriter out) {
    out.println("\n" + "</RDF>");
  }

  static int atoi(String number) {
    int rtnVal = 0; // on invalid string, return a zero

    try {
      rtnVal = Integer.parseInt(number.trim());
    }
    catch (NumberFormatException e) {
      /* Let it pass! */}
    return rtnVal;
  } // end atoi

  public static void main(String[] args) {
    String path = "data/con_data/";
    String count_file_name = ("data/con_data/count_total_dir_nosink.txt");
    String file_name = ("data/con_data/vertex_total_dir_nosink.txt");

    RDFCreator RDFCreator1 = new RDFCreator();
    String rdf_filename = "content_total.rdf.u8";

    try {
      String rdf_fullpath = path.concat(rdf_filename);
      File f = new File(rdf_fullpath);
      PrintWriter rdf_output = new PrintWriter(new FileWriter(f));
      RDFCreator1.createRDFSection0(rdf_output);

      BufferedReader inp1 = null, inp2 = null;
      String lineIn1 = "", lineIn2 = "";
      Library library = new Library();

      inp1 = new BufferedReader(new FileReader(count_file_name));
      inp2 = new BufferedReader(new FileReader(file_name));
      while (!library.eof(inp1)) {
        lineIn1 = library.readWord(inp1);
        if (lineIn1 == null || lineIn1.length() == 0) {
          break;
        }
        int total = RDFCreator1.atoi(lineIn1);
        RDFCreator1.createRDFSection1(rdf_output, total);
      }

      while (!library.eof(inp2)) {
        lineIn2 = library.readWord(inp2);
        if (lineIn2 == null || lineIn2.length() == 0) {
          break;
        }
        StringTokenizer token = new StringTokenizer(lineIn2, "&");
        int size = token.countTokens();

        if (size <= 1) {

          RDFCreator1.createRDFSection2(rdf_output, lineIn2);
        }
      }

      RDFCreator1.createRDFSection3(rdf_output);

      inp2 = new BufferedReader(new FileReader(file_name));
      while (!library.eof(inp2)) {
        lineIn2 = library.readWord(inp2);
        if (lineIn2 == null || lineIn2.length() == 0) {
          break;
        }
        StringTokenizer token = new StringTokenizer(lineIn2, "&");
        int size = token.countTokens();

        if (size <= 1) {

          RDFCreator1.createRDFSection4(rdf_output, lineIn2);
        }
      }

      RDFCreator1.createRDFSection5(rdf_output);

      rdf_output.close();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

  }

}