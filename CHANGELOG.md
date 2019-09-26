## [v1.16.0-SNAPSHOT](https://github.com/NASA-PDS-Incubator/validate/tree/v1.16.0-SNAPSHOT) (2019-09-25)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.15.3...v1.16.0-SNAPSHOT)

**Implemented enhancements:**

- Document multi-threaded architecture for maximized performance [\#43](https://github.com/NASA-PDS-Incubator/validate/issues/43)
- Enhance content validation to decrease validation time to \<=50% of current benchmark [\#30](https://github.com/NASA-PDS-Incubator/validate/issues/30)
- Update context name check to throw WARNING instead of ERROR [\#102](https://github.com/NASA-PDS-Incubator/validate/issues/102)
- Update context checks to include data\_to\_investigation [\#90](https://github.com/NASA-PDS-Incubator/validate/issues/90)
- Add new target-manifest flag for user to provide a manifest \(file\) of file/directory paths to validate [\#50](https://github.com/NASA-PDS-Incubator/validate/issues/50)
- Add flag to temporarily disable context validation [\#47](https://github.com/NASA-PDS-Incubator/validate/issues/47)

**Fixed bugs:**

- Registered context product resource lists only one of multiple instrument or target multiple types [\#107](https://github.com/NASA-PDS-Incubator/validate/issues/107)
- Update Validate to allow specification of schema/schematrons in config file [\#84](https://github.com/NASA-PDS-Incubator/validate/issues/84)
- Fix bug introduce by --skip-context-validation feature [\#99](https://github.com/NASA-PDS-Incubator/validate/issues/99)
- Context validation needs to be updated to include all possible product type use cases [\#62](https://github.com/NASA-PDS-Incubator/validate/issues/62)
- Update content validation to fail when a space character is used when an ASCII\_Integer type is specified [\#9](https://github.com/NASA-PDS-Incubator/validate/issues/9)
- Large Data File Uncaught Exception Error [\#8](https://github.com/NASA-PDS-Incubator/validate/issues/8)
- JAXB warning message with Java 9+ [\#3](https://github.com/NASA-PDS-Incubator/validate/issues/3)

**Closed issues:**

- Validate release candidate for Build 10a [\#105](https://github.com/NASA-PDS-Incubator/validate/issues/105)
- Create new "error message decoder ring" for common validate issues and how to solve them [\#97](https://github.com/NASA-PDS-Incubator/validate/issues/97)
- Investigate and determine potential content validation performance improvements [\#78](https://github.com/NASA-PDS-Incubator/validate/issues/78)
- Check that referenced context products are valid [\#45](https://github.com/NASA-PDS-Incubator/validate/issues/45)
- Perform benchmarking on validate to enable documenting of system requirements [\#41](https://github.com/NASA-PDS-Incubator/validate/issues/41)
- Improve documentation about usage of flags in config file with latest flags [\#20](https://github.com/NASA-PDS-Incubator/validate/issues/20)
- Verify that all name/type attribute values correspond to names denoted context products [\#15](https://github.com/NASA-PDS-Incubator/validate/issues/15)
- Test and document benchmark metrics from improved content validation [\#80](https://github.com/NASA-PDS-Incubator/validate/issues/80)
- Implement content validation performance improvements [\#79](https://github.com/NASA-PDS-Incubator/validate/issues/79)

**Merged pull requests:**

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
- Issue \#28: Integration tests for new feature checkout [\#61](https://github.com/NASA-PDS-Incubator/validate/pull/61) ([jordanpadams](https://github.com/jordanpadams))

## [v1.15.3](https://github.com/NASA-PDS-Incubator/validate/tree/v1.15.3) (2019-08-20)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.15.2...v1.15.3)

**Fixed bugs:**

- Validate execution script throws arguments error with v1.15.1 [\#74](https://github.com/NASA-PDS-Incubator/validate/issues/74)

**Merged pull requests:**

- Issue \#74: Update tar/zip assemblies to ignore sources jars [\#75](https://github.com/NASA-PDS-Incubator/validate/pull/75) ([jordanpadams](https://github.com/jordanpadams))

## [v1.15.2](https://github.com/NASA-PDS-Incubator/validate/tree/v1.15.2) (2019-08-19)
[Full Changelog](https://github.com/NASA-PDS-Incubator/validate/compare/v1.15.1...v1.15.2)

**Implemented enhancements:**

- Update pom to exclude additional SLF4J bindings [\#70](https://github.com/NASA-PDS-Incubator/validate/issues/70)
- Complete engineering point build 1.15.1 [\#64](https://github.com/NASA-PDS-Incubator/validate/issues/64)

**Fixed bugs:**

- Catalog file flag expects a List of files requiring it not be the last argument [\#72](https://github.com/NASA-PDS-Incubator/validate/issues/72)
- Unable to use catalog file after update to always force remote schemas [\#71](https://github.com/NASA-PDS-Incubator/validate/issues/71)

**Closed issues:**

- Add capability to pds4.bundle validation to ignore product-level validation [\#42](https://github.com/NASA-PDS-Incubator/validate/issues/42)

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