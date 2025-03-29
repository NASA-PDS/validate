Feature: 3.7.x

  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.

    @3.7.x
    Examples: 
      | issueNumber | subtest | datasrc      | args                                                                      | expectation |
      |        1100 |       1 | "github1100" | "-R pds4.bundle -t {datasrc}"                                             |             |
      |        1104 |       1 | "github1104" | "-t {datasrc}/xrs1.xml"                                                   |             |
      |        1104 |       2 | "github1104" | "-t {datasrc}/xrs2.xml"                                                   |             |
      |        1104 |       3 | "github1104" | "-t {datasrc}/xrs3.xml"                                                   |             |
      |        1118 |       1 | "github1118" | "--skip-context-validation -t {datasrc}/20170303t022534s621_sto_l0.b.xml" |             |
      |        1028 |       1 | "github1028" | "-t {datasrc}"                                                            |             |
      |        1129 |       1 | "github1129" | "-t {datasrc}/3778ml1037770010808163i01_dxxx.xml"                         |             |
      |        1158 |       1 | "github1158" | "--skip-content-validation -t {datasrc}/980226_046.xml"                   |             |
      |        1066 |       1 | "github1066" | "-t {datasrc}"                                                            |             |
      |        1135 |       1 | "github1135" | "--skip-context-validation -t {datasrc}/2017_FGM_KRTP_1M.xml"             |             |
      |        1118 |       1 | "github1118" | "--skip-context-validation -t {datasrc}/20170303t022534s621_sto_l0.b.xml" |             |
