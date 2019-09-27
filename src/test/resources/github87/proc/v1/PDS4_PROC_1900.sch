<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:proc  Version:1.9.0.0 - Fri Dec 29 20:43:48 PST 2017 -->
  <!-- Generated from the PDS4 Information Model Version 1.9.0.0 - System Build 8a -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/proc/v1" prefix="proc"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="//proc:Input_Product">
      <sch:assert test="count(child::*) > 0">
        PROC:error:Input_Product.child_check: proc:Input_Product class must contain at least 1 attribute or class.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//proc:Process">
      <sch:assert test="count(child::*) > 0">
        PROC:error:Process.child_check: proc:Process class must contain at least 1 attribute or class.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//proc:Software">
      <sch:assert test="count(child::*) > 0">
        PROC:error:Software.child_check: proc:Software class must contain at least 1 attribute or class.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//proc:Software_Program">
      <sch:assert test="count(child::*) > 0">
        PROC:error:Software_Program.child_check: proc:Software_Program class must contain at least 1 attribute or class.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//proc:Input_Product_List/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'data_to_input_product'">
        PROC:error:input_product_reference_type_check: pds:reference_type must have value 'data_to_input_product'</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//proc:Processing_Information/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'processing_information_to_data_object'">
        PROC:error:local_reference_type_check: pds:local_reference_type must have value 'processing_information_to_data_object'</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
