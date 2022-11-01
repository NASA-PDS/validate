// Copyright 2006-2018, by the California Institute of Technology.
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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import gov.nasa.pds.tools.label.CachedEntityResolver;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelValidator;
import gov.nasa.pds.tools.label.MissingLabelSchemaException;
import gov.nasa.pds.tools.label.SchematronTransformer;
import gov.nasa.pds.tools.label.XMLCatalogResolver;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.tools.validate.ProblemContainer;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.ValidationProblem;
import gov.nasa.pds.tools.validate.ValidationResourceManager;
import gov.nasa.pds.tools.validate.rule.AbstractValidationRule;
import gov.nasa.pds.tools.validate.rule.FileAreaExtractor;
import gov.nasa.pds.tools.validate.rule.GenericProblems;
import gov.nasa.pds.tools.validate.rule.ValidationTest;
import gov.nasa.pds.validate.constants.Constants;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

/**
 * Implements a validation chain that validates PDS4 bundles. It is applicable if there is a bundle
 * label in the root directory.
 */
public class LabelValidationRule extends AbstractValidationRule {

  private static final Logger LOG = LoggerFactory.getLogger(LabelValidationRule.class);

  private SchemaValidator schemaValidator;
  private SchematronTransformer schematronTransformer;
  private Map<URL, ProblemContainer> labelSchemaResults;
  private Map<URL, ProblemContainer> labelSchematronResults;
  private Map<URL, Transformer> labelSchematrons;
  private XMLExtractor extractor;

  private static Object lock = new Object();

  public LabelValidationRule() throws TransformerConfigurationException {
    schemaValidator = new SchemaValidator();
    schematronTransformer = new SchematronTransformer();
    labelSchemaResults = new HashMap<>();
    labelSchematronResults = new HashMap<>();
    labelSchematrons = new HashMap<>();
    extractor = null;
  }

  @Override
  public boolean isApplicable(String location) {
    if (Utility.isDir(location)) {
      return false;
    }
    return true;
  }

  /**
   * Implements a rule that checks the label file extension.
   */
  @ValidationTest
  public void checkLabelExtension() {
    LOG.debug("checkLabelExtension:getLabelExt: {} ", getContext().getLabelExtension());
    LOG.debug("checkLabelExtension:getRule: {} ", getContext().getRule());
    // Because the label extension is allowed for any case, we must lowercase the
    // extension before comparing.
    String labelSuffix = FilenameUtils.getExtension(getTarget().toString());
    LOG.debug("checkLabelExtension:getTarget(),labelSuffix,XML_SUFFIX {},[{}],[{}]", getTarget(),
        labelSuffix, getContext().getLabelExtension());

    // Modified from a straight check of endsWith() to comparing the label suffix
    // ignoring case.
    // Old (strict method:
    // (!FilenameUtils.getName(getTarget().toString()).endsWith(XML_SUFFIX))
    // Improved method : (!labelSuffix.equalsIgnoreCase(XML_SUFFIX))

    if (!labelSuffix.equalsIgnoreCase(getContext().getLabelExtension())) {
      LOG.error(
          "checkLabelExtension:Label extension [{}] does not match expected [{}] from target {}",
          labelSuffix, getContext().getLabelExtension(), getTarget());
      reportError(PDS4Problems.INVALID_LABEL_EXTENSION, getTarget(), -1, -1);
    }
  }

  private void flagNonExistentFile(URL target) throws IOException {
    LOG.debug("flagNonExistentFile: target {}", target);
    // Do a sanity check on existence of the label and flag it.
    try {
      if (!new File(target.toURI()).exists()) {
        // Option 1: Throw the exception.
        // Throwing the exception will cause the callee of this function to report the
        // error as "Uncaught exception".
        String message = "Label " + target.toString() + " is not found";
        LOG.error(message);

        throw new IOException(message);

        // Option 2: Report the error here.
        // reportError(GenericProblems.UNCAUGHT_EXCEPTION, target, -1, -1,
        // message);
      }
    } catch (Exception e) {
      throw new IOException(e.getMessage());
    }
  }

  private void flagBadFilename(URL target) {
    // It is possible that the file name violate naming rules. Check them here. If
    // there are problems,
    // add each problem to the listener in this class.
    //
    // Example: 3juno_lwr01896_ines%20fits%20headers.pdfa.xml contains spaces
    // between the characters.
    //
    // Note that the class FileAndDirectoryNamingChecker extends
    // FileAndDirectoryNamingRule but re-implement
    // with new function checkFileAndDirectoryNamingWithChecker() to not call
    // reportError() but to return a list of ValidationProblem.

    FileAndDirectoryNamingChecker FileAndDirectoryNamingChecker =
        new FileAndDirectoryNamingChecker();
    List<Target> list = new ArrayList<>();
    list.add(new Target(target, false)); // Make a list of just one name.
    List<ValidationProblem> validationProblems =
        FileAndDirectoryNamingChecker.checkFileAndDirectoryNamingWithChecker(list);
    LOG.debug("flagBadFilename:target {}", target);
    LOG.debug("flagBadFilename:validationProblems.size() {}", validationProblems.size());
    if (validationProblems.size() > 0) {
      for (ValidationProblem problem : validationProblems) {
        problem.setSource(target.toString()); // Because all problems with the filename are for
                                              // target, set the
                                              // source with target.
        getListener().addProblem(problem); // Add the problem of the file name to this listener.
        LOG.debug("flagBadFilename:addProblem(problem) {},{}", problem, target.toString());
      }
    }
  }

