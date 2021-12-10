package gov.nasa.pds.web.ui.utils;

import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.containers.FileReference;
import gov.nasa.pds.tools.containers.VolumeContainerSimple;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.dict.parser.DictionaryParser;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.DateTimeFormatter;
import gov.nasa.pds.tools.label.IncludePointer;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.label.SpecialPointer;
import gov.nasa.pds.tools.label.StandardPathResolver;
import gov.nasa.pds.web.ui.constants.DataSetConstants;
import gov.nasa.pds.web.ui.containers.IndexContainer;
import gov.nasa.pds.web.ui.containers.LabelContainer;
import gov.nasa.pds.web.ui.containers.LabelFragmentContainer;
import gov.nasa.pds.web.ui.containers.StatusContainer;
import gov.nasa.pds.web.ui.containers.dataSet.ValidationResults;
import gov.nasa.pds.web.ui.containers.dataSet.ValidationResults.NewValue;
import gov.nasa.pds.web.ui.containers.tabularData.Column;
import gov.nasa.pds.web.ui.containers.tabularData.Element;
import gov.nasa.pds.web.ui.containers.tabularData.Row;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Map.Entry;

public class DataSetValidator extends Observable {

	private static Dictionary masterDictionary;

	private static byte[] masterDDSerialized;

	protected Dictionary dictionary;

	protected ValidationResults results;

	private final File baseDirectory;

	private VolumeContainerSimple volume;

	private StandardPathResolver resolver = new StandardPathResolver();

	// folders that don't need to have labels indexed
	private final List<File> nonIndexedFolders = new ArrayList<File>();

	// folders that should not have labels indexed
	private final List<File> illegalIndexedFolders = new ArrayList<File>();

	// files that should be or are labels
	private final Map<Integer, File> labelFiles = new HashMap<Integer, File>();

	// files listed in an index, required files, pointed from labels, known
	// labels, etc
	private final Map<Integer, File> knownFiles = new HashMap<Integer, File>();

	// files listed in an index
	private final Map<Integer, File> indexedFiles = new HashMap<Integer, File>();

	// labels that don't need to be indexed and are known about
	private final Map<Integer, File> nonIndexedFiles = new HashMap<Integer, File>();

	// label fragments pointed to by other files
	private final Map<Integer, File> labelFragments = new HashMap<Integer, File>();

	private Map<Integer, File> files = new HashMap<Integer, File>();

	private File volDesc;

	private final List<Integer> validatedLabels = new ArrayList<Integer>(); // need_file?

	private final StatusContainer status;

	private long bucketCount = 1;

	public DataSetValidator(final String procId, final File baseDirectory) {
		this(procId, baseDirectory, null);
	}

	public DataSetValidator(final String procId, final File baseDirectory,
			final StatusContainer status) {
		this.baseDirectory = baseDirectory;
		this.status = status == null ? new StatusContainer() : status;
		this.results = new ValidationResults(procId);
	}

	public StatusContainer getStatus() {
		return this.status;
	}

	private void updateStatus(final String key) throws CancelledException {
		updateStatus(key, true);
	}

	private void updateStatus(final String key, Boolean incrementStep)
			throws CancelledException {
		if (this.status.isCancelled()) {
			throw new CancelledException();
		}
		if (incrementStep != null && incrementStep.equals(Boolean.TRUE)) {
			this.status.incrementStep();
		}
		this.status.setStatus(key);
		setChanged();
		notifyObservers(this.status);
	}

	protected void setVolume(final File baseDir) {
		this.volume = new VolumeContainerSimple(baseDir);
	}

