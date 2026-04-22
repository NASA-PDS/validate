# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

The NASA PDS Validate Tool is a Java-based application for validating PDS4 product labels and PDS3 volumes according to Planetary Data System standards. It validates XML labels against schemas and schematrons, checks data content integrity, and performs referential integrity checks for bundles and collections.

**Main Entry Point**: `gov.nasa.pds.validate.ValidateLauncher` (src/main/java/gov/nasa/pds/validate/ValidateLauncher.java:134)

## Build Commands

```bash
# Compile source code only
mvn compile

# Full build with tests
mvn package

# Full build with site documentation
mvn package site

# Build without running tests (useful when tests fail with extra logging)
mvn clean package -DskipTests

# Run tests only
mvn test

# Run tests excluding ReferenceIntegrityTest with specific Cucumber tags
mvn test -Dtest=\!ReferenceIntegrityTest* -Dcucumber.filter.tags='@v3.7.x'

# Run a single Cucumber scenario by name (issue number and subtest, e.g. #1458-1)
mvn test -Dtest=\!ReferenceIntegrityTest* "-Dcucumber.filter.name=NASA-PDS/validate#1458-1"

# Run a single test class
mvn test -Dtest=ValidationIntegrationTests

# Install to local Maven repository
mvn install

# Deploy to remote repository (automated by CI/CD)
mvn clean site site:stage package deploy -P release

# View documentation locally
mvn site:run
# Then visit http://localhost:8080
```

## Architecture

### Core Validation Flow

1. **ValidateLauncher** (src/main/java/gov/nasa/pds/validate/ValidateLauncher.java) - Command-line parser and orchestrator
   - Parses CLI arguments and configuration files
   - Sets up validation parameters (severity, rules, schemas, catalogs)
   - Initializes reporting system
   - Coordinates validation execution across targets

2. **ValidatorFactory** (src/main/java/gov/nasa/pds/validate/ValidatorFactory.java) - Factory pattern for creating validators
   - Singleton pattern managing validator instances
   - Creates LocationValidator instances configured with appropriate DocumentValidators
   - Caches validators for performance

3. **LocationValidator** (from pds4-jparser dependency) - Orchestrates validation of files/directories
   - Crawls file systems or URLs
   - Applies validation rules based on configured rule type
   - Manages target registrars for referential integrity

4. **Validation Rules** (src/main/java/gov/nasa/pds/tools/validate/rule/pds4/)
   - Rule-based architecture where different validation rules apply based on target type:
     - **pds4.label** - Single label validation
     - **pds4.collection** - Collection product with inventory validation
     - **pds4.bundle** - Bundle product with collection references
     - **pds4.folder** - Directory-level validation
     - **pds3.volume** - PDS3 volume validation
   - Key rule classes:
     - BundleReferentialIntegrityRule - Validates bundle member references
     - CollectionReferentialIntegrityRule - Validates collection inventory
     - ContextProductReferenceValidationRule - Validates context product references against registry
     - FileReferenceValidationRule - Validates file references in labels
     - DataDefinitionAndContentValidationRule - Validates data content matches label definitions
     - **RegisterTargets** (src/main/java/gov/nasa/pds/tools/validate/rule/RegisterTargets.java) - Registers targets and crawls child targets; also contains `checkReservedFilenameMismatch()` which enforces PDS4 SR 6C.1.3 reserved filenames (`collection_*`, `bundle_*`). This check must only apply to label files (XML/LBLX), NOT to non-label files like CSV inventory files. The `TargetType` is determined by `Utility.getTargetType()` → `TargetExaminer.isTargetCollectionType/BundleType()`, which returns `FILE` for non-XML files, causing false positives for CSV inventory files named `collection_*.csv`.

5. **Reporting** (src/main/java/gov/nasa/pds/validate/report/)
   - Multiple report formats supported:
     - **FullReport** - Human-readable text format (default)
     - **JSONReport** - JSON format for programmatic parsing
     - **XmlReport** - XML format
   - Reports capture validation problems at different severity levels (DEBUG, INFO, WARNING, ERROR, FATAL)

### Schema and Schematron Validation

- **SchemaValidator** (gov.nasa.pds.tools.validate.rule.pds4.SchemaValidator) - Validates XML against XSD schemas
- **SchematronTransformer** (gov.nasa.pds.tools.label.SchematronTransformer) - Transforms and applies Schematron rules
- **CachedEntityResolver** / **CachedLSResourceResolver** - Cache schemas/catalogs to avoid repeated downloads
- By default, validates against schemas/schematrons specified in label unless user overrides with `-S`, `-s`, `-C` flags

### Context Product Validation

The tool validates context product references (instruments, telescopes, targets, etc.) against registered products:
- Downloads registered context products from PDS Registry API
- Stores in JSON file (registered-context-products.json in resources.home)
- Use `--latest-json-file` flag to update from Registry
- Use `--nonregprod-json-file` to provide non-registered products for development

### Referential Integrity

