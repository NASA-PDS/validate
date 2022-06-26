package gov.nasa.pds.tools.validate.rule.pds4;

import gov.nasa.pds.tools.label.ExceptionType;
import gov.nasa.pds.tools.validate.ProblemDefinition;
import gov.nasa.pds.tools.validate.ProblemType;
import gov.nasa.pds.tools.validate.Standard;

/**
 * Defines the problems that can be reported by PDS4 validation rules.
 */
public final class PDS4Problems {

    /** Indicates a label that has invalid structure. */
    public static final ProblemDefinition INVALID_LABEL = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.INVALID_LABEL,
            "Label does not comply with PDS4 standards",
            Standard.PDS4_STANDARDS_REFERENCE,
            "3"
    );

    /** Indicates a label that does not have the required extension. */
    public static final ProblemDefinition INVALID_LABEL_EXTENSION = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.BAD_EXTENSION,
            "Label file name extension does not match expected extension. '.xml' is checked by default, and '.lblx' can be specified using the --label-extension flag.",
            Standard.PDS4_STANDARDS_REFERENCE,
            "3"
    );

    /** Indicates a table definition where the fields are not defined in physical order. */
    public static final ProblemDefinition FIELDS_NOT_IN_ORDER = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.TABLE_DEFINITION_PROBLEM,
            "Table fields must be defined in physical order within the record",
            Standard.PDS4_STANDARDS_REFERENCE,
            "4B.1"
    );

    /** Indicates a table definition where the fields are not defined in physical order. */
    public static final ProblemDefinition FIELDS_OVERLAP = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.TABLE_DEFINITION_PROBLEM,
            "Table fields must not overlap within the record",
            Standard.PDS4_STANDARDS_REFERENCE,
            "4B"
    );

	/** Indicates a file name that exceeds the maximum length. */
	public static final ProblemDefinition FILE_NAME_TOO_LONG = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.FILE_NAME_TOO_LONG,
			"File name must be no longer than 255 characters",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.1.1"
	);

	/** Indicates a file name that includes illegal characters. */
	public static final ProblemDefinition FILE_NAME_USES_INVALID_CHARACTER = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.FILE_NAME_HAS_INVALID_CHARS,
			"File name uses invalid character",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.1.1"
	);

	/** Indicates a file name that conflicts with the name of another file or folder
	 * in the same parent folder. Names conflict in case if they are the same except
	 * for case.
	 */
	public static final ProblemDefinition FILE_NAME_CONFLICTS_IN_CASE = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.FILE_NAMING_PROBLEM,
			"File name conflicts in case with another file or directory",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.1.1"
	);

	/** Indicates a file name that is explicitly disallowed by the standard. */
	public static final ProblemDefinition UNALLOWED_FILE_NAME = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.UNALLOWED_FILE_NAME,
			"File name is not allowed",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.1.2"
	);

	/** Indicates a file name that includes a disallowed base name. */
	public static final ProblemDefinition UNALLOWED_BASE_NAME = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.UNALLOWED_BASE_NAME,
			"File base name is not allowed",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.1.4"
	);

	/** Indicates a directory name that exceeds the maximum length. */
	public static final ProblemDefinition DIRECTORY_NAME_TOO_LONG = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.DIR_NAME_TOO_LONG,
			"Directory name is longer than 255 characters",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.2.1"
	);

	/** Indicates a directory name that includes a disallowed character. */
	public static final ProblemDefinition DIRECTORY_NAME_USES_INVALID_CHARACTER = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.DIR_NAME_HAS_INVALID_CHARS,
			"Directory name uses invalid character",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.2.1"
	);

	/** Indicates a directory name that conflicts with the name of another file or folder
	 * in the same parent folder. Names conflict in case if they are the same except
	 * for case.
	 */
	public static final ProblemDefinition DIRECTORY_NAME_CONFLICTS_IN_CASE = new ProblemDefinition(
			ExceptionType.ERROR,
			ProblemType.FILE_NAMING_PROBLEM,
			"Directory name conflicts in case with the name of another file or directory",
			Standard.PDS4_STANDARDS_REFERENCE,
			"6C.2.1"
	);

    /** Indicates a directory name that is explicitly disallowed by the standard. */
    public static final ProblemDefinition UNALLOWED_BUNDLE_SUBDIRECTORY_NAME = new ProblemDefinition(
    		ExceptionType.WARNING,
            ProblemType.UNALLOWED_BUNDLE_SUBDIR_NAME,
            "Directory name is reserved and not in the recommended location in the bundle root directory",
            Standard.PDS4_STANDARDS_REFERENCE,
            "6C.2.2"
    );

    /** Indicates a logical identifier that was used more than once. */
    public static final ProblemDefinition DUPLICATE_LOGICAL_IDENTIFIER = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.DUPLICATE_IDENTIFIER,
            "Logical identifiers must be unique",
            Standard.PDS4_STANDARDS_REFERENCE,
            "6D.2"
    );

    /** Indicates a directory name that is explicitly disallowed by the standard. */
    public static final ProblemDefinition UNALLOWED_DIRECTORY_NAME = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.UNALLOWED_DIRECTORY_NAME,
            "Directory name is not allowed",
            Standard.PDS4_STANDARDS_REFERENCE,
            "6C.2.3"
    );

    /** Indicates a collection directory that has an invalid name. */
    public static final ProblemDefinition INVALID_COLLECTION_NAME = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.INVALID_COLLECTION_NAME,
            "Collection directory name is not valid",
            Standard.PDS4_STANDARDS_REFERENCE,
            "2B.2.2.1"
    );

    /** Indicates an unexpected file in the root directory of a bundle. */
    public static final ProblemDefinition UNEXPECTED_FILE_IN_BUNDLE_ROOT = new ProblemDefinition(
    		ExceptionType.ERROR,
            ProblemType.UNEXPECTED_FILE_IN_BUNDLE_ROOT,
            "File is not valid in bundle root directory",
            Standard.PDS4_STANDARDS_REFERENCE,
            "2B.2.2.1"
    );

    /** Indicates a file that is not associated with any label. */
    public static final ProblemDefinition UNLABELED_FILE = new ProblemDefinition(
    		ExceptionType.WARNING,
            ProblemType.UNLABELED_FILE,
            "File is not referenced by any label",
            Standard.PDS4_STANDARDS_REFERENCE,
            "3"
    );

	private PDS4Problems() {
	    // Never instantiated.
	}

}
