package gov.nasa.pds.tools.util;

import java.util.List;

/**
 * Cache of identifier data extracted from a parsed label, used to avoid re-parsing during the
 * referential integrity phase. All lists are stored as unmodifiable defensive copies.
 */
public class LabelCacheEntry {
  private final List<String> logicalIdentifiers;
  private final List<String> lidOrLidVidReferences;
  private final List<String> contextAreaRefs;
  /**
   * Carriage-return violations in lid_reference / lidvid_reference that were suppressed during
   * caching (because single-label validation must not report them). Each entry is a two-element
   * array: {@code [tagName, trimmedValue]}. Replayed by the bundle/collection referential integrity
   * path via {@link LabelUtil#replayCarriageReturnErrors}.
   */
  private final List<String[]> suppressedLidVidErrors;

  public LabelCacheEntry(List<String> logicalIdentifiers, List<String> lidOrLidVidReferences,
      List<String> contextAreaRefs, List<String[]> suppressedLidVidErrors) {
    this.logicalIdentifiers = List.copyOf(logicalIdentifiers);
    this.lidOrLidVidReferences = List.copyOf(lidOrLidVidReferences);
    this.contextAreaRefs = List.copyOf(contextAreaRefs);
    this.suppressedLidVidErrors = List.copyOf(suppressedLidVidErrors);
  }

  public List<String> getLogicalIdentifiers() {
    return logicalIdentifiers;
  }

  public List<String> getLidOrLidVidReferences() {
    return lidOrLidVidReferences;
  }

  public List<String> getContextAreaRefs() {
    return contextAreaRefs;
  }

  public List<String[]> getSuppressedLidVidErrors() {
    return suppressedLidVidErrors;
  }
}
