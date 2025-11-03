# XML Report Example

The following is an example of an XML report:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<validateReport>
   <configuration>
      <version>1.14.0-dev</version>
      <date>2018-06-21T21:58:01Z</date>
   </configuration>
   <parameters>
      <targets>[file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/]</targets>
      <ruleType>pds4.bundle</ruleType>
      <severityLevel>WARNING</severityLevel>
      <recurseDirectories>true</recurseDirectories>
      <fileFiltersUsed>[*.xml, *.XML]</fileFiltersUsed>
      <forceMode>on</forceMode>
      <dataContentValidation>on</dataContentValidation>
      <maxErrors>100000</maxErrors>
   </parameters>
   <ProductLevelValidationResults>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle_checksums.xml">
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml">
         <fragments/>
         <dataFile uri="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/Collection_browse_inventory.tab">
            <message record="1" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
         </dataFile>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/ele_mom_browse.xml">
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/Collection_context.xml">
         <fragments/>
         <dataFile uri="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/Collection_context_inventory.tab">
            <message record="1" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
            <message record="2" severity="ERROR" table="1" type="table.error.fields_mismatch">
               <content>Record 2 has wrong number of fields (expected 2, got 1)</content>
            </message>
            <message record="3" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
            <message record="4" severity="ERROR" table="1" type="table.error.fields_mismatch">
               <content>Record 4 has wrong number of fields (expected 2, got 1)</content>
            </message>
         </dataFile>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_host_VG2_1.0.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_inst_PLS_VG2_1.0.xml">
         <message column="17" line="28" severity="WARNING" type="label.warning.schematron">
            <content>The value Plasma Analyzer for attribute Instrument.type is deprecated and should not be used.</content>
         </message>
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_mission_VOYAGER_1.0.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_target_JUPITER_1.0.xml">
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/Collection_data.xml">
         <fragments/>
         <dataFile uri="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/Collection_data_inventory.tab">
            <message record="1" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
         </dataFile>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/ele_mom_tblChar.xml">
         <fragments>
            <schema uri="https://pds.nasa.gov/pds4/dph/v1/PDS4_DPH_700.xsd">
               <message severity="FATAL_ERROR" type="label.error.unresolvable_resource">
                  <content>https://pds.nasa.gov/pds4/dph/v1/PDS4_DPH_700.xsd</content>
               </message>
            </schema>
         </fragments>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/checksums.xml">
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/Collection_document.xml">
         <fragments/>
         <dataFile uri="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/Collection_document_inventory.tab">
            <message record="1" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
            <message record="2" severity="ERROR" table="1" type="table.error.fields_mismatch">
               <content>Record 2 has wrong number of fields (expected 2, got 1)</content>
            </message>
            <message record="3" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
            <message record="4" severity="ERROR" table="1" type="table.error.fields_mismatch">
               <content>Record 4 has wrong number of fields (expected 2, got 1)</content>
            </message>
         </dataFile>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/errata.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/mission.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/plsinst.xml">
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/Collection_xml_schema.xml">
         <fragments/>
         <dataFile uri="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/Collection_xml_schema_inventory.tab">
            <message record="1" severity="ERROR" table="1" type="table.error.missing_CRLF">
               <content>Record does not end in carriage-return line feed.</content>
            </message>
         </dataFile>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xml">
         <fragments/>
      </label>
   </ProductLevelValidationResults>
   <PDS4BundleLevelValidationResults>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/Collection_browse.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/Collection_context.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/Collection_data.xml">
         <message severity="WARNING" type="integrity.warning.member_not_found">
            <content>The member 'urn:nasa:pds:example.dph.sample_archive_bundle:data:tablechar.vg2-j-pls-5-summ-ele-mom-96.0sec-v1.0::1.0' could not be found in any product within the given target.</content>
         </message>
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/Collection_document.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/Collection_xml_schema.xml">
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/.DS_Store">
         <message severity="ERROR" type="bundle.error.invalid_file_in_root_directory">
            <content>File is not valid in bundle root directory</content>
         </message>
         <message severity="ERROR" type="file.error.name_has_invalid_characters">
            <content>File name uses invalid character</content>
         </message>
         <message severity="ERROR" type="file.error.not_referenced_in_label">
            <content>File is not referenced by any label</content>
         </message>
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle_checksums.txt">
         <message severity="ERROR" type="bundle.error.invalid_file_in_root_directory">
            <content>File is not valid in bundle root directory</content>
         </message>
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/data/ELE_MOM.TAB">
         <message severity="ERROR" type="file.error.not_referenced_in_label">
            <content>File is not referenced by any label</content>
         </message>
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/PLS/INFO.TXT">
         <message severity="ERROR" type="file.error.not_referenced_in_label">
            <content>File is not referenced by any label</content>
         </message>
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/PLS/PLSINST.LBL">
         <message severity="ERROR" type="file.error.not_referenced_in_label">
            <content>File is not referenced by any label</content>
         </message>
         <fragments/>
      </label>
      <label status="FAIL"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/PDS4_DPH_1700.xsd">
         <message severity="ERROR" type="file.error.not_referenced_in_label">
            <content>File is not referenced by any label</content>
         </message>
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_inst_PLS_VG2_1.0.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/checksums.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/xml_schema/PDS4_PDS_1700.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_host_VG2_1.0.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/browse/ele_mom_browse.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/plsinst.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/mission.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_mission_VOYAGER_1.0.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/context/PDS4_target_JUPITER_1.0.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/document/errata.xml">
         <fragments/>
      </label>
      <label status="PASS"
             target="file:/Users/mcayanan/pds4/dph_example_archive_VG2PLS/bundle_checksums.xml">
         <message severity="WARNING" type="integrity.warning.unreferenced_member">
            <content>Identifier 'urn:nasa:pds:example.dph.sample_archive_bundle:bundle:bundle_checksums::1.0' is not a member of any collection within the given target</content>
         </message>
         <fragments/>
      </label>
      <summary>
         <totalErrors>20</totalErrors>
         <totalWarnings>3</totalWarnings>
         <messageTypes>
            <messageType total="1">label.error.unresolvable_resource</messageType>
            <messageType total="1">file.error.name_has_invalid_characters</messageType>
            <messageType total="7">table.error.missing_CRLF</messageType>
            <messageType total="1">integrity.warning.member_not_found</messageType>
            <messageType total="2">bundle.error.invalid_file_in_root_directory</messageType>
            <messageType total="1">integrity.warning.unreferenced_member</messageType>
            <messageType total="1">label.warning.schematron</messageType>
            <messageType total="4">table.error.fields_mismatch</messageType>
            <messageType total="5">file.error.not_referenced_in_label</messageType>
         </messageTypes>
      </summary>
   </PDS4BundleLevelValidationResults>
</validateReport>
```
