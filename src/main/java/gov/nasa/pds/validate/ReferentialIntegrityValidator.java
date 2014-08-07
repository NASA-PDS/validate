// Copyright 2006-2014, by the California Institute of Technology.
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
package gov.nasa.pds.validate;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.tinytree.TinyNodeImpl;
import gov.nasa.pds.tools.label.ExceptionContainer;
import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.label.LabelException;
import gov.nasa.pds.tools.label.validate.DocumentValidator;
import gov.nasa.pds.tools.util.XMLExtractor;
import gov.nasa.pds.validate.XPath.CoreXPaths;
import gov.nasa.pds.validate.crawler.Crawler;
import gov.nasa.pds.validate.crawler.CrawlerFactory;
import gov.nasa.pds.validate.inventory.reader.InventoryEntry;
import gov.nasa.pds.validate.inventory.reader.InventoryTableReader;
import gov.nasa.pds.validate.target.Target;
import gov.nasa.pds.validate.util.LidVid;

/**
 * Class to perform referential integrity on the given document.
 *
 * @author mcayanan
 *
 */
public class ReferentialIntegrityValidator implements DocumentValidator {
  /**
   * Intended to contain a mapping of all members of a Bundle within a
   * given target.
   */
  private Map<URL, List<LidVid>> bundleMembers;

  /**
   * Intended to contain a mapping of all members of a Collection within
   * a given target.
   */
  private Map<URL, List<LidVid>> collectionMembers;

  /** Intended to containin a mapping of all LIDVIDs within a given target. */
  private Map<URL, LidVid> lidVidReferences;

  public ReferentialIntegrityValidator() {
    this.bundleMembers = new HashMap<URL, List<LidVid>>();
    this.collectionMembers = new HashMap<URL, List<LidVid>>();
    this.lidVidReferences = new HashMap<URL, LidVid>();
  }

