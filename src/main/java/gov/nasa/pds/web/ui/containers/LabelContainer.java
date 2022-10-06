package gov.nasa.pds.web.ui.containers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import gov.nasa.arc.pds.tools.util.FileUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.containers.FileReference;
import gov.nasa.pds.tools.containers.VolumeContainerSimple;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Numeric;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;
import gov.nasa.pds.tools.label.Scalar;
import gov.nasa.pds.tools.label.Sequence;
import gov.nasa.pds.tools.label.Set;
import gov.nasa.pds.tools.label.StandardPathResolver;
import gov.nasa.pds.tools.label.Value;
import gov.nasa.pds.tools.label.parser.DefaultLabelParser;
import gov.nasa.pds.tools.label.validate.Validator;
import gov.nasa.pds.web.ui.utils.TabularData;

public class LabelContainer {

  protected final List<LabelParserException> problems = new ArrayList<>();

  protected final List<PointerStatement> pointers = new ArrayList<>();

  protected Label labelObj;

  protected File labelFile;

  protected URL labelUrl;

  protected boolean isValid = true;

  protected boolean validated = false;

  protected TabularData tabularData;

  protected StandardPathResolver resolver = new StandardPathResolver();

  protected boolean initLabel(final VolumeContainerSimple volume, final Dictionary dictionary,
      final boolean forceParse) {
    this.resolver.setVolumeContext(volume);
    DefaultLabelParser parser = new DefaultLabelParser(this.resolver);
    try {
      if (this.labelFile != null) {
        this.labelObj = parser.parseLabel(this.labelFile, forceParse);
      } else {
        this.labelObj = parser.parseLabel(this.labelUrl, forceParse);
      }
      Validator validator = new Validator();
      validator.validate(this.labelObj, dictionary);

      // pass values through
      this.problems.addAll(this.labelObj.getProblems());

      // get pointers
      // get top level pointers
      final List<PointerStatement> foundPointers = this.labelObj.getPointers();
      if (foundPointers != null) {
        this.pointers.addAll(foundPointers);
      }
      // recursively get pointers hanging on objects
      final List<ObjectStatement> foundObjects = this.labelObj.getObjects();
      for (ObjectStatement object : foundObjects) {
        addPointers(object);
      }
      return true;
    } catch (LabelParserException e) {
      this.isValid = false;
      this.problems.add(e);
      return false;
    } catch (IOException e) {
      // this.errors.add(e);
      e.printStackTrace();
      return false;
    }
  }

  public LabelContainer(final File labelFile, final VolumeContainerSimple volume,
      final Dictionary dictionary) {
    this.labelFile = labelFile;
    initLabel(volume, dictionary, false);
  }

  public LabelContainer(URL labelUrl, final VolumeContainerSimple volume,
      final Dictionary dictionary) {
    this.labelUrl = labelUrl;
    initLabel(volume, dictionary, false);
  }

  public LabelContainer(final File labelFile, final VolumeContainerSimple volume,
      final Dictionary dictionary, final boolean forceParse) {
    this.labelFile = labelFile;
    initLabel(volume, dictionary, forceParse);
  }

  public LabelContainer(URL labelUrl, final VolumeContainerSimple volume,
      final Dictionary dictionary, final boolean forceParse) {
    this.labelUrl = labelUrl;
    initLabel(volume, dictionary, forceParse);
  }

  public static List<FileReference> getFiles(final PointerStatement pointer) {
    final List<FileReference> files = new ArrayList<>();
    final Value value = pointer.getValue();
    if (value instanceof Set) {
      Iterator<Scalar> it = ((Set) value).iterator();
      while (it.hasNext()) {
        final String fileName = it.next().toString();
        FileReference fileRef =
            new FileReference(fileName, pointer.getLineNumber(), pointer.getIdentifier());
        files.add(fileRef);
      }
    } else if (value instanceof Sequence) {
      final String fileName = ((Sequence) value).get(0).toString();
      FileReference fileRef =
          new FileReference(fileName, pointer.getLineNumber(), pointer.getIdentifier());
      files.add(fileRef);
    } else {
      final String fileName = value.toString();
      FileReference fileRef =
          new FileReference(fileName, pointer.getLineNumber(), pointer.getIdentifier());
      files.add(fileRef);
    }
    return files;
  }

  public void addProblems(final List<LabelParserException> lpes) {
    this.problems.addAll(lpes);
  }

  public List<LabelParserException> getProblems() {
    return this.problems;
  }

  public List<PointerStatement> getPointers() {
    return this.pointers;
  }

  public Label getLabelObj() {
    return this.labelObj;
  }

  // TODO: turn this into a stack search rather than recursive
  protected void addPointers(final ObjectStatement object) {
    final List<PointerStatement> foundPointers = object.getPointers();
    this.pointers.addAll(foundPointers);
    final List<ObjectStatement> foundObjects = object.getObjects();
    for (final ObjectStatement curObject : foundObjects) {
      addPointers(curObject);
    }
  }