	@SuppressWarnings("nls")
	public void validate() throws CancelledException {
		// get current date to find duration
		Date now = new Date();
		final long start = now.getTime();

		// initialize volume
		updateStatus("Initializing volume context.");
		setVolume(this.baseDirectory);

		this.resolver.setVolumeContext(this.volume);

		// getting list of files
		updateStatus("Initializing volume file listing.");
		this.files.putAll(FileUtils.getFileMap(this.baseDirectory));

		// creating list of folders to ignore in index check
		updateStatus("Setting folders to ignore in index checks.");
		for (final String nonIndexedFolderName : DataSetConstants.NON_INDEXED_FOLDERS) {
			this.nonIndexedFolders.add(new File(this.baseDirectory,
					nonIndexedFolderName));
			// lowercase in case people are using the wrong case
			this.nonIndexedFolders.add(new File(this.baseDirectory,
					nonIndexedFolderName.toLowerCase()));
		}

		// creating list of folders to error on if indexed
		updateStatus("Setting folders illegal to index.");
		for (final String illegalIndexedFolderName : DataSetConstants.ILLEGAL_INDEXED_FOLDERS) {
			this.illegalIndexedFolders.add(new File(this.baseDirectory,
					illegalIndexedFolderName));
			// lowercase in case people are using the wrong case
			this.illegalIndexedFolders.add(new File(this.baseDirectory,
					illegalIndexedFolderName.toLowerCase()));
		}

		// add illegal folders to non indexed for convenience
		this.nonIndexedFolders.addAll(this.illegalIndexedFolders);

		// set some volume properties
		updateStatus("Initializing volume structure.");
		this.results.setRootNode(this.baseDirectory);
		updateStatus("Initializing local dictionary if present.");
		initDictionary();
		updateStatus("Retrieving VOLDESC to set meta info on volume.");
		this.volDesc = getRelativeFile(DataSetConstants.VOLUME_DESC_FILE_NAME);
		updateStatus("Setting volume name.");
		this.results.setName(getVolumeName());

		// set volume id
		updateStatus("Setting volume id.");
		setVolumeId();

		// validate voldesc first since defines cat info
		updateStatus("Validating VOLDESC.CAT");
		validateVolDesc();
		// validate cat directory so we can know new values to suppress errors
		updateStatus("Validating CATALOG directory.");
		validateCatalogDirectory();

		// validate required files
		updateStatus("Validating AAREADME.TXT");
		validateReadme();
		updateStatus("Validating ERRATA.TXT");
		validateErrata();

		// validate naming convention for volume
		// updateStatus("Validating volume naming convention.");
		validateVolumeName();

		// validate known folders
		updateStatus("Validating BROWSE folder");
		validateBrowseFolder();
		updateStatus("Validating CALIMB folder");
		validateCalibFolder();
		updateStatus("Validating DOCUMENT folder");
		validateDocumentFolder();
		updateStatus("Validating EXTRAS folder");
		validateExtrasFolder();
		updateStatus("Validating GAZZETTER folder");
		validateGazetteerFolder();
		updateStatus("Validating GEOMETRY folder");
		validateGeometryFolder();
		updateStatus("Validating INDEX folder");
		validateIndexDirectory();
		updateStatus("Validating LABEL folder");
		validateLabelFolder();
		updateStatus("Validating SOFTWARE folder.");
		validateSoftwareFolder();

		// find label files (.lbl) that weren't in index and format files that
		// may or may not be used
		updateStatus("Finding labels not in index.");
		registerUnknownLabels();

		// validate all labels and linkages
		updateStatus("Validating labels.");
		validateLabels();

		// validate undocumented products
		updateStatus("Finding undocumented products and empty files");
		findUndocumentedAndEmpty();

		// send up last bucket
		this.status.setStatus("Sending last group of errors.");
		sendBucket();

		// clear maps
		updateStatus("Clearing temp data.");
		this.files.clear();
		this.illegalIndexedFolders.clear();
		this.indexedFiles.clear();
		this.knownFiles.clear();
		this.labelFiles.clear();
		this.labelFragments.clear();
		this.nonIndexedFiles.clear();
		this.nonIndexedFolders.clear();

		// get duration of validation
		now = new Date();
		final long end = now.getTime();
		this.results.setDuration(end - start);
		updateStatus("Validation complete, forwarding to results display.");
		this.status.setDone();
	}

	private String getVolumeName() {
		// TODO: get name from label or folder name if not found, see logic in
		// ManageDataSets
		return null;
	}

	public ValidationResults getResults() {
		return this.results;
	}

	private String getRelativePath(final File targetFile) {
		return FileUtils.getRelativePath(this.baseDirectory, targetFile);
	}

	private File getRelativeFile(final String relativePath) {
		return FileUtils.getCaseUnknownFile(this.baseDirectory, relativePath);
	}

	// only do this for things that aren't in other lists
	private void addKnownFile(final File knownFile) {
		Integer hashCode = knownFile.hashCode();
		if (!isKnownFile(hashCode)) {
			this.knownFiles.put(hashCode, knownFile);
		}
	}

	private boolean isKnownFile(final Integer hashCode) {
		if (this.knownFiles.containsKey(hashCode)) {
			return true;
		}
		if (this.indexedFiles.containsKey(hashCode)) {
			return true;
		}
		if (this.labelFiles.containsKey(hashCode)) {
			return true;
		}
		if (this.nonIndexedFiles.containsKey(hashCode)) {
			return true;
		}
		if (this.labelFragments.containsKey(hashCode)) {
			return true;
		}
		return false;
	}

	// all indexed files are labels, use this in conjunction with labelFiles for
	// label iteration
	private void addIndexedFile(final File indexedFile) {
		if (!this.indexedFiles.containsKey(indexedFile.hashCode())) {
			this.indexedFiles.put(indexedFile.hashCode(), indexedFile);
		}
	}

	// required files that don't need to be indexed
	private void addNonIndexedFile(final File nonIndexedFile) {
		if (!this.nonIndexedFiles.containsKey(nonIndexedFile.hashCode())) {
			this.nonIndexedFiles.put(nonIndexedFile.hashCode(), nonIndexedFile);
		}
	}

	// included files that don't need to be indexed
	private void addLabelFragment(final File fragment) {
		if (!this.labelFragments.containsKey(fragment.hashCode())) {
			this.labelFragments.put(fragment.hashCode(), fragment);
		}
	}

	private void addNewValue(final File problemFile,
			final LabelParserException lpe) {
		this.results.addNewValue(problemFile, lpe);
	}

