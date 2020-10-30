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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.transform.sax.SAXSource;

import gov.nasa.pds.tools.util.LabelParser;
import gov.nasa.pds.tools.util.LidVid;
import gov.nasa.pds.tools.util.Utility;
import gov.nasa.pds.tools.util.XMLExtractor;

import gov.nasa.pds.tools.validate.Target;
import gov.nasa.pds.tools.validate.TargetExaminer;
import gov.nasa.pds.tools.validate.crawler.Crawler;
import gov.nasa.pds.tools.validate.crawler.CrawlerFactory;

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
    private static final Pattern COLLECTION_LABEL_PATTERN =
      Pattern.compile("(.*_)*collection(_.*)*\\.xml", Pattern.CASE_INSENSITIVE);
    private static final Pattern BUNDLE_LABEL_PATTERN =
      Pattern.compile("(.*_)*bundle(_.*)*\\.xml", Pattern.CASE_INSENSITIVE);

    private static ArrayList<Target> m_ignoreList = new ArrayList<Target>();
    private static String m_location = null;
    private static Target m_latestBundle = null;

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
     * Find all bundle files.
     * @param url the url of where to start looking for bundle files from.
     * @return a list of all bundle files.
     */
    public static List<Target> findAllBundleFiles(URL url) {
        List<Target> children = new ArrayList<Target>();
        try {
            IOFileFilter regexFileFilter = new RegexFileFilter(BUNDLE_LABEL_PATTERN);
            Crawler crawler = CrawlerFactory.newInstance(url);
            children = crawler.crawl(url, regexFileFilter);
        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findAllBundleFiles: children {}",children);
        LOG.debug("findAllBundleFiles: children.size() {}",children.size());
        return(children);
    }

    /**
     * Find all collection files.
     * @param url the url of where to start looking for collection files from.
     * @return a list of all collection files.
     */
    public static List<Target> findAllCollectionFiles(URL url) {
        List<Target> children = new ArrayList<Target>();
        try {
            IOFileFilter regexFileFilter = new RegexFileFilter(COLLECTION_LABEL_PATTERN);
            Crawler crawler = CrawlerFactory.newInstance(url);

            List<Target> dirs = new ArrayList<Target>();
            dirs = crawler.crawl(url, true);
            LOG.debug("findAllCollectionFiles: url,dirs.size() {},{}",url,dirs.size());
            LOG.debug("findAllCollectionFiles: url,dirs {},{}",url,dirs);
            List<Target> kids = new ArrayList<Target>();
            // For each sub directory found, get all collection files.
            for (Target dir : dirs) {
                if (dir.isDir()) {
                    kids = crawler.crawl(dir.getUrl(), regexFileFilter); 
                    LOG.debug("findAllCollectionFiles: dir.getUrl(),kids {},{}",dir.getUrl(),kids);
                    children.addAll(kids);
                }
            }
            LOG.debug("findAllCollectionFiles:children {}",children);
            LOG.debug("findAllCollectionFiles:children.size() {}",children.size());
        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findAllCollectionFiles: url,children {},{}",url,children);
        LOG.debug("findAllCollectionFiles: url,children.size() {},{}",url,children.size());
        return(children);
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
            LOG.debug("findAllCollectionFiles: url,dirs.size() {},{}",url,dirs.size());
            LOG.debug("findAllCollectionFiles: url,dirs {},{}",url,dirs);
            List<Target> kids = new ArrayList<Target>();
            // For each sub directory found, get all collection files.
            for (Target dir : dirs) {
                if (dir.isDir()) {
                    kids = crawler.crawl(dir.getUrl(), regexFileFilter); 
                    LOG.debug("findAllCollectionFiles: dir.getUrl(),kids {},{}",dir.getUrl(),kids);
                    children.addAll(kids);
                }
            }
            LOG.debug("findAllCollectionFiles:children {}",children);
            LOG.debug("findAllCollectionFiles:children.size() {}",children.size());

            // Purge all children unless they are the latest (largest version).
            // After this next statement, there should only be one element in children list.
            children = LidVid.reduceToLatestTargetOnly(children);
            LOG.debug("after:reduceToLatestTargetOnly:children.size() {}",children.size());
        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findCollectionWithLatestVersion:children {}",children);
        LOG.debug("findCollectionWithLatestVersion:children.size() {}",children.size());

        return(children);
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
                    kids = crawler.crawl(dir.getUrl(), regexFileFilter);
                    LOG.debug("findAllCollectionFiles: dir.getUrl(),kids {},{}",dir.getUrl(),kids);
                    children.addAll(kids);
                }
            }
            LOG.debug("findCollectionWithMatchingReference:children {}",children);
            LOG.debug("findCollectionWithMatchingReference:children.size() {}",children.size());

            ArrayList<String> bundleIdList  = TargetExaminer.getTargetContent(bundleUrl,"Product_Bundle/Identification_Area","logical_identifier","version_id");
            ArrayList<String> bundleLidList = TargetExaminer.getTargetContent(bundleUrl,"Product_Bundle/Bundle_Member_Entry","lidvid_reference","reference_type");
            LOG.debug("findAllCollectionFiles:bundleIdList {}",bundleIdList);
            LOG.debug("findAllCollectionFiles:bundleLidList {}",bundleLidList);

            String collectionReferenceToMatch = bundleLidList.get(0);
            String collectionVersionToMatch   = null;
            // If the reference contains "::" get it.
            if (collectionReferenceToMatch.indexOf("::") >= 0) {
                collectionVersionToMatch = collectionReferenceToMatch.split("::")[1];
                collectionReferenceToMatch = bundleLidList.get(0).split("::")[0];
            } else {
                collectionVersionToMatch   = bundleIdList.get(1);
            }

            // Purge all children unless they are the version referred to by the bundle.
            // After these statements, there should only be one element in children list.
            List<Target> childrenSelected = new ArrayList<Target>();
            // Look through all children for matching reference.  Only keep the child with matching reference.
            for (Target target : children) {
                ArrayList<String> collectionIdList = TargetExaminer.getTargetContent(target.getUrl(),"Product_Collection/Identification_Area","logical_identifier","version_id");
                // If the reference and version id matches, keep it.
                // Note that the first element has to be split using "::" in case it does contain it.
                LOG.debug("findCollectionWithMatchingReference:target.getUrl() comparing reference {} {} {}",target.getUrl(),collectionIdList.get(0), collectionReferenceToMatch);
                LOG.debug("findCollectionWithMatchingReference:target.getUrl() comparing version   {} {} {}",target.getUrl(),collectionIdList.get(1), collectionVersionToMatch);
                if (collectionIdList.get(0).split("::")[0].equals(collectionReferenceToMatch) && collectionIdList.get(1).equals(collectionVersionToMatch)) {
                    childrenSelected.add(target);
                }
            }

            LOG.debug("findCollectionWithMatchingReference:childrenSelected.size() {}",childrenSelected.size());
            LOG.debug("findCollectionWithMatchingReference:collectionReferenceToMatch " + collectionReferenceToMatch);
            LOG.debug("findCollectionWithMatchingReference:collectionVersionToMatch   " + collectionVersionToMatch);

            children = childrenSelected;
            LOG.debug("after:reduceToLatestTargetOnly:children.size() {}",children.size());
        } catch (IOException io) {
            LOG.error("Cannot crawl for files at url {}",url);
        }

        LOG.debug("findCollectionWithMatchingReference:children {}",children);
        LOG.debug("findCollectionWithMatchingReference:children.size() {}",children.size());
        return(children);
    }

    /**
     * Build a list of bundle files to ignore.
     * @param url the url of where to start looking for files from.
     * @return a list of files that are other than the given url.
     */
    public static ArrayList<Target> buildBundleIgnoreList(URL url) {
        List<Target> ignoreBundleList = new ArrayList<Target>();  // List of items to be removed from result of crawl() function.
        List<Target> latestBundles = BundleManager.findBundleWithLatestVersion(url);
        LOG.debug("latestBundles.size() ",latestBundles.size());
        LOG.debug("latestBundles {}",latestBundles);
        if (latestBundles.size() > 0)  {
            m_latestBundle = latestBundles.get(0);   //  Save this bundle for reference later on.
            LOG.debug("latestBundles[0] {}",latestBundles.get(0).getUrl());
            ignoreBundleList = BundleManager.findOtherBundleFiles(latestBundles.get(0).getUrl());
        }
        LOG.debug("ignoreBundleList {}",ignoreBundleList);
        LOG.debug("ignoreBundleList.size() {}",ignoreBundleList.size());

        return((ArrayList)ignoreBundleList);
    }

    /**
     * Build a list of collection files to ignore.
     * @param url the url of where to start looking for files from.
     * @return a list of files that are other than the given url.
     */
    public static ArrayList<Target> buildCollectionIgnoreList(URL url, URL bundleUrl) {
        LOG.debug("url {}",url);
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

        LOG.debug("latestCollections.size() {}",latestCollections.size());
        LOG.debug("latestCollections {}",latestCollections);

        if (latestCollections.size() > 0)  {
            LOG.debug("latestCollections[0] {}",latestCollections.get(0).getUrl());
            ignoreCollectionList = BundleManager.findOtherCollectionFiles(latestCollections.get(0).getUrl());
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
            allFiles = crawler.crawl(new File(dirName).toURI().toURL(),regexFileFilter);
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
                LOG.info("findOtherBundleFiles: (new File(target.toString())).getName() {}",(new File(target.toString())).getName());
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

    /**
     * Find other collection file(s).
     * @param url the url of where to start looking for files from.
     * @return a list of files that are other than the given url.
     */
    public static ArrayList<Target> findOtherCollectionFiles(URL url) {
        // Given a url containing the given collection, crawl parent directory and look for all the other
        // collection bundle files (which are just .xml files).

        ArrayList<Target> otherBundleFilesList = new ArrayList<Target>();

        LOG.debug("findOtherBundleFiles:url:" + url);

        List<Target> allFiles = null;

        try {
            // Get the parent directory of url and crawl for files that starts with 'bundle'
            String dirName     = (new File(url.getPath())).getParent();
            LOG.debug("findOtherBundleFiles:dirName {}",dirName);
            Crawler crawler = CrawlerFactory.newInstance(new File(dirName).toURI().toURL());
            IOFileFilter regexFileFilter = new RegexFileFilter(COLLECTION_LABEL_PATTERN);
            LOG.debug("findOtherBundleFiles:crawler {}",crawler);
            allFiles = crawler.crawl(new File(dirName).toURI().toURL(),regexFileFilter);

            for (Target target : allFiles) {
                LOG.debug("findOtherBundleFiles:target {}",target);
                // Add target if it is not the same as given url and is not a directory.
                if ((!target.getUrl().equals(url)) && !target.isDir()) {
                    if (TargetExaminer.isTargetCollectionType(target.getUrl())) {
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

    /**
     * Make an exception for bundle that is not applicable.
     * @param url the url of where to start looking for files from.
     * @param location the location of where to start looking for files from.
     */
    public static void makeException(URL url, String location) {
        // If the target a bundle, the exception can now be made.
        // Make the following changes:
        //     1.  Change the location from a file into a directory.
        //     2.  Create a list of other bundle files to ignore so only the provided bundle is processed.
        //     3.  Create a list of collection files to ignore so only the latest collection file is processed.

        // First, find any other bundle files so they can be eliminated from crawling later.
        ArrayList<Target> otherBundleFiles = BundleManager.findOtherBundleFiles(url);
        BundleManager.m_ignoreList.addAll(otherBundleFiles);

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
        BundleManager.m_ignoreList.addAll(ignoreCollectionList);  // Add a list of collection to ignore in the crawler.
    }
}
