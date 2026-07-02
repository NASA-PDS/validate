#!/usr/bin/env python3
"""Generate a synthetic PDS4 bundle and time validate against it.

Exercises the referential integrity path (additionalReferentialIntegrityChecks,
collectAllContextReferences) which is the target of the #1568 caching optimization.

Each product label includes Investigation_Area, Observing_System, and
Target_Identification Internal_References so all three cached identifier types
(logicalIdentifiers, lidOrLidVidReferences, contextAreaRefs) are exercised.

Usage
-----
  # Smoke test — 10 products, keep output dir for inspection:
  python3 scripts/benchmark_bundle.py --keep

  # Timing run:
  python3 scripts/benchmark_bundle.py --products 1000

  # Two-run comparison (warm schema cache on first run):
  python3 scripts/benchmark_bundle.py --products 500 --runs 2

  # Custom validate binary:
  python3 scripts/benchmark_bundle.py --products 500 \\
      --validate ./validate-4.2.0-SNAPSHOT/bin/validate
"""

import argparse
import hashlib
import os
import shlex
import shutil
import subprocess
import sys
import tempfile
import time

# ---------------------------------------------------------------------------
# PDS4 constants
# ---------------------------------------------------------------------------
BUNDLE_NS   = "http://pds.nasa.gov/pds4/pds/v1"
SCHEMA_URL  = "https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1D00.xsd"
SCH_URL     = "https://pds.nasa.gov/pds4/pds/v1/PDS4_PDS_1D00.sch"
IM_VERSION  = "1.13.0.0"

BUNDLE_LID  = "urn:nasa:pds:benchmark.bundle"
COLL_LID    = f"{BUNDLE_LID}:data.benchmark"
COLL_LIDVID = f"{COLL_LID}::1.0"
INV_LID     = "urn:nasa:pds:context:investigation:mission.cassini-huygens"
IH_LID      = "urn:nasa:pds:context:instrument_host:spacecraft.co"
INSTR_LID   = "urn:nasa:pds:context:instrument:rss.co"
TGT_LID     = "urn:nasa:pds:context:target:planet.saturn"

# ---------------------------------------------------------------------------
# XML generation helpers
# ---------------------------------------------------------------------------

def _header():
    return (
        '<?xml version="1.0" encoding="UTF-8"?>\n'
        f'<?xml-model href="{SCH_URL}"\n'
        '   schematypens="http://purl.oclc.org/dsdl/schematron"?>\n'
    )


def _ns_attrs():
    return (
        f'xmlns="{BUNDLE_NS}"\n'
        '   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"\n'
        f'   xsi:schemaLocation="{BUNDLE_NS} {SCHEMA_URL}"'
    )


def prod_lid(i):
    return f"{COLL_LID}:product_{i:06d}"


def prod_lidvid(i):
    return f"{prod_lid(i)}::1.0"


def bundle_xml():
    return f"""{_header()}<Product_Bundle {_ns_attrs()}>
  <Identification_Area>
    <logical_identifier>{BUNDLE_LID}</logical_identifier>
    <version_id>1.0</version_id>
    <title>Benchmark Bundle</title>
    <information_model_version>{IM_VERSION}</information_model_version>
    <product_class>Product_Bundle</product_class>
    <Citation_Information>
      <author_list>Benchmark Generator</author_list>
      <publication_year>2024</publication_year>
      <description>Synthetic bundle for benchmarking validate caching performance.</description>
    </Citation_Information>
  </Identification_Area>
  <Context_Area>
    <Time_Coordinates>
      <start_date_time>2005-05-02T00:00:00Z</start_date_time>
      <stop_date_time>2005-05-03T00:00:00Z</stop_date_time>
    </Time_Coordinates>
    <Primary_Result_Summary>
      <purpose>Science</purpose>
      <processing_level>Raw</processing_level>
    </Primary_Result_Summary>
    <Investigation_Area>
      <name>Cassini-Huygens</name>
      <type>Mission</type>
      <Internal_Reference>
        <lid_reference>{INV_LID}</lid_reference>
        <reference_type>bundle_to_investigation</reference_type>
      </Internal_Reference>
    </Investigation_Area>
    <Observing_System>
      <Observing_System_Component>
        <name>Cassini Orbiter</name>
        <type>Spacecraft</type>
        <Internal_Reference>
          <lid_reference>{IH_LID}</lid_reference>
          <reference_type>is_instrument_host</reference_type>
        </Internal_Reference>
      </Observing_System_Component>
      <Observing_System_Component>
        <name>Radio Science Subsystem</name>
        <type>Instrument</type>
        <Internal_Reference>
          <lid_reference>{INSTR_LID}</lid_reference>
          <reference_type>is_instrument</reference_type>
        </Internal_Reference>
      </Observing_System_Component>
    </Observing_System>
    <Target_Identification>
      <name>Saturn</name>
      <type>Planet</type>
      <Internal_Reference>
        <lid_reference>{TGT_LID}</lid_reference>
        <reference_type>bundle_to_target</reference_type>
      </Internal_Reference>
    </Target_Identification>
  </Context_Area>
  <Bundle>
    <bundle_type>Archive</bundle_type>
  </Bundle>
  <Bundle_Member_Entry>
    <lidvid_reference>{COLL_LIDVID}</lidvid_reference>
    <member_status>Primary</member_status>
    <reference_type>bundle_has_data_collection</reference_type>
  </Bundle_Member_Entry>
</Product_Bundle>
"""


