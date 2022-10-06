package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.net.URL;

public class FileFinder {
  // Class provide a utility to find a given file (name, and extension) with a
  // starting directory.
  // This works on file: protocol only.
  public static URL findMyFile(String startDirectory, String findFilename, String extension) {
    // File dir = new File("./target/classes");
    File dir = new File(startDirectory);

    File[] matches = dir.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        // System.out.println("ValidationResourceManager:accept:dir,name " + dir + " " +
        // name);
        return name.contains(findFilename) && name.endsWith(extension);
      }
    });
    if (matches.length <= 0) {
      return null;
    }
    String nameFound = ((File) (Array.get(matches, 0))).getPath();
    String relativePath = null;
    // System.err.println("FileFinder: nameFound " + nameFound);
    if (nameFound != null) {
      relativePath = startDirectory + File.separator + nameFound;
    }
    URL foundURL = null;
    try {
      File f = new File(nameFound);
      // Get the absolute path of file f
      String absolutePath = f.getAbsolutePath();
      foundURL = new URL("file:" + absolutePath); // Build a "file:" URL to return.
    } catch (MalformedURLException ex) {
      System.err.println("FileFinder: found exception ex " + ex);
    }
    // System.err.println("FileFinder: foundURL " + foundURL.toString());
    return foundURL;
  }
}