  public static boolean statementsContain(final String identifier, List<FileReference> statements) {
    return findPointer(identifier, statements) != null;
  }

  public static FileReference findPointer(final String identifier, List<FileReference> statements) {
    for (final FileReference statement : statements) {
      if (statement.getIdentifier().equals(identifier)) {
        return statement;
      }
    }
    return null;
  }

  public PointerStatement findPointer(final String identifier) {
    for (final PointerStatement pointer : this.pointers) {
      if (pointer.getIdentifier().toString().equals(identifier)) {
        return pointer;
      }
    }
    return null;
  }

  public File getFile(FileReference fileRef) {
    String fileName = fileRef.getPath();
    return new File(FileUtils.getBaseFile(this.labelFile), fileName);
  }

  public File getLabelFile() {
    return this.labelFile;
  }

  public URL getLabelUrl() {
    return this.labelUrl;
  }

  public boolean isValid() {
    return this.isValid;
  }

  public boolean validated() {
    return this.validated;
  }

  public void markValidated() {
    this.validated = true;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if ((obj == null) || (obj.getClass() != this.getClass())) {
      return false;
    }
    LabelContainer label = (LabelContainer) obj;
    return label.getLabelFile().equals(this.labelFile);
  }

  @Override
  public int hashCode() {
    return this.labelFile.hashCode();
  }

  public TabularData getTabularData(long numRows) {
    if (this.tabularData == null) {
      this.tabularData = getTabularData("TABLE", numRows); //$NON-NLS-1$
      if (this.tabularData == null) {
        this.tabularData = getTabularData("INDEX_TABLE", numRows); //$NON-NLS-1$
      }
    }
    return this.tabularData;
  }

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
          throw new RuntimeException("Referenced tabular data file does not exist"); //$NON-NLS-1$
        }
      } else {
        // get file - assume only one file
        Entry<Numeric, File> entry = getFileEntry(tablePointer);
        tabularFile = entry.getValue();
        startPosition = entry.getKey();

        if (!FileUtils.exists(tabularFile)) {
          throw new RuntimeException("Referenced tabular data file does not exist"); //$NON-NLS-1$
        }
      }
    }

    final long startByte = Label.getSkipBytes(this.labelObj, startPosition);

    final List<ObjectStatement> foundObjects = this.labelObj.getObjects(tableType);
    if (foundObjects.size() == 1) {
      final List<ColumnInfo> columns = new ArrayList<>();
      ObjectStatement tableObj = foundObjects.get(0);
      AttributeStatement tableFormat = tableObj.getAttribute("INTERCHANGE_FORMAT"); //$NON-NLS-1$
      if (tableFormat != null) {
        String tableFormatString = tableFormat.getValue().toString();
        if (tableFormatString.equalsIgnoreCase("BINARY")) { //$NON-NLS-1$
          return null;
        }
      }

      for (ObjectStatement column : tableObj.getObjects("COLUMN")) { //$NON-NLS-1$
        columns.add(new ColumnInfo(column));
      }

      if (tabFileUrl != null) {
        return new TabularData(tabFileUrl, columns, startByte, numRows);
      }
      return new TabularData(tabularFile, columns, startByte, numRows);

    }

    return null;
  }

  public Map<Numeric, File> getFileMap(final PointerStatement pointer) {
    return this.resolver.resolveFileMap(pointer);
  }

  public Entry<Numeric, File> getFileEntry(final PointerStatement pointer) {
    Map<Numeric, File> map = getFileMap(pointer);
    return map.entrySet().iterator().next();
  }

  public Map<Numeric, URI> getURIMap(final PointerStatement pointer) {
    return this.resolver.resolveURIMap(pointer);
  }

  public Entry<Numeric, URI> getURIEntry(final PointerStatement pointer) {
    Map<Numeric, URI> map = getURIMap(pointer);
    return map.entrySet().iterator().next();
  }

  public File getFirstFile(final PointerStatement pointer) {
    Entry<Numeric, File> entry = getFileEntry(pointer);
    return entry.getValue();
  }

  public URI getFirstURI(final PointerStatement pointer) {
    Entry<Numeric, URI> entry = getURIEntry(pointer);

    // Temporary fix to correct label data file error. The error consists
    // of the table pointer name that contains the incorrect formatting for
    // the data object pointer. The usual formats are
    // ^TABLE = "DATA"
    // ^TABLE = ("DATA", <BYTES>)
    // However, in some labels the table pointer was formatted as follows
    // ^TABLE = "(DATA, <BYTES>)"
    Pattern p = Pattern.compile("(.*)\\(([^,]+),([0-9]+)\\)");
    Matcher m = p.matcher(entry.getValue().toString());

    if (!m.matches()) {
      return entry.getValue();
    }
    String url = m.group(1);
    String dataFileName = m.group(2);

    // Construct a new URI with the corrected address
    try {
      URI newURI = new URI(url + dataFileName);
      return newURI;
    } catch (URISyntaxException e) {
      return entry.getValue();
    }

  }

}
