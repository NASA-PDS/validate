// Copyright 2006-2018, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology Transfer
// at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.IOException;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.label.object.ArrayObject;
import gov.nasa.pds.objectAccess.DataType.NumericDataType;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.EveryNCounter;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentValidator;
import gov.nasa.pds.tools.validate.rule.RuleContext;

public class ArrayValidator implements DataObjectValidator {
  private static final Logger LOG = LoggerFactory.getLogger(ArrayValidator.class);

  // #548: @jpl-jengelke wants a return to human-based indexing when reporting problems
  // #343: wants it to be the name if given
  private int arrayIndex = 1;
  private String arrayID = "-1";

  private ProblemListener listener = null;
  private RuleContext context = null;
  private ArrayObject array = null;

  public ArrayValidator(RuleContext context, ProblemListener listener) {
    this(context, listener, null);
  }

  public ArrayValidator(RuleContext context, ProblemListener listener, Object dataObject) {
    this.context = context;
    this.listener = listener;
    this.array = (ArrayObject) dataObject;
  }

  @Override
  public boolean validate() throws InvalidTableException, IOException {
    boolean valid = validateDataObjectDefinition();

    LOG.debug("getCheckData: {}", this.context.getCheckData());
    if (valid && this.context.getCheckData()) {
      valid = this.validateDataObjectContents();
    }

    return valid;
  }

  @Override
  public boolean validateDataObjectDefinition() {
    // Nothing to do here right now. Probably something we should be checking, but maybe later...
    return true;
  }

  @Override
  public boolean validateDataObjectContents() throws InvalidTableException, IOException {
	if (EveryNCounter.getInstance().getValue(EveryNCounter.Group.content) % this.context.getEveryN() != 0) return true;

    URL target = this.context.getTarget();
    String targetFileName = target.toString().substring(target.toString().lastIndexOf("/") + 1);
    long t0 = System.currentTimeMillis();

    boolean valid = true;

    LOG.debug("validateArray:targetFileName {}", targetFileName);

    try {
      // Check if the data type is something we support
      String dataType = "";
      try {
        dataType = this.array.getArray().getElementArray().getDataType();
        Enum.valueOf(NumericDataType.class, dataType);
      } catch (IllegalArgumentException a) {
        throw new IllegalArgumentException(dataType + " is not supported at this time.");
      }

      if (this.array.getName() != null && 0 < this.array.getName().strip().length()) {
        this.arrayID = this.array.getName() + " or index " + Integer.toString(this.arrayIndex);
      } else {
        this.arrayID = Integer.toString(this.arrayIndex);
      }
      // Verify that the elements match the object statistics defined
      // within their associated label, if they exist
      if (!this.array.getArray().getAxisArraies().isEmpty()) {
        ArrayContentValidator validator = new ArrayContentValidator(this.listener, target,
            this.array.getDataFile(), this.arrayID);
        validator.setSpotCheckData(this.context.getSpotCheckData());
        validator.validate(this.array);
      } else {
        addArrayProblem(ExceptionType.FATAL, ProblemType.INVALID_LABEL, "Missing Axis_Array area.",
            this.array.getDataFile(), this.arrayID);
        valid = false;
      }
    } catch (IllegalArgumentException ae) {
      addArrayProblem(ExceptionType.FATAL, ProblemType.ARRAY_DATA_FILE_READ_ERROR,
          "Error while reading array: " + ae.getMessage(), this.array.getDataFile(), this.arrayID);
      valid = false;
    } catch (UnsupportedOperationException ue) {
      addArrayProblem(ExceptionType.WARNING, ProblemType.ARRAY_INTERNAL_WARNING, ue.getMessage(),
          this.array.getDataFile(), this.arrayID);
      valid = false;
    } catch (OutOfMemoryError me) {
      addArrayProblem(ExceptionType.FATAL, ProblemType.OUT_OF_MEMORY,
          "Out of memory error occurred while processing array. " + "Please adjust the JVM Heap "
              + "Space settings and try again.",
          this.array.getDataFile(), this.arrayID);
      valid = false;
    }
    arrayIndex++;

    if (this.context.getLogLevel().isDebugApplicable()) {
      System.out.println("\nDEBUG  [" + ProblemType.TIMING_METRICS.getKey() + "]  "
          + System.currentTimeMillis() + " :: " + targetFileName + " :: validateArray() in "
          + (System.currentTimeMillis() - t0) + " ms\n");
    }

    return valid;
  }

  private void addArrayProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, String arrayID) {
    addArrayProblem(exceptionType, problemType, message, dataFile, arrayID, null);
  }

  private void addArrayProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, String arrayID, int[] location) {
    this.listener.addProblem(new ArrayContentProblem(exceptionType, problemType, message, dataFile,
        this.context.getTarget(), arrayID, location));
  }
}