	private boolean knownValue(final LabelParserException lpe) {
		for (final NewValue newVal : this.results.getNewValues()) {
			if (newVal.is(lpe)) {
				return true;
			}
		}
		return false;
	}

	private void markValidated(final File labelFile) {
		final int hashCode = labelFile.hashCode();
		if (!this.validatedLabels.contains(hashCode)) {
			this.validatedLabels.add(hashCode);
		}
	}

	private boolean validated(final File labelFile) {
		if (this.validatedLabels.contains(labelFile.hashCode())) {
			return true;
		}
		return false;
	}

	private void addProblem(final File problemFile, final LabelParserException e) {
		addProblem(problemFile, e.getKey(), e.getType(), e.getLineNumber(), e
				.getArguments());
	}

	private void addProblem(final File problemFile, final String key,
			final ProblemType type, final Object... arguments) {
		addProblem(problemFile, key, type, null, arguments);
	}

	@SuppressWarnings("nls")
	private void addProblem(final File problemFile, final String key,
			final ProblemType type, final Integer lineNumber,
			final Object... arguments) {
		File curProblemFile = problemFile;
		// if the file you're reporting a problem on doesn't exist, map problem
		// to the closest valid parent folder
		if (!FileUtils.exists(curProblemFile)) {
			curProblemFile = FileUtils.getValidParent(curProblemFile);
		}
		// if we never found a valid parent, the path doesn't refer to something
		// inside the data set, punt and say the problem is on the data set
		if (curProblemFile == null) {
			curProblemFile = this.baseDirectory;
		}

		// if a bucket is full, notify observers and allow them to send it over
		// the wire
		boolean full = this.results.addProblem(curProblemFile, key, type,
				lineNumber, arguments);
		if (full) {
			// TODO: get message through props file
			this.status.setStatus("Sending problem cluster " + this.bucketCount
					+ " to server.");
			this.bucketCount++;
			sendBucket();
		}
	}

	public void sendBucket() {
		setChanged();
		notifyObservers(this.results.getBucket());

	}

	// validation methods
	private void validateReadme() {
		final File readme = getRelativeFile(DataSetConstants.README_NAME);
		if (!FileUtils.exists(readme)) {
			addProblem(readme,
					"validation.error.missingRequiredFile", //$NON-NLS-1$
					ProblemType.MISSING_REQUIRED_RESOURCE,
					DataSetConstants.README_NAME);
		} else {
			addNonIndexedFile(readme);
		}
	}

	private void validateVolDesc() {
		// make sure that voldesc.cat exists in root folder
		if (!FileUtils.exists(this.volDesc)) {
			addProblem(this.volDesc,
					"validation.error.missingRequiredFile", //$NON-NLS-1$
					ProblemType.MISSING_REQUIRED_RESOURCE,
					DataSetConstants.VOLUME_DESC_FILE_NAME);
		} else {
			addNonIndexedFile(this.volDesc);
			// validate early so you can get new values
			validateLabel(this.volDesc, true);
		}
	}

	private void validateKnownFolder(final String folderName,
			final String fileName) {
		final File folder = getRelativeFile(folderName);
		if (folder.isDirectory()) {
			final File infoFile = FileUtils
					.getCaseUnknownFile(folder, fileName);
			if (!FileUtils.exists(infoFile)) {
				addProblem(infoFile,
						"validation.error.missingRequiredChild", //$NON-NLS-1$
						ProblemType.MISSING_REQUIRED_RESOURCE, fileName,
						folderName);
			} else {
				addNonIndexedFile(infoFile);
			}
		}
	}

	private void validateErrata() {
		final File errata = getRelativeFile(DataSetConstants.ERRATA_FILE_NAME);
		if (FileUtils.exists(errata)) {
			addNonIndexedFile(errata);
		}
	}

	private void validateVolumeName() {
		// TODO: make sure volume name is in all caps and is less than 60 chars
		// and matches things that reference volume_id
	}

	private void validateBrowseFolder() {
		validateKnownFolder(DataSetConstants.BROWSE_FOLDER_NAME,
				DataSetConstants.BROWSE_INFO_FILE_NAME);
	}

	private void validateCalibFolder() {
		validateKnownFolder(DataSetConstants.CALIB_FOLDER_NAME,
				DataSetConstants.CALIB_INFO_FILE_NAME);
	}

	private void validateDocumentFolder() {
		validateKnownFolder(DataSetConstants.DOCUMENT_FOLDER_NAME,
				DataSetConstants.DOCUMENT_INFO_FILE_NAME);
	}

	private void validateExtrasFolder() {
		validateKnownFolder(DataSetConstants.EXTRAS_FOLDER_NAME,
				DataSetConstants.EXTRAS_INFO_FILE_NAME);
	}

	private void validateGazetteerFolder() {
		validateKnownFolder(DataSetConstants.GAZETTEER_FOLDER_NAME,
				DataSetConstants.GAZETTEER_INFO_FILE_NAME);
		// TODO: do extra validation as required in 19.3.3.4 of spec
	}

