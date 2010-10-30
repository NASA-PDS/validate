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
// $Id$
package gov.nasa.pds.validate.crawler;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * Class that crawls a given directory.
 *
 * @author mcayanan
 *
 */
public class DirectoryCrawler  {
    private IOFileFilter fileFilter;
    private FileFilter directoryFilter;

    /**
     * Constructor.
     *
     */
    public DirectoryCrawler() {
        fileFilter = new WildcardOSFilter("*");
        directoryFilter = FileFilterUtils.directoryFileFilter();

    }

    /**
     * Crawl a given directory.
     *
     * @param directory A directory to crawl.
     * @param getSubDirectories Set to 'false' to ignore sub-directories.
     * @param fileFilters Specify file patterns to search for while crawling
     * a directory.
     *
     * @return A list of files and sub-directories (if found and if
     * getSubDirectories flag is 'true').
     */
    public List<File> crawl(File directory, boolean getSubDirectories,
            List<String> fileFilters) {
        List<File> results = new ArrayList<File>();

        if(fileFilters != null && !(fileFilters.isEmpty())) {
            fileFilter = new WildcardOSFilter(fileFilters);
        }
        if( !directory.isDirectory() ) {
            throw new IllegalArgumentException("Input file is not a directory: "
                    + directory);
        }

        //Find files only first
        results.addAll(FileUtils.listFiles(directory, fileFilter, null));

        //Visit sub-directories if the recurse flag is set
        if(getSubDirectories) {
            results.addAll(
                    Arrays.asList(directory.listFiles(directoryFilter)));
        }

        return results;
    }

}
