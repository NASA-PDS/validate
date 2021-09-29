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
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.transform.sax.SAXSource;

import gov.nasa.pds.tools.label.ExceptionType;

import gov.nasa.pds.tools.util.FlagsUtil;
import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.LidVid;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;

import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.TargetExaminer;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.CrawlerFactory;
import gov.nasa.pds.tools.validate.ValidateProblemHandler;
import gov.nasa.pds.tools.validate.ValidationProblem;

import gov.nasa.pds.validate.report.Report;

import gov.nasa.pds.validate.constants.Constants; 

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provide ways to get latest version of bundle/collection files, or build list of files to ignore
 * and make exception of when a bundle is not applicable for target as a file.
 * 
 */

public class BundleManager {
	private static final Logger LOG = LoggerFactory.getLogger(BundleManager.class);
    private static final Pattern COLLECTION_LABEL_PATTERN = Constants.COLLECTION_LABEL_PATTERN; // Ease the requirement to have an underscore after 'collection'.
    private static final Pattern BUNDLE_LABEL_PATTERN     = Constants.BUNDLE_LABEL_PATTERN;     // Ease the requirement to have an underscore after 'bundle'.
    public static final String BUNDLE_NAME_TOKEN     = Constants.BUNDLE_NAME_TOKEN;
    public static final String LABEL_EXTENSION       = Constants.LABEL_EXTENSION;   // Used to look for label files.  Note that the extension does not contain the dot.
    public static final String[] LABEL_EXTENSIONS_LIST = new String[1]; 

    private static final String PRODUCT_BUNDLE_ID_AREA_TAG      = "Product_Bundle/Identification_Area";
    private static final String PRODUCT_COLLECTION_ID_AREA_TAG  = "Product_Collection/Identification_Area";
    private static final String PRODUCT_BUNDLE_MEMBER_ENTRY_TAG = "Product_Bundle/Bundle_Member_Entry";
    private static final String LOGICAL_IDENTIFIER_TAG     = "logical_identifier";
    private static final String VERSION_ID_TAG             = "version_id";
    private static final String LIDVID_REFERENCE_TAG       = "lidvid_reference";
    private static final String LID_REFERENCE_TAG          = "lid_reference";
    private static final String REFERENCE_TYPE_TAG         = "reference_type";

    private static ArrayList<Target> m_ignoreList = new ArrayList<Target>();
    private static String m_location = null;
    private static Target m_latestBundle = null;
    private static Report m_report = null; 

    /**
     * Set the report object.
     * @param report The Report object.
     * @return None 
     */

    public static void setReport(Report report) {
        BundleManager.m_report = report;
    }

    /**
     * Returns the modified location.
     */

	public static String getLocation() { 
        return(m_location);
    }

    /**
     * Returns the target containing the latest bundle (one with the largest version).
     */
	public static Target getLatestBundle() { 
        return(m_latestBundle);
    }

    /**
     * Returns the list of files to ignore when crawling.
     */
	public static ArrayList<Target> getIgnoreList() { 
        return(BundleManager.m_ignoreList);
    }