	private void validateGeometryFolder() {
		validateKnownFolder(DataSetConstants.GEOMETRY_FOLDER_NAME,
				DataSetConstants.GEOMETRY_INFO_FILE_NAME);
	}

	private void validateLabelFolder() {
		validateKnownFolder(DataSetConstants.LABEL_FOLDER_NAME,
				DataSetConstants.LABEL_INFO_FILE_NAME);
	}

	private void validateSoftwareFolder() {
		validateKnownFolder(DataSetConstants.SOFTWARE_FOLDER_NAME,
				DataSetConstants.SOFTWARE_INFO_FILE_NAME);
	}

	// TODO: indication that there should be one index.lbl and index.tab, true?
	private void validateIndexDirectory() {

		final File indexFolder = getRelativeFile(DataSetConstants.INDEX_FOLDER_NAME);

		// check if index folder exists
		if (!indexFolder.isDirectory()) {
			addProblem(indexFolder,
					"validation.error.missingRequiredFolder", //$NON-NLS-1$
					ProblemType.MISSING_REQUIRED_RESOURCE,
					DataSetConstants.INDEX_FOLDER_NAME);
		} else {
			// make sure has required index info file
			try {
				final File indexInfo = FileUtils.getCaseUnknownFile(
						indexFolder, DataSetConstants.INDEX_INFO_FILE_NAME);
				// make sure that errata.txt exists in root folder
				if (!FileUtils.exists(indexInfo)) {
					addProblem(
							indexInfo,
							"validation.error.missingRequiredFile", //$NON-NLS-1$
							ProblemType.MISSING_REQUIRED_RESOURCE,
							DataSetConstants.INDEX_INFO_FILE_NAME);
				} else {
					addNonIndexedFile(indexInfo);
				}
			} catch (Exception e) {
				// noop - we already know the directory exists
			}

			List<File> foundFiles = FileUtils.getFiles(indexFolder,
					"^(.{3})?index\\.lbl$"); //$NON-NLS-1$
			for (File curFile : foundFiles) {
				IndexContainer index = null;
				try {
					index = new IndexContainer(curFile, this.volume,
							this.dictionary);

					// skip validation of files found in cumulative index
					if (!curFile.getName().matches(
							DataSetConstants.CUMINDEX_NAME_REGEX)) {
						// check to see if links from index tabular file are
						// valid
						final List<FileReference> linkedLabels = index
								.getLabelList();
						for (FileReference linkedLabel : linkedLabels) {
							File labelFile = new File(this.baseDirectory,
									linkedLabel.getPath());
							if (!FileUtils.exists(labelFile)) {
								labelFile = FileUtils.getAlternateCaseFile(
										this.baseDirectory, linkedLabel
												.getPath());
								if (!FileUtils.exists(labelFile)) {
									addProblem(
											index.getIndexDataFile(),
											"validation.error.missingLabel", //$NON-NLS-1$
											ProblemType.MISSING_INDEX_RESOURCE,
											linkedLabel.getLineNumber(),
											getRelativePath(labelFile));
								} else {
									addIndexedFile(labelFile);
									addProblem(
											index.getIndexDataFile(),
											"validation.error.mismatchedCase", //$NON-NLS-1$
											ProblemType.MISMATCHED_CASE,
											linkedLabel.getLineNumber(),
											getRelativePath(labelFile));
								}
							} else {
								addIndexedFile(labelFile);
							}
						}
					}
				} catch (Exception e) {
					addProblem(curFile, e.getMessage(),
							ProblemType.INVALID_LABEL);
				}
			}
			if (foundFiles.size() == 0) {
				addProblem(indexFolder, "validation.error.noIndexes", //$NON-NLS-1$
						ProblemType.MISSING_REQUIRED_RESOURCE);
			}
		}
	}

	// must be after validing index since depends on index being read
	private void registerUnknownLabels() {
		// get cluster of probable label-like files
		Map<Integer, File> probable = FileUtils.getFileMap(this.files,
				"^.*\\.(lbl|fmt)$"); //$NON-NLS-1$

		// find unknown complete labels
		Map<Integer, File> probableLables = FileUtils.getFileMap(probable,
				"^.*\\.lbl$"); //$NON-NLS-1$
		final Iterator<Entry<Integer, File>> it = probableLables.entrySet()
				.iterator();
		while (it.hasNext()) {
			final Entry<Integer, File> entry = it.next();
			final Integer hashCode = entry.getKey();
			if (!this.indexedFiles.containsKey(hashCode)) {
				this.labelFiles.put(hashCode, entry.getValue());
			}
		}

		// find format files
		Map<Integer, File> probableFormatFiles = FileUtils.getFileMap(probable,
				"^.*\\.fmt$"); //$NON-NLS-1$
		this.labelFragments.putAll(probableFormatFiles);
		probable.clear();
	}

