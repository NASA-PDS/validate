Feature: 3.6.x
  Scenario Outline: NASA-PDS/validate#<issueNumber> <subtest>
    Given an <issueNumber>, <subtest>, and <datasrc>
    When execute validate with <args>
    Then compare to the <expectation>.
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |

| 1028 | 1 | "github1028" | "--skip-context-validation -t {datasrc}/xa.s16..shz.1976.070.0.xml --schema {datasrc}/PDS4_PDS_1N00.xsd --schematron {datasrc}/PDS4_PDS_1N00.sch" | |
