package gov.nasa.pds.tools.label;

/**
 * Defines an interface for handling exceptions during label validation.
 */
public interface ExceptionHandler {

  /**
   * Reports an exception encountered during label validation.
   *
   * @param exception the exception encountered
   */
  void addException(LabelException exception);

}
