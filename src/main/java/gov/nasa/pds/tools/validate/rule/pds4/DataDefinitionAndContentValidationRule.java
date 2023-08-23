package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.label.Label;
import gov.nasa.pds.label.NameNotKnownException;
import gov.nasa.pds.label.ProductType;
import gov.nasa.pds.label.object.ArrayObject;
import gov.nasa.pds.label.object.DataObject;
import gov.nasa.pds.label.object.TableObject;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.objectAccess.ParseException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.EveryNCounter;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.table.InventoryTableValidator;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.ValidationTest;

public class DataDefinitionAndContentValidationRule extends AbstractValidationRule {
  private static final Logger LOG =
      LoggerFactory.getLogger(DataDefinitionAndContentValidationRule.class);

  public DataDefinitionAndContentValidationRule() {}

  @Override
  public boolean isApplicable(String location) {
    return true;
  }

  @ValidationTest
  public void validate() throws MalformedURLException, URISyntaxException {
    int objectCounter = 0;
    Label label = null;
    String objectIdentifier = "";
    
    try {
      URL target = getTarget();
      String targetFileName = target.toString().substring(target.toString().lastIndexOf("/") + 1);

      LOG.debug("START definition/content validation: {}", targetFileName);

      label = Label.open(target);
      if (label.getProductType() == ProductType.PRODUCT_BUNDLE) {
        InventoryTableValidator.uniqueBundleRefs(this.getListener(), this.getTarget());
      }
      if (label.getProductType() == ProductType.PRODUCT_COLLECTION) {
        InventoryTableValidator.uniqueCollectionRefs(this.getListener(), this.getTarget());
      }


      int tableCounter = 0;
      int arrayCounter = 0;
      int headerCounter = 0;
      long minimumExpectedOffset = -1;

      DataObjectValidator validator = null;

      URL previousDataFile = null;
      EveryNCounter.getInstance().increment();
      List<DataObject> offsetSorted = new ArrayList<DataObject>(label.getObjects());
      Collections.sort(offsetSorted, new DataObjectCompareViaOffset());
      for (DataObject obj : offsetSorted) {
        objectIdentifier = getObjectIdentifier(obj);
        LOG.debug("Checking DataObject #{} '{}'", objectCounter, obj.getName());

        // reset counters and everything if we are onto a new datafile
        // this indicates you are in a new file area,
        // e.g. File_Area_Observational + File_Area_Observational_Supplemental

        if (!obj.getDataFile().equals(previousDataFile)) {
          if (previousDataFile != null) {
            long filesize = new File(previousDataFile.getPath()).length();
            if (this.getContext().getCompleteDescriptions() && minimumExpectedOffset < filesize) {
              getListener().addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.WARNING, ProblemType.DATA_NOT_DESCRIBED,
                      "Data not described at the end of the file: " + (filesize - minimumExpectedOffset) + " bytes"),
                  getTarget(), objectCounter, -1));
            }
          }
          tableCounter = 0;
          arrayCounter = 0;
          headerCounter = 0;
          minimumExpectedOffset = -1;
          previousDataFile = obj.getDataFile();
        }

        objectCounter++;

        // Check offset
        checkOffset(target, minimumExpectedOffset, obj.getOffset(), objectCounter);
        LOG.debug("class: {}", obj.getClass());
        LOG.debug("label offset: {}, label size: {}", obj.getOffset(), obj.getSize());
        LOG.debug("minimumExpectedOffset: {}", minimumExpectedOffset);

        // Set expected offset for next loop through
        minimumExpectedOffset = obj.getOffset() + obj.getSize();

        // Check and validate per specific object type
        if (obj instanceof TableObject) {
          validator = new TableValidator(getContext(), getListener(), obj);
          validator.validate();
        } else if (obj instanceof ArrayObject) {
          validator = new ArrayValidator(getContext(), getListener(), obj);
          validator.validate();
        } else {
          // Must be a Header, so we just increment counter (even thought we don't really use it for
          // anything right now)
          headerCounter++;
        }
      }
      if (previousDataFile != null) {
        long filesize = new File(previousDataFile.getPath()).length();
        if (this.getContext().getCompleteDescriptions() && minimumExpectedOffset < filesize) {
          getListener().addProblem(new ValidationProblem(
              new ProblemDefinition(ExceptionType.WARNING, ProblemType.DATA_NOT_DESCRIBED,
                  "Data not described at the end of the file: " + (filesize - minimumExpectedOffset) + " bytes"),
              getTarget(), objectCounter, -1));
        }
      }
      LOG.debug("arrays validated: {}", arrayCounter);
      LOG.debug("tables validated: {}", tableCounter);
      LOG.debug("headers validated: {}", headerCounter);
      LOG.debug("END definition/content validation: {}", targetFileName);
    } catch (ClassCastException e) {
      // this was thrown by label.getDataObjects(), and means we won't perform validation for this
      // product
    } catch (InvalidTableException e) {
      String msg =
          "Data Object #" + objectCounter + " (" + objectIdentifier + "): " + e.getMessage();
      getListener().addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.TABLE_DEFINITION_PROBLEM, msg),
          getTarget(), objectCounter, -1));
    } catch (NameNotKnownException e) {
      getListener().addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.UNKNOWN_VALUE, e.getMessage()),
          getTarget(), objectCounter, -1));
    } catch (ParseException e) {
      // Ignore. Shouldn't happen
    } catch (FileNotFoundException e) {
      // File not found. This is caught and error is tracked elsewhere
    } catch (Exception e) {
      String msg =
          "Data Object #" + objectCounter + " (" + objectIdentifier + "): " + e.getMessage();
      getListener().addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.BAD_FIELD_READ, msg), getTarget(),
          objectCounter, -1));
      // e.printStackTrace();
    } finally {
      if (Objects.nonNull(label))
        label.close();
    }
  }

  private void checkOffset(URL target, long minimumExpected, long offset, int objectNumber) {
    if (offset < minimumExpected) {
      String msg = "Invalid object definition for object #" + objectNumber
          + ".  The previously defined object ends at byte " + minimumExpected
          + ". Offset defined in label: " + offset;

      getListener().addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.INVALID_OBJECT_DEFINITION, msg),
          target, objectNumber, -1));
    }
  }

  private String getObjectIdentifier(DataObject obj) {
    if (obj.getName() != null) {
      return obj.getName();
    } else if (obj.getLocalIdentifier() != null) {
      return obj.getLocalIdentifier();
    } else {
      return "Identifier Not Specified";
    }
  }
}

