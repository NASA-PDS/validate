[INFO] Scanning for projects...
[WARNING] 
[WARNING] Some problems were encountered while building the effective model for gov.nasa.pds:validate:jar:1.25.0-SNAPSHOT
[WARNING] 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-compiler-plugin is missing. @ gov.nasa:pds:1.4.0, /home/qchau/.m2/repository/gov/nasa/pds/1.4.0/pds-1.4.0.pom, line 143, column 15
[WARNING] 'build.plugins.plugin.version' for org.apache.maven.plugins:maven-jar-plugin is missing. @ line 92, column 15
[WARNING] 
[WARNING] It is highly recommended to fix these problems because they threaten the stability of your build.
[WARNING] 
[WARNING] For this reason, future Maven versions might no longer support building such malformed projects.
[WARNING] 
[INFO] Inspecting build with total of 1 modules...
[INFO] Installing Nexus Staging features:
[INFO]   ... total of 1 executions of maven-deploy-plugin replaced with nexus-staging-maven-plugin
[INFO] 
[INFO] -----------------------< gov.nasa.pds:validate >------------------------
[INFO] Building gov.nasa.pds:validate 1.25.0-SNAPSHOT
[INFO] --------------------------------[ jar ]---------------------------------
[INFO] 
[INFO] --- buildnumber-maven-plugin:1.4:create (default) @ validate ---
[INFO] Storing buildNumber: 2020-12-17 09:38:56 at timestamp: 1608226736387
[INFO] Storing buildScmBranch: #11
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ validate ---
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 60 resources
[INFO] Copying 1 resource to gov/nasa/pds/validate/util
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ validate ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 201 source files to /home/qchau/sandbox/validate/target/classes
[WARNING] /home/qchau/sandbox/validate/src/main/java/gov/nasa/pds/tools/util/LabelParser.java: Some input files use or override a deprecated API.
[WARNING] /home/qchau/sandbox/validate/src/main/java/gov/nasa/pds/tools/util/LabelParser.java: Recompile with -Xlint:deprecation for details.
[WARNING] /home/qchau/sandbox/validate/src/main/java/gov/nasa/pds/tools/validate/rule/RuleContext.java: Some input files use unchecked or unsafe operations.
[WARNING] /home/qchau/sandbox/validate/src/main/java/gov/nasa/pds/tools/validate/rule/RuleContext.java: Recompile with -Xlint:unchecked for details.
[INFO] 
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ validate ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 362 resources
[INFO] 
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ validate ---
[INFO] Nothing to compile - all classes are up to date
[INFO] 
[INFO] --- maven-surefire-plugin:3.0.0-M3:test (default-test) @ validate ---
[INFO] 
[INFO] -------------------------------------------------------
[INFO]  T E S T S
[INFO] -------------------------------------------------------
[INFO] Running gov.nasa.pds.validate.ValidationIntegrationTests
[ERROR] Tests run: 19, Failures: 4, Errors: 0, Skipped: 0, Time elapsed: 30.36 s <<< FAILURE! - in gov.nasa.pds.validate.ValidationIntegrationTests
[ERROR] testGithub153  Time elapsed: 1.275 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: 
No error messages expected.
{"title":"PDS Validation Tool Report","configuration":{"version":"1.25.0-SNAPSHOT","date":"2020-12-17T17:39:15Z"},"parameters":{"targets":"[file:/home/qchau/sandbox/validate/src/test/resources/github153/iue_asteroid_spectra/document/collection_iue_asteroid_spectra_document.xml]","ruleType":"pds4.label","severityLevel":"WARNING","recurseDirectories":"true","fileFiltersUsed":"[*.xml, *.XML]","dataContentValidation":"on","productLevelValidation":"on","maxErrors":"100000","registeredContextsFile":"/home/qchau/sandbox/validate/src/main/resources/util/registered_context_products.json"},"productLevelValidationResults":[{"status":"FAIL","label":"file:/home/qchau/sandbox/validate/src/test/resources/github153/iue_asteroid_spectra/document/collection_iue_asteroid_spectra_document.xml","messages":[{"severity":"ERROR","type":"error.validation.invalid_label","message":"Cannot extract Product_Observational/File_Area_Observational/Table_Character/Record_Character from label"}],"fragments":[],"dataContents":[]}],"summary":{"totalErrors":1,"totalWarnings":0,"messageTypes":[{"messageType":"error.validation.invalid_label","total":1}]}} ==> expected: <0> but was: <1>
	at gov.nasa.pds.validate.ValidationIntegrationTests.testGithub153(ValidationIntegrationTests.java:1101)

