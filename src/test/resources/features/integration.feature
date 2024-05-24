Feature: Running integration tests for validate module

Scenario Outline: Execute validate command for tests below.
    Given a test <testName> at dir <testDir> at resource <resourceDir> sending report to <reportDir> with <commandArgs> as arguments
    When with test property count <messageCount> text <messageText> problem <problemEnum> reference <refOutputValue>
    When execute a validate command
    Then produced output from validate command should be similar to reference <refOutputValue> or no error reported.

  Examples:
 | testName                                             | testDir     | messageCount | messageText         | problemEnum     | resourceDir          | reportDir     | commandArgs                                                                | refOutputValue | 

### TO REMOVE
# Validate#693
|"NASA-PDS/validate#693 Invalid PDF/A Checks"           | "github693" | 4            | "4 errors expected" | "NON_PDFA_FILE" | "src/test/resources" | "target/test" | "-r {reportDir}/report_github693.json -s json -t {resourceDir}/github693/" | "report_github693.json" |
### TO REMOVE