Feature: 3.7.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @3.7.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |

| 1135 | 1 | "github1135" | "--skip-context-validation -t {datasrc}/2017_FGM_KRTP_1M.xml" |  |
| 1118 | 1 | "github1118" | "--skip-context-validation -t {datasrc}/20170303t022534s621_sto_l0.b.xml" |  |
| 1100 | 1 | "github1100" | "-R pds4.bundle -t {datasrc}" |  |
| 1104 | 1 | "github1104" | "-t {datasrc}/xrs1.xml" |  |
| 1104 | 2 | "github1104" | "-t {datasrc}/xrs2.xml" |  |
| 1104 | 3 | "github1104" | "-t {datasrc}/xrs3.xml" |  |
| 1118 | 1 | "github1118" | "--skip-context-validation -t {datasrc}/20170303t022534s621_sto_l0.b.xml" |  |
| 1129 | 1 | "github1129" | "--disable-context-mismatch-warnings -t {datasrc}/3778ml1037770010808163i01_dxxx.xml" |  |
| 1158 | 1 | "github1158" | "--skip-content-validation -t {datasrc}/980226_046.xml" |  |
| 1090 | 1 | "github1135" | "--skip-context-validation --skip-product-validation -t {datasrc}/2017_FGM_KRTP_1M.xml" | "summary:productValidation:passed=0,summary:productValidation:skipped=1,summary:productValidation:total=1" |
