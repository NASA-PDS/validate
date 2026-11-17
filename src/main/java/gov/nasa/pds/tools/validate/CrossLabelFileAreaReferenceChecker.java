package gov.nasa.pds.tools.validate;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import gov.nasa.pds.tools.util.LabelCacheEntry;
import gov.nasa.pds.tools.util.LabelUtil;

/**
 * Finds file areas in different labels that point at the same file.
 *
 * <p>
 * One instance lives for one validation run (see
 * {@link gov.nasa.pds.tools.util.ReferentialIntegrityUtil#getCrossLabelFileAreaReferenceChecker()}).
 * Labels may be validated on several threads, so {@link #add} is safe to call concurrently.
 */
public class CrossLabelFileAreaReferenceChecker {
  private final ConcurrentHashMap<String, Boolean> isObservational = new ConcurrentHashMap<>();
  private final ConcurrentHashMap<String, List<String>> knownRefs = new ConcurrentHashMap<>();
  private final Function<URL, LabelCacheEntry> labelCache;

  /**
   * @param labelCache looks up identifiers already extracted from a label, or returns null
   */
  public CrossLabelFileAreaReferenceChecker(Function<URL, LabelCacheEntry> labelCache) {
    this.labelCache = labelCache;
  }
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
  public boolean add(String name, ValidationTarget target, boolean isObs)
      throws IOException, ParserConfigurationException, SAXException, URISyntaxException {
    boolean success = false;
    String full_name = resolve(name, target);
    // Once any label marks the file as observational it stays observational.
    isObservational.merge(full_name, isObs, Boolean::logicalOr);
    List<String> logicalIdentifiers;
    LabelCacheEntry cached = labelCache.apply(target.getUrl());
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
      // putIfAbsent makes "first label to claim the file wins" atomic across threads.
      if (knownRefs.putIfAbsent(full_name,
          Arrays.asList(lid, target.getUrl().getPath())) == null) {
        success = true;
      } else {
        success = !isObservational.get(full_name);
      }
    }
    return success;
  }
  public String getOtherId (String name, ValidationTarget target) throws URISyntaxException {
    return knownRefs.get(resolve(name, target)).get(0);
  }
  public String getOtherFilename (String name, ValidationTarget target) throws URISyntaxException {
    return knownRefs.get(resolve(name, target)).get(1);
  }
  public void reset() {
    knownRefs.clear();
    isObservational.clear();
  }
}
