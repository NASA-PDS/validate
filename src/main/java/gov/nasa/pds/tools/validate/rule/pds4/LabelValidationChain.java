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
package gov.nasa.pds.tools.validate.rule.pds4;

import java.util.regex.Pattern;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.validate.rule.AbstractValidationChain;

/**
 * Implements a validation chain for checking a single PDS4 label.
 */
public class LabelValidationChain extends AbstractValidationChain {

	private static final Pattern LABEL_PATTERN = Pattern.compile(".*\\.xml", Pattern.CASE_INSENSITIVE);

	@Override
	public boolean isApplicable(String location) {
	  if (Utility.isDir(location)) {
	    return false;
	  }

	  return true;
	}

}
