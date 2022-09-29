Feature: Running integration tests for validate module

Scenario Outline: Execute validate command for tests below.
    Given a test <testName> at dir <testDir> at resource <resourceDir> sending report to <reportDir> with <commandArgs> as arguments
    When with test property count <messageCount> text <messageText> problem <problemEnum> reference <refOutputValue>
    When execute a validate command
    Then produced output from validate command should be similiar to reference <refOutputValue> or no error reported.

  Examples:
 | testName     | testDir | messageCount | messageText | problemEnum | resourceDir | reportDir | commandArgs | refOutputValue | 

|"NASA-PDS/validate#230 1" | "github230" | 2 | "2 errors expected for MISSING_VERSION." | "MISSING_VERSION" | "src/test/resources" | "target/test" | "--skip-content-validation -R pds4.bundle -r {reportDir}/report_github230_invalid.json -s json -t {resourceDir}/github230/invalid/cocirs_c2h4abund/bundle_cocirs_c2h4abund.xml" | "report_github230_invalid.json" |
|"NASA-PDS/validate#230 2" | "github230" | 0 | "0 errors expected for MISSING_VERSION." | "MISSING_VERSION" | "src/test/resources" | "target/test" | "--skip-content-validation -R pds4.bundle -r {reportDir}/report_github230_valid.json -s json -t {resourceDir}/github230/valid/cocirs_c2h4abund/bundle_cocirs_c2h4abund.xml" | "report_github230_valid.json" |
