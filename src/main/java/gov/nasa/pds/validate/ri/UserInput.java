package gov.nasa.pds.validate.ri;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.xml.transform.sax.SAXSource;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.XMLExtractor;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

class UserInput {
  final private Logger log = LogManager.getLogger(UserInput.class);
  public String labels_lidvid = "";
  public static List<String> toLidvids (List<String> cliList){
    return new UserInput().process (cliList);
  }
  private List<String> expandManifest (String cliArg) {
    File file = new File(cliArg);
    List<String> lidvids = new ArrayList<String>();
    if (file.exists()) {
      try (Stream<String> lines = Files.lines(Paths.get(cliArg))) {
        lidvids.addAll (this.process(lines.collect(Collectors.toList())));
      } catch (Exception e) {
        log.warn ("The argument '" + cliArg + "' does not look like a LIDVID, Label, or manifest file. Ignoring it.");
      }
    }
    return lidvids;
  }
  private boolean isLabel (String cliArg) {
    if (cliArg.endsWith(".xml") || cliArg.endsWith (".lblx")) {
      File file = new File(cliArg);
      if (file.exists()) {
        try {
          SAXSource saxSource = new SAXSource(new InputSource(new FileReader(file)));
          TreeInfo docInfo = LabelParser.parse(saxSource, true); // Parses a label.
          List<TinyNodeImpl> xmlModels = new ArrayList<>();
          XMLExtractor extractor = new XMLExtractor(docInfo.getRootNode());
          xmlModels = extractor.getNodesFromDoc("//logical_identifier");
          this.labels_lidvid = xmlModels.get(0).getStringValue();
          return true;
        } catch (Exception e) {
          e.printStackTrace();
          return false;
        }
      }
    }
    return false;
  }
  private List<String> process (List<String> cliList){
    List<String> lidvids = new ArrayList<String>();
    for (String cliArg : cliList) {
      cliArg = StringUtils.substringBefore(cliArg, "#");
      if (cliArg.length() == 0) {
        // ignore empty lines
      } else if (cliArg.startsWith ("urn:")) {
        lidvids.add (cliArg);
      } else if (this.isLabel (cliArg)) {
        lidvids.add (this.labels_lidvid);
      } else {
        lidvids.addAll (this.expandManifest (cliArg));
      }
    }
    return lidvids;
  }
}
