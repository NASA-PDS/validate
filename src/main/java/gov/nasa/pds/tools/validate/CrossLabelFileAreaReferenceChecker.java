package gov.nasa.pds.tools.validate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import gov.nasa.pds.tools.util.LabelCacheEntry;
import gov.nasa.pds.tools.util.LabelUtil;
import gov.nasa.pds.tools.util.ReferentialIntegrityUtil;

public class CrossLabelFileAreaReferenceChecker {
  // Written from label validation threads, so both maps must be safe for concurrent use.
  private static final ConcurrentHashMap<String,Boolean> isObservational = new ConcurrentHashMap<>();
  private static final ConcurrentHashMap<String,List<String>> knownRefs = new ConcurrentHashMap<>();
  private static String resolve (String name, ValidationTarget target) throws URISyntaxException {
    if (!name.startsWith ("/")) {
      name = Path.of(target.getUrl().toURI()).getParent().resolve(name).toString();
    }
    return name;
  }
  /**
   * @param name - the file being referenced by the file area
   * @param target - the label being validated
   * @return true if name and target are unique and only known references and false otherwise
   * @throws URISyntaxException
   */
  public static boolean add(String name, ValidationTarget target, boolean isObs)
      throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
    boolean success = false;
    String full_name = resolve(name, target);
    // Once any label marks the file as observational it stays observational.
    isObservational.merge(full_name, isObs, Boolean::logicalOr);
    List<String> logicalIdentifiers;
    LabelCacheEntry cached = ReferentialIntegrityUtil.getCachedLabelIdentifiers(target.getUrl());
    if (cached != null) {
      logicalIdentifiers = cached.getLogicalIdentifiers();
    } else {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      Document xml = dbf.newDocumentBuilder().parse(target.getUrl().openStream());
      DOMSource domSource = new DOMSource(xml);
      logicalIdentifiers = LabelUtil.getLogicalIdentifiers(domSource, target.getUrl());
    }
    for (String lid : logicalIdentifiers) {
      if (lid.contains("::")) {
        lid = lid.substring (0, lid.indexOf("::"));
      }
      // putIfAbsent keeps "the first label to claim the file wins" atomic across threads.
      if (knownRefs.putIfAbsent(full_name, Arrays.asList(lid, target.getUrl().getPath())) == null) {
        success = true;
      } else {
        success = !isObservational.get(full_name);
      }
    }
    return success;
  }
  public static String getOtherId (String name, ValidationTarget target) throws URISyntaxException {
    return knownRefs.get(resolve(name, target)).get(0);
  }
  public static String getOtherFilename (String name, ValidationTarget target) throws URISyntaxException {
    return knownRefs.get(resolve(name, target)).get(1);
  }
  public static void reset() {
    knownRefs.clear();
    isObservational.clear();
  }
}