- **InMemoryRegistrar** - Stores product references during validation for cross-referencing
- **ReferentialIntegrityUtil** - Static utility for managing referential checks
- Use `--alternate-file-paths` to provide additional directories for resolving references
- Important: ReferentialIntegrityUtil must be reset between test runs (it's static)

## Testing

### Test Structure

- **Cucumber BDD Tests** (src/test/resources/features/)
  - Feature files define test scenarios: 3.6.x.feature, 3.7.x.feature, pre.3.6.x.feature
  - Test data located in src/test/resources/ organized by GitHub issue number (e.g., github123/)
  - Step definitions in src/test/java/cucumber/StepDefs.java
  - Main Cucumber runner: src/test/java/cucumber/CucumberTest.java

- **JUnit Integration Tests** (src/test/java/gov/nasa/pds/validate/)
  - ValidationIntegrationTests.java - Integration test suite

### Adding New Test Cases

Follow the procedure in src/site/markdown/developer/contribute.md:

1. Create test data directory: `src/test/resources/github<issue-number>/`
2. Add test scenario to appropriate feature file (e.g., src/test/resources/features/3.7.x.feature)
3. Test scenario format:
   ```gherkin
   Scenario Outline: Execute Test <testName>
     Given a test <testName> at <issueNumber> with <subtest> at <datasrc> with <args>
     When validate test <testName> at <issueNumber> with <subtest> at <datasrc> with <args>
     Then produced output from <testName> at <issueNumber> with <subtest> at <datasrc> should have <expectation>
   ```
4. Expectation format examples:
   - Expected success: `|  |` (one space)
   - Total warnings: `summary:totalWarnings=3`
   - Total errors: `summary:totalErrors=1`
   - Specific error type: `summary:messageTypes:error.pdf.file.not_pdfa_compliant=1`

5. Run tests:
   - Via Eclipse: Right-click feature file → Run as → Cucumber Feature
   - Via Maven: `mvn test -Dtest=\!ReferenceIntegrityTest* -Dcucumber.filter.tags='@v3.7.x'`

### Test Configuration Notes

- Maven Surefire plugin version pinned to 3.5.2 due to bugs in 3.5.3/3.5.4
- Cucumber test runner class excluded from normal Surefire execution
- Tests run with ERROR log level by default: `-Dorg.slf4j.simpleLogger.defaultLogLevel=ERROR`

## Debugging

### Enabling Debug Logging

The tool uses SLF4J with NOP logger by default (no debug output). To enable debug logging:

1. Edit pom.xml to comment out slf4j-nop dependency (lines 261-265)
2. Uncomment slf4j-simple dependency (lines 256-260)
3. Set system property: `-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG`
4. Rebuild: `mvn clean package -DskipTests`
5. Add property to validate CLI script or use `export JAVA_TOOL_OPTIONS=-Dorg.slf4j.simpleLogger.defaultLogLevel=DEBUG`

Alternatively, use `--verbose 1` flag for INFO level output in reports (default is WARNING level 2), or `--debug-mode` for full stack traces.

## Key Configuration

### Java Version
- Source/Target: Java 17 (pom.xml:172-173)

### Main Dependencies
- **pds4-jparser** (version 2.11.0) - PDS4 label parsing and validation
- **pds3-product-tools** (version 4.4.2) - PDS3 validation
- **Saxon-HE** (version 12.9) - XSLT and XPath processing
- **registry-common** (version 2.1.0-SNAPSHOT) - Registry API integration
- **opensearch-java** (version 3.2.0) - Registry backend queries
- **verapdf** (version 1.28.2) - PDF/A validation
- **junit-jupiter** (version 6.0.0) - Unit testing
- **cucumber** (version 7.30.0) - BDD testing

### System Properties
- `resources.home` - Base directory for configuration files and registered products JSON
- `https.protocols` - Set to "TLSv1.2" in main() (ValidateLauncher.java:1832)

## Important Implementation Details

### Static State Management
- **LabelUtil** maintains static list of Information Model versions across validation runs
  - Call `hardResetInformationModelVersions()` before new validation (ValidateLauncher.java:1432)
  - Call `reduceInformationModelVersions()` for pds4.label rule (ValidateLauncher.java:1514)
- **ReferentialIntegrityUtil** maintains static state for referential checks
  - Call `setContextReferenceCheckFlag()` before validation (ValidateLauncher.java:1436)
  - Call `reset()` after validation to clean up (ValidateLauncher.java:1599)
- **FlagsUtil** stores global flags that must be initialized via `initialize()` (ValidateLauncher.java:331)

### Error Handling
- Maximum errors configurable via `--max-errors` (default: 100,000)
- Tool exits with code 1 if max errors exceeded
- ValidationMonitor coalesces exceptions by location before reporting (ValidateLauncher.java:1852)

### File Crawling
- Default label extension: `.xml` (configurable via `--extension` flag)
- Directory recursion enabled by default (disable with `--local` flag)
- File filtering via regular expressions
- Crawler implementations: FileCrawler, URLCrawler (src/main/java/gov/nasa/pds/validate/crawler/)

## Release Process

Automated via GitHub Actions (roundup-action):
- Push `release/X.Y.Z` branch to trigger release workflow
- Workflow handles version updates, changelog generation, Maven deployment, and site publication
- Manual release steps documented in README.md (lines 140-265) if automation fails

## Common Validation Scenarios

### Validating a Single Label
```bash
validate path/to/label.xml
```

### Validating a Bundle
```bash
validate --rule pds4.bundle path/to/bundle/
```

### Validating with Custom Schema/Schematron
```bash
validate --schema path/to/schema.xsd --schematron path/to/rules.sch --target path/to/label.xml
```

### Skipping Content Validation
```bash
validate --skip-content-validation path/to/label.xml
```

### Validating with Alternate Reference Paths
```bash
validate --rule pds4.collection --alternate-file-paths /path/to/bundle1,/path/to/bundle2 path/to/collection/
```
