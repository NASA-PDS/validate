# About Validate Tool

The Validate Tool helps you verify that your PDS4 product labels and data meet PDS standards. It checks your labels against schemas and schematrons, validates your data content, and ensures everything is properly referenced and formatted.

## What Validate Can Do

Validate checks your PDS4 archives to make sure they meet all the requirements. Here's what it can validate:

* **Schema and Schematron validation** - You can validate using:
  - Schemas bundled with the tool
  - Your own schemas specified on the command line
  - Schemas referenced in your labels
  - Schemas from an XML Catalog file

* **File reference checks**:
  - Verifies that all referenced files actually exist
  - Checks file name casing matches the physical files
  - Validates checksums (when provided in labels or manifest files)

* **Bundle and Collection validation**:
  - Checks referential integrity across your archive
  - Ensures file and directory names follow PDS4 naming standards

### Table Validation

For tables, Validate checks:

* **All tables**:
  - Field values match their defined data types
  - Values fall within specified min/max ranges

* **Binary tables**:
  - Packed fields have the correct number of bit-fields
  - Bit-field values match their data types

* **Character and delimited tables**:
  - Records end with carriage return and line feed
  - Record lengths don't exceed defined limits
  - Record count matches the label
  - Values match field formats (when specified)

### Array Validation

For arrays, Validate checks:

* Array size matches the expected dimensions (element size × product of axis sizes)
* Element values conform to their defined data types
* Elements match object statistics in the label (when provided)

## Need Help?

**Found a bug or want a new feature?** We'd love to hear from you!

* [Request a new feature](https://github.com/NASA-PDS/validate/issues/new?template=feature_request.yml)
* [Report a bug](https://github.com/NASA-PDS/validate/issues/new?template=bug_report.yml)
* [Browse the source code](https://github.com/NASA-PDS/validate/)

For installation help, usage questions, or other feedback, contact us at [pds_operator@jpl.nasa.gov](mailto:pds_operator@jpl.nasa.gov).
