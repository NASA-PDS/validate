package gov.nasa.pds.tools.web.ui.containers.tabularManagement;

import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.pds.tools.containers.VolumeContainerSimple;
import gov.nasa.pds.tools.dict.DictIdentifier;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Numeric;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.web.ui.containers.ColumnInfo;
import gov.nasa.pds.tools.web.ui.containers.LabelContainer;
import gov.nasa.pds.tools.web.ui.utils.TabularData;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class TabularLabelContainer extends LabelContainer {

	public TabularLabelContainer(URL labelUrl, VolumeContainerSimple volume,
			Dictionary dictionary) {
		super(labelUrl, volume, dictionary);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("nls")
	public List<PointerStatement> getTabularPointers() {
		List<PointerStatement> allPointers = this.labelObj.getPointers();
		List<PointerStatement> tabularPointers = new ArrayList<PointerStatement>();
		for (PointerStatement pointer : allPointers) {
			DictIdentifier id = pointer.getIdentifier();
			if (id.getId().endsWith("TABLE") || id.getId().endsWith("SERIES")
					|| id.getId().endsWith("SPECTRUM") || id.getId().endsWith("STRUCTURE")) {
				// || id.endsWith("SPREADSHEET"))
				// {
				tabularPointers.add(pointer);
			}
		}
		return tabularPointers;
	}

	public List<ObjectStatement> getAllObjects(final DictIdentifier identifier) {

		List<ObjectStatement> objectStatements = new ArrayList<ObjectStatement>();
		List<ObjectStatement> innerLevelStatements = new ArrayList<ObjectStatement>();

		// get all top level object statements which have the identifier
		List<ObjectStatement> topLevelStatements = this.labelObj.getObjects();

		for (ObjectStatement os : topLevelStatements) {

			// object statement is at the top level
			if (os.getIdentifier().equals(identifier)) {
				objectStatements.add(os);
			}

			// find all object statements within data objects, if any
			if (!os.getObjects().isEmpty())
				innerLevelStatements.addAll(os.getObjects());
		}

		// search for object statements within inner data object statements
		for (ObjectStatement os : innerLevelStatements) {
			if (os.getIdentifier().equals(identifier)) {
				objectStatements.add(os);
			}
		}

		return objectStatements;
	}

	@SuppressWarnings("nls")
	@Override
	public TabularData getTabularData(final String tableType, long numRows) {

		// try to get table pointer
		PointerStatement tablePointer = findPointer(tableType);
		// initialize variables created in if and try below
		URL tabFileUrl = null;
		File tabularFile = null;
		Numeric startPosition = null;

		// if pointer found, try to get object def for it
		if (tablePointer != null) {
			// if no labelFile - using URL
			if (this.labelFile == null) {
				try {
					// get file - assume only one file
					Entry<Numeric, URI> entry = getURIEntry(tablePointer);
					tabFileUrl = entry.getValue().toURL();
					startPosition = entry.getKey();
				} catch (Exception e) {
					throw new RuntimeException(
							"Referenced tabular data file does not exist"); //$NON-NLS-1$
				}
			} else {
				// get file - assume only one file
				Entry<Numeric, File> entry = getFileEntry(tablePointer);
				tabularFile = entry.getValue();
				startPosition = entry.getKey();

				if (!FileUtils.exists(tabularFile)) {
					throw new RuntimeException(
							"Referenced tabular data file does not exist"); //$NON-NLS-1$
				}
			}
		}

		final long startByte = Label.getSkipBytes(this.labelObj, startPosition);

		final List<ObjectStatement> foundObjects = this.labelObj
				.getObjects(tableType);
		if (foundObjects.size() == 1) {
			final List<ColumnInfo> columns = new ArrayList<ColumnInfo>();
			ObjectStatement tableObj = foundObjects.get(0);

			AttributeStatement tableFormat = tableObj
					.getAttribute("INTERCHANGE_FORMAT"); //$NON-NLS-1$
			if (tableFormat != null) {
				String tableFormatString = tableFormat.getValue().toString();
				if (tableFormatString.equalsIgnoreCase("BINARY")) {
					// return null;
				}
			}

			if (tableObj.getIdentifier().toString().equals("SPREADSHEET")) {
				for (ObjectStatement column : tableObj.getObjects("FIELD")) { //$NON-NLS-1$
					columns.add(new ColumnInfo(column));
				}

			} else {
				for (ObjectStatement column : tableObj.getObjects("COLUMN")) { //$NON-NLS-1$
					columns.add(new ColumnInfo(column));
				}
			}
			if (tabFileUrl != null) {
				if (tableObj.getIdentifier().toString().equals("SPREADSHEET")) {
					String delimiterName = tableObj.getAttribute(
							"FIELD_DELIMITER").getValue().toString();
					@SuppressWarnings("unused")
					String delimiterValue;
					if (delimiterName.equalsIgnoreCase("COMMA"))
						delimiterValue = ",";
					else if (delimiterName.equalsIgnoreCase("SEMICOLON"))
						delimiterValue = ";";
					else if (delimiterName.equalsIgnoreCase("TAB"))
						delimiterValue = "\t";
					else if (delimiterName.equalsIgnoreCase("VERTICAL_BAR"))
						delimiterValue = "|";
					else
						delimiterValue = ",";

					// COMMA, SEMICOLON, TAB, or VERTICAL_BAR
					// return new SpreadsheetData(tabFileUrl, columns,
					// startByte,
					// numRows, delimiterValue);
				}

				return new TabularData(tabFileUrl, columns, startByte, numRows);
			}
			return new TabularData(tabularFile, columns, startByte, numRows);

		}

		return null;
	}
}