	// find files that look like products and aren't in the index or exclusion
	// list, also find empty files/folders
	@SuppressWarnings("nls")
	private void findUndocumentedAndEmpty() throws CancelledException {
		long volumeSpace = 0;
		long numFiles = 0;
		long numFolders = 0;
		long curFile = 0;
		int filesFolders = this.files.size();

		final Iterator<Entry<Integer, File>> it = this.files.entrySet()
				.iterator();
		while (it.hasNext()) {
			curFile++;
			// TODO: get message through props file
			updateStatus("Validating file " + curFile + " of " + filesFolders
					+ ".", false);
			final Entry<Integer, File> entry = it.next();
			final File file = entry.getValue();
			final Integer key = entry.getKey();
			if (file.isDirectory()) {
				numFolders++;
				if (FileUtils.empty(file)) {
					addProblem(file, "validation.error.emptyDirectory", //$NON-NLS-1$
							ProblemType.EMPTY_FOLDER, file.getName());
				}
			} else if (file.isFile()) {
				numFiles++;
				final long size = file.length();
				if (file.length() == 0) {

					addProblem(file, "validation.error.emptyFile", //$NON-NLS-1$
							ProblemType.EMPTY_FILE, file.getName());
				} else {
					// add file size to total
					volumeSpace += size;
				}
				if (!isKnownFile(key)) {
					// test for attached label which has a more specific
					// complaint
					boolean found = validateSuspectedLabel(key, file);

					if (!found) {
						// TODO: complain if non-indexed data object
						addProblem(file, "validation.error.unknownFile", //$NON-NLS-1$
								ProblemType.UNKNOWN_FILE, file.getName());
					}
				}
			}
		}
		this.results.setVolumeSpace(volumeSpace);
		this.results.setNumFiles(numFiles);
		this.results.setNumFolders(numFolders);
	}

	private void validateCatalogDirectory() {
		final File catalogFolder = this.volume.getCatalogFolder();

		// check if index folder exists
		if (!catalogFolder.isDirectory()) {
			addProblem(catalogFolder,
					"validation.error.missingRequiredFolder", //$NON-NLS-1$
					ProblemType.MISSING_REQUIRED_RESOURCE,
					DataSetConstants.CATALOG_FOLDER_NAME);
		} else {
			File catInfo = FileUtils.getCaseUnknownFile(catalogFolder,
					DataSetConstants.CATALOG_INFO_FILE_NAME);
			if (!FileUtils.exists(catInfo)) {
				addProblem(catInfo,
						"validation.error.missingRequiredFile", //$NON-NLS-1$
						ProblemType.MISSING_REQUIRED_RESOURCE,
						DataSetConstants.CATALOG_INFO_FILE_NAME);
			} else {
				addNonIndexedFile(catInfo);
			}
			// validate early so you can get new values
			final List<File> catFiles = FileUtils.getFiles(catalogFolder,
					"^.*\\.cat$"); //$NON-NLS-1$
			for (final File catFile : catFiles) {
				validateLabel(catFile, true);
			}
		}
	}

	private void validateKnownLabels(final Map<Integer, File> testFiles,
			final String labelType) throws CancelledException {
		validateKnownLabels(testFiles, labelType, true);
	}

	@SuppressWarnings("nls")
	private void validateKnownLabels(final Map<Integer, File> testFiles,
			final String labelType, final boolean checkIndexed)
			throws CancelledException {
		final Iterator<Entry<Integer, File>> labelFilesIt = testFiles
				.entrySet().iterator();
		int numFiles = testFiles.size();
		int curFile = 0;
		while (labelFilesIt.hasNext()) {
			final Entry<Integer, File> entry = labelFilesIt.next();
			final File labelFile = entry.getValue();
			curFile++;
			// TODO: get message through props file
			updateStatus("Validating " + labelType + " " + curFile + " of "
					+ numFiles + ".", false);
			// make sure label was in index if it's supposed to be
			final Integer hashCode = entry.getKey();
			if (checkIndexed) {
				verifyIndexed(labelFile, hashCode);
			}
			// do the actual validation - protected from duplicate validation
			// force capture of parse exceptions
			validateLabel(labelFile, true);
		}
	}

	@SuppressWarnings("nls")
	private void validateLabelFragments() throws CancelledException {
		final Iterator<Entry<Integer, File>> labelFilesIt = this.labelFragments
				.entrySet().iterator();
		int numFiles = this.labelFragments.size();
		int curFile = 0;
		while (labelFilesIt.hasNext()) {
			final Entry<Integer, File> entry = labelFilesIt.next();
			final File labelFile = entry.getValue();
			curFile++;
			// TODO: get message through props file
			updateStatus("Validating label fragment " + curFile + " of "
					+ numFiles + ".", false);

			// do the actual validation - protected from duplicate validation
			// force capture of parse exceptions
			final LabelFragmentContainer fragment = new LabelFragmentContainer(
					labelFile, this.volume, this.dictionary);
			validateLabel(fragment, true);
		}
	}

	private void verifyIndexed(final File testFile, final Integer hashCode) {
		if (!this.indexedFiles.containsKey(hashCode)
				&& !this.nonIndexedFiles.containsKey(hashCode)
				&& !FileUtils.hasParent(this.nonIndexedFolders, testFile)) {
			addProblem(testFile, "validation.error.unIndexedLabel", //$NON-NLS-1$
					ProblemType.LABEL_NOT_INDEXED, testFile.getName());
		}
	}

