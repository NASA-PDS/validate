Feature: 3.7.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @3.7.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |

| 1118 | 1 | "github1118" | "--skip-context-validation -t {datasrc}/20170303t022534s621_sto_l0.b.xml" |  |
