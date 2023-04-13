package gov.nasa.pds.validate.ri;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class LidvidComparator implements Comparator<String> {
  @Override
  public int compare(String a, String b) {
    List<Integer> aVid = vid(a), bVid = vid(b);
    return (aVid.get(0).equals(bVid.get(0))) ? Integer.compare(aVid.get(1), bVid.get(1))
        : Integer.compare(aVid.get(0), bVid.get(0));
  }

  private List<Integer> vid(String lidvid) {
    String VID = lidvid.substring(lidvid.indexOf("::") + 2);
    return Arrays.asList(Integer.valueOf(VID.substring(0, VID.indexOf('.'))),
        Integer.valueOf(VID.substring(VID.indexOf('.') + 1)));
  }
}
