package gov.nasa.pds.tools.web.ui.utils;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("nls")
public class ErrorCodeMapping {
	// mapping between resource key and error code
	public static final Map<String, Integer> MAPPINGS = new HashMap<String, Integer>();

	// number of digits to display, pad if number of digits found is less than
	// this
	private static final int CODE_LENGTH = 4;

	// TODO: make sure there's no way you could put the same value in twice
	static {
		addMapping("error.requiredstring", 1);
		addMapping("manageDataSets.error.invalidDataSetPath", 2);
		addMapping("loadLabel.error.missingLabelUrl", 3);
		addMapping("loadLabel.error.malformedLabelUrl", 4);
		addMapping("loadLabel.error.fileNotFound", 5);
		addMapping("selectColumns.error.noColumnsSelected", 6);
		addMapping("selectRows.error.noColumnSelected", 7);
		addMapping("selectRows.error.noCondition", 8);
		addMapping("selectRows.error.noValueEntered", 9);
		addMapping("enterDownloadFormat.error.noFileType", 10);
		// addMapping("orderRows.error.noRulesChecked", 12);
		addMapping("orderRows.error.noOrderDirection", 13);
		addMapping("selectRows.error.noResultsReturned", 14);
		addMapping("orderRows.error.noOrderColumnSelected", 15);
		addMapping("loadLabel.error.urlUnreachable", 16);
		addMapping("selectRows.error.numericOnly", 17);
		addMapping("selectRows.error.unrecognizedDateFormat", 19);
		addMapping("loadLabel.error.noSupportedObjectsFound", 20);
		addMapping("loadLabel.error.bitColumnsItemsNotSupported", 21);
		addMapping("loadLabel.error.structureNotFound", 22);
		addMapping("loadLabel.error.samplingNotSupported", 23);
		// addMapping("loadLabel.error.FileNotSupported", 24);

	}

	private static void addMapping(final String key, final int code) {
		if (MAPPINGS.containsKey(key)) {
			throw new RuntimeException("Attempting to add a key, \"" + key
					+ "\", that was previously added to error mappings.");
		} else if (MAPPINGS.containsValue(code)) {
			throw new RuntimeException("Attempting to add a code, \"" + code
					+ "\", that was previously added to error mappings.");
		} else {
			MAPPINGS.put(key, code);
		}
	}

	// try to get associated error code for a given key and format. ex. error
	// code error.foo.bar may have a code of 102 which results in a display
	// value of U0102
	public static String get(String key) throws Exception {
		String returnString = "";
		final String codeValue;
		try {
			codeValue = MAPPINGS.get(key).toString();
		} catch (Exception e) {
			throw new Exception("No error code was found for key \"" + key
					+ "\".");
		}
		final int missingZeros = CODE_LENGTH - codeValue.length();
		if (missingZeros < 0) {
			throw new Exception("UI error codes may only be 4 digits long");
		}
		for (int i = 0; i < missingZeros; i++) {
			returnString += "0";
		}
		returnString += codeValue;
		return returnString;

	}
}
