Feature: Running integration tests for validate module

Scenario Outline: Execute validate command for tests below.
    Given a test <testName> at dir <testDir> at resource <resourceDir> sending report to <reportDir> with <commandArgs> as arguments
    When with test property count <messageCount> text <messageText> problem <problemEnum> reference <refOutputValue>
    When execute a validate command
    Then produced output from validate command should be similar to reference <refOutputValue> or no error reported.

  @v3.6.0
  Examples:
 | testName                                             | testDir     | messageCount | messageText         | problemEnum     | resourceDir          | reportDir     | commandArgs                                                                | refOutputValue | 

# In .csv, Line1 good; L2,Field1=11.111 for %6.2f; L3,F2=22.22 for $4.2f; L4,F3=33333333 (no 'e') for %8.3e; L5,F4=4.4 for %3d
#|"NASA-PDS/validate#816 Throw WARNING for unmatched field_format in table char" | "github816" | 5 | "1 errors expected, 4 warnings expected" | "FIELD_VALUE_FORMAT_PRECISION_MISMATCH,FIELD_VALUE_TOO_LONG,FIELD_VALUE_FORMAT_SPECIFIER_MISMATCH,FIELD_VALUE_DATA_TYPE_MISMATCH,FIELD_VALUE_FORMAT_SPECIFIER_MISMATCH" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github816a.json -s json -t {resourceDir}/github816/ff_char.xml" | "report_github816a.json" |
|"NASA-PDS/validate#816 Throw WARNING for unmatched field_format in delimited" | "github816" | 5 | "1 errors expected, 4 warnings expected" | "FIELD_VALUE_FORMAT_PRECISION_MISMATCH,FIELD_VALUE_TOO_LONG,FIELD_VALUE_FORMAT_SPECIFIER_MISMATCH,FIELD_VALUE_DATA_TYPE_MISMATCH" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github816b.json -s json -t {resourceDir}/github816/ff_del.xml" | "report_github816b.json" |

# validate3.4.1 reported 0 errors or warnings.
|"NASA-PDS/validate#822 check for unlabeled files" | "github822" | 1 | "1 errors expected" | "UNLABELED_FILE" | "src/test/resources" | "target/test" | "-R pds4.bundle -r {reportDir}/report_github822.json -s json -t {resourceDir}/github822" | "report_github822.json" |

# validate3.4.1 reported ~10 fatal errors
|"NASA-PDS/validate#823 clear file read errors" | "github823" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github823.json -s json -t {resourceDir}/github823/mvn_swi_l2_onboardsvymom_20230827_v02_r01.xml" | "report_github823.json" |

# validate3.4.1 complained about not PDF/A-1b even though Product_Document
|"NASA-PDS/validate#824 clear file read errors" | "github824" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github824.json -s json -t {resourceDir}/github824/1203_12.xml" | "report_github824.json" |

# validate3.4.1 WARNs that 3 values are below min even though they match other constants
|"NASA-PDS/validate#831 Check specific constants before min or max constants" | "github831" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github831.json -s json -t {resourceDir}/github831/kplo.xml" | "report_github831.json" |

# someday line 3 in data (or line 88 of .xml) may reasonably throw an error for bad format
|"NASA-PDS/validate#837 Check constants before data_type" | "github837" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github837.json -s json -t {resourceDir}/github837/x.xml" | "report_github837.json" |

# validate3.4.1 froze on this
|"NASA-PDS/validate#849 Stalls when collection.csv has duplicates" | "github849" | 2 | "2 errors expected" | "INVENTORY_DUPLICATE_LIDVID" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github849.json -s json -t {resourceDir}/github849/collection.xml" | "report_github849.json" |

# previous versions gave no errors or warnings, which is bad
|"NASA-PDS/validate#857 WARNING when Observing_System_Component.name mismatches context product" | "github857" | 2 | "2 warnings expected" | "CONTEXT_REFERENCE_FOUND_MISMATCH_WARN" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github857.json -s json -t {resourceDir}/github857/highi_183_istria_fam3.xml" | "report_github857.json" |

# validate3.4.1 gave no errors or warnings
|"NASA-PDS/validate#861 WARNING when Observing_System_Component.name mismatches context product" | "github861" | 1 | "1 warnings expected" | "CONTEXT_REFERENCE_FOUND_MISMATCH_WARN" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github861.json -s json -t {resourceDir}/github861/PVO_OMAG_OEFD_ANC_ENG_0001.xml" | "report_github861.json" |

# target is parent directory, not either subdir. validate3.4.1 threw ERROR
|"NASA-PDS/validate#873 Allow same filenames in different subdirs" | "github873" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github873.json -s json -t {resourceDir}/github873/." | "report_github873.json" |

# same as github873
|"NASA-PDS/validate#902 Allow same filenames in different subdirs" | "github902" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github902.json -s json -t {resourceDir}/github902/s_00168901_thm.xml" | "report_github902.json" |

# validate3.4.1 throws incorrect ERROR
|"NASA-PDS/validate#905 Allow multiple Product_Documents to point to same file" | "github905" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github905.json -s json -t {resourceDir}/github905/." | "report_github905.json" |

# validate3.4.1 context_ref_mismatch fires only if -R pds4.label is specified
|"NASA-PDS/validate#915 Allow multiple Product_Documents to point to same file" | "github915" | 3 | "3 warnings expected" | "CONTEXT_REFERENCE_FOUND_MISMATCH_WARN" | "src/test/resources"   | "target/test" | "-r {reportDir}/report_github915.json -s json -R pds4.collection -t {resourceDir}/github915" | "report_github915.json" |

# validate3.4.1: Error while getting field value: Bit field spans bytes that are wider than a long (9 > 8)
|"NASA-PDS/validate#919 make  --disable-context-mismatch-warnings work generally" | "github919" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github919.json -s json -t {resourceDir}/github919/uh0003b_draft.xml" | "report_github919.json" |

# No old version of validate of mine had this error. Eh, too bad
|"NASA-PDS/validate#950 make  --disable-context-mismatch-warnings work generally" | "github950" | 0 | "0 errors expected" | "totalErrors" | "src/test/resources" | "target/test" | "--disable-context-mismatch-warnings -R pds4.bundle -r {reportDir}/report_github950.json -s json -t {resourceDir}/github950" | "report_github950.json" |