def collection_xml(n, csv_name, csv_size, csv_md5):
    return f"""{_header()}<Product_Collection {_ns_attrs()}>
  <Identification_Area>
    <logical_identifier>{COLL_LID}</logical_identifier>
    <version_id>1.0</version_id>
    <title>Benchmark Data Collection</title>
    <information_model_version>{IM_VERSION}</information_model_version>
    <product_class>Product_Collection</product_class>
    <Citation_Information>
      <author_list>Benchmark Generator</author_list>
      <publication_year>2024</publication_year>
      <description>Synthetic data collection for benchmarking validate caching performance.</description>
    </Citation_Information>
  </Identification_Area>
  <Context_Area>
    <Time_Coordinates>
      <start_date_time>2005-05-02T00:00:00Z</start_date_time>
      <stop_date_time>2005-05-03T00:00:00Z</stop_date_time>
    </Time_Coordinates>
    <Primary_Result_Summary>
      <purpose>Science</purpose>
      <processing_level>Raw</processing_level>
    </Primary_Result_Summary>
    <Investigation_Area>
      <name>Cassini-Huygens</name>
      <type>Mission</type>
      <Internal_Reference>
        <lid_reference>{INV_LID}</lid_reference>
        <reference_type>collection_to_investigation</reference_type>
      </Internal_Reference>
    </Investigation_Area>
    <Observing_System>
      <Observing_System_Component>
        <name>Cassini Orbiter</name>
        <type>Spacecraft</type>
        <Internal_Reference>
          <lid_reference>{IH_LID}</lid_reference>
          <reference_type>is_instrument_host</reference_type>
        </Internal_Reference>
      </Observing_System_Component>
      <Observing_System_Component>
        <name>Radio Science Subsystem</name>
        <type>Instrument</type>
        <Internal_Reference>
          <lid_reference>{INSTR_LID}</lid_reference>
          <reference_type>is_instrument</reference_type>
        </Internal_Reference>
      </Observing_System_Component>
    </Observing_System>
    <Target_Identification>
      <name>Saturn</name>
      <type>Planet</type>
      <Internal_Reference>
        <lid_reference>{TGT_LID}</lid_reference>
        <reference_type>collection_to_target</reference_type>
      </Internal_Reference>
    </Target_Identification>
  </Context_Area>
  <Collection>
    <collection_type>Data</collection_type>
  </Collection>
  <File_Area_Inventory>
    <File>
      <file_name>{csv_name}</file_name>
      <file_size unit="byte">{csv_size}</file_size>
      <md5_checksum>{csv_md5}</md5_checksum>
    </File>
    <Inventory>
      <offset unit="byte">0</offset>
      <parsing_standard_id>PDS DSV 1</parsing_standard_id>
      <records>{n}</records>
      <record_delimiter>Carriage-Return Line-Feed</record_delimiter>
      <field_delimiter>Comma</field_delimiter>
      <Record_Delimited>
        <fields>2</fields>
        <groups>0</groups>
        <maximum_record_length unit="byte">257</maximum_record_length>
        <Field_Delimited>
          <name>Member Status</name>
          <field_number>1</field_number>
          <data_type>ASCII_String</data_type>
          <maximum_field_length unit="byte">1</maximum_field_length>
        </Field_Delimited>
        <Field_Delimited>
          <name>LIDVID_LID</name>
          <field_number>2</field_number>
          <data_type>ASCII_LIDVID_LID</data_type>
          <maximum_field_length unit="byte">255</maximum_field_length>
        </Field_Delimited>
      </Record_Delimited>
      <reference_type>inventory_has_member_product</reference_type>
    </Inventory>
  </File_Area_Inventory>
</Product_Collection>
"""