[ERROR] testGithub209  Time elapsed: 1.726 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: 0 error message(s) expected. See validation report: /home/qchau/sandbox/validate/target/test/report_1608226758427VALID_odf07155_msgr_11.xml.json ==> expected: <0> but was: <1>
	at gov.nasa.pds.validate.ValidationIntegrationTests.validateRunner(ValidationIntegrationTests.java:1225)
	at gov.nasa.pds.validate.ValidationIntegrationTests.testGithub209(ValidationIntegrationTests.java:1190)

[ERROR] testPDS543  Time elapsed: 0.877 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: /home/qchau/sandbox/validate/src/test/resources/PDS-543/report.expected.json and /home/qchau/sandbox/validate/target/test/report_PDS543.json do not match. ==> expected: <{"totalErrors":14,"totalWarnings":0,"messageTypes":[{"messageType":"error.table.record_length_mismatch","total":13},{"messageType":"error.validation.invalid_label","total":1}]}> but was: <{"totalErrors":13,"totalWarnings":0,"messageTypes":[{"messageType":"error.table.record_length_mismatch","total":13}]}>
	at gov.nasa.pds.validate.ValidationIntegrationTests.testPDS543(ValidationIntegrationTests.java:110)

[ERROR] testGithub17  Time elapsed: 0.686 s  <<< FAILURE!
org.opentest4j.AssertionFailedError: 
No error messages expected.
{"title":"PDS Validation Tool Report","configuration":{"version":"1.25.0-SNAPSHOT","date":"2020-12-17T17:39:29Z"},"parameters":{"targets":"[file:/home/qchau/sandbox/validate/src/test/resources/github17/delimited_table_good.xml]","ruleType":"pds4.label","severityLevel":"WARNING","recurseDirectories":"true","fileFiltersUsed":"[*.xml, *.XML]","dataContentValidation":"on","productLevelValidation":"on","maxErrors":"100000","registeredContextsFile":"/home/qchau/sandbox/validate/src/main/resources/util/registered_context_products.json"},"productLevelValidationResults":[{"status":"FAIL","label":"file:/home/qchau/sandbox/validate/src/test/resources/github17/delimited_table_good.xml","messages":[{"severity":"ERROR","type":"error.validation.invalid_label","message":"Cannot extract Product_Observational/File_Area_Observational/Table_Character/Record_Character from label"}],"fragments":[],"dataContents":[]}],"summary":{"totalErrors":1,"totalWarnings":0,"messageTypes":[{"messageType":"error.validation.invalid_label","total":1}]}} ==> expected: <0> but was: <1>
	at gov.nasa.pds.validate.ValidationIntegrationTests.testGithub17(ValidationIntegrationTests.java:1053)

