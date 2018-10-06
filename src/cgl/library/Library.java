package cgl.library;
/*
 * <p>Title: Library.java</p>
 * <p>Description: This is a library to handle reading and writing to and/or from a file.
 */

public class Library {

  public Library() {

  }

  public static boolean eof() {
    int test = -1;

    try {
      System.in.mark(1);
      test = System.in.read();
      if (test >= 0) {
        System.in.reset();
      }
    }
    catch (java.io.IOException e) {
     e.printStackTrace();
    }
    return (test < 0);
  }

  public static String readLine() {
    int readItem;
    char charIn = '\0';
    String rtnVal = "";
    try {
      System.in.mark(1);
      charIn = (char) (readItem = System.in.read());
      if (charIn == '\r' || charIn == '\n') {
        while (charIn != '\n' && readItem != -1) {
          charIn = (char) (readItem = System.in.read());
        }
        return "";
      }
      System.in.reset();
    }
    catch (java.io.IOException e) {
      System.err.println("Input failed: " + e);
      System.exit( -1);
    }
    return rtnVal;
  }

  public static String readWord() {
    int readItem;
    char charIn;

    StringBuffer inWork = new StringBuffer(255);

    inWork.setLength(0);

    try {
      do {
        charIn = (char) (readItem = System.in.read());
      }
      while (Character.isWhitespace(charIn) && readItem > 0);

      if (readItem <= 0) {
        return "";
      }

      if (charIn == '"') {
        while (true) {
          charIn = (char) (readItem = System.in.read());
          if (charIn == '"' || readItem < 0) {
            break;
          }
          inWork.append(charIn);
        }
      }
      else {
        inWork.append(charIn);
        while (true) {
          System.in.mark(1);
          charIn = (char) (readItem = System.in.read());
          if (Character.isWhitespace(charIn) || readItem < 0) {
            break;
          }
          inWork.append(charIn);
        }
        if (readItem > 0) { // put the white space back
          System.in.reset();
        }
      }
    }
    catch (java.io.IOException e) {
      System.err.println("Console input failed: " + e);
      System.exit( -1);
    }
    return inWork.toString();
  }

  public static int readInt() {
    int readItem;
    char charIn;

    StringBuffer inWork = new StringBuffer(255);

    inWork.setLength(0);

    try {
      do {
        System.in.mark(1);
        charIn = (char) (readItem = System.in.read());
      }
      while (Character.isWhitespace(charIn) && readItem > 0);

      if (readItem <= 0) {
        throw new java.io.IOException("End-of-file found");
      }

      if (Character.isDigit(charIn) || charIn == '-' || charIn == '+') {
        inWork.append(charIn);
        while (true) {
          System.in.mark(1);
          charIn = (char) (readItem = System.in.read());
          if (!Character.isDigit(charIn) || readItem < 0) {
            break;
          }
          inWork.append(charIn);
        }
        if (readItem > 0) {
          System.in.reset();
        }
        return Integer.parseInt(inWork.toString());
      }
      else {
        System.in.reset();
        System.err.println("Illegal character (" + charIn
                           + ") found.  Returning zero.");
      }
    }
    catch (java.io.IOException e) {
      System.err.println("Console input failed: " + e);
      System.exit( -1);
    }
    return 0;
  }

  public static void discardLine() {
    int readItem;
    char charIn;

    try {
      do {
        charIn = (char) (readItem = System.in.read());
      }
      while (charIn != '\n' && readItem != -1);
    }
    catch (java.io.IOException e) {
      System.err.println("Input failed: " + e);
      System.exit( -1);
    }
  }

  public static boolean eof(java.io.BufferedReader inp) {
    int test = -1;

    try {
      inp.mark(1);
      test = inp.read();
      if (test >= 0) {
        inp.reset();
      }
    }
    catch (java.io.IOException e) {
    e.printStackTrace();
    }
    return (test < 0);
  }

  public static String readLine(java.io.BufferedReader inp) {
    String rtnVal = "";

    try {
      rtnVal = inp.readLine();
    }
    catch (java.io.IOException e) {
      System.err.println("File input failed: " + e);
      System.exit( -1);
    }
    return rtnVal;
  }

  public static String readWord(java.io.BufferedReader inp) {
    int readItem;
    char charIn;

    StringBuffer inWork = new StringBuffer(255);

    inWork.setLength(0);

    try {
      do {
        charIn = (char) (readItem = inp.read());
      }
      while (Character.isWhitespace(charIn) && readItem > 0);

      if (readItem <= 0) {
        return "";
      }

      if (charIn == '"') {
        while (true) {
          charIn = (char) (readItem = inp.read());
          if (charIn == '"' || readItem < 0) {
            break;
          }
          inWork.append(charIn);
        }
      }
      else {
        inWork.append(charIn);
        while (true) {
          inp.mark(1);
          charIn = (char) (readItem = inp.read());
          if (Character.isWhitespace(charIn) || readItem < 0) {
            break;
          }
          inWork.append(charIn);
        }
        if (readItem > 0) { // put the white space back
          inp.reset();
        }
      }
    }
    catch (java.io.IOException e) {
      System.err.println("File input failed: " + e);
      System.exit( -1);
    }
    return inWork.toString();
  }

  public static int readInt(java.io.BufferedReader inp) {
    int readItem;
    char charIn;

    StringBuffer inWork = new StringBuffer(255);

    inWork.setLength(0);

    try {
      do {
        inp.mark(1);
        charIn = (char) (readItem = inp.read());
      }
      while (Character.isWhitespace(charIn) && readItem > 0);

      if (readItem <= 0) {
        throw new java.io.IOException("End-of-file found");
      }

      if (Character.isDigit(charIn) || charIn == '-' || charIn == '+') {
        inWork.append(charIn);
        while (true) {
          inp.mark(1);
          charIn = (char) (readItem = inp.read());
          if (!Character.isDigit(charIn) || readItem < 0) {
            break;
          }
          inWork.append(charIn);
        }
        if (readItem > 0) {
          inp.reset();
        }
        return Integer.parseInt(inWork.toString());
      }
      else {
        inp.reset();
        System.err.println("Illegal character (" + charIn
                           + ") found.  Returning zero.");
      }
    }
    catch (java.io.IOException e) {
      System.err.println("File input failed: " + e);
      System.exit( -1);
    }
    return 0;
  }
}