def product_xml(i):
    lid = prod_lid(i)
    dat = f"product_{i:06d}.dat"
    return f"""{_header()}<Product_Observational {_ns_attrs()}>
  <Identification_Area>
    <logical_identifier>{lid}</logical_identifier>
    <version_id>1.0</version_id>
    <title>Benchmark Product {i:06d}</title>
    <information_model_version>{IM_VERSION}</information_model_version>
    <product_class>Product_Observational</product_class>
  </Identification_Area>
  <Observation_Area>
    <Time_Coordinates>
      <start_date_time>2005-05-02T00:00:00Z</start_date_time>
      <stop_date_time>2005-05-03T00:00:00Z</stop_date_time>
    </Time_Coordinates>
    <Primary_Result_Summary>
      <purpose>Science</purpose>
      <processing_level>Raw</processing_level>
    </Primary_Result_Summary>
    <Investigation_Area>
      <name>Cassini-Huygens</name>
      <type>Mission</type>
      <Internal_Reference>
        <lid_reference>{INV_LID}</lid_reference>
        <reference_type>data_to_investigation</reference_type>
      </Internal_Reference>
    </Investigation_Area>
    <Observing_System>
      <Observing_System_Component>
        <name>Cassini Orbiter</name>
        <type>Spacecraft</type>
        <Internal_Reference>
          <lid_reference>{IH_LID}</lid_reference>
          <reference_type>is_instrument_host</reference_type>
        </Internal_Reference>
      </Observing_System_Component>
    </Observing_System>
    <Target_Identification>
      <name>Saturn</name>
      <type>Planet</type>
      <Internal_Reference>
        <lid_reference>{TGT_LID}</lid_reference>
        <reference_type>data_to_target</reference_type>
      </Internal_Reference>
    </Target_Identification>
  </Observation_Area>
  <File_Area_Observational>
    <File>
      <file_name>{dat}</file_name>
      <file_size unit="byte">0</file_size>
    </File>
    <Header>
      <offset unit="byte">0</offset>
      <object_length unit="byte">1</object_length>
      <parsing_standard_id>UTF-8 Text</parsing_standard_id>
    </Header>
  </File_Area_Observational>
</Product_Observational>
"""


# ---------------------------------------------------------------------------
# Bundle generation
# ---------------------------------------------------------------------------

