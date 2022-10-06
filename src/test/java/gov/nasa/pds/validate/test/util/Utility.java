package gov.nasa.pds.validate.test.util;

import java.io.File;

public class Utility {

  public static String getAbsolutePath(String path) throws Exception {
    String finalPath = "";
    File testFile = new File(path);
    if (!testFile.isAbsolute()) {
      finalPath = System.getProperty("user.dir") + "/" + path;
    } else {
      finalPath = path;
    }

    if (!(new File(finalPath)).exists()) {
      throw new Exception("Path does not exist: " + finalPath);
    }

    return finalPath;
  }

}