    /**
     * Find bundle(s) with the latest version.
     * @param url the url of where to start looking for files from.
     * @return a list of files with latest version.
     */
    public static List<Target> findBundleWithLatestVersion(URL url) {
        List<Target> children = new ArrayList<Target>();
        try {
            IOFileFilter regexFileFilter = new RegexFileFilter(BUNDLE_LABEL_PATTERN);
            Crawler crawler = CrawlerFactory.newInstance(url);
            children = crawler.crawl(url, regexFileFilter);

            LOG.debug("findBundleWithLatestVersion:afor:reduceToLatestTargetOnly:children.size(),url {},{}",children.size(),url);

            // Purge all children unless they are the latest (largest version).
            // After this next statement, there should only be one element in children list.

            children = LidVid.reduceToLatestTargetOnly(children);

            LOG.debug("findBundleWithLatestVersion:after:reduceToLatestTargetOnly:children.size(),url {},{}",children.size(),url);
        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findBundleWithLatestVersion:children.size() {}",children.size());

        return(children);
    }

    /**
     * Find collection(s) with the latest version.
     * @param url the url of where to start looking for files from.
     * @return a list of files with latest version.
     */
    public static List<Target> findCollectionWithLatestVersion(URL url) {
        List<Target> children = new ArrayList<Target>();
        try {
            IOFileFilter regexFileFilter = new RegexFileFilter(COLLECTION_LABEL_PATTERN);
            Crawler crawler = CrawlerFactory.newInstance(url);
            List<Target> dirs = new ArrayList<Target>();
            dirs = crawler.crawl(url, true);
            LOG.debug("findCollectionWithLatestVersion: url,dirs.size() {},{}",url,dirs.size());
            LOG.debug("findCollectionWithLatestVersion: url,dirs {},{}",url,dirs);
            List<Target> kids = new ArrayList<Target>();
            // For each sub directory found, get all collection files.
            for (Target dir : dirs) {
                if (dir.isDir()) {
                    //kids = crawler.crawl(dir.getUrl(), regexFileFilter); 
                    // Note: For some strange reason, the crawler goes into an infinite loop using the above call
                    //       so we will use an alternate call to get the list of files.
                    LABEL_EXTENSIONS_LIST[0] = LABEL_EXTENSION;
                    kids = crawler.crawl(dir.getUrl(), LABEL_EXTENSIONS_LIST, false, Constants.COLLECTION_NAME_TOKEN);
                    LOG.debug("findCollectionWithLatestVersion: dir.getUrl(),kids {},{}",dir.getUrl(),kids);
                    children.addAll(kids);
                }
            }
            LOG.debug("findCollectionWithLatestVersion:children {}",children);
            LOG.debug("findCollectionWithLatestVersion:children.size() {}",children.size());

            // Purge all children unless they are the latest (largest version).
            // After this next statement, there should only be one element in children list.
            children = LidVid.reduceToLatestTargetOnly(children);
            LOG.debug("findCollectionWithLatestVersion:after:reduceToLatestTargetOnly:children.size() {}",children.size());
        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findCollectionWithLatestVersion:children {}",children);
        LOG.debug("findCollectionWithLatestVersion:children.size() {}",children.size());

        return(children);
    }

    private static List<Target> selectMatchingReferenceFromCollection(List<Target> collectionList, List<String> bundleLidList, List<String> bundleIdList) {
        // Purge all children unless they are the version referred to by the bundle.
        // After these statements, there should only be one element in childrenSelected list.
        List<Target> childrenSelected = new ArrayList<Target>();
        String collectionReferenceToMatch = bundleLidList.get(0);
        String collectionVersionToMatch   = null;
        // If the reference contains "::" get it.
        if (collectionReferenceToMatch.indexOf("::") >= 0) {
            collectionVersionToMatch = collectionReferenceToMatch.split("::")[1];
            collectionReferenceToMatch = bundleLidList.get(0).split("::")[0];
        } else {
            collectionVersionToMatch   = bundleIdList.get(1);
        }

        // Purge all collectionList unless they are the version referred to by the bundle.
        // After these statements, there should only be one element in childrenSelected list.
        // Look through all collectionList for matching reference.  Only keep the child with matching reference.
        for (Target target : collectionList) {
            // Check if target is a directory and ignore it since it cannot be inspect to be a collection or not.
            if (Utility.isDir(target.getUrl())) {
                LOG.debug("selectMatchingReferenceFromCollection:IGNORING_DIRECTORY: target.getUrl() {}",target.getUrl());
                continue;
            }
            ArrayList<String> collectionIdList = TargetExaminer.getTargetContent(target.getUrl(),PRODUCT_COLLECTION_ID_AREA_TAG,LOGICAL_IDENTIFIER_TAG,VERSION_ID_TAG);
            // If the reference and version id matches, keep it.
            // Note that the first element has to be split using "::" in case it does contain it.
            LOG.debug("selectMatchingReferenceFromCollection:collectionIdList {} {}",collectionIdList,collectionIdList.size());
            LOG.debug("selectMatchingReferenceFromCollection:target.getUrl() collectionReferenceToMatch {},{}",target.getUrl(), collectionReferenceToMatch);
            LOG.debug("selectMatchingReferenceFromCollection:target.getUrl() collectionVersionToMatch   {},{}",target.getUrl(), collectionVersionToMatch);

            // Because the size returned of collectionIdList is not guaranteed to have at least 2 elements, make the check to
            // avoid the java.lang.IndexOutOfBoundsException violation.
            //if (2 == 3) {
            if (collectionIdList.size() < 2) {
                LOG.warn("selectMatchingReferenceFromCollection:Expecting at least 2 elements from collectionIdList: target.getUrl() {}",target.getUrl());
            } else {
                LOG.debug("selectMatchingReferenceFromCollection:target.getUrl() comparing reference {} {} {}",target.getUrl(),collectionIdList.get(0), collectionReferenceToMatch);
                LOG.debug("selectMatchingReferenceFromCollection:target.getUrl() comparing version   {} {} {}",target.getUrl(),collectionIdList.get(1), collectionVersionToMatch);
                LOG.debug("selectMatchingReferenceFromCollection:collectionIdList.get(0).split('::')[0],collectionReferenceToMatch [{}] [{}]",collectionIdList.get(0).split("::")[0],collectionReferenceToMatch);
                LOG.debug("selectMatchingReferenceFromCollection:collectionIdList.get(1),collectionVersionToMatch {} {}",collectionIdList.get(1),collectionVersionToMatch);
                // Per documentation, use split(String regexp, int limit) to avoid surprising behavior 
                if (collectionIdList.get(0).split("::",2)[0].equals(collectionReferenceToMatch) && collectionIdList.get(1).equals(collectionVersionToMatch)) {
                    childrenSelected.add(target);
                    LOG.debug("selectMatchingReferenceFromCollection:ADD_TARGET {}",target);
                }
            }
        }
        return(childrenSelected);
     }

    /**
     * Find collection(s) with matching reference.
     * @param url the url of where to start looking for files from.
     * @return a list of files with matching reference.
     */
    public static List<Target> findCollectionWithMatchingReference(URL url, URL bundleUrl) {
        List<Target> children = new ArrayList<Target>();
        try {
            IOFileFilter regexFileFilter = new RegexFileFilter(COLLECTION_LABEL_PATTERN);
            Crawler crawler = CrawlerFactory.newInstance(url);
            List<Target> dirs = new ArrayList<Target>();
            dirs = crawler.crawl(url, true);
            LOG.debug("findCollectionWithMatchingReference: url,dirs.size() {},{}",url,dirs.size());
            LOG.debug("findCollectionWithMatchingReference: url,dirs {},{}",url,dirs);
            List<Target> kids = new ArrayList<Target>();
            // For each sub directory found, get all collection files.
            for (Target dir : dirs) {
                if (dir.isDir()) {
                    //kids = crawler.crawl(dir.getUrl(), regexFileFilter);
                    // Note: For some strange reason, the crawler goes into an infinite loop using the above call
                    //       so we will use an alternate call to get the list of files.
                    LABEL_EXTENSIONS_LIST[0] = LABEL_EXTENSION;
                    kids = crawler.crawl(dir.getUrl(), LABEL_EXTENSIONS_LIST, false, Constants.COLLECTION_NAME_TOKEN);
                    LOG.debug("findCollectionWithMatchingReference: dir.getUrl(),kids {},{}",dir.getUrl(),kids);
                    children.addAll(kids);
                }
            }
            // At this point, the value of children is a list of collection labels found from crawling.
            // It still need to be trimmmed to contain the list of collections explicitly referenced by the bundle.
            LOG.debug("findCollectionWithMatchingReference:children {}",children);
            LOG.debug("findCollectionWithMatchingReference:children.size() {}",children.size());

            boolean bundleReferToCollectionViaLidvidFlag = true;  //  Flag is true if bundle using LIDVID_REFERENCE_TAG to refer to collection, false if otherwise.
            ArrayList<String> bundleIdList  = TargetExaminer.getTargetContent(bundleUrl,PRODUCT_BUNDLE_ID_AREA_TAG,LOGICAL_IDENTIFIER_TAG,VERSION_ID_TAG);
            // Attempt to fetch the LIDVID_REFERENCE_TAG only.  The last parameter is null on purpose.
            ArrayList<String> bundleLidList = TargetExaminer.getTargetContent(bundleUrl,PRODUCT_BUNDLE_MEMBER_ENTRY_TAG,LIDVID_REFERENCE_TAG,null);

            // If the size of bundleLidList is zero, make another effort to use LID_REFERENCE_TAG to get to the collection reference.
            LOG.debug("findCollectionWithMatchingReference:bundleLidList.size(),bundleUrl.toString() {},{}",bundleLidList.size(),bundleUrl.toString());
            if (bundleLidList.size() == 0) {
                LOG.info("findCollectionWithMatchingReference:The bundle {} does not refer to collection using '{}', will fetch using '{}'",bundleUrl.toString(),LIDVID_REFERENCE_TAG,LID_REFERENCE_TAG);
                bundleLidList = TargetExaminer.getTargetContent(bundleUrl,PRODUCT_BUNDLE_MEMBER_ENTRY_TAG,LID_REFERENCE_TAG,null);
                bundleReferToCollectionViaLidvidFlag = false;
                // Do a sanity check now that we have checked using both LIDVID_REFERENCE_TAG and LID_REFERENCE_TAG, there should be one element.
                if (bundleLidList.size() == 0) {
                    LOG.error("Failed to find neither either {} or {} tags from target {}",LIDVID_REFERENCE_TAG,LID_REFERENCE_TAG,bundleUrl);
                    throw new IOException("Failed to find lidvid or lid reference tags from bundleUrl " + bundleUrl.toString());
                }
            }

            LOG.debug("findCollectionWithMatchingReference:bundleIdList,bundleUrl.toString() {},{}",bundleIdList,bundleUrl.toString());
            LOG.debug("findCollectionWithMatchingReference:bundleLidList,bundleUrl.toString() {},{}",bundleLidList,bundleUrl.toString());
            LOG.debug("findCollectionWithMatchingReference:bundleReferToCollectionViaLidvidFlag,bundleUrl.toString() {},{}",bundleReferToCollectionViaLidvidFlag,bundleUrl.toString());

            // If bundleReferToCollectionViaLidvidFlag is not true, a specific collection cannot be found since no version of collection
            // is specified by the bundle, the largest (latest) collection must be now look for.
            if (!bundleReferToCollectionViaLidvidFlag) {
                children = BundleManager.findCollectionWithLatestVersion(Utility.getParent(bundleUrl));
            } else {
                // Get the list of collections that were explicitly referenced by the bundle's tags: lidvid_reference or lid_reference
                children = BundleManager.selectMatchingReferenceFromCollection(children,bundleLidList,bundleIdList);
            }

            // Do a sanity check on the list of collection file selected from the reference in bundle and report the error.
            if (children.size() == 0) {
                LOG.error("Could not find any collection from bundle {}",bundleUrl.toString());
            } else {
                LOG.debug("BUNDLE_COLLECTION_SELECTED {},{}",bundleUrl.toString(),children.get(0));
            }

            // At this point, the value of children is a list of collection labels explicitly referenced by the bundle.
            // As an example, for the below lidvid_reference tag in the bundle label:
            //
            //     <lidvid_reference>urn:nasa:pds:kaguya_grs_spectra:data_spectra::1.0</lidvid_reference>
            //
            // Only collection that contains the logical_identifier "urn:nasa:pds:kaguya_grs_spectra:data_spectra" and version_id "1.0" will be selected.
            // So if the label containing the following logical_identifier and version_id will be selected:
            //     <logical_identifier>urn:nasa:pds:kaguya_grs_spectra:data_spectra</logical_identifier>
            //     <version_id>1.0</version_id>

        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findCollectionWithMatchingReference:children {}",children);
        LOG.debug("findCollectionWithMatchingReference:children.size() {}",children.size());

        return(children);
    }

    private static ArrayList<Target> getCollectionFilesWithSameLogicalIdentifier(List<Target> latestCollectionList, List<Target> otherCollectionList) {
        // Given a list of targets containing collection labels and a list of other collection targets, returns the list of Targets
        // that do share the same logical_identifier as the provided targets in latestCollectionList.
        // Function will fetch the logical_identifier from the list of targets in latestCollectionList and check the logical_identifier against the list of provided collection files.
        // If the logical_identifier in each of the collection list is the same, add it to the "trimmed" lists of collection files.
        // If it is different, ignore it because the collection does not belong to the bundle.
        //
        // As an example, if the logical_identifier tag of one of the target in latestCollectionList is:
        //
        //     <logical_identifier>urn:nasa:pds:kaguya_grs_spectra:data_spectra</logical_identifier>
        //
        // This function will look for all collection with the same logical_idenfitier value "urn:nasa:pds:kaguya_grs_spectra:data_spectra" and save it because
        // there is already one collection containing that identifier and there is no need to select the same collection id.
        // If the logical_identifier is different, say:
        //
        //     <logical_identifier>urn:nasa:pds:insight_documents:document_rise</logical_identifier>
        //
        // then that collection label will not be added to the "trimmed" collection list.

        List<Target> trimmedCollectionList = new ArrayList<Target>();

        LOG.debug("getCollectionFilesWithSameLogicalIdentifier:latestCollectionList {}",latestCollectionList);
        LOG.debug("getCollectionFilesWithSameLogicalIdentifier:otherCollectionList {}",otherCollectionList);

        ArrayList<String> collectionIdList = new ArrayList<String>(0);

        // Loop through latestCollectionList to collect all the logical_identifiers to collectionIdList
        for (Target target : latestCollectionList) {
            ArrayList<String> latestIdList = TargetExaminer.getTargetContent(target.getUrl(),PRODUCT_COLLECTION_ID_AREA_TAG,LOGICAL_IDENTIFIER_TAG,VERSION_ID_TAG);
            // It is possible that the file does not contain the logical_identifier tag so null-ness must be checked.
            if (latestIdList != null && latestIdList.size() > 0) {
                collectionIdList.add(latestIdList.get(0)); // Save the logical_identifier (with index 0.  Index 1 is the version_id which we don't need)
            }
        }

        LOG.debug("getCollectionFilesWithSameLogicalIdentifier:collectionIdList {}",collectionIdList);
        for (Target target : otherCollectionList) {
            ArrayList<String> otherCollectionIdList = TargetExaminer.getTargetContent(target.getUrl(),PRODUCT_COLLECTION_ID_AREA_TAG,LOGICAL_IDENTIFIER_TAG,VERSION_ID_TAG);
            LOG.debug("getCollectionFilesWithSameLogicalIdentifier:collectionIdList2 {}",otherCollectionIdList);
            // The check for null-ness and size should be done for the same variable otherCollectionIdList
            if (otherCollectionIdList  != null && otherCollectionIdList.size() >= 1) {
                // Loop through all logical_identifier in collectionIdList and compare with otherCollectionIdList
                // If the logical_identifier matches the logical_identifier from the latestCollectionList, add it to trimmedCollectionList.
                for (String latestCollectionId : collectionIdList) {
                    if (latestCollectionId.equals(otherCollectionIdList.get(0))) {
                        trimmedCollectionList.add(target);
                        LOG.debug("getCollectionFilesWithSameLogicalIdentifier:COLLECTION_FILE_MATCHES_TRUE:logical_identifier,logical_identifier_2,target {},{},{}",latestCollectionId,otherCollectionIdList.get(0),target.getUrl());
                    } else {
                        LOG.debug("getCollectionFilesWithSameLogicalIdentifier:COLLECTION_FILE_MATCHES_FALSE:logical_identifier,logical_identifier_2,target {},{},{}",latestCollectionId,otherCollectionIdList.get(0),target.getUrl());
                    }
                }
            }
        }

        // Return a list of targets whose logical_identifier matches those in latestCollectionList.
        // This list can then be used by the crawler to ignore these collection files since they do not belong to the bundle.
        LOG.debug("getCollectionFilesWithSameLogicalIdentifier:trimmedCollectionList.size() {}",trimmedCollectionList.size());
        return((ArrayList)trimmedCollectionList);
    }   


    /**
     * Build a list of bundle files to ignore.
     * @param url the url of where to start looking for files from.
     * @return a list of files that are other than the given url.
     */
    public static ArrayList<Target> buildBundleIgnoreList(URL url) {
        List<Target> ignoreBundleList = new ArrayList<Target>();  // List of items to be removed from result of crawl() function.
        List<Target> latestBundles = BundleManager.findBundleWithLatestVersion(url);
        LOG.debug("buildBundleIgnoreList:latestBundles.size() ",latestBundles.size());
        LOG.debug("buildBundleIgnoreList:latestBundles {}",latestBundles);
        if (latestBundles.size() > 0)  {
            m_latestBundle = latestBundles.get(0);   //  Save this bundle for reference later on.
            LOG.debug("buildBundleIgnoreList:latestBundles[0] {}",latestBundles.get(0).getUrl());
            ignoreBundleList = BundleManager.findOtherBundleFiles(latestBundles.get(0).getUrl());
        }
        LOG.debug("buildBundleIgnoreList:ignoreBundleList {}",ignoreBundleList);
        LOG.debug("buildBundleIgnoreList:ignoreBundleList.size() {}",ignoreBundleList.size());

        // This function has knowledge of why a bundle file is ignored.  Write to the report here.
        for (Target target : ignoreBundleList) {
            LOG.info("buildBundleIgnoreList:SKIP: {} due to not being selected as the bundle target",target.getUrl());
            // Write a record to report that we are skipping this file.
            if (BundleManager.m_report != null) {
                try {
                    ValidationProblem p1 = new ValidationProblem(new ProblemDefinition(ExceptionType.INFO,
                                                                     ProblemType.UNREFERENCED_FILE, "Skipping " + target.getUrl() + " due to version not latest version"),
                                                                 target.getUrl());
                    BundleManager.m_report.recordSkip(new URI(target.getUrl().toString()), p1);
                } catch (Exception e) {
                    LOG.error("buildBundleIgnoreList:Cannot build ValidationProblem object or report skip file: {}",target.getUrl());
                }
            } else {
                LOG.warn("buildBundleIgnoreList:Object BundleManager.m_report is null");
            }
        }

        return((ArrayList)ignoreBundleList);
    }

    /**
     * Build a list of collection files to ignore.
     * @param url the url of where to start looking for files from.
     * @return a list of files that are other than the given url.
     */
    public static ArrayList<Target> buildCollectionIgnoreList(URL url, URL bundleUrl) {
        LOG.debug("buildCollectionIgnoreList:url,bundleUrl {},{}",url,bundleUrl);
        List<Target> ignoreCollectionList = new ArrayList<Target>();  // List of items to be removed from result of crawl() function.
        try {
            if (url.getProtocol().equals("file")) {
                if (!(new File(url.getFile())).isDirectory()) {
                    LOG.error("This function buildCollectionIgnoreList() only work on directory input: " + url);
                    return((ArrayList)ignoreCollectionList);
                }
            }
        } catch (Exception e) {
            LOG.error("Cannot build File for url " + url );
            return((ArrayList)ignoreCollectionList);
        }

        List<Target> latestCollections = BundleManager.findCollectionWithMatchingReference(url,bundleUrl);

        LOG.debug("buildCollectionIgnoreList:latestCollections.size() {}",latestCollections.size());
        LOG.debug("buildCollectionIgnoreList:latestCollections {}",latestCollections);

        if (latestCollections.size() > 0)  {
            LOG.debug("buildCollectionIgnoreList:latestCollections[0] {}",latestCollections.get(0).getUrl());

            // The latest collections can be a list of collections, not just the first element.
            List<Target> otherCollectionFiles = BundleManager.findOtherCollectionFiles(latestCollections);

            LOG.debug("buildCollectionIgnoreList:otherCollectionFiles.size {}",otherCollectionFiles.size());
            LOG.debug("buildCollectionIgnoreList:otherCollectionFiles {}",otherCollectionFiles);

            // From the list of other collection files trim it down to the list of collections
            // that share the same logical_identifier with the ones in latestCollections.
            // These collection will be the ones the crawler will ignore while crawling.
            ignoreCollectionList = BundleManager.getCollectionFilesWithSameLogicalIdentifier(latestCollections,otherCollectionFiles);
        }

        LOG.debug("buildCollectionIgnoreList:ignoreCollectionList.size() {}",ignoreCollectionList.size());
        LOG.debug("buildCollectionIgnoreList:ignoreCollectionList {}",ignoreCollectionList);

        return((ArrayList)ignoreCollectionList);
    }

    /**
     * Find other bundle file(s).
     * @param url the url of where to start looking for files from.
     * @return a list of files that are other than the given url.
     */
    public static ArrayList<Target> findOtherBundleFiles(URL url) {
        // Given a url containing the given bundle, crawl parent directory and look for all the other
        // bundle files (which are just .xml files).
        // The return list can be an empty list if no other names are found.

        ArrayList<Target> otherBundleFilesList = new ArrayList<Target>();

        LOG.debug("findOtherBundleFiles:url:" + url);

        List<Target> allFiles = new ArrayList<Target>();//null;

        try {
            // Get the parent directory of url and crawl for files that starts with 'bundle'
            String dirName     = (new File(url.getPath())).getParent();
            LOG.debug("findOtherBundleFiles:dirName {}",dirName);
            IOFileFilter regexFileFilter = new RegexFileFilter(BUNDLE_LABEL_PATTERN);
            Crawler crawler = CrawlerFactory.newInstance(new File(dirName).toURI().toURL());

            LOG.debug("findOtherBundleFiles:crawler {}",crawler);

            //allFiles = crawler.crawl(new File(dirName).toURI().toURL(),regexFileFilter);
            // Note: For some strange reason, the crawler goes into an infinite loop using the above call
            //       so we will use an alternate call to get the list of files.
            LABEL_EXTENSIONS_LIST[0] = LABEL_EXTENSION;
            allFiles = crawler.crawl(new File(dirName).toURI().toURL(), LABEL_EXTENSIONS_LIST, false, BUNDLE_NAME_TOKEN);

            for (Target target : allFiles) {
                LOG.debug("findOtherBundleFiles:target {}",target);
                // Skip target if it starts with or ends with '#'.
                //String nameOnly = (new File(target.toString())).getName();
                //if (nameOnly.startsWith("#") || nameOnly.endsWith("#")) {
                //    //LOG.debug("findOtherBundleFiles:TARGET_ADD {}",target);
                //    //otherBundleFilesList.add(target);
                //    LOG.info("findOtherBundleFiles: Skipping bundle file {}",target.toString());
                //   continue;
                //}
                LOG.debug("findOtherBundleFiles: (new File(target.toString())).getName() {}",(new File(target.toString())).getName());
                // Add target if it is not the same as given url and is not a directory.
                if ((!target.getUrl().equals(url)) && !target.isDir()) {
                    if (TargetExaminer.isTargetBundleType(target.getUrl())) {
                    //if (BundleManager.isTargetBundleType(target.getUrl())) {
                        LOG.debug("findOtherBundleFiles:TARGET_ADD {}",target);
                        otherBundleFilesList.add(target);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error(" Cannot crawl for files in: " + url + ": " + e.getMessage());
        }
        LOG.debug("allFiles.size(),otherBundleFilesList.size() {},{}",allFiles.size(),otherBundleFilesList.size());
        return(otherBundleFilesList);
    }

    private static boolean containsExistingTarget(List<Target> targetList, URL existingURL) {
        // Given a list of targets, inspect if the list contains the provided URL.
        // This function is needed as the default List.contains() does not work.
        boolean containsFlag = false;
        Target target = null;

        LOG.debug("containsExistingTarget:existingURL,targetList.size {},{}",existingURL,targetList.size());

        int ii = 0;
        while (!containsFlag && ii < targetList.size()) {
            target = targetList.get(ii);
            LOG.debug("containsExistingTarget: ii,existingURL,target.getUrl,COMPARE_URL_FLAG {},{},{},{}",ii,existingURL,target.getUrl(),existingURL.equals(target.getUrl()));
            // If the url within target is the same as existingURL, we have found it in the list.
            if (existingURL.equals(target.getUrl())) {
                containsFlag = true;
            }
            ii += 1; 
        }
        LOG.debug("containsExistingTarget:existingURL,targetList.size,containsFlag {},{},{}",existingURL,targetList.size(),containsFlag);
        return(containsFlag);
    }

    /**
     * Find other collection file(s).
     * @param targetList the url List of where to start looking for files from.
     * @return a list of files that are other than the given urls.
     */

    public static ArrayList<Target> findOtherCollectionFiles(List<Target> targetList) {
        // Given a list of targets containing the collections, crawl parent directory and look for all the other
        // collection files (which are just .xml files).

        ArrayList<Target> otherCollectionFilesList = new ArrayList<Target>();

        LOG.debug("findOtherCollectionFiles:targetList.size():" + targetList.size());

        List<Target> allFiles = null;
        URL urlCrawl = null;
        URL urlLatest = null;
        List<String> crawledDirectory = new ArrayList<String>();  // Keep a list of directory we have crawled to keep from crawling same directory again.

        try {
          // Because the input to this function is a list of target, we have loop through targetList.
          for (Target crawlTarget : targetList) {
            urlCrawl = crawlTarget.getUrl(); 
            // Get the parent directory of url and crawl for files that starts with 'bundle'
            String dirName     = (new File(urlCrawl.getPath())).getParent();
            LOG.debug("findOtherCollectionFiles:dirName,crawledDirectory.size {},{}",dirName,crawledDirectory.size());

            // Check to see if we have crawled this directory before.
            if (!crawledDirectory.contains(dirName)) {
                crawledDirectory.add(dirName);
            } else {
                // Do not crawl the same directory again.
                LOG.debug("findOtherCollectionFiles:dirName {} crawled before crawledDirectory.size {}",dirName,crawledDirectory.size());
                continue;
            }

            Crawler crawler = CrawlerFactory.newInstance(new File(dirName).toURI().toURL());
            IOFileFilter regexFileFilter = new RegexFileFilter(COLLECTION_LABEL_PATTERN);
            LOG.debug("findOtherCollectionFiles:crawler {}",crawler);
            //allFiles = crawler.crawl(new File(dirName).toURI().toURL(),regexFileFilter);
            // Note: For some strange reason, the crawler goes into an infinite loop using the above call
            //       so we will use an alternate call to get the list of files.
            LABEL_EXTENSIONS_LIST[0] = LABEL_EXTENSION;
            allFiles = crawler.crawl(new File(dirName).toURI().toURL(), LABEL_EXTENSIONS_LIST, false, Constants.COLLECTION_NAME_TOKEN);

            LOG.debug("findOtherCollectionFiles:allFiles.size() {}",allFiles.size());
            int targetIndex = 0;
            for (Target target : allFiles) {
                            LOG.debug("findOtherCollectionFiles:targetIndex,target {},{}",targetIndex,target);
                            // Remove extraneous for loop since the value of target.getUrl() can be checked against targetList with the containsExistingTarget() function directly.

                            // Check if the newly found find is in targetList.  If not, add it to the otherCollectionFilesList so it can be skip
                            if (!BundleManager.containsExistingTarget(targetList,target.getUrl())) {
                                otherCollectionFilesList.add(target);

                                LOG.info("findOtherCollectionFiles:SKIP: {} due to collection not latest or does not sharing the same logical_identifier as the bundle target",target.getUrl());
                                // Write a record to report that we are skipping this file.
                                if (BundleManager.m_report != null) {
                                    try {
                                        ValidationProblem p1 = new ValidationProblem(new ProblemDefinition(ExceptionType.INFO,
                                                                                                           ProblemType.UNREFERENCED_FILE, "Skipping " + target.getUrl() + " due to collection not latest or does not sharing the same logical_identifier as the bundle target"),
                                                                                         target.getUrl());
                                        BundleManager.m_report.recordSkip(new URI(target.getUrl().toString()), p1);
                                    } catch (Exception e) {
                                        LOG.error("findOtherCollectionFiles:Cannot build ValidationProblem object or report skip file: {}",target.getUrl());
                                    }
                                } else {
                                    LOG.warn("findOtherCollectionFiles:Object BundleManager.m_report is null");
                                }
                           }

                           targetIndex += 1;
            }
          }
        } catch (Exception e) {
            // Print stack trace for developer to inspect.
            e.printStackTrace();
            LOG.error(" Cannot crawl for files in: " + urlCrawl + ": " + e.getMessage());
        }
        // Check for null-ness before getting list size.
        if (allFiles != null) {
            LOG.debug("allFiles.size(),otherCollectionFilesList.size() {},{}",allFiles.size(),otherCollectionFilesList.size());
        }
        return(otherCollectionFilesList);
    }

    /**
     * Make an exception for bundle that is not applicable.
     * @param url the url of where to start looking for files from.
     * @param location the location of where to start looking for files from.
     */
    public static void makeException(URL url, String location) {
        // If the target is a bundle, the exception can now be made.
        // Make the following changes:
        //     1.  Change the location from a file into a directory.
        //     2.  Create a list of other bundle files to ignore so only the provided bundle is processed.
        //     3.  Create a list of collection files to ignore so only the latest collection file is processed.

        LOG.debug("makeException:url,location {},{}",url,location);

        // First, find any other bundle files so they can be eliminated from crawling later.
        ArrayList<Target> otherBundleFiles = BundleManager.findOtherBundleFiles(url);
        BundleManager.m_ignoreList.addAll(otherBundleFiles);

        // Because this function has knowledge of why a particular file is being ignored while crawling
        // we will log it here.
        if (otherBundleFiles != null) {
            // Only print the size if we know that otherBundleFiles is not null.
            LOG.debug("makeException:url,otherBundleFiles.size() {},{}",url,otherBundleFiles.size());
            for (Target target : otherBundleFiles) {
                LOG.info("makeException:SKIP: {} due to not being selected as the bundle target",target.getUrl());
                    // Write a record to report that we are skipping this file.
                    if (BundleManager.m_report != null) {
                        try {
                            ValidationProblem p1 = new ValidationProblem(new ProblemDefinition(ExceptionType.INFO,
                                                                                               ProblemType.UNREFERENCED_FILE, "Skipping " + target.getUrl() + " due to not being selected as the bundle target"),
                                                                             target.getUrl());
                            BundleManager.m_report.recordSkip(new URI(target.getUrl().toString()), p1);
                        } catch (Exception e) {
                            LOG.error("makeException:Cannot build ValidationProblem object or report skip file: {}",target.getUrl());
                        }
                    } else {
                        LOG.warn("makeException:Object BundleManager.m_report is null");
                    }
                    // Remove System.out.println() so as keeping output from pipeline clean.
                //}
            }
        }

        LOG.debug("makeException:url,otherBundleFiles {},{}",url,otherBundleFiles);

        String parentToLocation = null;
        try {
            // The new location (as a directory) will be applicable later on.
            m_location = "file:" + (new File(url.getPath())).getParent() + File.separator;
            parentToLocation = (new File(url.getPath())).getParent() + File.separator;
            LOG.debug("m_location {}",m_location);
        } catch (Exception e) {
            LOG.error("Cannot setLocation() to location. " + location + ": " + e.getMessage());
            return;
        }

        // Second, build a list of collection files that are not the latest to ignore.
        LOG.debug("pre_call:buildCollectionIgnoreList:url {}",url);
        URL parentURL = null;
        try {
            parentURL = new File(parentToLocation).toURI().toURL();
            LOG.debug("url,BundleManager.m_ignoreList {},{}",url,BundleManager.m_ignoreList);
            LOG.debug("url,BundleManager.m_ignoreList.size() {},{}",url,BundleManager.m_ignoreList.size());
        } catch (Exception e) {
            LOG.error("Cannot build URL for parentToLocation " + parentToLocation);
            return;
        }

        ArrayList<Target> ignoreCollectionList = BundleManager.buildCollectionIgnoreList(parentURL,url);
        LOG.debug("post_call:buildCollectionIgnoreList:url {}",url);

        // Because any reporting about a collection file being ignored/skipped is already done in function buildCollectionIgnoreList() above, no need to report it again.

        if (ignoreCollectionList != null) {
            // Remove the double reporting loop.
            BundleManager.m_ignoreList.addAll(ignoreCollectionList);  // Add a list of collection to ignore in the crawler.
        }
    }
}
