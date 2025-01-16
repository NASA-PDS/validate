Feature: < 3.6
  Scenario Outline: NASA-PDS/validate#<issueNumber> <subtest>
    Given an <issueNumber>, <subtest>, and <datasrc>
    When execute validate with <args>
    Then compare to the <expectation>.
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |
    @pre.3.6

| 1028 | 2 | "github1028" | "--skip-context-validation -t {datasrc}/xa.s16..shz.1976.070.0.xml --schema {datasrc}/PDS4_PDS_1N00.xsd --schematron {datasrc}/PDS4_PDS_1N00.sch" | |
