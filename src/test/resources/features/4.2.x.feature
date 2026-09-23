Feature: 4.2.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @4.2.x
    Examples:
      | issueNumber | subtest | datasrc | args | expectation |
#begin

| 1265 | 1 | "github1265" | " -t {datasrc}/" | "summary:productValidation:passed=2,summary:productValidation:total=2,summary:totalWarnings=7,messageTypes:warning.integrity.pds4_version_mismatch=1,messageTypes:warning.label.context_ref_mismatch=5,messageTypes:warning.label.schematron=1" |

# github1601: member_not_found should be ERROR by default; --skip-strict-collection-membership downgrades to WARNING
| 1601 | 1 | "github1601" | "-R pds4.bundle --skip-context-validation -t {datasrc}/bundle_test_1601.xml" | "summary:totalErrors=1,summary:referentialIntegrity:failed=1,summary:messageTypes:error.integrity.member_not_found=1" |
| 1601 | 2 | "github1601" | "-R pds4.bundle --skip-context-validation --skip-strict-collection-membership -t {datasrc}/bundle_test_1601.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.integrity.member_not_found=1" |

# github1635: M4A/AAC should be a recognized encoding type; content validation not yet supported → WARNING not ERROR
| 1635 | 1 | "github1635" | "--skip-context-validation -t {datasrc}/audio_m4a.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.validation.content_validation_not_yet_supported=1" |

# github1660: missing_constant must suppress min/max range errors in ASCII table fields
| 1660 | 1 | "github1660" | "--skip-context-validation -t {datasrc}/pccds.xml" | "summary:productValidation:passed=1,summary:totalErrors=0,summary:totalWarnings=2,summary:messageTypes:warning.label.bad_schematypens=1,summary:messageTypes:warning.label.missing_schematron_spec=1" |

#end
