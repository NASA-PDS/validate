Feature: 4.2.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @4.2.x
    Examples:
      | issueNumber | subtest | datasrc | args | expectation |
#begin

# github1601: member_not_found should be ERROR by default; --skip-strict-collection-membership downgrades to WARNING
| 1601 | 1 | "github1601" | "-R pds4.bundle --skip-context-validation -t {datasrc}/bundle_test_1601.xml" | "summary:totalErrors=1,summary:referentialIntegrity:failed=1,summary:messageTypes:error.integrity.member_not_found=1" |
| 1601 | 2 | "github1601" | "-R pds4.bundle --skip-context-validation --skip-strict-collection-membership -t {datasrc}/bundle_test_1601.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.integrity.member_not_found=1" |

#end
