Feature: 3.7.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @3.7.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |

| 1359 | 1 | "github1359" | "--skip-context-validation --label-extension lblx -R pds4.label --strict-field-checks -t {datasrc}/collection_hyb2_nirs3_sp_ard_data_iof_thermalcorr_v001.lblx" |  |
| 1358 | 1 | "github1358" | "--skip-context-validation --label-extension lblx -R pds4.label --strict-field-checks -t {datasrc}/uvi_20151207_051953_283_l2b_v21_edited.lblx " |  |
| 1357 | 1 | "github1357" | "--skip-context-validation --label-extension lblx -R pds4.label -t {datasrc}/uvi_20151207_051953_283_l3b_v21.lblx" |  |
| 1332 | 1 | "github1332" | "--skip-context-validation -t {datasrc}/nh0001x.xml" |  |
| 1316 | 1 | "github1316" | "--skip-context-validation -R pds4.bundle -t {datasrc}/bundle.xml" |  |
| 1294 | 1 | "github1294" | "--skip-context-validation -t {datasrc}/t014_b0606_sp_20110528_17550949_y3.xml" |  |
| 1276 | 1 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.lon.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1276 | 2 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.no.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1276 | 3 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.utc.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1201 | 1 | "github1201" | "--skip-context-validation -R pds4.folder -t {datasrc}" | "summary:totalWarnings=1,summary:productValidation:skipped=1,summary:messageTypes:warning.label.not_understandable=1" |
| 1201 | 2 | "github1201" | "--skip-context-validation -R pds4.folder --label-extension lblx -t {datasrc}" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.label.not_understandable=1" |
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
