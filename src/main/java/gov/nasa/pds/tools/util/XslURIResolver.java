//	Copyright 2009-2010, by the California Institute of Technology.
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

package gov.nasa.pds.tools.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.dom.DOMSource;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This class will load stylesheets used for processing schematron from within
 * the jar file. These resources are in the schematron directory within the jar
 * file of the core library.
 * 
 * @author pramirez
 * 
 */
public class XslURIResolver implements URIResolver {

  /*
   * (non-Javadoc)
   * 
   * @see javax.xml.transform.URIResolver#resolve(java.lang.String,
   * java.lang.String)
   */
  @Override
  public Source resolve(String href, String base) throws TransformerException {
    try {
      ClassLoader cl = this.getClass().getClassLoader();
      InputStream in = cl.getResourceAsStream("schematron/" + href);
      InputSource xslInputSource = new InputSource(in);
      Document xslDoc = DocumentBuilderFactory.newInstance()
          .newDocumentBuilder().parse(xslInputSource);
      DOMSource xslDomSource = new DOMSource(xslDoc);
      xslDomSource.setSystemId("schematron/" + href);
      return xslDomSource;
    } catch (IOException e) {
      throw new TransformerException(e.getMessage());
    } catch (SAXException e) {
      throw new TransformerException(e.getMessage());
    } catch (ParserConfigurationException e) {
      throw new TransformerException(e.getMessage());
    }
  }

}
