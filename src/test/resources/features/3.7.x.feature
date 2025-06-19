Feature: 3.7.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @3.7.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |

| 1276 | 1 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.lon.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1276 | 2 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.no.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1276 | 3 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.utc.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1135 | 1 | "github1135" | "-t {datasrc}/2017_FGM_KRTP_1M.xml" |  |
| 1130 | 1 | "github1130" | "--skip-context-validation -t {datasrc}/hyb2_ldr_l0_aocsm_range_ts_20151219_v01.xml" |  |
| 1118 | 1 | "github1118" | "--skip-context-validation -t {datasrc}/20170303t022534s621_sto_l0.b.xml" |  |
| 1100 | 1 | "github1100" | "-R pds4.bundle -t {datasrc}" |  |
| 1104 | 1 | "github1104" | "-t {datasrc}/xrs1.xml" |  |
| 1104 | 2 | "github1104" | "-t {datasrc}/xrs2.xml" |  |
| 1104 | 3 | "github1104" | "-t {datasrc}/xrs3.xml" |  |
| 1129 | 1 | "github1129" | "--disable-context-mismatch-warnings -t {datasrc}/3778ml1037770010808163i01_dxxx.xml" |  |
| 1158 | 1 | "github1158" | "--skip-content-validation -t {datasrc}/980226_046.xml" |  |
| 1090 | 1 | "github1135" | "--skip-context-validation --skip-product-validation -t {datasrc}/2017_FGM_KRTP_1M.xml" | "summary:productValidation:passed=0,summary:productValidation:skipped=1,summary:productValidation:total=1" |
