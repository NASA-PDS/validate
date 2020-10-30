# Changelog

## [1.25.0-SNAPSHOT](https://github.com/NASA-PDS/validate/tree/1.25.0-SNAPSHOT) (2020-10-30)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/1.24.0...1.25.0-SNAPSHOT)

**Improvements:**

- rename validate CLI tool to pds-validate [\#259](https://github.com/NASA-PDS/validate/issues/259)

**Defects:**

- Missing documentation about deprecated flags [\#260](https://github.com/NASA-PDS/validate/issues/260) [[low](https://github.com/NASA-PDS/validate/labels/low)]

## [1.24.0](https://github.com/NASA-PDS/validate/tree/1.24.0) (2020-09-09)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/1.23.3...1.24.0)

## [1.23.3](https://github.com/NASA-PDS/validate/tree/1.23.3) (2020-08-06)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/1.23.2...1.23.3)

**Defects:**

- validate 1.24.0-SNAPSHOT chokes on a probably good file [\#243](https://github.com/NASA-PDS/validate/issues/243) [[low](https://github.com/NASA-PDS/validate/labels/low)]

## [1.23.2](https://github.com/NASA-PDS/validate/tree/1.23.2) (2020-08-04)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.23.1...1.23.2)

**Defects:**

- Validate gives incorrect records mismatch WARNING for interleaved data objects [\#234](https://github.com/NASA-PDS/validate/issues/234) [[medium](https://github.com/NASA-PDS/validate/labels/medium)]
- Product validation does not detect the number of table records correctly for Table + Array object [\#233](https://github.com/NASA-PDS/validate/issues/233) [[high](https://github.com/NASA-PDS/validate/labels/high)]

**Other closed issues:**

- The tool shall verify bundle directories adhere directory naming standards [\#239](https://github.com/NASA-PDS/validate/issues/239)

## [v1.23.1](https://github.com/NASA-PDS/validate/tree/v1.23.1) (2020-05-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.23.0...v1.23.1)

**Improvements:**

- Packed\_Data\_Fields and bit fields do not validate as expected [\#209](https://github.com/NASA-PDS/validate/issues/209) [[high](https://github.com/NASA-PDS/validate/labels/high)]

**Defects:**

- Incorrect validation of number of records [\#220](https://github.com/NASA-PDS/validate/issues/220) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.23.0](https://github.com/NASA-PDS/validate/tree/v1.23.0) (2020-05-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.4...v1.23.0)

**Defects:**

- validate v1.22.3 has large performance degradation on products with many tables [\#219](https://github.com/NASA-PDS/validate/issues/219) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Software raises a field\_value\_overlap error on Table\_Binary Packed data fields [\#177](https://github.com/NASA-PDS/validate/issues/177) [[medium](https://github.com/NASA-PDS/validate/labels/medium)]

**Other closed issues:**

- Reduce error messages for overlapping fields [\#222](https://github.com/NASA-PDS/validate/issues/222)
- Throw warning when data exists after number of records [\#215](https://github.com/NASA-PDS/validate/issues/215)

## [v1.22.4](https://github.com/NASA-PDS/validate/tree/v1.22.4) (2020-05-01)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.3...v1.22.4)

## [v1.22.3](https://github.com/NASA-PDS/validate/tree/v1.22.3) (2020-04-18)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.2...v1.22.3)

**Defects:**

- Update software to check number of records specified in label matches the records in the table [\#149](https://github.com/NASA-PDS/validate/issues/149) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.22.2](https://github.com/NASA-PDS/validate/tree/v1.22.2) (2020-04-11)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.1...v1.22.2)

**Improvements:**

- Validate Table\_Character groups and their specified lengths match the specified group\_length [\#63](https://github.com/NASA-PDS/validate/issues/63) [[high](https://github.com/NASA-PDS/validate/labels/high)]

**Defects:**

- validate outputs an extra '.' [\#214](https://github.com/NASA-PDS/validate/issues/214)
- Validate incorrectly fails ASCII\_Integer field that is space-padded \(empty\) in Table\_Character [\#206](https://github.com/NASA-PDS/validate/issues/206) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Assembly plugin non-fatal errors on Windows [\#203](https://github.com/NASA-PDS/validate/issues/203)
- Schematron doesn't fire if \<?xml-model ...\> has an extra space at the end [\#201](https://github.com/NASA-PDS/validate/issues/201)

## [v1.22.1](https://github.com/NASA-PDS/validate/tree/v1.22.1) (2020-03-28)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.22.0...v1.22.1)

## [v1.22.0](https://github.com/NASA-PDS/validate/tree/v1.22.0) (2020-03-28)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.21.0...v1.22.0)

**Improvements:**

- Fix schematron parsing bug introduced during performance improvements [\#175](https://github.com/NASA-PDS/validate/issues/175) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Add validated product counter to pass/fail reporting and end summary [\#132](https://github.com/NASA-PDS/validate/issues/132) [[high](https://github.com/NASA-PDS/validate/labels/high)]

**Other closed issues:**

- Change behaviour for printing of "dots" to show progress [\#197](https://github.com/NASA-PDS/validate/issues/197)
- Improve installation procedure [\#195](https://github.com/NASA-PDS/validate/issues/195) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Update validate-bundle to include report filepath flag [\#194](https://github.com/NASA-PDS/validate/issues/194) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- CCB-282: Revert validation checks on strict file naming requirements [\#192](https://github.com/NASA-PDS/validate/issues/192) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Update documentation to include more details about how/where to file bug reports [\#187](https://github.com/NASA-PDS/validate/issues/187) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Upgrade to Saxon 9.9.1-7 to fix Schematron bug [\#182](https://github.com/NASA-PDS/validate/issues/182) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.21.0](https://github.com/NASA-PDS/validate/tree/v1.21.0) (2020-03-11)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.20.0...v1.21.0)

**Improvements:**

- Enhance validate performance to average \<1 second with content validation enabled [\#123](https://github.com/NASA-PDS/validate/issues/123)

**Other closed issues:**

- Document new test case for I&T and deliver to I&T engineer [\#130](https://github.com/NASA-PDS/validate/issues/130)
- Document new performance benchmarks [\#129](https://github.com/NASA-PDS/validate/issues/129)
- Develop validate-bundle script for Validation performance improvements using parallelization approach [\#128](https://github.com/NASA-PDS/validate/issues/128) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.20.0](https://github.com/NASA-PDS/validate/tree/v1.20.0) (2020-02-03)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.19.0...v1.20.0)

**Defects:**

- Memory leak issue has returned after Saxon downgrade [\#183](https://github.com/NASA-PDS/validate/issues/183) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.19.0](https://github.com/NASA-PDS/validate/tree/v1.19.0) (2020-01-31)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.18.2...v1.19.0)

**Defects:**

- Fix multi-threading bug where reporting finishes prior to threads completing execution [\#180](https://github.com/NASA-PDS/validate/issues/180) [[medium](https://github.com/NASA-PDS/validate/labels/medium)]
- Improve performance when content validation is disabled [\#178](https://github.com/NASA-PDS/validate/issues/178) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.18.2](https://github.com/NASA-PDS/validate/tree/v1.18.2) (2020-01-22)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.18.1...v1.18.2)

**Defects:**

- Bug with \#149 fix: Product counter is invalid and does not reset after each collection [\#173](https://github.com/NASA-PDS/validate/issues/173) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.18.1](https://github.com/NASA-PDS/validate/tree/v1.18.1) (2020-01-21)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.18.0...v1.18.1)

## [v1.18.0](https://github.com/NASA-PDS/validate/tree/v1.18.0) (2020-01-21)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.5...v1.18.0)

**Improvements:**

- L5.PRP.VA.42	The tool shall average less than 1 second execution time when content validation is disabled. [\#124](https://github.com/NASA-PDS/validate/issues/124)

**Defects:**

- Update validate to allow for empty fields in delimited tables [\#170](https://github.com/NASA-PDS/validate/issues/170) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Content validation multi-threading issue [\#167](https://github.com/NASA-PDS/validate/issues/167) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Spot check bug throws internal\_error when record number is multiple of spot check number [\#165](https://github.com/NASA-PDS/validate/issues/165)
- Remove erroneous timing messages from log output [\#160](https://github.com/NASA-PDS/validate/issues/160) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Fix string == comparison issues per vulnerability scan [\#144](https://github.com/NASA-PDS/validate/issues/144) [[low](https://github.com/NASA-PDS/validate/labels/low)]

**Other closed issues:**

- Update POM to use PDS Parent POM [\#158](https://github.com/NASA-PDS/validate/issues/158)
- Introduce label and array timing metrics logging [\#156](https://github.com/NASA-PDS/validate/issues/156)
- Document new test case for I&T and delivery to I&T engineer [\#127](https://github.com/NASA-PDS/validate/issues/127)
- Document new performance benchmarks [\#126](https://github.com/NASA-PDS/validate/issues/126)
- Implement validation performance improvements [\#125](https://github.com/NASA-PDS/validate/issues/125)
- Validate that Table\_Character/Table\_Binary fields match the field length definitions in the label [\#56](https://github.com/NASA-PDS/validate/issues/56) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.17.5](https://github.com/NASA-PDS/validate/tree/v1.17.5) (2019-10-25)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.4...v1.17.5)

**Defects:**

- Suppress INFO messages related to initial fix for JAXB vulnerability [\#2](https://github.com/NASA-PDS/validate/issues/2) [[low](https://github.com/NASA-PDS/validate/labels/low)]

## [v1.17.4](https://github.com/NASA-PDS/validate/tree/v1.17.4) (2019-10-22)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.3...v1.17.4)

**Defects:**

- Update parent LID integrity checking to fix bug introduced for collection integrity checking [\#146](https://github.com/NASA-PDS/validate/issues/146) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Incorrect help information for --spot-check-data flag [\#145](https://github.com/NASA-PDS/validate/issues/145) [[low](https://github.com/NASA-PDS/validate/labels/low)]

**Other closed issues:**

- Validate operational release for Build 10a [\#106](https://github.com/NASA-PDS/validate/issues/106)

## [v1.17.3](https://github.com/NASA-PDS/validate/tree/v1.17.3) (2019-10-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.2...v1.17.3)

**Defects:**

- Revisit PDS-605: L5.PRP.VA.36: Check that the LID for a product has the LID of its parent collection product as its base [\#139](https://github.com/NASA-PDS/validate/issues/139)

## [v1.17.2](https://github.com/NASA-PDS/validate/tree/v1.17.2) (2019-10-16)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.1...v1.17.2)

**Defects:**

- Update delimited table handling to allow for empty fields [\#137](https://github.com/NASA-PDS/validate/issues/137) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.17.1](https://github.com/NASA-PDS/validate/tree/v1.17.1) (2019-10-15)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.17.0...v1.17.1)

**Other closed issues:**

- Update config to include telescopes and facilities telescopes and facilities [\#135](https://github.com/NASA-PDS/validate/issues/135)

## [v1.17.0](https://github.com/NASA-PDS/validate/tree/v1.17.0) (2019-10-15)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.15.3...v1.17.0)

**Improvements:**

- Check that referenced context products are valid [\#45](https://github.com/NASA-PDS/validate/issues/45)
- Enhance content validation to decrease validation time to \<=50% of current benchmark [\#30](https://github.com/NASA-PDS/validate/issues/30) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Improve pds4.bundle validation performance [\#29](https://github.com/NASA-PDS/validate/issues/29)

**Defects:**

- Update Validate error/warning counts per ATM regression testing [\#118](https://github.com/NASA-PDS/validate/issues/118) [[low](https://github.com/NASA-PDS/validate/labels/low)]
- Registered context product resource lists only one of multiple instrument or target multiple types [\#107](https://github.com/NASA-PDS/validate/issues/107) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Fix bug introduce by --skip-context-validation feature [\#99](https://github.com/NASA-PDS/validate/issues/99) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Update Validate to allow specification of schema/schematrons in config file [\#84](https://github.com/NASA-PDS/validate/issues/84) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Context validation needs to be updated to include all possible product type use cases [\#62](https://github.com/NASA-PDS/validate/issues/62) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Update content validation to fail when the value of an ASCII\_Integer is all spaces [\#9](https://github.com/NASA-PDS/validate/issues/9) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Large Data File Uncaught Exception Error [\#8](https://github.com/NASA-PDS/validate/issues/8) [[low](https://github.com/NASA-PDS/validate/labels/low)]
- JAXB warning message with Java 9+ [\#3](https://github.com/NASA-PDS/validate/issues/3) [[low](https://github.com/NASA-PDS/validate/labels/low)]

**Other closed issues:**

- Deprecate --no-data-check to --skip-content-validation to fall in line with other argument naming [\#133](https://github.com/NASA-PDS/validate/issues/133) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Revert name/type checks from context validation until discussed with TWG [\#120](https://github.com/NASA-PDS/validate/issues/120)
- Validate release candidate for Build 10a [\#105](https://github.com/NASA-PDS/validate/issues/105)
- Update context name check to throw WARNING instead of ERROR [\#102](https://github.com/NASA-PDS/validate/issues/102) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Create new "error message decoder ring" for common validate issues and how to solve them [\#97](https://github.com/NASA-PDS/validate/issues/97) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Update context checks to include data\_to\_investigation [\#90](https://github.com/NASA-PDS/validate/issues/90)
- Update validate to apply catalog file and local schemas to discipline schema resolution [\#87](https://github.com/NASA-PDS/validate/issues/87) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Update validate to apply catalogs/schemas used as input across all ingested schemas [\#86](https://github.com/NASA-PDS/validate/issues/86)
- Test and document benchmark metrics from improved content validation [\#80](https://github.com/NASA-PDS/validate/issues/80)
- Implement content validation performance improvements [\#79](https://github.com/NASA-PDS/validate/issues/79)
- Investigate and determine potential content validation performance improvements [\#78](https://github.com/NASA-PDS/validate/issues/78)
- Add new target-manifest flag for user to provide a manifest \(file\) of file/directory paths to validate [\#50](https://github.com/NASA-PDS/validate/issues/50)
- Add flag to temporarily disable context validation [\#47](https://github.com/NASA-PDS/validate/issues/47)
- Document multi-threaded architecture for maximized performance [\#43](https://github.com/NASA-PDS/validate/issues/43)
- Perform benchmarking on validate to enable documenting of system requirements [\#41](https://github.com/NASA-PDS/validate/issues/41)
- Improve documentation about usage of flags in config file with latest flags [\#20](https://github.com/NASA-PDS/validate/issues/20)
- Verify that all name/type attribute values correspond to names denoted context products [\#15](https://github.com/NASA-PDS/validate/issues/15)

## [v1.15.3](https://github.com/NASA-PDS/validate/tree/v1.15.3) (2019-08-20)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.15.2...v1.15.3)

**Defects:**

- Validate execution script throws arguments error with v1.15.1 [\#74](https://github.com/NASA-PDS/validate/issues/74) [[high](https://github.com/NASA-PDS/validate/labels/high)]

## [v1.15.2](https://github.com/NASA-PDS/validate/tree/v1.15.2) (2019-08-19)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/v1.15.1...v1.15.2)

**Defects:**

- Catalog file flag expects a List of files requiring it not be the last argument [\#72](https://github.com/NASA-PDS/validate/issues/72) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Unable to use catalog file after update to always force remote schemas [\#71](https://github.com/NASA-PDS/validate/issues/71) [[high](https://github.com/NASA-PDS/validate/labels/high)]

**Other closed issues:**

- Update pom to exclude additional SLF4J bindings [\#70](https://github.com/NASA-PDS/validate/issues/70)
- Complete engineering point build 1.15.1 [\#64](https://github.com/NASA-PDS/validate/issues/64)
- Add capability to pds4.bundle validation to ignore product-level validation [\#42](https://github.com/NASA-PDS/validate/issues/42)

## [v1.15.1](https://github.com/NASA-PDS/validate/tree/v1.15.1) (2019-08-08)

[Full Changelog](https://github.com/NASA-PDS/validate/compare/6703364685bd0f55e3521491f29589cd801fa6f6...v1.15.1)

**Defects:**

- Content validation does not work properly [\#55](https://github.com/NASA-PDS/validate/issues/55)
- Incorrect package declarations under gov.nasa.tools.web.ui [\#52](https://github.com/NASA-PDS/validate/issues/52)
- JAVA Path does not handle whitespaces in MAC as it used to [\#37](https://github.com/NASA-PDS/validate/issues/37)
- Add missing core.properties dependency [\#34](https://github.com/NASA-PDS/validate/issues/34) [[high](https://github.com/NASA-PDS/validate/labels/high)]
- Validation succeeds despite throwing exception [\#33](https://github.com/NASA-PDS/validate/issues/33)

**Other closed issues:**

- Update Maven docs to refer to Github release assets [\#65](https://github.com/NASA-PDS/validate/issues/65)
- Investigate and fix memory leak causing validate crash on large bundles [\#39](https://github.com/NASA-PDS/validate/issues/39)
- Prepare repository for Maven Central deployment [\#31](https://github.com/NASA-PDS/validate/issues/31)
- Add capability for user to input JSON file with additional context products [\#28](https://github.com/NASA-PDS/validate/issues/28)
- Update build per pds4-tools rename and open source [\#26](https://github.com/NASA-PDS/validate/issues/26)
- Update context check to retrieve and use latest context products from EN Registry [\#24](https://github.com/NASA-PDS/validate/issues/24)
- Deprecate -f and -m flags [\#23](https://github.com/NASA-PDS/validate/issues/23)
- Update pom and readme to reference public docs [\#21](https://github.com/NASA-PDS/validate/issues/21)
- Remove references to PDS Maven Repo and update packages to build without it [\#19](https://github.com/NASA-PDS/validate/issues/19)



\* *This Changelog was automatically generated       by [github_changelog_generator]      (https://github.com/github-changelog-generator/github-changelog-generator)*