  @Override
  public boolean validate(ExceptionContainer container, DocumentInfo xml) {
    boolean passFlag = true;
    try {
      XMLExtractor extractor = new XMLExtractor(xml);
      String productClass = extractor.getValueFromDoc(CoreXPaths.PRODUCT_CLASS);
      if (productClass == null) {
        throw new Exception(
            "Cannot find product_class element using the following XPath: "
                + CoreXPaths.PRODUCT_CLASS);
      }
      LidVid target = lidVidReferences.get(new URL(xml.getSystemId()));

      if ("Product_Bundle".equalsIgnoreCase(productClass)) {
        // Check for missing and duplicate members
        List<LidVid> bundleMembers = this.bundleMembers.get(
            new URL(xml.getSystemId()));
        if (bundleMembers != null) {
          for (LidVid bundleMember : bundleMembers) {
            List<URL> matchingMembers = new ArrayList<URL>();
            for (Map.Entry<URL, LidVid> entry : lidVidReferences.entrySet()) {
              if (entry.getValue().getLid().equals(bundleMember.getLid())) {
                matchingMembers.add(entry.getKey());
              }
            }
            if (matchingMembers.isEmpty()) {
              container.addException(new LabelException(ExceptionType.WARNING,
                  "The member '" + bundleMember + "' could not be found in "
                      + "any product within the given target.",
                  xml.getSystemId(),
                  xml.getSystemId(),
                  null,
                  null)
               );
            } else if (matchingMembers.size() == 1) {
              container.addException(new LabelException(ExceptionType.INFO,
                  "The member '" + bundleMember + "' is referenced in "
                      + "the following product: " + matchingMembers.get(0),
                  xml.getSystemId(),
                  xml.getSystemId(),
                  null,
                  null)
               );
            } else if (matchingMembers.size() > 1) {
              ExceptionType exceptionType = ExceptionType.ERROR;
              if (!bundleMember.hasVersion()) {
                exceptionType = ExceptionType.WARNING;
              }
              container.addException(new LabelException(exceptionType,
                  "The member '" + bundleMember + "' is referenced in "
                      + "multiple products: " + matchingMembers.toString(),
                  xml.getSystemId(),
                  xml.getSystemId(),
                  null,
                  null)
               );
            }
          }
        }
      } else if ("Product_Collection".equalsIgnoreCase(productClass)) {
        // If this is a Product_Collection product, check to see if it is a
        // member of a Bundle
        boolean found = false;
        for (Map.Entry<URL, List<LidVid>> entry : this.bundleMembers.entrySet()) {
          for (LidVid bundleMember : entry.getValue()) {
            if (bundleMember.getLid().equals(target.getLid())) {
              found = true;
              container.addException(new LabelException(ExceptionType.INFO,
                  "The lidvid '" + target.toString()
                  + "' is a member of the following bundle: " + entry.getKey(),
                  xml.getSystemId(), xml.getSystemId(), null, null));
              break;
            }
          }
        }
        if (!found && !this.bundleMembers.isEmpty()) {
          container.addException(new LabelException(ExceptionType.WARNING,
              "The lidvid '" + target.toString() + "' is not a member of "
                  + "any bundle within the given target.",
              xml.getSystemId(),
              xml.getSystemId(),
              null,
              null)
            );
        }
        // Check for missing members of a Collection. Also check to see if
        // multiple LIDs/LIDVIDs are referencing the same member.
        List<LidVid> collectionMembers = this.collectionMembers.get(
            new URL(xml.getSystemId()));
        if (collectionMembers != null) {
          for (LidVid collectionMember : collectionMembers) {
            List<URL> matchingMembers = new ArrayList<URL>();
            for (Map.Entry<URL, LidVid> entry : lidVidReferences.entrySet()) {
              if (entry.getValue().getLid().equals(collectionMember.getLid())) {
                matchingMembers.add(entry.getKey());
              }
            }
            if (matchingMembers.isEmpty()) {
              container.addException(new LabelException(ExceptionType.WARNING,
                  "The member '" + collectionMember + "' could not be found in "
                      + "any product within the given target.",
                  xml.getSystemId(),
                  xml.getSystemId(),
                  null,
                  null)
               );
            } else if (matchingMembers.size() == 1) {
              container.addException(new LabelException(ExceptionType.INFO,
                  "The member '" + collectionMember + "' is referenced in "
                      + "the following product: " + matchingMembers.get(0),
                  xml.getSystemId(),
                  xml.getSystemId(),
                  null,
                  null)
               );
            } else if (matchingMembers.size() > 1) {
              ExceptionType exceptionType = ExceptionType.ERROR;
              if (!collectionMember.hasVersion()) {
                exceptionType = ExceptionType.WARNING;
              }
              container.addException(new LabelException(exceptionType,
                  "The member '" + collectionMember + "' is referenced in "
                      + "multiple products: " + matchingMembers.toString(),
                  xml.getSystemId(),
                  xml.getSystemId(),
                  null,
                  null)
               );
            }
          }
        }
      } else {
        // Assume that the product is supposed to be a member of some
        // collection.
        boolean found = false;
        for (Map.Entry<URL, List<LidVid>> entry : this.collectionMembers.entrySet()) {
          for (LidVid collectionMember : entry.getValue()) {
            if (collectionMember.getLid().equals(target.getLid())) {
              found = true;
              container.addException(new LabelException(ExceptionType.INFO,
                  "The lidvid '" + target.toString()
                  + "' is a member of the following collection: "
                  + entry.getKey(),
                  xml.getSystemId(), xml.getSystemId(), null, null));
              break;
            }
          }
        }
        if (!found && !this.collectionMembers.isEmpty()) {
          container.addException(new LabelException(ExceptionType.WARNING,
              "The lidvid '" + target.toString() + "' is not a member of "
                  + "any collection within the given target.",
              xml.getSystemId(),
              xml.getSystemId(),
              null,
              null)
            );
        }
      }
    } catch (Exception e) {
      container.addException(new LabelException(ExceptionType.FATAL,
          "Error occurred while performing referential integrity check: "
              + e.getMessage(),
          xml.getSystemId(),
          xml.getSystemId(),
          null,
          null)
      );
      passFlag = false;
    }
    return passFlag;
  }

