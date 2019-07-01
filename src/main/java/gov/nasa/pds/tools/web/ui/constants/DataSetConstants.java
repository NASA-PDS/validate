package gov.nasa.pds.web.ui.constants;

import gov.nasa.arc.pds.tools.util.FileUtils;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("nls")
public class DataSetConstants {

	public static final Set<String> NON_PROCESSED_TYPES = new HashSet<String>();

	static {
		NON_PROCESSED_TYPES.add("png"); // binary image
		// NON_PROCESSED_TYPES.add("img"); // disabled - often attached label
		NON_PROCESSED_TYPES.add("jpg"); // binary image
		NON_PROCESSED_TYPES.add("gif"); // binary image
		NON_PROCESSED_TYPES.add("zip"); // archives
		NON_PROCESSED_TYPES.add("mpg"); // binary video
		NON_PROCESSED_TYPES.add("avi"); // binary video
		NON_PROCESSED_TYPES.add("mp3"); // binary audio
		NON_PROCESSED_TYPES.add("mp4"); // binary video
		NON_PROCESSED_TYPES.add("asf"); // binary video
		NON_PROCESSED_TYPES.add("fla"); // flash video
		NON_PROCESSED_TYPES.add("mpeg"); // binary video
		NON_PROCESSED_TYPES.add("exe"); // executable
		NON_PROCESSED_TYPES.add("ppt"); // powerpoint, process in future?
		NON_PROCESSED_TYPES.add("ps"); // postscript, process in future?
		NON_PROCESSED_TYPES.add("tif"); // tiff document
		NON_PROCESSED_TYPES.add("pdf"); // portable document format
		NON_PROCESSED_TYPES.add("doc"); // word document
		NON_PROCESSED_TYPES.add("odf"); // open office spreadsheet format
		NON_PROCESSED_TYPES.add("xls"); // excel spreadsheet
	}

	public enum Status {
		INCOMPLETE("dataSet.status.incomplete", 1), //
		VALID("dataSet.status.valid", 2), //
		SUBMITTED("dataSet.status.submitted", 3), //
		IN_REVIEW("dataSet.status.inReview", 4), //
		REVIEW_FAILED("dataSet.status.reviewFailed", 5), //
		ARCHIVED("dataSet.status.archived", 6);

		private final String key;

		private final int sortIndex;

		private Status(final String key, final int sortIndex) {
			this.key = key;
			this.sortIndex = sortIndex;
		}

		public String getKey() {
			return this.key;
		}

		public int getSortIndex() {
			return this.sortIndex;
		}
	}

	public enum SetNodeType {
		DIRECTORY, //
		LABEL, // lbl
		FORMAT, // fmt
		IMAGE, // img, png, gif, jpg, jpeg
		TABULAR, // tab
		DOCUMENT, // txt, doc, pdf
		CATALOG, // cat
		WEB, // htm, html
		INDEX, // special index file
	}

	public enum DataType {
		IMAGE("dictionary.dataType.image"), // (.img, .png, .gif, .jpg, .jpeg)
		// general tabular data
		TABULAR_DATA("dictionary.dataType.tabularData"), //
		// tabular data sliced by spectrum
		SPECTRAL_CUBE("dictionary.dataType.spectralCube"), //
		// tabular data displaying content over time
		TIME_SERIES("dictionary.dataType.timeSeries"), //
		// (.txt, .doc, .pdf) text documents
		TEXTUAL_DOCUMENT("dictionary.dataType.textDocument"), //
		SPICE_KERNAL("dictionary.dataType.spiceKernal"), //
		SHAPE_FILE("dictionary.dataType.shapeFile"), //
		VRML("dictionary.dataType.vrml"), //
		DIRECTORY("dictionary.dataType.directory"), // directory
		LABEL("dictionary.dataType.label"), // (.lbl) label file
		FORMAT("dictionary.dataType.format"), // (.fmt) format file
		CATALOG("dictionary.dataType.catalog"), // (.cat) catalog file
		WEB("dictionary.dataType.web"), // (.htm, .html) browser renderable file
		INDEX("dictionary.dataType.index"), // special index file
		UNKNOWN("dictionary.dataType.unknown"), // unknown from avail info but
		// may be known type
		OTHER("dictionary.dataType.other"); // unknown

