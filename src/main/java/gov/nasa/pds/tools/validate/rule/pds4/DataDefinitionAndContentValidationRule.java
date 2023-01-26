package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.label.Label;
import gov.nasa.pds.label.object.ArrayObject;
import gov.nasa.pds.label.object.DataObject;
import gov.nasa.pds.label.object.TableObject;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.objectAccess.ParseException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
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

      int tableCounter = 0;
      int arrayCounter = 0;
      int headerCounter = 0;
      long minimumExpectedOffset = -1;

      DataObjectValidator validator = null;

      URL previousDataFile = null;
      for (DataObject obj : label.getObjects()) {
        objectIdentifier = getObjectIdentifier(obj);
        LOG.debug("Checking DataObject #{} '{}'", objectCounter, obj.getName());

        // reset counters and everything if we are onto a new datafile
        // this indicates you are in a new file area,
        // e.g. File_Area_Observational + File_Area_Observational_Supplemental

        if (!obj.getDataFile().equals(previousDataFile)) {
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

