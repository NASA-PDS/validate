package gov.nasa.pds.tools.validate.rule;

import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.ValidateProblemHandler;
import gov.nasa.pds.tools.validate.ValidationProblem;

import gov.nasa.pds.tools.validate.rule.pds4.FileAndDirectoryNamingChecker;

import gov.nasa.pds.tools.util.Utility;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gov.nasa.arc.pds.xml.generated.FileArea;
import gov.nasa.arc.pds.xml.generated.FileAreaAncillary;
import gov.nasa.arc.pds.xml.generated.FileAreaBrowse;
import gov.nasa.arc.pds.xml.generated.FileAreaInventory;
import gov.nasa.arc.pds.xml.generated.FileAreaObservational;
import gov.nasa.arc.pds.xml.generated.FileAreaSIPDeepArchive;
import gov.nasa.arc.pds.xml.generated.FileAreaTransferManifest;
import gov.nasa.arc.pds.xml.generated.InformationPackageComponent;

import gov.nasa.arc.pds.xml.generated.Product;
import gov.nasa.arc.pds.xml.generated.ProductAIP;
import gov.nasa.arc.pds.xml.generated.ProductAncillary;
import gov.nasa.arc.pds.xml.generated.ProductBrowse;
import gov.nasa.arc.pds.xml.generated.ProductCollection;
import gov.nasa.arc.pds.xml.generated.ProductObservational;
import gov.nasa.arc.pds.xml.generated.ProductSIPDeepArchive;

import gov.nasa.pds.objectAccess.ObjectAccess;
import gov.nasa.pds.objectAccess.ObjectProvider;
import gov.nasa.pds.objectAccess.ParseException;

public class FileAreaExtractor {
    // Class to report if file referenced in file_name tag in a label contains prohibited file names. 
    private static final Logger LOG = LoggerFactory.getLogger(FileAreaExtractor.class);

	private RuleContext context = new RuleContext();
	private ProblemListener listener = null;

    protected void flagBadFilename(URL target) {
        // It is possible that the file name violate naming rules.  Check them here.  If there are problems,
        // add each problem to the listener in this class.
        //
        // Example: 3juno_lwr01896_ines%20fits%20headers.pdfa.xml contains spaces between the characters.
        //
        // Note that the class FileAndDirectoryNamingChecker extends FileAndDirectoryNamingRule but re-implement
        // with new function checkFileAndDirectoryNamingWithChecker() to not call reportError() but to return a list of ValidationProblem.

        FileAndDirectoryNamingChecker FileAndDirectoryNamingChecker = new FileAndDirectoryNamingChecker();
        List<Target> list = new ArrayList<Target>();
        list.add(new Target(target,false)); // Make a list of just one name.
        List<ValidationProblem> validationProblems = FileAndDirectoryNamingChecker.checkFileAndDirectoryNamingWithChecker(list);
        LOG.debug("flagBadFilename:target {}",target);
        LOG.debug("flagBadFilename:validationProblems.size() {}",validationProblems.size());
        if (validationProblems.size() > 0) {
            for (ValidationProblem problem : validationProblems) {
                problem.setSource(target.toString()); // Because all problems with the filename are for target, set the source with target.
                getListener().addProblem(problem);    // Add the problem of the file name to this listener.
                LOG.debug("flagBadFilename:addProblem(problem) {},{}",problem,target.toString());
                LOG.error("flagBadFilename:addProblem(problem) {},{}",problem,target.toString());
            }
        }
    }

