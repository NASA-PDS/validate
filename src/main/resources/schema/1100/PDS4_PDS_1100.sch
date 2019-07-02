<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:pds  Version:1.1.0.0 - Tue Oct 01 08:24:06 PDT 2013 -->
  <!-- Generated from the PDS4 Information Model Version 1.1.0.0 - System Build 4a -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

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
      <sch:let name="num_colons" value="string-length(./pds:Identification_Area/pds:logical_identifier) - string-length(translate(./pds:Identification_Area/pds:logical_identifier, ':', ''))"/>
      <sch:let name="required_colons" value="5"/>
      <sch:let name="product_name" value="Product_Observational"/>
      <sch:assert test="if ((not (contains(name(), 'Ingest'))) and (not (contains(name(), 'Bundle'))) and (not (contains(name(), 'Collection')))) then $num_colons eq $required_colons else true()">
        In <sch:value-of select="name()"/>, the number of colons found: (<sch:value-of select="$num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$required_colons"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Agency">
      <sch:assert test="if (pds:name) then pds:name = ('European Space Agency', 'National Aeronautics and Space Administration') else true()">
        The attribute pds:name must be equal to one of the following values 'European Space Agency', 'National Aeronautics and Space Administration'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array">
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array/pds:offset">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_1D">
      <sch:assert test="if (pds:axes) then pds:axes = ('1') else true()">
        The attribute pds:axes must be equal to the value '1'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D">
      <sch:assert test="if (pds:axes) then pds:axes = ('2') else true()">
        The attribute pds:axes must be equal to the value '2'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image">
      <sch:assert test="if (pds:axes) then pds:axes = ('2') else true()">
        The attribute pds:axes must be equal to the value '2'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image/pds:Axis_Array[1]">
      <sch:assert test="pds:axis_name = ('Line', 'Sample')">
        The name of the first axis of an Array_2D_Image must be set to either Line or Sample.</sch:assert>
      <sch:assert test="pds:sequence_number eq '1'">
        The sequence number of the first axis of an Array_2D_Image must be set to 1.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Image/pds:Axis_Array[2]">
      <sch:assert test="pds:axis_name = ('Line', 'Sample')">
        The name of the second axis of an Array_2D_Image must be set to either Line or Sample.</sch:assert>
      <sch:assert test="pds:sequence_number eq '2'">
        The sequence number of the second axis of an Array_2D_Image must be set to 2.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Map">
      <sch:assert test="if (pds:axes) then pds:axes = ('2') else true()">
        The attribute pds:axes must be equal to the value '2'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_2D_Spectrum">
      <sch:assert test="if (pds:axes) then pds:axes = ('2') else true()">
        The attribute pds:axes must be equal to the value '2'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D">
      <sch:assert test="if (pds:axes) then pds:axes = ('3') else true()">
        The attribute pds:axes must be equal to the value '3'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Image">
      <sch:assert test="if (pds:axes) then pds:axes = ('3') else true()">
        The attribute pds:axes must be equal to the value '3'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Movie">
      <sch:assert test="if (pds:axes) then pds:axes = ('3') else true()">
        The attribute pds:axes must be equal to the value '3'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Spectrum">
      <sch:assert test="if (pds:axes) then pds:axes = ('3') else true()">
        The attribute pds:axes must be equal to the value '3'.</sch:assert>
      <sch:assert test="if (pds:axis_index_order) then pds:axis_index_order = ('Last Index Fastest') else true()">
        The attribute pds:axis_index_order must be equal to the value 'Last Index Fastest'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Array_3D_Spectrum/pds:Axis_Array">
      <sch:assert test="(pds:axis_name = 'Band' and pds:Band_Bin_Set) or pds:axis_name != 'Band'">
        In an Array_3D_Spectrum, if the axis_name is 'Band', then the Band_Bin_Set class must be present.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Axis_Array/pds:unit">
      <sch:assert test="name() = 'pds:Axis_Array/pds:unit'">
        pds:Axis_Array/pds:unit is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Band_Bin_Set">
      <sch:assert test="name() = 'pds:Band_Bin_Set'">
        pds:Band_Bin_Set is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle">
      <sch:assert test="if (pds:bundle_type) then pds:bundle_type = ('Archive', 'Supplemental') else true()">
        The attribute pds:bundle_type must be equal to one of the following values 'Archive', 'Supplemental'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Bundle_Member_Entry">
      <sch:let name="lid_num_colons" value="string-length(pds:lid_reference) - string-length(translate(pds:lid_reference, ':', ''))"/>
      <sch:let name="lidvid_num_colons" value="string-length(pds:lidvid_reference) - string-length(translate(pds:lidvid_reference, ':', ''))"/>
      <sch:let name="lid_required_colons" value="4"/>
      <sch:let name="lidvid_required_colons" value="6"/>
      <sch:assert test="if (pds:lid_reference) then ($lid_num_colons eq $lid_required_colons) else true()">
        The number of colons found in lid_reference: (<sch:value-of select="$lid_num_colons"/>) is inconsistent with the number expected: <sch:value-of select="$lid_required_colons"/>.</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then ($lidvid_num_colons eq $lidvid_required_colons) else true()">
        The number of colons found in lidvid_reference: (<sch:value-of select="$lidvid_num_colons"/>) is inconsistent with the number expected: <sch:value-of select="$lidvid_required_colons"/>.</sch:assert>
      <sch:assert test="if (pds:lid_reference) then starts-with(pds:lid_reference,'urn:nasa:pds:') else true()">
        The value of the attribute lid_reference must start with 'urn:nasa:pds:'</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then starts-with(pds:lidvid_reference,'urn:nasa:pds:') else true()">
        The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'</sch:assert>
      <sch:assert test="if (pds:lid_reference) then not(contains(pds:lid_reference,'::')) else true()">
        The value of the attribute lid_reference must not include a value that contains '::' followed by version id</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()">
        The value of the attribute lidvid_reference must include a value that contains '::' followed by version id</sch:assert>
      <sch:assert test="if (pds:reference_type) then pds:reference_type = ('bundle_has_browse_collection', 'bundle_has_calibration_collection', 'bundle_has_context_collection', 'bundle_has_data_collection', 'bundle_has_document_collection', 'bundle_has_geometry_collection', 'bundle_has_member_collection', 'bundle_has_schema_collection', 'bundle_has_spice_kernel_collection') else true()">
        The attribute pds:reference_type must be equal to one of the following values 'bundle_has_browse_collection', 'bundle_has_calibration_collection', 'bundle_has_context_collection', 'bundle_has_data_collection', 'bundle_has_document_collection', 'bundle_has_geometry_collection', 'bundle_has_member_collection', 'bundle_has_schema_collection', 'bundle_has_spice_kernel_collection'.</sch:assert>
      <sch:assert test="if (pds:member_status) then pds:member_status = ('Primary', 'Secondary') else true()">
        The attribute pds:member_status must be equal to one of the following values 'Primary', 'Secondary'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Checksum_Manifest">
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('MD5Deep 4.n') else true()">
        The attribute pds:parsing_standard_id must be equal to the value 'MD5Deep 4.n'.</sch:assert>
      <sch:assert test="if (pds:record_delimiter) then pds:record_delimiter = ('carriage-return line-feed') else true()">
        The attribute pds:record_delimiter must be equal to the value 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//pds:Citation_Information/pds:description">
      <sch:assert test="string-length(translate(., ' ', '')) &gt;= 1 and string-length(translate(., ' ','')) &lt;= 5000">
        The description in Citation_Information must be greater than 1 and less than 5000 bytes (not counting spaces).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Collection">
      <sch:assert test="if (pds:collection_type) then pds:collection_type = ('Browse', 'Calibration', 'Context', 'Data', 'Document', 'Geometry', 'Miscellaneous', 'SPICE Kernel', 'XML Schema') else true()">
        The attribute pds:collection_type must be equal to one of the following values 'Browse', 'Calibration', 'Context', 'Data', 'Document', 'Geometry', 'Miscellaneous', 'SPICE Kernel', 'XML Schema'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association">
      <sch:assert test="if (pds:reference_type) then pds:reference_type = ('attribute_of', 'component_of', 'extension_of', 'restriction_of', 'subclass_of') else true()">
        The attribute pds:reference_type must be equal to one of the following values 'attribute_of', 'component_of', 'extension_of', 'restriction_of', 'subclass_of'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Association_External">
      <sch:assert test="if (pds:reference_type) then pds:reference_type = ('attribute_of', 'component_of', 'extension_of', 'restriction_of', 'subclass_of') else true()">
        The attribute pds:reference_type must be equal to one of the following values 'attribute_of', 'component_of', 'extension_of', 'restriction_of', 'subclass_of'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Attribute_Full">
      <sch:assert test="if (pds:attribute_concept) then pds:attribute_concept = ('ADDRESS', 'ANGLE', 'ATTRIBUTE', 'BIT', 'CHECKSUM', 'COLLECTION', 'CONSTANT', 'COSINE', 'COUNT', 'DELIMITER', 'DESCRIPTION', 'DEVIATION', 'DIRECTION', 'DISTANCE', 'DOI', 'DURATION', 'FACTOR', 'FLAG', 'FORMAT', 'GROUP', 'HOME', 'ID', 'LATITUDE', 'LENGTH', 'LIST', 'LOCATION', 'LOGICAL', 'LONGITUDE', 'MASK', 'MAXIMUM', 'MEAN', 'MEDIAN', 'MINIMUM', 'NAME', 'NOTE', 'NUMBER', 'OFFSET', 'ORDER', 'PARALLEL', 'PASSWORD', 'PATH', 'PATTERN', 'PIXEL', 'QUATERNION', 'RADIUS', 'RATIO', 'REFERENCE', 'RESOLUTION', 'ROLE', 'ROTATION', 'SCALE', 'SEQUENCE', 'SET', 'SIZE', 'STATUS', 'SUMMARY', 'SYNTAX', 'TEMPERATURE', 'TEXT', 'TITLE', 'TYPE', 'UNIT', 'UNKNOWN', 'VALUE', 'VECTOR') else true()">
        The attribute pds:attribute_concept must be equal to one of the following values 'ADDRESS', 'ANGLE', 'ATTRIBUTE', 'BIT', 'CHECKSUM', 'COLLECTION', 'CONSTANT', 'COSINE', 'COUNT', 'DELIMITER', 'DESCRIPTION', 'DEVIATION', 'DIRECTION', 'DISTANCE', 'DOI', 'DURATION', 'FACTOR', 'FLAG', 'FORMAT', 'GROUP', 'HOME', 'ID', 'LATITUDE', 'LENGTH', 'LIST', 'LOCATION', 'LOGICAL', 'LONGITUDE', 'MASK', 'MAXIMUM', 'MEAN', 'MEDIAN', 'MINIMUM', 'NAME', 'NOTE', 'NUMBER', 'OFFSET', 'ORDER', 'PARALLEL', 'PASSWORD', 'PATH', 'PATTERN', 'PIXEL', 'QUATERNION', 'RADIUS', 'RATIO', 'REFERENCE', 'RESOLUTION', 'ROLE', 'ROTATION', 'SCALE', 'SEQUENCE', 'SET', 'SIZE', 'STATUS', 'SUMMARY', 'SYNTAX', 'TEMPERATURE', 'TEXT', 'TITLE', 'TYPE', 'UNIT', 'UNKNOWN', 'VALUE', 'VECTOR'.</sch:assert>
      <sch:assert test="if (pds:type) then pds:type = ('PDS3', 'PDS4') else true()">
        The attribute pds:type must be equal to one of the following values 'PDS3', 'PDS4'.</sch:assert>
      <sch:assert test="if (pds:registration_authority_id) then pds:registration_authority_id = ('0001_NASA_PDS_1') else true()">
        The attribute pds:registration_authority_id must be equal to the value '0001_NASA_PDS_1'.</sch:assert>
      <sch:assert test="if (pds:steward_id) then pds:steward_id = ('atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn') else true()">
        The attribute pds:steward_id must be equal to one of the following values 'atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn'.</sch:assert>
      <sch:assert test="if (pds:nillable_flag) then pds:nillable_flag = ('true', 'false') else true()">
        The attribute pds:nillable_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Class_Full">
      <sch:assert test="if (pds:type) then pds:type = ('PDS3', 'PDS4') else true()">
        The attribute pds:type must be equal to one of the following values 'PDS3', 'PDS4'.</sch:assert>
      <sch:assert test="if (pds:steward_id) then pds:steward_id = ('atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn') else true()">
        The attribute pds:steward_id must be equal to one of the following values 'atm', 'geo', 'img', 'naif', 'ops', 'pds', 'ppi', 'rings', 'rs', 'sbn'.</sch:assert>
      <sch:assert test="if (pds:abstract_flag) then pds:abstract_flag = ('true', 'false') else true()">
        The attribute pds:abstract_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain">
      <sch:assert test="if (pds:unit_of_measure_type) then pds:unit_of_measure_type = ('Units_of_Acceleration', 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume') else true()">
        The attribute pds:unit_of_measure_type must be equal to one of the following values 'Units_of_Acceleration', 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume'.</sch:assert>
      <sch:assert test="if (pds:value_data_type) then pds:value_data_type = ('ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Preserved', 'Vector_Cartesian_3', 'Vector_Cartesian_3_Acceleration', 'Vector_Cartesian_3_Pointing', 'Vector_Cartesian_3_Position', 'Vector_Cartesian_3_Velocity') else true()">
        The attribute pds:value_data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Preserved', 'Vector_Cartesian_3', 'Vector_Cartesian_3_Acceleration', 'Vector_Cartesian_3_Pointing', 'Vector_Cartesian_3_Position', 'Vector_Cartesian_3_Velocity'.</sch:assert>
      <sch:assert test="if (pds:enumeration_flag) then pds:enumeration_flag = ('true', 'false') else true()">
        The attribute pds:enumeration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:DD_Value_Domain_Full">
      <sch:assert test="if (pds:unit_of_measure_type) then pds:unit_of_measure_type = ('Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume') else true()">
        The attribute pds:unit_of_measure_type must be equal to one of the following values 'Units_of_Amount_Of_Substance', 'Units_of_Angle', 'Units_of_Angular_Velocity', 'Units_of_Area', 'Units_of_Frame_Rate', 'Units_of_Frequency', 'Units_of_Length', 'Units_of_Map_Scale', 'Units_of_Mass', 'Units_of_Misc', 'Units_of_None', 'Units_of_Optical_Path_Length', 'Units_of_Pressure', 'Units_of_Radiance', 'Units_of_Rates', 'Units_of_Solid_Angle', 'Units_of_Storage', 'Units_of_Temperature', 'Units_of_Time', 'Units_of_Velocity', 'Units_of_Voltage', 'Units_of_Volume'.</sch:assert>
      <sch:assert test="if (pds:value_data_type) then pds:value_data_type = ('ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Preserved') else true()">
        The attribute pds:value_data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_Short_String_Collapsed', 'ASCII_Short_String_Preserved', 'ASCII_Text_Collapsed', 'ASCII_Text_Preserved', 'ASCII_Time', 'ASCII_VID', 'UTF8_Short_String_Collapsed', 'UTF8_Short_String_Preserved', 'UTF8_Text_Preserved'.</sch:assert>
      <sch:assert test="if (pds:conceptual_domain) then pds:conceptual_domain = ('BOOLEAN', 'INTEGER', 'NAME', 'NUMERIC', 'REAL', 'SHORT_STRING', 'TEXT', 'TIME', 'TYPE', 'UNKNOWN') else true()">
        The attribute pds:conceptual_domain must be equal to one of the following values 'BOOLEAN', 'INTEGER', 'NAME', 'NUMERIC', 'REAL', 'SHORT_STRING', 'TEXT', 'TIME', 'TYPE', 'UNKNOWN'.</sch:assert>
      <sch:assert test="if (pds:enumeration_flag) then pds:enumeration_flag = ('true', 'false') else true()">
        The attribute pds:enumeration_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Data_Set_PDS3">
      <sch:assert test="if (pds:archive_status) then pds:archive_status = ('ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED') else true()">
        The attribute pds:archive_status must be equal to one of the following values 'ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Display_2D_Image">
      <sch:assert test="name() = 'pds:Display_2D_Image'">
        pds:Display_2D_Image is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Document_File">
      <sch:assert test="if (pds:document_standard_id) then pds:document_standard_id = ('7-Bit ASCII Text', 'Encapsulated Postscript', 'GIF', 'HTML 2.0', 'HTML 3.2', 'HTML 4.0', 'HTML 4.01', 'JPEG', 'LaTEX', 'Microsoft Word', 'PDF', 'PDF/A', 'PNG', 'Postscript', 'Rich Text', 'TIFF', 'UTF-8 Text') else true()">
        The attribute pds:document_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'Encapsulated Postscript', 'GIF', 'HTML 2.0', 'HTML 3.2', 'HTML 4.0', 'HTML 4.01', 'JPEG', 'LaTEX', 'Microsoft Word', 'PDF', 'PDF/A', 'PNG', 'Postscript', 'Rich Text', 'TIFF', 'UTF-8 Text'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Document_Format">
      <sch:assert test="if (pds:format_type) then pds:format_type = ('multiple file', 'single file') else true()">
        The attribute pds:format_type must be equal to one of the following values 'multiple file', 'single file'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Element_Array">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8') else true()">
        The attribute pds:data_type must be equal to one of the following values 'ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Binary">
      <sch:assert test="if (pds:encoding_standard_id) then pds:encoding_standard_id = ('CCSDS Communication Protocols') else true()">
        The attribute pds:encoding_standard_id must be equal to the value 'CCSDS Communication Protocols'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Header">
      <sch:assert test="if (pds:encoding_standard_id) then pds:encoding_standard_id = ('TIFF') else true()">
        The attribute pds:encoding_standard_id must be equal to the value 'TIFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Encoded_Image">
      <sch:assert test="if (pds:encoding_standard_id) then pds:encoding_standard_id = ('GIF', 'J2C', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF') else true()">
        The attribute pds:encoding_standard_id must be equal to one of the following values 'GIF', 'J2C', 'JPEG', 'PDF', 'PDF/A', 'PNG', 'TIFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Facility">
      <sch:assert test="if (pds:type) then pds:type = ('Laboratory', 'Observatory') else true()">
        The attribute pds:type must be equal to one of the following values 'Laboratory', 'Observatory'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UTF8_String', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8') else true()">
        The attribute pds:data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'ComplexLSB16', 'ComplexLSB8', 'ComplexMSB16', 'ComplexMSB8', 'IEEE754LSBDouble', 'IEEE754LSBSingle', 'IEEE754MSBDouble', 'IEEE754MSBSingle', 'SignedBitString', 'SignedByte', 'SignedLSB2', 'SignedLSB4', 'SignedLSB8', 'SignedMSB2', 'SignedMSB4', 'SignedMSB8', 'UTF8_String', 'UnsignedBitString', 'UnsignedByte', 'UnsignedLSB2', 'UnsignedLSB4', 'UnsignedLSB8', 'UnsignedMSB2', 'UnsignedMSB4', 'UnsignedMSB8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary/pds:field_length">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Binary/pds:field_location">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Bit">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('SignedBitString', 'UnsignedBitString') else true()">
        The attribute pds:data_type must be equal to one of the following values 'SignedBitString', 'UnsignedBitString'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String') else true()">
        The attribute pds:data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character/pds:field_length">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Character/pds:field_location">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Delimited">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String') else true()">
        The attribute pds:data_type must be equal to one of the following values 'ASCII_AnyURI', 'ASCII_Boolean', 'ASCII_DOI', 'ASCII_Date', 'ASCII_Date_DOY', 'ASCII_Date_Time', 'ASCII_Date_Time_DOY', 'ASCII_Date_Time_UTC', 'ASCII_Date_Time_YMD', 'ASCII_Date_YMD', 'ASCII_Directory_Path_Name', 'ASCII_File_Name', 'ASCII_File_Specification_Name', 'ASCII_Integer', 'ASCII_LID', 'ASCII_LIDVID', 'ASCII_LIDVID_LID', 'ASCII_MD5_Checksum', 'ASCII_NonNegative_Integer', 'ASCII_Numeric_Base16', 'ASCII_Numeric_Base2', 'ASCII_Numeric_Base8', 'ASCII_Real', 'ASCII_String', 'ASCII_Time', 'ASCII_VID', 'UTF8_String'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Field_Delimited/pds:maximum_field_length">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Header">
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'FITS 3.0', 'ISIS2', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'UTF-8 Text', 'VICAR1', 'VICAR2') else true()">
        The attribute pds:parsing_standard_id must be equal to one of the following values '7-Bit ASCII Text', 'CDF 3.4 ISTP/IACG', 'FITS 3.0', 'ISIS2', 'ISIS3', 'PDS DSV 1', 'PDS ODL 2', 'PDS3', 'Pre-PDS3', 'UTF-8 Text', 'VICAR1', 'VICAR2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Header/pds:object_length">
      <sch:assert test="@unit = ('byte')">
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Identification_Area">
      <sch:assert test="pds:product_class = local-name(/*)">
        The attribute pds:product_class must match parent product class of '<sch:value-of select="local-name(/*)" />'.</sch:assert>
      <sch:assert test="pds:logical_identifier eq lower-case(pds:logical_identifier)">
        The value of the attribute logical_identifier must only contain lower-case letters'</sch:assert>
      <sch:assert test="if (pds:logical_identifier) then starts-with(pds:logical_identifier,'urn:nasa:pds:') else true()">
        The value of the attribute logical_identifier must start with 'urn:nasa:pds:'</sch:assert>
      <sch:assert test="if (pds:logical_identifier) then not(contains(pds:logical_identifier,'::')) else true()">
        The value of the attribute logical_identifier must not include a value that contains '::'</sch:assert>
      <sch:assert test="if (pds:product_class) then pds:product_class = ('Product_AIP', 'Product_Attribute_Definition', 'Product_Browse', 'Product_Bundle', 'Product_Class_Definition', 'Product_Collection', 'Product_Context', 'Product_DIP', 'Product_DIP_Deep_Archive', 'Product_Data_Set_PDS3', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3', 'Product_Mission_PDS3', 'Product_Observational', 'Product_Proxy_PDS3', 'Product_SIP', 'Product_SPICE_Kernel', 'Product_Service', 'Product_Software', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Thumbnail', 'Product_Update', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_XML_Schema', 'Product_Zipped') else true()">
        The attribute pds:product_class must be equal to one of the following values 'Product_AIP', 'Product_Attribute_Definition', 'Product_Browse', 'Product_Bundle', 'Product_Class_Definition', 'Product_Collection', 'Product_Context', 'Product_DIP', 'Product_DIP_Deep_Archive', 'Product_Data_Set_PDS3', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3', 'Product_Mission_PDS3', 'Product_Observational', 'Product_Proxy_PDS3', 'Product_SIP', 'Product_SPICE_Kernel', 'Product_Service', 'Product_Software', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Thumbnail', 'Product_Update', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_XML_Schema', 'Product_Zipped'.</sch:assert>
      <sch:assert test="if (pds:information_model_version) then pds:information_model_version = ('1.1.0.0') else true()">
        The attribute pds:information_model_version must be equal to the value '1.1.0.0'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="/*">
      <sch:assert test="name() = ('Product_Attribute_Definition','Product_Browse', 'Product_Bundle', 'Product_Class_Definition',  'Product_Collection', 'Product_Context', 'Product_Document', 'Product_File_Repository', 'Product_File_Text', 'Product_Observational', 'Product_Service', 'Product_Software', 'Product_SPICE_Kernel', 'Product_Thumbnail', 'Product_Update', 'Product_XML_Schema', 'Product_Zipped','Product_Data_Set_PDS3', 'Product_Instrument_Host_PDS3', 'Product_Instrument_PDS3','Product_Mission_PDS3', 'Product_Proxy_PDS3', 'Product_Subscription_PDS3', 'Product_Target_PDS3', 'Product_Volume_PDS3', 'Product_Volume_Set_PDS3', 'Product_AIP', 'Product_DIP', 'Product_SIP', 'Product_DIP_Deep_Archive', 'Ingest_LDD')">
        The ROOT element must be one of the allowed types.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument">
      <sch:assert test="if (pds:type) then pds:type = ('Accelerometer', 'Alpha Particle Detector', 'Alpha Particle Xray Spectrometer', 'Altimeter', 'Anemometer', 'Atomic Force Microscope', 'Barometer', 'Biology Experiments', 'Bolometer', 'Camera', 'Cosmic Ray Detector', 'Dust Detector', 'Electrical Probe', 'Energetic Particle Detector', 'Gamma Ray Detector', 'Gas Analyzer', 'Grinding And Drilling Tool', 'Hygrometer', 'Imager', 'Imaging Spectrometer', 'Inertial Measurement Unit', 'Infrared Spectrometer', 'Laser Induced Breakdown Spectrometer', 'Magnetometer', 'Mass Spectrometer', 'Microwave Spectrometer', 'Moessbauer Spectrometer', 'Naked Eye', 'Neutral Particle Detector', 'Neutron Detector', 'Photometer', 'Plasma Analyzer', 'Plasma Detector', 'Plasma Wave Spectrometer', 'Polarimeter', 'RADAR', 'Radio Science', 'Radio Spectrometer', 'Radio Telescope', 'Radiometer', 'Reflectometer', 'Robotic Arm', 'Spectrograph Imager', 'Spectrometer', 'Thermal And Electrical Conductivity Probe', 'Thermal Imager', 'Thermal Probe', 'Thermometer', 'Ultraviolet Spectrometer', 'Wet Chemistry Laboratory', 'X-ray Defraction Spectrometer', 'X-ray Detector', 'X-ray Fluorescence', 'X-ray Fluorescence Spectrometer') else true()">
        The attribute pds:type must be equal to one of the following values 'Accelerometer', 'Alpha Particle Detector', 'Alpha Particle Xray Spectrometer', 'Altimeter', 'Anemometer', 'Atomic Force Microscope', 'Barometer', 'Biology Experiments', 'Bolometer', 'Camera', 'Cosmic Ray Detector', 'Dust Detector', 'Electrical Probe', 'Energetic Particle Detector', 'Gamma Ray Detector', 'Gas Analyzer', 'Grinding And Drilling Tool', 'Hygrometer', 'Imager', 'Imaging Spectrometer', 'Inertial Measurement Unit', 'Infrared Spectrometer', 'Laser Induced Breakdown Spectrometer', 'Magnetometer', 'Mass Spectrometer', 'Microwave Spectrometer', 'Moessbauer Spectrometer', 'Naked Eye', 'Neutral Particle Detector', 'Neutron Detector', 'Photometer', 'Plasma Analyzer', 'Plasma Detector', 'Plasma Wave Spectrometer', 'Polarimeter', 'RADAR', 'Radio Science', 'Radio Spectrometer', 'Radio Telescope', 'Radiometer', 'Reflectometer', 'Robotic Arm', 'Spectrograph Imager', 'Spectrometer', 'Thermal And Electrical Conductivity Probe', 'Thermal Imager', 'Thermal Probe', 'Thermometer', 'Ultraviolet Spectrometer', 'Wet Chemistry Laboratory', 'X-ray Defraction Spectrometer', 'X-ray Detector', 'X-ray Fluorescence', 'X-ray Fluorescence Spectrometer'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Instrument_Host">
      <sch:assert test="pds:type != 'Earth Based'">
        The value Earth Based for attribute Instrument_Host.type is deprecated and should not be used.</sch:assert>
      <sch:assert test="if (pds:type) then pds:type = ('Earth-based', 'Lander', 'Rover', 'Spacecraft') else true()">
        The attribute pds:type must be equal to one of the following values 'Earth-based', 'Lander', 'Rover', 'Spacecraft'.</sch:assert>
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
      <sch:assert test="if (pds:lid_reference) then not(contains(pds:lid_reference,'::')) else true()">
        The value of the attribute lid_reference must not include a value that contains '::' followed by version id</sch:assert>
      <sch:assert test="if (pds:lid_reference) then (($lid_num_colons &gt;= $lid_min_required_colons) and ($lid_num_colons &lt;= $lid_max_required_colons)) else true()">
        The number of colons found in lid_reference: (<sch:value-of select="$lid_num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$lid_min_required_colons"/>:<sch:value-of select="$lid_max_required_colons"/>).</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then (($lidvid_num_colons &gt;= $lidvid_min_required_colons) and ($lidvid_num_colons &lt;= $lidvid_max_required_colons)) else true()">
        The number of colons found in lidvid_reference: (<sch:value-of select="$lidvid_num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$lidvid_min_required_colons"/>:<sch:value-of select="$lidvid_max_required_colons"/>).</sch:assert>
      <sch:assert test="if (pds:lid_reference) then starts-with(pds:lid_reference,'urn:nasa:pds:') else true()">
        The value of the attribute lid_reference must start with 'urn:nasa:pds:'</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then starts-with(pds:lidvid_reference,'urn:nasa:pds:') else true()">
        The value of the attribute lidvid_reference must start with 'urn:nasa:pds:'</sch:assert>
      <sch:assert test="if (pds:lidvid_reference) then contains(pds:lidvid_reference,'::') else true()">
        The value of the attribute lidvid_reference must include a value that contains '::' followed by version id</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory">
      <sch:assert test="((pds:reference_type eq 'inventory_has_member_product') and (count(pds:Record_Delimited/pds:Field_Delimited) eq 2))">
        Inventory.Field_Delimited does not match the expected number of instances</sch:assert>
      <sch:assert test="pds:offset eq '0'">
        Inventory.offset must have a value of '0'</sch:assert>
      <sch:assert test="if (pds:reference_type) then pds:reference_type = ('inventory_has_member_product') else true()">
        The attribute pds:reference_type must be equal to the value 'inventory_has_member_product'.</sch:assert>
      <sch:assert test="if (pds:record_delimiter) then pds:record_delimiter = ('carriage-return line-feed') else true()">
        The attribute pds:record_delimiter must be equal to the value 'carriage-return line-feed'.</sch:assert>
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('PDS DSV 1') else true()">
        The attribute pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
      <sch:assert test="if (pds:field_delimiter) then pds:field_delimiter = ('comma', 'horizontal tab', 'semicolon', 'vertical bar') else true()">
        The attribute pds:field_delimiter must be equal to one of the following values 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[1]">
      <sch:assert test="pds:field_number eq '1'">
        The first field of an Inventory must have field_number set to 1.</sch:assert>
      <sch:assert test="pds:maximum_field_length eq '1'">
        The first field of an Inventory must have maximum_field_length set to 1.</sch:assert>
      <sch:assert test="pds:data_type eq 'ASCII_String'">
        The first field of an Inventory must have data type set to 'ASCII_String'.</sch:assert>
      <sch:assert test="pds:name eq 'Member_Status'">
        The first field of an Inventory must have name set to 'Member_Status'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Inventory/pds:Record_Delimited/pds:Field_Delimited[2]">
      <sch:assert test="pds:field_number eq '2'">
        The second field of an Inventory must have field_number set to 2.</sch:assert>
      <sch:assert test="(pds:data_type eq 'ASCII_LIDVID_LID')">
        The second field of an Inventory must have data_type set to 'ASCII_LIDVID_LID'.</sch:assert>
      <sch:assert test="(pds:name eq 'LIDVID_LID')">
        The second field of an Inventory must have name set to 'LIDVID_LID'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Investigation">
      <sch:assert test="if (pds:type) then pds:type = ('Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation') else true()">
        The attribute pds:type must be equal to one of the following values 'Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Investigation_Area">
      <sch:assert test="if (pds:type) then pds:type = ('Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation') else true()">
        The attribute pds:type must be equal to one of the following values 'Individual Investigation', 'Mission', 'Observing Campaign', 'Other Investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Node">
      <sch:assert test="if (pds:name) then pds:name = ('Engineering', 'Geosciences', 'Imaging', 'Management', 'Navigation Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Planetary Science Archive', 'Radio Science', 'Small Bodies') else true()">
        The attribute pds:name must be equal to one of the following values 'Engineering', 'Geosciences', 'Imaging', 'Management', 'Navigation Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Planetary Science Archive', 'Radio Science', 'Small Bodies'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Observing_System_Component">
      <sch:assert test="if (pds:type) then pds:type = ('Artificial Illumination', 'Instrument', 'Laboratory', 'Literature Search', 'Naked Eye', 'Observatory', 'Spacecraft', 'Telescope') else true()">
        The attribute pds:type must be equal to one of the following values 'Artificial Illumination', 'Instrument', 'Laboratory', 'Literature Search', 'Naked Eye', 'Observatory', 'Spacecraft', 'Telescope'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Observing_System_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('is_instrument', 'is_instrument_host', 'is_other', 'is_facility', 'is_telescope')">
        The attribute reference_type must be set to one of the following values 'is_instrument', 'is_instrument_host', 'is_other', 'is_facility', 'is_telescope'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:PDS_Affiliate">
      <sch:assert test="if (pds:affiliation_type) then pds:affiliation_type = ('Affiliate', 'Data Provider', 'Manager', 'Technical Staff') else true()">
        The attribute pds:affiliation_type must be equal to one of the following values 'Affiliate', 'Data Provider', 'Manager', 'Technical Staff'.</sch:assert>
      <sch:assert test="if (pds:team_name) then pds:team_name = ('Engineering', 'Geosciences', 'Headquarters', 'Imaging', 'Management', 'National Space Science Data Center', 'Navigation Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Radio Science', 'Small Bodies') else true()">
        The attribute pds:team_name must be equal to one of the following values 'Engineering', 'Geosciences', 'Headquarters', 'Imaging', 'Management', 'National Space Science Data Center', 'Navigation Ancillary Information Facility', 'Planetary Atmospheres', 'Planetary Plasma Interactions', 'Planetary Rings', 'Radio Science', 'Small Bodies'.</sch:assert>
      <sch:assert test="if (pds:phone_book_flag) then pds:phone_book_flag = ('true', 'false') else true()">
        The attribute pds:phone_book_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary">
      <sch:assert test="if (pds:processing_level) then pds:processing_level = ('Calibrated', 'Derived', 'Partially Processed', 'Raw', 'Telemetry') else true()">
        The attribute pds:processing_level must be equal to one of the following values 'Calibrated', 'Derived', 'Partially Processed', 'Raw', 'Telemetry'.</sch:assert>
      <sch:assert test="if (pds:purpose) then pds:purpose = ('Calibration', 'Checkout', 'Engineering', 'Navigation', 'Science') else true()">
        The attribute pds:purpose must be equal to one of the following values 'Calibration', 'Checkout', 'Engineering', 'Navigation', 'Science'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:Science_Facets">
      <sch:assert test="if (pds:discipline_name) then pds:discipline_name = ('Atmospheres', 'Fields', 'Flux Measurements', 'Imaging', 'Particles', 'Ring-Moon Systems', 'Small Bodies', 'Spectroscopy') else true()">
        The attribute pds:discipline_name must be equal to one of the following values 'Atmospheres', 'Fields', 'Flux Measurements', 'Imaging', 'Particles', 'Ring-Moon Systems', 'Small Bodies', 'Spectroscopy'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Atmospheres')) then pds:facet1 = ('Structure', 'Meteorology') else true()">
        If the attribute pds:discipline_name equals Atmospheres then if present pds:facet1 must be equal to one of the following values 'Structure', 'Meteorology'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Fields')) then pds:facet1 = ('Electric', 'Magnetic') else true()">
        If the attribute pds:discipline_name equals Fields then if present pds:facet1 must be equal to one of the following values 'Electric', 'Magnetic'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet2 and (pds:discipline_name eq 'Fields')) then pds:facet2 = ('Background', 'Waves') else true()">
        If the attribute pds:discipline_name equals Fields then if present pds:facet2 must be equal to one of the following values 'Background', 'Waves'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Flux Measurements')) then pds:facet1 = ('Photometry', 'Polarimetry') else true()">
        If the attribute pds:discipline_name equals Flux Measurements then if present pds:facet1 must be equal to one of the following values 'Photometry', 'Polarimetry'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Imaging')) then pds:facet1 = ('Grayscale', 'Color', 'Movie', 'Color Movie') else true()">
        If the attribute pds:discipline_name equals Imaging then if present pds:facet1 must be equal to one of the following values 'Grayscale', 'Color', 'Movie', 'Color Movie'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Particles')) then pds:facet1 = ('Ions', 'Electrons', 'Neutrals') else true()">
        If the attribute pds:discipline_name equals Particles then if present pds:facet1 must be equal to one of the following values 'Ions', 'Electrons', 'Neutrals'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet2 and (pds:discipline_name eq 'Particles')) then pds:facet2 = ('Cosmic Ray', 'Solar Energetic', 'Energetic', 'Plasma') else true()">
        If the attribute pds:discipline_name equals Particles then if present pds:facet2 must be equal to one of the following values 'Cosmic Ray', 'Solar Energetic', 'Energetic', 'Plasma'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Ring-Moon Systems')) then pds:facet1 = ('Satellite Astrometry', 'Ring Compositional Map', 'Ring Occultation Profile', 'Ring Thermal Map') else true()">
        If the attribute pds:discipline_name equals Ring-Moon Systems then if present pds:facet1 must be equal to one of the following values 'Satellite Astrometry', 'Ring Compositional Map', 'Ring Occultation Profile', 'Ring Thermal Map'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Small Bodies')) then pds:facet1 = ('Dynamical Properties', 'Lightcurve', 'Meteoritics', 'Physical Properties', 'Production Rates', 'Shape Model', 'Taxonomy', 'Dust Study', 'Historical Reference', 'Gas Study') else true()">
        If the attribute pds:discipline_name equals Small Bodies then if present pds:facet1 must be equal to one of the following values 'Dynamical Properties', 'Lightcurve', 'Meteoritics', 'Physical Properties', 'Production Rates', 'Shape Model', 'Taxonomy', 'Dust Study', 'Historical Reference', 'Gas Study'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet1 and (pds:discipline_name eq 'Spectroscopy')) then pds:facet1 = ('2D', 'Linear', 'Spectral Cube', 'Spectral Image', 'Tabulated') else true()">
        If the attribute pds:discipline_name equals Spectroscopy then if present pds:facet1 must be equal to one of the following values '2D', 'Linear', 'Spectral Cube', 'Spectral Image', 'Tabulated'.</sch:assert>
      <sch:assert test="if (pds:discipline_name and pds:facet2) then pds:discipline_name = ('Fields', 'Particles') else true()">
        Facet2 is allowed only when pds:discipline_name is one of the following 'Fields', 'Particles'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet1'">
        pds:subfacet1 should not be used. No values have been provided.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:Science_Facets/pds:subfacet2'">
        pds:subfacet2 should not be used. No values have been provided.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:data_regime">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:data_regime'">
        pds:Primary_Result_Summary/pds:data_regime is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:processing_level_id">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:processing_level_id'">
        pds:Primary_Result_Summary/pds:processing_level_id is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Primary_Result_Summary/pds:type">
      <sch:assert test="name() = 'pds:Primary_Result_Summary/pds:type'">
        pds:Primary_Result_Summary/pds:type is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_AIP/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package')">
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Browse/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('browse_to_data', 'browse_to_thumbnail')">
        The attribute reference_type must be set to one of the following values 'browse_to_data', 'browse_to_thumbnail'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('bundle_to_investigation')">
        The attribute reference_type must be set to one of the following values 'bundle_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        In Product_Bundle a description is required in Citation_Information.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Identification_Area/pds:logical_identifier">
      <sch:let name="num_colons" value="string-length(.) - string-length(translate(., ':', ''))"/>
      <sch:let name="required_colons" value="3"/>
      <sch:assert test="$num_colons eq $required_colons">
        In Product_Bundle, the number of colons found: (<sch:value-of select="$num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$required_colons"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Bundle/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('bundle_to_errata', 'bundle_to_document', 'bundle_to_investigation', 'bundle_to_instrument', 'bundle_to_instrument_host', 'bundle_to_target', 'bundle_to_associate')">
        The attribute reference_type must be set to one of the following values 'bundle_to_errata', 'bundle_to_document', 'bundle_to_investigation', 'bundle_to_instrument', 'bundle_to_instrument_host', 'bundle_to_target', 'bundle_to_associate'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('collection_to_investigation')">
        The attribute reference_type must be set to one of the following values 'collection_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        In Product_Collection a description is required in Citation_Information.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Identification_Area/pds:logical_identifier">
      <sch:let name="num_colons" value="string-length(.) - string-length(translate(., ':', ''))"/>
      <sch:let name="required_colons" value="4"/>
      <sch:assert test="$num_colons eq $required_colons">
        In Product_Collection, the number of colons found: (<sch:value-of select="$num_colons"/>) is inconsistent with the number expected: (<sch:value-of select="$required_colons"/>).</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Collection/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('collection_to_resource', 'collection_to_associate', 'collection_to_calibration', 'collection_to_geometry', 'collection_to_spice_kernel', 'collection_curated_by_node', 'collection_to_document', 'collection_to_browse', 'collection_to_context', 'collection_to_data', 'collection_to_schema', 'collection_to_errata', 'collection_to_bundle', 'collection_to_personnel', 'collection_to_investigation', 'collection_to_instrument', 'collection_to_instrument_host', 'collection_to_target', 'collection_to_associate')">
        The attribute reference_type must be set to one of the following values 'collection_to_resource', 'collection_to_associate', 'collection_to_calibration', 'collection_to_geometry', 'collection_to_spice_kernel', 'collection_curated_by_node', 'collection_to_document', 'collection_to_browse', 'collection_to_context', 'collection_to_data', 'collection_to_schema', 'collection_to_errata', 'collection_to_bundle', 'collection_to_personnel', 'collection_to_investigation', 'collection_to_instrument', 'collection_to_instrument_host', 'collection_to_target', 'collection_to_associate'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Context/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('context_to_associate', 'instrument_host_to_investigation', 'instrument_host_to_document', 'instrument_host_to_target', 'instrument_to_instrument_host', 'instrument_to_document', 'investigation_to_target', 'investigation_to_document', 'node_to_personnel', 'node_to_agency', 'node_to_manager', 'node_to_operator', 'node_to_data_archivist', 'resource_to_instrument', 'resource_to_instrument_host', 'resource_to_investigation', 'resource_to_target', 'target_to_document')">
        The attribute reference_type must be set to one of the following values 'context_to_associate', 'instrument_host_to_investigation', 'instrument_host_to_document', 'instrument_host_to_target', 'instrument_to_instrument_host', 'instrument_to_document', 'investigation_to_target', 'investigation_to_document', 'node_to_personnel', 'node_to_agency', 'node_to_manager', 'node_to_operator', 'node_to_data_archivist', 'resource_to_instrument', 'resource_to_instrument_host', 'resource_to_investigation', 'resource_to_target', 'target_to_document'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_DIP/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package')">
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_DIP_Deep_Archive/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package')">
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product', 'package_compiled_from_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('document_to_investigation')">
        The attribute reference_type must be set to one of the following values 'document_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Context_Area/pds:Target_Identification/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('document_to_target')">
        The attribute reference_type must be set to one of the following values 'document_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        In Product_Document a description is required in Citation_Information.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Document/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('document_to_associate', 'document_to_investigation', 'document_to_instrument_host', 'document_to_instrument', 'document_to_target')">
        The attribute reference_type must be set to one of the following values 'document_to_associate', 'document_to_investigation', 'document_to_instrument_host', 'document_to_instrument', 'document_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_File_Text/pds:Identification_Area">
      <sch:assert test="pds:Citation_Information/pds:description">
        In Product_File_Text a description is required in Citation_Information.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Observational/pds:Context_Area/pds:Investigation_Area/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_investigation')">
        The attribute reference_type must be set to one of the following values 'data_to_investigation'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Observational/pds:Reference_List/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_resource', 'data_to_calibration_document', 'data_to_calibration_product', 'data_to_raw_product', 'data_to_calibrated_product', 'data_to_geometry', 'data_to_spice_kernel', 'data_to_thumbnail', 'data_to_document', 'data_curated_by_node', 'data_to_browse', 'data_to_ancillary_data')">
        The attribute reference_type must be set to one of the following values 'data_to_resource', 'data_to_calibration_document', 'data_to_calibration_product', 'data_to_raw_product', 'data_to_calibrated_product', 'data_to_geometry', 'data_to_spice_kernel', 'data_to_thumbnail', 'data_to_document', 'data_curated_by_node', 'data_to_browse', 'data_to_ancillary_data'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_SIP/pds:Information_Package_Component/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('package_has_collection', 'package_has_bundle', 'package_has_product')">
        The attribute reference_type must be set to one of the following values 'package_has_collection', 'package_has_bundle', 'package_has_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_SPICE_Kernel/pds:Context_Area">
      <sch:assert test="(pds:Time_Coordinates and pds:Investigation_Area and pds:Target_Identification and pds:Observing_System)">
        In Product_SPICE_Kernel the Time_Coordinates, Investigation_Area, Target_Identification, and Observing_System classes must be present</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Product_Zipped/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('zip_to_package')">
        The attribute reference_type must be set to one of the following values 'zip_to_package'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Quaternion">
      <sch:assert test="if (pds:type) then pds:type = ('SPICE', 'Spacecraft Telemetry') else true()">
        The attribute pds:type must be equal to one of the following values 'SPICE', 'Spacecraft Telemetry'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Quaternion_Component">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('ASCII_Real') else true()">
        The attribute pds:data_type must be equal to the value 'ASCII_Real'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Resource">
      <sch:assert test="if (pds:type) then pds:type = ('Information.Agency', 'Information.Instrument', 'Information.Instrument_Host', 'Information.Investigation', 'Information.Node', 'Information.Person', 'Information.Resource', 'Information.Science_Portal', 'Information.Target', 'System.Browse', 'System.Directory_Listing', 'System.Registry_Query', 'System.Search', 'System.Transform', 'System.Transport') else true()">
        The attribute pds:type must be equal to one of the following values 'Information.Agency', 'Information.Instrument', 'Information.Instrument_Host', 'Information.Investigation', 'Information.Node', 'Information.Person', 'Information.Resource', 'Information.Science_Portal', 'Information.Target', 'System.Browse', 'System.Directory_Listing', 'System.Registry_Query', 'System.Search', 'System.Transform', 'System.Transport'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:SPICE_Kernel">
      <sch:assert test="if (pds:encoding_type) then pds:encoding_type = ('Binary', 'Character') else true()">
        The attribute pds:encoding_type must be equal to one of the following values 'Binary', 'Character'.</sch:assert>
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('SPICE') else true()">
        The attribute pds:parsing_standard_id must be equal to the value 'SPICE'.</sch:assert>
      <sch:assert test="if (pds:kernel_type) then pds:kernel_type = ('CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK') else true()">
        The attribute pds:kernel_type must be equal to one of the following values 'CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Science_Facets">
      <sch:assert test="if (pds:domain) then pds:domain = ('Atmosphere', 'Heliosphere', 'Interior', 'Interstellar', 'Ionosphere', 'Magnetosphere', 'Surface') else true()">
        The attribute pds:domain must be equal to one of the following values 'Atmosphere', 'Heliosphere', 'Interior', 'Interstellar', 'Ionosphere', 'Magnetosphere', 'Surface'.</sch:assert>
      <sch:assert test="if (pds:wavelength_range) then pds:wavelength_range = ('Far Infrared', 'Gamma Ray', 'Infrared', 'Microwave', 'Millimeter', 'Near Infrared', 'Radio', 'Submillimeter', 'Ultraviolet', 'Visible', 'X-ray') else true()">
        The attribute pds:wavelength_range must be equal to one of the following values 'Far Infrared', 'Gamma Ray', 'Infrared', 'Microwave', 'Millimeter', 'Near Infrared', 'Radio', 'Submillimeter', 'Ultraviolet', 'Visible', 'X-ray'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Service_Description">
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('WADL', 'WSDL 2.n') else true()">
        The attribute pds:parsing_standard_id must be equal to one of the following values 'WADL', 'WSDL 2.n'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Special_Constants">
      <sch:assert test="if (pds:valid_maximum) then pds:valid_maximum = ('254', '32767', '65522') else true()">
        The attribute pds:valid_maximum must be equal to one of the following values '254', '32767', '65522'.</sch:assert>
      <sch:assert test="if (pds:valid_minimum) then pds:valid_minimum = ('-32752', '1', '3', '5', 'FF7FFFFA', 'FFEFFFFF') else true()">
        The attribute pds:valid_minimum must be equal to one of the following values '-32752', '1', '3', '5', 'FF7FFFFA', 'FFEFFFFF'.</sch:assert>
      <sch:assert test="if (pds:low_representation_saturation) then pds:low_representation_saturation = ('-32767', '1', '16#FF7FFFFC#', '16#FFFEFFFF#') else true()">
        The attribute pds:low_representation_saturation must be equal to one of the following values '-32767', '1', '16#FF7FFFFC#', '16#FFFEFFFF#'.</sch:assert>
      <sch:assert test="if (pds:high_representation_saturation) then pds:high_representation_saturation = ('-32764', '255', '4', '65535', 'FF7FFFFF', 'FFFBFFFF') else true()">
        The attribute pds:high_representation_saturation must be equal to one of the following values '-32764', '255', '4', '65535', 'FF7FFFFF', 'FFFBFFFF'.</sch:assert>
      <sch:assert test="if (pds:high_instrument_saturation) then pds:high_instrument_saturation = ('-32765', '255', '3', '65534', 'FF7FFFFE', 'FFFCFFFF') else true()">
        The attribute pds:high_instrument_saturation must be equal to one of the following values '-32765', '255', '3', '65534', 'FF7FFFFE', 'FFFCFFFF'.</sch:assert>
      <sch:assert test="if (pds:low_instrument_saturation) then pds:low_instrument_saturation = ('-32766', '0', '2', 'FF7FFFFD', 'FFFDFFFF') else true()">
        The attribute pds:low_instrument_saturation must be equal to one of the following values '-32766', '0', '2', 'FF7FFFFD', 'FFFDFFFF'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Stream_Text">
      <sch:assert test="if (pds:record_delimiter) then pds:record_delimiter = ('carriage-return line-feed') else true()">
        The attribute pds:record_delimiter must be equal to the value 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Character">
      <sch:assert test="if (pds:record_delimiter) then pds:record_delimiter = ('carriage-return line-feed') else true()">
        The attribute pds:record_delimiter must be equal to the value 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Table_Delimited">
      <sch:assert test="if (pds:record_delimiter) then pds:record_delimiter = ('carriage-return line-feed') else true()">
        The attribute pds:record_delimiter must be equal to the value 'carriage-return line-feed'.</sch:assert>
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('PDS DSV 1') else true()">
        The attribute pds:parsing_standard_id must be equal to the value 'PDS DSV 1'.</sch:assert>
      <sch:assert test="if (pds:field_delimiter) then pds:field_delimiter = ('comma', 'horizontal tab', 'semicolon', 'vertical bar') else true()">
        The attribute pds:field_delimiter must be equal to one of the following values 'comma', 'horizontal tab', 'semicolon', 'vertical bar'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target">
      <sch:assert test="if (pds:type) then pds:type = ('Asteroid', 'Comet', 'Dust', 'Dwarf Planet', 'Galaxy', 'Globular Cluster', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Ring', 'Satellite', 'Star', 'Star Cluster', 'Sun', 'Terrestrial Sample', 'Trans-Neptunian Object') else true()">
        The attribute pds:type must be equal to one of the following values 'Asteroid', 'Comet', 'Dust', 'Dwarf Planet', 'Galaxy', 'Globular Cluster', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Ring', 'Satellite', 'Star', 'Star Cluster', 'Sun', 'Terrestrial Sample', 'Trans-Neptunian Object'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target_Identification">
      <sch:assert test="if (pds:type) then pds:type = ('Asteroid', 'Comet', 'Dust', 'Dwarf Planet', 'Galaxy', 'Globular Cluster', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Ring', 'Satellite', 'Star', 'Star Cluster', 'Sun', 'Terrestrial Sample', 'Trans-Neptunian Object') else true()">
        The attribute pds:type must be equal to one of the following values 'Asteroid', 'Comet', 'Dust', 'Dwarf Planet', 'Galaxy', 'Globular Cluster', 'Meteorite', 'Meteoroid', 'Meteoroid Stream', 'Nebula', 'Open Cluster', 'Planet', 'Planetary Nebula', 'Planetary System', 'Plasma Cloud', 'Ring', 'Satellite', 'Star', 'Star Cluster', 'Sun', 'Terrestrial Sample', 'Trans-Neptunian Object'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Target_Identification/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_target', 'collection_to_target', 'bundle_to_target')">
        The attribute reference_type must be set to one of the following values 'data_to_target', 'collection_to_target', 'bundle_to_target'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope">
      <sch:assert test="if (pds:coordinate_source) then pds:coordinate_source = ('Aerial survey - North American (1983) datum', 'Astronomical', 'Doppler determined - WGS 72 datum', 'Geodetic - Adindan datum', 'Geodetic - Australian datum', 'Geodetic - Campo Inchauspe (Argentina) datum', 'Geodetic - Cape (South Africa) datum', 'Geodetic - Corregio Alegre (Brazil) datum', 'Geodetic - European 1979 datum', 'Geodetic - European datum', 'Geodetic - GRS 80 datum', 'Geodetic - Hermannskogel datum', 'Geodetic - Indian datum', 'Geodetic - La Canoa (Venezuela) datum', 'Geodetic - New Zealand datum', 'Geodetic - North American (1927) datum', 'Geodetic - Old Hawaiian datum', 'Geodetic - Ordnance Survey of Great Britain (1936) datum', 'Geodetic - Ordnance Survey of Great Britain (SN) 1980 datum', 'Geodetic - Potsdam datum', 'Geodetic - Puerto Rican (1940) datum', 'Geodetic - South American datum', 'Geodetic - Tokyo datum', 'Geodetic - WGS 84 datum', 'Geodetic - datum unknown', 'Satellite determined - datum unknown', 'Unknown') else true()">
        The attribute pds:coordinate_source must be equal to one of the following values 'Aerial survey - North American (1983) datum', 'Astronomical', 'Doppler determined - WGS 72 datum', 'Geodetic - Adindan datum', 'Geodetic - Australian datum', 'Geodetic - Campo Inchauspe (Argentina) datum', 'Geodetic - Cape (South Africa) datum', 'Geodetic - Corregio Alegre (Brazil) datum', 'Geodetic - European 1979 datum', 'Geodetic - European datum', 'Geodetic - GRS 80 datum', 'Geodetic - Hermannskogel datum', 'Geodetic - Indian datum', 'Geodetic - La Canoa (Venezuela) datum', 'Geodetic - New Zealand datum', 'Geodetic - North American (1927) datum', 'Geodetic - Old Hawaiian datum', 'Geodetic - Ordnance Survey of Great Britain (1936) datum', 'Geodetic - Ordnance Survey of Great Britain (SN) 1980 datum', 'Geodetic - Potsdam datum', 'Geodetic - Puerto Rican (1940) datum', 'Geodetic - South American datum', 'Geodetic - Tokyo datum', 'Geodetic - WGS 84 datum', 'Geodetic - datum unknown', 'Satellite determined - datum unknown', 'Unknown'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:altitude">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:aperture">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:telescope_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Telescope/pds:telescope_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Terminological_Entry">
      <sch:assert test="if (pds:language) then pds:language = ('English', 'Russian') else true()">
        The attribute pds:language must be equal to one of the following values 'English', 'Russian'.</sch:assert>
      <sch:assert test="if (pds:preferred_flag) then pds:preferred_flag = ('true', 'false') else true()">
        The attribute pds:preferred_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Transfer_Manifest">
      <sch:assert test="if (pds:record_delimiter) then pds:record_delimiter = ('carriage-return line-feed') else true()">
        The attribute pds:record_delimiter must be equal to the value 'carriage-return line-feed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Uniformly_Sampled">
      <sch:assert test="if (pds:sampling_parameter_scale) then pds:sampling_parameter_scale = ('Exponential', 'Linear', 'Logarithmic') else true()">
        The attribute pds:sampling_parameter_scale must be equal to one of the following values 'Exponential', 'Linear', 'Logarithmic'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Update_Entry/pds:Internal_Reference">
      <sch:assert test="every $ref in (pds:reference_type) satisfies $ref = ('data_to_update', 'collection_to_update', 'bundle_to_update')">
        The attribute reference_type must be set to one of the following values 'data_to_update', 'collection_to_update', 'bundle_to_update'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector">
      <sch:assert test="if (pds:data_type) then pds:data_type = ('ASCII_Real') else true()">
        The attribute pds:data_type must be equal to the value 'ASCII_Real'.</sch:assert>
      <sch:assert test="if (pds:type) then pds:type = ('Acceleration', 'Pointing', 'Position', 'Velocity') else true()">
        The attribute pds:type must be equal to one of the following values 'Acceleration', 'Pointing', 'Position', 'Velocity'.</sch:assert>
      <sch:assert test="if (pds:reference_frame_id) then pds:reference_frame_id = ('ICRF', 'MOON_ME_DE421') else true()">
        The attribute pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3">
      <sch:assert test="if (pds:reference_frame_id) then pds:reference_frame_id = ('ICRF', 'MOON_ME_DE421') else true()">
        The attribute pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Acceleration">
      <sch:assert test="if (pds:reference_frame_id) then pds:reference_frame_id = ('ICRF', 'MOON_ME_DE421') else true()">
        The attribute pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Pointing">
      <sch:assert test="if (pds:reference_frame_id) then pds:reference_frame_id = ('ICRF', 'MOON_ME_DE421') else true()">
        The attribute pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Position">
      <sch:assert test="if (pds:reference_frame_id) then pds:reference_frame_id = ('ICRF', 'MOON_ME_DE421') else true()">
        The attribute pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Vector_Cartesian_3_Velocity">
      <sch:assert test="if (pds:reference_frame_id) then pds:reference_frame_id = ('ICRF', 'MOON_ME_DE421') else true()">
        The attribute pds:reference_frame_id must be equal to one of the following values 'ICRF', 'MOON_ME_DE421'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Volume_PDS3">
      <sch:assert test="if (pds:archive_status) then pds:archive_status = ('ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED') else true()">
        The attribute pds:archive_status must be equal to one of the following values 'ARCHIVED', 'ARCHIVED_ACCUMULATING', 'IN_LIEN_RESOLUTION', 'IN_LIEN_RESOLUTION_ACCUMULATING', 'IN_PEER_REVIEW', 'IN_PEER_REVIEW_ACCUMULATING', 'IN_QUEUE', 'IN_QUEUE_ACCUMULATING', 'LOCALLY_ARCHIVED', 'LOCALLY_ARCHIVED_ACCUMULATING', 'PRE_PEER_REVIEW', 'PRE_PEER_REVIEW_ACCUMULATING', 'SAFED', 'SUPERSEDED'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:XML_Schema">
      <sch:assert test="pds:offset eq '0'">
        XML_Schema.offset must have a value of '0'</sch:assert>
      <sch:assert test="if (pds:parsing_standard_id) then pds:parsing_standard_id = ('Schematron ISO/IEC 19757-3:2006', 'XML Schema Version 1.1') else true()">
        The attribute pds:parsing_standard_id must be equal to one of the following values 'Schematron ISO/IEC 19757-3:2006', 'XML Schema Version 1.1'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="pds:Zip">
      <sch:assert test="if (pds:container_type) then pds:container_type = ('GZIP', 'LZIP', 'TAR', 'ZIP') else true()">
        The attribute pds:container_type must be equal to one of the following values 'GZIP', 'LZIP', 'TAR', 'ZIP'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