		private final String key;

		private DataType(final String key) {
			this.key = key;
		}

		public String getKey() {
			return this.key;
		}

		// TODO: find better way to do this. If we add an array of exclusive
		// extensions to the ctor, is there a way to do a .valueOf()?
		// NOTE: while the file may be a specific type, we may not be able to
		// tell from extension
		// TODO: if extension non-exclusive... inspect file?
		public static DataType fromFile(final File file) {
			if (file.isDirectory()) {
				return DataType.DIRECTORY;
			}
			return fromFileName(file.getName());

		}

		public static DataType fromFileName(final String fileName) {
			final String extension = FileUtils.getExtension(fileName);
			if (extension != null) {
				if (extension.equalsIgnoreCase("lbl")) {
					return DataType.LABEL;
				} else if (extension.equalsIgnoreCase("cat")) {
					return DataType.CATALOG;
				} else if (extension.equalsIgnoreCase("html")
						|| extension.equalsIgnoreCase("htm")) {
					return DataType.WEB;
				}
			}
			// don't return other or unknown because it may be a known type we
			// can't determine from provided info
			return UNKNOWN;
		}
	}

	public static final String README_NAME = "AAREADME.TXT";

	public static final String INDEX_FOLDER_NAME = "INDEX";

	public static final String INDEX_INFO_FILE_NAME = "INDXINFO.TXT";

	// public static final String INDEX_FILE_NAME = "index.tab";

	public static final String BROWSE_FOLDER_NAME = "BROWSE";

	public static final String BROWSE_INFO_FILE_NAME = "BROWINFO.TXT";

	public static final String CATALOG_FOLDER_NAME = "CATALOG";

	public static final String CATALOG_INFO_FILE_NAME = "CATINFO.TXT";

	public static final String CALIB_FOLDER_NAME = "CALIB";

	public static final String CALIB_INFO_FILE_NAME = "CALINFO.TXT";

	public static final String VOLUME_DESC_FILE_NAME = "VOLDESC.CAT";

	public static final String DOCUMENT_FOLDER_NAME = "DOCUMENT";

	public static final String DOCUMENT_INFO_FILE_NAME = "DOCINFO.TXT";

	public static final String EXTRAS_FOLDER_NAME = "EXTRAS";

	public static final String EXTRAS_INFO_FILE_NAME = "EXTRINFO.TXT";

	public static final String ERRATA_FILE_NAME = "ERRATA.TXT";

	public static final String GAZETTEER_FOLDER_NAME = "GAZETTEER";

	public static final String GAZETTEER_INFO_FILE_NAME = "GAZINFO.TXT";

	public static final String GAZETTEER_DESC_FILE_NAME = "GAZETTER.TXT";

	public static final String GAZETTEER_LABEL_FILE_NAME = "GAZETTER.LBL";

	public static final String GAZETTEER_TAB_FILE_NAME = "GAZETTER.TAB";

	public static final String GEOMETRY_FOLDER_NAME = "GEOMETRY";

	public static final String GEOMETRY_INFO_FILE_NAME = "GEOMINFO.TXT";

	public static final String LABEL_FOLDER_NAME = "LABEL";

	public static final String LABEL_INFO_FILE_NAME = "LABINFO.TXT";

	public static final String SOFTWARE_FOLDER_NAME = "SOFTWARE";

	public static final String SOFTWARE_INFO_FILE_NAME = "SOFTINFO.TXT";

	public static final String DATA_FOLDER_NAME = "DATA";

	public static final String TEXT_FILE_ENCODING = "ASCII";

	public static final String[] NON_INDEXED_FOLDERS = new String[] { "INDEX",
			"DOCUMENT", "CATALOG", "CALIB", "CALIBRATION", "GEOMETRY",
			"BROWSE", "EXTRAS" };

	public static final String[] ILLEGAL_INDEXED_FOLDERS = new String[] {
			"DOCUMENT", "CATALOG" };

	// regex for matching all possible cumulative index filenames
	public static final String CUMINDEX_NAME_REGEX = "(?i:(CUMINDEX|([0-9a-zA-Z]{3}CMIDX))\\.LBL)";
}
