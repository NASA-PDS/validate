package gov.nasa.pds.tools.label;

/**
 * Defines keys for validation settings that the user may specify.
 */
public class ValidationSettings {

	private ValidationSettings() {
		// never called
	}
	
	/** Specifies the type of validation to perform. */
	public static final String VALIDATION_RULE = "pds.validation.type";
	
	/** Specifies the path to the PDS3 Master Data Dictionary. */
	public static final String PDS3_DICTIONARY_PATH = "pds3.dictionary.path";
	
}
