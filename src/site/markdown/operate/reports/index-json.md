# JSON Report Example

The following is an example of a JSON report:

```json
{
  "title": "PDS Validation Tool Report",
  "configuration": {
    "version": "1.14.0-dev",
    "date": "2018-06-21T21:58:09Z"
  },
  "parameters": {
    "targets": "[file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/]",
    "ruleType": "pds4.bundle",
    "severityLevel": "WARNING",
    "recurseDirectories": "true",
    "fileFiltersUsed": "[*.xml, *.XML]",
    "forceMode": "on",
    "dataContentValidation": "on",
    "maxErrors": "100000"
  },
  "productLevelValidationResults": [
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle_checksums.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml",
      "messages": [],
      "fragments": [],
      "dataContents": [
        {
          "dataFile": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/Collection_browse_inventory.tab",
          "messages": [
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 1,
              "message": "Record does not end in carriage-return line feed."
            }
          ]
        }
      ]
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/ele_mom_browse.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/Collection_context.xml",
      "messages": [],
      "fragments": [],
      "dataContents": [
        {
          "dataFile": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/Collection_context_inventory.tab",
          "messages": [
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 1,
              "message": "Record does not end in carriage-return line feed."
            },
            {
              "severity": "ERROR",
              "type": "table.error.fields_mismatch",
              "table": 1,
              "record": 2,
              "message": "Record 2 has wrong number of fields (expected 2, got 1)"
            },
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 3,
              "message": "Record does not end in carriage-return line feed."
            },
            {
              "severity": "ERROR",
              "type": "table.error.fields_mismatch",
              "table": 1,
              "record": 4,
              "message": "Record 4 has wrong number of fields (expected 2, got 1)"
            }
          ]
        }
      ]
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_host_VG2_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_inst_PLS_VG2_1.0.xml",
      "messages": [
        {
          "severity": "WARNING",
          "type": "label.warning.schematron",
          "line": 28,
          "column": 17,
          "message": "The value Plasma Analyzer for attribute Instrument.type is deprecated and should not be used."
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_mission_VOYAGER_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_target_JUPITER_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/Collection_data.xml",
      "messages": [],
      "fragments": [],
      "dataContents": [
        {
          "dataFile": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/Collection_data_inventory.tab",
          "messages": [
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 1,
              "message": "Record does not end in carriage-return line feed."
            }
          ]
        }
      ]
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/ele_mom_tblChar.xml",
      "messages": [],
      "fragments": [
        {
          "schema": "https://pds.nasa.gov/pds4/dph/v1/PDS4_DPH_700.xsd",
          "messages": [
            {
              "severity": "FATAL_ERROR",
              "type": "label.error.unresolvable_resource",
              "message": "https://pds.nasa.gov/pds4/dph/v1/PDS4_DPH_700.xsd"
            }
          ]
        }
      ],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/checksums.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/Collection_document.xml",
      "messages": [],
      "fragments": [],
      "dataContents": [
        {
          "dataFile": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/Collection_document_inventory.tab",
          "messages": [
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 1,
              "message": "Record does not end in carriage-return line feed."
            },
            {
              "severity": "ERROR",
              "type": "table.error.fields_mismatch",
              "table": 1,
              "record": 2,
              "message": "Record 2 has wrong number of fields (expected 2, got 1)"
            },
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 3,
              "message": "Record does not end in carriage-return line feed."
            },
            {
              "severity": "ERROR",
              "type": "table.error.fields_mismatch",
              "table": 1,
              "record": 4,
              "message": "Record 4 has wrong number of fields (expected 2, got 1)"
            }
          ]
        }
      ]
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/errata.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/mission.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/plsinst.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/Collection_xml_schema.xml",
      "messages": [],
      "fragments": [],
      "dataContents": [
        {
          "dataFile": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/Collection_xml_schema_inventory.tab",
          "messages": [
            {
              "severity": "ERROR",
              "type": "table.error.missing_CRLF",
              "table": 1,
              "record": 1,
              "message": "Record does not end in carriage-return line feed."
            }
          ]
        }
      ]
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    }
  ],
  "PDS4BundleLevelValidationResults": [
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/Collection_context.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/Collection_data.xml",
      "messages": [
        {
          "severity": "WARNING",
          "type": "integrity.warning.member_not_found",
          "message": "The member 'urn:nasa:pds:example.dph.sample_archive_bundle:data:tablechar.vg2-j-pls-5-summ-ele-mom-96.0sec-v1.0::1.0' could not be found in any product within the given target."
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/Collection_document.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/Collection_xml_schema.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/.DS_Store",
      "messages": [
        {
          "severity": "ERROR",
          "type": "bundle.error.invalid_file_in_root_directory",
          "message": "File is not valid in bundle root directory"
        },
        {
          "severity": "ERROR",
          "type": "file.error.name_has_invalid_characters",
          "message": "File name uses invalid character"
        },
        {
          "severity": "ERROR",
          "type": "file.error.not_referenced_in_label",
          "message": "File is not referenced by any label"
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle_checksums.txt",
      "messages": [
        {
          "severity": "ERROR",
          "type": "bundle.error.invalid_file_in_root_directory",
          "message": "File is not valid in bundle root directory"
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/ELE_MOM.TAB",
      "messages": [
        {
          "severity": "ERROR",
          "type": "file.error.not_referenced_in_label",
          "message": "File is not referenced by any label"
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/PLS/INFO.TXT",
      "messages": [
        {
          "severity": "ERROR",
          "type": "file.error.not_referenced_in_label",
          "message": "File is not referenced by any label"
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/PLS/PLSINST.LBL",
      "messages": [
        {
          "severity": "ERROR",
          "type": "file.error.not_referenced_in_label",
          "message": "File is not referenced by any label"
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "FAIL",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/PDS4_DPH_1700.xsd",
      "messages": [
        {
          "severity": "ERROR",
          "type": "file.error.not_referenced_in_label",
          "message": "File is not referenced by any label"
        }
      ],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_inst_PLS_VG2_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/checksums.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_host_VG2_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/ele_mom_browse.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/plsinst.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/mission.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_mission_VOYAGER_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_target_JUPITER_1.0.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/errata.xml",
      "messages": [],
      "fragments": [],
      "dataContents": []
    },
    {
      "status": "PASS",
      "label": "file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle_checksums.xml",
      "messages": [
        {
          "severity": "WARNING",
          "type": "integrity.warning.unreferenced_member",
          "message": "Identifier 'urn:nasa:pds:example.dph.sample_archive_bundle:bundle:bundle_checksums::1.0' is not a member of any collection within the given target"
        }
      ],
      "fragments": [],
      "dataContents": []
    }
  ],
  "summary": {
    "totalErrors": 20,
    "totalWarnings": 3,
    "messageTypes": [
      {
        "messageType": "label.error.unresolvable_resource",
        "total": 1
      },
      {
        "messageType": "file.error.name_has_invalid_characters",
        "total": 1
      },
      {
        "messageType": "table.error.missing_CRLF",
        "total": 7
      },
      {
        "messageType": "integrity.warning.member_not_found",
        "total": 1
      },
      {
        "messageType": "bundle.error.invalid_file_in_root_directory",
        "total": 2
      },
      {
        "messageType": "integrity.warning.unreferenced_member",
        "total": 1
      },
      {
        "messageType": "label.warning.schematron",
        "total": 1
      },
      {
        "messageType": "table.error.fields_mismatch",
        "total": 4
      },
      {
        "messageType": "file.error.not_referenced_in_label",
        "total": 5
      }
    ]
  }
}
```