[INFO] 
[INFO] Results:
[INFO] 
[ERROR] Failures: 
[ERROR]   ValidationIntegrationTests.testGithub153:1101 No error messages expected.
{"title":"PDS Validation Tool Report","configuration":{"version":"1.25.0-SNAPSHOT","date":"2020-12-17T17:39:15Z"},"parameters":{"targets":"[file:/home/qchau/sandbox/validate/src/test/resources/github153/iue_asteroid_spectra/document/collection_iue_asteroid_spectra_document.xml]","ruleType":"pds4.label","severityLevel":"WARNING","recurseDirectories":"true","fileFiltersUsed":"[*.xml, *.XML]","dataContentValidation":"on","productLevelValidation":"on","maxErrors":"100000","registeredContextsFile":"/home/qchau/sandbox/validate/src/main/resources/util/registered_context_products.json"},"productLevelValidationResults":[{"status":"FAIL","label":"file:/home/qchau/sandbox/validate/src/test/resources/github153/iue_asteroid_spectra/document/collection_iue_asteroid_spectra_document.xml","messages":[{"severity":"ERROR","type":"error.validation.invalid_label","message":"Cannot extract Product_Observational/File_Area_Observational/Table_Character/Record_Character from label"}],"fragments":[],"dataContents":[]}],"summary":{"totalErrors":1,"totalWarnings":0,"messageTypes":[{"messageType":"error.validation.invalid_label","total":1}]}} ==> expected: <0> but was: <1>
[ERROR]   ValidationIntegrationTests.testGithub17:1053 No error messages expected.
{"title":"PDS Validation Tool Report","configuration":{"version":"1.25.0-SNAPSHOT","date":"2020-12-17T17:39:29Z"},"parameters":{"targets":"[file:/home/qchau/sandbox/validate/src/test/resources/github17/delimited_table_good.xml]","ruleType":"pds4.label","severityLevel":"WARNING","recurseDirectories":"true","fileFiltersUsed":"[*.xml, *.XML]","dataContentValidation":"on","productLevelValidation":"on","maxErrors":"100000","registeredContextsFile":"/home/qchau/sandbox/validate/src/main/resources/util/registered_context_products.json"},"productLevelValidationResults":[{"status":"FAIL","label":"file:/home/qchau/sandbox/validate/src/test/resources/github17/delimited_table_good.xml","messages":[{"severity":"ERROR","type":"error.validation.invalid_label","message":"Cannot extract Product_Observational/File_Area_Observational/Table_Character/Record_Character from label"}],"fragments":[],"dataContents":[]}],"summary":{"totalErrors":1,"totalWarnings":0,"messageTypes":[{"messageType":"error.validation.invalid_label","total":1}]}} ==> expected: <0> but was: <1>
[ERROR]   ValidationIntegrationTests.testGithub209:1190->validateRunner:1225 0 error message(s) expected. See validation report: /home/qchau/sandbox/validate/target/test/report_1608226758427VALID_odf07155_msgr_11.xml.json ==> expected: <0> but was: <1>
[ERROR]   ValidationIntegrationTests.testPDS543:110 /home/qchau/sandbox/validate/src/test/resources/PDS-543/report.expected.json and /home/qchau/sandbox/validate/target/test/report_PDS543.json do not match. ==> expected: <{"totalErrors":14,"totalWarnings":0,"messageTypes":[{"messageType":"error.table.record_length_mismatch","total":13},{"messageType":"error.validation.invalid_label","total":1}]}> but was: <{"totalErrors":13,"totalWarnings":0,"messageTypes":[{"messageType":"error.table.record_length_mismatch","total":13}]}>
[INFO] 
[ERROR] Tests run: 19, Failures: 4, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD FAILURE
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  47.901 s
[INFO] Finished at: 2020-12-17T09:39:39-08:00
[INFO] ------------------------------------------------------------------------
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3:test (default-test) on project validate: There are test failures.
[ERROR] 
[ERROR] Please refer to /home/qchau/sandbox/validate/target/surefire-reports for the individual test results.
[ERROR] Please refer to dump files (if any exist) [date].dump, [date]-jvmRun[N].dump and [date].dumpstream.
[ERROR] -> [Help 1]
[ERROR] 
[ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
[ERROR] Re-run Maven using the -X switch to enable full debug logging.
[ERROR] 
[ERROR] For more information about the errors and possible solutions, please read the following articles:
[ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
