Feature: 4.0.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>-<subtest>
    Given validate issue <issueNumber>, test <subtest>, and test data at <datasrc>
    When execute validate with <args>
    Then compare to the expected outcome <expectation>.
    @4.0.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |

| 1408 | 1 | "github1408" | " -t {datasrc}/" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.label.table_definition_problem=1" |
| 1391 | 1 | "github1391" | "--skip-context-validation -t {datasrc}/vl0axrat_char.xml" |  |
| 1391 | 2 | "github1391" | "--skip-context-validation -t {datasrc}/vl0axrat_delim.xml" |  |
| 1379 | 1 | "github1379" | " -v1 -t {datasrc}/V11979080.xml" | "summary:totalWarnings=3,summary:messageTypes:warning.table.field_value_out_of_special_constant_min_max_range=3,summary:messageTypes:info.label.context_ref_found=3,summary:messageTypes:info.label.missing_filesize=1,summary:messageTypes:info.table.field_value_is_special_constant=3" |
| 1359 | 1 | "github1359" | "--skip-context-validation --label-extension lblx -R pds4.label --strict-field-checks -t {datasrc}/collection_hyb2_nirs3_sp_ard_data_iof_thermalcorr_v001.lblx" |  |
| 1358 | 1 | "github1358" | "--skip-context-validation --label-extension lblx -R pds4.label --strict-field-checks -t {datasrc}/uvi_20151207_051953_283_l2b_v21_edited.lblx " |  |
| 1357 | 1 | "github1357" | "--skip-context-validation --label-extension lblx -R pds4.label -t {datasrc}/uvi_20151207_051953_283_l3b_v21.lblx" |  |
| 1332 | 1 | "github1332" | " -t {datasrc}/nh0001x.xml" |  |
| 1316 | 1 | "github1316" | "--skip-context-validation -R pds4.bundle -t {datasrc}/bundle.xml" |  |
| 1276 | 1 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.lon.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1276 | 2 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.no.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1276 | 3 | "github1276" | "--skip-context-validation --strict-field-checks {datasrc}/vco_v07.utc.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.table.characters_between_fields=1" |
| 1234 | 1 | "github1234" | " -t {datasrc}/collection6.xml" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.table.records_mismatch=1" |
| 1234 | 2 | "github1234" | " -t {datasrc}/collection8.xml" | "summary:totalErrors=2,summary:productValidation:failed=1,summary:messageTypes:error.table.records_mismatch=2" |
| 1201 | 1 | "github1201" | " -R pds4.folder -e lblx -t {datasrc}/" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.label.not_understandable=1" |
| 1137 | 1 | "github1137" | " -R pds4.collection -progress 4 -t {datasrc}/collection.xml" |  |
| 970 | 1 | "github970" | " -t {datasrc}/V11979060.xml" |  |
| 967 | 1 | "github967" | " -t {datasrc}/dlbi.xml" |  |