	// TODO: validate tabular information
	@SuppressWarnings("nls")
	private void validateLabels() throws CancelledException {
		// validate labels from index
		validateKnownLabels(this.indexedFiles, "indexed label", false);

		// special label files that aren't indexed like readmes
		validateKnownLabels(this.nonIndexedFiles, "known label", false);

		// validate labels added to label list manually
		validateKnownLabels(this.labelFiles, "presumed label");

		// validate label fragments (included files)
		validateLabelFragments();

		// make sure indexed files were supposed to be indexed
		final Iterator<Entry<Integer, File>> indexedFilesIt = this.indexedFiles
				.entrySet().iterator();
		while (indexedFilesIt.hasNext()) {
			final Entry<Integer, File> entry = indexedFilesIt.next();
			final File indexedFile = entry.getValue();
			if (FileUtils.hasParent(this.illegalIndexedFolders, indexedFile)) {
				addProblem(indexedFile, "validation.error.illegalIndexedLabel", //$NON-NLS-1$
						ProblemType.LABEL_NOT_TO_BE_INDEXED, indexedFile
								.getName());
			}
		}
	}

	private boolean validateSuspectedLabel(final Integer key, final File file) {
		if (!isKnownFile(key)) {
			final String extension = FileUtils.getExtension(file).toLowerCase();
			if (!DataSetConstants.NON_PROCESSED_TYPES.contains(extension)) {

				// this only adds errors if it IS a label
				LabelContainer label = validateLabel(file);
				// if a label, make sure indexed
				if (label.isValid()) {
					verifyIndexed(file, file.hashCode());
					// make sure we don't double report an error
					// addKnownFile(file);
					// now returns a found flag to indicate not to double report
					return true;
				}
			}
		}
		return false;
	}

	protected LabelContainer validateLabel(File labelFile) {
		return validateLabel(labelFile, false);
	}

	protected LabelContainer validateLabel(final File labelFile,
			final boolean forceValidate) {
		final LabelContainer label = new LabelContainer(labelFile, this.volume,
				this.dictionary, forceValidate);
		return validateLabel(label, forceValidate);
	}

	private LabelContainer validateLabel(final LabelContainer label,
			final boolean forceValidate) {
		final File labelFile = label.getLabelFile();
		boolean useNewValues = labelFile.equals(this.volDesc)
				|| FileUtils
						.isParent(this.volume.getCatalogFolder(), labelFile);
		// only validate if it's a label and it has not been validated yet
		if (!validated(labelFile)) {
			if (label.isValid()) {
				final List<PointerStatement> pointers = label.getPointers();

				for (final PointerStatement pointer : pointers) {

					// TODO: switch to getting file map and use start
					// position
					List<File> pointerFiles = this.resolver
							.resolveFiles(pointer);
					for (File searchFile : pointerFiles) {
						if (FileUtils.exists(searchFile)) {
							if (pointer instanceof IncludePointer) {
								// TODO: suppress .cat files? shouldn't
								// matter since already validated but...
								addLabelFragment(searchFile);
							} else {
								addKnownFile(searchFile);
							}
							// validate naming convention
							final String labelName = FileUtils
									.getBaseName(labelFile);
							final String targetName = FileUtils
									.getBaseName(searchFile);
							if (!targetName.startsWith(labelName)
									&& !(pointer instanceof SpecialPointer)) {
								final String expectedName = labelName + "." //$NON-NLS-1$
										+ FileUtils.getExtension(searchFile);
								addProblem(
										labelFile,
										"validation.error.badPointerName", //$NON-NLS-1$
										ProblemType.POTENTIAL_POINTER_PROBLEM,
										pointer.getLineNumber(), labelFile
												.getName(), searchFile
												.getName(), expectedName);
							}
						} else {
							addProblem(
									labelFile,
									"validation.error.missingTargetFile", //$NON-NLS-1$
									ProblemType.MISSING_RESOURCE, pointer
											.getLineNumber(),
									getRelativePath(searchFile));
						}
					}

					// validate tabular data
					try {
						// just get the first three rows to test
						// TODO: return to 3 when you fix below to only add a
						// problem
						// once for a given type of error
						TabularData tabularData = label.getTabularData(1);
						validateTabularFile(label, tabularData);
					} catch (Exception e) {
						// should just be bad pointer, handled elsewhere
					}

					// TODO: if .fmt file found, try to validate format of
					// referenced
					// file
				}

				// do pass through of problems last since new ones may be added
				// in tabular validation
				for (LabelParserException e : label.getProblems()) {
					final ProblemType probType = e.getType();
					if (probType.equals(ProblemType.UNKNOWN_VALUE)
							|| probType.equals(ProblemType.INVALID_VALUE)) {
						if (useNewValues) {
							// add to list of exception values
							addNewValue(labelFile, e);
						} else if (!knownValue(e)) {
							// not in list of exceptions, just add problem
							addProblem(labelFile, e);
						}
					} else {
						addProblem(labelFile, e);
					}
				}

				// mark validated
				markValidated(labelFile);
			} else if (forceValidate) {
				for (LabelParserException e : label.getProblems()) {
					addProblem(labelFile, e);
				}
				// mark validated
				// NOTE: not moving outside of if statements since possibility
				// that not capturing parse errors first time and may be
				// swallowed
				markValidated(labelFile);
			}
		}
		return label;
	}

