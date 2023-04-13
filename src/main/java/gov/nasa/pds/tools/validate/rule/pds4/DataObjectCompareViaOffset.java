package gov.nasa.pds.tools.validate.rule.pds4;

import java.util.Comparator;
import gov.nasa.pds.label.object.DataObject;

class DataObjectCompareViaOffset implements Comparator<DataObject> {
  @Override
  public int compare(DataObject arg0, DataObject arg1) {
    return Long.compare(arg0.getOffset(), arg1.getOffset());
  }
}
