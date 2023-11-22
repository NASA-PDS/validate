package gov.nasa.pds.tools.validate;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import gov.nasa.pds.tools.util.LabelUtil;

public class CrossLabelFileAreaReferenceChecker {
  final private static HashMap<String,List<String>> knownRefs = new HashMap<String,List<String>>();
  private static String resolve (String name, ValidationTarget target) {
    if (!name.startsWith ("/")) {
      name = Path.of(target.getUrl().getPath()).getParent().resolve(name).toString();
    }
    return name;
  }
  /**
   * @param name - the file being referenced by the file area
   * @param target - the label being validated
   * @return true if name and target are unique and only known references and false otherwise
   * All of these exceptions should not happen because they are getting the LID from the target.
   * Would not have made it this far if that could not have happened already, So, just pass them
   * back to the called and let them handle it with their own generic exception handler/message.
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public static boolean add (String name, ValidationTarget target) throws IOException, ParserConfigurationException, SAXException {
    boolean success = false;
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    Document xml = dbf.newDocumentBuilder().parse(target.getUrl().openStream());
    DOMSource domSource = new DOMSource(xml);    
    for (String lid : LabelUtil.getLogicalIdentifiers (domSource, target.getUrl())) {
      if (lid.contains("::")) {
        lid = lid.substring (0, lid.indexOf("::"));
      }
      if (!knownRefs.keySet().contains (resolve(name, target))) {
        knownRefs.put(resolve(name, target), (List<String>)Arrays.asList(lid, target.getUrl().getPath()));
        success = true;
      } else {
        success = false;
      }
    }
    return success;
  }
  public static String getOtherId (String name, ValidationTarget target) {
    return knownRefs.get(resolve(name, target)).get(0);
  }
  public static String getOtherFilename (String name, ValidationTarget target) {
    return knownRefs.get(resolve(name, target)).get(1);
  }
  public static void reset() {
    knownRefs.clear();
  }
}