	private void validateTabularFile(final LabelContainer label,
			final TabularData tabularData) {
		if (tabularData != null) {
			// check row length
			final List<Row> rows = tabularData.getRows();
			int numColumns = 0;
			int definedColumns = tabularData.getColumnDefs().size();
			for (final Row row : rows) {
				final List<Element> elements = row.getElements();
				numColumns = Math.max(numColumns, elements.size());
			}
			if (numColumns != definedColumns) {
				addProblem(
						tabularData.getDataFile(),
						"validation.error.columnNumberMismatch", //$NON-NLS-1$
						ProblemType.COLUMN_NUMBER_MISMATCH, null, numColumns,
						definedColumns);
			}

			// compare elements to column defs
			final List<Column> columns = tabularData.getColumns();
			for (final Column column : columns) {
				final List<Element> elements = column.getElements();
				for (final Element element : elements) {
					final List<String> values = element.getValues();
					for (final String value : values) {
						validateTabElement(value, column, tabularData);
					}
				}
			}
		}
		// if file is selected file, validate all rows and do extra
		// validation
		// make sure to note problem discrepancy as extra validation in
		// header area

		// pass through problems
		if (tabularData != null) {
			label.addProblems(tabularData.getProblems());
		}

	}

	private void setVolumeId() {
		String volumeId = null;
		if (this.volDesc.exists()) {
			final LabelContainer label = new LabelContainer(this.volDesc,
					this.volume, this.dictionary, true);
			if (label.isValid()) {
				List<ObjectStatement> volumeStatements = label.getLabelObj()
						.getObjects("VOLUME"); //$NON-NLS-1$
				if (volumeStatements != null && volumeStatements.size() > 0) {
					ObjectStatement volumeStatement = volumeStatements.get(0);

					// get id
					AttributeStatement volIdStatement = volumeStatement
							.getAttribute("VOLUME_ID"); //$NON-NLS-1$
					if (volIdStatement != null) {
						volumeId = volIdStatement.getValue().toString();
					}
				}
			}
		}
		if (volumeId == null) {
			volumeId = this.baseDirectory.getName();
		}
		this.results.setVolumeId(volumeId);
	}