  public void clearSources() {
    this.bundleMembers.clear();
    this.collectionMembers.clear();
    this.lidVidReferences.clear();
  }

  public void setSources(Target target, boolean traverse, List<String> regExps)
      throws IOException, Exception {
    if (target.isDir()) {
      Crawler crawler = CrawlerFactory.newInstance(target.getUrl(), traverse,
          regExps);
      setSources(crawler.crawl(target.getUrl()), traverse, regExps);
    } else {
      try {
        getReferences(target.getUrl());
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
  }

  private void setSources(List<Target> targets, boolean traverse,
      List<String> regExps) throws Exception {
    for (Target target : targets) {
      if (target.isDir()) {
        Crawler crawler = CrawlerFactory.newInstance(target.getUrl(), traverse,
            regExps);
        setSources(crawler.crawl(target.getUrl()), traverse, regExps);
      } else {
        try {
          getReferences(target.getUrl());
        } catch (Exception e) {
          throw new Exception ("Error occurred while getting references from '"
              + target.getUrl() + "': " + e.getMessage());
        }
      }
    }
  }

  private void getReferences(URL target) throws Exception {
    List<LidVid> lidVids = new ArrayList<LidVid>();
    XMLExtractor extractor = new XMLExtractor(target);
    String productClass = extractor.getValueFromDoc(CoreXPaths.PRODUCT_CLASS);
    String lid = extractor.getValueFromDoc(CoreXPaths.LOGICAL_IDENTIFIER);
    String version = extractor.getValueFromDoc(CoreXPaths.VERSION_ID);
    if (lid == null) {
      throw new Exception(
          "Cannot find logical_identifier element using the following XPath: "
              + CoreXPaths.LOGICAL_IDENTIFIER);
    }
    if (version == null) {
      throw new Exception(
          "Cannot find version_id element using the following XPath: "
              + CoreXPaths.VERSION_ID);
    }
    if (productClass == null) {
      throw new Exception("Cannot find the 'product_class' element.");
    }
    lidVidReferences.put(target, new LidVid(lid, version));
    if ("Product_Bundle".equalsIgnoreCase(productClass)) {
      List<TinyNodeImpl> nodes = extractor.getNodesFromDoc(
          CoreXPaths.BUNDLE_MEMBER_ENTRY);
      for (TinyNodeImpl node : nodes) {
        String reference = extractor.getValueFromItem(
            CoreXPaths.IDENTITY_REFERENCE,
            node);
        LidVid lidVid = parseIdentifier(reference);
        lidVids.add(lidVid);
      }
      if (!lidVids.isEmpty()) {
        this.bundleMembers.put(target, lidVids);
      }
    } else if ("Product_Collection".equalsIgnoreCase(productClass)) {
      InventoryTableReader reader = new InventoryTableReader(target);
      for (InventoryEntry entry = new InventoryEntry(); entry != null;) {
        if (!entry.isEmpty()) {
          String identifier = entry.getIdentifier();
          if (!identifier.equals("")) {
            //Check for a LID or LIDVID
            LidVid lidVid = parseIdentifier(identifier);
            lidVids.add(lidVid);
          }
        }
        entry = reader.getNext();
      }
      if (!lidVids.isEmpty()) {
        this.collectionMembers.put(target, lidVids);
      }
    }
  }

  private LidVid parseIdentifier(String identifier) {
    if (identifier.indexOf("::") != -1) {
      return new LidVid(identifier.split("::")[0],
          identifier.split("::")[1]);
    } else {
      return new LidVid(identifier.split("::")[0]);
    }
  }
}
