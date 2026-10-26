# Feature files are named after the version currently under development in
# pom.xml's <version> (the "-SNAPSHOT" version), not the last released
# version. For example, this file was created while pom.xml had
# <version>5.1.0-SNAPSHOT</version>, immediately after v5.0.0 shipped. New
# test scenarios go in the feature file matching the CURRENT pom.xml
# version; do not add them to a file named after a version that has already
# been released.
Feature: 5.1.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @5.1.x
    Examples:
      | issueNumber | subtest | datasrc | args | expectation |
#begin

# github1696: directory_path_name with a trailing slash caused File/Document_File references
# to be registered with a doubled path separator, so the crawled on-disk file never matched
# the registered reference and was spuriously flagged as unreferenced.
| 1696 | 1 | "github1696" | "-R pds4.collection --skip-context-validation -t {datasrc}/collection_1696.xml" |  |

# github1698: a Document_File declared as "PDF" (not "PDF/A") must not be checked for PDF/A conformance
| 1698 | 1 | "github1698" | "--skip-context-validation -t {datasrc}/artemis2_lunar_targeting_package.xml" | "summary:productValidation:passed=1,summary:totalErrors=0,summary:totalWarnings=0" |

#end