  /**
   * Parses the label and records any errors resulting from the parse, including schema and
   * schematron errors.
   */
  @ValidationTest
  public void validateLabel() {
    long t0 = System.currentTimeMillis();
    boolean labelIsValidFlag = false; // Explicit flag to indicate if target is valid or not. Value
                                      // is set to true
                                      // if parsed successfully.
    URL target = getTarget();
    String targetFileName = target.toString().substring(target.toString().lastIndexOf("/") + 1);
    ProblemProcessor processor = new ProblemProcessor(getListener(), target);

    LabelValidator validator = ValidationResourceManager.INSTANCE.getResource(LabelValidator.class);

    LOG.debug("validateLabel:target,targetFileName {},{}", target, targetFileName);
    LOG.debug("validateLabel:getContext().isForceLabelSchemaValidation() {}",
        getContext().isForceLabelSchemaValidation());

    // https://github.com/NASA-PDS/validate/issues/188 As a user, I want to validate
    // a bundle that uses multiple versions of the Information Model / Discipline
    // LDDs
    //
    // Each label may contain different schemas and different versions between each
    // other.
    // The list of schema locations must be updated for every label in the
    // schemaValidator object.
    // If not, subsequent label(s) will be using the schema location from a previous
    // label and
    // if the versions differ, there will be errors and it will be very difficult to
    // understand
    // since the user assuming the correct schema was being used.
    if (getContext().isForceLabelSchemaValidation()) {
      try {
        schemaValidator.setExternalLocations(getExtractor(target).getSchemaLocation());
      } catch (Exception ignore) {
        // Should not throw an exception
      }
    }

    try {
      // Do a sanity check on existence of the label.
      this.flagNonExistentFile(target);

      // Do a sanity check on bad file name.
      this.flagBadFilename(target);

      // Also check any file names referred to in this label.
      try {
        FileAreaExtractor fileAreaExtractor = new FileAreaExtractor();
        fileAreaExtractor.findAndFlagBadFilenames(target, getListener());
      } catch (Exception ignore) {
        LOG.error("Function findAndFlagBadFilenames() failed for target {}", target);
      }

      Document document = null;
      boolean pass = true;
      boolean forcedValidateLabel = true; // Force the validation of the label even if there are
                                          // issues with
                                          // schematron.
      boolean hasValidSchemas = false;
      ProblemContainer problemContainer = new ProblemContainer();
      if (getContext().getCatalogResolver() != null
          || getContext().isForceLabelSchemaValidation()) {
        // boolean hasValidSchemas = false;
        Map<String, Transformer> labelSchematrons = null;
        synchronized (lock) {
          // Validate the label's schema and schematron first before doing
          // label validation
          hasValidSchemas =
              validateLabelSchemas(target, problemContainer, getContext().getCatalogResolver());

          labelSchematrons =
              validateLabelSchematrons(target, problemContainer, getContext().getCatalogResolver());
          LOG.debug("validateLabel:target,hasValidSchemas,labelSchematrons.size() {},{},{}", target,
              hasValidSchemas, labelSchematrons.size());
        }

        // https://github.com/NASA-PDS/validate/issues/17
        // Important note: Any errors found in the above two functions:
        //
        // validateLabelSchemas(),
        // `validateLabelSchematrons()
        //
        // would have been reported already to the listener. There is no need to keep
        // them around
        // because the same errors would be added again later in this function.
        // The errors need to be cleared either by the object problemContainer
        // performing an
        // explicit clear function or by creating a new ProblemContainer object.
        //
        // Option 1: problemContainer.clear() [Chosen in this function]
        // Option 2: problemContainer = new ProblemContainer()

        // Option 1: problemContainer.clear() [Chosen in this function]
        problemContainer.clear();

        LOG.debug("validateLabel:problemContainer.clear() called");
        LOG.debug("validateLabel:target,hasValidSchemas,labelSchematrons.size() {},{},{}", target,
            hasValidSchemas, labelSchematrons.size());

        if (hasValidSchemas && !labelSchematrons.isEmpty()) {
          CachedEntityResolver resolver = new CachedEntityResolver();
          resolver.addCachedEntities(schemaValidator.getCachedLSResolver().getCachedEntities());
          validator.setCachedEntityResolver(resolver);
          validator.setCachedLSResourceResolver(schemaValidator.getCachedLSResolver());
          validator.setLabelSchematrons(labelSchematrons);
          if (getContext().isForceLabelSchemaValidation()) {
            try {
              schemaValidator.setExternalLocations(getExtractor(target).getSchemaLocation());
            } catch (Exception ignore) {
              // Should not throw an exception
            }
          }
        } else {
          // Print any label problems that occurred during schema and schematron
          // validation.
          if (problemContainer.getProblems().size() != 0) {
            for (ValidationProblem problem : problemContainer.getProblems()) {
              problem.setSource(target.toString());
              getListener().addProblem(problem);
              LOG.debug("validateLabel:addProblem(problem) {}", problem);
            }
          }
          pass = false;
          LOG.debug("validateLabel:PASS_TO_FALSE:target,pass {},{}", target, pass);
        }
      }
      LOG.debug("validateLabel:target,hasValidSchemas,labelSchematrons.isEmpty() {},{},{}", target,
          hasValidSchemas, labelSchematrons.isEmpty());
      LOG.debug("validateLabel:target,pass {},{}", target, pass);
      // If forcedValidateLabel is true, call parseAndValidate() regardless.
      if (pass || forcedValidateLabel) {
        // if (2 == 2) {
        getListener().addLocation(target.toString());
        LOG.debug("validateLabel:afor:target {}", target);
        document = validator.parseAndValidate(processor, target);
      }
      LOG.debug("validateLabel:target,document {},{}", target, document);
      if (document != null) {
        getContext().put(PDS4Context.LABEL_DOCUMENT, document);
        labelIsValidFlag = true; // A non-null document signified that the label is valid.
      }
    } catch (SAXException | IOException | ParserConfigurationException | TransformerException
        | MissingLabelSchemaException e) {
      if (e instanceof XPathException) {
        XPathException xe = (XPathException) e;
        if (!xe.hasBeenReported()) {
          reportError(GenericProblems.UNCAUGHT_EXCEPTION, target, -1, -1, e.getMessage());
        }
      } else // Don't need to report SAXParseException messages as they have already
      // been reported by the LabelValidator's error handler
      if (!(e instanceof SAXParseException)) {
        reportError(GenericProblems.UNCAUGHT_EXCEPTION, target, -1, -1, e.getMessage());
      }
    }
    long t1 = System.currentTimeMillis();

    if (isDebugLogLevel()) {
      System.out.println(
          "\nDEBUG  [" + ProblemType.TIMING_METRICS.getKey() + "]  " + System.currentTimeMillis()
              + " :: " + targetFileName + " :: validateLabel() in " + (t1 - t0) + " ms\n");
    }
    LOG.debug("validateLabel:target,labelIsValidFlag {},{}", target, labelIsValidFlag);
  }

