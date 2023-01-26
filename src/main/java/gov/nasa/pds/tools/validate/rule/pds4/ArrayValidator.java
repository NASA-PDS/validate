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
import java.math.BigInteger;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.arc.pds.xml.generated.Array;
import gov.nasa.arc.pds.xml.generated.Offset;
import gov.nasa.pds.label.object.ArrayObject;
import gov.nasa.pds.objectAccess.DataType.NumericDataType;
import gov.nasa.pds.objectAccess.InvalidTableException;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.util.FileSizesUtil;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemListener;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentProblem;
import gov.nasa.pds.tools.validate.content.array.ArrayContentValidator;
import gov.nasa.pds.tools.validate.rule.RuleContext;

public class ArrayValidator implements DataObjectValidator {
  private static final Logger LOG = LoggerFactory.getLogger(ArrayValidator.class);

  private static int every_n_counter = 0;

  // #548: @jpl-jengelke wants a return to human-based indexing when reporting problems
  private int arrayIndex = 1;

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
	if (ArrayValidator.every_n_counter++ % this.context.getEveryN() != 0) return true;

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

      // Verify that the elements match the object statistics defined
      // within their associated label, if they exist
      if (!this.array.getArray().getAxisArraies().isEmpty()) {
        ArrayContentValidator validator = new ArrayContentValidator(this.listener, target,
            this.array.getDataFile(), this.arrayIndex);
        validator.setSpotCheckData(this.context.getSpotCheckData());
        validator.validate(this.array);
      } else {
        addArrayProblem(ExceptionType.FATAL, ProblemType.INVALID_LABEL, "Missing Axis_Array area.",
            this.array.getDataFile(), arrayIndex);
        valid = false;
      }
    } catch (IllegalArgumentException ae) {
      addArrayProblem(ExceptionType.FATAL, ProblemType.ARRAY_DATA_FILE_READ_ERROR,
          "Error while reading array: " + ae.getMessage(), this.array.getDataFile(), arrayIndex);
      valid = false;
    } catch (UnsupportedOperationException ue) {
      addArrayProblem(ExceptionType.WARNING, ProblemType.ARRAY_INTERNAL_WARNING, ue.getMessage(),
          this.array.getDataFile(), arrayIndex);
      valid = false;
    } catch (OutOfMemoryError me) {
      addArrayProblem(ExceptionType.FATAL, ProblemType.OUT_OF_MEMORY,
          "Out of memory error occurred while processing array. " + "Please adjust the JVM Heap "
              + "Space settings and try again.",
          this.array.getDataFile(), arrayIndex);
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

  private boolean validateMinimumFileSize(URL target, Array array, URL dataFile, String fileName,
      String dataType) throws IOException {

    BigInteger minimalFileSize = BigInteger.valueOf(0);
    long arrayFinalSize = 1;
    long numArrayElements = 1;

    Offset offset = array.getOffset();

    // Get all the dimensions and calculate the size of the array by getting the
    // product of all the dimensions.
    int[] dimensions = new int[array.getAxisArraies().size()];
    for (int i = 0; i < dimensions.length; i++) {
      dimensions[i] = array.getAxisArraies().get(i).getElements().intValueExact();

      numArrayElements = numArrayElements * dimensions[i];

      LOG.debug("validateMinimumFileSize:fileName,i,dimensions[i],numArrayElements {},{},{},{}",
          fileName, i, dimensions[i], numArrayElements);
    }

    LOG.debug(
        "validateMinimumFileSize:fileName,offset.getUnit().value(),offset.getValue(),numArrayElements {},{},{},{}",
        fileName, offset.getUnit().value(), offset.getValue(), numArrayElements);
    LOG.debug("validateMinimumFileSize:dataType {},{}", dataType,
        Enum.valueOf(NumericDataType.class, dataType).getBits());

    // Using the dimensions, the dataType, the offset, to calculate the minimum size
    // of the file to be able
    // to accommodate reading in a 2D or 3D array.
    // the array size is the product of the number of elements in the array by the
    // size of each element.

    // How many elements times the size of each elements in bytes
    arrayFinalSize =
        numArrayElements * (Enum.valueOf(NumericDataType.class, dataType).getBits() / 8);
    minimalFileSize = offset.getValue().add(BigInteger.valueOf(arrayFinalSize));

    long actualFileSize = -1;
    try {
      actualFileSize = FileSizesUtil.getExternalFilesize(dataFile);
    } catch (Exception ex) {
      LOG.error("Cannot retrieve file size from file " + dataFile.toString());
      throw new IOException("Error attempting to retrieve file size for " + dataFile.toString());
    }

    LOG.debug(
        "validateMinimumFileSize:dataType,bits,arrayFinalSize,minimalFileSize,actualFileSize {},{},{},{},{}",
        dataType, Enum.valueOf(NumericDataType.class, dataType).getBits(), minimalFileSize,
        actualFileSize);

    // Now compare the minimalFileSize with the actual file size extract from the
    // file system and report any error.

    if (minimalFileSize.longValue() > actualFileSize) {
      LOG.error("Object-defined size+offset is greater than actualFileSize in file: {},{},{}",
          minimalFileSize.longValue(), actualFileSize, dataFile.toString());
      String errorMessage =
          "Object-defined size+offset is greater than actual file size in data file: "
              + String.valueOf(minimalFileSize.longValue()) + " > " + String.valueOf(actualFileSize)
              + " " + dataFile.toString();
      this.listener.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.WARNING, ProblemType.FILESIZE_MISMATCH, errorMessage),
          target));
      return false;
    }

    return true;
  }

  private void addArrayProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, int array) {
    addArrayProblem(exceptionType, problemType, message, dataFile, array, null);
  }

  private void addArrayProblem(ExceptionType exceptionType, ProblemType problemType, String message,
      URL dataFile, int array, int[] location) {
    this.listener.addProblem(new ArrayContentProblem(exceptionType, problemType, message, dataFile,
        this.context.getTarget(), array, location));
  }

  public ArrayObject getArray() {
    return array;
  }

  public void setArray(ArrayObject array) {
    this.array = array;
  }

  public int getArrayIndex() {
    return arrayIndex;
  }

  public void setArrayIndex(int arrayIndex) {
    this.arrayIndex = arrayIndex;
  }
}
