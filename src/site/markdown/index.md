# About Validate Tool

The Validate Tool project contains software for validating PDS4 product labels and product data. The associated specific schema for the product label specifies syntactic and semantic constraints. The product label itself specifies the constraints for the data.

## Tool Overview

The Validate Tool currently supports the following features:

* Validation against a Schema and Schematron through the following means:
  - Supplied with the tool
  - Specified by the user on the command-line
  - Specified in the label
  - Specified in an XML Catalog file

* Validation against file references in the product label:
  - Check that files referenced do exist
  - Check casing of file references to see if they match their physical files on the file system
  - Checksum validation against the actual checksum and a Manifest file (if supplied)

* Validation at the Bundle/Collection level, including, but not limited to:
  - Referential integrity
  - Names of files and directories adhere to the PDS4 Standards

Additionally, the tool performs data content validation of tables and arrays. The features that the tool supports with regards to table content validation are:

* For all tables, field values are checked for the following:
  - Values match their defined data type
  - Values are within the defined minimum and maximum values of the field, if specified

* Specifically for binary tables, fields are checked for the following:
  - Fields that are packed match the defined number of bit-fields
  - Bit-field values match their defined data type

* Specifically for character and delimited tables, records are checked for the following:
  - Records in the data file end with a carriage return and line feed
  - The length of a record does not exceed the defined length (or defined maximum length in the case of delimited tables)
  - Number of records match the defined number in the label
  - Values match their defined field format, if specified

With respect to array content validation, the tool checks the following:

* The size of the array object is equal to the element size times the product of the sizes of all axes
* Array elements have values which conform to their data type
* Verify that the elements match the object statistics defined within their associated label, if they exist

## Support

**Found a bug?** Or **want a new feature?** We would love your feedback and contributions. Here are some links to our public Github repository for source code and submitting issues:

* Submit for a New Feature: [https://github.com/NASA-PDS/validate/issues/new?template=feature_request.yml](https://github.com/NASA-PDS/validate/issues/new?template=feature_request.yml)
* Submit a Bug: [https://github.com/NASA-PDS/validate/issues/new?template=bug_report.yml](https://github.com/NASA-PDS/validate/issues/new?template=bug_report.yml)
* Source Code: [https://github.com/NASA-PDS/validate/](https://github.com/NASA-PDS/validate/)

Need help with installation or operation of the software, or any other additional feedback, please contact us at [pds_operator@jpl.nasa.gov](mailto:pds_operator@jpl.nasa.gov).
