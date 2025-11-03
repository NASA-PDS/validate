# Common Errors

This guide helps you troubleshoot common validation errors. We're constantly adding more examples as we encounter them.

**Want to contribute?** If you've solved an error not listed here, please [create a pull request](https://github.com/NASA-PDS/validate/) or [open an issue](https://github.com/NASA-PDS/validate/issues) on GitHub, or email [pds_operator@jpl.nasa.gov](mailto:pds_operator@jpl.nasa.gov).

* [White spaces are required error](#white-spaces-are-required-error)
* [java.lang.OutOfMemoryError](#javalangoutofmemoryerror)
* [No checksum found in the manifest errors](#no-checksum-found-in-the-manifest-errors)
* [error.table.fields_mismatch](#errortablefields_mismatch)
* [error.label.context_ref_mismatch](#errorlabelcontext_ref_mismatch)
* [Invalid maximum heap size](#invalid-maximum-heap-size)
* [PDF/A Issues](#pdfa-issues)
* [Enabling Debug Logging](#enabling-debug-logging)

## White spaces are required error

You might see this error in your validation output:

```
FAIL: file:/Users/.../hi0173794441_9080000_001_r.xml
    FATAL_ERROR  line 1, 55: White spaces are required between publicId and systemId.
```

**What this means:** This cryptic message from the Xerces XML parser usually means the schema for your label's default namespace is missing. For example, if your label uses namespace "http://pds.nasa.gov/pds4/pds/v03", Validate needs the corresponding schema file (PDS4_PDS_0300a.xsd).

**How to fix:** Make sure you're providing all required schemas, either bundled with Validate or via the `--schema` flag.

## java.lang.OutOfMemoryError

**Error message:** `Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded`

When validating large bundles, you might see this error:

```
Sep 19, 2017 12:02:39 PM gov.nasa.pds.tools.label.LocationValidator validate
INFO: Using validation style 'PDS4 Directory' for location file:/home/atmos7/anonymous/PDS/data/PDS4/MAVEN/iuvs_calibrated_bundle/
Sep 19, 2017 12:02:39 PM gov.nasa.pds.tools.validate.task.ValidationTask execute
INFO: Starting validation task for location 'file:/home/atmos7/anonymous/PDS/data/PDS4/MAVEN/iuvs_calibrated_bundle/'
Sep 22, 2017 7:07:31 AM gov.nasa.pds.tools.validate.task.ValidationTask execute
INFO: Validation complete for location 'file:/home/atmos7/anonymous/PDS/data/PDS4/MAVEN/iuvs_calibrated_bundle/'
Exception in thread "main" java.lang.OutOfMemoryError: GC overhead limit exceeded
```

**What this means:** Validate ran out of memory. This happens with large bundles because Validate caches results in memory until validation completes.

**How to fix:** Increase Java's heap space to `-Xms4096m -Xmx8192m`:

**For Unix/Linux/Mac:**
Edit the `validate` shell script:
```bash
"${JAVA_HOME}"/bin/java -Xms4096m -Xmx8192m -jar ${VALIDATE_JAR} "$@"
```

**For Windows:**
Edit the `validate.bat` batch file:
```bat
"%JAVA_HOME%"\bin\java -Xms4096m -Xmx8192m -jar "%VALIDATE_JAR%" %*
```

**Still having issues?** Check the [Improve Performance](index.md#improve-performance) section for tips on batching large validations.

## No checksum found in the manifest errors

If you're using a checksum manifest and see errors like this:

```
FAIL: file:/home/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml
    ERROR  No checksum found in the manifest for 'file:/home/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml'.
```

**What this means:** Your manifest file's base path doesn't match your file paths.

**How to fix:** Check the "Manifest File Base Path" in your validation report. If it's wrong, use the `-B` or `--base-path` flag to specify the correct base path.

## error.table.fields_mismatch

**What this means:** The fields in your table don't match what's defined in your label.

**What to check:**
* **Table offset value** - If wrong, Validate reads from the wrong position. See [GitHub issue #96](https://github.com/NASA-PDS/validate/issues/96) for an example.
* **field_delimiter** - Verify the delimiter matches your data file
* **Record fields count** - Make sure the number matches your actual data

## error.label.context_ref_mismatch

**What this means:** The name and type values for context products in your label (like Target_Identification, Investigation_Area, or Observing_System_Component) don't match what's registered in PDS. This check was added in Validate 1.16.0 to improve search accuracy.

**How to fix:**
* **For older archives** (pre-1.16.0): You can reprocess your data, document the errors in your readme, or use `--skip-context-validation`
* **For new archives**: Update either the context product or your data labels to match

## Invalid maximum heap size

**Error message:**
```
Invalid maximum heap size: -Xmx4096m
The specified size exceeds the maximum representable size.
Error: Could not create the Java Virtual Machine.
Error: A fatal exception has occurred. Program will exit.
```

**What this means:** You're using 32-bit Java instead of the required 64-bit version.

**How to fix:** Check your Java version:

```
$ java -version
openjdk version "15.0.1" 2020-10-20
OpenJDK Runtime Environment (build 15.0.1+9)
OpenJDK 64-Bit Server VM (build 15.0.1+9, mixed mode, sharing)
```

The last line should say **64-Bit Server VM**. If not, install 64-bit Java (see [System Requirements](../install/index.html#system-requirements)).

## PDF/A Issues

**Error message:**
```
ERROR  [error.pdf.file.not_pdfa_compliant]
```

**What this means:** Your PDF doesn't meet PDF/A 1a or 1b compliance standards.

**Common issues:**
* **Embedded multimedia** - PDF/A doesn't support embedded audio/video
* **Proprietary fonts** - Copyrighted fonts can't be embedded in PDF/A (Microsoft fonts in Word/PowerPoint often cause this)
* **3D models** - PDF/A doesn't support embedded 3D content

**How to fix:** Check the [VeraPDF Rules documentation](https://github.com/veraPDF/veraPDF-validation-profiles/wiki/PDFA-Part-1-rules) to identify specific issues. If you need help, contact your PDS Node representative or email [pds_operator@jpl.nasa.gov](mailto:pds_operator@jpl.nasa.gov).

## Enabling Debug Logging

**When to use:** When you need detailed information to troubleshoot validation problems.

**How to enable:**

Use `--verbose` to control what messages you see:
- `--verbose 1` = INFO, WARNING, and ERROR messages
- `--verbose 2` = WARNING and ERROR messages (default)
- `--verbose 3` = ERROR messages only

```
% validate --verbose 1 --target myproduct.xml
```

For full error stack traces, add `--debug-mode`:

```
% validate --verbose 1 --debug-mode --target myproduct.xml
```

**Note:** Debug logging generates a lot of output and slows down validation. Only use it when troubleshooting specific problems.
