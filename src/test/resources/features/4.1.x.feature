Feature: 4.1.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @4.1.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |
| 956 | 1 | "github956" | "--skip-context-validation -t {datasrc}/" | |
| 1458 | 1 | "github1458" | " -t {datasrc}/" | "summary:productValidation:passed=2,summary:productValidation:total=2,summary:totalWarnings=6,messageTypes:warning.label.context_ref_mismatch=5,messageTypes:warning.label.schematron=1" |
| 1481 | 1 | "github1481" | "--skip-context-validation --skip-product-validation -R pds4.bundle -t {datasrc}/bundle_test.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.integrity.reference_not_found=1" |
| 1548 | 1 | "github1548" | "--skip-context-validation -t {datasrc}/nonexistent.xml" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.label.missing_file=1" |
