package gov.nasa.pds.web.ui.containers.tabularData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: figure out how to get get start byte position
public class Element implements Serializable {

  private static final long serialVersionUID = -7299384943668362289L;

  private final List<String> values = new ArrayList<>();

  private final Column column;

  private final int lineNumber;

  public Element(final String value, final Column column, final int lineNumber) {
    if (column.getNumItems() != null) {
      // get the number of items this column is split into, defaults to 1
      final int numItems = column.getNumItems();

      // get the number of bytes in each item, if not specified, defaults
      // to the total bytes divided by the number of items\
      final int itemBytes = (int) (column.getItemBytes() != null ? column.getItemBytes()
          : Math.floor(column.getBytes() / numItems));

      final Integer itemOffset = column.getItemOffset();

      // current byte index
      int curIndex = 0;
      final String source = value.toString();
      for (int i = 0; i < numItems; i++) {
        int endIndex = curIndex + itemBytes;
        try {
          final String data = source.substring(curIndex, endIndex);
          this.values.add(data);
        } catch (StringIndexOutOfBoundsException e) {
          // missing values in row at end probably
        }

        // if an item offset was used, start index for next item is
        // start index for current plus offset. otherwise just add item
        // bytes
        if (itemOffset != null) {
          curIndex += itemOffset;
        } else {
          curIndex += itemBytes;
        }
      }
    } else {
      this.values.add(value);
    }
    this.column = column;
    this.lineNumber = lineNumber;
  }

  private Element(final Element element) {
    this.values.addAll(element.getValues());
    this.column = element.getColumn().clone();
    this.lineNumber = element.getLineNumber();
  }

  @Override
  public Element clone() {
    return new Element(this);
  }

  public String getValue() {
    return this.values.get(0);
  }

  public List<String> getValues() {
    return this.values;
  }

  public Column getColumn() {
    return this.column;
  }

  public int getLineNumber() {
    return this.lineNumber;
  }
}
