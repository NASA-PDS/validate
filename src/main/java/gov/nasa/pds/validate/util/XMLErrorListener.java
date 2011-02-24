// Copyright 2006-2010, by the California Institute of Technology.
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
// $Id: XMLErrorListener.java 8360 2011-01-11 19:26:28Z mcayanan $
package gov.nasa.pds.validate.util;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Class that handles errors while parsing an XML file.
 *
 * @author mcayanan
 *
 */
public class XMLErrorListener implements ErrorListener {

    /**
     * Method is called when an error is encountered.
     *
     * @param exception The exception containing the error.
     *
     * @throws TransformerException Throws the exception.
     */
    @Override
    public void error(TransformerException exception)
    throws TransformerException {
        throw new TransformerException(exception);
    }

    /**
     * Method is called when a fatal error is encountered.
     *
     * @param exception The exception containing the fatal error.
     *
     * @throws TransformerException Throws the exception.
     */
    @Override
    public void fatalError(TransformerException exception)
        throws TransformerException {
      throw new TransformerException(exception);

    }

    /**
     * Method is called when a warning is encountered.
     *
     * @param exception The exception containing the warning.
     *
     * @throws TransformerException Throws the exception.
     */
    @Override
    public void warning(TransformerException exception)
    throws TransformerException {
      throw new TransformerException(exception);
    }

}
