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
// $Id: InventoryKeys.java -1M 2010-11-01 17:33:08Z (local) $
package gov.nasa.pds.tools.inventory.reader;

import java.util.HashMap;
import java.util.Map;

public class InventoryKeys {

  public static final Map<String, String> fieldDelimiters =
    new HashMap<String, String>();

  static {
    fieldDelimiters.put("comma",",");
    fieldDelimiters.put("horizontal_tab", "\\t");
    fieldDelimiters.put("semicolon", ";");
    fieldDelimiters.put("vertical_bar", "\\|");
  }
}
