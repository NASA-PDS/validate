<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:pds  Version:1.18.0.0 - Mon Jun 06 09:40:04 EDT 2022 -->
  <!-- Generated from the PDS4 Information Model Version 1.18.0.0 - System Build 12.1 -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="/*">
      <sch:assert test="name() = ('Product_Attribute_Definition','Product_Browse', 'Product_Ancillary', 'Product_Bundle', 'Product_Class_Definition',  'Product_Collection', 'Product_Context', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Metadata_Supplemental', 'Product_Observational', 'Product_Service', 'Product_Native', 'Product_Software', 'Product_SPICE_Kernel', 'Product_Thumbnail', 'Product_Update', 'Product_XML_Schema', 'Product_Zipped', 'Product_Data_Set_PDS3', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3','Product_Mission_PDS3', 'Product_Proxy_PDS3', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_AIP', 'Product_DIP', 'Product_SIP', 'Product_SIP_Deep_Archive', 'Product_DIP_Deep_Archive', 'Ingest_LDD')">
        <title>aaa:Root/product_class</title>
        The ROOT element must be one of the allowed types.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="*[@xsi:nil]">
      <sch:assert test="(@xsi:nil eq 'true' and @nilReason) or not(@xsi:nil eq 'true' or @nilReason)">
        <title>aaa:Root/nilReason/nilReason</title>
        The 'nilReason' attribute must be used in conjunction with xsi:nil="true"</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="/*[not(contains(name(), 'Ingest') or contains(name(), 'Bundle') or contains(name(), 'Collection'))]/pds:Identification_Area/pds:logical_identifier">
      <sch:assert test="string-length(.) - string-length(translate(., ':', '')) eq 5">
        <title>pds:/Product_Observational/pds:Identification_Area/pds:logical_identifier/logical_identifier</title>
        pds:logical_identifier must have the form "urn:agencyId:authorityId:bundleID:collectionID:productID"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:ASCII_Date" role="warning">
      <sch:assert test="false()">
        <title>pds:ASCII_Date role="warning"/pds:ASCII_Date</title>
        pds:ASCII_Date is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:ASCII_Date_Time" role="warning">
      <sch:assert test="false()">
        <title>pds:ASCII_Date_Time role="warning"/pds:ASCII_Date_Time</title>
        pds:ASCII_Date_Time is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:ASCII_Date_Time_UTC" role="warning">
      <sch:assert test="false()">
        <title>pds:ASCII_Date_Time_UTC role="warning"/pds:ASCII_Date_Time_UTC</title>
        pds:ASCII_Date_Time_UTC is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Agency/pds:name">
      <sch:assert test=". = ('European Space Agency', 'Japan Aerospace Exploration Agency', 'National Aeronautics and Space Administration', 'Roscosmos State Corporation for Space Activities')">
        <title>pds:Agency/pds:name/pds:name</title>
        The attribute pds:Agency/pds:name must be equal to one of the following values 'European Space Agency', 'Japan Aerospace Exploration Agency', 'National Aeronautics and Space Administration', 'Roscosmos State Corporation for Space Activities'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Airborne" role="warning">
      <sch:assert test="false()">
        <title>pds:Airborne role="warning"/pds:Airborne</title>
        pds:Airborne is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Airborne/pds:type">
      <sch:assert test=". = ('Aircraft', 'Balloon', 'Suborbital Rocket')">
        <title>pds:Airborne/pds:type/pds:type</title>
        The attribute pds:Airborne/pds:type must be equal to one of the following values 'Aircraft', 'Balloon', 'Suborbital Rocket'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array">
      <sch:assert test="number(pds:axes) = count(pds:Axis_Array)">
        <title>pds:Array/Array</title>
        The value of pds:axes must match the number of pds:Axis_Array classes in the Array.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array/pds:Local_Internal_Reference" role="warning">
      <sch:assert test="false()">
        <title>pds:Array/pds:Local_Internal_Reference role="warning"/pds:Array.Local_Internal_Reference</title>
        pds:Array/pds:Local_Internal_Reference is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array/pds:offset">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Array/pds:offset/pds:offset</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_1D/pds:axes">
      <sch:assert test=". = ('1')">
        <title>pds:Array_1D/pds:axes/pds:axes</title>
        The attribute pds:Array_1D/pds:axes must be equal to the value '1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_1D/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_1D/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_1D/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D/pds:axes">
      <sch:assert test=". = ('2')">
        <title>pds:Array_2D/pds:axes/pds:axes</title>
        The attribute pds:Array_2D/pds:axes must be equal to the value '2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_2D/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_2D/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image">
      <sch:assert test="pds:Axis_Array[1]/pds:axis_name = ('Line') and pds:Axis_Array[2]/pds:axis_name = ('Sample') or pds:Axis_Array[1]/pds:axis_name = ('Sample') and pds:Axis_Array[2]/pds:axis_name = ('Line')">
        <title>pds:Array_2D_Image/axis_name</title>
        The names of the first and second axis of an Array_2D_Image must be set to Line and Sample.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image/pds:Axis_Array[1]">
      <sch:assert test="pds:axis_name = ('Line', 'Sample')">
        <title>pds:Array_2D_Image/pds:Axis_Array[1]/axis_name</title>
        The name of the first axis of an Array_2D_Image must be set to either Line or Sample.</sch:assert>
      <sch:assert test="pds:sequence_number eq '1'">
        <title>pds:Array_2D_Image/pds:Axis_Array[1]/sequence_number</title>
        The sequence number of the first axis of an Array_2D_Image must be set to 1.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image/pds:Axis_Array[2]">
      <sch:assert test="pds:axis_name = ('Line', 'Sample')">
        <title>pds:Array_2D_Image/pds:Axis_Array[2]/axis_name</title>
        The name of the second axis of an Array_2D_Image must be set to either Line or Sample.</sch:assert>
      <sch:assert test="pds:sequence_number eq '2'">
        <title>pds:Array_2D_Image/pds:Axis_Array[2]/sequence_number</title>
        The sequence number of the second axis of an Array_2D_Image must be set to 2.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image/pds:axes">
      <sch:assert test=". = ('2')">
        <title>pds:Array_2D_Image/pds:axes/pds:axes</title>
        The attribute pds:Array_2D_Image/pds:axes must be equal to the value '2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_2D_Image/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_2D_Image/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Map/pds:axes">
      <sch:assert test=". = ('2')">
        <title>pds:Array_2D_Map/pds:axes/pds:axes</title>
        The attribute pds:Array_2D_Map/pds:axes must be equal to the value '2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Map/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_2D_Map/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_2D_Map/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Spectrum/pds:axes">
      <sch:assert test=". = ('2')">
        <title>pds:Array_2D_Spectrum/pds:axes/pds:axes</title>
        The attribute pds:Array_2D_Spectrum/pds:axes must be equal to the value '2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Spectrum/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_2D_Spectrum/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_2D_Spectrum/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D/pds:axes">
      <sch:assert test=". = ('3')">
        <title>pds:Array_3D/pds:axes/pds:axes</title>
        The attribute pds:Array_3D/pds:axes must be equal to the value '3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_3D/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_3D/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Image/pds:axes">
      <sch:assert test=". = ('3')">
        <title>pds:Array_3D_Image/pds:axes/pds:axes</title>
        The attribute pds:Array_3D_Image/pds:axes must be equal to the value '3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Image/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_3D_Image/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_3D_Image/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Movie/pds:axes">
      <sch:assert test=". = ('3')">
        <title>pds:Array_3D_Movie/pds:axes/pds:axes</title>
        The attribute pds:Array_3D_Movie/pds:axes must be equal to the value '3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Movie/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_3D_Movie/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_3D_Movie/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Spectrum/pds:axes">
      <sch:assert test=". = ('3')">
        <title>pds:Array_3D_Spectrum/pds:axes/pds:axes</title>
        The attribute pds:Array_3D_Spectrum/pds:axes must be equal to the value '3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Spectrum/pds:axis_index_order">
      <sch:assert test=". = ('Last Index Fastest')">
        <title>pds:Array_3D_Spectrum/pds:axis_index_order/pds:axis_index_order</title>
        The attribute pds:Array_3D_Spectrum/pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Axis_Array/pds:unit" role="warning">
      <sch:assert test="false()">
        <title>pds:Axis_Array/pds:unit role="warning"/pds:Axis_Array.unit</title>
        pds:Axis_Array/pds:unit is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Band_Bin" role="warning">
      <sch:assert test="false()">
        <title>pds:Band_Bin role="warning"/pds:Band_Bin</title>
        pds:Band_Bin is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Band_Bin/pds:band_width">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>pds:Band_Bin/pds:band_width/pds:band_width</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Band_Bin/pds:center_wavelength">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>pds:Band_Bin/pds:center_wavelength/pds:center_wavelength</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Band_Bin_Set" role="warning">
      <sch:assert test="false()">
        <title>pds:Band_Bin_Set role="warning"/pds:Band_Bin_Set</title>
        pds:Band_Bin_Set is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle/pds:bundle_type">
      <sch:assert test=". = ('Archive', 'Supplemental')">
        <title>pds:Bundle/pds:bundle_type/pds:bundle_type</title>
        The attribute pds:Bundle/pds:bundle_type must be equal to one of the following values 'Archive', 'Supplemental'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle_Member_Entry">
      <sch:let name="lid_num_colons" value="string-length(pds:lid_reference) - string-length(translate(pds:lid_reference, ':', ''))"/>
      <sch:let name="lidvid_num_colons" value="string-length(pds:lidvid_reference) - string-length(translate(pds:lidvid_reference, ':', ''))"/>
      <sch:let name="lid_required_colons" value="4"/>
      <sch:let name="lidvid_required_colons" value="6"/>
      <sch:let name="urn_nasa" value="'urn:nasa:pds:'"/>
      <sch:let name="urn_esa" value="'urn:esa:psa:'"/>
      <sch:let name="urn_ros" value="'urn:ros:rssa:'"/>
      <sch:let name="urn_jaxa" value="'urn:jaxa:darts:'"/>
      <sch:let name="urn_isro" value="'urn:isro:isda:'"/>
      <sch:assert test="if (pds:lid_reference) then ($lid_num_colons eq $lid_required_colons) else true()">
        <title>pds:Bundle_Member_Entry/lid_reference</title>
        The number of colons found in lid_reference: (<sch:value-of select="$lid_num_colons"/>) is inconsistent with the number expected: <sch:value-of select="$lid_required_colons"/>.</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then ($lidvid_num_colons eq $lidvid_required_colons) else true()">
        <title>pds:Bundle_Member_Entry/lidvid_reference</title>
        The number of colons found in lidvid_reference: (<sch:value-of select="$lidvid_num_colons"/>) is inconsistent with the number expected: <sch:value-of select="$lidvid_required_colons"/>.</sch:assert>
      <sch:assert test="if (pds:lid_reference) then starts-with(pds:lid_reference, $urn_nasa) or starts-with(pds:lid_reference, $urn_esa) or starts-with(pds:lid_reference, $urn_jaxa) or starts-with(pds:lid_reference, $urn_ros) or starts-with(pds:lid_reference, $urn_isro) else true()">
        <title>pds:Bundle_Member_Entry/lid_reference</title>
        The value of the attribute lid_reference must start with either: <sch:value-of select="$urn_nasa"/> or <sch:value-of select="$urn_esa"/> or <sch:value-of select="$urn_jaxa"/> or <sch:value-of select="$urn_ros"/> or <sch:value-of select="$urn_isro"/></sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then starts-with(pds:lidvid_reference, $urn_nasa) or starts-with(pds:lidvid_reference, $urn_esa) or starts-with(pds:lidvid_reference, $urn_jaxa) or starts-with(pds:lidvid_reference, $urn_ros) or starts-with(pds:lidvid_reference, $urn_isro) else true()">
        <title>pds:Bundle_Member_Entry/lidvid_reference</title>
        The value of the attribute lidvid_reference must start with either: <sch:value-of select="$urn_nasa"/> or <sch:value-of select="$urn_esa"/> or <sch:value-of select="$urn_jaxa"/> or <sch:value-of select="$urn_ros"/> or <sch:value-of select="$urn_isro"/></sch:assert>
      <sch:assert test="if (pds:lid_reference) then not(contains(pds:lid_reference,'::')) else true()">
        <title>pds:Bundle_Member_Entry/lid_reference</title>
        The value of the attribute lid_reference must not include a value that contains '::' followed by version id</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()">
        <title>pds:Bundle_Member_Entry/lidvid_reference</title>
        The value of the attribute lidvid_reference must include a value that contains '::' followed by version id</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle_Member_Entry" role="warning">
      <sch:assert test="pds:reference_type != 'bundle_has_member_collection'">
        <title>pds:Bundle_Member_Entry role="warning"/pds:Bundle_Member_Entry.reference_type</title>
        The value bundle_has_member_collection for attribute Bundle_Member_Entry.reference_type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle_Member_Entry/pds:member_status">
      <sch:assert test=". = ('Primary', 'Secondary')">
        <title>pds:Bundle_Member_Entry/pds:member_status/pds:member_status</title>
        The attribute pds:Bundle_Member_Entry/pds:member_status must be equal to one of the following values 'Primary', 'Secondary'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle_Member_Entry/pds:reference_type">
      <sch:assert test=". = ('bundle_has_browse_collection', 'bundle_has_calibration_collection', 'bundle_has_context_collection', 'bundle_has_data_collection', 'bundle_has_document_collection', 'bundle_has_geometry_collection', 'bundle_has_member_collection', 'bundle_has_miscellaneous_collection', 'bundle_has_schema_collection', 'bundle_has_spice_kernel_collection')">
        <title>pds:Bundle_Member_Entry/pds:reference_type/pds:reference_type</title>
        The attribute pds:Bundle_Member_Entry/pds:reference_type must be equal to one of the following values 'bundle_has_browse_collection', 'bundle_has_calibration_collection', 'bundle_has_context_collection', 'bundle_has_data_collection', 'bundle_has_document_collection', 'bundle_has_geometry_collection', 'bundle_has_member_collection', 'bundle_has_miscellaneous_collection', 'bundle_has_schema_collection', 'bundle_has_spice_kernel_collection'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Checksum_Manifest" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Checksum_Manifest role="warning"/pds:Checksum_Manifest.record_delimiter</title>
        The value carriage-return line-feed for attribute Checksum_Manifest.record_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Checksum_Manifest/pds:parsing_standard_id">
      <sch:assert test=". = ('MD5Deep 4.n')">
        <title>pds:Checksum_Manifest/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Checksum_Manifest/pds:parsing_standard_id must be equal to the value 'MD5Deep 4.n'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Checksum_Manifest/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Checksum_Manifest/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Checksum_Manifest/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//pds:Citation_Information/pds:description">
      <sch:assert test="string-length(translate(., ' ', '')) &gt;= 1 and string-length(translate(., ' ','')) &lt;= 5000">
        <title>pds:Citation_Information/pds:description/description</title>
        The description in Citation_Information must be greater than 0 and less than 5000 bytes (not counting spaces).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Collection/pds:collection_type">
      <sch:assert test=". = ('Browse', 'Calibration', 'Context', 'Data', 'Document', 'Geometry', 'Miscellaneous', 'SPICE Kernel', 'XML Schema')">
        <title>pds:Collection/pds:collection_type/pds:collection_type</title>
        The attribute pds:Collection/pds:collection_type must be equal to one of the following values 'Browse', 'Calibration', 'Context', 'Data', 'Document', 'Geometry', 'Miscellaneous', 'SPICE Kernel', 'XML Schema'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association" role="warning">
      <sch:assert test="pds:reference_type != 'subclass_of'">
        <title>pds:DD_Association role="warning"/pds:DD_Association.reference_type</title>
        The value subclass_of for attribute DD_Association.reference_type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:reference_type != 'restriction_of'">
        <title>pds:DD_Association role="warning"/pds:DD_Association.reference_type</title>
        The value restriction_of for attribute DD_Association.reference_type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:reference_type != 'extension_of'">
        <title>pds:DD_Association role="warning"/pds:DD_Association.reference_type</title>
        The value extension_of for attribute DD_Association.reference_type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association/pds:local_identifier" role="warning">
      <sch:assert test="false()">
        <title>pds:DD_Association/pds:local_identifier role="warning"/pds:DD_Association.local_identifier</title>
        pds:DD_Association/pds:local_identifier is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association/pds:reference_type">
      <sch:assert test=". = ('attribute_of', 'component_of', 'extension_of', 'parent_of', 'restriction_of', 'subclass_of')">
        <title>pds:DD_Association/pds:reference_type/pds:reference_type</title>
        The attribute pds:DD_Association/pds:reference_type must be equal to one of the following values 'attribute_of', 'component_of', 'extension_of', 'parent_of', 'restriction_of', 'subclass_of'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association_External" role="warning">
      <sch:assert test="false()">
        <title>pds:DD_Association_External role="warning"/pds:DD_Association_External</title>
        pds:DD_Association_External is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association_External/pds:reference_type">
      <sch:assert test=". = ('attribute_of', 'component_of', 'extension_of', 'parent_of', 'restriction_of', 'subclass_of')">
        <title>pds:DD_Association_External/pds:reference_type/pds:reference_type</title>
        The attribute pds:DD_Association_External/pds:reference_type must be equal to one of the following values 'attribute_of', 'component_of', 'extension_of', 'parent_of', 'restriction_of', 'subclass_of'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('attribute_to_image', 'attribute_to_document')">
        <title>pds:DD_Attribute/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'attribute_to_image', 'attribute_to_document'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute_Full">
      <sch:assert test="if (pds:nillable_flag) then pds:nillable_flag = ('true', 'false') else true()">
        <title>pds:DD_Attribute_Full/pds:nillable_flag</title>
        The attribute pds:nillable_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute_Full/pds:attribute_concept">
      <sch:assert test=". = ('Address', 'Angle', 'Attribute', 'Bit', 'Checksum', 'Collection', 'Constant', 'Cosine', 'Count', 'DOI', 'Delimiter', 'Description', 'Deviation', 'Direction', 'Distance', 'Duration', 'Factor', 'Flag', 'Format', 'Group', 'Home', 'ID', 'Latitude', 'Length', 'List', 'Location', 'Logical', 'Longitude', 'Mask', 'Maximum', 'Mean', 'Median', 'Minimum', 'Name', 'Note', 'Number', 'Offset', 'Order', 'Parallel', 'Password', 'Path', 'Pattern', 'Pixel', 'Quaternion', 'Radius', 'Ratio', 'Reference', 'Resolution', 'Role', 'Rotation', 'Scale', 'Sequence', 'Set', 'Size', 'Status', 'Summary', 'Syntax', 'Temperature', 'Text', 'Title', 'Type', 'Unit', 'Unknown', 'Value', 'Vector')">
        <title>pds:DD_Attribute_Full/pds:attribute_concept/pds:attribute_concept</title>
        The attribute pds:DD_Attribute_Full/pds:attribute_concept must be equal to one of the following values 'Address', 'Angle', 'Attribute', 'Bit', 'Checksum', 'Collection', 'Constant', 'Cosine', 'Count', 'DOI', 'Delimiter', 'Description', 'Deviation', 'Direction', 'Distance', 'Duration', 'Factor', 'Flag', 'Format', 'Group', 'Home', 'ID', 'Latitude', 'Length', 'List', 'Location', 'Logical', 'Longitude', 'Mask', 'Maximum', 'Mean', 'Median', 'Minimum', 'Name', 'Note', 'Number', 'Offset', 'Order', 'Parallel', 'Password', 'Path', 'Pattern', 'Pixel', 'Quaternion', 'Radius', 'Ratio', 'Reference', 'Resolution', 'Role', 'Rotation', 'Scale', 'Sequence', 'Set', 'Size', 'Status', 'Summary', 'Syntax', 'Temperature', 'Text', 'Title', 'Type', 'Unit', 'Unknown', 'Value', 'Vector'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute_Full/pds:registration_authority_id">
      <sch:assert test=". = ('0001_NASA_PDS_1')">
        <title>pds:DD_Attribute_Full/pds:registration_authority_id/pds:registration_authority_id</title>
        The attribute pds:DD_Attribute_Full/pds:registration_authority_id must be equal to the value '0001_NASA_PDS_1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute_Full/pds:steward_id">
      <sch:assert test=". = ('atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn')">
        <title>pds:DD_Attribute_Full/pds:steward_id/pds:steward_id</title>
        The attribute pds:DD_Attribute_Full/pds:steward_id must be equal to one of the following values 'atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute_Full/pds:type">
      <sch:assert test=". = ('PDS3', 'PDS4')">
        <title>pds:DD_Attribute_Full/pds:type/pds:type</title>
        The attribute pds:DD_Attribute_Full/pds:type must be equal to one of the following values 'PDS3', 'PDS4'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Class/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('class_to_image', 'class_to_document')">
        <title>pds:DD_Class/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'class_to_image', 'class_to_document'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Class_Full">
      <sch:assert test="if (pds:abstract_flag) then pds:abstract_flag = ('true', 'false') else true()">
        <title>pds:DD_Class_Full/pds:abstract_flag</title>
        The attribute pds:abstract_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (pds:element_flag) then pds:element_flag = ('true', 'false') else true()">
        <title>pds:DD_Class_Full/pds:element_flag</title>
        The attribute pds:element_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Class_Full/pds:steward_id">
      <sch:assert test=". = ('atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn')">
        <title>pds:DD_Class_Full/pds:steward_id/pds:steward_id</title>
        The attribute pds:DD_Class_Full/pds:steward_id must be equal to one of the following values 'atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Class_Full/pds:type">
      <sch:assert test=". = ('PDS3', 'PDS4')">
        <title>pds:DD_Class_Full/pds:type/pds:type</title>
        The attribute pds:DD_Class_Full/pds:type must be equal to one of the following values 'PDS3', 'PDS4'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Rule_Statement/pds:rule_type">
      <sch:assert test=". = ('Assert', 'Assert Every', 'Assert If', 'Report')">
        <title>pds:DD_Rule_Statement/pds:rule_type/pds:rule_type</title>
        The attribute pds:DD_Rule_Statement/pds:rule_type must be equal to one of the following values 'Assert', 'Assert Every', 'Assert If', 'Report'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain">
      <sch:let name="unitOfMeasureRef" value="pds:unit_of_measure_type"/>
      <sch:let name="specUnitIDRef" value="pds:specified_unit_id"/>
      <sch:let name="combinedUnitRef" value="($unitOfMeasureRef and $specUnitIDRef)"/>
      <sch:assert test="if ($specUnitIDRef) then ($unitOfMeasureRef) else true()">
        <title>pds:DD_Value_Domain/unit_of_measure_type</title>
        Must specify attribute 'unit_of_measure_type' if attribute 'specified_unit_id' has been specified.</sch:assert>
      <sch:assert test="if (pds:enumeration_flag) then pds:enumeration_flag = ('true', 'false') else true()">
        <title>pds:DD_Value_Domain/pds:enumeration_flag</title>
        The attribute pds:enumeration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain/pds:unit_of_measure_type" role="warning">
      <sch:assert test=". != ('Units_of_Radiance')">
        <title>pds:DD_Value_Domain/pds:unit_of_measure_type/unit_of_measure_type</title>
        The value 'W*m**-2*sr**-1' in 'Units_of_Radiance' in the attribute pds:unit_of_measure_type is deprecated and must not be used'.</sch:assert>
      <sch:assert test=". = ('Units_of_Acceleration', 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Current', 'Units_of_Energy', 'Units_of_Force', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Gmass', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pixel_Resolution_Angular', 'Units_of_Pixel_Resolution_Linear', 'Units_of_Pixel_Resolution_Map', 'Units_of_Pixel_Scale_Angular', 'Units_of_Pixel_Scale_Linear', 'Units_of_Pixel_Scale_Map', 'Units_of_Power', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Spectral_Irradiance', 'Units_of_Spectral_Radiance', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume', 'Units_of_Wavenumber')">
        <title>pds:DD_Value_Domain/pds:unit_of_measure_type/pds:unit_of_measure_type</title>
        The attribute pds:DD_Value_Domain/pds:unit_of_measure_type must be equal to one of the following values 'Units_of_Acceleration', 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Current', 'Units_of_Energy', 'Units_of_Force', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Gmass', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pixel_Resolution_Angular', 'Units_of_Pixel_Resolution_Linear', 'Units_of_Pixel_Resolution_Map', 'Units_of_Pixel_Scale_Angular', 'Units_of_Pixel_Scale_Linear', 'Units_of_Pixel_Scale_Map', 'Units_of_Power', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Spectral_Irradiance', 'Units_of_Spectral_Radiance', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume', 'Units_of_Wavenumber'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain/pds:value_data_type">
      <sch:assert test=". = ('ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Collapsed', 'UTF8_Text_Preserved', 'Vector_Cartesian_3', 'Vector_Cartesian_3_Acceleration', 'Vector_Cartesian_3_Pointing', 'Vector_Cartesian_3_Position', 'Vector_Cartesian_3_Velocity')">
        <title>pds:DD_Value_Domain/pds:value_data_type/pds:value_data_type</title>
        The attribute pds:DD_Value_Domain/pds:value_data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Collapsed', 'UTF8_Text_Preserved', 'Vector_Cartesian_3', 'Vector_Cartesian_3_Acceleration', 'Vector_Cartesian_3_Pointing', 'Vector_Cartesian_3_Position', 'Vector_Cartesian_3_Velocity'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Radiance')]" role="warning">
      <sch:assert test="if (pds:specified_unit_id) then not(pds:specified_unit_id = ('W*m**-2*sr**-1')) else true()">
        <title>pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Radiance')]/unit_of_measure_type</title>
        The value '<sch:value-of select="pds:specified_unit_id"/>' in 'Units_of_Radiance' in the attribute pds:unit_of_measure_type is deprecated and must not be used'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Spectral_Irradiance')]" role="warning">
      <sch:assert test="if (pds:specified_unit_id) then not(pds:specified_unit_id = ('SFU', 'W*m**-2*Hz**-1', 'W*m**-2*nm**-1', 'W*m**-3', 'uW*cm**-2*um**-1')) else true()">
        <title>pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Spectral_Irradiance')]/unit_of_measure_type</title>
        The value '<sch:value-of select="pds:specified_unit_id"/>' in 'Units_of_Spectral_Irradiance' in the attribute pds:unit_of_measure_type is deprecated and must not be used'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Spectral_Radiance')]" role="warning">
      <sch:assert test="if (pds:specified_unit_id) then not(pds:specified_unit_id = ('W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1, W*m**-3*sr**-1', 'uW*cm**-2*sr**-1*um**-1')) else true()">
        <title>pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Spectral_Radiance')]/unit_of_measure_type</title>
        The value '<sch:value-of select="pds:specified_unit_id"/>' in 'Units_of_Spectral_Radiance' in the attribute pds:unit_of_measure_type is deprecated and must not be used'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Wavenumber')]" role="warning">
      <sch:assert test="if (pds:specified_unit_id) then not(pds:specified_unit_id = ('cm**-1','m**-1','nm**-1')) else true()">
        <title>pds:DD_Value_Domain[pds:unit_of_measure_type = ('Units_of_Wavenumber')]/unit_of_measure_type</title>
        The value '<sch:value-of select="pds:specified_unit_id"/>' in 'Units_of_Wavenumber' in the attribute pds:unit_of_measure_type is deprecated and must not be used'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain_Full">
      <sch:assert test="if (pds:enumeration_flag) then pds:enumeration_flag = ('true', 'false') else true()">
        <title>pds:DD_Value_Domain_Full/pds:enumeration_flag</title>
        The attribute pds:enumeration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain_Full/pds:conceptual_domain">
      <sch:assert test=". = ('Boolean', 'Integer', 'Name', 'Numeric', 'Real', 'Short_String', 'Text', 'Time', 'Type', 'Unknown')">
        <title>pds:DD_Value_Domain_Full/pds:conceptual_domain/pds:conceptual_domain</title>
        The attribute pds:DD_Value_Domain_Full/pds:conceptual_domain must be equal to one of the following values 'Boolean', 'Integer', 'Name', 'Numeric', 'Real', 'Short_String', 'Text', 'Time', 'Type', 'Unknown'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain_Full/pds:unit_of_measure_type">
      <sch:assert test=". = ('Units_of_Acceleration', 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Current', 'Units_of_Energy', 'Units_of_Force', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Gmass', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pixel_Resolution_Angular', 'Units_of_Pixel_Resolution_Linear', 'Units_of_Pixel_Resolution_Map', 'Units_of_Pixel_Scale_Angular', 'Units_of_Pixel_Scale_Linear', 'Units_of_Pixel_Scale_Map', 'Units_of_Power', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Spectral_Irradiance', 'Units_of_Spectral_Radiance', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume', 'Units_of_Wavenumber')">
        <title>pds:DD_Value_Domain_Full/pds:unit_of_measure_type/pds:unit_of_measure_type</title>
        The attribute pds:DD_Value_Domain_Full/pds:unit_of_measure_type must be equal to one of the following values 'Units_of_Acceleration', 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Current', 'Units_of_Energy', 'Units_of_Force', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Gmass', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pixel_Resolution_Angular', 'Units_of_Pixel_Resolution_Linear', 'Units_of_Pixel_Resolution_Map', 'Units_of_Pixel_Scale_Angular', 'Units_of_Pixel_Scale_Linear', 'Units_of_Pixel_Scale_Map', 'Units_of_Power', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Spectral_Irradiance', 'Units_of_Spectral_Radiance', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume', 'Units_of_Wavenumber'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain_Full/pds:value_data_type">
      <sch:assert test=". = ('ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Collapsed', 'UTF8_Text_Preserved')">
        <title>pds:DD_Value_Domain_Full/pds:value_data_type/pds:value_data_type</title>
        The attribute pds:DD_Value_Domain_Full/pds:value_data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Collapsed', 'UTF8_Text_Preserved'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Data_Set_PDS3/pds:archive_status">
      <sch:assert test=". = ('ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED')">
        <title>pds:Data_Set_PDS3/pds:archive_status/pds:archive_status</title>
        The attribute pds:Data_Set_PDS3/pds:archive_status must be equal to one of the following values 'ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Data_Set_PDS3/pds:start_date_time" role="warning">
      <sch:assert test="false()">
        <title>pds:Data_Set_PDS3/pds:start_date_time role="warning"/pds:Data_Set_PDS3.start_date_time</title>
        pds:Data_Set_PDS3/pds:start_date_time is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Data_Set_PDS3/pds:stop_date_time" role="warning">
      <sch:assert test="false()">
        <title>pds:Data_Set_PDS3/pds:stop_date_time role="warning"/pds:Data_Set_PDS3.stop_date_time</title>
        pds:Data_Set_PDS3/pds:stop_date_time is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Display_2D_Image" role="warning">
      <sch:assert test="false()">
        <title>pds:Display_2D_Image role="warning"/pds:Display_2D_Image</title>
        pds:Display_2D_Image is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Display_2D_Image/pds:line_display_direction">
      <sch:assert test=". = ('Down', 'Up')">
        <title>pds:Display_2D_Image/pds:line_display_direction/pds:line_display_direction</title>
        The attribute pds:Display_2D_Image/pds:line_display_direction must be equal to one of the following values 'Down', 'Up'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Display_2D_Image/pds:sample_display_direction">
      <sch:assert test=". = ('Right')">
        <title>pds:Display_2D_Image/pds:sample_display_direction/pds:sample_display_direction</title>
        The attribute pds:Display_2D_Image/pds:sample_display_direction must be equal to the value 'Right'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Document_Edition/pds:language">
      <sch:assert test=". = ('English')">
        <title>pds:Document_Edition/pds:language/pds:language</title>
        The attribute pds:Document_Edition/pds:language must be equal to the value 'English'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Document_File" role="warning">
      <sch:assert test="pds:document_standard_id != 'HTML 2.0'">
        <title>pds:Document_File role="warning"/pds:Document_File.document_standard_id</title>
        The value HTML 2.0 for attribute Document_File.document_standard_id is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:document_standard_id != 'HTML 3.2'">
        <title>pds:Document_File role="warning"/pds:Document_File.document_standard_id</title>
        The value HTML 3.2 for attribute Document_File.document_standard_id is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:document_standard_id != 'HTML 4.0'">
        <title>pds:Document_File role="warning"/pds:Document_File.document_standard_id</title>
        The value HTML 4.0 for attribute Document_File.document_standard_id is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:document_standard_id != 'HTML 4.01'">
        <title>pds:Document_File role="warning"/pds:Document_File.document_standard_id</title>
        The value HTML 4.01 for attribute Document_File.document_standard_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Document_File/pds:document_standard_id">
      <sch:assert test=". = ('7-Bit ASCII Text', 'Encapsulated Postscript', 'GIF', 'HTML', 'HTML 2.0', 'HTML 3.2', 'HTML 4.0', 'HTML 4.01', 'JPEG', 'LaTEX', 'MPEG-4', 'Microsoft Excel', 'Microsoft Word', 'PDF', 'PDF/A', 'PNG', 'Postscript', 'Rich Text', 'TIFF', 'UTF-8 Text')">
        <title>pds:Document_File/pds:document_standard_id/pds:document_standard_id</title>
        The attribute pds:Document_File/pds:document_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'Encapsulated Postscript', 'GIF', 'HTML', 'HTML 2.0', 'HTML 3.2', 'HTML 4.0', 'HTML 4.01', 'JPEG', 'LaTEX', 'MPEG-4', 'Microsoft Excel', 'Microsoft Word', 'PDF', 'PDF/A', 'PNG', 'Postscript', 'Rich Text', 'TIFF', 'UTF-8 Text'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Document_Format" role="warning">
      <sch:assert test="pds:format_type != 'single file'">
        <title>pds:Document_Format role="warning"/pds:Document_Format.format_type</title>
        The value single file for attribute Document_Format.format_type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:format_type != 'multiple file'">
        <title>pds:Document_Format role="warning"/pds:Document_Format.format_type</title>
        The value multiple file for attribute Document_Format.format_type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Element_Array/pds:data_type">
      <sch:assert test=". = ('ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8')">
        <title>pds:Element_Array/pds:data_type/pds:data_type</title>
        The attribute pds:Element_Array/pds:data_type must be equal to one of the following values 'ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Audio/pds:encoding_standard_id">
      <sch:assert test=". = ('M4A/AAC', 'WAV')">
        <title>pds:Encoded_Audio/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Audio/pds:encoding_standard_id must be equal to one of the following values 'M4A/AAC', 'WAV'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Binary" role="warning">
      <sch:assert test="pds:encoding_standard_id != 'CCSDS Space Communications Protocols'">
        <title>pds:Encoded_Binary role="warning"/pds:Encoded_Binary.encoding_standard_id</title>
        The value CCSDS Space Communications Protocols for attribute Encoded_Binary.encoding_standard_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Binary/pds:encoding_standard_id">
      <sch:assert test=". = ('CCSDS Space Communications Protocols')">
        <title>pds:Encoded_Binary/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Binary/pds:encoding_standard_id must be equal to the value 'CCSDS Space Communications Protocols'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Byte_Stream/pds:object_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Encoded_Byte_Stream/pds:object_length/pds:object_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Byte_Stream/pds:offset">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Encoded_Byte_Stream/pds:offset/pds:offset</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Header/pds:encoding_standard_id">
      <sch:assert test=". = ('TIFF')">
        <title>pds:Encoded_Header/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Header/pds:encoding_standard_id must be equal to the value 'TIFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Image/pds:encoding_standard_id">
      <sch:assert test=". = ('GIF', 'J2C', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF')">
        <title>pds:Encoded_Image/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Image/pds:encoding_standard_id must be equal to one of the following values 'GIF', 'J2C', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Native/pds:encoding_standard_id">
      <sch:assert test=". = ('SEED 2.4')">
        <title>pds:Encoded_Native/pds:encoding_standard_id/pds:encoding_standard_id</title>
        The attribute pds:Encoded_Native/pds:encoding_standard_id must be equal to the value 'SEED 2.4'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Facility/pds:type">
      <sch:assert test=". = ('Laboratory', 'Observatory')">
        <title>pds:Facility/pds:type/pds:type</title>
        The attribute pds:Facility/pds:type must be equal to one of the following values 'Laboratory', 'Observatory'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary">
      <sch:let name="isPresent" value=" boolean(pds:Packed_Data_Fields)"/>
      <sch:let name="Bit-1_values" value="('SignedByte', 'UnsignedByte')"/>
      <sch:let name="Bit-2_values" value="('SignedLSB2', 'SignedMSB2', 'UnsignedLSB2', 'UnsignedMSB2')"/>
      <sch:let name="Bit-4_values" value="('IEEE754LSBSingle', 'IEEE754MSBSingle', 'SignedLSB4', 'SignedMSB4', 'UnsignedLSB4', 'UnsignedMSB4')"/>
      <sch:let name="Bit-8_values" value="('ComplexLSB8', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754MSBDouble', 'SignedLSB8', 'SignedMSB8', 'UnsignedLSB8', 'UnsignedMSB8')"/>
      <sch:let name="Bit-16_values" value="('ComplexLSB16', 'ComplexMSB16')"/>
      <sch:let name="Bit-1_test" value="(some $ref in (pds:data_type) satisfies $ref = $Bit-1_values)"/>
      <sch:let name="Bit-2_test" value="(some $ref in (pds:data_type) satisfies $ref = $Bit-2_values)"/>
      <sch:let name="Bit-4_test" value="(some $ref in (pds:data_type) satisfies $ref = $Bit-4_values)"/>
      <sch:let name="Bit-8_test" value="(some $ref in (pds:data_type) satisfies $ref = $Bit-8_values)"/>
      <sch:let name="Bit-16_test" value="(some $ref in (pds:data_type) satisfies $ref = $Bit-16_values)"/>
      <sch:assert test="if ($isPresent) then (pds:data_type = 'UnsignedBitString') else true()">
        <title>pds:Field_Binary/field_length</title>
        When Field_Binary has a child Packed_Data_Fields, "pds:Field_Binary/pds:data_type" must have a value of 'UnsignedBitString'</sch:assert>
      <sch:assert test="if (not($isPresent) and ($Bit-1_test)) then (pds:field_length = 1) else true()">
        <title>pds:Field_Binary/field_length</title>
        The attribute pds:data_type ('<sch:value-of select="pds:data_type" />') is invalid with respect to the value of pds:field_length ('<sch:value-of select="pds:field_length" />')</sch:assert>
      <sch:assert test="if (not($isPresent) and ($Bit-2_test)) then (pds:field_length = 2) else true()">
        <title>pds:Field_Binary/field_length</title>
        The attribute pds:data_type ('<sch:value-of select="pds:data_type" />') is invalid with respect to the value of pds:field_length ('<sch:value-of select="pds:field_length" />')</sch:assert>
      <sch:assert test="if (not($isPresent) and ($Bit-4_test)) then (pds:field_length = 4) else true()">
        <title>pds:Field_Binary/field_length</title>
        The attribute pds:data_type ('<sch:value-of select="pds:data_type" />') is invalid with respect to the value of pds:field_length ('<sch:value-of select="pds:field_length" />')</sch:assert>
      <sch:assert test="if (not($isPresent) and ($Bit-8_test)) then (pds:field_length = 8) else true()">
        <title>pds:Field_Binary/field_length</title>
        The attribute pds:data_type ('<sch:value-of select="pds:data_type" />') is invalid with respect to the value of pds:field_length ('<sch:value-of select="pds:field_length" />')</sch:assert>
      <sch:assert test="if (not($isPresent) and ($Bit-16_test)) then (pds:field_length = 16) else true()">
        <title>pds:Field_Binary/field_length</title>
        The attribute pds:data_type ('<sch:value-of select="pds:data_type" />') is invalid with respect to the value of pds:field_length ('<sch:value-of select="pds:field_length" />')</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary/pds:data_type">
      <sch:assert test=". = ('ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UTF8_String', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8')">
        <title>pds:Field_Binary/pds:data_type/pds:data_type</title>
        The attribute pds:Field_Binary/pds:data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UTF8_String', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary/pds:field_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Field_Binary/pds:field_length/pds:field_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary/pds:field_location">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Field_Binary/pds:field_location/pds:field_location</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary/pds:unit" role="warning">
      <sch:let name="unit_radiance" value="'W*m**-2*sr**-1'"/>
      <sch:let name="unit_spectral_irradiance" value="'SFU', 'W*m**-2*Hz**-1', 'W*m**-2*nm**-1', 'W*m**-3', 'uW*cm**-2*um**-1'"/>
      <sch:let name="unit_spectral_radiance" value="'W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'uW*cm**-2*sr**-1*um**-1'"/>
      <sch:let name="unit_wavenumber" value="('cm**-1','m**-1','nm**-1')"/>
      <sch:assert test="if (.) then not(. = ($unit_radiance, $unit_spectral_irradiance, $unit_spectral_radiance, $unit_wavenumber)) else true()">
        <title>pds:Field_Binary/pds:unit/unit</title>
        The value '<sch:value-of select="."/>' in the attribute 'unit' is deprecated and must not be used'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Bit/pds:data_type">
      <sch:assert test=". = ('SignedBitString', 'UnsignedBitString')">
        <title>pds:Field_Bit/pds:data_type/pds:data_type</title>
        The attribute pds:Field_Bit/pds:data_type must be equal to one of the following values 'SignedBitString', 'UnsignedBitString'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Bit/pds:start_bit" role="warning">
      <sch:assert test="false()">
        <title>pds:Field_Bit/pds:start_bit role="warning"/pds:Field_Bit.start_bit</title>
        pds:Field_Bit/pds:start_bit is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Bit/pds:stop_bit" role="warning">
      <sch:assert test="false()">
        <title>pds:Field_Bit/pds:stop_bit role="warning"/pds:Field_Bit.stop_bit</title>
        pds:Field_Bit/pds:stop_bit is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character/pds:data_type">
      <sch:assert test=". = ('ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String')">
        <title>pds:Field_Character/pds:data_type/pds:data_type</title>
        The attribute pds:Field_Character/pds:data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character/pds:field_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Field_Character/pds:field_length/pds:field_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character/pds:field_location">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Field_Character/pds:field_location/pds:field_location</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character/pds:validation_format">
      <sch:assert test=". = ../pds:field_format">
        <title>pds:Field_Character/pds:validation_format/validation_format</title>
        If a validation_format is present, its value must be equal to the value in field_format</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Delimited/pds:data_type">
      <sch:assert test=". = ('ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String')">
        <title>pds:Field_Delimited/pds:data_type/pds:data_type</title>
        The attribute pds:Field_Delimited/pds:data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_BibCode', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_DOY_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_Time_YMD_UTC', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Delimited/pds:maximum_field_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Field_Delimited/pds:maximum_field_length/pds:maximum_field_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:File/pds:file_size">
      <sch:assert test="@unit = ('byte')">
        <title>pds:File/pds:file_size/pds:file_size</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:File_Area_Update" role="warning">
      <sch:assert test="false()">
        <title>pds:File_Area_Update role="warning"/pds:File_Area_Update</title>
        pds:File_Area_Update is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Binary">
      <sch:let name="num_fields" value="(pds:fields)"/>
      <sch:let name="num_groups" value="(pds:groups)"/>
      <sch:assert test="$num_fields > '0' or $num_groups > '0'">
        <title>pds:Group_Field_Binary/fields</title>
        The values for 'fields' and 'groups' must not both be '0'.  The number of fields found: (<sch:value-of select="$num_fields"/>).  The number of groups found: (<sch:value-of select="$num_groups"/>).</sch:assert>
      <sch:assert test="$num_fields = count(pds:Field_Binary)">
        <title>pds:Group_Field_Binary/fields</title>
        The number of Field_Binary elements (<sch:value-of select="count(pds:Field_Binary)"/>) does not match the number found in the 'fields' attribute (<sch:value-of select="$num_fields"/>).</sch:assert>
      <sch:assert test="$num_groups = count(pds:Group_Field_Binary)">
        <title>pds:Group_Field_Binary/fields</title>
        The number of Group_Field_Binary elements (<sch:value-of select="count(pds:Group_Field_Binary)"/>) does not match the number found in the 'groups' attribute (<sch:value-of select="$num_groups"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Binary/pds:group_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Group_Field_Binary/pds:group_length/pds:group_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Binary/pds:group_location">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Group_Field_Binary/pds:group_location/pds:group_location</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Character">
      <sch:let name="num_fields" value="(pds:fields)"/>
      <sch:let name="num_groups" value="(pds:groups)"/>
      <sch:assert test="$num_fields > '0' or $num_groups > '0'">
        <title>pds:Group_Field_Character/fields</title>
        The values for 'fields' and 'groups' must not both be '0'.  The number of fields found: (<sch:value-of select="$num_fields"/>).  The number of groups found: (<sch:value-of select="$num_groups"/>).</sch:assert>
      <sch:assert test="$num_fields = count(pds:Field_Character)">
        <title>pds:Group_Field_Character/fields</title>
        The number of Field_Character elements (<sch:value-of select="count(pds:Field_Character)"/>) does not match the number found in the 'fields' attribute (<sch:value-of select="$num_fields"/>).</sch:assert>
      <sch:assert test="$num_groups = count(pds:Group_Field_Character)">
        <title>pds:Group_Field_Character/fields</title>
        The number of Group_Field_Character elements (<sch:value-of select="count(pds:Group_Field_Character)"/>) does not match the number found in the 'groups' attribute (<sch:value-of select="$num_groups"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Character/pds:group_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Group_Field_Character/pds:group_length/pds:group_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Character/pds:group_location">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Group_Field_Character/pds:group_location/pds:group_location</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Group_Field_Delimited">
      <sch:let name="num_fields" value="(pds:fields)"/>
      <sch:let name="num_groups" value="(pds:groups)"/>
      <sch:assert test="$num_fields > '0' or $num_groups > '0'">
        <title>pds:Group_Field_Delimited/fields</title>
        The values for 'fields' and 'groups' must not both be '0'.  The number of fields found: (<sch:value-of select="$num_fields"/>).  The number of groups found: (<sch:value-of select="$num_groups"/>).</sch:assert>
      <sch:assert test="$num_fields = count(pds:Field_Delimited)">
        <title>pds:Group_Field_Delimited/fields</title>
        The number of Field_Delimited elements (<sch:value-of select="count(pds:Field_Delimited)"/>) does not match the number found in the 'fields' attribute (<sch:value-of select="$num_fields"/>).</sch:assert>
      <sch:assert test="$num_groups = count(pds:Group_Field_Delimited)">
        <title>pds:Group_Field_Delimited/fields</title>
        The number of Group_Field_Delimited elements (<sch:value-of select="count(pds:Group_Field_Delimited)"/>) does not match the number found in the 'groups' attribute (<sch:value-of select="$num_groups"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Header/pds:object_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Header/pds:object_length/pds:object_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Header/pds:parsing_standard_id">
      <sch:assert test=". = ('7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2')">
        <title>pds:Header/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Header/pds:parsing_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'FITS 3.0', 'FITS 4.0', 'ISIS2', 'ISIS2 History Label', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'TIFF 6.0', 'UTF-8 Text', 'VICAR1', 'VICAR2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Identification_Area">
      <sch:let name="urn_nasa" value="'urn:nasa:pds:'"/>
      <sch:let name="urn_esa" value="'urn:esa:psa:'"/>
      <sch:let name="urn_ros" value="'urn:ros:rssa:'"/>
      <sch:let name="urn_jaxa" value="'urn:jaxa:darts:'"/>
      <sch:let name="urn_isro" value="'urn:isro:isda:'"/>
      <sch:assert test="pds:product_class = local-name(/*)">
        <title>pds:Identification_Area/logical_identifier</title>
        The attribute pds:product_class must match parent product class of '<sch:value-of select="local-name(/*)" />'.</sch:assert>
      <sch:assert test="pds:logical_identifier eq lower-case(pds:logical_identifier)">
        <title>pds:Identification_Area/logical_identifier</title>
        The value of the attribute logical_identifier must only contain lower-case letters</sch:assert>
      <sch:assert test="if (pds:logical_identifier) then starts-with(pds:logical_identifier, $urn_nasa) or starts-with(pds:logical_identifier, $urn_esa) or starts-with(pds:logical_identifier, $urn_jaxa) or starts-with(pds:logical_identifier, $urn_ros) or starts-with(pds:logical_identifier, $urn_isro) else true()">
        <title>pds:Identification_Area/logical_identifier</title>
        The value of the attribute logical_identifier must start with either: <sch:value-of select="$urn_nasa"/> or <sch:value-of select="$urn_esa"/> or <sch:value-of select="$urn_jaxa"/> or <sch:value-of select="$urn_ros"/> or <sch:value-of select="$urn_isro"/></sch:assert>
      <sch:assert test="if (pds:logical_identifier) then not(contains(pds:logical_identifier,'::')) else true()">
        <title>pds:Identification_Area/logical_identifier</title>
        The value of the attribute logical_identifier must not include a value that contains '::'</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Identification_Area/pds:information_model_version">
      <sch:assert test=". = ('1.18.0.0')">
        <title>pds:Identification_Area/pds:information_model_version/pds:information_model_version</title>
        The attribute pds:Identification_Area/pds:information_model_version must be equal to the value '1.18.0.0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Identification_Area/pds:product_class">
      <sch:assert test=". = ('Product_AIP', 'Product_Ancillary', 'Product_Attribute_Definition', 'Product_Browse', 'Product_Bundle', 'Product_Class_Definition', 'Product_Collection', 'Product_Context', 'Product_DIP', 'Product_DIP_Deep_Archive', 'Product_Data_Set_PDS3', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3', 'Product_Metadata_Supplemental', 'Product_Mission_PDS3', 'Product_Native', 'Product_Observational', 'Product_Proxy_PDS3', 'Product_SIP', 'Product_SIP_Deep_Archive', 'Product_SPICE_Kernel', 'Product_Service', 'Product_Software', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Thumbnail', 'Product_Update', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_XML_Schema', 'Product_Zipped')">
        <title>pds:Identification_Area/pds:product_class/pds:product_class</title>
        The attribute pds:Identification_Area/pds:product_class must be equal to one of the following values 'Product_AIP', 'Product_Ancillary', 'Product_Attribute_Definition', 'Product_Browse', 'Product_Bundle', 'Product_Class_Definition', 'Product_Collection', 'Product_Context', 'Product_DIP', 'Product_DIP_Deep_Archive', 'Product_Data_Set_PDS3', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3', 'Product_Metadata_Supplemental', 'Product_Mission_PDS3', 'Product_Native', 'Product_Observational', 'Product_Proxy_PDS3', 'Product_SIP', 'Product_SIP_Deep_Archive', 'Product_SPICE_Kernel', 'Product_Service', 'Product_Software', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Thumbnail', 'Product_Update', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_XML_Schema', 'Product_Zipped'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Information_Package_Component_Deep_Archive" role="warning">
      <sch:assert test="pds:checksum_type != 'MD5Deep 4.n'">
        <title>pds:Information_Package_Component_Deep_Archive role="warning"/pds:Information_Package_Component_Deep_Archive.checksum_type</title>
        The value MD5Deep 4.n for attribute Information_Package_Component_Deep_Archive.checksum_type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Information_Package_Component_Deep_Archive/pds:Internal_Reference">
      <sch:assert test="pds:lidvid_reference">
        <title>pds:Information_Package_Component_Deep_Archive/pds:Internal_Reference/lidvid_reference</title>
        In Product_SIP_Deep_Archive both Internal_Reference and lidvid_reference are required.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Information_Package_Component_Deep_Archive/pds:checksum_type">
      <sch:assert test=". = ('MD5', 'MD5Deep 4.n')">
        <title>pds:Information_Package_Component_Deep_Archive/pds:checksum_type/pds:checksum_type</title>
        The attribute pds:Information_Package_Component_Deep_Archive/pds:checksum_type must be equal to one of the following values 'MD5', 'MD5Deep 4.n'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Ingest_LDD/pds:dictionary_type">
      <sch:assert test=". = ('Common', 'Discipline', 'Mission')">
        <title>pds:Ingest_LDD/pds:dictionary_type/pds:dictionary_type</title>
        The attribute pds:Ingest_LDD/pds:dictionary_type must be equal to one of the following values 'Common', 'Discipline', 'Mission'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument/pds:subtype" role="warning">
      <sch:assert test="false()">
        <title>pds:Instrument/pds:subtype role="warning"/pds:Instrument.subtype</title>
        pds:Instrument/pds:subtype is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument/pds:type">
      <sch:assert test=". = ('Accelerometer', 'Alpha Particle Detector', 'Alpha Particle X-Ray Spectrometer', 'Altimeter', 'Anemometer', 'Atmospheric Sciences', 'Atomic Force Microscope', 'Barometer', 'Biology Experiments', 'Bolometer', 'Camera', 'Cosmic Ray Detector', 'Drilling Tool', 'Dust', 'Dust Detector', 'Electrical Probe', 'Energetic Particle Detector', 'Gamma Ray Detector', 'Gas Analyzer', 'Gravimeter', 'Grinding Tool', 'Hygrometer', 'Imager', 'Imaging Spectrometer', 'Inertial Measurement Unit', 'Infrared Spectrometer', 'Interferometer', 'Laser Induced Breakdown Spectrometer', 'Magnetometer', 'Mass Spectrometer', 'Microscope', 'Microwave Spectrometer', 'Moessbauer Spectrometer', 'Naked Eye', 'Neutral Particle Detector', 'Neutron Detector', 'Particle Detector', 'Photometer', 'Plasma Analyzer', 'Plasma Detector', 'Plasma Wave Spectrometer', 'Polarimeter', 'Radar', 'Radio Science', 'Radio Spectrometer', 'Radio Telescope', 'Radio-Radar', 'Radiometer', 'Reflectometer', 'Regolith Properties', 'Robotic Arm', 'Seismometer', 'Small Bodies Sciences', 'Spectrograph', 'Spectrograph Imager', 'Spectrometer', 'Thermal Imager', 'Thermal Probe', 'Thermometer', 'Ultraviolet Spectrometer', 'Weather Station', 'Wet Chemistry Laboratory', 'X-ray Detector', 'X-ray Diffraction Spectrometer', 'X-ray Fluorescence Spectrometer')">
        <title>pds:Instrument/pds:type/pds:type</title>
        The attribute pds:Instrument/pds:type must be equal to one of the following values 'Accelerometer', 'Alpha Particle Detector', 'Alpha Particle X-Ray Spectrometer', 'Altimeter', 'Anemometer', 'Atmospheric Sciences', 'Atomic Force Microscope', 'Barometer', 'Biology Experiments', 'Bolometer', 'Camera', 'Cosmic Ray Detector', 'Drilling Tool', 'Dust', 'Dust Detector', 'Electrical Probe', 'Energetic Particle Detector', 'Gamma Ray Detector', 'Gas Analyzer', 'Gravimeter', 'Grinding Tool', 'Hygrometer', 'Imager', 'Imaging Spectrometer', 'Inertial Measurement Unit', 'Infrared Spectrometer', 'Interferometer', 'Laser Induced Breakdown Spectrometer', 'Magnetometer', 'Mass Spectrometer', 'Microscope', 'Microwave Spectrometer', 'Moessbauer Spectrometer', 'Naked Eye', 'Neutral Particle Detector', 'Neutron Detector', 'Particle Detector', 'Photometer', 'Plasma Analyzer', 'Plasma Detector', 'Plasma Wave Spectrometer', 'Polarimeter', 'Radar', 'Radio Science', 'Radio Spectrometer', 'Radio Telescope', 'Radio-Radar', 'Radiometer', 'Reflectometer', 'Regolith Properties', 'Robotic Arm', 'Seismometer', 'Small Bodies Sciences', 'Spectrograph', 'Spectrograph Imager', 'Spectrometer', 'Thermal Imager', 'Thermal Probe', 'Thermometer', 'Ultraviolet Spectrometer', 'Weather Station', 'Wet Chemistry Laboratory', 'X-ray Detector', 'X-ray Diffraction Spectrometer', 'X-ray Fluorescence Spectrometer'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument/pds:type" role="warning">
      <sch:assert test="false()">
        <title>pds:Instrument/pds:type role="warning"/pds:Instrument.type</title>
        pds:Instrument/pds:type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument_Host" role="warning">
      <sch:assert test="pds:type != 'Earth Based'">
        <title>pds:Instrument_Host role="warning"/pds:Instrument_Host.type.Earth Based</title>
        The value Earth Based for attribute Instrument_Host.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Earth-based'">
        <title>pds:Instrument_Host role="warning"/pds:Instrument_Host.type.Earth Based</title>
        The value Earth-based for attribute Instrument_Host.type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument_Host/pds:instrument_host_version_id" role="warning">
      <sch:assert test="false()">
        <title>pds:Instrument_Host/pds:instrument_host_version_id role="warning"/pds:Instrument_Host.instrument_host_version_id</title>
        pds:Instrument_Host/pds:instrument_host_version_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument_Host/pds:type">
      <sch:assert test=". = ('Aircraft', 'Balloon', 'Earth Based', 'Earth-based', 'Lander', 'Rover', 'Spacecraft', 'Suborbital Rocket')">
        <title>pds:Instrument_Host/pds:type/pds:type</title>
        The attribute pds:Instrument_Host/pds:type must be equal to one of the following values 'Aircraft', 'Balloon', 'Earth Based', 'Earth-based', 'Lander', 'Rover', 'Spacecraft', 'Suborbital Rocket'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument_Host/pds:version_id" role="warning">
      <sch:assert test="false()">
        <title>pds:Instrument_Host/pds:version_id role="warning"/pds:Instrument_Host.version_id</title>
        pds:Instrument_Host/pds:version_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Internal_Reference">
      <sch:let name="lid_num_colons" value="string-length(pds:lid_reference) - string-length(translate(pds:lid_reference, ':', ''))"/>
      <sch:let name="lidvid_num_colons" value="string-length(pds:lidvid_reference) - string-length(translate(pds:lidvid_reference, ':', ''))"/>
      <sch:let name="lid_min_required_colons" value="3"/>
      <sch:let name="lid_max_required_colons" value="5"/>
      <sch:let name="lidvid_min_required_colons" value="5"/>
      <sch:let name="lidvid_max_required_colons" value="7"/>
      <sch:let name="urn_nasa" value="'urn:nasa:pds:'"/>
      <sch:let name="urn_esa" value="'urn:esa:psa:'"/>
      <sch:let name="urn_ros" value="'urn:ros:rssa:'"/>
      <sch:let name="urn_jaxa" value="'urn:jaxa:darts:'"/>
      <sch:let name="urn_isro" value="'urn:isro:isda:'"/>
      <sch:assert test="if (pds:lid_reference) then not(contains(pds:lid_reference,'::')) else true()">
        <title>pds:Internal_Reference/lid_reference</title>
        The value of the attribute lid_reference must not include a value that contains '::' followed by version id</sch:assert>
      <sch:assert test="if (pds:lid_reference) then (($lid_num_colons &gt;= $lid_min_required_colons) and ($lid_num_colons &lt;= $lid_max_required_colons)) else true()">
        <title>pds:Internal_Reference/lid_reference</title>
        The number of colons found in lid_reference: (<sch:value-of select="$lid_num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$lid_min_required_colons"/>:<sch:value-of select="$lid_max_required_colons"/>).</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then (($lidvid_num_colons &gt;= $lidvid_min_required_colons) and ($lidvid_num_colons &lt;= $lidvid_max_required_colons)) else true()">
        <title>pds:Internal_Reference/lidvid_reference</title>
        The number of colons found in lidvid_reference: (<sch:value-of select="$lidvid_num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$lidvid_min_required_colons"/>:<sch:value-of select="$lidvid_max_required_colons"/>).</sch:assert>
      <sch:assert test="if (pds:lid_reference) then starts-with(pds:lid_reference, $urn_nasa) or starts-with(pds:lid_reference, $urn_esa) or starts-with(pds:lid_reference, $urn_jaxa) or starts-with(pds:lid_reference, $urn_ros) or starts-with(pds:lid_reference, $urn_isro) else true()">
        <title>pds:Internal_Reference/lid_reference</title>
        The value of the attribute lid_reference must start with either: <sch:value-of select="$urn_nasa"/> or <sch:value-of select="$urn_esa"/> or <sch:value-of select="$urn_jaxa"/> or <sch:value-of select="$urn_ros"/> or <sch:value-of select="$urn_isro"/></sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then starts-with(pds:lidvid_reference, $urn_nasa) or starts-with(pds:lidvid_reference, $urn_esa) or starts-with(pds:lidvid_reference, $urn_jaxa) or starts-with(pds:lidvid_reference, $urn_ros) or starts-with(pds:lidvid_reference, $urn_isro) else true()">
        <title>pds:Internal_Reference/lidvid_reference</title>
        The value of the attribute lidvid_reference must start with either: <sch:value-of select="$urn_nasa"/> or <sch:value-of select="$urn_esa"/> or <sch:value-of select="$urn_jaxa"/> or <sch:value-of select="$urn_ros"/> or <sch:value-of select="$urn_isro"/></sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()">
        <title>pds:Internal_Reference/lidvid_reference</title>
        The value of the attribute lidvid_reference must include a value that contains '::' followed by version id</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Internal_Reference" role="warning">
      <sch:assert test="pds:reference_type != 'is_airborne'">
        <title>pds:Internal_Reference role="warning"/pds:Internal_Reference.reference_type</title>
        The value is_airborne for attribute Internal_Reference.reference_type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory">
      <sch:assert test="(count(pds:Record_Delimited/pds:Field_Delimited) eq 2)">
        <title>pds:Inventory/Inventory</title>
        Inventory.Field_Delimited does not match the expected number of instances</sch:assert>
      <sch:assert test="pds:offset eq '0'">
        <title>pds:Inventory/offset</title>
        Inventory.offset must have a value of '0'</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Inventory role="warning"/pds:Inventory.record_delimiter</title>
        The value carriage-return line-feed for attribute Inventory.record_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'comma'">
        <title>pds:Inventory role="warning"/pds:Inventory.record_delimiter</title>
        The value comma for attribute Inventory.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'horizontal tab'">
        <title>pds:Inventory role="warning"/pds:Inventory.record_delimiter</title>
        The value horizontal tab for attribute Inventory.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'semicolon'">
        <title>pds:Inventory role="warning"/pds:Inventory.record_delimiter</title>
        The value semicolon for attribute Inventory.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'vertical bar'">
        <title>pds:Inventory role="warning"/pds:Inventory.record_delimiter</title>
        The value vertical bar for attribute Inventory.field_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:Record_Delimited">
      <sch:assert test="pds:fields eq '2'">
        <title>pds:Inventory/pds:Record_Delimited/name</title>
        The attribute pds:fields must be equal to '2'.</sch:assert>
      <sch:assert test="pds:groups eq '0'">
        <title>pds:Inventory/pds:Record_Delimited/name</title>
        The attribute pds:groups must be equal to '0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]">
      <sch:assert test="pds:field_number eq '1'">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]/field_number</title>
        The first field of an Inventory must have field_number set to 1.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '1'">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]/maximum_field_length</title>
        The first field of an Inventory must have maximum_field_length set to 1.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]/data_type</title>
        The first field of an Inventory must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'Member Status'">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]/name</title>
        The first field of an Inventory must have name set to 'Member Status'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]">
      <sch:assert test="pds:field_number eq '2'">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]/field_number</title>
        The second field of an Inventory must have field_number set to 2.</sch:assert>
      <sch:assert test="(pds:data_type eq 'ASCII_LID') or (pds:data_type eq 'ASCII_LIDVID') or (pds:data_type eq 'ASCII_LIDVID_LID')">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]/data_type</title>
        The second field of an Inventory must have data_type set to either 'ASCII_LID' or 'ASCII_LIDVID' or 'ASCII_LIDVID_LID'.</sch:assert>
      <sch:assert test="(pds:name eq 'LIDVID_LID')">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]/name</title>
        The second field of an Inventory must have name set to 'LIDVID_LID'.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]/name</title>
        The second field of an Inventory must have maximum_field_length set to 255.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:field_delimiter">
      <sch:assert test=". = ('Comma')">
        <title>pds:Inventory/pds:field_delimiter/field_delimiter</title>
        The attribute pds:Inventory/pds:field_delimiter must ONLY be equal to the value 'Comma'.</sch:assert>
      <sch:assert test=". = ('Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar')">
        <title>pds:Inventory/pds:field_delimiter/pds:field_delimiter</title>
        The attribute pds:Inventory/pds:field_delimiter must be equal to one of the following values 'Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:parsing_standard_id">
      <sch:assert test=". = ('PDS DSV 1')">
        <title>pds:Inventory/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Inventory/pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Inventory/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Inventory/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:reference_type">
      <sch:assert test=". = ('inventory_has_member_product')">
        <title>pds:Inventory/pds:reference_type/pds:reference_type</title>
        The attribute pds:Inventory/pds:reference_type must be equal to the value 'inventory_has_member_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Investigation/pds:type">
      <sch:assert test=". = ('Field Campaign', 'Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation')">
        <title>pds:Investigation/pds:type/pds:type</title>
        The attribute pds:Investigation/pds:type must be equal to one of the following values 'Field Campaign', 'Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Investigation_Area/pds:type">
      <sch:assert test=". = ('Field Campaign', 'Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation')">
        <title>pds:Investigation_Area/pds:type/pds:type</title>
        The attribute pds:Investigation_Area/pds:type must be equal to one of the following values 'Field Campaign', 'Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Local_ID_Reference/pds:id_reference_type">
      <sch:assert test=". = ('has_component', 'has_primary_component')">
        <title>pds:Local_ID_Reference/pds:id_reference_type/pds:id_reference_type</title>
        The attribute pds:Local_ID_Reference/pds:id_reference_type must be equal to one of the following values 'has_component', 'has_primary_component'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Local_ID_Relation/pds:id_reference_type">
      <sch:assert test=". = ('has_axis_values', 'has_backplane', 'has_column_headers', 'has_display_settings', 'has_spectral_characteristics')">
        <title>pds:Local_ID_Relation/pds:id_reference_type/pds:id_reference_type</title>
        The attribute pds:Local_ID_Relation/pds:id_reference_type must be equal to one of the following values 'has_axis_values', 'has_backplane', 'has_column_headers', 'has_display_settings', 'has_spectral_characteristics'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Manifest_SIP_Deep_Archive/pds:field_delimiter">
      <sch:assert test=". = ('Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar')">
        <title>pds:Manifest_SIP_Deep_Archive/pds:field_delimiter/pds:field_delimiter</title>
        The attribute pds:Manifest_SIP_Deep_Archive/pds:field_delimiter must be equal to one of the following values 'Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Manifest_SIP_Deep_Archive/pds:parsing_standard_id">
      <sch:assert test=". = ('PDS DSV 1')">
        <title>pds:Manifest_SIP_Deep_Archive/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Manifest_SIP_Deep_Archive/pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Manifest_SIP_Deep_Archive/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Manifest_SIP_Deep_Archive/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Manifest_SIP_Deep_Archive/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Node" role="warning">
      <sch:assert test="pds:name != 'Imaging'">
        <title>pds:Node role="warning"/pds:Node.name.Imaging</title>
        The value Imaging for attribute Node.name is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:name != 'Planetary Rings'">
        <title>pds:Node role="warning"/pds:Node.name.Imaging</title>
        The value Planetary Rings for attribute Node.name is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Node/pds:name">
      <sch:assert test=". = ('Cartography and Imaging Sciences Discipline', 'Engineering', 'Geosciences', 'Imaging', 'Management', 'Navigation and Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Planetary Science Archive', 'Radio Science', 'Ring-Moon Systems', 'Small Bodies')">
        <title>pds:Node/pds:name/pds:name</title>
        The attribute pds:Node/pds:name must be equal to one of the following values 'Cartography and Imaging Sciences Discipline', 'Engineering', 'Geosciences', 'Imaging', 'Management', 'Navigation and Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Planetary Science Archive', 'Radio Science', 'Ring-Moon Systems', 'Small Bodies'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Object_Statistics/pds:bit_mask" role="warning">
      <sch:assert test="false()">
        <title>pds:Object_Statistics/pds:bit_mask role="warning"/pds:Object_Statistics.bit_mask</title>
        pds:Object_Statistics/pds:bit_mask is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Object_Statistics/pds:md5_checksum" role="warning">
      <sch:assert test="false()">
        <title>pds:Object_Statistics/pds:md5_checksum role="warning"/pds:Object_Statistics.md5_checksum</title>
        pds:Object_Statistics/pds:md5_checksum is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Observing_System_Component" role="warning">
      <sch:assert test="pds:type != 'Airborne'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Airborne for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Aircraft'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Aircraft for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Balloon'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Balloon for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Suborbital Rocket'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Suborbital Rocket for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Computer'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Computer for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Facility'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Facility for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Laboratory'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Laboratory for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Naked Eye'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Naked Eye for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Observatory'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Observatory for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Spacecraft'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Spacecraft for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Artificial Illumination'">
        <title>pds:Observing_System_Component role="warning"/pds:Observing_System_Component.type</title>
        The value Artificial Illumination for attribute Observing_System_Component.type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Observing_System_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('is_instrument', 'is_instrument_host', 'is_other', 'is_facility', 'is_telescope', 'is_airborne')">
        <title>pds:Observing_System_Component/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'is_instrument', 'is_instrument_host', 'is_other', 'is_facility', 'is_telescope', 'is_airborne'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Observing_System_Component/pds:type">
      <sch:assert test=". = ('Airborne', 'Aircraft', 'Artificial Illumination', 'Balloon', 'Computer', 'Facility', 'Host', 'Instrument', 'Laboratory', 'Literature Search', 'Naked Eye', 'Observatory', 'Suborbital Rocket', 'Telescope')">
        <title>pds:Observing_System_Component/pds:type/pds:type</title>
        The attribute pds:Observing_System_Component/pds:type must be equal to one of the following values 'Airborne', 'Aircraft', 'Artificial Illumination', 'Balloon', 'Computer', 'Facility', 'Host', 'Instrument', 'Laboratory', 'Literature Search', 'Naked Eye', 'Observatory', 'Spacecraft', 'Suborbital Rocket', 'Telescope'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:PDS_Affiliate">
      <sch:assert test="if (pds:phone_book_flag) then pds:phone_book_flag = ('true', 'false') else true()">
        <title>pds:PDS_Affiliate/pds:phone_book_flag</title>
        The attribute pds:phone_book_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:PDS_Affiliate" role="warning">
      <sch:assert test="pds:team_name != 'Imaging'">
        <title>pds:PDS_Affiliate role="warning"/pds:PDS_Affiliate.team_name.Imaging</title>
        The value Imaging for attribute PDS_Affiliate.team_name is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:team_name != 'Planetary Rings'">
        <title>pds:PDS_Affiliate role="warning"/pds:PDS_Affiliate.team_name.Imaging</title>
        The value Planetary Rings for attribute PDS_Affiliate.team_name is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:team_name != 'Navigation Ancillary Information Facility'">
        <title>pds:PDS_Affiliate role="warning"/pds:PDS_Affiliate.team_name.Imaging</title>
        The value Navigation Ancillary Information Facility for attribute PDS_Affiliate.team_name is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:PDS_Affiliate/pds:affiliation_type">
      <sch:assert test=". = ('Affiliate', 'Data Provider', 'Manager', 'Technical Staff')">
        <title>pds:PDS_Affiliate/pds:affiliation_type/pds:affiliation_type</title>
        The attribute pds:PDS_Affiliate/pds:affiliation_type must be equal to one of the following values 'Affiliate', 'Data Provider', 'Manager', 'Technical Staff'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:PDS_Affiliate/pds:team_name">
      <sch:assert test=". = ('Cartography and Imaging Sciences Discipline', 'Engineering', 'Geosciences', 'Headquarters', 'Imaging', 'Management', 'National Space Science Data Center', 'Navigation and Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Radio Science', 'Ring-Moon Systems', 'Small Bodies')">
        <title>pds:PDS_Affiliate/pds:team_name/pds:team_name</title>
        The attribute pds:PDS_Affiliate/pds:team_name must be equal to one of the following values 'Cartography and Imaging Sciences Discipline', 'Engineering', 'Geosciences', 'Headquarters', 'Imaging', 'Management', 'National Space Science Data Center', 'Navigation and Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Radio Science', 'Ring-Moon Systems', 'Small Bodies'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Parsable_Byte_Stream/pds:object_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Parsable_Byte_Stream/pds:object_length/pds:object_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Parsable_Byte_Stream/pds:offset">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Parsable_Byte_Stream/pds:offset/pds:offset</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:Science_Facets">
      <sch:assert test="if (pds:discipline_name) then pds:discipline_name = ('Atmospheres', 'Fields', 'Flux Measurements', 'Geosciences', 'Imaging', 'Particles', 'Radio Science', 'Ring-Moon Systems', 'Small Bodies', 'Spectroscopy') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        The attribute pds:discipline_name must be equal to one of the following values 'Atmospheres', 'Fields', 'Flux Measurements', 'Geosciences', 'Imaging', 'Particles', 'Radio Science', 'Ring-Moon Systems', 'Small Bodies', 'Spectroscopy'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Atmospheres')) then pds:facet1 = ('Structure', 'Meteorology') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Atmospheres then if present pds:facet1 must be equal to one of the following values 'Structure', 'Meteorology'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Fields')) then pds:facet1 = ('Electric', 'Magnetic') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Fields then if present pds:facet1 must be equal to one of the following values 'Electric', 'Magnetic'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet2 and (pds:discipline_name eq 'Fields')) then pds:facet2 = ('Background', 'Waves') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Fields then if present pds:facet2 must be equal to one of the following values 'Background', 'Waves'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Flux Measurements')) then pds:facet1 = ('Photometry', 'Polarimetry') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Flux Measurements then if present pds:facet1 must be equal to one of the following values 'Photometry', 'Polarimetry'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Imaging')) then pds:facet1 = ('Grayscale', 'Color', 'Movie', 'Color Movie') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Imaging then if present pds:facet1 must be equal to one of the following values 'Grayscale', 'Color', 'Movie', 'Color Movie'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Particles')) then pds:facet1 = ('Ions', 'Electrons', 'Neutrals') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Particles then if present pds:facet1 must be equal to one of the following values 'Ions', 'Electrons', 'Neutrals'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet2 and (pds:discipline_name eq 'Particles')) then pds:facet2 = ('Cosmic Ray', 'Solar Energetic', 'Energetic', 'Plasma') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Particles then if present pds:facet2 must be equal to one of the following values 'Cosmic Ray', 'Solar Energetic', 'Energetic', 'Plasma'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Ring-Moon Systems')) then pds:facet1 = ('Satellite Astrometry', 'Ring Compositional Map', 'Ring Occultation Profile', 'Ring Thermal Map') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Ring-Moon Systems then if present pds:facet1 must be equal to one of the following values 'Satellite Astrometry', 'Ring Compositional Map', 'Ring Occultation Profile', 'Ring Thermal Map'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Small Bodies')) then pds:facet1 = ('Dynamical Properties', 'Lightcurve', 'Meteoritics', 'Physical Properties', 'Production Rates', 'Shape Model', 'Taxonomy', 'Dust Study', 'Historical Reference', 'Gas Study') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Small Bodies then if present pds:facet1 must be equal to one of the following values 'Dynamical Properties', 'Lightcurve', 'Meteoritics', 'Physical Properties', 'Production Rates', 'Shape Model', 'Taxonomy', 'Dust Study', 'Historical Reference', 'Gas Study'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Spectroscopy')) then pds:facet1 = ('2D', 'Linear', 'Spectral Cube', 'Spectral Image', 'Tabulated') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        If the attribute pds:discipline_name equals Spectroscopy then if present pds:facet1 must be equal to one of the following values '2D', 'Linear', 'Spectral Cube', 'Spectral Image', 'Tabulated'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1) then pds:discipline_name = ('Atmospheres', 'Fields', 'Flux Measurements', 'Imaging', 'Particles', 'Ring-Moon Systems', 'Small Bodies', 'Spectroscopy') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        Facet1 is allowed only when pds:discipline_name is one of the following 'Atmospheres', 'Fields', 'Flux Measurements', 'Imaging', 'Particles', 'Ring-Moon Systems', 'Small Bodies', 'Spectroscopy'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet2) then pds:discipline_name = ('Fields', 'Particles') else true()">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:discipline_name</title>
        Facet2 is allowed only when pds:discipline_name is one of the following 'Fields', 'Particles'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1'">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1/pds:subfacet1</title>
        pds:subfacet1 should not be used. No values have been provided.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2'">
        <title>pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2/pds:subfacet2</title>
        pds:subfacet2 should not be used. No values have been provided.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:data_regime">
      <sch:assert test=". = ('Dust', 'Electric Field', 'Electrons', 'Far Infrared', 'Gamma Ray', 'Infrared', 'Ions', 'Magnetic Field', 'Microwave', 'Millimeter', 'Near Infrared', 'Particles', 'Pressure', 'Radio', 'Sub-Millimeter', 'Temperature', 'Ultraviolet', 'Visible', 'X-Ray')">
        <title>pds:Primary_Result_Summary/pds:data_regime/pds:data_regime</title>
        The attribute pds:Primary_Result_Summary/pds:data_regime must be equal to one of the following values 'Dust', 'Electric Field', 'Electrons', 'Far Infrared', 'Gamma Ray', 'Infrared', 'Ions', 'Magnetic Field', 'Microwave', 'Millimeter', 'Near Infrared', 'Particles', 'Pressure', 'Radio', 'Sub-Millimeter', 'Temperature', 'Ultraviolet', 'Visible', 'X-Ray'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:data_regime" role="warning">
      <sch:assert test="false()">
        <title>pds:Primary_Result_Summary/pds:data_regime role="warning"/pds:Primary_Result_Summary.data_regime</title>
        pds:Primary_Result_Summary/pds:data_regime is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:processing_level">
      <sch:assert test=". = ('Calibrated', 'Derived', 'Partially Processed', 'Raw', 'Telemetry')">
        <title>pds:Primary_Result_Summary/pds:processing_level/pds:processing_level</title>
        The attribute pds:Primary_Result_Summary/pds:processing_level must be equal to one of the following values 'Calibrated', 'Derived', 'Partially Processed', 'Raw', 'Telemetry'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:processing_level_id">
      <sch:assert test=". = ('Calibrated', 'Derived', 'Partially Processed', 'Raw', 'Telemetry')">
        <title>pds:Primary_Result_Summary/pds:processing_level_id/pds:processing_level_id</title>
        The attribute pds:Primary_Result_Summary/pds:processing_level_id must be equal to one of the following values 'Calibrated', 'Derived', 'Partially Processed', 'Raw', 'Telemetry'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:processing_level_id" role="warning">
      <sch:assert test="false()">
        <title>pds:Primary_Result_Summary/pds:processing_level_id role="warning"/pds:Primary_Result_Summary.processing_level_id</title>
        pds:Primary_Result_Summary/pds:processing_level_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:purpose">
      <sch:assert test=". = ('Calibration', 'Checkout', 'Engineering', 'Navigation', 'Observation Geometry', 'Science', 'Supporting Observation')">
        <title>pds:Primary_Result_Summary/pds:purpose/pds:purpose</title>
        The attribute pds:Primary_Result_Summary/pds:purpose must be equal to one of the following values 'Calibration', 'Checkout', 'Engineering', 'Navigation', 'Observation Geometry', 'Science', 'Supporting Observation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:type">
      <sch:assert test=". = ('Altimetry', 'Astrometry', 'Count', 'E/B-Field Vectors', 'Gravity Model', 'Image', 'Lightcurves', 'Map', 'Meteorology', 'Null Result', 'Occultation', 'Photometry', 'Physical Parameters', 'Polarimetry', 'Radiometry', 'Reference', 'Shape Model', 'Spectrum')">
        <title>pds:Primary_Result_Summary/pds:type/pds:type</title>
        The attribute pds:Primary_Result_Summary/pds:type must be equal to one of the following values 'Altimetry', 'Astrometry', 'Count', 'E/B-Field Vectors', 'Gravity Model', 'Image', 'Lightcurves', 'Map', 'Meteorology', 'Null Result', 'Occultation', 'Photometry', 'Physical Parameters', 'Polarimetry', 'Radiometry', 'Reference', 'Shape Model', 'Spectrum'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:type" role="warning">
      <sch:assert test="false()">
        <title>pds:Primary_Result_Summary/pds:type role="warning"/pds:Primary_Result_Summary.type</title>
        pds:Primary_Result_Summary/pds:type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package')">
        <title>pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Ancillary/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('ancillary_to_investigation')">
        <title>pds:Product_Ancillary/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'ancillary_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Ancillary/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('ancillary_to_target')">
        <title>pds:Product_Ancillary/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'ancillary_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Ancillary/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('ancillary_to_data', 'ancillary_to_document', 'ancillary_to_browse')">
        <title>pds:Product_Ancillary/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'ancillary_to_data', 'ancillary_to_document', 'ancillary_to_browse'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Browse/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('browse_to_data', 'browse_to_thumbnail', 'browse_to_browse', 'browse_to_document', 'browse_to_ancillary')">
        <title>pds:Product_Browse/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'browse_to_data', 'browse_to_thumbnail', 'browse_to_browse', 'browse_to_document', 'browse_to_ancillary'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle">
      <sch:let name="bundTypeRef" value="pds:Bundle/pds:bundle_type"/>
      <sch:let name="bundMissionRef" value="pds:Context_Area/pds:Investigation_Area/pds:type"/>
      <sch:let name="bundPurposeRef" value="pds:Context_Area/pds:Primary_Result_Summary/pds:purpose"/>
      <sch:let name="bundCI_descriptionRef" value="pds:Identification_Area/pds:Citation_Information/pds:description"/>
      <sch:let name="bundStartTimeRef" value="pds:Context_Area/pds:Time_Coordinates/pds:start_date_time"/>
      <sch:let name="bundStopTimeRef" value="pds:Context_Area/pds:Time_Coordinates/pds:stop_date_time"/>
      <sch:let name="bundTargetNameRef" value="pds:Context_Area/pds:Target_Identification/pds:name"/>
      <sch:let name="bundTargetTypeRef" value="pds:Context_Area/pds:Target_Identification/pds:type"/>
      <sch:let name="bundInvestNameRef" value="pds:Context_Area/pds:Investigation_Area/pds:name"/>
      <sch:let name="bundObsSysCompNameRef" value="pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:name"/>
      <sch:let name="bundObsSysCompTypeRef" value="pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:type"/>
      <sch:let name="bundPRSlevelRef" value="pds:Context_Area/pds:Primary_Result_Summary/pds:processing_level"/>
      <sch:let name="bundleType" value="some $ref in ($bundTypeRef) satisfies $ref = ('Archive')"/>
      <sch:let name="MissionType" value="some $ref in ($bundMissionRef) satisfies $ref = ('Mission')"/>
      <sch:let name="PurposeType" value="some $ref in ($bundPurposeRef) satisfies $ref = ('Science')"/>
      <sch:let name="InstrumentType" value="some $ref in ($bundObsSysCompTypeRef) satisfies $ref = ('Instrument')"/>
      <sch:let name="isTypeMSD" value="($bundleType and $MissionType and $PurposeType)"/>
      <sch:let name="bundStreamTextlRef" value="pds:File_Area_Text/pds:Stream_Text/pds:parsing_standard_id"/>
      <sch:let name="SteamTextType" value="some $ref in ($bundStreamTextlRef) satisfies $ref = ('PDS3')"/>
      <sch:assert test="if ($isTypeMSD) then ($bundleType) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/bundle_type</title>
        For a Bundle of type 'Data', "pds:Bundle/pds:type" must have a value of 'Archive'.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($MissionType) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/type</title>
        For a Bundle of type 'Data', at least one instance of "pds:Context_Area/pds:Investigation_Area/pds:type" must have a value of 'Mission'.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($PurposeType) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/purpose</title>
        For a Bundle of type 'Data', at least one instance of "pds:Context_Area/pds:Primary_Result_Summary/pds:purpose" must have a value of 'Science'.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundCI_descriptionRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/description</title>
        For a Bundle of type 'Data', "pds:Identification_Area/pds:Citation_Information/description" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundStartTimeRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/start_date_time</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Time_Coordinates/pds:start_date_time" must be present and have a (nillable) value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundStopTimeRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/stop_date_time</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Time_Coordinates/pds:stop_date_time" must be present and have a (nillable) value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundTargetNameRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/name</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Target_Identification/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundTargetTypeRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/type</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Target_Identification/pds:type" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundInvestNameRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/name</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Investigation_Area/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundObsSysCompNameRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/name</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Observing_System/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundObsSysCompNameRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/type</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($InstrumentType) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/start_date_time</title>
        For a Bundle of type 'Data', an instance of "pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:type" must have a value of 'Instrument'.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($bundPRSlevelRef) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/processing_level</title>
        For a Bundle of type 'Data', "pds:Context_Area/pds:Primary_Result_Summary/pds:processing_level" must be present and have a value.</sch:assert>
      <sch:assert test="if ($bundStreamTextlRef) then (not($SteamTextType)) else true()">
        <title>pds:Product_Bundle/pds:Context_Area/parsing_standard_id</title>
        For a Bundle, if "pds:File_Area_Text/pds:Stream_Text" is present, then value must not be 'PDS3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('bundle_to_investigation')">
        <title>pds:Product_Bundle/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'bundle_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        <title>pds:Product_Bundle/pds:Identification_Area/description</title>
        In Product_Bundle both Citation_Information and its description are required.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Identification_Area/pds:logical_identifier">
      <sch:let name="num_colons" value="string-length(.) - string-length(translate(., ':', ''))"/>
      <sch:let name="required_colons" value="3"/>
      <sch:assert test="$num_colons eq $required_colons">
        <title>pds:Product_Bundle/pds:Identification_Area/pds:logical_identifier/logical_identifier</title>
        In Product_Bundle, the number of colons found: (<sch:value-of select="$num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$required_colons"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('bundle_to_errata', 'bundle_to_document', 'bundle_to_investigation', 'bundle_to_instrument', 'bundle_to_instrument_host', 'bundle_to_target', 'bundle_to_resource', 'bundle_to_associate')">
        <title>pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'bundle_to_errata', 'bundle_to_document', 'bundle_to_investigation', 'bundle_to_instrument', 'bundle_to_instrument_host', 'bundle_to_target', 'bundle_to_resource', 'bundle_to_associate'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:let name="collTypeRef" value="pds:Product_Collection/pds:Collection/pds:collection_type"/>
    <sch:let name="collMissionRef" value="pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:type"/>
    <sch:let name="collPurposeRef" value="pds:Product_Collection/pds:Context_Area/pds:Primary_Result_Summary/pds:purpose"/>
    <sch:let name="collObsSysCompTypeRef" value="pds:Product_Collection/pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:type"/>
    <sch:let name="collType" value="every $ref in ($collTypeRef) satisfies $ref = ('Data')"/>
    <sch:let name="missionType" value="some $ref in ($collMissionRef) satisfies $ref = ('Mission')"/>
    <sch:let name="purposeType" value="every $ref in ($collPurposeRef) satisfies $ref = ('Science')"/>
    <sch:let name="instrumentType" value="some $ref in ($collObsSysCompTypeRef) satisfies $ref = ('Instrument')"/>
    <sch:let name="isTypeMSD" value="($collType and $missionType and $purposeType)"/>
    <sch:rule context="pds:Product_Collection/pds:Context_Area">
      <sch:let name="collStartTimeRef" value="pds:Time_Coordinates/pds:start_date_time"/>
      <sch:let name="collStopTimeRef" value="pds:Time_Coordinates/pds:stop_date_time"/>
      <sch:let name="collTargetNameRef" value="pds:Target_Identification/pds:name"/>
      <sch:let name="collTargetTypeRef" value="pds:Target_Identification/pds:type"/>
      <sch:let name="collInvestNameRef" value="pds:Investigation_Area/pds:name"/>
      <sch:let name="collObsSysCompNameRef" value="pds:Observing_System/pds:Observing_System_Component/pds:name"/>
      <sch:let name="collObsSysCompTypeRef" value="pds:Observing_System/pds:Observing_System_Component/pds:type"/>
      <sch:let name="instrumentType" value="some $ref in ($collObsSysCompTypeRef) satisfies $ref = ('Instrument')"/>
      <sch:let name="collPRSlevelRef" value="pds:Primary_Result_Summary/pds:processing_level"/>
      <sch:assert test="if ($isTypeMSD) then ($collStartTimeRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/start_date_time</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Time_Coordinates/pds:start_date_time" must be present and have a (nillable) value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($collStopTimeRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/stop_date_time</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Time_Coordinates/pds:stop_date_time" must be present and have a (nillable) value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($collTargetNameRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/name</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Target_Identification/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($collTargetTypeRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/type</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Target_Identification/pds:type" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($collInvestNameRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/name</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Investigation_Area/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($collObsSysCompNameRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/name</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:name" must be present and have a value.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($instrumentType) else true()">
        <title>pds:Product_Collection/pds:Context_Area/type</title>
        For a Collection of type 'Mission Science Data', an instance of "pds:Context_Area/pds:Observing_System/pds:Observing_System_Component/pds:type" must have a value of 'Instrument'.</sch:assert>
      <sch:assert test="if ($isTypeMSD) then ($collPRSlevelRef) else true()">
        <title>pds:Product_Collection/pds:Context_Area/processing_level</title>
        For a Collection of type 'Mission Science Data', "pds:Context_Area/pds:Primary_Result_Summary/pds:processing_level" must be present and have a value.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('collection_to_investigation')">
        <title>pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'collection_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        <title>pds:Product_Collection/pds:Identification_Area/description</title>
        In Product_Collection both Citation_Information and its description are required.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Identification_Area/pds:logical_identifier">
      <sch:let name="num_colons" value="string-length(.) - string-length(translate(., ':', ''))"/>
      <sch:let name="required_colons" value="4"/>
      <sch:assert test="$num_colons eq $required_colons">
        <title>pds:Product_Collection/pds:Identification_Area/pds:logical_identifier/logical_identifier</title>
        In Product_Collection, the number of colons found: (<sch:value-of select="$num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$required_colons"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('collection_to_resource', 'collection_to_associate', 'collection_to_calibration', 'collection_to_geometry', 'collection_to_spice_kernel', 'collection_curated_by_node', 'collection_to_document', 'collection_to_browse', 'collection_to_context', 'collection_to_data', 'collection_to_ancillary', 'collection_to_schema', 'collection_to_errata', 'collection_to_bundle', 'collection_to_personnel', 'collection_to_investigation', 'collection_to_instrument', 'collection_to_instrument_host', 'collection_to_target')">
        <title>pds:Product_Collection/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'collection_to_resource', 'collection_to_associate', 'collection_to_calibration', 'collection_to_geometry', 'collection_to_spice_kernel', 'collection_curated_by_node', 'collection_to_document', 'collection_to_browse', 'collection_to_context', 'collection_to_data', 'collection_to_ancillary', 'collection_to_schema', 'collection_to_errata', 'collection_to_bundle', 'collection_to_personnel', 'collection_to_investigation', 'collection_to_instrument', 'collection_to_instrument_host', 'collection_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Context/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('context_to_associate', 'instrument_to_document', 'instrument_to_instrument_host', 'instrument_host_to_document', 'instrument_host_to_instrument', 'instrument_host_to_investigation', 'instrument_host_to_target', 'investigation_to_document', 'investigation_to_instrument', 'investigation_to_instrument_host', 'investigation_to_target', 'node_to_personnel', 'node_to_agency', 'node_to_manager', 'node_to_operator', 'node_to_data_archivist', 'resource_to_instrument', 'resource_to_instrument_host', 'resource_to_investigation', 'resource_to_target', 'target_to_document', 'target_to_instrument', 'target_to_instrument_host', 'target_to_investigation', 'instrument_to_telescope', 'instrument_to_observatory', 'instrument_to_facility', 'telescope_to_observatory', 'telescope_to_facility', 'telescope_to_airborne', 'investigation_to_telescope', 'facility_to_investigation', 'facility_to_instrument', 'facility_to_telescope', 'investigation_to_facility', 'telescope_to_instrument')">
        <title>pds:Product_Context/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'context_to_associate', 'instrument_to_document', 'instrument_to_instrument_host', 'instrument_host_to_document', 'instrument_host_to_instrument', 'instrument_host_to_investigation', 'instrument_host_to_target', 'investigation_to_document', 'investigation_to_instrument', 'investigation_to_instrument_host', 'investigation_to_target', 'node_to_personnel', 'node_to_agency', 'node_to_manager', 'node_to_operator', 'node_to_data_archivist', 'resource_to_instrument', 'resource_to_instrument_host', 'resource_to_investigation', 'resource_to_target', 'target_to_document', 'target_to_instrument', 'target_to_instrument_host', 'target_to_investigation', 'instrument_to_telescope', 'instrument_to_observatory', 'instrument_to_facility', 'telescope_to_observatory', 'telescope_to_facility', 'telescope_to_airborne', 'investigation_to_telescope', 'facility_to_investigation', 'facility_to_instrument', 'facility_to_telescope', 'investigation_to_facility', 'telescope_to_instrument'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package')">
        <title>pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package')">
        <title>pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('document_to_investigation')">
        <title>pds:Product_Document/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'document_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('document_to_target')">
        <title>pds:Product_Document/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'document_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        <title>pds:Product_Document/pds:Identification_Area/description</title>
        In Product_Document both Citation_Information and its description are required.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('document_to_associate', 'document_to_investigation', 'document_to_instrument_host', 'document_to_instrument', 'document_to_target', 'document_to_data')">
        <title>pds:Product_Document/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'document_to_associate', 'document_to_investigation', 'document_to_instrument_host', 'document_to_instrument', 'document_to_target', 'document_to_data'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_File_Text/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        <title>pds:Product_File_Text/pds:Identification_Area/description</title>
        In Product_File_Text both Citation_Information and its description are required.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Native/pds:Reference_List">
      <sch:let name="collTypeRef" value="pds:Internal_Reference/pds:reference_type"/>
      <sch:let name="refType" value="some $ref in ($collTypeRef) satisfies $ref = ('native_to_archival')"/>
      <sch:assert test="($collTypeRef)">
        <title>pds:Product_Native/pds:Reference_List/reference_type</title>
        In Product_Native, at least one instance of 'pds:Internal_Reference/pds:reference_type' must be present.</sch:assert>
      <sch:assert test="($refType)">
        <title>pds:Product_Native/pds:Reference_List/reference_type</title>
        In Product_Native, at least one instance of 'pds:Internal_Reference/pds:reference_type' must be set to the following value 'native_to_archival'</sch:assert>
      <sch:assert test="every $ref in ($collTypeRef) satisfies $ref = ('described_by_document', 'native_to_archival')">
        <title>pds:Product_Native/pds:Reference_List/reference_type</title>
        In Product_Native, every instance of 'pds:Internal_Reference/pds:reference_type' must be set to one of the following values: 'described_by_document', 'native_to_archival'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Observational/pds:Observation_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_investigation')">
        <title>pds:Product_Observational/pds:Observation_Area/pds:Investigation_Area/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'data_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Observational/pds:Observation_Area/pds:Target_Identification/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_target')">
        <title>pds:Product_Observational/pds:Observation_Area/pds:Target_Identification/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'data_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Observational/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_associate', 'data_to_resource', 'data_to_calibration_document', 'data_to_calibration_product', 'data_to_raw_product', 'data_to_calibrated_product', 'data_to_derived_product', 'data_to_geometry', 'data_to_spice_kernel', 'data_to_thumbnail', 'data_to_document', 'data_curated_by_node', 'data_to_browse', 'data_to_ancillary_data', 'data_to_partially_processed_product')">
        <title>pds:Product_Observational/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'data_to_associate', 'data_to_resource', 'data_to_calibration_document', 'data_to_calibration_product', 'data_to_raw_product', 'data_to_calibrated_product', 'data_to_derived_product', 'data_to_geometry', 'data_to_spice_kernel', 'data_to_thumbnail', 'data_to_document', 'data_curated_by_node', 'data_to_browse', 'data_to_ancillary_data', 'data_to_partially_processed_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product')">
        <title>pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_SIP_Deep_Archive/pds:Information_Package_Component_Deep_Archive/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_bundle')">
        <title>pds:Product_SIP_Deep_Archive/pds:Information_Package_Component_Deep_Archive/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'package_has_bundle'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_SPICE_Kernel/pds:Context_Area">
      <sch:assert test="(pds:Time_Coordinates and pds:Investigation_Area and pds:Target_Identification and pds:Observing_System)">
        <title>pds:Product_SPICE_Kernel/pds:Context_Area/x</title>
        In Product_SPICE_Kernel the Time_Coordinates, Investigation_Area, Target_Identification, and Observing_System classes must be present</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Thumbnail/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('thumbnail_to_data', 'thumbnail_to_browse', 'thumbnail_to_document')">
        <title>pds:Product_Thumbnail/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'thumbnail_to_data', 'thumbnail_to_browse', 'thumbnail_to_document'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Update" role="warning">
      <sch:assert test="false()">
        <title>pds:Product_Update role="warning"/pds:Product_Update</title>
        pds:Product_Update is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Update/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('update_to_collection')">
        <title>pds:Product_Update/pds:Reference_List/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'update_to_collection'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Zipped/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('zip_to_package')">
        <title>pds:Product_Zipped/pds:Internal_Reference/reference_type</title>
        The attribute reference_type must be set to one of the following values 'zip_to_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Property_Map_Entry/pds:property_map_type">
      <sch:assert test=". = ('Nuance', 'Query Model', 'Rationale', 'Synonym', 'Velocity Variable')">
        <title>pds:Property_Map_Entry/pds:property_map_type/pds:property_map_type</title>
        The attribute pds:Property_Map_Entry/pds:property_map_type must be equal to one of the following values 'Nuance', 'Query Model', 'Rationale', 'Synonym', 'Velocity Variable'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Quaternion/pds:type">
      <sch:assert test=". = ('SPICE', 'Spacecraft Telemetry')">
        <title>pds:Quaternion/pds:type/pds:type</title>
        The attribute pds:Quaternion/pds:type must be equal to one of the following values 'SPICE', 'Spacecraft Telemetry'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Quaternion_Component/pds:data_type">
      <sch:assert test=". = ('ASCII_Real')">
        <title>pds:Quaternion_Component/pds:data_type/pds:data_type</title>
        The attribute pds:Quaternion_Component/pds:data_type must be equal to the value 'ASCII_Real'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Record_Binary">
      <sch:let name="num_fields" value="(pds:fields)"/>
      <sch:let name="num_groups" value="(pds:groups)"/>
      <sch:assert test="$num_fields > '0' or $num_groups > '0'">
        <title>pds:Record_Binary/num_fields</title>
        The values for 'fields' and 'groups' must not both be '0'.  The number of fields found: (<sch:value-of select="$num_fields"/>).  The number of groups found: (<sch:value-of select="$num_groups"/>).</sch:assert>
      <sch:assert test="$num_fields = count(pds:Field_Binary)">
        <title>pds:Record_Binary/fields</title>
        The number of Field_Binary elements (<sch:value-of select="count(pds:Field_Binary)"/>) does not match the number found in the 'fields' attribute (<sch:value-of select="$num_fields"/>).</sch:assert>
      <sch:assert test="$num_groups = count(pds:Group_Field_Binary)">
        <title>pds:Record_Binary/fields</title>
        The number of Group_Field_Binary elements (<sch:value-of select="count(pds:Group_Field_Binary)"/>) does not match the number found in the 'groups' attribute (<sch:value-of select="$num_groups"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Record_Binary/pds:record_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Record_Binary/pds:record_length/pds:record_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Record_Character">
      <sch:let name="num_fields" value="(pds:fields)"/>
      <sch:let name="num_groups" value="(pds:groups)"/>
      <sch:assert test="$num_fields > '0' or $num_groups > '0'">
        <title>pds:Record_Character/num_fields</title>
        The values for 'fields' and 'groups' must not both be '0'.  The number of fields found: (<sch:value-of select="$num_fields"/>).  The number of groups found: (<sch:value-of select="$num_groups"/>).</sch:assert>
      <sch:assert test="$num_fields = count(pds:Field_Character)">
        <title>pds:Record_Character/fields</title>
        The number of Field_Character elements (<sch:value-of select="count(pds:Field_Character)"/>) does not match the number found in the 'fields' attribute (<sch:value-of select="$num_fields"/>).</sch:assert>
      <sch:assert test="$num_groups = count(pds:Group_Field_Character)">
        <title>pds:Record_Character/fields</title>
        The number of Group_Field_Character elements (<sch:value-of select="count(pds:Group_Field_Character)"/>) does not match the number found in the 'groups' attribute (<sch:value-of select="$num_groups"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Record_Character/pds:record_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Record_Character/pds:record_length/pds:record_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Record_Delimited">
      <sch:let name="num_fields" value="(pds:fields)"/>
      <sch:let name="num_groups" value="(pds:groups)"/>
      <sch:assert test="$num_fields > '0' or $num_groups > '0'">
        <title>pds:Record_Delimited/fields</title>
        The values for 'fields' and 'groups' must not both be '0'.  The number of fields found: (<sch:value-of select="$num_fields"/>).  The number of groups found: (<sch:value-of select="$num_groups"/>).</sch:assert>
      <sch:assert test="$num_fields = count(pds:Field_Delimited)">
        <title>pds:Record_Delimited/fields</title>
        The number of Field_Delimited elements (<sch:value-of select="count(pds:Field_Delimited)"/>) does not match the number found in the 'fields' attribute (<sch:value-of select="$num_fields"/>).</sch:assert>
      <sch:assert test="$num_groups = count(pds:Group_Field_Delimited)">
        <title>pds:Record_Delimited/fields</title>
        The number of Group_Field_Delimited elements (<sch:value-of select="count(pds:Group_Field_Delimited)"/>) does not match the number found in the 'groups' attribute (<sch:value-of select="$num_groups"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Record_Delimited/pds:maximum_record_length">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Record_Delimited/pds:maximum_record_length/pds:maximum_record_length</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Resource/pds:type">
      <sch:assert test=". = ('Information.Agency', 'Information.Instrument', 'Information.Instrument_Host', 'Information.Investigation', 'Information.Node', 'Information.Person', 'Information.Resource', 'Information.Science_Portal', 'Information.Target', 'System.Browse', 'System.Directory_Listing', 'System.Registry_Query', 'System.Search', 'System.Transform', 'System.Transport')">
        <title>pds:Resource/pds:type/pds:type</title>
        The attribute pds:Resource/pds:type must be equal to one of the following values 'Information.Agency', 'Information.Instrument', 'Information.Instrument_Host', 'Information.Investigation', 'Information.Node', 'Information.Person', 'Information.Resource', 'Information.Science_Portal', 'Information.Target', 'System.Browse', 'System.Directory_Listing', 'System.Registry_Query', 'System.Search', 'System.Transform', 'System.Transport'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:SIP_Deep_Archive/pds:provider_site_id">
      <sch:assert test=". = ('PDS_ATM', 'PDS_ENG', 'PDS_GEO', 'PDS_IMG', 'PDS_JPL', 'PDS_NAI', 'PDS_PPI', 'PDS_PSI', 'PDS_RNG', 'PDS_SBN')">
        <title>pds:SIP_Deep_Archive/pds:provider_site_id/pds:provider_site_id</title>
        The attribute pds:SIP_Deep_Archive/pds:provider_site_id must be equal to one of the following values 'PDS_ATM', 'PDS_ENG', 'PDS_GEO', 'PDS_IMG', 'PDS_JPL', 'PDS_NAI', 'PDS_PPI', 'PDS_PSI', 'PDS_RNG', 'PDS_SBN'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:SPICE_Kernel/pds:encoding_type">
      <sch:assert test=". = ('Binary', 'Character')">
        <title>pds:SPICE_Kernel/pds:encoding_type/pds:encoding_type</title>
        The attribute pds:SPICE_Kernel/pds:encoding_type must be equal to one of the following values 'Binary', 'Character'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:SPICE_Kernel/pds:kernel_type">
      <sch:assert test=". = ('CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK')">
        <title>pds:SPICE_Kernel/pds:kernel_type/pds:kernel_type</title>
        The attribute pds:SPICE_Kernel/pds:kernel_type must be equal to one of the following values 'CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:SPICE_Kernel/pds:parsing_standard_id">
      <sch:assert test=". = ('SPICE')">
        <title>pds:SPICE_Kernel/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:SPICE_Kernel/pds:parsing_standard_id must be equal to the value 'SPICE'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Science_Facets/pds:domain">
      <sch:assert test=". = ('Atmosphere', 'Dynamics', 'Heliosheath', 'Heliosphere', 'Interior', 'Interstellar', 'Ionosphere', 'Magnetosphere', 'Rings', 'Surface')">
        <title>pds:Science_Facets/pds:domain/pds:domain</title>
        The attribute pds:Science_Facets/pds:domain must be equal to one of the following values 'Atmosphere', 'Dynamics', 'Heliosheath', 'Heliosphere', 'Interior', 'Interstellar', 'Ionosphere', 'Magnetosphere', 'Rings', 'Surface'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Science_Facets/pds:wavelength_range">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('Far Infrared', 'Gamma Ray', 'Infrared', 'Microwave', 'Millimeter', 'Near Infrared', 'Radio', 'Submillimeter', 'Ultraviolet', 'Visible', 'X-ray')))) then false() else true()">
        <title>pds:Science_Facets/pds:wavelength_range/pds:wavelength_range</title>
        The attribute pds:Science_Facets/pds:wavelength_range must be nulled or equal to one of the following values 'Far Infrared', 'Gamma Ray', 'Infrared', 'Microwave', 'Millimeter', 'Near Infrared', 'Radio', 'Submillimeter', 'Ultraviolet', 'Visible', 'X-ray'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Service/pds:category">
      <sch:assert test=". = ('Analysis', 'Design', 'Dissemination', 'Generation', 'Planning', 'Reader', 'Search', 'Transformation', 'Validation', 'Visualization')">
        <title>pds:Service/pds:category/pds:category</title>
        The attribute pds:Service/pds:category must be equal to one of the following values 'Analysis', 'Design', 'Dissemination', 'Generation', 'Planning', 'Reader', 'Search', 'Transformation', 'Validation', 'Visualization'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Service/pds:interface_type">
      <sch:assert test=". = ('API', 'Command-Line', 'GUI', 'Service')">
        <title>pds:Service/pds:interface_type/pds:interface_type</title>
        The attribute pds:Service/pds:interface_type must be equal to one of the following values 'API', 'Command-Line', 'GUI', 'Service'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Service/pds:service_type">
      <sch:assert test=". = ('Service', 'Tool')">
        <title>pds:Service/pds:service_type/pds:service_type</title>
        The attribute pds:Service/pds:service_type must be equal to one of the following values 'Service', 'Tool'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Service_Description/pds:parsing_standard_id">
      <sch:assert test=". = ('WADL', 'WSDL 2.n')">
        <title>pds:Service_Description/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Service_Description/pds:parsing_standard_id must be equal to one of the following values 'WADL', 'WSDL 2.n'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Software/pds:version_id" role="warning">
      <sch:assert test="false()">
        <title>pds:Software/pds:version_id role="warning"/pds:Software.version_id</title>
        pds:Software/pds:version_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Source_Product_External">
      <sch:assert test="if ( (not(pds:doi)) and (not(pds:curating_facility)) ) then false() else true()">
        <title>pds:Source_Product_External/doi</title>
        The class Source_Product_External must contain at least one of the attributes 'pds:doi' or 'pds:curating_facility'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Source_Product_External/pds:reference_type">
      <sch:assert test=". = ('data_to_calibrated_source_product', 'data_to_derived_source_product', 'data_to_partially_processed_source_product', 'data_to_raw_source_product', 'data_to_telemetry_source_product')">
        <title>pds:Source_Product_External/pds:reference_type/pds:reference_type</title>
        The attribute pds:Source_Product_External/pds:reference_type must be equal to one of the following values 'data_to_calibrated_source_product', 'data_to_derived_source_product', 'data_to_partially_processed_source_product', 'data_to_raw_source_product', 'data_to_telemetry_source_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Source_Product_Internal/pds:reference_type">
      <sch:assert test=". = ('data_to_calibrated_source_product', 'data_to_derived_source_product', 'data_to_partially_processed_source_product', 'data_to_raw_source_product', 'data_to_telemetry_source_product')">
        <title>pds:Source_Product_Internal/pds:reference_type/pds:reference_type</title>
        The attribute pds:Source_Product_Internal/pds:reference_type must be equal to one of the following values 'data_to_calibrated_source_product', 'data_to_derived_source_product', 'data_to_partially_processed_source_product', 'data_to_raw_source_product', 'data_to_telemetry_source_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Stream_Text" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Stream_Text role="warning"/pds:Stream_Text.record_delimiter</title>
        The value carriage-return line-feed for attribute Stream_Text.record_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Stream_Text/pds:parsing_standard_id">
      <sch:assert test=". = ('7-Bit ASCII Text', 'PDS3', 'UTF-8 Text')">
        <title>pds:Stream_Text/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Stream_Text/pds:parsing_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'PDS3', 'UTF-8 Text'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Stream_Text/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Stream_Text/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Stream_Text/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Base/pds:offset">
      <sch:assert test="@unit = ('byte')">
        <title>pds:Table_Base/pds:offset/pds:offset</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Binary/pds:record_delimiter" role="warning">
      <sch:assert test="false()">
        <title>pds:Table_Binary/pds:record_delimiter role="warning"/pds:Table_Binary.record_delimiter</title>
        pds:Table_Binary/pds:record_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Character" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Table_Character role="warning"/pds:Table_Character.record_delimiter</title>
        The value carriage-return line-feed for attribute Table_Character.record_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Character/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Table_Character/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Table_Character/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Table_Delimited role="warning"/pds:Table_Delimited.record_delimiter</title>
        The value carriage-return line-feed for attribute Table_Delimited.record_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'comma'">
        <title>pds:Table_Delimited role="warning"/pds:Table_Delimited.record_delimiter</title>
        The value comma for attribute Table_Delimited.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'horizontal tab'">
        <title>pds:Table_Delimited role="warning"/pds:Table_Delimited.record_delimiter</title>
        The value horizontal tab for attribute Table_Delimited.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'semicolon'">
        <title>pds:Table_Delimited role="warning"/pds:Table_Delimited.record_delimiter</title>
        The value semicolon for attribute Table_Delimited.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'vertical bar'">
        <title>pds:Table_Delimited role="warning"/pds:Table_Delimited.record_delimiter</title>
        The value vertical bar for attribute Table_Delimited.field_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited/pds:field_delimiter">
      <sch:assert test=". = ('Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar')">
        <title>pds:Table_Delimited/pds:field_delimiter/pds:field_delimiter</title>
        The attribute pds:Table_Delimited/pds:field_delimiter must be equal to one of the following values 'Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited/pds:parsing_standard_id">
      <sch:assert test=". = ('PDS DSV 1')">
        <title>pds:Table_Delimited/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Table_Delimited/pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Table_Delimited/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Table_Delimited/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External">
      <sch:assert test="count(pds:Record_Delimited/pds:Field_Delimited) eq 4">
        <title>pds:Table_Delimited_Source_Product_External/TBD_Identifier</title>
        Table_Delimited_Source_Product_External.Field_Delimited does not match the expected number of instances</sch:assert>
      <sch:assert test="pds:offset eq '0'">
        <title>pds:Table_Delimited_Source_Product_External/TBD_Identifier</title>
        Table_Delimited_Source_Product_External.offset must have a value of '0'</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Table_Delimited_Source_Product_External role="warning"/pds:Table_Delimited_Source_Product_External.record_delimiter</title>
        The value carriage-return line-feed for attribute Table_Delimited_Source_Product_External.record_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'comma'">
        <title>pds:Table_Delimited_Source_Product_External role="warning"/pds:Table_Delimited_Source_Product_External.record_delimiter</title>
        The value comma for attribute Table_Delimited_Source_Product_External.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'horizontal tab'">
        <title>pds:Table_Delimited_Source_Product_External role="warning"/pds:Table_Delimited_Source_Product_External.record_delimiter</title>
        The value horizontal tab for attribute Table_Delimited_Source_Product_External.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'semicolon'">
        <title>pds:Table_Delimited_Source_Product_External role="warning"/pds:Table_Delimited_Source_Product_External.record_delimiter</title>
        The value semicolon for attribute Table_Delimited_Source_Product_External.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'vertical bar'">
        <title>pds:Table_Delimited_Source_Product_External role="warning"/pds:Table_Delimited_Source_Product_External.record_delimiter</title>
        The value vertical bar for attribute Table_Delimited_Source_Product_External.field_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:Record_Delimited">
      <sch:assert test="pds:fields eq '4'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/TBD_Identifier</title>
        The attribute pds:fields must be 4.</sch:assert>
      <sch:assert test="pds:groups eq '0'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/TBD_Identifier</title>
        The attribute pds:groups must be equal to '0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[1]">
      <sch:assert test="pds:field_number eq '1'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_External must have field_number set to 1.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_External must have maximum_field_length set to 255.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_External must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'Reference Type'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_External must have name set to 'Reference Type'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[2]">
      <sch:assert test="pds:field_number eq '2'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_External must have field_number set to 2.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_External must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'External Source Product Identifier'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_External must have name set to 'External Source Product Identifier'.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_External must have maximum_field_length set to 255.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[3]">
      <sch:assert test="pds:field_number eq '3'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[3]/TBD_Identifier</title>
        The third field of a Table_Delimited_Source_Product_External must have field_number set to 3.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[3]/TBD_Identifier</title>
        The third field of a Table_Delimited_Source_Product_External must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'DOI'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[3]/TBD_Identifier</title>
        The third field of a Table_Delimited_Source_Product_External must have name set to 'DOI'.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[3]/TBD_Identifier</title>
        The third field of a Table_Delimited_Source_Product_External must have maximum_field_length set to 255.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[4]">
      <sch:assert test="pds:field_number eq '4'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[4]/TBD_Identifier</title>
        The fourth field of a Table_Delimited_Source_Product_External must have field_number set to 4.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[4]/TBD_Identifier</title>
        The fourth field of a Table_Delimited_Source_Product_External must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'Curating Facility'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[4]/TBD_Identifier</title>
        The fourth field of a Table_Delimited_Source_Product_External must have name set to 'Curating Facility'.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Table_Delimited_Source_Product_External/pds:Record_Delimited/pds:Field_Delimited[4]/TBD_Identifier</title>
        The fourth field of a Table_Delimited_Source_Product_External must have maximum_field_length set to 255.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:field_delimiter">
      <sch:assert test=". = ('Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar')">
        <title>pds:Table_Delimited_Source_Product_External/pds:field_delimiter/pds:field_delimiter</title>
        The attribute pds:Table_Delimited_Source_Product_External/pds:field_delimiter must be equal to one of the following values 'Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:parsing_standard_id">
      <sch:assert test=". = ('PDS DSV 1')">
        <title>pds:Table_Delimited_Source_Product_External/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Table_Delimited_Source_Product_External/pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Table_Delimited_Source_Product_External/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Table_Delimited_Source_Product_External/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_External/pds:reference_type">
      <sch:assert test=". = ('table_has_source_products')">
        <title>pds:Table_Delimited_Source_Product_External/pds:reference_type/TBD_Identifier</title>
        The attribute pds:reference_type must be equal to the value 'table_has_source_products'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal">
      <sch:assert test="(count(pds:Record_Delimited/pds:Field_Delimited) eq 2)">
        <title>pds:Table_Delimited_Source_Product_Internal/TBD_Identifier</title>
        Table_Delimited_Source_Product_Internal.Field_Delimited does not match the expected number of instances</sch:assert>
      <sch:assert test="pds:offset eq '0'">
        <title>pds:Table_Delimited_Source_Product_Internal/TBD_Identifier</title>
        Table_Delimited_Source_Product_Internal.offset must have a value of '0'</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Table_Delimited_Source_Product_Internal role="warning"/pds:Table_Delimited_Source_Product_Internal.record_delimiter</title>
        The value carriage-return line-feed for attribute Table_Delimited_Source_Product_Internal.record_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'comma'">
        <title>pds:Table_Delimited_Source_Product_Internal role="warning"/pds:Table_Delimited_Source_Product_Internal.record_delimiter</title>
        The value comma for attribute Table_Delimited_Source_Product_Internal.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'horizontal tab'">
        <title>pds:Table_Delimited_Source_Product_Internal role="warning"/pds:Table_Delimited_Source_Product_Internal.record_delimiter</title>
        The value horizontal tab for attribute Table_Delimited_Source_Product_Internal.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'semicolon'">
        <title>pds:Table_Delimited_Source_Product_Internal role="warning"/pds:Table_Delimited_Source_Product_Internal.record_delimiter</title>
        The value semicolon for attribute Table_Delimited_Source_Product_Internal.field_delimiter is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:field_delimiter != 'vertical bar'">
        <title>pds:Table_Delimited_Source_Product_Internal role="warning"/pds:Table_Delimited_Source_Product_Internal.record_delimiter</title>
        The value vertical bar for attribute Table_Delimited_Source_Product_Internal.field_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited">
      <sch:assert test="pds:fields eq '2'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/TBD_Identifier</title>
        The attribute pds:fields must be equal to '2'.</sch:assert>
      <sch:assert test="pds:groups eq '0'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/TBD_Identifier</title>
        The attribute pds:groups must be equal to '0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[1]">
      <sch:assert test="pds:field_number eq '1'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_Internal must have field_number set to 1.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_Internal must have maximum_field_length set to 255.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_Internal must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'Reference Type'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[1]/TBD_Identifier</title>
        The first field of a Table_Delimited_Source_Product_Internal must have name set to 'Reference Type'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[2]">
      <sch:assert test="pds:field_number eq '2'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_Internal must have field_number set to 2.</sch:assert>
      <sch:assert test="(pds:data_type eq 'ASCII_LID') or (pds:data_type eq 'ASCII_LIDVID') or (pds:data_type eq 'ASCII_LIDVID_LID')">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_Internal must have data_type set to either 'ASCII_LID' or 'ASCII_LIDVID' or 'ASCII_LIDVID_LID'.</sch:assert>
      <sch:assert test="(pds:name eq 'LIDVID_LID')">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_Internal must have name set to 'LIDVID_LID'.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '255'">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:Record_Delimited/pds:Field_Delimited[2]/TBD_Identifier</title>
        The second field of a Table_Delimited_Source_Product_Internal must have maximum_field_length set to 255.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:field_delimiter">
      <sch:assert test=". = ('Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar')">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:field_delimiter/pds:field_delimiter</title>
        The attribute pds:Table_Delimited_Source_Product_Internal/pds:field_delimiter must be equal to one of the following values 'Comma', 'Horizontal Tab', 'Semicolon', 'Vertical Bar', 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:parsing_standard_id">
      <sch:assert test=". = ('PDS DSV 1')">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:Table_Delimited_Source_Product_Internal/pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Table_Delimited_Source_Product_Internal/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited_Source_Product_Internal/pds:reference_type">
      <sch:assert test=". = ('table_has_source_products')">
        <title>pds:Table_Delimited_Source_Product_Internal/pds:reference_type/TBD_Identifier</title>
        The attribute pds:reference_type must be equal to the value 'table_has_source_products'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target" role="warning">
      <sch:assert test="pds:type != 'Calibration'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Calibration for attribute Target.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Open Cluster'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Open Cluster for attribute Target.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Globular Cluster'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Globular Cluster for attribute Target.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Terrestrial Sample'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Terrestrial Sample for attribute Target.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Lunar Sample'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Lunar Sample for attribute Target.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Synthetic Sample'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Synthetic Sample for attribute Target.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Meteorite'">
        <title>pds:Target role="warning"/pds:Target.type</title>
        The value Meteorite for attribute Target.type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target/pds:type">
      <sch:assert test=". = ('Asteroid', 'Astrophysical', 'Calibration', 'Calibration Field', 'Calibrator', 'Centaur', 'Comet', 'Dust', 'Dwarf Planet', 'Equipment', 'Exoplanet System', 'Galaxy', 'Globular Cluster', 'Laboratory Analog', 'Lunar Sample', 'Magnetic Field', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Plasma Stream', 'Ring', 'Sample', 'Satellite', 'Star', 'Star Cluster', 'Synthetic Sample', 'Terrestrial Sample', 'Trans-Neptunian Object')">
        <title>pds:Target/pds:type/pds:type</title>
        The attribute pds:Target/pds:type must be equal to one of the following values 'Asteroid', 'Astrophysical', 'Calibration', 'Calibration Field', 'Calibrator', 'Centaur', 'Comet', 'Dust', 'Dwarf Planet', 'Equipment', 'Exoplanet System', 'Galaxy', 'Globular Cluster', 'Laboratory Analog', 'Lunar Sample', 'Magnetic Field', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Plasma Stream', 'Ring', 'Sample', 'Satellite', 'Star', 'Star Cluster', 'Synthetic Sample', 'Terrestrial Sample', 'Trans-Neptunian Object'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target_Identification" role="warning">
      <sch:assert test="pds:type != 'Calibration'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Calibration for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Open Cluster'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Open Cluster for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Globular Cluster'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Globular Cluster for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Terrestrial Sample'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Terrestrial Sample for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Lunar Sample'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Lunar Sample for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Synthetic Sample'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Synthetic Sample for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="pds:type != 'Meteorite'">
        <title>pds:Target_Identification role="warning"/pds:Target_Identification.type</title>
        The value Meteorite for attribute Target_Identification.type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target_Identification/pds:type">
      <sch:assert test=". = ('Asteroid', 'Astrophysical', 'Calibration', 'Calibration Field', 'Calibrator', 'Centaur', 'Comet', 'Dust', 'Dwarf Planet', 'Equipment', 'Exoplanet System', 'Galaxy', 'Globular Cluster', 'Laboratory Analog', 'Lunar Sample', 'Magnetic Field', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Plasma Stream', 'Ring', 'Sample', 'Satellite', 'Star', 'Star Cluster', 'Synthetic Sample', 'Terrestrial Sample', 'Trans-Neptunian Object')">
        <title>pds:Target_Identification/pds:type/pds:type</title>
        The attribute pds:Target_Identification/pds:type must be equal to one of the following values 'Asteroid', 'Astrophysical', 'Calibration', 'Calibration Field', 'Calibrator', 'Centaur', 'Comet', 'Dust', 'Dwarf Planet', 'Equipment', 'Exoplanet System', 'Galaxy', 'Globular Cluster', 'Laboratory Analog', 'Lunar Sample', 'Magnetic Field', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Plasma Stream', 'Ring', 'Sample', 'Satellite', 'Star', 'Star Cluster', 'Synthetic Sample', 'Terrestrial Sample', 'Trans-Neptunian Object'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:altitude">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>pds:Telescope/pds:altitude/pds:altitude</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:altitude" role="warning">
      <sch:assert test="false()">
        <title>pds:Telescope/pds:altitude role="warning"/pds:Telescope.altitude</title>
        pds:Telescope/pds:altitude is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:aperture">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>pds:Telescope/pds:aperture/pds:aperture</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:telescope_altitude">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>pds:Telescope/pds:telescope_altitude/pds:telescope_altitude</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:telescope_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>pds:Telescope/pds:telescope_latitude/pds:telescope_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:telescope_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>pds:Telescope/pds:telescope_longitude/pds:telescope_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry">
      <sch:assert test="if (pds:preferred_flag) then pds:preferred_flag = ('true', 'false') else true()">
        <title>pds:Terminological_Entry/pds:preferred_flag</title>
        The attribute pds:preferred_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry/pds:language">
      <sch:assert test=". = ('English', 'Russian')">
        <title>pds:Terminological_Entry/pds:language/pds:language</title>
        The attribute pds:Terminological_Entry/pds:language must be equal to one of the following values 'English', 'Russian'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry/pds:skos_relation_name">
      <sch:assert test=". = ('broadMatch', 'closeMatch', 'exactMatch', 'narrowMatch', 'relatedMatch')">
        <title>pds:Terminological_Entry/pds:skos_relation_name/pds:skos_relation_name</title>
        The attribute pds:Terminological_Entry/pds:skos_relation_name must be equal to one of the following values 'broadMatch', 'closeMatch', 'exactMatch', 'narrowMatch', 'relatedMatch'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry_SKOS/pds:model_object_type">
      <sch:assert test=". = ('Attribute', 'Class', 'Keyword', 'Nuance', 'Value')">
        <title>pds:Terminological_Entry_SKOS/pds:model_object_type/pds:model_object_type</title>
        The attribute pds:Terminological_Entry_SKOS/pds:model_object_type must be equal to one of the following values 'Attribute', 'Class', 'Keyword', 'Nuance', 'Value'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry_SKOS/pds:registration_authority">
      <sch:assert test=". = ('PDS3', 'PDS4', 'VICAR')">
        <title>pds:Terminological_Entry_SKOS/pds:registration_authority/pds:registration_authority</title>
        The attribute pds:Terminological_Entry_SKOS/pds:registration_authority must be equal to one of the following values 'PDS3', 'PDS4', 'VICAR'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry_SKOS/pds:skos_relation_name">
      <sch:assert test=". = ('broadMatch', 'closeMatch', 'exactMatch', 'narrowMatch', 'relatedMatch')">
        <title>pds:Terminological_Entry_SKOS/pds:skos_relation_name/pds:skos_relation_name</title>
        The attribute pds:Terminological_Entry_SKOS/pds:skos_relation_name must be equal to one of the following values 'broadMatch', 'closeMatch', 'exactMatch', 'narrowMatch', 'relatedMatch'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Time_Coordinates/pds:solar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>pds:Time_Coordinates/pds:solar_longitude/pds:solar_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest" role="warning">
      <sch:assert test="pds:record_delimiter != 'carriage-return line-feed'">
        <title>pds:Transfer_Manifest role="warning"/pds:Transfer_Manifest.record_delimiter</title>
        The value carriage-return line-feed for attribute Transfer_Manifest.record_delimiter is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest/pds:record_delimiter">
      <sch:assert test=". = ('Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed')">
        <title>pds:Transfer_Manifest/pds:record_delimiter/pds:record_delimiter</title>
        The attribute pds:Transfer_Manifest/pds:record_delimiter must be equal to one of the following values 'Carriage-Return Line-Feed', 'Line-Feed', 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Uniformly_Sampled">
      <sch:let name="collScaleRef" value="pds:sampling_parameter_scale"/>
      <sch:let name="collBaseRef" value="pds:sampling_parameter_base"/>
      <sch:let name="refExpType" value="every $ref in ($collScaleRef) satisfies $ref = ('Exponential')"/>
      <sch:let name="refNonExpType" value="every $ref in ($collScaleRef) satisfies $ref = ('Linear', 'Logarithmic')"/>
      <sch:let name="purposeType" value="every $ref in ($collBaseRef) satisfies $ref > 0"/>
      <sch:assert test="if ($refNonExpType) then (not($collBaseRef)) else true()">
        <title>pds:Uniformly_Sampled/sampling_parameter_scale</title>
        For 'pds:Uniformly_Sampled/pds:sampling_parameter_scale' not equal 'Exponential', 'pds:Uniformly_Sampled/pds:sampling_parameter_base' must not exist.</sch:assert>
      <sch:assert test="if ($refExpType) then ($collBaseRef) else true()">
        <title>pds:Uniformly_Sampled/sampling_parameter_scale</title>
        For 'pds:Uniformly_Sampled/pds:sampling_parameter_scale' = 'Exponential', 'pds:Uniformly_Sampled/pds:sampling_parameter_base' must exist.</sch:assert>
      <sch:assert test="if ($refExpType) then ($purposeType) else true()">
        <title>pds:Uniformly_Sampled/sampling_parameter_scale</title>
        For 'pds:Uniformly_Sampled/pds:sampling_parameter_scale' = 'Exponential', 'pds:Uniformly_Sampled/pds:sampling_parameter_base' must have a value > '0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Uniformly_Sampled/pds:sampling_parameter_scale">
      <sch:assert test=". = ('Exponential', 'Linear', 'Logarithmic')">
        <title>pds:Uniformly_Sampled/pds:sampling_parameter_scale/pds:sampling_parameter_scale</title>
        The attribute pds:Uniformly_Sampled/pds:sampling_parameter_scale must be equal to one of the following values 'Exponential', 'Linear', 'Logarithmic'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Uniformly_Sampled/pds:sampling_parameters" role="warning">
      <sch:assert test="false()">
        <title>pds:Uniformly_Sampled/pds:sampling_parameters role="warning"/pds:Uniformly_Sampled.sampling_parameters</title>
        pds:Uniformly_Sampled/pds:sampling_parameters is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Units_of_Map_Scale" role="warning">
      <sch:assert test="false()">
        <title>pds:Units_of_Map_Scale role="warning"/pds:Units_of_Map_Scale</title>
        pds:Units_of_Map_Scale is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Update" role="warning">
      <sch:assert test="false()">
        <title>pds:Update role="warning"/pds:Update</title>
        pds:Update is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Update/pds:update_purpose">
      <sch:assert test=". = ('Update Label Metadata', 'Update Supplemental Metadata')">
        <title>pds:Update/pds:update_purpose/pds:update_purpose</title>
        The attribute pds:Update/pds:update_purpose must be equal to one of the following values 'Update Label Metadata', 'Update Supplemental Metadata'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Update/pds:update_purpose" role="warning">
      <sch:assert test="false()">
        <title>pds:Update/pds:update_purpose role="warning"/pds:Update.update_purpose</title>
        pds:Update/pds:update_purpose is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector" role="warning">
      <sch:assert test="false()">
        <title>pds:Vector role="warning"/pds:Vector</title>
        pds:Vector is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector/pds:data_type">
      <sch:assert test=". = ('ASCII_Real')">
        <title>pds:Vector/pds:data_type/pds:data_type</title>
        The attribute pds:Vector/pds:data_type must be equal to the value 'ASCII_Real'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector/pds:reference_frame_id">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('ICRF', 'MOON_ME_DE421')))) then false() else true()">
        <title>pds:Vector/pds:reference_frame_id/pds:reference_frame_id</title>
        The attribute pds:Vector/pds:reference_frame_id must be nulled or equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector/pds:type">
      <sch:assert test=". = ('Acceleration', 'Pointing', 'Position', 'Velocity')">
        <title>pds:Vector/pds:type/pds:type</title>
        The attribute pds:Vector/pds:type must be equal to one of the following values 'Acceleration', 'Pointing', 'Position', 'Velocity'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3" role="warning">
      <sch:assert test="false()">
        <title>pds:Vector_Cartesian_3 role="warning"/pds:Vector_Cartesian_3</title>
        pds:Vector_Cartesian_3 is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3/pds:reference_frame_id">
      <sch:assert test=". = ('ICRF', 'MOON_ME_DE421')">
        <title>pds:Vector_Cartesian_3/pds:reference_frame_id/pds:reference_frame_id</title>
        The attribute pds:Vector_Cartesian_3/pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Acceleration" role="warning">
      <sch:assert test="false()">
        <title>pds:Vector_Cartesian_3_Acceleration role="warning"/pds:Vector_Cartesian_3_Acceleration</title>
        pds:Vector_Cartesian_3_Acceleration is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Acceleration/pds:reference_frame_id">
      <sch:assert test=". = ('ICRF', 'MOON_ME_DE421')">
        <title>pds:Vector_Cartesian_3_Acceleration/pds:reference_frame_id/pds:reference_frame_id</title>
        The attribute pds:Vector_Cartesian_3_Acceleration/pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Pointing/pds:reference_frame_id">
      <sch:assert test=". = ('ICRF', 'MOON_ME_DE421')">
        <title>pds:Vector_Cartesian_3_Pointing/pds:reference_frame_id/pds:reference_frame_id</title>
        The attribute pds:Vector_Cartesian_3_Pointing/pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Position" role="warning">
      <sch:assert test="false()">
        <title>pds:Vector_Cartesian_3_Position role="warning"/pds:Vector_Cartesian_3_Position</title>
        pds:Vector_Cartesian_3_Position is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Position/pds:reference_frame_id">
      <sch:assert test=". = ('ICRF', 'MOON_ME_DE421')">
        <title>pds:Vector_Cartesian_3_Position/pds:reference_frame_id/pds:reference_frame_id</title>
        The attribute pds:Vector_Cartesian_3_Position/pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Velocity" role="warning">
      <sch:assert test="false()">
        <title>pds:Vector_Cartesian_3_Velocity role="warning"/pds:Vector_Cartesian_3_Velocity</title>
        pds:Vector_Cartesian_3_Velocity is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Velocity/pds:reference_frame_id">
      <sch:assert test=". = ('ICRF', 'MOON_ME_DE421')">
        <title>pds:Vector_Cartesian_3_Velocity/pds:reference_frame_id/pds:reference_frame_id</title>
        The attribute pds:Vector_Cartesian_3_Velocity/pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Component" role="warning">
      <sch:assert test="false()">
        <title>pds:Vector_Component role="warning"/pds:Vector_Component</title>
        pds:Vector_Component is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Volume_PDS3/pds:archive_status">
      <sch:assert test=". = ('ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED')">
        <title>pds:Volume_PDS3/pds:archive_status/pds:archive_status</title>
        The attribute pds:Volume_PDS3/pds:archive_status must be equal to one of the following values 'ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:XML_Schema">
      <sch:assert test="pds:offset eq '0'">
        <title>pds:XML_Schema/offset</title>
        XML_Schema.offset must have a value of '0'</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:XML_Schema/pds:parsing_standard_id">
      <sch:assert test=". = ('Schematron ISO/IEC 19757-3:2006', 'XML Schema Version 1.1')">
        <title>pds:XML_Schema/pds:parsing_standard_id/pds:parsing_standard_id</title>
        The attribute pds:XML_Schema/pds:parsing_standard_id must be equal to one of the following values 'Schematron ISO/IEC 19757-3:2006', 'XML Schema Version 1.1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Zip/pds:container_type">
      <sch:assert test=". = ('GZIP', 'LZIP', 'TAR', 'ZIP')">
        <title>pds:Zip/pds:container_type/pds:container_type</title>
        The attribute pds:Zip/pds:container_type must be equal to one of the following values 'GZIP', 'LZIP', 'TAR', 'ZIP'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
