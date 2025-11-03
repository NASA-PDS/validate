Title: Operate

## Operation
This document describes how to operate the Validate Tool. The following topics can be found in this document:

<!-- MarkdownTOC autolink="true" -->

- [Quick Start](#quick-start)
    - [Validating a Product](#validating-a-product)
    - [Validating a Bundle](#validating-a-bundle)
    - [Validating References with PDS Registry \(NEW\)](#validating-references-with-pds-registry-new)
- [Command-Line Options](#command-line-options)
    - [Quick Reference: Most Common Flags](#quick-reference-most-common-flags)
    - [Full Command-Line Options](#full-command-line-options)
- [Advanced Usage](#advanced-usage)
    - [Tool Execution](#tool-execution)
        - [Validating a Target Directory](#validating-a-target-directory)
        - [Validating Against User-Specified Schemas and Schematrons](#validating-against-user-specified-schemas-and-schematrons)
        - [Validating Against User-Specified XML Catalogs](#validating-against-user-specified-xml-catalogs)
        - [Validating Against Label Specified Schemas and Schematrons](#validating-against-label-specified-schemas-and-schematrons)
        - [Ignoring Sub-Directories During Validation](#ignoring-sub-directories-during-validation)
        - [Changing Tool Behaviors With The Configuration File](#changing-tool-behaviors-with-the-configuration-file)
    - [Specifying Targets](#specifying-targets)
    - [Validation Rules](#validation-rules)
        - [pds4.label](#pds4-label)
        - [pds4.bundle](#pds4-bundle)
        - [pds4.folder](#pds4-folder)
        - [pds4.collection](#pds4-collection)
        - [pds3.volume](#pds3-volume)
    - [Referential Integrity Checking](#referential-integrity-checking)
    - [Improve Performance](#improve-performance)
        - [Batching](#batching)   
        - [Referential Integrity Checking Only](#referential-integrity-checking-only)
        - [Specify Additional File Paths for Accumulating Bundles](#specify-additional-file-paths-for-accumulating-bundles)
        - [Example of using multiple-flags to improve performance](#example-of-using-multiple-flags-to-improve-performance)
        - [Using validate-bundle parallelization](#using-validate-bundle-parallelization)
    - [Spot Checking Data](#spot-checking-data)
        - [Spot Checking Every Nth Record of a Product](#spot-checking-every-nth-record-of-a-product)
        - [Spot Checking Every Nth Product](#spot-checking-every-nth-product)
    - [Checksum Manifest File Validation](#checksum-manifest-file-validation)
    - [Using an XML Catalog](#using-an-xml-catalog)
        - [Mapping Label-Defined Schemas and Schematrons to Local Copies](#mapping-label-defined-schemas-and-schematrons-to-local-copies)
        - [Mapping Namespaces to Local Copies of Schemas](#mapping-namespaces-to-local-copies-of-schemas)
        - [Mapping Many Schemas At Once](#mapping-many-schemas-at-once)
        - [Passing References to Another Catalog](#passing-references-to-another-catalog)
    - [Using a Configuration File](#using-a-configuration-file)
    - [Passing in Multiple Schemas](#passing-in-multiple-schemas)
    - [Context Product Reference Validation](#context-product-reference-validation)
- [Understanding Your Results](#understanding-your-results)
    - [Exit Codes](#exit-codes)
    - [Interpreting the Summary](#interpreting-the-summary)
    - [What Does "PASS" Mean?](#what-does-pass-mean)
- [Report Format](#report-format)
    - [Choosing a Report Format](#choosing-a-report-format)
    - [Full Report](#full-report)
    - [XML Report](#xml-report)
    - [JSON Report](#json-report)
- [Recommended Workflows](#recommended-workflows)
    - [During Development (Iterative)](#during-development-iterative)
    - [Pre-Release Validation (Thorough)](#pre-release-validation-thorough)
    - [Release Validation (Final Check)](#release-validation-final-check)
    - [Incremental Validation (Large Bundles)](#incremental-validation-large-bundles)
    - [CI/CD Integration](#cicd-integration)
- [Common Errors](#common-errors)
- [Performance](#performance)
    - [Metrics](#metrics)
    - [Maximize Performance](#maximize-performance)

<!-- /MarkdownTOC -->


Note: The command-line examples in this section have been broken into multiple lines for readability. The commands should be reassembled into a single line prior to execution.

## Quick Start
This section is intended to give a quick and easy way to run the Validate Tool. For a more detailed explanation on other ways to run the tool, go to the [Advanced Usage](#advanced-usage) section.

### Validating a Product

The command below shows the recommended way to validate a single product:

```
% validate --target product.xml

PDS Validate Tool Report

Configuration:
   Version                       3.8.0-SNAPSHOT
   ...

Product Level Validation Results

  PASS: file:/path/to/product.xml

Summary:
  0 error(s)
  0 warning(s)

End of Report
```

By default, validate prints results to the console. Use `--report-file filename.txt` to write results to a file instead:

```
% validate --report-file validate-report.txt --target product.xml
```

This validates the given product against the latest core schema and schematron packaged with the tool.

### Validating a Bundle

The command below shows the recommended way to validate a bundle:

```
% validate --rule pds4.bundle --checksum-manifest checksum-manifest.txt --report-file validate-report.txt --target $HOME/pds/bundle
```
The _--rule_ flag indicates to the tool to apply bundle validation rules to the target bundle. This means that validation at the bundle level will be performed, which includes referential integrity checking among other things. Please see the [Validation Rules](#validation-rules) section for more details. The _--checksum-manifest_ flag performs additional checksum validation.

### Validating References with PDS Registry (NEW)

**Prerequisites:**
1. User has authorized access to PDS Registry
2. Data has been harvested into the PDS Registry
3. Validate is running on machine with IP address within whitelisted subnets for Registry access.

The command below shows the recommended way to validate all product references for a product and any child products:

```
% validate-refs --auth-opensearch  /path/to/harvest-config.xml --threads 5 urn:nasa:pds:my-bundle::1.0

16:05:22,982  WARN main CommandLineInterface:process:65 - Using OpenSearch Registry to check references.
16:05:24,539 ERROR Thread-0 Reference Integrity:run:52 - In the search the lidvid 'urn:nasa:pds:my-bundle::1.0' references 'urn:nasa:pds:context:instrument:BAD' that is missing in the database.
```

This utility uses the PDS Registry to check all references from a particular a product.

If given a bundle LIDVID, it will validate the references for the bundle, it's child collections, and their child products.

If given a collection LIDVID, it will validate the references for the collection, and all it's child products.

Use your harvest configuration file as input to this tool in order to utilize the same authentication as Harvest.

## Command-Line Options

### Quick Reference: Most Common Flags

| Flag | Description | Example |
|------|-------------|---------|
| `--target` or `-t` | File or directory to validate | `--target myproduct.xml` |
| `--rule` or `-R` | Validation rule to apply | `--rule pds4.bundle` |
| `--report-file` or `-r` | Output file for report | `--report-file results.txt` |
| `--verbose` or `-v` | Set severity level (1=INFO, 2=WARNING, 3=ERROR) | `--verbose 1` |
| `--skip-content-validation` | Skip data content checks (faster) | `--skip-content-validation` |
| `--skip-context-validation` | Skip context product reference checks | `--skip-context-validation` |
| `--config` or `-c` | Use configuration file | `--config myconfig.txt` |
| `--schematron` or `-S` | Specify custom schematron | `--schematron custom.sch` |
| `--schema` or `-x` | Specify custom schema | `--schema custom.xsd` |

### Full Command-Line Options

To see all command-line options, run the following command for both the `validate` and `validate-refs` utilities:

```
% validate -h

% validate-refs -h
```

## Advanced Usage
This section describes more advanced ways to run the tool, as well as its behaviors and caveats.

### Tool Execution

This section demonstrates some of the ways that the tool can be executed using the command-line option flags:

- Validating a Target Directory

- Validating Against User-Specified Schemas

- Validating Against User-Specified XML Catalogs

- Validating Against User-Specified Schematron Files

- Validating Against Label Specified Schemas and Schematrons

- Validating Against an Older Version of the PDS4 Data Model

- Ignoring Sub-Directories During Validation

- Changing Tool Behaviors With The Configuration File

#### Validating a Target Directory

The following command demonstrates the validation of a target directory against the core PDS schemas:

```
% validate --target /home/pds/collection
```

#### Validating Against User-Specified Schemas and Schematrons

Specifying XML Schemas and schematrons on the command line will allow the Validate Tool to validate against the user-specified schemas and schematrons instead of those packaged with the tool. The following command demonstrates the validation of a single product label against a user-specified schema and schematron:

```
% validate --target /home/pds/dph_example_archive_VG2PLS/data/ele_mom_tblChar.xml --schema /home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xsd, /home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_DPH_1700.xsd --schematron /home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.sch
```
The following command demonstrates the validation of a set of target files against a set of user-specified schemas:

```
% validate --target producta.xml, productb.xml --schema producta.xsd, productb.xsd
```
#### Validating Against User-Specified XML Catalogs

The following command demonstrates the validation of a single data product against a user-specified XML Catalog:

```
% validate --target product.xml --catalog catalog.xml
```
#### Validating Against Label Specified Schemas and Schematrons

The following command demonstrates forcing the tool to validate against the schemas and schematrons specified in a given label.

```
% validate --target product.xml --force
```

Note that validating with the force flag option versus passing in the label specified schemas using the _-x_ flag may yield different validation results. This is due to how the underlying Xerces library treats these 2 scenarios. Basically, with the force flag behavior, namespaces are resolved by matching the element namespace in the label to one of the namespaces declared in the _schemaLocation_ attribute of the label. When passing in schemas using the _-x_ flag, those user given schemas override any schemas specified in the _schemaLocation_ attribute of the label. They are read in and cached in memory prior to the validation step. Namespaces are then resolved by matching the element namespace to one defined in one of those schemas cached in memory.

#### Ignoring Sub-Directories During Validation

By default, the Validate Tool will recursively traverse a target directory during validation. The _local_ flag option is used to tell the Validate Tool to not perform recursion. The following command demonstrates the validation of a target directory without directory recursion:

```
% validate --target /home/pds/collection --local
```
#### Changing Tool Behaviors With The Configuration File

A configuration file can be passed into the command-line to change the default behaviors of the tool and to also provide users a way to perform validation with a single flag. For more details on how to setup the configuration file, see the [Using a Configuration File](#using-a-configuration-file) section.

The following command demonstrates performing validation using a configuration file:

```
% validate --config config.txt
```

### Specifying Targets

Targets are validated in the order in which they are specified on the command-line. They can be specified implicitly and explicitly.

To specify targets implicitly, it is best to specify them first on the command-line before any other command-line option flags. The following command demonstrates the validation of an implicitly defined, single target product label:

```
% validate --target product.xml
```
The following command demonstrates the validation of implicitly defined, multiple targets:

```
% validate --target product.xml, /home/pds/collection
```
Note: Implicit targets should not be specified after option flags that allow multiple arguments (see example below). Unexpected results can occur.

```
% validate --schema product.xsd --target product.xml
```
In this example, the Validate Tool will inadvertently treat the implicit target, _product.xml_, as a schema file.

Targets can be specified both implicitly and explicitly at the same time. Targets specified implicitly are validated first, followed by those that are specified explicitly with the target flag.

The following command demonstrates the validation of multiple product labels, specified both implicitly and explicitly:

```
% validate producta.xml, productb.xml --target productc.xml, /home/pds/collection
```
In this example, _producta.xml_ and _productb.xml_ will get validated first, then _productc.xml_ and the product labels in _/home/pds/collection_ will get validated next.

In each scenario above, the target product(s) were the equivalent of observational or document products. The data model also consists of bundle and collection products, which in turn reference other products. When the Validate Tool encounters one of these products, it traverses the inventory associated with that product and validates each product referenced as well as the target product.

### Validation Rules

The Validate Tool provides the capability of doing various additional checks beyond the usual PDS4 label validation. This is done through the _-R, --rule_ flag option.

**Default Behavior:** If the _-R_ flag is not specified, the tool uses _pds4.label_ for file inputs and _pds4.folder_ for directory inputs.

#### Validation Rules Comparison

| Rule | When to Use | Key Checks | Referential Integrity |
|------|-------------|------------|----------------------|
| **pds4.label** | Single product validation | Schema, schematron, file refs, checksums | No |
| **pds4.folder** | Directory of products | Same as pds4.label for each file | No |
| **pds4.collection** | Collection product with inventory | File naming, all files referenced, member integrity | Yes - within collection |
| **pds4.bundle** | Complete bundle validation | Root directory rules, all collections valid | Yes - entire bundle |
| **pds3.volume** | PDS3 volume validation | PDS3-specific checks | N/A |

**Important:** Bundle and collection validation can be slow for large datasets. See [Improve Performance](#improve-performance) for optimization strategies.

The command-line run examples below reference the example PDS4 Archive Bundle that can be found on the [PDS4 web site](https://pds.jpl.nasa.gov/pds4/doc/examples/).

#### pds4.label

The _pds4.label_ validation rule will:

- Check that a product label conforms to a given schema and schematron.

- Check that files referenced in a product label exist.

- Check that the file references in a product label and the file on the file system match case.

- Check that file reference checksums in a product label matches their actual checksums, if present.

- Perform checksum validation against a Checksum Manifest file, if supplied.

The following example demonstrates validating a product label using the default _pds4.label_ validation rule:

```

%> validate --target /home/pds/dph_example_archive_VG2PLS/data/ele_mom_tblChar.xml
```

#### pds4.bundle

The _pds4.bundle_ validation rule applies the _pds4.collection_ validation rule on each collection found within a target collection. In addition, it will:

- Check that files and directories at the root of the target bundle are valid as defined in section 2B.2.2.1 of the PDS4 Standards Reference.

- Perform referential integrity checking among the target bundle and its members.

- Verify that the LID of the bundle is used as the base of the LIDs of collections that are members of the bundle.

The following example demonstrates running the tool using the PDS4 Bundle validation rules on a target bundle:

```
%> validate --target /home/pds/dph_example_archive_VG2PLS --rule pds4.bundle
```

Note that the _-e_ flag option to filter on specific files is ignored when validating under the _pds4.bundle_ rules.

#### pds4.folder

The _pds4.folder_ validation rule allows the tool to do a file-by-file validation using the _pds4.label_ rule on each file found in the target directory.

The following example demonstrates performing a file-by-file validation on a target directory:

```
%> validate --target /home/pds/dph_example_archive_VG2PLS
```

#### pds4.collection

The _pds4.collection_ validation rule applies the _pds4.label_ validation rule on each product label found within a target collection. In addition, it will:

- Check that file names follow the file-naming rules as defined in section 6C.1.1 of the PDS4 Standards Reference.

- Check that file names do not match the prohibited file names as defined in section 6C.1.2 of the PDS4 Standards Reference.

- Check that file names do not contain the prohibited base names as defined in section 6C.1.4 of the PDS4 Standards Reference.

- Check that directory names follow directory-naming rules as defined in section 6C.2.1 of the PDS4 Standards Reference.

- Check that directory names do not match prohibited directory names as defined in section 6C.2.3 of the PDS4 Standards Reference.

- Check that all files in the target collection are referenced by some label.

- Perform referential integrity checking among the target collection and its members.

- Verify that the LID of the collection is used as the base of the LIDs of products that are members of the collection.

The following example demonstrates applying a PDS4 Collection validation rule on a target collection:

```
%> validate --target /home/pds/dph_example_archive_VG2PLS/data --rule pds4.collection
```

Note that the _-e_ flag option to filter on specific files is ignored when validating under the _pds4.collection_ rules.

#### pds3.volume

The _pds3.volume_ validation rule allows the Validate Tool to perform PDS3 volume validation on a target directory. Currently, only local targets are supported for this validation rule.

The following example demonstrates running the tool against a PDS3 volume:

```
%> validate --target / home/pds/VG2-J-PLS-5-SUMM-ELE-MOM-96.0SEC-V1.0 --rule pds3.volume
```

### Referential Integrity Checking

The Validate Tool performs referential integrity checking within a given target directory when the _pds4.collection_ or _pds4.bundle_ validation rules are applied to a validation run (via the _-R, --rule_ flag option). Assuming that the target directory points to a full PDS4 bundle, the following referential integrity checks are made as the tool validates each product:

For Bundle products,

- Verify that each Collection member of a Bundle is present within the given target.

- Verify that each Collection member of a Bundle is referenced only once within the given target.

For Collection products,

- Verify that each Collection product is referenced by a Bundle product within the given target.

- Verify that each Product member of a Collection is present within the given target.

- Verify that each Product member of a Collection is referenced only once within the given target.

All other products are assumed to be members of a Collection. So for these, the tool will verify that each product is referenced by a Collection product within the given target.

It is important to note that the tool only performs referential integrity checking on the targets that the tool is given. As an example, it is fine to specify a target directory that contains just a Collection product and its members:

```
% validate ${HOME}/bundle/collection --rule pds4.collection
```
In this case, the tool will not report that the given Collection is not referenced by a Bundle product.

Another thing to note is that the report will record verified references under the _INFO_ message severity level. So in order to see these messages, run the tool with the verbose level set to _1_ to produce a report with the INFO messages. The following command demonstrates running the tool with the severity level set to _INFO_ and above:

```
% validate ${HOME}/bundle1 --rule pds4.bundle -v1
```
Below is a snippet of an example report of a validation run using the pds4.bundle rules and the severity level set to _INFO_ and above:

```
...
PDS4 Bundle Level Validation Results
PASS: file:/home/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml
INFO  The member 'urn:nasa:pds:example.dph.sample_archive_bundle:browse:ele_mom::1.0' is identified in the following product: \
file:/home/pds4/dph_example_archive_VG2PLS/browse/ele_mom_browse.xml
INFO  Identifier 'urn:nasa:pds:example.dph.sample_archive_bundle:browse::1.0' is a member of \
'file:/home/pds4/dph_example_archive_VG2PLS/bundle.xml'
PASS: file:/home/pds4/dph_example_archive_VG2PLS/context/Collection_context.xml
INFO  The member 'urn:nasa:pds:context:instrument_host:spacecraft.vg2::1.0' is identified in the following product: \
file:/home/pds4/dph_example_archive_VG2PLS/context/PDS4_host_VG2_1.0.xml
INFO  The member 'urn:nasa:pds:context:instrument:pls.vg2::1.0' is identified in the following product: \
file:/home/pds4/dph_example_archive_VG2PLS/context/PDS4_inst_PLS_VG2_1.0.xml
INFO  The member 'urn:nasa:pds:context:investigation:mission.voyager::1.0' is identified in the following product: \
file:/home/pds4/dph_example_archive_VG2PLS/context/PDS4_mission_VOYAGER_1.0.xml
INFO  The member 'urn:nasa:pds:context:target:planet.jupiter::1.0' is identified in the following product: \
file:/home/pds4/dph_example_archive_VG2PLS/context/PDS4_target_JUPITER_1.0.xml
INFO  Identifier 'urn:nasa:pds:example.dph.sample_archive_bundle:context::1.0' is a member of \
'file:/home/pds4/dph_example_archive_VG2PLS/bundle.xml'
...
```
In the case that 2 targets are specified on the command line, the tool will treat each target as a separate entity when performing referential integrity checking. As an example, the following command demonstrates performing referential integrity on 2 different bundles:

```
% validate --target ${HOME}/bundle1, ${HOME}/bundle2 --rule pds4.bundle
```
The tool will perform referential integrity checking within the products located in the _$\{HOME\}/bundle1_ target, then will perform referential integrity checking within the products located in the _$\{HOME\}/bundle2_ target. In other words, the tool will not cross over to the _$\{HOME\}/bundle2_ directory to find LID/LIDVID references of a product in the _$\{HOME\}/bundle1_ directory.

### Improve Performance

Validate is a very memory intensive applications that requires a significant amount of resources and time in order to complete validation on large data sets.

There are a few ways you can enhance the way validate executes PDS4 Bundle validation using several options. There are several flags added to validate so you can split out various parts
and run multiple Validate executions to speed up the processing. However, to ensure proper validity of the archive, several of these flags should

only be used during development or staging of data prior to release.

#### Batching

For both memory and performance improvements, it is highly recommended that you batch large bundles, both in terms of data volume and number of products, into batches.

You can batch either by directory or by using target manifests.

```
# Point at several targets with different validate runs for parallelizing content validation
validate --rule pds4.label --target my_bundle/document/*.xml
validate --rule pds4.label --target my_bundle/release1/*.xml
validate --rule pds4.label --target my_bundle/release2/*.xml
validate --rule pds4.label --target my_bundle/release3/*.xml

# Or you could do the same thing with target manifests
validate --rule pds4.label --target-manifest documents.txt
validate --rule pds4.label --target-manifest release1_data.txt
validate --rule pds4.label --target-manifest release2_data.txt
validate --rule pds4.label --target-manifest release3_data.txt
```

#### Referential Integrity Checking Only

The _--skip-product-validation_ flag when used with the _pds4.bundle_ rule will only perform referential integrity checking

for the bundle, and ignore validation of the individual products themselves.

```
# Execute bundle validation but only check bundle/collection validity and ignore the products
% validate --skip-product-validation --rule pds4.bundle --target my_bundle
```

#### Specify Additional File Paths for Accumulating Bundles

When staging an accumulating bundle for release, there may be a case where you want to perform validation on the entire bundle

with the new data to-be-released, as well as the old data that is in the archive. The _--alternate_file_paths_ flag can be

used to specify additional file paths you would like validate to check for files in.

NOTE: This should be used for staging and development purposes only, and validate should be re-run one the data once it has been released
and is in line with the other bundle data.

```
% validate --skip-product-validation --rule pds4.bundle --target path/to/staged/bundle_v2 --alternate_file_paths path/to/online/bundle_v1
```

#### Example of using multiple-flags to improve performance

Here is one example of how to

split up execution. There are other permutations you could do to improve performance, but this is one example:

```
my_bundle/
├── bundle.xml
├── data
│   ├── data_collection.tab
│   ├── data_collection.xml
│   ├── release1
│   │   ├── file1.tab
│   │   ├── file1.xml
│   │   ├── file2.tab
│   │   └── file2.xml
│   ├── release2
│   │   ├── file3.tab
│   │   ├── file3.xml
│   │   ├── file4.tab
│   │   └── file4.xml
│   └── release3
│       ├── file5.tab
│       ├── file5.xml
│       ├── file6.tab
│       └── file6.xml
└── document
├── document_collection.tab
└── document_collection.xml
```
Here is how you could execute multiple validates to split out the work. **Note: Always make sure if you split out execution

that you execute Bundle Validation (pds4.bundle rule) and content validation.**

```
# Execute bundle validation but only check bundle/collection validity and ignore the products
validate --skip-product-validation --rule pds4.bundle --target my_bundle

# Make sure the labels are valid upon delivery but skip content validation until later since it takes longer
validate --skip-content-validation --rule pds4.label --target my_bundle

# Point at several targets with different validate runs for parallelizing content validation
validate --rule pds4.label --target my_bundle/document/*.xml
validate --rule pds4.label --target my_bundle/release1/*.xml
validate --rule pds4.label --target my_bundle/release2/*.xml
validate --rule pds4.label --target my_bundle/release3/*.xml

# Or you could do the same thing with target manifests
validate --rule pds4.label --target-manifest documents.txt
validate --rule pds4.label --target-manifest release1_data.txt
validate --rule pds4.label --target-manifest release2_data.txt
validate --rule pds4.label --target-manifest release3_data.txt

```

#### Using validate-bundle parallelization

To facilitate the splitting up, and running in parallel the data, one may use the **validate-bundle** script in the [src/main/resources/bin](https://github.com/NASA-PDS/validate/tree/master/src/main/resources/bin) directory.
\
This script has two prerequisites:

- the **validate** program be on your system path
- the **[GNU Parallel](https://www.gnu.org/software/parallel/)** program be installed, and be on your system path

The usage looks like this:

```

./validate-bundle --target /path/to/bundle/bundle.xml
```
By default, this program will split your input files up into N groups.  By default N is half the number of available cores on your machine.
There are some circumstances where overriding this value may improve performance.  If you wish to do so, you may use the "-n" or "--num-groups" argument.

```

./validate-bundle --target /dir/containing/products --num-groups 3
```
When this runs, it will break your inputs up into groups, and run validate on them in parallel.
Here's an example showing a run using the default number of cores:
```
$ ./validate-bundle --target ~/dph_example_archive_VG2PLS/bundle.xml
validate binary is available (/Users/jpadams/Documents/proj/pds/pdsen/workspace/validate/validate-1.20.0-SNAPSHOT/bin/validate) [OK]
GNU parallel binary is available (/usr/local/bin/parallel) [OK]
Directory to process is : /Users/jpadams/dph_example_archive_VG2PLS
Number of effective cores on this system: 8
Half of effective cores on this system  : 4 (usually better than running all cores)
Splitting up into groups, based on number of cores this machine has...
Total number of products found to validate: 18
Cleaning up files from previous runs...
Splitting up inputs into 4 groups...
(5 files per group)
+--------------------+----------------+
| Group  |   # in Group   |
+--------------------+----------------+
| ./validate_set_aa  |   5
| ./validate_set_ac  |   5
| ./validate_set_ad  |   3
| ./validate_set_ab  |   5
+--------------------+-----------------
Running all 4 groups in parallel now...
validate --rule pds4.label --target-manifest ./validate_set_aa
validate --rule pds4.label --target-manifest ./validate_set_ac
validate --rule pds4.label --target-manifest ./validate_set_ad
validate --rule pds4.label --target-manifest ./validate_set_ab
Performing XML and Content validation...
Each validate command will output to an individual log.
real	0m13.615s
user	1m25.390s
sys	0m4.082s
Performing Referential Integrity validation...
Completed execution in 5365 ms

>>>>>>>>>>>>>>> ./validate_set_ad.log :
Summary:
2281 error(s)
0 warning(s)
Message Types:
2278   error.table.field_value_data_type_mismatch
2error.label.context_ref_not_found
1error.table.missing_CRLF
End of Report
>>>>>>>>>>>>>>>
>>>>>>>>>>>>>>> ./validate_set_aa.log :
Summary:
6 error(s)
1 warning(s)
Message Types:
2error.label.context_ref_not_found
2error.table.fields_mismatch
2error.table.missing_CRLF
1warning.label.schematron
End of Report
>>>>>>>>>>>>>>>
>>>>>>>>>>>>>>> ./validate_set_ac.log :
Summary:
12 error(s)
0 warning(s)
Message Types:
6error.label.context_ref_not_found
4error.table.missing_CRLF
2error.table.fields_mismatch
End of Report
>>>>>>>>>>>>>>>
>>>>>>>>>>>>>>> ./validate_set_ab.log :
Summary:
6 error(s)
0 warning(s)
Message Types:
6error.label.context_ref_not_found
End of Report
>>>>>>>>>>>>>>>
>>>>>>>>>>>>>>> ./validate_referential.log :
Summary:
1 error(s)
2 warning(s)
Message Types:
1error.file.name_has_invalid_characters
1warning.label.schematron
1warning.integrity.unreferenced_member
End of Report
>>>>>>>>>>>>>>>
Validation complete. See validate_20200217_151431/validate_summary.log for a summary of results.
All Validate Tool execution reports can be found in validate_20200217_151431.

```

### Spot Checking Data

By default, the tool performs a byte by byte validation of the data content. To improve performance on large data sets upon ingestion into the archive,
you can validate a subset of the content within the archive when a data provider pipeline has been verified. You can do this through spot check validation
either by every Nth record of a product, or every Nth product.

#### Spot Checking Every Nth Record of a Product

Validates every Nth record within a given product.

```
% validate --target product.xml --spot-check-data 100
```

In the above example, assuming the data product is a table, the tool will perform content validation on every 100th record in the table. If the data content is an array, the tool will perform content validation on every 100th line in the array.

#### Spot Checking Every Nth Product

Validates every Nth product within a given target bundle or directory.

```
% validate --target product.xml --everyN 10
```

In the above example, assuming the target contains 100 products, the tool will perform label validation on all products, but content validation on only every 10th product.

### Checksum Manifest File Validation

When a Checksum Manifest file is passed into the tool, the generated checksum value of a file is compared against the supplied value in the Manifest file.

The dph example archive bundle, which can be downloaded from the [PDS web site](https://pds.nasa.gov/pds4/doc/examples/), contains an example of how a Checksum Manifest file could look like:

```
97a7569daeaacf57b5abceca57bdca43  .\Product_Bundle.xml
e7da7276dd553f30496b868a2007bf5b  .\README.TXT
84f68c7706f379c401e2f8e08a82edea  .\browse\Collection_browse.xml
bcb852a6a9292e304a93668e6cc2068c  .\browse\Collection_browse_inventory.tab
3ff61c98cbed11e99690f76b5f6831b0  .\browse\ELE_MOM.PDF
f2e5969b0f1a0f54e530e416b7f5d54a  .\browse\ele_mom_browse.xml
76d6463510bc48233d65b46d08087ef8  .\context\Collection_context.xml
b8f5301ad7c868c76f0e627f0da19aed  .\context\Collection_context_inventory.tab
c226a6a0867e003696a752b8c24e56f3  .\context\PDS4_host_VG2_1.0.xml
...
```

It is important to note that the tool supports either absolute or relative file references specified in a Checksum Manifest file. In the event that the file references are relative paths, the tool assumes that the target root is the base path of these file references. The _Parameter_ section of the Validate Tool Report will indicate the base path that the tool uses to resolve relative file references in a Manifest file. This is found under the setting _Manifest File Base Path_.

The following command demonstrates performing Checksum Manifest file validation against the dph example archive bundle using its manifest file _bundle_checksums.txt_:

```
% validate --target /home/pds4/dph_example_archive_VG2PLS -M /home/pds4/dph_example_archive_VG2PLS/bundle_checksums.txt
```

In the example above, the base path of the file references in the _bundle_checksums.txt_ file is assumed to be _/home/pds4/dph_example_archive_VG2PLS_.

Another use case is doing checksum manifest file validation on a sub-directory of a bundle. Using the dph example archive bundle example, we would need to tell the tool to use a different base than the target root since the file references in the _bundle_checksums.txt_ file would not match up with the target sub-directory. This can be done using the _-B_ flag option. The following command demonstrates performing Checksum Manifest file validation on a a sub-directory of the dph example archive bundle:

```
% validate --target /home/pds4/dph_example_archive_VG2PLS/browse -M /home/pds4/dph_example_archive_VG2PLS/bundle_checksums.txt -B /home/pds4/dph_example_archive_VG2PLS
```

In the command line example above, the base path is set to _/home/pds4/dph_example_archive_VG2PLS_.

In the event that multiple targets are specified when performing checksum manifest file validation, the _-B_ flag option must be specified. Continuing with the dph example archive bundle example, the following command demonstrates Checksum Manifest file validation on multiple sub-directories of the bundle:

```
% validate --target /home/pds4/dph_example_archive_VG2PLS/browse, /home/pds4/dph_example_archive_VG2PLS/context \

-M /home/pds4/dph_example_archive_VG2PLS/bundle_checksums.txt \
-B /home/pds4/dph_example_archive_VG2PLS
```

### Using an XML Catalog

An XML Catalog allows the user to describe a mapping between external entity references in their products and locally-available XML Schema and Schematron documents. This section details some of the ways that the Catalog file can be set up that should support most use-case scenarios. The examples can be used to validate the _ele_mom_tblChar.xml_ product label that can be found in the Data Provider's Handbook (DPH) example bundle, which is made available at the [PDS website](https://pds.nasa.gov/pds4/doc/examples/).

#### Mapping Label-Defined Schemas and Schematrons to Local Copies

The following is an example of an XML Catalog that maps schemas and schematrons defined in the product label to local copies:

```
<?xml version="1.0" encoding="UTF-8"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog">
<group xml:base="file:///home/pds/dph_example_archive_VG2PLS/xml_schema/">
<uri name="https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1700.xsd"
uri="PDS4_PDS_1700.xsd"/>
<uri name="https://pds.nasa.gov/pds4/dph/v1/PDS4_DPH_1700.xsd"
uri="PDS4_DPH_1700.xsd"/>
<uri name="https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1700.sch"
uri="PDS4_PDS_1700.sch"/>
</group>
</catalog>
```
The _xml:base_ attribute sets the top-level directory to where the local copies of the schemas and schematrons can be found. So, in this example, the _https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1700.xsd_ schema maps to the local schema at _file:///home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xsd_. Note that the _xml:base_ attribute value must end in the backslash '_/_' character in order for the tool to properly resolve the URI references set in the Catalog file.

#### Mapping Namespaces to Local Copies of Schemas

The following is an example of an XML Catalog that maps namespaces defined in the product label to local copies of the Schemas:

```
<?xml version="1.0" encoding="UTF-8"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog">
<group xml:base="file:///home/pds/dph_example_archive_VG2PLS/xml_schema/">
<uri name="http://pds.nasa.gov/pds4/pds/v1"
uri="PDS4_PDS_1700.xsd"/>
<uri name="http://pds.nasa.gov/pds4/dph/v1"
uri="PDS4_DPH_1700.xsd"/>
<uri name="https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1700.sch"
uri="PDS4_PDS_1700.sch"/>
</group>
</catalog>
```

In this example, the namespace _http://pds.nasa.gov/pds4/pds/v1_ maps to the schema _file:///home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xsd_.

#### Mapping Many Schemas At Once

The following is an example of an XML Catalog that maps many Schemas and Schematrons at once based on a common initial substring in those URIs:

```
<?xml version="1.0" encoding="UTF-8"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog">
<rewriteURI uriStartString="https://pds.nasa.gov/pds4/pds/v1"

rewritePrefix="file:///home/pds/dph_example_archive_VG2PLS/xml_schema"/>
<rewriteURI uriStartString="https://pds.nasa.gov/pds4/dph/v1"

rewritePrefix="file:///home/pds/dph_example_archive_VG2PLS/xml_schema"/>
</catalog>
```

The _uriStartString_ represents the prefix to look for in the product label. The _rewritePrefix_ is the replacement string in order to resolve the URI reference. So in this example, the first entry maps the _https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1700.xsd_ schema in the product label to _file:///home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xsd_ and maps the _https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1700.sch_ schematron to _file:///home/pds/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.sch_.

#### Passing References to Another Catalog

The following example of an XML Catalog file passes off the DPH URI reference off to another XML Catalog named _XMLCatalog_dph.xml_ for resolution:

```
<?xml version="1.0" encoding="UTF-8"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog">
<delegateURI uriStartString="https://pds.nasa.gov/pds4/dph/v1"

catalog="file:///home/pds/catalog/dph/XMLCatalog_dph.xml"/>
<rewriteURI uriStartString="https://pds.nasa.gov/pds4/pds/v1"

rewritePrefix="file:///home/pds/dph_example_archive_VG2PLS/xml_schema"/>

</catalog>
```

The _XMLCatalog_dph.xml_ would contain the following:

```
<?xml version="1.0" encoding="UTF-8"?>
<catalog xmlns="urn:oasis:names:tc:entity:xmlns:xml:catalog">
<rewriteURI uriStartString="https://pds.nasa.gov/pds4/dph/v1"

rewritePrefix="file:///home/pds/dph_example_archive_VG2PLS/xml_schema"/>
</catalog>
```

In the first example, the _uriStartString_ value in the _delegateURI_ element matches the _https://pds.nasa.gov/pds4/dph/v1/PDS4_DPH_1700.xsd_ DPH schema in the product label, so the tool passes this off to the _XMLCatalog_dph.xml_ for resolution.

### Using a Configuration File

A configuration file is an alternative way to set the different behaviors of the tool instead of the command-line option flags. It consists of a text file made up of keyword/value pairs. The configuration file follows the syntax of the stream parsed by the Java Properties.load(java.io.InputStream) method.

Some of the important syntax rules are as follows:

- Blank lines and lines which begin with the hash character "#" are ignored.

- Values may be separated on different lines if a backslash is placed at the end of the line that continues below.

- Escape sequences for special characters like a line feed, a tabulation or a unicode character, are allowed in the values and are specified in the same notation as those used in Java strings (e.g. \\n, \\t, \\r).

Since backslashes (\\) have special meanings in a configuration file, keyword values that contain this character will not be interpreted properly by the Validate Tool even if it is surrounded by quotes. A common example would be a Windows path name (e.g. c:\\pds\\collection). Use the forward slash character instead (c:/pds/collection) or escape the backslash character (c:\\\\pds\\\\collection).

Note: Any flag specified on the command-line takes precedence over any equivalent settings placed in the configuration file.

See the [Config Key](https://github.com/NASA-PDS/validate/blob/8287091b9fa5c375f0f9d0b6ba8e82335dc3f902/src/main/java/gov/nasa/pds/validate/commandline/options/ConfigKey.java#L46C43-L46C61) class for available configuration options that match to the command-line option.


The following example demonstrates how to set a configuration file:

```
# This is a Validate Tool configuration file
validate.target = ./collection
validate.report = report.txt
validate.regexp = "*.xml"
```
This is equivalent to running the tool with the following flags:

```
--target ./collection --label-extension "*.xml" --report-file report.txt
```
The following example demonstrates how to set a configuration file with multiple values for a keyword:

```
# This is a Validate Tool configuration file with multiple values
validate.target = product.xml, ./collection
validate.regexp = "*.xml", "Mars*"
```
This is equivalent to running the tool with the following flags:

```
--target product.xml, ./collection --label-extension "*.xml", "Mars*"
```
The following example demonstrates how to set a configuration file with multiple values that span across multiple lines:

```
# This is a Validate configuration file with multiple values
# that span across multiple lines
validate.target = product.xml, \
./collection
validate.regexp = "*.xml", \
"Mars*"
```
As previously mentioned, any flag options set on the command-line will overwrite settings set in the configuration file. The following example demonstrates how to override a setting in the configuration file.

Suppose the configuration file named _config.txt_ is defined as follows:

```
validate.target = ./collection
validate.regexp = "*.xml"
```
This configuration allows the tool to validate files with a _.xml_ extension in the _collection_ directory. To change the behavior to validate all files instead of just files ending in _.xml_, then specify the _regexp_ flag option on the command-line to overwrite the _validate.regexp_ property:

```
% validate -c config.txt --label-extension "*"
```

### Passing in Multiple Schemas

The Validate Tool allows multiple XML Schemas to be passed in through the command-line via the _-x_ flag option. When passing in multiple schemas, the definitions found in each file are merged internally. In the case where two schemas are passed in and both define the same element under the same namespace, then the definition in the first schema passed in will take precedence over the second schema. As an example, suppose a schema file, _schema1.xsd_, contains the following definition:

```
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
targetNamespace="http://pds.nasa.gov/pds4/pds/v1"
xmlns:pds="http://pds.nasa.gov/pds4/pds/v1"
elementFormDefault="qualified"
attributeFormDefault="unqualified"
version="1.1.0.0">
...
<xs:complexType name="File_Area_Browse">
<xs:annotation>
<xs:documentation> The File Area Browse class describes a file and one or more tagged_data_objects contained within the file. </xs:documentation>
</xs:annotation>
<xs:complexContent>
<xs:extension base="pds:File_Area">
<xs:sequence>
<xs:element name="File" type="pds:File" minOccurs="1" maxOccurs="1"> </xs:element>
<xs:choice minOccurs="1" maxOccurs="unbounded">
<xs:element name="Array_1D" type="pds:Array_1D"> </xs:element>
<xs:element name="Array_2D" type="pds:Array_2D"> </xs:element>
<xs:element name="Array_2D_Image" type="pds:Array_2D_Image"> </xs:element>
<xs:element name="Array_2D_Map" type="pds:Array_2D_Map"> </xs:element>
<xs:element name="Array_2D_Spectrum" type="pds:Array_2D_Spectrum"> </xs:element>
<xs:element name="Array_3D" type="pds:Array_3D"> </xs:element>
<xs:element name="Array_3D_Image" type="pds:Array_3D_Image"> </xs:element>
<xs:element name="Array_3D_Movie" type="pds:Array_3D_Movie"> </xs:element>
<xs:element name="Array_3D_Spectrum" type="pds:Array_3D_Spectrum"> </xs:element>
<xs:element name="Encoded_Header" type="pds:Encoded_Header"> </xs:element>
<xs:element name="Encoded_Image" type="pds:Encoded_Image"> </xs:element>
<xs:element name="Header" type="pds:Header"> </xs:element>
<xs:element name="Stream_Text" type="pds:Stream_Text"> </xs:element>
<xs:element name="Table_Binary" type="pds:Table_Binary"> </xs:element>
<xs:element name="Table_Character" type="pds:Table_Character"> </xs:element>
<xs:element name="Table_Delimited" type="pds:Table_Delimited"> </xs:element>
</xs:choice>
</xs:sequence>
</xs:extension>
</xs:complexContent>
</xs:complexType>
...
```
Suppose the other schema, _schema2.xsd_, contains the same element definition:

```
<xs:schema xmlns:xs="http://www.w3.org/2001/XMLSchema"
targetNamespace="http://pds.nasa.gov/pds4/pds/v1"
xmlns:pds="http://pds.nasa.gov/pds4/pds/v1"
elementFormDefault="qualified"
attributeFormDefault="unqualified"
version="1.0.0.0">
...
<xs:complexType name="File_Area_Browse">
<xs:annotation>
<xs:documentation> The File Area Browse class describes a file and one or more tagged_data_objects contained within the file. </xs:documentation>
</xs:annotation>
<xs:complexContent>
<xs:extension base="pds:File_Area">
<xs:sequence>
<xs:element name="File" type="pds:File" minOccurs="1" maxOccurs="1"> </xs:element>
<xs:choice minOccurs="1" maxOccurs="unbounded">
<xs:element name="Array_2D" type="pds:Array_2D"> </xs:element>
<xs:element name="Array_2D_Image" type="pds:Array_2D_Image"> </xs:element>
<xs:element name="Array_2D_Map" type="pds:Array_2D_Map"> </xs:element>
<xs:element name="Array_2D_Spectrum" type="pds:Array_2D_Spectrum"> </xs:element>
<xs:element name="Array_3D" type="pds:Array_3D"> </xs:element>
<xs:element name="Array_3D_Image" type="pds:Array_3D_Image"> </xs:element>
<xs:element name="Array_3D_Movie" type="pds:Array_3D_Movie"> </xs:element>
<xs:element name="Array_3D_Spectrum" type="pds:Array_3D_Spectrum"> </xs:element>
<xs:element name="Encoded_Header" type="pds:Encoded_Header"> </xs:element>
<xs:element name="Encoded_Image" type="pds:Encoded_Image"> </xs:element>
<xs:element name="Header" type="pds:Header"> </xs:element>
<xs:element name="Stream_Text" type="pds:Stream_Text"> </xs:element>
<xs:element name="Table_Binary" type="pds:Table_Binary"> </xs:element>
<xs:element name="Table_Character" type="pds:Table_Character"> </xs:element>
<xs:element name="Table_Delimited" type="pds:Table_Delimited"> </xs:element>
</xs:choice>
</xs:sequence>
</xs:extension>
</xs:complexContent>
</xs:complexType>
...
```
If the schemas are passed into the Validate Tool as follows:

```
% validate product.xml -x schema1.xsd, schema2.xsd
```
then the _File_Area_Browse_ definition from the _schema1.xsd_ file takes precedence over the _schema2.xsd_ file. If it was passed into the tool in the reverse order, then the _File_Area_Browse_ definition in the _schema2.xsd_ file will take precedence over the one in the _schema1.xsd_ file.

### Context Product Reference Validation

The _resources/_ folder in the Validate Tool Release Package contains a JSON-formatted file (`registered-context-products.json`) that contains a list intended to represent a snapshot of the Context Product LIDVIDs (Logical Identifier/Version Identifier) currently registered at the PDS Engineering Node. This file is read in at execution time so that the tool can validate that Context Products referenced in a product label exist within this supplied list.

**Updating the Registered Context Products List:**

- To use the latest registered context products from the PDS Registry, use the `--latest-json-file` flag when running validate. This will download the current list from the Registry API.
- For development purposes, you can provide your own list of non-registered products using the `--nonregprod-json-file` flag.
- The context products JSON file is stored in the `resources.home` directory (configurable via system property).

## Understanding Your Results

### Exit Codes

The validate tool uses exit codes to indicate validation status, which is essential for automation:

| Exit Code | Meaning | Description |
|-----------|---------|-------------|
| 0 | Success | All validations passed with no errors or warnings above the specified severity level |
| 1 | Failure | One or more validation errors occurred |

**Example for CI/CD:**
```bash
validate --target bundle/ --rule pds4.bundle --report-file results.txt
if [ $? -eq 0 ]; then
    echo "Validation passed!"
else
    echo "Validation failed - see results.txt"
    exit 1
fi
```

### Interpreting the Summary

At the end of each validation report, you'll see a summary section:

```
Summary:
  5 error(s)
  2 warning(s)

  Message Types:
    3   error.table.fields_mismatch
    2   error.label.context_ref_not_found
    1   warning.label.schematron
    1   warning.integrity.unreferenced_member
```

**What this means:**
- **Total counts**: Overall number of errors and warnings
- **Message Types**: Breakdown by error/warning type with counts
- Use these counts to prioritize fixes (e.g., if one error type appears many times, fix the root cause)

### What Does "PASS" Mean?

- **PASS**: Product passed all validation checks at the specified severity level
- **FAIL**: Product has one or more errors at or above the severity level
- **SKIP**: Product validation was skipped (e.g., when using `--skip-product-validation`)

**Note:** A product can show PASS at the product level but FAIL at the bundle level if there are referential integrity issues.

## Report Format
This section describes the contents of the Validate Tool report. The links below detail the validation results of the same run for each format.

The tool can represent a validation report in three different formats: a full, XML, or JSON format. The report style option is used to change the formatting. When this option is not specified on the command-line, the default is to generate a full report.

### Choosing a Report Format

| Format | When to Use | Pros | Cons |
|--------|-------------|------|------|
| **Full (default)** | Human review, debugging | Easy to read, includes all details | Large file size for big datasets |
| **JSON** | Automation, parsing, dashboards | Machine-readable, structured | Harder to read manually |
| **XML** | Integration with XML tools | Structured, schema-based | More verbose than JSON |

Use `--report-style json` or `--report-style xml` to change format.

### Full Report

In a [full](reports/index-full.html) report, the location, severity, message type, and textual description of each detected anomaly is reported. A 'PASS', 'FAIL', or 'SKIP' keyword is displayed next to each file to indicate when a file has passed, failed, or skipped PDS validation, respectively. A summary is printed at the end indicating the total number of warnings and errors, followed by a summary of each of the different message types that were found in the validation run.

### XML Report

In an [XML](reports/index-xml.html) report, the contents are the same as the full report.

### JSON Report

In a [JSON](reports/index-json.html) report, the contents are the same as the full report.

## Recommended Workflows

Different phases of data preparation require different validation approaches. Here are recommended workflows:

### During Development (Iterative)

**Goal:** Quick feedback while developing labels

```bash
# 1. Validate individual products quickly (skip content validation for speed)
validate --skip-content-validation --target myproduct.xml

# 2. Once label structure is correct, validate content
validate --target myproduct.xml

# 3. Validate with context checks disabled if developing new context products
validate --skip-context-validation --target myproduct.xml
```

### Pre-Release Validation (Thorough)

**Goal:** Catch all issues before submitting to PDS

```bash
# 1. Full validation of entire bundle
validate --rule pds4.bundle --target /path/to/bundle/

# 2. If bundle is large, use batching approach (see Performance section)
validate --skip-product-validation --rule pds4.bundle --target /path/to/bundle/
validate --rule pds4.label --target /path/to/bundle/data/*.xml
validate --rule pds4.label --target /path/to/bundle/document/*.xml

# 3. Include checksum validation if you have a manifest
validate --rule pds4.bundle --checksum-manifest bundle_checksums.txt --target /path/to/bundle/
```

### Release Validation (Final Check)

**Goal:** Verify nothing was missed and document validation

```bash
# Full validation with detailed output saved to file
validate --rule pds4.bundle \
         --verbose 1 \
         --report-file validation-report-$(date +%Y%m%d).txt \
         --checksum-manifest bundle_checksums.txt \
         --target /path/to/bundle/

# For automation, capture exit code
validate --rule pds4.bundle --target /path/to/bundle/ --report-file results.txt
echo "Validation exit code: $?"
```

### Incremental Validation (Large Bundles)

**Goal:** Validate only what changed since last release

```bash
# 1. Validate just the new collection
validate --rule pds4.collection --target /path/to/bundle/new_collection/

# 2. Validate bundle-level integrity with alternate paths to old data
validate --skip-product-validation \
         --rule pds4.bundle \
         --alternate_file_paths /path/to/previous/release \
         --target /path/to/new/release/
```

### CI/CD Integration

**Goal:** Automated validation on every commit

```bash
#!/bin/bash
# Example validation script for CI/CD

# Set strict error handling
set -e

# Run validation
validate --rule pds4.bundle \
         --report-file validation-report.txt \
         --target $BUNDLE_PATH

# Check exit code
if [ $? -eq 0 ]; then
    echo "✓ Validation passed"
    exit 0
else
    echo "✗ Validation failed - see validation-report.txt"
    # Upload report as artifact for review
    exit 1
fi
```

## Common Errors
See [Common Errors](errors.html) page for more details.

## Performance
Validate does a lot of varying checks on products from the schema/schematron checks to the ensuring the validity the actual product content.
We are continuing to improve performance of the software, but there are also things you can do to split up execution in order to maximize your computer's
processing power to speed up performance.

### Metrics

To give you a sense of how long it would take to validate individual products with content validation enabled, the following are
are a few benchmarks for validate performance. These times are rough estimates subject to local operating system. Please contact
[Engineering Node](mailto:pds_operator@jpl.nasa.gov) if these times are significantly different from your execution times:

|Content Validation _Enabled_|
|---|
|Number of Products|Avg. Product Size (MB)|Total Time to Completion|Avg. Time/Product|
|1|4|4s|4s|
|1|10|6s|6s|
|1|50|7s|7s|
|1|4800|600s|600s|
|1|0.1|11.069s||
|1|3|17.208s||
|1|30|21.792s,21.591s||
|1|120|30.029s,30.291s||
|1|4945|43.988s||
|40|3.44|77.554s|1.9s|
|151|18.98|469.003s,464.861s|3.1s|
|541|7|901.993s,890.327s|1.68s|
|925|9.3|1734.853s,1732.398s|1.88s|
|4099|10.49|8629.407s|2.10s|
|23189|8.2|82105s (~23h)|3.5s|
|Content Validation _Disabled_ (--no-data-check)|
|Number of Labels|Avg. Label Size (KB)|Total Time to Completion|Avg. Time/Label|
|151|40|215s|1.4s|
|1347|40|1655s|1.2s|
|22164|40|47160s|2.1s|
|Content Validation _Disabled_ (--skip-content-validation)|
|Number of Products|Avg. Product Size (MB)|Total Time to Completion|Avg. Time/Product|
|1|0.1|9.207s||
|1|3|14.531s||
|1|30|14.250s,14.228s||
|1|120|14.714s,14.533s||
|1|4945|30.445s,30.211s||
|40|3.44|45.010s|1.13s|
|151|18.98|115.098s,114.192s|0.76s|
|541|7|334.860s,333.391s|0.62s|
|925|9.3|548.309s,552.169s|0.59s|
|4099|10.49|2326.820s|0.57s|
|22164|N/A|12323.288s|0.56s|
|Content Validation _Disabled_ (--skip-content-validation) and using Catalog File (local schemas)|
|Number of Labels|Avg. Label Size (KB)|Total Time to Completion|Avg. Time/Label|
|151|40|196s|1.3s|
|1347|40|1454s|1.1s|

### Maximize Performance
See _Advanced Bundle Validation_ above for more details on how to maximize performance when validating bundles.