def generate(outdir, n):
    """Write bundle/ collection / products to outdir. Returns path to bundle label."""
    data_dir = os.path.join(outdir, "data")
    os.makedirs(data_dir, exist_ok=True)

    # --- inventory CSV (CRLF line endings as required by PDS4) ---
    csv_name = "collection_benchmark.csv"
    csv_path = os.path.join(outdir, csv_name)
    csv_lines = b"".join(
        f"P,{prod_lidvid(i)}\r\n".encode() for i in range(1, n + 1)
    )
    with open(csv_path, "wb") as f:
        f.write(csv_lines)
    csv_size = len(csv_lines)
    csv_md5  = hashlib.md5(csv_lines).hexdigest()

    # --- collection label ---
    coll_path = os.path.join(outdir, "collection_benchmark.xml")
    with open(coll_path, "w", encoding="utf-8") as f:
        f.write(collection_xml(n, csv_name, csv_size, csv_md5))

    # --- bundle label ---
    bundle_path = os.path.join(outdir, "bundle_benchmark.xml")
    with open(bundle_path, "w", encoding="utf-8") as f:
        f.write(bundle_xml())

    # --- product labels + 0-byte stub data files ---
    print(f"  Generating {n} product labels...", end="", flush=True)
    t0 = time.time()
    for i in range(1, n + 1):
        ppath = os.path.join(data_dir, f"product_{i:06d}.xml")
        with open(ppath, "w", encoding="utf-8") as f:
            f.write(product_xml(i))
        # 0-byte stub satisfies file-existence check; content skipped via --skip-content-validation
        open(os.path.join(data_dir, f"product_{i:06d}.dat"), "wb").close()
        if i % max(1, n // 20) == 0:
            print(".", end="", flush=True)
    elapsed = time.time() - t0
    print(f" done ({elapsed:.1f}s)")

    size_mb = sum(
        os.path.getsize(os.path.join(dp, fn))
        for dp, _, fns in os.walk(outdir)
        for fn in fns
    ) / 1_048_576
    print(f"  Bundle size: {size_mb:.1f} MB  ({n + 2} labels + 1 CSV)")
    return bundle_path


# ---------------------------------------------------------------------------
# Validate runner
# ---------------------------------------------------------------------------

def find_validate():
    """Return path to the validate binary.

    Preference order:
    1. Freshly built distribution zip in target/ (if newer than snapshot) — auto-extracted to /tmp
    2. Pre-built snapshot in validate-4.2.0-SNAPSHOT/
    3. validate on PATH
    """
    repo_root = os.path.normpath(os.path.join(os.path.dirname(__file__), ".."))
    snapshot_bin = os.path.join(repo_root, "validate-4.2.0-SNAPSHOT", "bin", "validate")
    dist_zip = os.path.join(repo_root, "target", "validate-4.2.0-SNAPSHOT-bin.zip")

    # Auto-extract freshly built zip when it's newer than the snapshot binary
    if os.path.isfile(dist_zip) and (
        not os.path.isfile(snapshot_bin)
        or os.path.getmtime(dist_zip) > os.path.getmtime(snapshot_bin)
    ):
        extract_dir = "/tmp/validate_benchmark_bin"
        extracted_bin = os.path.join(extract_dir, "validate-4.2.0-SNAPSHOT", "bin", "validate")
        if not os.path.isfile(extracted_bin) or os.path.getmtime(dist_zip) > os.path.getmtime(extracted_bin):
            import zipfile
            print(f"  Extracting fresh build from {dist_zip}...")
            shutil.rmtree(extract_dir, ignore_errors=True)
            os.makedirs(extract_dir, exist_ok=True)
            with zipfile.ZipFile(dist_zip) as z:
                z.extractall(extract_dir)
            os.chmod(extracted_bin, 0o755)
            print(f"  Extracted to {extract_dir}")
        return extracted_bin

    if os.path.isfile(snapshot_bin):
        return snapshot_bin
    return shutil.which("validate")


def run_validate(validate_cmd, bundle_path, extra_args):
    """Run validate --rule pds4.bundle --skip-content-validation on bundle_path.
    Returns (returncode, elapsed_seconds, stdout+stderr).
    """
    bundle_dir = os.path.dirname(bundle_path)
    cmd = [
        validate_cmd,
        "--rule", "pds4.bundle",
        "--skip-content-validation",
        bundle_dir,
    ] + extra_args

    print(f"  $ {' '.join(cmd)}")
    t0 = time.time()
    result = subprocess.run(cmd, capture_output=True, text=True)
    elapsed = time.time() - t0
    return result.returncode, elapsed, result.stdout + result.stderr


def summarise(output):
    """Extract the final Summary block from validate output."""
    # Find the last "Summary:" heading (not per-product lines)
    idx = output.rfind("\nSummary:")
    if idx == -1:
        return "(no summary found — see full output)"
    return output[idx:].strip()


# ---------------------------------------------------------------------------
# Main
# ---------------------------------------------------------------------------

def main():
    parser = argparse.ArgumentParser(
        description="Generate a synthetic PDS4 bundle and time validate.",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog=__doc__,
    )
    parser.add_argument("--products", type=int, default=10,
                        help="Number of product labels to generate (default: 10)")
    parser.add_argument("--output-dir", default=None,
                        help="Directory to write bundle into (default: temp dir)")
    parser.add_argument("--validate", default=None,
                        help="Path to validate binary (default: auto-detect)")
    parser.add_argument("--runs", type=int, default=1,
                        help="How many timed validate runs to perform (default: 1)")
    parser.add_argument("--keep", action="store_true",
                        help="Keep output directory after run (useful for smoke tests)")
    parser.add_argument("--validate-args", default="",
                        help="Extra args passed through to validate (quoted string)")
    args = parser.parse_args()

    # --- locate validate ---
    validate_cmd = args.validate or find_validate()
    if not validate_cmd:
        sys.exit(
            "ERROR: cannot find validate binary.\n"
            "Run `mvn package -DskipTests` first, or pass --validate <path>."
        )
    validate_cmd = os.path.abspath(validate_cmd)
    print(f"validate: {validate_cmd}")

    # --- output directory ---
    tmp_owned = False
    if args.output_dir:
        outdir = os.path.abspath(args.output_dir)
        os.makedirs(outdir, exist_ok=True)
    else:
        outdir = tempfile.mkdtemp(prefix="pds4_benchmark_")
        tmp_owned = True

    print(f"output:   {outdir}")
    print(f"products: {args.products}")
    print()

    try:
        # --- generate ---
        print("=== Generating bundle ===")
        bundle_path = generate(outdir, args.products)
        print()

        # --- run validate ---
        extra = shlex.split(args.validate_args) if args.validate_args else []
        timings = []
        for run_n in range(1, args.runs + 1):
            label = f"Run {run_n}/{args.runs}" if args.runs > 1 else "Validate"
            print(f"=== {label} ===")
            rc, elapsed, output = run_validate(validate_cmd, bundle_path, extra)
            timings.append(elapsed)
            print(f"  Exit code : {rc}")
            print(f"  Elapsed   : {elapsed:.2f}s")
            print(f"  Summary   :")
            for line in summarise(output).splitlines():
                print(f"    {line}")
            print()

        # --- timing summary ---
        if args.runs > 1:
            print("=== Timing summary ===")
            for i, t in enumerate(timings, 1):
                print(f"  Run {i}: {t:.2f}s")
            print(f"  Best : {min(timings):.2f}s")
            print(f"  Mean : {sum(timings)/len(timings):.2f}s")
            print()

        if args.keep or args.output_dir:
            print(f"Bundle kept at: {outdir}")

    finally:
        if tmp_owned and not args.keep:
            shutil.rmtree(outdir, ignore_errors=True)


if __name__ == "__main__":
    main()
