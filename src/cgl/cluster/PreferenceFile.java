package cgl.cluster;
/*
 * <p>Title: PreferenceFile.java</p>
 * <p>Description: PreferenceFile Class
 * </p>
 * <p>authors: M Aktas & M Nacar
 * </p>
 */

import java.io.IOException;
import javax.servlet.ServletContext;

public class PreferenceFile {
  public static PreferenceFile get(ServletContext app) throws IOException {
    PreferenceFile bean = (PreferenceFile)app.getAttribute("nutchBean");
    if (bean == null) {
     // LOG.info("creating new bean");
      bean = new PreferenceFile();
      app.setAttribute("nutchBean", bean);
    }
    return bean;
  }

  public static void main(String[] args) {
    PreferenceFile preferenceFile1 = new PreferenceFile();
  }

}
