package gov.nasa.pds.tools.validate;

/**
 * Defines the standards documents against which we are testing.
 */
public final class Standard {

  /** The standard for PDS4. */
  public static final String PDS4_STANDARDS_REFERENCE = "PDS4 Standards Reference";

  /** The standard for PDS3. */
  public static final String PDS3_STANDARDS_REFERENCE = "PDS3 Standards Reference";

  /** The standard for UTF-8. */
  public static final String RFC_3629 = "RFC 3629";

  /**
   * Creates an instance. This can never be instantiated.
   */
  private Standard() {
    // Never instantiated.
  }

}
