# Contribute

## Adding New Test Cases

### Pre-requisites
* Eclipse installed (`brew cask install eclipse-java`)
* maven installed (`brew install maven`)
* Cloned validate repo

### Procedure
* Create new directory with test data src/test/resources/github123
* Add new line(s) for test cases to [src/test/resources/features/v3.7.x.feature](https://github.com/NASA-PDS/validate/blob/main/src/test/resources/features/3.7.x.feature)
  * testName - title for the test, e.g. `NASA-PDS/validate#693 Invalid PDF/A Checks`, `NASA-PDS/validate#693 Valid PDF/A File Checks`
  * issueNumber - whatever the issue number is from GitHub
  * subtest - start with 1 and increment for any subsequent test
  * datasrc - the directory name under `src/test/resources` where the test data will go. often this is something like `github123`
  * args - any/all arguments you want to use to run validate. at minimum, `--target` is required. do not include `--report-style` or `--report-file` as they are already included by default: 
  * expectation - a comma separated list of expected errors, warnings, etc.
    * For expected success run: put 1 space here if the test is expected to be successful. (e.g. it should look like this `|  |`
    * For expected 3 total warnings: `summary:totalWarnings=3`
    * For expected 3 total errors: `summary:totalErrors=3`
    * For expected 1 total errors, and check for a specific PDF error is thrown: `summary:totalErrors=1,summary:messageTypes:error.pdf.file.not_pdfa_compliant=1`

* Now run the test(s):
  * Via Eclipse: right-click `Run as` -> `Cucumber Feature`
  * Via Maven (replace with applicable tag for this build):
    * bash: `mvn test -Dtest=\!ReferenceIntegrityTest* -Dcucumber.filter.tags='@v3.7.x'`
    * tcsh: `mvn test -Dtest="\!ReferenceIntegrityTest*" -Dcucumber.filter.tags='@v3.7.x'`
 
