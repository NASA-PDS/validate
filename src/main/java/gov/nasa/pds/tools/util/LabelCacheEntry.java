package gov.nasa.pds.tools.util;

import java.util.ArrayList;

/**
 * Immutable cache of identifier data extracted from a parsed label, used to avoid re-parsing
 * during the referential integrity phase.
 */
public class LabelCacheEntry {
  private final ArrayList<String> logicalIdentifiers;
  private final ArrayList<String> lidOrLidVidReferences;
  private final ArrayList<String> contextAreaRefs;

  public LabelCacheEntry(ArrayList<String> logicalIdentifiers,
      ArrayList<String> lidOrLidVidReferences, ArrayList<String> contextAreaRefs) {
    this.logicalIdentifiers = logicalIdentifiers;
    this.lidOrLidVidReferences = lidOrLidVidReferences;
    this.contextAreaRefs = contextAreaRefs;
  }

  public ArrayList<String> getLogicalIdentifiers() {
    return logicalIdentifiers;
  }

  public ArrayList<String> getLidOrLidVidReferences() {
    return lidOrLidVidReferences;
  }

  public ArrayList<String> getContextAreaRefs() {
    return contextAreaRefs;
  }
}
