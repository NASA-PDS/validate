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

#end
