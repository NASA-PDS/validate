package gov.nasa.pds.tools.validate.rule.pds4;

import java.io.IOException;
import gov.nasa.pds.objectAccess.InvalidTableException;

public interface DataObjectValidator {

  /**
   * Validate the data object
   * 
   * @throws InvalidTableException
   * @throws IOException
   */
  public boolean validate() throws InvalidTableException, IOException, Exception;

  /**
   * Validate the data object definition in the label
   * 
   * @throws InvalidTableException
   * @throws IOException
   */
  public boolean validateDataObjectDefinition() throws InvalidTableException, IOException;

  /**
   * Validate the contents (bits) of the data object
   * 
   * @throws InvalidTableException
   * @throws IOException
   */
  public boolean validateDataObjectContents() throws InvalidTableException, IOException, Exception;

}
