//	Copyright 2009-2017, by the California Institute of Technology.
//	ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//	Any commercial use must be negotiated with the Office of Technology
//	Transfer at the California Institute of Technology.
//
//	This software is subject to U. S. export control laws and regulations
//	(22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//	is subject to U.S. export control laws and regulations, the recipient has
//	the responsibility to obtain export licenses or other export authority as
//	may be required before exporting such information to foreign countries or
//	providing access to foreign nationals.
//
//	$Id$
//

package gov.nasa.pds.tools.label;

/**
 * @author pramirez
 *
 */
public class LabelException extends Exception {
  private static final long serialVersionUID = 6046402124396835033L;
  private ExceptionType exceptionType;
  private String messageKey;
  private String publicId;
  private String systemId;
  private Integer lineNumber;
  private Integer columnNumber;
  private String source;

  public LabelException(ExceptionType exceptionType, String messageKey,
      String publicId, String systemId, Integer lineNumber, Integer columnNumber) {
    this.exceptionType = exceptionType;
    this.messageKey = messageKey;
    this.publicId = publicId;
    this.systemId = systemId;
    this.source = systemId;
    if (lineNumber == null) {
      this.lineNumber = -1;
    } else {
      this.lineNumber = lineNumber;
    }
    if (columnNumber == null) {
      this.columnNumber = -1;
    } else {
      this.columnNumber = columnNumber;
    }
  }

  public LabelException(ExceptionType exceptionType, String message, String filepath) {
    this(exceptionType, message, "", filepath, -1, -1);
  }

  public ExceptionType getExceptionType() {
    return this.exceptionType;
  }

  @Override
  public String getMessage() {
    return this.messageKey;
  }

  public String getPublicId() {
    return this.publicId;
  }

  public String getSystemId() {
    return this.systemId;
  }

  public Integer getLineNumber() {
    return this.lineNumber;
  }

  public Integer getColumnNumber() {
    return this.columnNumber;
  }

  public String getSource() {
    return this.source;
  }
  
  public void setSource(String source) {
    this.source = source;
  }
}
