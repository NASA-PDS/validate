# Contribute

## Adding New Test Cases

### Pre-requisites
* Eclipse installed (`brew cask install eclipse-java`)
* maven installed (`brew install maven`)
* Cloned validate repo

### Procedure
* Create new directory with test data src/test/resources/github123
* Add new line(s) for test cases to src/test/resources/features/v3.6.0.feature
  * testName - title for the test, e.g. `NASA-PDS/validate#693 Invalid PDF/A Checks`, `NASA-PDS/validate#693 Valid PDF/A File Checks`
  * testDir - `github123`
  * messageCount - number of expected errors or warnings 
  * messageText - the expected output should match very closely to messageCount, e.g. `4 errors expected`, `8 warnings expected` (not sure how this is really used or why we have this right now...)
  * problemEnum - `totalErrors`, `totalWarnings`, or specific error you expect from validate output
    * for specific error, search `src/main/java/gov/nasa/pds/tools/validate/ProblemType.java` for the error identifier, and put the all caps value in the file, e.g. for `error.pdf.file.not_pdfa_compliant`, if I search the file I see this line `NON_PDFA_FILE("error.pdf.file.not_pdfa_compliant")`, so the problemEnum value == `NON_PDFA_FILE`
  * resourceDir - always `src/test/resources`
  * reportDir - always `target/test`
  * commandArgs - at minimum, the following must be specified: 
    * `--report-style json`
    * `--report-file {reportDir}/report_github123.json`
    * `--target {resourceDir}/github123/myfile.xml`  (could also just point to a directory or a regex or --target-manifest instead)
  * refOutputValue - same filename as the `--report-file` specified above
* Now run the test(s):
  * Via Eclipse: right-click `Run as` -> `Cucumber Feature`
  * Via Maven (replace with applicable tag for this build):
    * bash: `mvn test -Dtest=\!ReferenceIntegrityTest* -Dcucumber.filter.tags='@v3.6.x'`
    * tcsh: `mvn test -Dtest="\!ReferenceIntegrityTest*" -Dcucumber.filter.tags='@v3.6.x'`
 
