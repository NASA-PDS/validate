# Change Log

## [v1.22.2](https://github.com/NASA-PDS-Incubator/validate/tree/v1.22.2) (2020-04-11)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.22.1...v1.22.2)

**Fixed bugs:**

- validate outputs an extra '.' [\#214](https://github.com/NASA-PDS-Incubator/validate/issues/214)
- Validate incorrectly fails ASCII\_Integer field that is space-padded \(empty\) in Table\_Character [\#206](https://github.com/NASA-PDS-Incubator/validate/issues/206)
- Assembly plugin non-fatal errors on Windows [\#203](https://github.com/NASA-PDS-Incubator/validate/issues/203)
- Schematron doesn't fire if \<?xml-model ...\> has an extra space at the end [\#201](https://github.com/NASA-PDS-Incubator/validate/issues/201)
- Validate Table\_Character groups and their specified lengths match the specified group\_length [\#63](https://github.com/NASA-PDS-Incubator/validate/issues/63)

**Merged pull requests:**

- Revert "Remove confusion about XML file filters" [\#213](https://github.com/NASA-PDS-Incubator/validate/pull/213) ([jordanpadams](https://github.com/jordanpadams))
- Validate incorrectly fails for ASCII\_Integer value in Table\_Character [\#207](https://github.com/NASA-PDS-Incubator/validate/pull/207) ([hhlee445](https://github.com/hhlee445))
- Fixed \#203, Assembly plugin non-fatal errors on Windows [\#205](https://github.com/NASA-PDS-Incubator/validate/pull/205) ([tdddblog](https://github.com/tdddblog))
- Update schematron schematyphens pattern with whitespace check [\#202](https://github.com/NASA-PDS-Incubator/validate/pull/202) ([jordanpadams](https://github.com/jordanpadams))

## [v1.22.1](https://github.com/NASA-PDS-Incubator/validate/tree/v1.22.1) (2020-03-28)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.22.0...v1.22.1)

## [v1.22.0](https://github.com/NASA-PDS-Incubator/validate/tree/v1.22.0) (2020-03-28)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.21.0...v1.22.0)

**Implemented enhancements:**

- Change behaviour for printing of "dots" to show progress [\#197](https://github.com/NASA-PDS-Incubator/validate/issues/197)
- Improve installation procedure [\#195](https://github.com/NASA-PDS-Incubator/validate/issues/195)
- Update validate-bundle to include report filepath flag [\#194](https://github.com/NASA-PDS-Incubator/validate/issues/194)
- Update documentation to include more details about how/where to file bug reports [\#187](https://github.com/NASA-PDS-Incubator/validate/issues/187)
- Upgrade to Saxon 9.9.1-7 to fix Schematron bug [\#182](https://github.com/NASA-PDS-Incubator/validate/issues/182)
- Add validated product counter to pass/fail reporting and end summary [\#132](https://github.com/NASA-PDS-Incubator/validate/issues/132)

**Fixed bugs:**

- Fix schematron parsing bug introduced during performance improvements [\#175](https://github.com/NASA-PDS-Incubator/validate/issues/175)

**Closed issues:**

- CCB-282: Revert validation checks on strict file naming requirements [\#192](https://github.com/NASA-PDS-Incubator/validate/issues/192)

**Merged pull requests:**

- Lessen restriciton on file/directory naming [\#200](https://github.com/NASA-PDS-Incubator/validate/pull/200) ([jordanpadams](https://github.com/jordanpadams))
- Add report file path flag to validate-bundle [\#198](https://github.com/NASA-PDS-Incubator/validate/pull/198) ([jordanpadams](https://github.com/jordanpadams))
- Update to Saxon v9.9.1-7 [\#193](https://github.com/NASA-PDS-Incubator/validate/pull/193) ([jordanpadams](https://github.com/jordanpadams))
- Add validated product counter to pass/fail reporting and end summary [\#191](https://github.com/NASA-PDS-Incubator/validate/pull/191) ([hhlee445](https://github.com/hhlee445))

## [v1.21.0](https://github.com/NASA-PDS-Incubator/validate/tree/v1.21.0) (2020-03-11)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.20.0...v1.21.0)

**Implemented enhancements:**

- Develop validate-bundle script for Validation performance improvements using parallelization approach [\#128](https://github.com/NASA-PDS-Incubator/validate/issues/128)

**Closed issues:**

- Document new test case for I&T and deliver to I&T engineer [\#130](https://github.com/NASA-PDS-Incubator/validate/issues/130)
- Document new performance benchmarks [\#129](https://github.com/NASA-PDS-Incubator/validate/issues/129)
- Enhance validate performance to average \<1 second with content validation enabled [\#123](https://github.com/NASA-PDS-Incubator/validate/issues/123)

**Merged pull requests:**

- Validate Table\_Character groups and group\_lengths do not overlap [\#186](https://github.com/NASA-PDS-Incubator/validate/pull/186) ([hhlee445](https://github.com/hhlee445))
- Implement validate-bundle script [\#184](https://github.com/NASA-PDS-Incubator/validate/pull/184) ([galenatjpl](https://github.com/galenatjpl))

## [v1.20.0](https://github.com/NASA-PDS-Incubator/validate/tree/v1.20.0) (2020-02-03)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.19.0...v1.20.0)

**Fixed bugs:**

- Memory leak issue has returned after Saxon downgrade [\#183](https://github.com/NASA-PDS-Incubator/validate/issues/183)

**Merged pull requests:**

- Upgrade Saxon to help prevent memory leak [\#185](https://github.com/NASA-PDS-Incubator/validate/pull/185) ([jordanpadams](https://github.com/jordanpadams))

## [v1.19.0](https://github.com/NASA-PDS-Incubator/validate/tree/v1.19.0) (2020-01-31)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.18.2...v1.19.0)

**Fixed bugs:**

- Fix multi-threading bug where reporting finishes prior to threads completing execution [\#180](https://github.com/NASA-PDS-Incubator/validate/issues/180)
- Improve performance when content validation is disabled [\#178](https://github.com/NASA-PDS-Incubator/validate/issues/178)

**Merged pull requests:**

- Fixes multi-threading and MD5 checksum generation bugs [\#181](https://github.com/NASA-PDS-Incubator/validate/pull/181) ([jordanpadams](https://github.com/jordanpadams))
- issue\_63: Modify pom.xml to use latest pds4-jparser pkg [\#179](https://github.com/NASA-PDS-Incubator/validate/pull/179) ([hhlee445](https://github.com/hhlee445))
- Revert back to Saxon 9.5.1-3 to avoid schematron bug [\#176](https://github.com/NASA-PDS-Incubator/validate/pull/176) ([jordanpadams](https://github.com/jordanpadams))

## [v1.18.2](https://github.com/NASA-PDS-Incubator/validate/tree/v1.18.2) (2020-01-22)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.18.1...v1.18.2)

**Fixed bugs:**

- Bug with \#149 fix: Product counter is invalid and does not reset after each collection [\#173](https://github.com/NASA-PDS-Incubator/validate/issues/173)

**Merged pull requests:**

- Fix bug introduced from \#149 fix [\#174](https://github.com/NASA-PDS-Incubator/validate/pull/174) ([jordanpadams](https://github.com/jordanpadams))

## [v1.18.1](https://github.com/NASA-PDS-Incubator/validate/tree/v1.18.1) (2020-01-21)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.18.0...v1.18.1)

## [v1.18.0](https://github.com/NASA-PDS-Incubator/validate/tree/v1.18.0) (2020-01-21)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.17.5...v1.18.0)

**Implemented enhancements:**

- Update POM to use PDS Parent POM [\#158](https://github.com/NASA-PDS-Incubator/validate/issues/158)
- L5.PRP.VA.42	The tool shall average less than 1 second execution time when content validation is disabled. [\#124](https://github.com/NASA-PDS-Incubator/validate/issues/124)
- Validate that Table\_Character/Table\_Binary fields match the field length definitions in the label [\#56](https://github.com/NASA-PDS-Incubator/validate/issues/56)

**Fixed bugs:**

- Update validate to allow for empty fields in delimited tables [\#170](https://github.com/NASA-PDS-Incubator/validate/issues/170)
- Content validation multi-threading issue [\#167](https://github.com/NASA-PDS-Incubator/validate/issues/167)
- Spot check bug throws internal\_error when record number is multiple of spot check number [\#165](https://github.com/NASA-PDS-Incubator/validate/issues/165)
- Remove erroneous timing messages from log output [\#160](https://github.com/NASA-PDS-Incubator/validate/issues/160)
- Fix string == comparison issues per vulnerability scan [\#144](https://github.com/NASA-PDS-Incubator/validate/issues/144)

**Closed issues:**

- Introduce label and array timing metrics logging [\#156](https://github.com/NASA-PDS-Incubator/validate/issues/156)
- Document new test case for I&T and delivery to I&T engineer [\#127](https://github.com/NASA-PDS-Incubator/validate/issues/127)
- Document new performance benchmarks [\#126](https://github.com/NASA-PDS-Incubator/validate/issues/126)
- Implement validation performance improvements [\#125](https://github.com/NASA-PDS-Incubator/validate/issues/125)

**Merged pull requests:**

- Update field value validator to allow empty fields [\#172](https://github.com/NASA-PDS-Incubator/validate/pull/172) ([jordanpadams](https://github.com/jordanpadams))
- issue\_149: Update to check number of records specified in label and from the table [\#169](https://github.com/NASA-PDS-Incubator/validate/pull/169) ([hhlee445](https://github.com/hhlee445))
- Fix spotCheckData bug for content validation of tables [\#166](https://github.com/NASA-PDS-Incubator/validate/pull/166) ([jordanpadams](https://github.com/jordanpadams))
- Update pom to use PDS Parent POM [\#163](https://github.com/NASA-PDS-Incubator/validate/pull/163) ([jordanpadams](https://github.com/jordanpadams))
- Update string comparison to use .equals\(\) [\#162](https://github.com/NASA-PDS-Incubator/validate/pull/162) ([jordanpadams](https://github.com/jordanpadams))
- Update all timing metrics to only appear for DEBUG [\#161](https://github.com/NASA-PDS-Incubator/validate/pull/161) ([jordanpadams](https://github.com/jordanpadams))
- issue\_56: Added error message when the definition of two fields in a … [\#159](https://github.com/NASA-PDS-Incubator/validate/pull/159) ([hhlee445](https://github.com/hhlee445))
- issue\_156:  Performance Log Metrics and more [\#157](https://github.com/NASA-PDS-Incubator/validate/pull/157) ([galenatjpl](https://github.com/galenatjpl))
- Issue 126 [\#154](https://github.com/NASA-PDS-Incubator/validate/pull/154) ([hhlee445](https://github.com/hhlee445))
- Issue \#124: The tool shall average less than 1 second execution time when content validation is disabled [\#152](https://github.com/NASA-PDS-Incubator/validate/pull/152) ([hhlee445](https://github.com/hhlee445))

## [v1.17.5](https://github.com/NASA-PDS-Incubator/validate/tree/v1.17.5) (2019-10-25)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.17.4...v1.17.5)

**Fixed bugs:**

- Suppress INFO messages related to initial fix for JAXB vulnerability [\#2](https://github.com/NASA-PDS-Incubator/validate/issues/2)

**Merged pull requests:**

- Added java.util.logging properties to suppress messages [\#151](https://github.com/NASA-PDS-Incubator/validate/pull/151) ([jordanpadams](https://github.com/jordanpadams))

## [v1.17.4](https://github.com/NASA-PDS-Incubator/validate/tree/v1.17.4) (2019-10-22)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.17.3...v1.17.4)

**Fixed bugs:**

- Update parent LID integrity checking to fix bug introduced for collection integrity checking [\#146](https://github.com/NASA-PDS-Incubator/validate/issues/146)
- Incorrect help information for --spot-check-data flag [\#145](https://github.com/NASA-PDS-Incubator/validate/issues/145)

**Closed issues:**

- Validate operational release for Build 10a [\#106](https://github.com/NASA-PDS-Incubator/validate/issues/106)

**Merged pull requests:**

- Fix bug with parent LID integrity checking for collections [\#147](https://github.com/NASA-PDS-Incubator/validate/pull/147) ([jordanpadams](https://github.com/jordanpadams))

## [v1.17.3](https://github.com/NASA-PDS-Incubator/validate/tree/v1.17.3) (2019-10-16)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.17.2...v1.17.3)

**Fixed bugs:**

- Revisit PDS-605: L5.PRP.VA.36: Check that the LID for a product has the LID of its parent collection product as its base [\#139](https://github.com/NASA-PDS-Incubator/validate/issues/139)

**Merged pull requests:**

- Updated parent LID check to include ':' [\#140](https://github.com/NASA-PDS-Incubator/validate/pull/140) ([jordanpadams](https://github.com/jordanpadams))

## [v1.17.2](https://github.com/NASA-PDS-Incubator/validate/tree/v1.17.2) (2019-10-16)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.17.1...v1.17.2)

**Fixed bugs:**

- Update delimited table handling to allow for empty fields [\#137](https://github.com/NASA-PDS-Incubator/validate/issues/137)

**Merged pull requests:**

- Update field validator to allow empty fields for delimited tables [\#138](https://github.com/NASA-PDS-Incubator/validate/pull/138) ([jordanpadams](https://github.com/jordanpadams))

## [v1.17.1](https://github.com/NASA-PDS-Incubator/validate/tree/v1.17.1) (2019-10-15)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.17.0...v1.17.1)

**Implemented enhancements:**

- Update config to include telescopes and facilities telescopes and facilities [\#135](https://github.com/NASA-PDS-Incubator/validate/issues/135)

**Merged pull requests:**

- Update context product config query to include all context products [\#136](https://github.com/NASA-PDS-Incubator/validate/pull/136) ([jordanpadams](https://github.com/jordanpadams))

## [v1.17.0](https://github.com/NASA-PDS-Incubator/validate/tree/v1.17.0) (2019-10-15)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.15.3...v1.17.0)

**Implemented enhancements:**

- Deprecate --no-data-check to --skip-content-validation to fall in line with other argument naming [\#133](https://github.com/NASA-PDS-Incubator/validate/issues/133)
- Update context checks to include data\_to\_investigation [\#90](https://github.com/NASA-PDS-Incubator/validate/issues/90)
- Update validate to apply catalog file and local schemas to discipline schema resolution [\#87](https://github.com/NASA-PDS-Incubator/validate/issues/87)
- Add new target-manifest flag for user to provide a manifest \(file\) of file/directory paths to validate [\#50](https://github.com/NASA-PDS-Incubator/validate/issues/50)
- Add flag to temporarily disable context validation [\#47](https://github.com/NASA-PDS-Incubator/validate/issues/47)
- Document multi-threaded architecture for maximized performance [\#43](https://github.com/NASA-PDS-Incubator/validate/issues/43)
- Perform benchmarking on validate to enable documenting of system requirements [\#41](https://github.com/NASA-PDS-Incubator/validate/issues/41)
- Enhance content validation to decrease validation time to \<=50% of current benchmark [\#30](https://github.com/NASA-PDS-Incubator/validate/issues/30)
- Improve pds4.bundle validation performance [\#29](https://github.com/NASA-PDS-Incubator/validate/issues/29)
- Improve documentation about usage of flags in config file with latest flags [\#20](https://github.com/NASA-PDS-Incubator/validate/issues/20)

**Fixed bugs:**

- Update Validate error/warning counts per ATM regression testing [\#118](https://github.com/NASA-PDS-Incubator/validate/issues/118)
- Registered context product resource lists only one of multiple instrument or target multiple types [\#107](https://github.com/NASA-PDS-Incubator/validate/issues/107)
- Fix bug introduce by --skip-context-validation feature [\#99](https://github.com/NASA-PDS-Incubator/validate/issues/99)
- Update Validate to allow specification of schema/schematrons in config file [\#84](https://github.com/NASA-PDS-Incubator/validate/issues/84)
- Context validation needs to be updated to include all possible product type use cases [\#62](https://github.com/NASA-PDS-Incubator/validate/issues/62)
- Update content validation to fail when the value of an ASCII\_Integer is all spaces [\#9](https://github.com/NASA-PDS-Incubator/validate/issues/9)
- Large Data File Uncaught Exception Error [\#8](https://github.com/NASA-PDS-Incubator/validate/issues/8)
- JAXB warning message with Java 9+ [\#3](https://github.com/NASA-PDS-Incubator/validate/issues/3)

**Closed issues:**

- Revert name/type checks from context validation until discussed with TWG [\#120](https://github.com/NASA-PDS-Incubator/validate/issues/120)
- Validate release candidate for Build 10a [\#105](https://github.com/NASA-PDS-Incubator/validate/issues/105)
- Update context name check to throw WARNING instead of ERROR [\#102](https://github.com/NASA-PDS-Incubator/validate/issues/102)
- Create new "error message decoder ring" for common validate issues and how to solve them [\#97](https://github.com/NASA-PDS-Incubator/validate/issues/97)
- Update validate to apply catalogs/schemas used as input across all ingested schemas [\#86](https://github.com/NASA-PDS-Incubator/validate/issues/86)
- Test and document benchmark metrics from improved content validation [\#80](https://github.com/NASA-PDS-Incubator/validate/issues/80)
- Implement content validation performance improvements [\#79](https://github.com/NASA-PDS-Incubator/validate/issues/79)
- Investigate and determine potential content validation performance improvements [\#78](https://github.com/NASA-PDS-Incubator/validate/issues/78)
- Check that referenced context products are valid [\#45](https://github.com/NASA-PDS-Incubator/validate/issues/45)
- Verify that all name/type attribute values correspond to names denoted context products [\#15](https://github.com/NASA-PDS-Incubator/validate/issues/15)

**Merged pull requests:**

- Change --no-data-check flag to --skip-content-validation [\#134](https://github.com/NASA-PDS-Incubator/validate/pull/134) ([jordanpadams](https://github.com/jordanpadams))
- Build 10a I&T bug fixes [\#122](https://github.com/NASA-PDS-Incubator/validate/pull/122) ([jordanpadams](https://github.com/jordanpadams))
- Update schema resolution to use catalog resolver [\#115](https://github.com/NASA-PDS-Incubator/validate/pull/115) ([jordanpadams](https://github.com/jordanpadams))
- Update launcher to allow config specification of schemas [\#112](https://github.com/NASA-PDS-Incubator/validate/pull/112) ([jordanpadams](https://github.com/jordanpadams))
- Update context product check to allow multi-valued name/type [\#111](https://github.com/NASA-PDS-Incubator/validate/pull/111) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#42: Add capability to ignore product-level validation [\#109](https://github.com/NASA-PDS-Incubator/validate/pull/109) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#30: Performance Improvements [\#108](https://github.com/NASA-PDS-Incubator/validate/pull/108) ([galenatjpl](https://github.com/galenatjpl))
- Update context\_ref\_mismatch rule to be WARNING instead of ERROR [\#104](https://github.com/NASA-PDS-Incubator/validate/pull/104) ([jordanpadams](https://github.com/jordanpadams))
- Add new target-manifest flag for user to provide a manifest of file/directory paths [\#103](https://github.com/NASA-PDS-Incubator/validate/pull/103) ([jordanpadams](https://github.com/jordanpadams))
- Add skip context validation to class [\#100](https://github.com/NASA-PDS-Incubator/validate/pull/100) ([jordanpadams](https://github.com/jordanpadams))
- Add new Common Errors page to documentation [\#98](https://github.com/NASA-PDS-Incubator/validate/pull/98) ([jordanpadams](https://github.com/jordanpadams))
- issue \#3: JAXB warning message with Java 9+ [\#95](https://github.com/NASA-PDS-Incubator/validate/pull/95) ([danyu-pds](https://github.com/danyu-pds))
- issue \#20: Improve documentation about usage of flags in config file [\#94](https://github.com/NASA-PDS-Incubator/validate/pull/94) ([danyu-pds](https://github.com/danyu-pds))
- \[\#47\] Add capability for disabling context reference validation [\#93](https://github.com/NASA-PDS-Incubator/validate/pull/93) ([jordanpadams](https://github.com/jordanpadams))
- Issue 41: Added timing\_metrics.sh [\#92](https://github.com/NASA-PDS-Incubator/validate/pull/92) ([galenatjpl](https://github.com/galenatjpl))
- Adds `data\_to\_investigation` context references [\#91](https://github.com/NASA-PDS-Incubator/validate/pull/91) ([jordanpadams](https://github.com/jordanpadams))
- Fix for ASCII\_Integer should fail on space character [\#88](https://github.com/NASA-PDS-Incubator/validate/pull/88) ([hhlee445](https://github.com/hhlee445))
- issue\_8: Fix for Uncaught Exception Error [\#83](https://github.com/NASA-PDS-Incubator/validate/pull/83) ([hhlee445](https://github.com/hhlee445))
- Issue 15: Verify that all name/type attribute values correspond to  names denoted context products [\#82](https://github.com/NASA-PDS-Incubator/validate/pull/82) ([danyu-pds](https://github.com/danyu-pds))
- Issue \#62: Include wider range of context products in validation [\#76](https://github.com/NASA-PDS-Incubator/validate/pull/76) ([jordanpadams](https://github.com/jordanpadams))

## [v1.15.3](https://github.com/NASA-PDS-Incubator/validate/tree/v1.15.3) (2019-08-20)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.15.2...v1.15.3)

**Fixed bugs:**

- Validate execution script throws arguments error with v1.15.1 [\#74](https://github.com/NASA-PDS-Incubator/validate/issues/74)

**Merged pull requests:**

- Issue \#74: Update tar/zip assemblies to ignore sources jars [\#75](https://github.com/NASA-PDS-Incubator/validate/pull/75) ([jordanpadams](https://github.com/jordanpadams))

## [v1.15.2](https://github.com/NASA-PDS-Incubator/validate/tree/v1.15.2) (2019-08-19)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.15.1...v1.15.2)

**Implemented enhancements:**

- Complete engineering point build 1.15.1 [\#64](https://github.com/NASA-PDS-Incubator/validate/issues/64)
- Add capability to pds4.bundle validation to ignore product-level validation [\#42](https://github.com/NASA-PDS-Incubator/validate/issues/42)

**Fixed bugs:**

- Catalog file flag expects a List of files requiring it not be the last argument [\#72](https://github.com/NASA-PDS-Incubator/validate/issues/72)
- Unable to use catalog file after update to always force remote schemas [\#71](https://github.com/NASA-PDS-Incubator/validate/issues/71)

**Closed issues:**

- Update pom to exclude additional SLF4J bindings [\#70](https://github.com/NASA-PDS-Incubator/validate/issues/70)

**Merged pull requests:**

- Issue \#70 \#71 \#72: Update launcher and pom to better handle catalog files [\#73](https://github.com/NASA-PDS-Incubator/validate/pull/73) ([jordanpadams](https://github.com/jordanpadams))

## [v1.15.1](https://github.com/NASA-PDS-Incubator/validate/tree/v1.15.1) (2019-08-08)
**Implemented enhancements:**

- Update Maven docs to refer to Github release assets [\#65](https://github.com/NASA-PDS-Incubator/validate/issues/65)
- Investigate and fix memory leak causing validate crash on large bundles [\#39](https://github.com/NASA-PDS-Incubator/validate/issues/39)
- Prepare repository for Maven Central deployment [\#31](https://github.com/NASA-PDS-Incubator/validate/issues/31)
- Add capability for user to input JSON file with additional context products [\#28](https://github.com/NASA-PDS-Incubator/validate/issues/28)
- Update build per pds4-tools rename and open source [\#26](https://github.com/NASA-PDS-Incubator/validate/issues/26)
- Update context check to retrieve and use latest context products from EN Registry [\#24](https://github.com/NASA-PDS-Incubator/validate/issues/24)
- Deprecate -f and -m flags [\#23](https://github.com/NASA-PDS-Incubator/validate/issues/23)
- Update pom and readme to reference public docs [\#21](https://github.com/NASA-PDS-Incubator/validate/issues/21)
- Remove references to PDS Maven Repo and update packages to build without it [\#19](https://github.com/NASA-PDS-Incubator/validate/issues/19)

**Fixed bugs:**

- Content validation does not work properly [\#55](https://github.com/NASA-PDS-Incubator/validate/issues/55)
- Incorrect package declarations under gov.nasa.tools.web.ui [\#52](https://github.com/NASA-PDS-Incubator/validate/issues/52)
- JAVA Path does not handle whitespaces in MAC as it used to [\#37](https://github.com/NASA-PDS-Incubator/validate/issues/37)
- Add missing core.properties dependency [\#34](https://github.com/NASA-PDS-Incubator/validate/issues/34)
- Validation succeeds despite throwing exception [\#33](https://github.com/NASA-PDS-Incubator/validate/issues/33)

**Merged pull requests:**

- Issue \#28: Integration tests for new feature checkout [\#61](https://github.com/NASA-PDS-Incubator/validate/pull/61) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#28: Add capability for user to input JSON file with additional context products [\#60](https://github.com/NASA-PDS-Incubator/validate/pull/60) ([danyu-pds](https://github.com/danyu-pds))
- Issue \#23: Validate Tool - Deprecate -f and -m flags [\#59](https://github.com/NASA-PDS-Incubator/validate/pull/59) ([hhlee445](https://github.com/hhlee445))
- Issue \#24: Retrieve and use latest context products from EN Registry [\#53](https://github.com/NASA-PDS-Incubator/validate/pull/53) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#33: Ignore resources with null systemId [\#49](https://github.com/NASA-PDS-Incubator/validate/pull/49) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#39: Fix memory leak on large bundles [\#36](https://github.com/NASA-PDS-Incubator/validate/pull/36) ([galenatjpl](https://github.com/galenatjpl))
- Merging develop into master [\#35](https://github.com/NASA-PDS-Incubator/validate/pull/35) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#31: POM and README updates for Maven operational deployment [\#32](https://github.com/NASA-PDS-Incubator/validate/pull/32) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#26: Update build per pds4-tools rename and open source [\#27](https://github.com/NASA-PDS-Incubator/validate/pull/27) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#19: Add PDS Core library from PDSEN repo [\#25](https://github.com/NASA-PDS-Incubator/validate/pull/25) ([jordanpadams](https://github.com/jordanpadams))
- Issue \#21: Update docs to reference public data [\#22](https://github.com/NASA-PDS-Incubator/validate/pull/22) ([jordanpadams](https://github.com/jordanpadams))



\* *This Change Log was automatically generated by [github_changelog_generator](https://github.com/skywinder/Github-Changelog-Generator)*