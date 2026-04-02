// Copyright 2009-2018, by the California Institute of Technology.
// ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
// Any commercial use must be negotiated with the Office of Technology
// Transfer at the California Institute of Technology.
//
// This software is subject to U. S. export control laws and regulations
// (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
// is subject to U.S. export control laws and regulations, the recipient has
// the responsibility to obtain export licenses or other export authority as
// may be required before exporting such information to foreign countries or
// providing access to foreign nationals.
//
// $Id$
package gov.nasa.pds.tools.label;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XslURIResolver;
import gov.nasa.pds.tools.validate.ProblemHandler;

/**
 * A class that transforms Schematron files based on the isoSchematron stylesheet.
 *
 * @author mcayanan
 *
 */
public class SchematronTransformer {
  private static final Logger LOG = LoggerFactory.getLogger(SchematronTransformer.class);
  private final Map<String, Templates> cachedTemplates = new ConcurrentHashMap<>();
  
  /**
   * Constructor.
   *
   * @throws TransformerConfigurationException A transformer configuration error occurred.
   */
  public SchematronTransformer() throws TransformerConfigurationException {
    // Use saxon for schematron (i.e. the XSLT generation).
    System.setProperty("javax.xml.transform.TransformerFactory",
        "net.sf.saxon.TransformerFactoryImpl");
    DocumentBuilderFactory.newInstance().setNamespaceAware(true);
  }

  private Transformer buildIsoTransformer() throws TransformerConfigurationException {
    TransformerFactory isoFactory = TransformerFactory.newInstance();
    try {
      isoFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
      //isoFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, ""); see validate#1103
      isoFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    } catch (TransformerConfigurationException e) {
      throw new TransformerConfigurationException("Failed to configure TransformerFactory for secure processing", e);
    }
    // Set the resolver that will look in the jar for imports
    isoFactory.setURIResolver(new XslURIResolver());
    // Load the isoSchematron stylesheet that will be used to transform each
    // schematron file
    Source isoSchematron = new StreamSource(
        LabelValidator.class.getResourceAsStream("/schematron/iso_svrl_for_xslt2.xsl"));
    return isoFactory.newTransformer(isoSchematron);    
  }
  /**
   * Transform the given schematron source.
   *
   * @param source The schematron source.
   *
   * @return A transformed schematron.
   *
   * @throws TransformerException If an error occurred during the transform process.
   */
  public Transformer transform(Source source) throws TransformerException {
    return transform(source, null);
  }

  /**
   * Transform the given schematron source.
   *
   * @param source The schematron source.
   * @param handler Container to hold problems that occurred during the transform process.
   *
   * @return A transformed schematron.
   *
   * @throws TransformerException If an error occurred during the transform process.
   */
  // Not cached — Source objects are not stably comparable/hashable.
  // In practice LabelValidator only calls the String-based overload (line 622),
  // so this uncached path does not affect bundle-validation performance.
  public Transformer transform(Source source, ProblemHandler handler) throws TransformerException {
    return compileSchematron(source, handler).newTransformer();
  }

  private Templates compileSchematron(Source source, ProblemHandler handler) throws TransformerException {
    Transformer isoTransformer = this.buildIsoTransformer();
    if (handler != null) {
      isoTransformer.setErrorListener(new TransformerErrorListener(handler));
    }
    StringWriter schematronStyleSheet = new StringWriter();
    isoTransformer.transform(source, new StreamResult(schematronStyleSheet));
    return TransformerFactory.newInstance()
        .newTemplates(new StreamSource(new StringReader(schematronStyleSheet.toString())));
  }

  public Transformer transform(String source) throws TransformerException {
    return this.transform(source, null);
  }
  public Transformer transform(String source, ProblemHandler handler) throws TransformerException {
    String key = sha256(source);
    // Intentional check-then-act: a concurrent miss may compile twice, but
    // both results are equivalent and the race is self-healing after warmup.
    // computeIfAbsent is avoided because it holds a lock during compilation.
    Templates templates = cachedTemplates.get(key);
    if (templates == null) {
      LOG.debug("transform: cache miss, compiling schematron (length={})", source.length());
      templates = compileSchematron(new StreamSource(new StringReader(source)), handler);
      cachedTemplates.put(key, templates);
    } else {
      LOG.debug("transform: cache hit, reusing compiled schematron (length={})", source.length());
    }
    return templates.newTransformer();
  }

  // Package-private: cache lifetime is tied to the SchematronTransformer instance.
  // LabelValidator.clear() creates a new instance (line 219), which naturally
  // starts with an empty cache. Exposed for testing.
  void clearCache() {
    cachedTemplates.clear();
  }

  /** Returns the number of cached templates entries. Package-private for testing. */
  int cacheSize() {
    return cachedTemplates.size();
  }

  private static final ThreadLocal<MessageDigest> SHA256 = ThreadLocal.withInitial(() -> {
    try {
      return MessageDigest.getInstance("SHA-256");
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  });

  private static String sha256(String input) {
    MessageDigest digest = SHA256.get();
    digest.reset();
    return HexFormat.of().formatHex(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
  }
  /**
   * Transform the given schematron.
   *
   * @param schematron The URL to the schematron.
   *
   * @return A transformed schematron.
   *
   * @throws TransformerException If an error occurred during the transform process.
   */
  public String fetch(URL schematron) throws TransformerException {
    return fetch(schematron, null);
  }

  /**
   * Transform the given schematron.
   *
   * @param schematron the URL to the schematron.
   * @param handler an problem handler to capture problems.
   *
   * @return a transformed schematron.
   *
   * @throws TransformerException if an error occurred during the transform process.
   */
  public String fetch(URL schematron, ProblemHandler handler) throws TransformerException {
    LOG.debug("transform:schematron {}", schematron);
    final int MAX_RETRIES = 3;
    int retry = 0;
    while (retry < MAX_RETRIES) {
      try (InputStream in = Utility.openConnection(schematron.openConnection())) {
        String document = IOUtils.toString(in, StandardCharsets.UTF_8);
        this.transform(document, handler); // test that document is valid
        return document;
      } catch (IOException io) {
        String message = "";
        retry++;
        if (io instanceof FileNotFoundException) {
          message = "Cannot read schematron as URL cannot be found: " + io.getMessage();
        } else {
          // message = io.getMessage();
          // Put a more detail message since io.getMessage only return the schematron file
          // name.
          message = "Cannot read schematron from URL " + schematron;
        }
        LOG.debug("transform:message {}", message);
        if (retry < MAX_RETRIES) {
          LOG.info(message);
        } else {
          throw new TransformerException(message);
        }
      }
    }
    throw new RuntimeException("impossible to get here but static analysis cannot tell so adding this useless exception");
  }
}
