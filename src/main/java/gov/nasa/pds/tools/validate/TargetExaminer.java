//  Copyright 2009-2018, by the California Institute of Technology.
//  ALL RIGHTS RESERVED. United States Government Sponsorship acknowledged.
//  Any commercial use must be negotiated with the Office of Technology
//  Transfer at the California Institute of Technology.
//
//  This software is subject to U. S. export control laws and regulations
//  (22 C.F.R. 120-130 and 15 C.F.R. 730-774). To the extent that the software
//  is subject to U.S. export control laws and regulations, the recipient has
//  the responsibility to obtain export licenses or other export authority as
//  may be required before exporting such information to foreign countries or
//  providing access to foreign nationals.
//
//  $Id$
package gov.nasa.pds.tools.validate;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.sax.SAXSource;

import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;

import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.tree.tiny.TinyNodeImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

/**
 * Class to examine if a Target maches a certain product type, either a bundle or a collection.
 * 
 */
public class TargetExaminer extends Target {
	private static final Logger LOG = LoggerFactory.getLogger(TargetExaminer.class);
    private static String BUNDLE_NODE_TAG     = "Product_Bundle";
    private static String COLLECTION_NODE_TAG = "Product_Collection";
	/**
	 * Creates a new instance.
	 */
	public TargetExaminer(URL url, boolean isDir) { 
        super(url,isDir);
	}
	
    /**
     * Check if a given url is of Bundle type.
     * @param url the url of file to check.
     * @return true if the given url is of Bundle type.
     */
    public static boolean isTargetBundleType(URL url) {
        // Function returns true if the product has the tag defined in PRODUCT_NAME_TAG.
        String PRODUCT_NAME_TAG = TargetExaminer.BUNDLE_NODE_TAG;
        boolean targetIsBundleFlag = false;

        // Do a sanity check if the file or directory exist.
        if (! (new File(url.getPath()).exists())) {
            LOG.error("Provided url does not exist: {}",url);
            return(targetIsBundleFlag);
        }

        if ((new File(url.getPath()).isFile()) && (TargetExaminer.tagMatches(url,PRODUCT_NAME_TAG))) {
            targetIsBundleFlag = true;
        }
        LOG.debug("isTargetBundleType:url,(new File(url.getPath()).isFile() {},{}",url,(new File(url.getPath()).isFile()));
        LOG.debug("isTargetBundleType:url,PRODUCT_NAME_TAG,targetIsBundleFlag {},{},{}",url,PRODUCT_NAME_TAG,targetIsBundleFlag);

        return(targetIsBundleFlag);
    }

    /**
     * Check if a given url is of Collection type.
     * @param url the url of file to check.
     * @return true if the given url is of Collection type.
     */
    public static boolean isTargetCollectionType(URL url) {
        // Function returns true if the product has the tag defined in PRODUCT_NAME_TAG.
        String PRODUCT_NAME_TAG = TargetExaminer.COLLECTION_NODE_TAG;
        boolean targetIsCollectionFlag = false;

        // Do a sanity check if the file or directory exist.
        if (! (new File(url.getPath()).exists())) {
            LOG.error("Provided url does not exist: {}",url);
            return(targetIsCollectionFlag);
        }

        if ((new File(url.getPath()).isFile()) &&  (TargetExaminer.tagMatches(url,PRODUCT_NAME_TAG))) {
            targetIsCollectionFlag = true;
        }
        LOG.debug("isTargetCollectionType:url,(new File(url.getPath()).isFile() {},{}",url,(new File(url.getPath()).isFile()));
        LOG.debug("isTargetCollectionType:url,PRODUCT_NAME_TAG,targetIsCollectionFlag {},{},{}",url,PRODUCT_NAME_TAG,targetIsCollectionFlag);

        return(targetIsCollectionFlag);
    }

