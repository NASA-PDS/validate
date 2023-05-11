# Changelog

## [«unknown»](https://github.com/NASA-PDS/validate/tree/«unknown») (2023-05-11)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v3.2.0...«unknown»)

**Requirements:**

- As a user, I want to throw a WARNING when a product's schematron version does not match the schema version [\#628](https://github.com/NASA-PDS/validate/issues/628)
- As a user, I would like to enforce browse file extension with encoding type [\#617](https://github.com/NASA-PDS/validate/issues/617)
- As a user, I want to be able to use both online and local schema/schematron files. [\#599](https://github.com/NASA-PDS/validate/issues/599)
- As a user I want to see the name of a table/array in errors, if one is specified [\#343](https://github.com/NASA-PDS/validate/issues/343)
- As a user, I want to validate content for all possible PDS4 table types [\#217](https://github.com/NASA-PDS/validate/issues/217)
- As a user, I want to validate all data types possible per the PDS4 Information Model [\#212](https://github.com/NASA-PDS/validate/issues/212)

**Improvements:**

- Update datetime regex for content validation [\#608](https://github.com/NASA-PDS/validate/issues/608)

**Defects:**

- Expected value in validate report for context reference name is not same as value in the context file [\#631](https://github.com/NASA-PDS/validate/issues/631) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Validate incorrectly enforces file naming requirements on bundles/collections [\#561](https://github.com/NASA-PDS/validate/issues/561) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- warning.table.characters\_between\_fields missing for last record in table [\#431](https://github.com/NASA-PDS/validate/issues/431) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Validation fails to catch real value in ASCII\_NonNegative\_Integer field \(Table\_Delimited\) [\#190](https://github.com/NASA-PDS/validate/issues/190) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

## [v3.2.0](https://github.com/NASA-PDS/validate/tree/v3.2.0) (2023-04-14)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v3.1.1...v3.2.0)

**Requirements:**

- As a user, I want to validate all products referenced from a collection exist within the archive [\#596](https://github.com/NASA-PDS/validate/issues/596)
- As a user, I want to validate all collections referenced from a bundle exist within the archive [\#595](https://github.com/NASA-PDS/validate/issues/595)
- As a user, I want validate to throw an error when a collection inventory contains an invalid secondary product reference [\#462](https://github.com/NASA-PDS/validate/issues/462)
- As a user, I want to validate all internal references to products in the PDS archive are valid [\#415](https://github.com/NASA-PDS/validate/issues/415)
- As a user, I want to validate all internal references from one product to another exist within the archive [\#316](https://github.com/NASA-PDS/validate/issues/316)
- As a user, I want to execute content validation against every nth file [\#1](https://github.com/NASA-PDS/validate/issues/1)

**Improvements:**

- Create command-line script to wrap the new registry referential integrity checker [\#601](https://github.com/NASA-PDS/validate/issues/601)
- Update ArrayObject exceptions to enable improved error messaging [\#567](https://github.com/NASA-PDS/validate/issues/567)
- Scaled value min/max error does not display scaled value [\#434](https://github.com/NASA-PDS/validate/issues/434)

**Defects:**

- verbosity flag does not appear to output INFO messages [\#620](https://github.com/NASA-PDS/validate/issues/620) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate does not correctly validate byte offsets to data objects [\#616](https://github.com/NASA-PDS/validate/issues/616) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- invalid\_object\_definition occurs upon out-of-order data objects [\#614](https://github.com/NASA-PDS/validate/issues/614) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Missing validation of valid\_maximum and valid\_minimum from Special\_Constants [\#611](https://github.com/NASA-PDS/validate/issues/611) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate does not maintain history of other versions it comes across as it traverses directories causing erroneous WARNING messages [\#597](https://github.com/NASA-PDS/validate/issues/597) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Regression in validate no longer enabling CRLF to be embedded within a Table\_Character record [\#593](https://github.com/NASA-PDS/validate/issues/593) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
-  ERROR  \[error.array.value\_out\_of\_min\_max\_range\] evaluation is not correct [\#529](https://github.com/NASA-PDS/validate/issues/529) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate should throw record length error when record delimiter does not occur in correct location [\#519](https://github.com/NASA-PDS/validate/issues/519) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate does not catch NaNs in Binary Tables [\#514](https://github.com/NASA-PDS/validate/issues/514) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

**Other closed issues:**

- Update to use non-zero exit code if any ERROR in execution [\#622](https://github.com/NASA-PDS/validate/issues/622)
- Improve new referential integrity checker timeout tolerance [\#619](https://github.com/NASA-PDS/validate/issues/619)
- Add unit tests to support registry referential integrity checker tool [\#603](https://github.com/NASA-PDS/validate/issues/603)
- Add summary to report to referential integrity execution [\#602](https://github.com/NASA-PDS/validate/issues/602)

## [v3.1.1](https://github.com/NASA-PDS/validate/tree/v3.1.1) (2023-01-03)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v3.1.0...v3.1.1)

## [v3.1.0](https://github.com/NASA-PDS/validate/tree/v3.1.0) (2023-01-03)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v3.0.3...v3.1.0)

**Requirements:**

- As a user, I want to be able to use validate from a docker container [\#556](https://github.com/NASA-PDS/validate/issues/556)

**Improvements:**

- Improve error message when file is truncated and cannot be read [\#569](https://github.com/NASA-PDS/validate/issues/569)
- Improve error messages for overlapping objects in a label [\#436](https://github.com/NASA-PDS/validate/issues/436)

**Defects:**

- validate does not correctly handle field format checks for hex values [\#576](https://github.com/NASA-PDS/validate/issues/576) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Array object validation regression in v3.0.3 [\#564](https://github.com/NASA-PDS/validate/issues/564) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- --spot-check-data flag throws IOException [\#554](https://github.com/NASA-PDS/validate/issues/554) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- Validate fails regression test on issue 188 [\#551](https://github.com/NASA-PDS/validate/issues/551) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate partially fails regression test on issue 388 [\#550](https://github.com/NASA-PDS/validate/issues/550) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate fails regression test on issue 303 [\#548](https://github.com/NASA-PDS/validate/issues/548) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate gives a error.table.bad\_field\_read error [\#544](https://github.com/NASA-PDS/validate/issues/544) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- ERROR  \[error.table.bad\_file\_read\] incorrectly reports that GroupFieldBinary group\_length is larger than size of contained fields [\#531](https://github.com/NASA-PDS/validate/issues/531) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Table\_Character not accurately checking field formats [\#511](https://github.com/NASA-PDS/validate/issues/511) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate doesn't flag a data file with only LF [\#499](https://github.com/NASA-PDS/validate/issues/499) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate can't find files in directory specified by \<directory\_path\_name\> [\#474](https://github.com/NASA-PDS/validate/issues/474) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

## [v3.0.3](https://github.com/NASA-PDS/validate/tree/v3.0.3) (2022-11-10)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v3.0.2...v3.0.3)

**Defects:**

- Validate 2.4.0/3.0.0 fail with NPE/internal error on certain test cases [\#545](https://github.com/NASA-PDS/validate/issues/545)

## [v3.0.2](https://github.com/NASA-PDS/validate/tree/v3.0.2) (2022-11-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v3.0.0...v3.0.2)

## [v3.0.0](https://github.com/NASA-PDS/validate/tree/v3.0.0) (2022-11-02)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.4.0...v3.0.0)

## [v2.4.0](https://github.com/NASA-PDS/validate/tree/v2.4.0) (2022-11-01)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.3.0...v2.4.0)

**Improvements:**

- Refactor `getMessageCountBasedOnProblemType` function using introspection [\#537](https://github.com/NASA-PDS/validate/issues/537)
- Refactor content validation to more robustly handle intermingled Headers [\#425](https://github.com/NASA-PDS/validate/issues/425)

**Defects:**

- Bug in validate when reading products with a number of records \> 4-byte range [\#533](https://github.com/NASA-PDS/validate/issues/533) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- Validate does not calculate overlaps correctly when Header is not first object in file [\#480](https://github.com/NASA-PDS/validate/issues/480) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- NullPointerException when Table\_Delimited is missing records attribute [\#473](https://github.com/NASA-PDS/validate/issues/473) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Validate should not check PDF/A validity if content validation is disabled [\#453](https://github.com/NASA-PDS/validate/issues/453) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- pds4.bundle option seems to not travel through enough subdirectories [\#444](https://github.com/NASA-PDS/validate/issues/444) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Build and unit tests do not pass on Windows OS [\#204](https://github.com/NASA-PDS/validate/issues/204) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Fix uncaught exception error when validating an array object [\#155](https://github.com/NASA-PDS/validate/issues/155) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]

## [v2.3.0](https://github.com/NASA-PDS/validate/tree/v2.3.0) (2022-07-25)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.2.3...v2.3.0)

**Requirements:**

- As a user, I want to receive an error when no products are found within the validation target [\#524](https://github.com/NASA-PDS/validate/issues/524)
- As a user, I want to validate labels/bundles/collections using the LBLX file extension [\#482](https://github.com/NASA-PDS/validate/issues/482)
- As a developer, I want an API method enable specifying of non-registered context products [\#241](https://github.com/NASA-PDS/validate/issues/241)

**Defects:**

- validate embedded in an app bundled as a fat-jar raise exception on product validation  [\#516](https://github.com/NASA-PDS/validate/issues/516) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]

**Other closed issues:**

- Set registered context products in doValidation method [\#526](https://github.com/NASA-PDS/validate/issues/526)
- Improve Content Validation for PDF/A [\#497](https://github.com/NASA-PDS/validate/issues/497)

## [v2.2.3](https://github.com/NASA-PDS/validate/tree/v2.2.3) (2022-06-09)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.2.2...v2.2.3)

## [v2.2.2](https://github.com/NASA-PDS/validate/tree/v2.2.2) (2022-06-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.2.1...v2.2.2)

## [v2.2.1](https://github.com/NASA-PDS/validate/tree/v2.2.1) (2022-06-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.2.0...v2.2.1)

**Defects:**

- validate having issues checking some file content on windows [\#507](https://github.com/NASA-PDS/validate/issues/507) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- validate passes confusing message to the command window [\#503](https://github.com/NASA-PDS/validate/issues/503) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- validate erroneously flags PDF/A-1a compliant file [\#479](https://github.com/NASA-PDS/validate/issues/479) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

## [v2.2.0](https://github.com/NASA-PDS/validate/tree/v2.2.0) (2022-04-07)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.1.4...v2.2.0)

**Defects:**

- Fix validate compilation issues due to removal of veraPDF artifacts from maven central [\#470](https://github.com/NASA-PDS/validate/issues/470) [[s.critical](https://github.com/NASA-PDS/validate/labels/s.critical)]
- Validate content validation does not handle properly special\_constants and field\_statistics when they both appear [\#469](https://github.com/NASA-PDS/validate/issues/469) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate repo cannot be checked out on Windows without errors [\#411](https://github.com/NASA-PDS/validate/issues/411) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]

## [v2.1.4](https://github.com/NASA-PDS/validate/tree/v2.1.4) (2021-12-23)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.1.3...v2.1.4)

**Defects:**

- Validate is reporting an "Uncaught exception while validating" error on Windows [\#464](https://github.com/NASA-PDS/validate/issues/464)
- \[SECURITY\] Patch log4j library [\#461](https://github.com/NASA-PDS/validate/issues/461) [[s.critical](https://github.com/NASA-PDS/validate/labels/s.critical)]

## [v2.1.3](https://github.com/NASA-PDS/validate/tree/v2.1.3) (2021-12-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.1.2...v2.1.3)

## [v2.1.2](https://github.com/NASA-PDS/validate/tree/v2.1.2) (2021-12-10)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.1.1...v2.1.2)

**Defects:**

- validate flags .ASC extension as invalid but offers not expected suffices [\#457](https://github.com/NASA-PDS/validate/issues/457)

**Other closed issues:**

- Update PDF/A documentation to include link to VeraPDF validation rules [\#454](https://github.com/NASA-PDS/validate/issues/454)

## [v2.1.1](https://github.com/NASA-PDS/validate/tree/v2.1.1) (2021-12-10)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.1.0...v2.1.1)

**Requirements:**

- Improve PDF/A validation to include more robust reporting on failures [\#388](https://github.com/NASA-PDS/validate/issues/388)
- As a user, I want to check that all Internal References are valid references to other PDS4 products within the current validating bundle [\#308](https://github.com/NASA-PDS/validate/issues/308)

**Improvements:**

- Improve warning message for missing\_context\_reference [\#421](https://github.com/NASA-PDS/validate/issues/421)
- Update PDF validation to check against flavour specified in PDF metadata [\#412](https://github.com/NASA-PDS/validate/issues/412)
- Update rule documentation to remove "auto-detect" mention [\#377](https://github.com/NASA-PDS/validate/issues/377)

**Defects:**

- Validate does not correctly pass PDF/A files that are in a subdirectory [\#447](https://github.com/NASA-PDS/validate/issues/447) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- Validate is reporting a 'String index out of range' error for a text file [\#441](https://github.com/NASA-PDS/validate/issues/441) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- Incorrect Warning for Missing document\_standard\_id is Stream\_Text [\#439](https://github.com/NASA-PDS/validate/issues/439) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Array Content Validator is not accepting values at the min/max due to false precision [\#435](https://github.com/NASA-PDS/validate/issues/435) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate warns "document standard id ... is not correct" on good labels [\#429](https://github.com/NASA-PDS/validate/issues/429) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- Validate does not allow SIP tab file to have lines of differing lengths [\#424](https://github.com/NASA-PDS/validate/issues/424) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate does not allow a single-character subdirectory [\#423](https://github.com/NASA-PDS/validate/issues/423) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate 2.2.0-SNAPSHOT warns about a pretty benign bundle + readme.txt [\#419](https://github.com/NASA-PDS/validate/issues/419) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate 2.1.0 indicates the table offset is not correct when validating a binary table inside a FITS [\#416](https://github.com/NASA-PDS/validate/issues/416) [[s.critical](https://github.com/NASA-PDS/validate/labels/s.critical)]
- Validate 2.1.0-SNAPSHOT skips a collection XML label [\#408](https://github.com/NASA-PDS/validate/issues/408) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Checksums output lowercase and do not accept uppercase checksums [\#376](https://github.com/NASA-PDS/validate/issues/376) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- validate allows absolute path in directory\_path\_name but shouldn't [\#349](https://github.com/NASA-PDS/validate/issues/349) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

## [v2.1.0](https://github.com/NASA-PDS/validate/tree/v2.1.0) (2021-10-05)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.7...v2.1.0)

**Requirements:**

- As a user, I want to validate all files referenced by a Product\_Document [\#367](https://github.com/NASA-PDS/validate/issues/367)

**Improvements:**

- Refactor PDF/A check handling to match with similar product checks [\#374](https://github.com/NASA-PDS/validate/issues/374)
- Update pds4 version mismatch warning message and problem type [\#373](https://github.com/NASA-PDS/validate/issues/373)
- Improve validate performance by removing unnecessary file IO [\#358](https://github.com/NASA-PDS/validate/issues/358)

**Defects:**

- validate does not flag \<CR\> within lid\_reference [\#401](https://github.com/NASA-PDS/validate/issues/401) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate throws incorrect overlap error when first Field\_Bit has length 1 [\#392](https://github.com/NASA-PDS/validate/issues/392) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate does not work correctly when the path name contains a space [\#381](https://github.com/NASA-PDS/validate/issues/381) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- stack trace being created during successful validate execution [\#380](https://github.com/NASA-PDS/validate/issues/380) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- FileService:printStackTraceToFile:ERROR when validating a product with overlapping fields [\#379](https://github.com/NASA-PDS/validate/issues/379)
- validate raises an unexpected error with doi attribute in the Citation\_information class [\#378](https://github.com/NASA-PDS/validate/issues/378)
- validate halts if label has name "collection" embedded [\#375](https://github.com/NASA-PDS/validate/issues/375) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Issues with logic for reading latest version of collections, and file read logging lost with v2.\* of validate [\#372](https://github.com/NASA-PDS/validate/issues/372) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Product referential integrity check throws invalid WARNINGs [\#368](https://github.com/NASA-PDS/validate/issues/368) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate should not check if file is PDF/A if --skip-content-validation is enabled [\#366](https://github.com/NASA-PDS/validate/issues/366) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- validate does not check Header of a File\_Area\_Ancillary nor does not provide a meaningful error message for an incorrect Table\_Character offset [\#361](https://github.com/NASA-PDS/validate/issues/361) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate does not parse colon in Windows path [\#360](https://github.com/NASA-PDS/validate/issues/360) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]

## [v2.0.7](https://github.com/NASA-PDS/validate/tree/v2.0.7) (2021-07-03)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.6...v2.0.7)

**Improvements:**

- Improve validate reporting when trying to read a null row [\#355](https://github.com/NASA-PDS/validate/issues/355)

**Defects:**

- validate does not allow ".XML" as an extension for a label file [\#364](https://github.com/NASA-PDS/validate/issues/364) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate allows CRLF within a Table\_Delimited field [\#357](https://github.com/NASA-PDS/validate/issues/357) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate labels error.sub\_directory.unallowed\_name as a warning [\#356](https://github.com/NASA-PDS/validate/issues/356) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]

## [v2.0.6](https://github.com/NASA-PDS/validate/tree/v2.0.6) (2021-05-25)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.5...v2.0.6)

**Defects:**

- validate incorrectly flags integers bounded by "" in a .csv [\#345](https://github.com/NASA-PDS/validate/issues/345) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

## [v2.0.5](https://github.com/NASA-PDS/validate/tree/v2.0.5) (2021-05-20)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.4...v2.0.5)

**Defects:**

- validate inexplicably writes to validate\_stack\_traces.log [\#344](https://github.com/NASA-PDS/validate/issues/344) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]

## [v2.0.4](https://github.com/NASA-PDS/validate/tree/v2.0.4) (2021-05-11)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.3...v2.0.4)

**Defects:**

- Validate fails with Java Error [\#337](https://github.com/NASA-PDS/validate/issues/337) [[s.high](https://github.com/NASA-PDS/validate/labels/s.high)]
- validate2.0.3 throws unexpected JavaExceptions, and after fix, it hangs when trying bundle validation [\#336](https://github.com/NASA-PDS/validate/issues/336) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate gives a NullPointerException during validation of a directory containing Table\_Character products [\#335](https://github.com/NASA-PDS/validate/issues/335) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate 2.1.0 snapshot fails on a label with 2 table\_character [\#334](https://github.com/NASA-PDS/validate/issues/334) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate -u exits badly [\#333](https://github.com/NASA-PDS/validate/issues/333)
- validate bundle incorrectly reports "not a member of any collection" that it passed before [\#328](https://github.com/NASA-PDS/validate/issues/328) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate fails to process large data file [\#327](https://github.com/NASA-PDS/validate/issues/327) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- File-size check fails for large data files [\#326](https://github.com/NASA-PDS/validate/issues/326) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate Incorrectly Throws Error When Embedded Field\_Character Contains \<CR\>\<LF\> [\#325](https://github.com/NASA-PDS/validate/issues/325) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]

## [v2.0.3](https://github.com/NASA-PDS/validate/tree/v2.0.3) (2021-05-03)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.2...v2.0.3)

**Requirements:**

- As a user, I want to the raise a WARNING if the object-defined size in the label does not match the file\_size value [\#303](https://github.com/NASA-PDS/validate/issues/303)
- As a user, I want to validate PDF files are PDF/A [\#164](https://github.com/NASA-PDS/validate/issues/164)
- As a user, I want to be warned when there are alphanumeric characters between fields in Table\_Character [\#57](https://github.com/NASA-PDS/validate/issues/57)

## [v2.0.2](https://github.com/NASA-PDS/validate/tree/v2.0.2) (2021-04-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v2.0.0...v2.0.2)

**Improvements:**

- Upgrade to Java 9+ [\#323](https://github.com/NASA-PDS/validate/issues/323)
- Update installation documentation to require Java 1.9+ [\#322](https://github.com/NASA-PDS/validate/issues/322)

## [v2.0.0](https://github.com/NASA-PDS/validate/tree/v2.0.0) (2021-04-06)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/1.24.0...v2.0.0)

**Requirements:**

- CCB-264: Make the Line Feed \(LF\) character an allowed record delimiter [\#292](https://github.com/NASA-PDS/validate/issues/292)
- As a user, I want validate to raise a WARNING when differing versions of IM are used within a bundle [\#210](https://github.com/NASA-PDS/validate/issues/210)
- As a user, I want to validate a bundle that uses multiple versions of the Information Model / Discipline LDDs [\#188](https://github.com/NASA-PDS/validate/issues/188)

**Improvements:**

- Migrate subset of existing regression tests to cucumber behavioral testing [\#290](https://github.com/NASA-PDS/validate/issues/290)
- Update installation documentation to include 64-bit Java as system requirement [\#264](https://github.com/NASA-PDS/validate/issues/264)
- validate does not perform expediently when doing bundle-level validation against large bundles [\#254](https://github.com/NASA-PDS/validate/issues/254)
- Implement initial behavioral testing framework with cucumber [\#252](https://github.com/NASA-PDS/validate/issues/252)
- Add output directory flag to validate-bundle tool [\#246](https://github.com/NASA-PDS/validate/issues/246)
- validate does not perform full bundle validation when using a specific bundle.xml [\#238](https://github.com/NASA-PDS/validate/issues/238)
- Update validate per SR requirements for collection inventories [\#230](https://github.com/NASA-PDS/validate/issues/230)
- Validate and throw error when duplicate LIDs are found in Bundle [\#81](https://github.com/NASA-PDS/validate/issues/81)
- Provide the capability to specify multiple locations for pds4.bundle validation [\#51](https://github.com/NASA-PDS/validate/issues/51)
- Validate schematron references and throw fatal error if invalid URI specified [\#17](https://github.com/NASA-PDS/validate/issues/17)

**Defects:**

- Validate missing collections in bundle after CCB-282 updates [\#310](https://github.com/NASA-PDS/validate/issues/310) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- unclear error message for field count matching [\#301](https://github.com/NASA-PDS/validate/issues/301) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate -u flag reports an error on Windows [\#300](https://github.com/NASA-PDS/validate/issues/300) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate tool does not PASS a bundle with a single-character filename [\#299](https://github.com/NASA-PDS/validate/issues/299) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- validate misses double quotes within a delimited table [\#298](https://github.com/NASA-PDS/validate/issues/298) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Content validation of ASCII\_Integer field does not accept value with leading zeroes [\#297](https://github.com/NASA-PDS/validate/issues/297) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Content validation incorrectly reports error for floating-point values out of specified min/max range [\#294](https://github.com/NASA-PDS/validate/issues/294) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- When validating a product with a bad schematron definition, bundle validation also fails indicating the associated product does not exist [\#291](https://github.com/NASA-PDS/validate/issues/291) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Validate fails to report error in   File.file\_size [\#281](https://github.com/NASA-PDS/validate/issues/281) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Registered context products file does not retain older versions of context products [\#278](https://github.com/NASA-PDS/validate/issues/278) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Bug performing bundle validation with nested directories [\#273](https://github.com/NASA-PDS/validate/issues/273) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate 1.25.0-SNAPSHOT raises an exception when validating a product [\#271](https://github.com/NASA-PDS/validate/issues/271) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Missing documentation about deprecated flags [\#260](https://github.com/NASA-PDS/validate/issues/260) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Product with incorrect table binary definition pass validation [\#257](https://github.com/NASA-PDS/validate/issues/257) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- validate should only do integrity checking on latest version of a collection when referenced by LID [\#256](https://github.com/NASA-PDS/validate/issues/256)
- Unexpected error for data collection in a sub-directory [\#240](https://github.com/NASA-PDS/validate/issues/240) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Validate error reading tables \> 2GiB [\#189](https://github.com/NASA-PDS/validate/issues/189)
- Update validate to throw error when a file has a space in the filename [\#153](https://github.com/NASA-PDS/validate/issues/153) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Update allowable field\_format values per Standards Reference definition regarding \[+|-\] characters [\#11](https://github.com/NASA-PDS/validate/issues/11) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]
- Improve pds4.bundle unlabeled files check to handle files without a file suffix [\#6](https://github.com/NASA-PDS/validate/issues/6) [[s.medium](https://github.com/NASA-PDS/validate/labels/s.medium)]
- Improve file base name check according to Standards Reference [\#5](https://github.com/NASA-PDS/validate/issues/5) [[s.low](https://github.com/NASA-PDS/validate/labels/s.low)]

**Other closed issues:**

- Implement initial test to exercise framework [\#283](https://github.com/NASA-PDS/validate/issues/283)
- Implement initial framework to be able to start developing tests [\#282](https://github.com/NASA-PDS/validate/issues/282)
- Retrofit validate CI to use roundup-action [\#255](https://github.com/NASA-PDS/validate/issues/255)

## [1.24.0](https://github.com/NASA-PDS/validate/tree/1.24.0) (2020-09-09)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/1.23.3...1.24.0)

## [1.23.3](https://github.com/NASA-PDS/validate/tree/1.23.3) (2020-08-06)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/1.23.2...1.23.3)

**Defects:**

- validate 1.24.0-SNAPSHOT chokes on a probably good file [\#243](https://github.com/NASA-PDS/validate/issues/243)

## [1.23.2](https://github.com/NASA-PDS/validate/tree/1.23.2) (2020-08-04)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.23.1...1.23.2)

**Defects:**

- Validate gives incorrect records mismatch WARNING for interleaved data objects [\#234](https://github.com/NASA-PDS/validate/issues/234)
- Product validation does not detect the number of table records correctly for Table + Array object [\#233](https://github.com/NASA-PDS/validate/issues/233)

## [v1.23.1](https://github.com/NASA-PDS/validate/tree/v1.23.1) (2020-05-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.23.0...v1.23.1)

**Defects:**

- Incorrect validation of number of records [\#220](https://github.com/NASA-PDS/validate/issues/220)
- Packed\_Data\_Fields and bit fields do not validate as expected [\#209](https://github.com/NASA-PDS/validate/issues/209)

## [v1.23.0](https://github.com/NASA-PDS/validate/tree/v1.23.0) (2020-05-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.4...v1.23.0)

**Improvements:**

- Reduce error messages for overlapping fields [\#222](https://github.com/NASA-PDS/validate/issues/222)
- Throw warning when data exists after number of records [\#215](https://github.com/NASA-PDS/validate/issues/215)

**Defects:**

- validate v1.22.3 has large performance degradation on products with many tables [\#219](https://github.com/NASA-PDS/validate/issues/219)
- Software raises a field\_value\_overlap error on Table\_Binary Packed data fields [\#177](https://github.com/NASA-PDS/validate/issues/177)

## [v1.22.4](https://github.com/NASA-PDS/validate/tree/v1.22.4) (2020-05-01)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.3...v1.22.4)

## [v1.22.3](https://github.com/NASA-PDS/validate/tree/v1.22.3) (2020-04-18)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.2...v1.22.3)

**Requirements:**

- As a user, I want validate to check number of records/fields specified in label matches the records in the actual data table [\#149](https://github.com/NASA-PDS/validate/issues/149)

## [v1.22.2](https://github.com/NASA-PDS/validate/tree/v1.22.2) (2020-04-11)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.1...v1.22.2)

**Requirements:**

- Validate Table\_Character groups and their specified lengths match the specified group\_length [\#63](https://github.com/NASA-PDS/validate/issues/63)

**Defects:**

- validate outputs an extra '.' [\#214](https://github.com/NASA-PDS/validate/issues/214)
- Validate incorrectly fails ASCII\_Integer field that is space-padded \(empty\) in Table\_Character [\#206](https://github.com/NASA-PDS/validate/issues/206)
- Assembly plugin non-fatal errors on Windows [\#203](https://github.com/NASA-PDS/validate/issues/203)
- Schematron doesn't fire if \<?xml-model ...\> has an extra space at the end [\#201](https://github.com/NASA-PDS/validate/issues/201)

## [v1.22.1](https://github.com/NASA-PDS/validate/tree/v1.22.1) (2020-03-28)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.0...v1.22.1)

## [v1.22.0](https://github.com/NASA-PDS/validate/tree/v1.22.0) (2020-03-28)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.21.0...v1.22.0)

**Improvements:**

- Change behaviour for printing of "dots" to show progress [\#197](https://github.com/NASA-PDS/validate/issues/197)
- Improve installation procedure [\#195](https://github.com/NASA-PDS/validate/issues/195)
- Update validate-bundle to include report filepath flag [\#194](https://github.com/NASA-PDS/validate/issues/194)
- Update documentation to include more details about how/where to file bug reports [\#187](https://github.com/NASA-PDS/validate/issues/187)
- Upgrade to Saxon 9.9.1-7 to fix Schematron bug [\#182](https://github.com/NASA-PDS/validate/issues/182)
- Add validated product counter to pass/fail reporting and end summary [\#132](https://github.com/NASA-PDS/validate/issues/132)

**Defects:**

- Fix schematron parsing bug introduced during performance improvements [\#175](https://github.com/NASA-PDS/validate/issues/175)

**Other closed issues:**

- CCB-282: Revert validation checks on strict file naming requirements [\#192](https://github.com/NASA-PDS/validate/issues/192)

## [v1.21.0](https://github.com/NASA-PDS/validate/tree/v1.21.0) (2020-03-11)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.20.0...v1.21.0)

**Improvements:**

- Develop validate-bundle script for Validation performance improvements using parallelization approach [\#128](https://github.com/NASA-PDS/validate/issues/128)

**Other closed issues:**

- Document new test case for I&T and deliver to I&T engineer [\#130](https://github.com/NASA-PDS/validate/issues/130)
- Document new performance benchmarks [\#129](https://github.com/NASA-PDS/validate/issues/129)
- Enhance validate performance to average \<1 second with content validation enabled [\#123](https://github.com/NASA-PDS/validate/issues/123)

## [v1.20.0](https://github.com/NASA-PDS/validate/tree/v1.20.0) (2020-02-03)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.19.0...v1.20.0)

**Defects:**

- Memory leak issue has returned after Saxon downgrade [\#183](https://github.com/NASA-PDS/validate/issues/183)

## [v1.19.0](https://github.com/NASA-PDS/validate/tree/v1.19.0) (2020-01-31)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.18.2...v1.19.0)

**Defects:**

- Fix multi-threading bug where reporting finishes prior to threads completing execution [\#180](https://github.com/NASA-PDS/validate/issues/180)
- Improve performance when content validation is disabled [\#178](https://github.com/NASA-PDS/validate/issues/178)

## [v1.18.2](https://github.com/NASA-PDS/validate/tree/v1.18.2) (2020-01-22)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.18.1...v1.18.2)

**Defects:**

- Bug with \#149 fix: Product counter is invalid and does not reset after each collection [\#173](https://github.com/NASA-PDS/validate/issues/173)

## [v1.18.1](https://github.com/NASA-PDS/validate/tree/v1.18.1) (2020-01-21)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.18.0...v1.18.1)

## [v1.18.0](https://github.com/NASA-PDS/validate/tree/v1.18.0) (2020-01-21)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.5...v1.18.0)

**Improvements:**

- Update POM to use PDS Parent POM [\#158](https://github.com/NASA-PDS/validate/issues/158)
- L5.PRP.VA.42	The tool shall average less than 1 second execution time when content validation is disabled. [\#124](https://github.com/NASA-PDS/validate/issues/124)
- Validate that Table\_Character/Table\_Binary fields match the field length definitions in the label [\#56](https://github.com/NASA-PDS/validate/issues/56)

**Defects:**

- Update validate to allow for empty fields in delimited tables [\#170](https://github.com/NASA-PDS/validate/issues/170)
- Content validation multi-threading issue [\#167](https://github.com/NASA-PDS/validate/issues/167)
- Spot check bug throws internal\_error when record number is multiple of spot check number [\#165](https://github.com/NASA-PDS/validate/issues/165)
- Remove erroneous timing messages from log output [\#160](https://github.com/NASA-PDS/validate/issues/160)
- Fix string == comparison issues per vulnerability scan [\#144](https://github.com/NASA-PDS/validate/issues/144)

**Other closed issues:**

- Introduce label and array timing metrics logging [\#156](https://github.com/NASA-PDS/validate/issues/156)
- Document new test case for I&T and delivery to I&T engineer [\#127](https://github.com/NASA-PDS/validate/issues/127)
- Document new performance benchmarks [\#126](https://github.com/NASA-PDS/validate/issues/126)
- Implement validation performance improvements [\#125](https://github.com/NASA-PDS/validate/issues/125)

## [v1.17.5](https://github.com/NASA-PDS/validate/tree/v1.17.5) (2019-10-25)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.4...v1.17.5)

**Defects:**

- Suppress INFO messages related to initial fix for JAXB vulnerability [\#2](https://github.com/NASA-PDS/validate/issues/2)

## [v1.17.4](https://github.com/NASA-PDS/validate/tree/v1.17.4) (2019-10-22)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.3...v1.17.4)

**Defects:**

- Update parent LID integrity checking to fix bug introduced for collection integrity checking [\#146](https://github.com/NASA-PDS/validate/issues/146)
- Incorrect help information for --spot-check-data flag [\#145](https://github.com/NASA-PDS/validate/issues/145)

**Other closed issues:**

- Validate operational release for Build 10a [\#106](https://github.com/NASA-PDS/validate/issues/106)

## [v1.17.3](https://github.com/NASA-PDS/validate/tree/v1.17.3) (2019-10-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.2...v1.17.3)

**Defects:**

- Revisit PDS-605: L5.PRP.VA.36: Check that the LID for a product has the LID of its parent collection product as its base [\#139](https://github.com/NASA-PDS/validate/issues/139)

## [v1.17.2](https://github.com/NASA-PDS/validate/tree/v1.17.2) (2019-10-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.1...v1.17.2)

**Defects:**

- Update delimited table handling to allow for empty fields [\#137](https://github.com/NASA-PDS/validate/issues/137)

## [v1.17.1](https://github.com/NASA-PDS/validate/tree/v1.17.1) (2019-10-15)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.0...v1.17.1)

**Improvements:**

- Update config to include telescopes and facilities telescopes and facilities [\#135](https://github.com/NASA-PDS/validate/issues/135)

## [v1.17.0](https://github.com/NASA-PDS/validate/tree/v1.17.0) (2019-10-15)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.15.3...v1.17.0)

**Requirements:**

- Add new target-manifest flag for user to provide a manifest \(file\) of file/directory paths to validate [\#50](https://github.com/NASA-PDS/validate/issues/50)
- Verify that all name/type attribute values correspond to names denoted context products [\#15](https://github.com/NASA-PDS/validate/issues/15)

**Improvements:**

- Deprecate --no-data-check to --skip-content-validation to fall in line with other argument naming [\#133](https://github.com/NASA-PDS/validate/issues/133)
- Update context checks to include data\_to\_investigation [\#90](https://github.com/NASA-PDS/validate/issues/90)
- Update validate to apply catalog file and local schemas to discipline schema resolution [\#87](https://github.com/NASA-PDS/validate/issues/87)
- Add flag to temporarily disable context validation [\#47](https://github.com/NASA-PDS/validate/issues/47)
- Document multi-threaded architecture for maximized performance [\#43](https://github.com/NASA-PDS/validate/issues/43)
- Perform benchmarking on validate to enable documenting of system requirements [\#41](https://github.com/NASA-PDS/validate/issues/41)
- Enhance content validation to decrease validation time to \<=50% of current benchmark [\#30](https://github.com/NASA-PDS/validate/issues/30)
- Improve pds4.bundle validation performance [\#29](https://github.com/NASA-PDS/validate/issues/29)
- Improve documentation about usage of flags in config file with latest flags [\#20](https://github.com/NASA-PDS/validate/issues/20)

**Defects:**

- Update Validate error/warning counts per ATM regression testing [\#118](https://github.com/NASA-PDS/validate/issues/118)
- Registered context product resource lists only one of multiple instrument or target multiple types [\#107](https://github.com/NASA-PDS/validate/issues/107)
- Fix bug introduce by --skip-context-validation feature [\#99](https://github.com/NASA-PDS/validate/issues/99)
- Update Validate to allow specification of schema/schematrons in config file [\#84](https://github.com/NASA-PDS/validate/issues/84)
- Context validation needs to be updated to include all possible product type use cases [\#62](https://github.com/NASA-PDS/validate/issues/62)
- Update content validation to fail when the value of an ASCII\_Integer is all spaces [\#9](https://github.com/NASA-PDS/validate/issues/9)
- Large Data File Uncaught Exception Error [\#8](https://github.com/NASA-PDS/validate/issues/8)
- JAXB warning message with Java 9+ [\#3](https://github.com/NASA-PDS/validate/issues/3)

**Other closed issues:**

- Revert name/type checks from context validation until discussed with TWG [\#120](https://github.com/NASA-PDS/validate/issues/120)
- Validate release candidate for Build 10a [\#105](https://github.com/NASA-PDS/validate/issues/105)
- Update context name check to throw WARNING instead of ERROR [\#102](https://github.com/NASA-PDS/validate/issues/102)
- Create new "error message decoder ring" for common validate issues and how to solve them [\#97](https://github.com/NASA-PDS/validate/issues/97)
- Update validate to apply catalogs/schemas used as input across all ingested schemas [\#86](https://github.com/NASA-PDS/validate/issues/86)
- Test and document benchmark metrics from improved content validation [\#80](https://github.com/NASA-PDS/validate/issues/80)
- Implement content validation performance improvements [\#79](https://github.com/NASA-PDS/validate/issues/79)
- Investigate and determine potential content validation performance improvements [\#78](https://github.com/NASA-PDS/validate/issues/78)
- Check that referenced context products are valid [\#45](https://github.com/NASA-PDS/validate/issues/45)

## [v1.15.3](https://github.com/NASA-PDS/validate/tree/v1.15.3) (2019-08-20)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.15.2...v1.15.3)

**Defects:**

- Validate execution script throws arguments error with v1.15.1 [\#74](https://github.com/NASA-PDS/validate/issues/74)

## [v1.15.2](https://github.com/NASA-PDS/validate/tree/v1.15.2) (2019-08-19)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.15.1...v1.15.2)

**Improvements:**

- Complete engineering point build 1.15.1 [\#64](https://github.com/NASA-PDS/validate/issues/64)
- Add capability to pds4.bundle validation to ignore product-level validation [\#42](https://github.com/NASA-PDS/validate/issues/42)

**Defects:**

- Catalog file flag expects a List of files requiring it not be the last argument [\#72](https://github.com/NASA-PDS/validate/issues/72)
- Unable to use catalog file after update to always force remote schemas [\#71](https://github.com/NASA-PDS/validate/issues/71)

**Other closed issues:**

- Update pom to exclude additional SLF4J bindings [\#70](https://github.com/NASA-PDS/validate/issues/70)

## [v1.15.1](https://github.com/NASA-PDS/validate/tree/v1.15.1) (2019-08-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/6703364685bd0f55e3521491f29589cd801fa6f6...v1.15.1)

**Improvements:**

- Update Maven docs to refer to Github release assets [\#65](https://github.com/NASA-PDS/validate/issues/65)
- Investigate and fix memory leak causing validate crash on large bundles [\#39](https://github.com/NASA-PDS/validate/issues/39)
- Prepare repository for Maven Central deployment [\#31](https://github.com/NASA-PDS/validate/issues/31)
- Add capability for user to input JSON file with additional context products [\#28](https://github.com/NASA-PDS/validate/issues/28)
- Update build per pds4-tools rename and open source [\#26](https://github.com/NASA-PDS/validate/issues/26)
- Update context check to retrieve and use latest context products from EN Registry [\#24](https://github.com/NASA-PDS/validate/issues/24)
- Deprecate -f and -m flags [\#23](https://github.com/NASA-PDS/validate/issues/23)
- Update pom and readme to reference public docs [\#21](https://github.com/NASA-PDS/validate/issues/21)
- Remove references to PDS Maven Repo and update packages to build without it [\#19](https://github.com/NASA-PDS/validate/issues/19)

**Defects:**

- Content validation does not work properly [\#55](https://github.com/NASA-PDS/validate/issues/55)
- Incorrect package declarations under gov.nasa.tools.web.ui [\#52](https://github.com/NASA-PDS/validate/issues/52)
- JAVA Path does not handle whitespaces in MAC as it used to [\#37](https://github.com/NASA-PDS/validate/issues/37)
- Add missing core.properties dependency [\#34](https://github.com/NASA-PDS/validate/issues/34)
- Validation succeeds despite throwing exception [\#33](https://github.com/NASA-PDS/validate/issues/33)



\* *This Changelog was automatically generated by [github_changelog_generator](https://github.com/github-changelog-generator/github-changelog-generator)*
