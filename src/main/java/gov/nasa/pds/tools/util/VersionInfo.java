//	Copyright 2009-2010, by the California Institute of Technology.
//	ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//	Any commercial use must be negotiated with the Office of Technology 
//	Transfer at the California Institute of Technology.
//	
//	This software is subject to U. S. export control laws and regulations 
//	(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software 
//	is subject to U.S. export control laws and regulations, the recipient has 
//	the responsibility to obtain export licenses or other export authority as 
//	may be required before exporting such information to foreign countries or 
//	providing access to foreign nationals.
//	
//	$Id$
//

package gov.nasa.pds.tools.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class provides the means to retrieve underlying supported versions of
 * standards.
 * 
 * @author pramirez
 * 
 */
public class VersionInfo {

  public final static String XML_VERSION = "xml.version";

  public final static String LIBRARY_VERSION = "library.version";

  public final static String STANDARDS_VERSION = "standards-ref.version";

  public final static String PDS_VERSION = "pds.version";

  public final static String COPYRIGHT = "core.copyright";

  public final static String MODEL_VERSION = "model.version";

  public final static String SCHEMA_DIR = "schema";

  public final static String SCHEMA_DIR_PROP = "core.schema.dir";

  public final static String PDS_DEFAULT_NAMESPACE = "pds.default.namespace";
  
  public final static String SCHEMATRON_NAMESPACE = "core.schematron.namespace";

  private final static Properties props = new Properties();
  private final static File schemaDir;
  private final static Boolean internalMode;
  private final static String xmlParserVersion;

  static {
    try {
      xmlParserVersion = org.apache.xerces.impl.Version.getVersion();
      InputStream is = VersionInfo.class.getResourceAsStream("/core.properties");
      if (is == null) {
        throw new RuntimeException("ERROR: Unable to locate core.properties");
      }
      props.load(is);
      String schemaDirString = System.getProperty(SCHEMA_DIR_PROP);
      internalMode = (schemaDirString == null) ? true : false;
      if (!internalMode) {
        schemaDir = new File(schemaDirString);
        if (!schemaDir.exists()) {
          throw new RuntimeException("Schema directory does not exist: "
              + schemaDirString);
        }
        if (!schemaDir.isDirectory()) {
          throw new RuntimeException("Schema directory is not a directory: "
              + schemaDirString);
        }
      } else {
        schemaDir = null;
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String getXMLVersion() {
    return props.getProperty(XML_VERSION);
  }

  public static String getXMLParserVersion() {
    return xmlParserVersion;
  }

  public static String getLibraryVersion() {
    return props.getProperty(LIBRARY_VERSION);
  }

  public static String getStandardsVersion() {
    return props.getProperty(STANDARDS_VERSION);
  }

  public static String getPDSVersion() {
    return props.getProperty(PDS_VERSION);
  }

  public static String getCopyright() {
    return props.getProperty(COPYRIGHT);
  }

  public static String getDefaultModelVersion() {
    return props.getProperty(MODEL_VERSION);
  }

  public static String getSchematronNamespace() {
    return props.getProperty(SCHEMATRON_NAMESPACE);
  }
  
  public static List<String> getSchemasFromDirectory() {
      List<String> filenames = getDirectoryFileNames(schemaDir);
      List<String> results = new ArrayList<String>();
      for (String filename : filenames) {
        if (filename.endsWith(".xsd")) {
          results.add(filename);
        }
      }
      return results;
  }

  public static List<String> getSchemasFromJar(String modelVersion) {
    List<String> filenames = Arrays.asList(getSafeResourceListing(SCHEMA_DIR + "/"
        + modelVersion));
    List<String> results = new ArrayList<String>();
    for (String filename : filenames) {
      if (filename.endsWith(".xsd")) {
        results.add(filename);
      }
    }
    return results;
  }
  
  public static List<String> getSchematronsFromJar(String modelVersion) {
    List<String> filenames = Arrays.asList(getSafeResourceListing(SCHEMA_DIR + "/"
        + modelVersion));
    List<String> results = new ArrayList<String>();
    for (String filename : filenames) {
      if (filename.endsWith(".sch")) {
        results.add(filename);
      }
    }
    return results;
  }
  
  public static String getSchematronRefFromJar(String modelVersion, String schematronFile) {
    return "/" + SCHEMA_DIR + "/" + modelVersion + "/" + schematronFile;
  }
  
  public static String getSchemaRefFromJar(String modelVersion, String schemaFile) {
    return "/" + SCHEMA_DIR + "/" + modelVersion + "/" + schemaFile;
  }
  
  public static List<String> getSchemas() {
    if (internalMode) {
      return getSchemasFromJar(getDefaultModelVersion());
    } else {
      return getDirectoryFileNames(schemaDir);
    }
  }
  
  public static List<String> getDirectoryFileNames(File directory) {
    List<String> names = new ArrayList<String>();
    for (File file : getDirectoryListing(directory)) {
      // Ignore directories
      if (file.isFile()) {
        names.add(file.getName());
      }
    }
    return names;
  }

  public static File[] getDirectoryListing(File directory) {
    return directory.listFiles();
  }

  public static String getPDSDefaultNamespace(String modelVersion) {
    return props.getProperty(PDS_DEFAULT_NAMESPACE + "." + modelVersion, props
        .getProperty(PDS_DEFAULT_NAMESPACE));
  }

  public static List<String> getSupportedModels() {
    return Arrays.asList(getSafeResourceListing(SCHEMA_DIR));
  }

  private static String[] getSafeResourceListing(String path) {
    try {
      return getResourceListing(path);
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (UnsupportedOperationException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String[] getResourceListing(String path)
      throws URISyntaxException, IOException {
    URL dirURL = VersionInfo.class.getClassLoader().getResource(path);
    if (dirURL != null && dirURL.getProtocol().equals("file")) {
      /* A file path: easy enough */
      return new File(dirURL.toURI()).list();
    }

    if (dirURL == null) {
      /*
       * In case of a jar file, we can't actually find a directory. Have to
       * assume the same jar as clazz.
       */
      String me = VersionInfo.class.getName().replace(".", "/") + ".class";
      dirURL = VersionInfo.class.getClassLoader().getResource(me);
    }

    if (!path.endsWith("/")) {
      path += "/";
    }

    if (dirURL.getProtocol().equals("jar")) {
      /* A JAR path */
      String jarPath = dirURL.getPath().substring(5,
          dirURL.getPath().indexOf("!")); // strip out only the JAR file
      JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
      Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries in jar
      Set<String> result = new HashSet<String>(); // avoid duplicates in case it
      // is a subdirectory
      while (entries.hasMoreElements()) {
        String name = entries.nextElement().getName();
        if (name.startsWith(path)) { // filter according to the path
          String entry = name.substring(path.length());
          if (entry.length() != 0 && !"".equals(entry.trim())) {
            int checkSubdir = entry.indexOf("/");
            if (checkSubdir >= 0) {
              // if it is a subdirectory, we just return the directory name
              entry = entry.substring(0, checkSubdir);
            }
            result.add(entry);
          }
        }
      }
      return result.toArray(new String[result.size()]);
    }

    throw new UnsupportedOperationException("Cannot list files for URL "
        + dirURL);
  }

  public static Boolean isInternalMode() {
    return internalMode;
  }

}
