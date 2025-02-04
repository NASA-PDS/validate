Feature: 3.6.x
  Scenario Outline: NASA-PDS/validate#<issueNumber>_<subtest>
    Given an <issueNumber>, <subtest>, and <datasrc>
    When execute validate with <args>
    Then compare to the <expectation>.
    @3.6.x
    Examples: 
      | issueNumber | subtest | datasrc | args | expectation |
#begin

| 1100 |  | "github1100" | "--skip-context-validation -R pds4.bundle -t {datasrc}" |  |
| 1066 |  | "github1066" | "--skip-content-validation -t {datasrc}/vg2u_49xr_1986024t232141.xml" | "summary:totalWarnings=3,summary:messageTypes:warning.label.context_ref_mismatch=3" |
| 1028 |  | "github1028" | "--skip-context-validation -t {datasrc}/xa.s16..shz.1976.070.0.xml --schema {datasrc}/PDS4_PDS_1N00.xsd --schematron {datasrc}/PDS4_PDS_1N00.sch" |  |
| 1008 |  | "github1008" | "--skip-context-validation -t {datasrc}/example.xml" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.pdf.file.not_pdfa_compliant=1" |
| 992 | 1 | "github992" | "--skip-context-validation -t {datasrc}/ff_char.xml" | "summary:totalErrors=1,summary:totalWarnings=5,summary:productValidation:failed=1,summary:messageTypes:error.table.field_value_data_type_mismatch=1,summary:messageTypes:warning.table.field_value_format_precision_mismatch=1,summary:messageTypes:warning.table.field_value_format_specifier_mismatch=3,summary:messageTypes:warning.table.field_value_too_long=1" |
| 992 | 2 | "github992" | "--skip-context-validation -t {datasrc}/ff_del.xml" | "summary:totalErrors=1,summary:totalWarnings=5,summary:productValidation:failed=1,summary:messageTypes:error.table.field_value_data_type_mismatch=1,summary:messageTypes:warning.table.field_value_format_precision_mismatch=1,summary:messageTypes:warning.table.field_value_format_specifier_mismatch=3,summary:messageTypes:warning.table.field_value_too_long=1" |
| 950 | 1 | "github915" | "--skip-content-validation --disable-context-mismatch-warnings -R pds4.collection -t {datasrc}/collection.xml" | "summary:totalErrors=1,summary:totalWarnings=1,summary:productValidation:failed=1,summary:messageTypes:error.label.duplicate_identifier=1,summary:messageTypes:warning.integrity.unreferenced_member=1" |
| 950 | 2 | "github950" | "--disable-context-mismatch-warnings -R pds4.bundle -t {datasrc}" |  |
| 919 | 1 | "github919" | "--skip-context-validation -t {datasrc}/uh0003b_draft.xml" |  |
| 919 | 2 | "github919" | "-t {datasrc}/uh0003b_draft.xml" |  |
| 915 |  | "github915" | "--skip-content-validation -R pds4.collection -t {datasrc}/collection.xml" | "summary:totalErrors=1,summary:totalWarnings=6,summary:productValidation:failed=1,summary:messageTypes:error.label.duplicate_identifier=1,summary:messageTypes:warning.integrity.unreferenced_member=1,summary:messageTypes:warning.label.context_ref_mismatch=5" |
| 905 | 1 | "github905" | "--skip-context-validation -t {datasrc}/dsn_0159-science.2008-02-29.xml {datasrc}/dsn_0159-science.2009-05-18.xml" |  |
| 905 | 2 | "github905" | "-t {datasrc}/." |  |
| 902 | 1 | "github902" | "--skip-context-validation -t {datasrc}/s_00168901_thm.xml" |  |
| 902 | 2 | "github902" | "-t {datasrc}/s_00168901_thm.xml" |  |
| 873 | 1 | "github873" | "--skip-context-validation -R pds4.bundle -t {datasrc}" | "summary:totalWarnings=2,summary:messageTypes:warning.integrity.unreferenced_member=2" |
| 873 | 2 | "github873" | "-t {datasrc}/." |  |
| 861 |  | "github861" | "-t {datasrc}/PVO_OMAG_OEFD_ANC_ENG_0001.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.label.context_ref_mismatch=1" |
| 857 |  | "github857" | "-t {datasrc}/highi_183_istria_fam3.xml" | "summary:totalWarnings=2,summary:messageTypes:warning.label.context_ref_mismatch=2" |
| 849 | 1 | "github849" | "--skip-context-validation -t {datasrc}/collection_uvs_data_raw.xml" | "summary:totalErrors=2,summary:productValidation:failed=1,summary:messageTypes:error.inventory.duplicate_lidvid=2" |
| 849 | 2 | "github849" | "-t {datasrc}/collection.xml" | "summary:totalErrors=1,summary:productValidation:failed=1,summary:messageTypes:error.inventory.duplicate_lidvid=1" |
| 837 | 1 | "github837" | "--skip-context-validation -t {datasrc}/times_table.xml" |  |
| 837 | 2 | "github837" | "-t {datasrc}/x.xml" | "summary:totalWarnings=1,summary:messageTypes:warning.label.context_ref_mismatch=1" |
| 831 |  | "github831" | "--skip-context-validation -t {datasrc}/kplo.xml" |  |
| 824 | 1 | "github824" | "--skip-context-validation -t {datasrc}/1203_12.xml" |  |
| 824 | 2 | "github824" | "-t {datasrc}/1203_12.xml" |  |
| 823 |  | "github823" | "--skip-context-validation -t {datasrc}/mvn_swi_l2_onboardsvymom_20230827_v02_r01.xml" |  |
| 822 |  | "github822" | "-R pds4.bundle -t {datasrc}" | "summary:totalWarnings=1,summary:messageTypes:warning.file.not_referenced_in_label=1" |
| 816 | 1 | "github681" | "-t {datasrc}/ff_char_fail.xml" | "summary:totalErrors=1,summary:totalWarnings=2,summary:productValidation:failed=1,summary:messageTypes:error.table.field_value_format_precision_mismatch=1,summary:messageTypes:warning.label.context_ref_mismatch=2" |
| 816 | 2 | "github681" | "-t {datasrc}/ff_char_warn.xml {datasrc}/ff_del_warn.xml" | "summary:totalErrors=1,summary:totalWarnings=6,summary:productValidation:failed=1,summary:messageTypes:error.label.file_areas_duplicated_reference=1,summary:messageTypes:warning.label.context_ref_mismatch=4,summary:messageTypes:warning.table.field_value_format_precision_mismatch=2" |
| 816 | 3 | "github816" | "-t {datasrc}/ff_del.xml" | "summary:totalErrors=1,summary:totalWarnings=4,summary:productValidation:failed=1,summary:messageTypes:error.table.field_value_data_type_mismatch=1,summary:messageTypes:warning.table.field_value_format_precision_mismatch=1,summary:messageTypes:warning.table.field_value_format_specifier_mismatch=2,summary:messageTypes:warning.table.field_value_too_long=1" |