  private boolean resolveSingleSchema(URL label, Map.Entry<String, URL> schemaLocation,
      URL schemaUrl, ProblemContainer container, ProblemContainer labelProblems,
      XMLCatalogResolver resolver) {
    boolean resolvableUrl = true;

    LOG.debug("resolveSingleSchema:schemaUrl {}", schemaUrl);
    LOG.debug("resolveSingleSchema:schemaUrl,key {},{}", schemaUrl, schemaLocation.getKey());

    if (resolver != null) {
      String resolvedUrl = null;
      try {
        resolvedUrl =
            resolver.resolveSchema(schemaLocation.getKey(), schemaUrl.toString(), label.toString());

        LOG.debug("resolvedUrl: {}", resolvedUrl);
        if (resolvedUrl != null) {
          schemaUrl = new URL(resolvedUrl);
        } else {
          labelProblems
              .addProblem(new ValidationProblem(
                  new ProblemDefinition(ExceptionType.ERROR,
                      ProblemType.CATALOG_UNRESOLVABLE_SCHEMA, "Could not resolve schema '"
                          + schemaLocation.getValue().toString() + "' through the catalog"),
                  label));
          resolvableUrl = false;
        }
      } catch (IOException io) {
        labelProblems.addProblem(new ValidationProblem(
            new ProblemDefinition(ExceptionType.ERROR, ProblemType.CATALOG_UNRESOLVABLE_SCHEMA,
                "Error while trying to resolve schema '" + schemaLocation.getValue().toString()
                    + "' through the catalog: " + io.getMessage()),
            label));
        resolvableUrl = false;
      }
    }

    LOG.debug("resolveSingleSchema:schemaUrl,resolvableUrl {},{}", schemaUrl, resolvableUrl);
    return (resolvableUrl);
  }

