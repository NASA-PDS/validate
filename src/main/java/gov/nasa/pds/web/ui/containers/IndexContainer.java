package gov.nasa.pds.web.ui.containers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import gov.nasa.arc.pds.tools.util.StrUtils;
import gov.nasa.pds.tools.LabelParserException;
import gov.nasa.pds.tools.constants.Constants.ProblemType;
import gov.nasa.pds.tools.containers.FileReference;
import gov.nasa.pds.tools.containers.VolumeContainerSimple;
import gov.nasa.pds.tools.dict.Dictionary;
import gov.nasa.pds.tools.label.AttributeStatement;
import gov.nasa.pds.tools.label.Label;
import gov.nasa.pds.tools.label.Numeric;
import gov.nasa.pds.tools.label.ObjectStatement;
import gov.nasa.pds.tools.label.PointerStatement;

@SuppressWarnings("nls")
public class IndexContainer extends LabelContainer {

  private File indexDataFile;

  private long indexDataStartByte;

  private Integer fileSpecNameColumnStartByte;

  private Integer fileSpecNameColumnEndByte;

  private Integer pathNameColumnStartByte;

  private Integer pathNameColumnEndByte;

  private Integer fileNameColumnStartByte;

  private Integer fileNameColumnEndByte;

  private boolean useFileSpecName = false;

  private ObjectStatement indexTableDef;

  public IndexContainer(final File labelFile, final VolumeContainerSimple volume,
      final Dictionary dictionary) {
    super(labelFile, volume, dictionary, true);
    List<ObjectStatement> indexTableStatements = this.labelObj.getObjects("INDEX_TABLE");

    if (indexTableStatements.size() != 1) {
      throw new RuntimeException(
          "No INDEX_TABLE definition was found describing the contents of the tabular information describing the files in the data set");
    }
    this.indexTableDef = indexTableStatements.get(0);

    Numeric startPosition = null;

    for (PointerStatement pointer : this.pointers) {
      if (pointer.getIdentifier().toString().equals("INDEX_TABLE")) {
        Entry<Numeric, File> entry = getFileEntry(pointer);
        this.indexDataFile = entry.getValue();
        startPosition = entry.getKey();
        break;
      }
    }

    // TODO: add error instead of throwing so that
    if (this.indexDataFile == null) {
      throw new RuntimeException(
          "No reference was found for an INDEX_TABLE. An external tabular file is required to index files in the data set");
    }

    final List<ObjectStatement> columns = this.indexTableDef.getObjects("COLUMN");
    for (ObjectStatement column : columns) {
      final AttributeStatement nameAttrib = column.getAttribute("NAME");
      final String name = nameAttrib.getValue().toString();
      // get pertinent positions for descriptors of full path (A.21.4.1)
      // TODO: support logical volumes
      // TODO: add error if don't get file spec name or path + file name?
      // Should dictionary do this?
      if (name.equalsIgnoreCase("FILE_SPECIFICATION_NAME")) {
        this.useFileSpecName = true;
        ColumnInfo columnInfo = new ColumnInfo(column);
        this.fileSpecNameColumnStartByte = columnInfo.getStartByte() - 1;
        this.fileSpecNameColumnEndByte = columnInfo.getBytes() + this.fileSpecNameColumnStartByte;
      } else if (name.equalsIgnoreCase("PATH_NAME")) {
        ColumnInfo columnInfo = new ColumnInfo(column);
        this.pathNameColumnStartByte = columnInfo.getStartByte() - 1;
        this.pathNameColumnEndByte = columnInfo.getBytes() + this.pathNameColumnStartByte;
      } else if (name.equalsIgnoreCase("FILE_NAME")) {
        ColumnInfo columnInfo = new ColumnInfo(column);
        this.fileNameColumnStartByte = columnInfo.getStartByte() - 1;
        this.fileNameColumnEndByte = columnInfo.getBytes() + this.fileNameColumnStartByte;
      }
    }

    // make sure have enough info to collect labels
    if (this.fileSpecNameColumnStartByte == null
        && (this.pathNameColumnStartByte == null || this.fileNameColumnStartByte == null)) {
      throw new RuntimeException(
          "FILE_SPECIFICATION_NAME or PATH_NAME + FILE_NAME are required columns in INDEX_TABLE to resolve label indexing.");
    }

    this.indexDataStartByte = Label.getSkipBytes(this.labelObj, startPosition);
  }

  // NOTE: getting directly instead of using TabularData due to memory issues
  public List<FileReference> getLabelList() {
    final List<FileReference> labelList = new ArrayList<>();

    try {

      // need to handle here if url case is wrong?
      final BufferedReader br = new BufferedReader(
          new InputStreamReader(this.indexDataFile.toURI().toURL().openStream()));
      String currLine = null;
      int lineNumber = 1;
      br.skip(this.indexDataStartByte);

      while ((currLine = br.readLine()) != null) {
        // get file ref part of line
        // TODO: clean this up
        String labelString;
        try {
          if (this.useFileSpecName) {
            labelString = currLine.substring(this.fileSpecNameColumnStartByte,
                this.fileSpecNameColumnEndByte);
            labelString = StrUtils.dequote(labelString).trim();
          } else {
            String path =
                currLine.substring(this.pathNameColumnStartByte, this.pathNameColumnEndByte);
            path = StrUtils.dequote(path).trim();
            String fileName =
                currLine.substring(this.fileNameColumnStartByte, this.fileNameColumnEndByte);
            fileName = StrUtils.dequote(fileName).trim();
            labelString = path + fileName;
          }

          // create file from file ref
          final FileReference reference = new FileReference(labelString, lineNumber);
          // add to list
          labelList.add(reference);
        } catch (final StringIndexOutOfBoundsException sie) {
          Integer startByte;
          Integer endByte;
          if (this.useFileSpecName) {
            startByte = this.fileSpecNameColumnStartByte;
            endByte = this.fileSpecNameColumnEndByte;
          } else {
            startByte = Math.min(this.pathNameColumnStartByte, this.fileNameColumnStartByte);
            endByte = Math.max(this.pathNameColumnEndByte, this.fileNameColumnEndByte);
          }
          int lineLength = currLine.length();
          LabelParserException lpe;
          if (startByte <= lineLength) {
            lpe = new LabelParserException(this.indexDataFile, lineNumber, null,
                "validation.error.columnOutOfRange", ProblemType.COLUMN_DEF_OOR, startByte,
                lineLength);
          } else {
            int columnLength = endByte - startByte;
            int remaining = lineLength - startByte;
            lpe = new LabelParserException(this.indexDataFile, lineNumber, null,
                "validation.error.columnOutOfRange", ProblemType.COLUMN_DEF_OOR, columnLength,
                remaining);
          }

          this.problems.add(lpe);
        }

        lineNumber++;
      }

    } catch (IOException e) {
      // TODO Auto-generated catch block
      throw new RuntimeException("Problem reading source file:" + this.indexDataFile.toString());
    }

    return labelList;
  }

  public File getIndexDataFile() {
    return this.indexDataFile;
  }

}