	protected void initDictionary() {
		initMasterDictionary();

		// find local dictionary if present
		final File docFolder = getRelativeFile(DataSetConstants.DOCUMENT_FOLDER_NAME);
		List<File> dictionaries = new ArrayList<File>();
		if (docFolder != null && docFolder.exists()) {
			dictionaries = FileUtils.getFiles(docFolder, "^.*\\.ful$"); //$NON-NLS-1$
		}

		if (dictionaries.size() > 0) {
			// get copy of master dictionary
			Dictionary master = getMasterDictionaryCopy();

			// initialize merged dictionary
			for (final File dictionaryFile : dictionaries) {
				// mark dictionary as a "label" that's been seen
				addKnownFile(dictionaryFile);
				Dictionary localDictionary = null;
				try {
					localDictionary = DictionaryParser.parse(dictionaryFile,
							true);
					// pass through problems
					final List<LabelParserException> problems = localDictionary
							.getProblems();
					for (final LabelParserException problem : problems) {
						addProblem(dictionaryFile, problem);
					}

					// clear problems from dictionary to reduce memory -
					localDictionary.clearProblems();

					// merge local into the master
					master.merge(localDictionary);

					// pass through definition changes from merge
					this.results.setDictionaryChanges(master.getMergeChanges());

				} catch (LabelParserException e) {
					// TODO: push parse exceptions into the Dictionary container
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// set dictionary as merged master
			this.dictionary = master;
		}

		if (this.dictionary == null) {
			this.dictionary = masterDictionary;
		}
	}

	public static synchronized void initMasterDictionary(final URL dictionaryURL) {
		if (masterDictionary == null) {
			try {
				masterDictionary = DictionaryParser.parse(dictionaryURL);
			} catch (LabelParserException e) {
				// TODO: consider what to do with parse issues, note that won't
				// be thrown in figure, just added to container
				e.printStackTrace();
			} catch (IOException e) {
				// shouldn't happen
				e.printStackTrace();
			}
		}
	}

	protected static synchronized void initMasterDictionary() {
		if (masterDictionary == null) {
			File dataDictionaryFile;
			URL dictionaryURL = DataSetValidator.class.getResource("/masterdd.full"); //$NON-NLS-1$
			try {
				dataDictionaryFile = new File(dictionaryURL.toURI());
			} catch (URISyntaxException e) {
				dataDictionaryFile = new File(dictionaryURL.getPath());
			}

			try {
				masterDictionary = DictionaryParser.parse(dataDictionaryFile);
			} catch (LabelParserException e) {
				// TODO: consider what to do with parse issues, note that won't
				// be thrown in figure, just added to container
				e.printStackTrace();
			} catch (IOException e) {
				// shouldn't happen
				e.printStackTrace();
			}
		}
	}

	// TODO: right location for the retrieval of this? move to a manager?
	public static Dictionary getMasterDictionary() {
		initMasterDictionary();
		return masterDictionary;
	}

	public static void initMasterDDSerialized() {
		if (masterDDSerialized == null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos;
			try {
				oos = new ObjectOutputStream(baos);

				oos.writeObject(getMasterDictionary());
				masterDDSerialized = baos.toByteArray();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static Dictionary getMasterDictionaryCopy() {
		initMasterDDSerialized();
		ByteArrayInputStream bais = new ByteArrayInputStream(masterDDSerialized);
		ObjectInputStream ois;
		try {
			ois = new ObjectInputStream(bais);
			Dictionary deepCopy = (Dictionary) ois.readObject();
			return deepCopy;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// validate a single value or portion of a value against the column def
	// TODO: factor out each check to own method
	private void validateTabElement(final String value, final Column column,
			final TabularData tabularData) {
		// don't consider the quotes as part of the value except
		// for evaluating character type matching
		String stringVal = StrUtils.dequote(value);
		final String dataType = column.getDataType();

		int actualBytes;
		// TODO: is forcing ASCII necessary?
		// TODO: should actual bytes include padding?
		final String trimmed = value.trim();
		try {
			actualBytes = trimmed.getBytes(DataSetConstants.TEXT_FILE_ENCODING).length;
		} catch (UnsupportedEncodingException e) {
			actualBytes = trimmed.getBytes().length;
		}

		// check data type
		boolean badType = false;
		if (dataType.equals("ASCII_REAL")) { //$NON-NLS-1$
			if (!value.matches(Constants.ASCII_REAL_REGEX)) {
				badType = true;
			}
		} else if (dataType.equals("ASCII_INTEGER")) { //$NON-NLS-1$
			if (!value.matches(Constants.ASCII_INTEGER_REGEX)) {
				badType = true;
			}
		} else if (dataType.matches("INTEGER|MSB_INTEGER|MAC_INTEGER")) { //$NON-NLS-1$
			// wrong byte count
			// TODO: is this correct? see spec
			// http://pds.jpl.nasa.gov/documents/sr/Chapter03.pdf
			// page 3
			if (actualBytes != 1 && actualBytes != 2 && actualBytes != 4) {
				addProblem(
						tabularData.getDataFile(),
						"validation.error.badInteger", //$NON-NLS-1$
						ProblemType.INVALID_VALUE_FOR_COLUMN, column.getName(),
						dataType, actualBytes, value);
			} else if (!stringVal.matches(Constants.ASCII_INTEGER_REGEX)) {
				badType = true;
				// TODO: make better error message instead of
				// generic?
			}
		} else if (dataType.equals("DATE")) { //$NON-NLS-1$
			// TODO: determine if there is any flexibility with
			// format. Spec indicates must use milliseconds but
			// then shows example without milliseconds
			Date testDate = null;
			try {
				// TODO: Label wrap is a hack to get the file through the
				// formatter, need to update to take file or url from variety of
				// containers
				testDate = DateTimeFormatter.parse(new Label(tabularData
						.getDataFile()), value, 1);
			} catch (Exception e) {
				// noop
			}
			if (testDate == null) {
				addProblem(
						tabularData.getDataFile(),
						"validation.error.badDate", //$NON-NLS-1$
						ProblemType.INVALID_VALUE_FOR_COLUMN, column.getName(),
						dataType, value);
			}
		} else if (dataType.equals("CHARACTER")) { //$NON-NLS-1$
			String originalStringVal = value;
			// if has space and not quoted, bad char value
			if (!originalStringVal.matches(Constants.CHARACTER_REGEX)) {
				badType = true;
			}
		}

		if (badType) {
			addProblem(
					tabularData.getDataFile(),
					"validation.error.columnTypeMismatch", //$NON-NLS-1$
					ProblemType.INVALID_VALUE_FOR_COLUMN, column.getName(),
					dataType, value);
		}

		// TODO: add tests for other types listed below
		// BOOLEAN - a single digit number, anything not 1 or 0
		// is a warning
		// EBCDIC_CHARACTER -
		// http://en.wikipedia.org/wiki/EBCDIC
		// IBM_COMPLEX IBM_INTEGER IBM_REAL IBM_UNSIGNED_INTEGER
		// IEEE_COMPLEX IEEE_REAL LSB_BIT_STRING LSB_INTEGER
		// LSB_UNSIGNED_INTEGER MAC_COMPLEX alias for
		// IEEE_COMPLEX
		// MAC_INTEGER MAC_REAL MAC_UNSIGNED_INTEGER
		// MSB_BIT_STRING MSB_INTEGER MSB_UNSIGNED_INTEGER

		// check max and min, need to do unit conversion to support this
	}

}