    public ProblemListener findAndFlagBadFilenames(URL target, ProblemListener listener) throws MalformedURLException, URISyntaxException {
	    this.listener = listener;
        ObjectProvider objectAccess = null;
        LOG.debug("findAndFlagBadFilenames:target {}",target);
    	context.setTarget(target);
        objectAccess = new ObjectAccess(target);
        List<FileArea> tableFileAreas = new ArrayList<FileArea>();
        try {
          tableFileAreas = getTableFileAreas(objectAccess);
        } catch (ParseException pe) {
           LOG.error("Error occurred while trying to get the table file areas: " + pe.getCause().getMessage());
       }


        Map<String, Integer> numTables = scanTables(tableFileAreas, objectAccess);
        Map<URL, Integer> tableIndexes = new LinkedHashMap<URL, Integer>();
        LOG.debug("findAndFlagBadFilenames:numTables {}",numTables.size());
        LOG.debug("findAndFlagBadFilenames:tableIndexes {}",tableIndexes.size());
        //int fileAreaObserveIndex = 0;
        for (FileArea fileArea : tableFileAreas) {
          //int tableIndex = -1;
          String fileName = getDataFile(fileArea);
          String fullName = Utility.getParent(target).toString() + File.separator + fileName;
          LOG.debug("findAndFlagBadFilenames:fileName,fullName {},{}",fileName,fullName);
          flagBadFilename(new URL(fullName));
    //System.out.println("findAndFlagBadFilenames: early#exit#0001");
    //System.exit(0);
        }
        return(this.listener);
    }

  private Map<String, Integer> scanTables(List<FileArea> fileAreas,
      ObjectProvider objectAccess) {
    Map<String, Integer> results = new LinkedHashMap<String, Integer>();
    for (FileArea fileArea : fileAreas) {
      String dataFile = getDataFile(fileArea);
      List<Object> tables = objectAccess.getTableObjects(fileArea);
      Integer numTables = results.get(dataFile);
      if (numTables == null) {
        results.put(dataFile, new Integer(tables.size()));
      } else {
        numTables = results.get(dataFile);
        numTables = new Integer(numTables.intValue() + tables.size());
        results.put(dataFile, numTables);
      }
    }
    return results;
  }

  private List<FileArea> getTableFileAreas(ObjectProvider objectAccess)
      throws ParseException {
    List<FileArea> results = new ArrayList<FileArea>();
    Product product = objectAccess.getProduct(getTarget(), Product.class);
    if (product instanceof ProductObservational) {
      results.addAll(((ProductObservational) product)
          .getFileAreaObservationals());
    } else if (product instanceof ProductSIPDeepArchive) {
      ProductSIPDeepArchive psda = (ProductSIPDeepArchive) product;
      if (psda.getInformationPackageComponentDeepArchive() != null &&
          psda.getInformationPackageComponentDeepArchive()
          .getFileAreaSIPDeepArchive() != null) {
        results.add(psda.getInformationPackageComponentDeepArchive()
            .getFileAreaSIPDeepArchive());
      }
    } else if (product instanceof ProductAIP) {
      ProductAIP pa = (ProductAIP) product;
      for ( InformationPackageComponent ipc :
        pa.getInformationPackageComponents()) {
        if (ipc.getFileAreaTransferManifest() != null) {
          results.add(ipc.getFileAreaTransferManifest());
        }
      }
    } else if (product instanceof ProductCollection) {
      ProductCollection pc = (ProductCollection) product;
      if (pc.getFileAreaInventory() != null &&
        pc.getFileAreaInventory().getInventory() != null) {
        results.add(pc.getFileAreaInventory());
      }
    } else if (product instanceof ProductAncillary) {
        ProductAncillary pa = (ProductAncillary) product;
        if (pa.getFileAreaAncillaries() != null &&
            pa.getFileAreaAncillaries().size()>0)
          results.addAll(pa.getFileAreaAncillaries());
    } else if (product instanceof ProductBrowse) {
      results.addAll(((ProductBrowse) product).getFileAreaBrowses());
    }
    return results;
  }

  private String getDataFile(FileArea fileArea) {
    String result = "";
    if (fileArea instanceof FileAreaObservational) {
      result = ((FileAreaObservational) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaSIPDeepArchive) {
      result = ((FileAreaSIPDeepArchive) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaTransferManifest) {
      result = ((FileAreaTransferManifest) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaInventory) {
      result = ((FileAreaInventory) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaBrowse) {
      result = ((FileAreaBrowse) fileArea).getFile().getFileName();
    } else if (fileArea instanceof FileAreaAncillary) {
      result = ((FileAreaAncillary) fileArea).getFile().getFileName();
    }
    return result;
  }

	protected URL getTarget() {
		return context.getTarget();
	}

  protected ProblemListener getListener() {
    return listener;
  }

}
