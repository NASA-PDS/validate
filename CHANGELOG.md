# Change Log

## [v1.15.3](https://github.com/NASA-PDS-Incubator/validate/tree/v1.15.3) (2019-08-19)
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