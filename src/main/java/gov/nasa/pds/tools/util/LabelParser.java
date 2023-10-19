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
package gov.nasa.pds.tools.util;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import net.sf.saxon.Configuration;
import net.sf.saxon.lib.ParseOptions;
import net.sf.saxon.om.TreeInfo;
import net.sf.saxon.xpath.XPathEvaluator;

public class LabelParser {

  /**
   * Parses a label.
   * 
   * @param source the source to parse.
   * @return a DocumentInfo object.
   * @throws TransformerException
   */
  public static TreeInfo parse(Source source) throws TransformerException {
    XPathEvaluator xpath = new XPathEvaluator();
    Configuration configuration = xpath.getConfiguration();
    configuration.setLineNumbering(true);
    configuration.setXIncludeAware(Utility.supportXincludes());
    ParseOptions options = new ParseOptions();
    options.withErrorHandler(new XMLErrorListener());
    options.withLineNumbering(true);
    options.withXIncludeAware(Utility.supportXincludes());
    return configuration.buildDocumentTree(source, options);
  }
}