  private boolean loadSingleSchemaIntoSources(URL label, URL schemaUrl,
      ProblemContainer problemContainer, ProblemContainer labelProblems,
      List<StreamSource> streamSources) {
    // Note that these 2 parameters will serve as both input and output:
    // labelProblems, and streamSources.
    // for holding problems with the label and the streamSources will grow as a
    // list.

    boolean streamSourcePassFlag = true;

    schemaValidator.getCachedLSResolver().setProblemHandler(problemContainer);
    LSInput input = schemaValidator.getCachedLSResolver().resolveResource("", "", "",
        schemaUrl.toString(), schemaUrl.toString());
    boolean addSourceFlag = true;
    if (problemContainer.getProblems().size() != 0) {
      try {
        for (ValidationProblem le : problemContainer.getProblems()) {
          le.setSource(label.toURI().toString());
          getListener().addProblem(le);
        }
        if (problemContainer.hasError() || problemContainer.hasFatal()) {
          streamSourcePassFlag = false;
          addSourceFlag = false;
        }
      } catch (URISyntaxException u) {
        labelProblems.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.FATAL,
            ProblemType.SCHEMA_ERROR, "URI syntax exception occurred for schema '"
                + schemaUrl.toString() + "': " + u.getMessage()),
            label));
      }
    }

    // Load the streamSource to streamSources if there are no problems.
    if (addSourceFlag) {
      StreamSource streamSource = new StreamSource(input.getByteStream());
      streamSource.setSystemId(schemaUrl.toString());
      streamSources.add(streamSource);
    }

    LOG.debug(
        "loadSingleSchemaIntoSources:label,schemaUrl,addSourceFlag,streamSourcePassFlag,streamSources.size() {},{},{},{},{}",
        label, schemaUrl, addSourceFlag, streamSourcePassFlag, streamSources.size());
    return (streamSourcePassFlag);
  }

  private boolean validateLoadedSchemas(URL label, List<StreamSource> streamSources,
      ProblemContainer labelProblems) {
    // Validate the loaded Schemas that are in streamSources, a list of StreamSource
    // for correctness.
    boolean passFlag = true; // Will be set to false if at least one schema did not validate.
    for (StreamSource source : streamSources) {
      try {
        URL schemaUrl = null;
        try {
          LOG.debug("validateAllLoadedSchemas:source.getSystemId() {}", source.getSystemId());
          schemaUrl = new URL(source.getSystemId());
        } catch (MalformedURLException ignore) {
          // Should never throw an exception
        }
        LOG.debug("validateAllLoadedSchemas:schemaUrl {}", schemaUrl);
        ProblemContainer container = new ProblemContainer();
        if (labelSchemaResults.containsKey(schemaUrl)) {
          container = labelSchemaResults.get(schemaUrl);
          if (container.getProblems().size() != 0) {
            for (ValidationProblem le : container.getProblems()) {
              le.setSource(label.toURI().toString());
              getListener().addProblem(le);
            }
            if (container.hasError() || container.hasFatal()) {
              passFlag = false;
              LOG.debug("validateAllLoadedSchemas:schemaUrl,passFlag SET_TO_FALSE_1 {},{}",
                  schemaUrl, passFlag);
            }
          }
        } else {
          try {
            // Before validating we need to load all schemas

            // Validate the schema source
            container = schemaValidator.validate(source);
            LOG.debug("validateAllLoadedSchemas:schemaUrl,passFlag {},{}", schemaUrl, passFlag);
          } catch (Exception e) {
            LOG.debug("validateAllLoadedSchemas:schemaUrl,passFlag SET_TO_error_1 {},{}", schemaUrl,
                passFlag); // Change to lowercase so as not to confuse the log file
            container.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.ERROR,
                ProblemType.SCHEMA_ERROR, "Error reading schema: " + e.getMessage()), schemaUrl));
          }
          LOG.debug(
              "validateAllLoadedSchemas:schemaUrl,passFlag SET_TO_error_2,container.hasError,container.hasFatal {},{},{},{}",
              schemaUrl, passFlag, container.hasError(), container.hasFatal()); // Change to
                                                                                // lowercase so
                                                                                // as not to confuse
                                                                                // the
                                                                                // log file
          if (container.getProblems().size() != 0) {
            for (ValidationProblem le : container.getProblems()) {
              le.setSource(label.toURI().toString());
              getListener().addProblem(le);
              LOG.debug(
                  "validateAllLoadedSchemas:schemaUrl,passFlag,message SET_TO_error_3 {},{},[{}]",
                  schemaUrl, passFlag, le.getMessage()); // Change to lowercase so as not to confuse
                                                         // the log file
            }
            if (container.hasError() || container.hasFatal()) {
              passFlag = false;
              LOG.debug("validateAllLoadedSchemas:schemaUrl,passFlag SET_TO_FALSE_2 {},{}",
                  schemaUrl, passFlag);
            }
          }
          labelSchemaResults.put(schemaUrl, container);
        }
        LOG.debug("validateAllLoadedSchemas:label,schemaUrl,passFlag {},{},{}", label, schemaUrl,
            passFlag);
      } catch (URISyntaxException u) {
        labelProblems.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.FATAL,
            ProblemType.SCHEMA_ERROR, "URI syntax exception occurred for schema '"
                + source.getSystemId() + "': " + u.getMessage()),
            label));
      }
    } // end for (StreamSource source : streamSources) {
    return (passFlag);
  }

  private boolean validateLabelSchemas(URL label, ProblemContainer labelProblems,
      XMLCatalogResolver resolver) {

    // Each label has a list of schemas that describe the format of the content of
    // the label that should adhere to.
    // For example:
    // schemaLocation="http://pds.nasa.gov/pds4/pds/v1
    // http://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1A00.xsd"
    // This function will:
    // 1. Retrieve these locations with getSchemaLocations(), return false if cannot
    // retrieve them.
    // 2. For each location, ensure that the location exists and can be resolved
    // with resolveSingleSchema() function.
    // 3. Load each schema into a list of stream sources with
    // loadSingleSchemaIntoSources() function.
    // 4. And finally, validate the schema for correctness.

    boolean passFlag = true;
    List<StreamSource> sources = new ArrayList<>();
    Map<String, URL> schemaLocations = new LinkedHashMap<>();
    this.schemaValidator.setCatalogResolver(resolver);
    LOG.debug("validateLabelSchemas:MARKER_MARKER_MARKER");
    LOG.debug("validateLabelSchemas: label {}", label);

    try {
      schemaLocations = getSchemaLocations(label);
      LOG.debug("validateLabelSchemas: schemaLocations,schemaLocations.size() {},{}",
          schemaLocations, schemaLocations.size());
      LOG.debug("validateLabelSchemas: label,schemaLocations.size() {},{}", label,
          schemaLocations.size());
      LOG.debug("validateLabelSchemas: label,schemaLocations,schemaLocations.size() {},{},{}",
          label, schemaLocations, schemaLocations.size());
    } catch (Exception e) {
      labelProblems.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.FATAL, ProblemType.SCHEMA_ERROR, e.getMessage()),
          label));
      LOG.debug("validateLabelSchemas: passFlag {}", false);
      return false;
    }

    LOG.debug("validateLabelSchemas: label,labelProblems.getProblemCount():START_LOOP {},{}", label,
        labelProblems.getProblemCount());
    LOG.debug("validateLabelSchemas: label,labelProblems.getProblemCount() {},{}", label,
        labelProblems.getProblemCount());
    LOG.debug("validateLabelSchemas: schemaLocations.entrySet().size {}",
        schemaLocations.entrySet().size());
    // Loop through all the schemaLocations from the label and resolve them
    // boolean resolvableUrl = false;
    for (Map.Entry<String, URL> schemaLocation : schemaLocations.entrySet()) {

      URL schemaUrl = schemaLocation.getValue();
      ProblemContainer container = new ProblemContainer();

      // Make sure the schema is a valid address or a file.
      // Break up long code into resolveSingleSchema() function for readability.
      boolean resolvableUrl =
          resolveSingleSchema(label, schemaLocation, schemaUrl, container, labelProblems, resolver);

      // If we found the schema, let's read it into memory
      if (resolvableUrl) {

        // Break up long code into loadSingleSchemaIntoSources() function for
        // readability.
        passFlag = loadSingleSchemaIntoSources(label, schemaUrl, container, labelProblems, sources);
      } else {
        passFlag = false;
      }
    }

    LOG.debug("validateLabelSchemas: afor:validate schema:passFlag {}", passFlag);
    if (passFlag) {
      // Now let's loop through the schemas themselves, and validate them
      // Break up long code into validateLoadedSchemas() function for readability.
      passFlag = validateLoadedSchemas(label, sources, labelProblems);
    }
    LOG.debug("validateLabelSchemas: after:validate schema:passFlag {}", passFlag);
    LOG.debug("validateLabelSchemas: label,passFlag {},{}", label, passFlag);
    return passFlag;
  }

  private boolean validateSingleSchematron(URL label, URL schematronRef,
      XMLCatalogResolver resolver, ProblemContainer labelProblems,
      Map<String, Transformer> results) {
    // Note that these 2 parameters will serve as both input and output:
    // labelProblems, and results.
    // for holding problems with the label and the results will grow as a map.

    // Validate a single schematron related to a label for correctness.
    boolean schematronPassFlag = true;
    try {
      ProblemContainer container = null;
      boolean resolvableUrl = true;
      LOG.debug("validateSingleSchematron:schematronRef,resolver {},{}", schematronRef, resolver);
      if (resolver != null) {
        String resolvedUrl = null;
        try {
          String absoluteUrl =
              Utility.makeAbsolute(Utility.getParent(label).toString(), schematronRef.toString());
          resolvedUrl = resolver.resolveSchematron(absoluteUrl);
          LOG.debug("validateSingleSchematron:resolvedUrl {}", resolvedUrl);
          if (resolvedUrl != null) {
            schematronRef = new URL(resolvedUrl);
          } else {
            labelProblems
                .addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.ERROR,
                    ProblemType.CATALOG_UNRESOLVABLE_SCHEMATRON, "Could not resolve schematron '"
                        + schematronRef.toString() + "' through the catalog."),
                    label));
            resolvableUrl = false;
          }
        } catch (IOException io) {
          labelProblems.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.ERROR,
              ProblemType.CATALOG_UNRESOLVABLE_SCHEMATRON,
              "Error while trying to resolve schematron '" + schematronRef.toString()
                  + "' through the catalog: " + io.getMessage()),
              label));
          resolvableUrl = false;
        }
      }
      LOG.debug("validateSingleSchematron:schematronRef,resolvableUrl {},{}", schematronRef,
          resolvableUrl);
      if (resolvableUrl) {
        if (labelSchematrons.containsKey(schematronRef)) {
          container = labelSchematronResults.get(schematronRef);
          if (container.getProblems().size() != 0) {
            for (ValidationProblem le : container.getProblems()) {
              le.setSource(label.toURI().toString());
              getListener().addProblem(le);
            }
            if (container.hasError() || container.hasFatal()) {
              schematronPassFlag = false;
            }
          } else {
            results.put(schematronRef.toString(), labelSchematrons.get(schematronRef));
          }
        } else {
          container = new ProblemContainer();
          try {
            Transformer transformer = schematronTransformer.transform(schematronRef, container);
            labelSchematrons.put(schematronRef, transformer);
            results.put(schematronRef.toString(), transformer);
          } catch (TransformerException te) {
            // Ignore as the listener handles the exceptions and puts it into
            // the container
          } catch (Exception e) {
            container.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.FATAL, ProblemType.SCHEMATRON_ERROR,
                    "Error occurred while attempting to read schematron: " + e.getMessage()),
                schematronRef));
          }
          if (container.getProblems().size() != 0) {
            for (ValidationProblem le : container.getProblems()) {
              le.setSource(label.toURI().toString());
              getListener().addProblem(le);
            }
            if (container.hasError() || container.hasFatal()) {
              schematronPassFlag = false;
            }
          }
          labelSchematronResults.put(schematronRef, container);
        }
      } else {
        schematronPassFlag = false;
      }
    } catch (URISyntaxException u) {
      labelProblems.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.FATAL,
          ProblemType.SCHEMATRON_ERROR, "URI syntax exception occurred for schematron '"
              + schematronRef.toString() + "': " + u.getMessage()),
          label));
    }
    LOG.debug("validateSingleSchematron:label,schematronRef,schematronPassFlag {},{},{}", label,
        schematronRef, schematronPassFlag);
    return (schematronPassFlag);
  }

  private Map<String, Transformer> validateLabelSchematrons(URL label,
      ProblemContainer labelProblems, XMLCatalogResolver resolver) {
    boolean passFlag = true;
    Map<String, Transformer> results = new HashMap<>();
    List<URL> schematronRefs = new ArrayList<>();
    try {
      schematronRefs = getSchematrons(label, labelProblems);
      LOG.debug("validateLabelSchematrons:labelProblems.getWarningCount() {}",
          labelProblems.getWarningCount());
      if (labelProblems.getProblems().size() != 0) {
        for (ValidationProblem le : labelProblems.getProblems()) {
          getListener().addProblem(le);
          LOG.debug("validateLabelSchematrons:addProblem(le) {}", le);
        }
        passFlag = false;
      }
    } catch (Exception e) {
      labelProblems.addProblem(new ValidationProblem(
          new ProblemDefinition(ExceptionType.ERROR, ProblemType.SCHEMATRON_ERROR, e.getMessage()),
          label));
      passFlag = false;
    }

    LOG.debug("validateLabelSchematrons:schematronRefs.size() {}", schematronRefs.size());
    LOG.debug("validateLabelSchematrons:label,schematronRefs.size() {},{}", label,
        schematronRefs.size());
    // Now validate the schematrons
    boolean allSchematronsPassFlag = true;
    for (URL schematronRef : schematronRefs) {
      // Break up long code into validateSingleSchematron() function for readability.
      passFlag = validateSingleSchematron(label, schematronRef, resolver, labelProblems, results);

      // If one schematron does not pass, the whole label does not get pass.
      if (!passFlag) {
        allSchematronsPassFlag = false;
      }
    }
    if (!allSchematronsPassFlag) {
      results.clear();
    }
    LOG.debug(
        "validateLabelSchematrons:label,allSchematronsPassFlag,schematronRefs.size() {},{},{}",
        label, allSchematronsPassFlag, schematronRefs.size());
    LOG.debug("validateLabelSchematrons:label,labelProblems.getProblemCount() {},{}", label,
        labelProblems.getProblemCount());
    return results;
  }

  private Map<String, URL> getSchemaLocations(URL label) throws Exception {
    Map<String, URL> schemaLocations = new LinkedHashMap<>();
    String value = "";
    try {
      XMLExtractor extractor = getExtractor(label);
      value = extractor.getSchemaLocation();
    } catch (Exception e) {
      LOG.error(
          "getSchemaLocations:Error occurred while attempting to find schemas using the XPath '"
              + XMLExtractor.SCHEMA_LOCATION_XPATH + "': " + e.getMessage());
      throw new Exception("Error occurred while attempting to find schemas using the XPath '"
          + XMLExtractor.SCHEMA_LOCATION_XPATH + "': " + e.getMessage());
    }
    if (value == null || value.isEmpty()) {
      LOG.error("getSchemaLocations:No schema(s) found in the label.");
      throw new Exception("No schema(s) found in the label.");
    }
    StringTokenizer tokenizer = new StringTokenizer(value);
    if ((tokenizer.countTokens() % 2) != 0) {
      LOG.error("getSchemaLocations:schemaLocation value does not appear to have matching sets of "
          + "namespaces to uris: '" + schemaLocations + "'");
      throw new Exception("schemaLocation value does not appear to have matching sets of "
          + "namespaces to uris: '" + schemaLocations + "'");
    }
    // While loop that will grab the schema URIs
    while (tokenizer.hasMoreTokens()) {
      // First token assumed to be the namespace
      String namespace = tokenizer.nextToken();
      // Second token assumed to be the URI
      String uri = tokenizer.nextToken();
      URL schemaUrl = null;
      try {
        schemaUrl = new URL(uri);
      } catch (MalformedURLException mu) {
        // The schema specification value does not appear to be
        // a URL. Assume a local reference to the schematron and
        // attempt to resolve it.
        try {
          URL parent = label.toURI().resolve(".").toURL();
          schemaUrl = new URL(parent, uri);
        } catch (MalformedURLException mue) {
          LOG.error("getSchemaLocations:Cannot resolve schema specification '" + uri + "': "
              + mue.getMessage());
          throw new Exception(
              "Cannot resolve schema specification '" + uri + "': " + mue.getMessage());
        } catch (URISyntaxException e) {
          // Ignore
        }
      }
      schemaLocations.put(namespace, schemaUrl);
    }
    // LOG.debug("getSchemaLocations:schemaLocations.size()
    // {}",schemaLocations.size());
    return schemaLocations;
  }

  private void spaceCheckSchematypensPattern(URL label, ProblemContainer labelProblems,
      List<TinyNodeImpl> xmlModels) {
    // Check for case when there is no space before schematypens and report it.
    // <?xml-model
    // href="http://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1B00.sch"schematypens="http://purl.oclc.org/dsdl/schematron"
    // A better xml-model statement:
    // <?xml-model href="http://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1B00.sch"
    // schematypens="http://purl.oclc.org/dsdl/schematron"

    // https://github.com/NASA-PDS/validate/issues/17 Validate schematron references
    // and throw fatal error if invalid URI specified

    String SCHEMATYPENS_PATTERN_ONLY = "schematypens=";
    LOG.debug("spaceCheckSchematypensPattern:label,xmlModels.size() {},{}", label,
        xmlModels.size());
    for (TinyNodeImpl xmlModel : xmlModels) {
      String filteredData = xmlModel.getStringValue().replaceAll("\\s+", " ");
      filteredData = filteredData.trim();
      LOG.debug("spaceCheckSchematypensPattern:label,filteredData {},{}", label, filteredData);
      if (filteredData.contains(SCHEMATYPENS_PATTERN_ONLY)) {
        int posOfSchemaType = filteredData.indexOf(SCHEMATYPENS_PATTERN_ONLY);
        if (posOfSchemaType > 0) {
          LOG.debug("spaceCheckSchematypensPattern:label,substring {},[{}],{}", label,
              filteredData.substring(posOfSchemaType - 1, posOfSchemaType),
              filteredData.substring(posOfSchemaType - 1, posOfSchemaType).length());
          // If the character before SCHEMATYPENS_PATTERN_ONLY is not a space, report a
          // WARNING message.
          if (!filteredData.substring(posOfSchemaType - 1, posOfSchemaType).equals(" ")) {

            LOG.debug(
                "spaceCheckSchematypensPattern:label,message,xmlModel.getParent().getLineNumber() {},{},{}",
                label, "Expecting a space before pattern [" + SCHEMATYPENS_PATTERN_ONLY + "]",
                xmlModel.getParent().getLineNumber());
            LOG.error(
                "Expecting a space before pattern:SCHEMATYPENS_PATTERN_ONLY,lineNumber,label {},{},{}",
                SCHEMATYPENS_PATTERN_ONLY, xmlModel.getParent().getLineNumber(), label);
            labelProblems
                .addProblem(
                    new ValidationProblem(
                        new ProblemDefinition(ExceptionType.WARNING, ProblemType.BAD_SCHEMATYPENS,
                            "Invalid xml-model statement.  Expecting a space before pattern ["
                                + SCHEMATYPENS_PATTERN_ONLY + "] in " + filteredData),
                        label, -1, -1));
            break;
          }
        }
      } else {
        LOG.error("Could not find expected pattern [{}] in label {}", SCHEMATYPENS_PATTERN_ONLY,
            label);
        labelProblems.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
            ProblemType.BAD_SCHEMATYPENS, "Could not find expected pattern ["
                + SCHEMATYPENS_PATTERN_ONLY + "] in label " + label),
            label, -1, -1));
      }
    }

    LOG.debug("spaceCheckSchematypensPattern:label,labelProblems.getProblemCount() {},{}", label,
        labelProblems.getProblemCount());
  }

  private List<URL> getSchematrons(URL label, ProblemContainer labelProblems) throws Exception {
    List<URL> schematronRefs = new ArrayList<>();
    List<TinyNodeImpl> xmlModels = new ArrayList<>();
    try {
      XMLExtractor extractor = getExtractor(label);
      xmlModels = extractor.getNodesFromDoc(XMLExtractor.XML_MODEL_XPATH);
    } catch (Exception e) {
      throw new Exception("Error occurred while attempting to find schematrons using "
          + "the XPath '" + XMLExtractor.XML_MODEL_XPATH + "': " + e.getMessage());
    }
    Pattern pattern = Pattern.compile(Constants.SCHEMATRON_SCHEMATYPENS_PATTERN);

    LOG.debug("getSchematrons:afor:xmlModels.size() {}", xmlModels.size());

    // Add a space check before the pattern and put a WARNING message.

    spaceCheckSchematypensPattern(label, labelProblems, xmlModels);

    LOG.debug("getSchematrons:labelProblems.getWarningCount() {}", labelProblems.getWarningCount());
    LOG.debug("getSchematrons:after:xmlModels.size() {}", xmlModels.size());

    for (TinyNodeImpl xmlModel : xmlModels) {
      String filteredData = xmlModel.getStringValue().replaceAll("\\s+", " ");
      filteredData = filteredData.trim();
      LOG.debug("validateLabel:label,filteredData {},{}", label, filteredData);
      Matcher matcher = pattern.matcher(filteredData);
      if (matcher.matches()) {
        String value = matcher.group(1).trim();
        URL schematronRef = null;
        URL parent = Utility.getParent(label);
        LOG.debug("validateLabel:label,parent {},{}", label, parent);
        try {
          schematronRef = new URL(value);
          schematronRef =
              new URL(Utility.makeAbsolute(parent.toString(), schematronRef.toString()));
          LOG.debug("validateLabel:label,schematronRef {},{}", label, schematronRef);
        } catch (MalformedURLException ue) {
          // The schematron specification value does not appear to be
          // a URL. Assume a local reference to the schematron and
          // attempt to resolve it.
          try {
            schematronRef = new URL(parent, value);
          } catch (MalformedURLException mue) {
            labelProblems.addProblem(new ValidationProblem(
                new ProblemDefinition(ExceptionType.ERROR, ProblemType.SCHEMATRON_ERROR,
                    "Cannot resolve schematron specification '" + value + "': " + mue.getMessage()),
                label, xmlModel.getLineNumber(), -1));
            continue;
          }
        }
        schematronRefs.add(schematronRef);
      }
    }

    LOG.debug("validateLabel:label,schematronRefs.isEmpty() {},{}", label,
        schematronRefs.isEmpty());

    if (schematronRefs.isEmpty()) {
      labelProblems.addProblem(new ValidationProblem(new ProblemDefinition(ExceptionType.WARNING,
          ProblemType.MISSING_SCHEMATRON_SPEC, "No schematrons specified in the label"), label));
    }
    LOG.debug("validateLabel:label,labelProblems.getProblemCount() {},{}", label,
        labelProblems.getProblemCount());
    LOG.debug("validateLabel:label,schematronRefs {},{}", label, schematronRefs);
    // System.out.println("validateLabel:early#exit#0002");
    // System.exit(0);
    return schematronRefs;
  }

  private XMLExtractor getExtractor(URL label) throws XPathException, XPathExpressionException {
    if (extractor == null || !(label.toString().equals(extractor.getSystemId()))) {
      extractor = new XMLExtractor(label);
    }
    return extractor;
  }
}
