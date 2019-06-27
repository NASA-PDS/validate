//  Copyright 2009-2016, by the California Institute of Technology.
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
//
package gov.nasa.pds.tools.label;

/**
 * Implements an object that stores a line, column, and location
 * in a source document.
 */
public final class SourceLocation {

	private int lineNumber;
	private int columnNumber;
	private String url;

	/**
	 * Creates a new instance with a given line, column number and
	 * url of the source.
	 *
	 * @param line the source line number, or -1 if not available
	 * @param column the source column number, or -1 if not available
	 * @param url the source location, or null if not available
	 */
	public SourceLocation(int line, int column, String url) {
		lineNumber = line;
		columnNumber = column;
		this.url = url;
	}

	/**
	 * Gets the source line number.
	 *
	 * @return the source line number, or -1 if not available
	 */
	public int getLineNumber() {
		return lineNumber;
	}

	/**
	 * Gets the source column number.
	 *
	 * @return the source column number, or -1 if not available.
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * Gets the url of the source location.
	 * 
	 * @return the url, or null if not available.
	 */
	public String getUrl() {
	  return url;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj==null || !(obj instanceof SourceLocation)) {
			return false;
		}

		SourceLocation other = (SourceLocation) obj;
		return lineNumber==other.lineNumber && columnNumber==other.columnNumber 
		    && url==other.getUrl();
	}

	@Override
	public int hashCode() {
		int result = lineNumber*17 + columnNumber;
		result = result * 17 + ((url == null) ? 0 : url.hashCode());
		return result;
	}

}
