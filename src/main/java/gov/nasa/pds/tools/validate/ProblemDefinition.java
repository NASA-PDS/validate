// Copyright 2006-2017, by the California Institute of Technology.
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
package gov.nasa.pds.tools.validate;

import gov.nasa.pds.tools.label.ExceptionType;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * Defines a specific problem uncovered by a validation rule.
 */
public class ProblemDefinition {

  private static final AtomicInteger keyGenerator = new AtomicInteger();

	private final int id;
	private final ExceptionType severity;
	private final ProblemType type;
	private final String message;
	private final String standardsDocument;
	private final String standardsSection;
	private int knownHashCode;

	public ProblemDefinition(
			ExceptionType severity,
			ProblemType type,
			String message,
			String standardsDocument,
			String standardsSection
	) {
	    this.id = keyGenerator.incrementAndGet();
		this.severity = severity;
		this.type = type;
		this.message = message;
		this.standardsDocument = standardsDocument;
		this.standardsSection = standardsSection;

	}
	
  public ProblemDefinition(
      ExceptionType severity,
      ProblemType type,
      String message
  ) {
    this(severity, type, message, null, null);
  }

	public int getID() {
	  return id;
	}

	public ExceptionType getSeverity() {
	  return severity;
	}

	public ProblemType getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

	public String getStandardsDocument() {
		return standardsDocument;
	}

	public String getStandardsSection() {
		return standardsSection;
	}

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof ProblemDefinition)) {
      return false;
    }

    ProblemDefinition other = (ProblemDefinition) obj;
    
    return message.equals(other.message)
            && severity==other.severity
            && type==other.type
            && Objects.equals(standardsDocument, other.standardsDocument)
            && Objects.equals(standardsSection, other.standardsSection);
  }

  @Override
  public int hashCode() {
    if (knownHashCode == 0) {
      String combined = message + severity.toString() + standardsDocument + standardsSection + type.toString();
      knownHashCode = combined.hashCode();
    }
    
    return knownHashCode;
  }
}
