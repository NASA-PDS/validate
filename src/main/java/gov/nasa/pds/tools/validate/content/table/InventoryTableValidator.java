package gov.nasa.pds.tools.validate.content.table;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.xml.xpath.XPathExpressionException;
import gov.nasa.pds.tools.inventory.reader.InventoryEntry;
import gov.nasa.pds.tools.inventory.reader.InventoryReaderException;
import gov.nasa.pds.tools.inventory.reader.InventoryTableReader;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import net.sf.saxon.trans.XPathException;

public class InventoryTableValidator {
  public static void uniqueBundleRefs (ProblemListener listener, URL aggregate) {
    try {
      ArrayList<String> ids = new ArrayList<String>();
      XMLExtractor xml = new XMLExtractor(aggregate);
      ids.addAll(xml.getValuesFromDoc ("/Product_Bundle/Bundle_Member_Entry/lid_reference"));
      ids.addAll(xml.getValuesFromDoc ("/Product_Bundle/Bundle_Member_Entry/lidvid_reference"));
      InventoryTableValidator.checkAllUnique(ids, listener, aggregate);
    } catch (XPathException | XPathExpressionException e) {
      listener.addProblem(new ValidationProblem(GenericProblems.UNCAUGHT_EXCEPTION,
          ValidationTarget.build(aggregate),-1, -1, e.getMessage()));
    }
  }
  public static void uniqueCollectionRefs (ProblemListener listener, URL aggregate) {
    try {
      ArrayList<String> ids = new ArrayList<String>();
      InventoryTableReader reader = new InventoryTableReader(aggregate);
      InventoryEntry entry = reader.getNext();
      while (entry != null) {
        if (!entry.isEmpty()) {
          ids.add (entry.getIdentifier());
        }
        entry = reader.getNext();
      }
      InventoryTableValidator.checkAllUnique(ids, listener, aggregate);
      if (ids.size() != reader.getNumRecords()) {
        listener.addProblem(new TableContentProblem(ExceptionType.ERROR, ProblemType.RECORDS_MISMATCH,
            "Number of records read is not equal "
                + "to the defined number of records in the label (expected "
                + reader.getNumRecords() + ", got " + ids.size()
                + ").",
               reader.getDataFile(), aggregate, "-1", -1, -1));
      }
    } catch (InventoryReaderException e) {
      listener.addProblem(new ValidationProblem(GenericProblems.UNCAUGHT_EXCEPTION,
          ValidationTarget.build(aggregate),-1, -1, e.getMessage()));
    }
  }
  private static void checkAllUnique(List<String> ids, ProblemListener listener, URL aggregate) {
    HashSet<String> dups = new HashSet<String>();
    HashMap<String,Integer> counts = new HashMap<String,Integer>();
    for (String id : ids) {
      Integer count = counts.getOrDefault(id, Integer.valueOf(0));
      counts.put(id, ++count);
      if (count > 1) {
        dups.add(id);
      }
    }
    for (String id : dups) {
      listener.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.INVENTORY_DUPLICATE_LIDVID,
              "Inventory contains " + Integer.toString(counts.get(id)) + " instances of LIDVID " + id),
          aggregate));
    }
  }
}
