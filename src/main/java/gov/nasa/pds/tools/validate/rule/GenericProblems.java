package gov.nasa.pds.tools.validate.rule;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Standard;

/**
 * Defines problems which are not specific to a data standard.
 */
public final class GenericProblems {

  /**
   * Indicates a problem in a UTF-8 byte stream, where one or more bytes do not indicate a legal
   * encoding of a character.
   */
  public static final ProblemDefinition MALFORMED_UTF8_CHARACTER =
      new ProblemDefinition(ExceptionType.ERROR, ProblemType.INVALID_CHARACTER_STREAM,
          "Malformed UTF-8 byte sequence", Standard.RFC_3629, "4");

  public static final ProblemDefinition UNCAUGHT_EXCEPTION =
      new ProblemDefinition(ExceptionType.ERROR, ProblemType.INTERNAL_ERROR,
          "Uncaught exception while validating", null, null);

  private GenericProblems() {
    // Never instantiated.
  }

}
