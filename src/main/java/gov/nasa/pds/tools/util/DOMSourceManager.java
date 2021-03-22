package gov.nasa.pds.tools.util;

import java.net.URL;

import java.util.HashMap;

import javax.xml.transform.dom.DOMSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 /**
  * Class that does management of DOMSource objects for reuse.
  * 
  */
public class DOMSourceManager {
  private static final Logger LOG = LoggerFactory.getLogger(DOMSourceManager.class);
  private static final boolean REUSE_SOURCE_FLAG = true;  // Flag to allow turning on/off the reuse capability.
  private static HashMap<String,DOMSource> domMAP = new HashMap<String,DOMSource>();

  /**
   * Save a DOMSource to a map.
   * 
   * @param location The location where the DOMSource came from.
   * @param source   The DOMSource object.
   */

  public static void saveDOM(String location, DOMSource source) {
      // If REUSE_SOURCE_FLAG is set to true, save the DOMSource object to the domMAP.
      if (REUSE_SOURCE_FLAG) {
          // Save the DOMSource from a location for later reuse.
          domMAP.put(location,source);
      }
  }

  /**
   * Retrieve a saved DOMSource from a map.
   * 
   * @param location The location to retrieve the DOMSource from if available.
   */
  public static DOMSource reuseDOM(String location) {
      // Reuse the DOMSource from domMAP and clear out the list to keep list from growing.
      // Can return null if the target has not been parsed before.  Client should check for nullness.
      DOMSource source = null;

      if (REUSE_SOURCE_FLAG) {
          if (!domMAP.isEmpty() && domMAP.containsKey(location)) {
              source = domMAP.get(location);
              LOG.debug("DOMSourceManager:reuseDOM: REUSE_TRUE {}",location);
              if (source != null) {
                  LOG.debug("DOMSourceManager:reuseDOM: DOM_MAP_SIZE_PRECLEAR {}",domMAP.size());
                  domMAP.clear(); // Not sure if should delete one or clear out all.  Had not observed any adverse reactions from regression tests.
                  LOG.debug("DOMSourceManager:reuseDOM: DOM_MAP_SIZE_POSTCLEAR {}",domMAP.size());
              }
          } else {
              LOG.debug("DOMSourceManager:reuseDOM: REUSE_FALSE {}",location);
          }
      } else {
          LOG.debug("DOMSourceManager:reuseDOM: REUSE_FALSE {}",location);
      }
      return(source);
  }
}