    /**
     * Check if content of url contains a node that matches tagCheck.
     * @param url the url of file to check.
     * @param tagCheck the tag of the node to check.
     * @return true if the given url contains a node that matches tagCheck.
     */
    private static boolean tagMatches(URL url, String tagCheck) {
        // Given a target URL, make an educated guess by checking if content of target contains a given tag.
        // A PDS4 label uses the tag to identify itself the first 10 lines, e.g.
        //  head /data/home/pds4/insight_cameras/bundle.xml
        //  <?xml version="1.0" encoding="UTF-8"?>
        //  <?xml-model href="https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1B10.sch"
        //    schematypens="http://purl.oclc.org/dsdl/schematron"?>
        //
        //  <Product_Bundle xmlns="http://pds.nasa.gov/pds4/pds/v1" xmlns:pds="http://pds.nasa.gov/pds4/pds/v1"

        LOG.debug("tagMatches:url,tagCheck {},{}",url,tagCheck);
        boolean tagMatchedFlag = false;

        try {
        	InputSource source = Utility.getInputSourceByURL(url);
            SAXSource saxSource = new SAXSource(source);
            saxSource.setSystemId(url.toString());
            DocumentInfo docInfo = LabelParser.parse(saxSource); // Parses a label.
            LOG.debug("tagMatches:docInfo {},{}",docInfo,docInfo.getClass());
            List<TinyNodeImpl> xmlModels = new ArrayList<TinyNodeImpl>();
            try {
                XMLExtractor extractor = new XMLExtractor(docInfo);
                xmlModels = extractor.getNodesFromDoc(tagCheck);
                LOG.debug("tagMatches:url,tagCheck,xmlModels.size() {},{},{}",url,tagCheck,xmlModels.size());
                if (xmlModels.size() > 0) {
                    tagMatchedFlag = true;  // If xmlModels has more than one element, the tag matches.
                    // Should only print debug if you really mean it.  Too busy for operation.
                    //for (TinyNodeImpl xmlModel : xmlModels) {
                    //    LOG.debug("xmlModel.getStringValue() {}",xmlModel.getStringValue());
                    //}
                }
            } catch (Exception e) {
                LOG.error("Exception encountered in tagMatches:url {},{}",url,e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Exception encountered in tagMatches:url {},{}",url,e.getMessage());
            e.printStackTrace();
        }

        LOG.debug("tagMatches:url,tagCheck,tagMatchedFlag {},{},{}",url,tagCheck,tagMatchedFlag);
        return(tagMatchedFlag);
    }

    /**
     * Return the content of the field within a node.
     * @param url the url of file to check.
     * @param nodeCheck the tag of the node to check.
     * @param fieldCheck the tag of the field within node to check.
     * @param fieldCheck2 additional tag of the field within node to check.
     * @return the content of the field within a node as string.
     */
    public static ArrayList<String> getTargetContent(URL url, String nodeCheck, String fieldCheck, String fieldCheck2) {
        // Given a target URL, read the content and return the content of the given fieldCheck(s) in a given nodeCheck.

        LOG.debug("getTargetContent:url,nodeCheck,fieldCheck {},{},{},{}",url,nodeCheck,fieldCheck,fieldCheck2);
        ArrayList<String> fieldContent = new ArrayList<String>();

        try {
        	InputSource source = Utility.getInputSourceByURL(url);
            SAXSource saxSource = new SAXSource(source);
            saxSource.setSystemId(url.toString());
            DocumentInfo docInfo = LabelParser.parse(saxSource); // Parses a label.
            LOG.debug("getTargetContent:docInfo {},{}",docInfo,docInfo.getClass());
            List<TinyNodeImpl> xmlModels = new ArrayList<TinyNodeImpl>();
            try {
                XMLExtractor extractor = new XMLExtractor(docInfo);
                xmlModels = extractor.getNodesFromDoc(nodeCheck);
                LOG.debug("getTargetContent:url,nodeCheck,xmlModels.size() {},{},{}",url,nodeCheck,xmlModels.size());
                if (xmlModels.size() > 0) {
                    for (TinyNodeImpl xmlModel : xmlModels) {
                        // It is possible that the tag we look for does not exist, function getValueFromItem will return an empty string.
                        if (!extractor.getValueFromItem(fieldCheck,xmlModel).equals("")) {
                            fieldContent.add(extractor.getValueFromItem(fieldCheck,xmlModel));
                        }
                        LOG.debug("getTargetContent:url,fieldCheck,getValueFromItem {},{},[{}]",url,fieldCheck,extractor.getValueFromItem(fieldCheck,xmlModel));
                        if (fieldCheck2 != null) {
                            if (!extractor.getValueFromItem(fieldCheck2,xmlModel).equals("")) {
                                fieldContent.add(extractor.getValueFromItem(fieldCheck2,xmlModel));
                            }
                        }
                        // We only need one value.
                        break;
                    }
                }
            } catch (Exception e) {
                LOG.error("Exception encountered in getTargetContent:url {},{}",url,e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            LOG.error("Exception encountered in getTargetContent:url {},{}",url,e.getMessage());
            e.printStackTrace();
        }

        LOG.debug("getTargetContent:url,nodeCheck,fieldCheck,fieldContent {},{},{},{}",url,nodeCheck,fieldCheck,fieldContent);

        return(fieldContent);
    }
}
