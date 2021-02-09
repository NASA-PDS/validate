Feature: Running integration tests for validate module

Scenario Outline: Execute validate command for tests below.
    Given a test <testName> at dir <testDir> at resource <resourceDir> sending report to <reportDir> with <commandArgs> as arguments
    When with test property count <messageCount> text <messageText> problem <problemEnum> reference <refOutputValue>
    When execute a validate command
    Then produced output from validate command should be similiar to reference <refOutputValue> or no error reported.

  Examples:
 | testName     | testDir | messageCount | messageText | problemEnum | resourceDir | reportDir | commandArgs | refOutputValue | 

# The below 2 are for practice only.

# https://github.com/NASA-PDS/validate/issues/15 Verify that all name/type attribute values correspond to names denoted context products

# |"github15" | "github15" | 4 | "4 valid context references should be found" | "CONTEXT_REFERENCE_FOUND,CONTEXT_REFERENCE_NOT_FOUND" | "src/test/resources" | "target/test" | "-v1 -r {reportDir}/report_github15_pass.json -s json -R pds4.label -t {resourceDir}/github15/test_check-pass_context_products.xml" | "report_github15_pass.json" |
# |"github15_2" | "github15" | 3 | "3 errors expected for invalid context reference (Lid, name, value) test." | "CONTEXT_REFERENCE_NOT_FOUND,CONTEXT_REFERENCE_FOUND_MISMATCH" | "src/test/resources" | "target/test" | "-v1 -r {reportDir}/report_github15_no-pass.json -s json {resourceDir}/github15/test_check-no-pass_context_products.xml" | "report_github15_no-pass.json" |

# The below tests are for real.

# https://github.com/NASA-PDS/validate/issues/281 Validate fails to report error in File.file_size

 |"NASA-PDS/validate#281 1" | "github281" | 1 | "1 errors expected for FILESIZE_MISMATCH." | "FILESIZE_MISMATCH" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github281_label_invalid_1_cucumber.json -s json {resourceDir}/github281/invalid/collection_gwe_spk_invalid_1_bad_filesize.xml" | "report_github281_label_invalid_1_cucumber.json" |
 |"NASA-PDS/validate#281 2" | "github281" | 1 | "1 errors expected for CHECKSUM_MISMATCH." | "CHECKSUM_MISMATCH" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github281_label_invalid_2_cucumber.json -s json {resourceDir}/github281/invalid/collection_gwe_spk_invalid_2_bad_checksum.xml" | "report_github281_label_invalid_2_cucumber.json" |
 |"NASA-PDS/validate#281 3" | "github281" | 1 | "1 errors expected for CHECKSUM_MISMATCH." | "CHECKSUM_MISMATCH" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github281_label_invalid_3_cucumber.json -s json {resourceDir}/github281/invalid/collection_gwe_spk_invalid_3_bad_checksum_no_filesize.xml" | "report_github281_label_invalid_3_cucumber.json" |
 |"NASA-PDS/validate#281 4" | "github281" | 0 | "0 errors expected for FILESIZE_MISMATCH." | "FILESIZE_MISMATCH" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github281_label_valid_1_cucumber.json -s json {resourceDir}/github281/valid/collection_gwe_spk_valid_1.xml" | "report_github281_label_valid_1_cucumber.json" |
 |"NASA-PDS/validate#281 5" | "github281" | 0 | "0 errors expected for FILESIZE_MISMATCH." | "FILESIZE_MISMATCH" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github281_label_valid_2_cucumber.json -s json {resourceDir}/github281/valid/collection_gwe_spk_valid_2_no_filesize.xml" | "report_github281_label_valid_2_cucumber.json" |
 |"NASA-PDS/validate#281 6" | "github281" | 0 | "0 errors expected for FILESIZE_MISMATCH." | "FILESIZE_MISMATCH" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github281_label_valid_3_cucumber.json -s json {resourceDir}/github281/valid/collection_gwe_spk_valid_3_no_filesize_no_checksum.xml" | "report_github281_label_valid_3_cucumber.json" |

# https://github.com/NASA-PDS/validate/issues/278 Registered context products file does not retain older versions of context products

 |"NASA-PDS/validate#278 1" | "github278" | 1 | "1 errors expected for CONTEXT_REFERENCE_NOT_FOUND." | "CONTEXT_REFERENCE_NOT_FOUND" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github278_label_invalid_cucumber.json -s json {resourceDir}/github278/invalid/trk-2-34-revn-l5_tnf_invalid.xml" | "report_github278_label_invalid_cucumber.json" |
 |"NASA-PDS/validate#278 2" | "github278" | 0 | "0 errors expected for CONTEXT_REFERENCE_NOT_FOUND." | "CONTEXT_REFERENCE_NOT_FOUND" | "src/test/resources" | "target/test" | "-R pds4.label -r {reportDir}/report_github278_label_valid_cucumber.json -s json {resourceDir}/github278/valid/trk-2-34-revn-l5_tnf.xml" | "report_github278_label_valid_cucumber.json" |

 |"NASA-PDS/validate#6 1" | "github6" | 9 | "9 errors expected for FILE_NAME_HAS_INVALID_CHARS,UNALLOWED_BASE_NAME,UNALLOWED_FILE_NAME,DIR_NAME_HAS_INVALID_CHARS." | "FILE_NAME_HAS_INVALID_CHARS,UNALLOWED_BASE_NAME,UNALLOWED_FILE_NAME,DIR_NAME_HAS_INVALID_CHARS" | "src/test/resources" | "target/test" | "-R pds4.bundle -r {reportDir}/report_github6_bundle_invalid_cucumber.json -s json {resourceDir}/github6/invalid/bundle_kaguya_derived.xml" | "report_github6_bundle_invalid_cucumber.json" |
 |"NASA-PDS/validate#6 2" | "github6" | 0 | "0 errors expected for totalErrors." | "totalErrors" | "src/test/resources" | "target/test" | "-R pds4.bundle -r {reportDir}/report_github6_bundle_valid_cucumber.json -s json {resourceDir}/github6/valid/bundle_kaguya_derived.xml" | "report_github6_bundle_valid_cucumber.json" |

# https://github.com/NASA-PDS/validate/issues/240 Unexpected error for data collection in a sub-directory

 |"NASA-PDS/validate#240 1" | "github240" | 0 | "0 errors expected for totalErrors." | "totalErrors" | "src/test/resources" | "target/test" | "-R pds4.bundle -r {reportDir}/report_github240_bundle_valid_cucumber.json -s json {resourceDir}/github240/valid/bundle_kaguya_derived.xml" | "report_github240_bundle_valid_cucumber.json" |
 |"NASA-PDS/validate#240 2" | "github240" | 3 | "3 errors expected for UNALLOWED_BUNDLE_SUBDIR_NAME." | "UNALLOWED_BUNDLE_SUBDIR_NAME" | "src/test/resources" | "target/test" | "-R pds4.bundle -r {reportDir}/report_github240_bundle_invalid_cucumber.json -s json {resourceDir}/github240/invalid/bundle_kaguya_derived.xml" | "report_github240_bundle_invalid_cucumber.json" |

