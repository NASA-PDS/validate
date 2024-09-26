Feature: Running integration tests for validate module

Scenario Outline: Execute validate command for tests below.
    Given a test <testName> at dir <testDir> at resource <resourceDir> sending report to <reportDir> with <commandArgs> as arguments
    When with test property count <messageCount> text <messageText> problem <problemEnum> reference <refOutputValue>
    When execute a validate command
    Then produced output from validate command should be similar to reference <refOutputValue> or no error reported.

  @v3.6.x
  Examples:
 | testName                                             | testDir     | messageCount | messageText         | problemEnum     | resourceDir          | reportDir       | commandArgs                                                                | refOutputValue | 

# Validate#816 (reuse validate#681 tests)
|"NASA-PDS/validate#816 Field format FAIL: ASCII table invalid precision" | "github681" | 1 | "1 errors expected" | "FIELD_VALID_FORMAT_PRECISION_MISMATCH" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github681_2.json -s json -t {resourceDir}/github681/ff_char_fail.xml" | "report_github681_2.json" |
|"NASA-PDS/validate#816 Field format WARNING: Precision mismatch" | "github681" | 2 | "2 warnings expected" | "FIELD_VALUE_FORMAT_PRECISION_MISMATCH" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github681_3.json -s json  -t {resourceDir}/github681/ff_char_warn.xml {resourceDir}/github681/ff_del_warn.xml" | "report_github681_3.json" |

# Validate#822
#|"NASA-PDS/validate#822 Re-test to close this ticket" | "github6" | 7 | "7 warnings expected for warning.file.not_referenced_in_label." | "UNLABELED_FILE" | "src/test/resources" | "target/test" | "-R pds4.bundle -r {reportDir}/report_github6_2.json -s json {resourceDir}/github6/invalid/bundle_kaguya_derived_7.xml" | "report_github6_2.json" |

# Validate#823
|"NASA-PDS/validate#823 Success process problematic floats" | "github823" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github823.json -s json --skip-context-validation  -t {resourceDir}/github823/mvn_swi_l2_onboardsvymom_20230827_v02_r01.xml" | "report_github823.json" |

# Validate#824
|"NASA-PDS/validate#824 Success PDF vs PDF/A" | "github824" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github824.json -s json --skip-context-validation  -t {resourceDir}/github824/1203_12.xml" | "report_github824.json" |

# Validate#831
|"NASA-PDS/validate#831 Success high instrument saturation" | "github831" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github831.json -s json --skip-context-validation  -t {resourceDir}/github831/kplo.xml" | "report_github831.json" |

# Validate#837
|"NASA-PDS/validate#837 Success match out of format constants" | "github837" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github837.json -s json --skip-context-validation  -t {resourceDir}/github837/times_table.xml" | "report_github837.json" |

# Validate#849
|"NASA-PDS/validate#849 Fails inventory with duplicate entries" | "github849" | 2 | "2 errors expected" | "INVENTORY_DUPLICATE_LIDVID" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github849.json -s json --skip-context-validation  -t {resourceDir}/github849/collection_uvs_data_raw.xml" | "report_github849.json" |

# Validate#873
|"NASA-PDS/validate#873 Success files same name different paths" | "github873" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github873.json -s json --skip-context-validation -R pds4.bundle -t {resourceDir}/github873" | "report_github873.json" |

# Validate#902
|"NASA-PDS/validate#902 Success with windows file URL" | "github902" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github902.json -s json --skip-context-validation -t {resourceDir}/github902/s_00168901_thm.xml" | "report_github902.json" |

# Validate#905
|"NASA-PDS/validate#905 Success no duplicates in non-observational;" | "github905" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github905.json -s json --skip-context-validation -t {resourceDir}/github905/dsn_0159-science.2008-02-29.xml {resourceDir}/github905/dsn_0159-science.2009-05-18.xml" | "report_github905.json" |

# Validate#915
|"NASA-PDS/validate#915 Failure context ref mismatch" | "github915" | 4 | "4 warnings expected" | "CONTEXT_REFERENCE_FOUND_MISMATCH_WARN" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github915.json -s json --skip-content-validation -R pds4.collection -t {resourceDir}/github915/collection.xml" | "report_github915.json" |

# Validate#919
|"NASA-PDS/validate#919 Success with 61 bit value" | "github919" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github919.json -s json --skip-context-validation -t {resourceDir}/github919/uh0003b_draft.xml" | "report_github919.json" |

# Validate#950
|"NASA-PDS/validate#950 Disable context ref mismatch collection" | "github915" | 0 | "0 warnings expected" | "CONTEXT_REFERENCE_FOUND_MISMATCH_WARN" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github950.json -s json --skip-content-validation --disable-context-mismatch-warnings -R pds4.collection -t {resourceDir}/githfub915/collection.xml" | "report_github950.json" |