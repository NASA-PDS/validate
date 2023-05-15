package gov.nasa.pds.tools.validate.content.table;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import gov.nasa.pds.tools.inventory.reader.InventoryEntry;
import gov.nasa.pds.tools.inventory.reader.InventoryReaderException;
import gov.nasa.pds.tools.inventory.reader.InventoryTableReader;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationTarget;
import gov.nasa.pds.tools.validate.rule.GenericProblems;

public class InventoryTableValidator {
  public static void uniqueLidvids (ProblemListener listener, URL aggregate) {
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
      HashSet<String> uniqueIDs = new HashSet<String>(ids);
      if (ids.size() != uniqueIDs.size()) {
        int dups = ids.size() - uniqueIDs.size();
        for (String uid : uniqueIDs) {
          int copies = Collections.frequency (ids, uid);
          if (1 < copies) {
            dups = dups - copies + 1;
            listener.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR, ProblemType.INVENTORY_DUPLICATE_LIDVID,
                    "Inventory contains " + Integer.toString(copies) + "' instances of LIDVID " + uid),
                aggregate));
          }
          if (dups < 1) break;  // shortcut the for loop
        }
      }
    } catch (InventoryReaderException e) {
      listener.addProblem(new ValidationProblem(GenericProblems.UNCAUGHT_EXCEPTION,
          new ValidationTarget(aggregate),-1, -1, e.getMessage()));
    }
  }
}
