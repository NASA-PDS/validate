<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:vco  Version:1.0.0.0 - Mon Mar 04 15:40:19 JST 2024 -->
  <!-- Generated from the PDS4 Information Model Version 1.19.0.0 - System Build 13.0 -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://darts.isas.jaxa.jp/pds4/mission/vco/v1" prefix="vco"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="//vco:IR1_Instrument_Attributes">
      <sch:assert test="(count(vco:IR1_Interquadrant_Sensitivity_Correction_Status) = 4) and (count(vco:IR1_Interquadrant_Sensitivity_Correction) = 4)">
        <title>//vco:IR1_Instrument_Attributes/Rule</title>
        VCO:error:iq_sens_corr_corr_factor: number of vco:IR1_Interquadrant_Sensitivity_Correction_Status classes must be equal to number of vco:IR1_Interquadrant_Sensitivity_Correction classes.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//vco:IR2_Instrument_Attributes">
      <sch:assert test="count(child::*) > 0">
        <title>//vco:IR2_Instrument_Attributes/Rule</title>
        ir2_child_check: vco:IR2_Instrument_Attributes must have at least 1 attribute specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//vco:LIR_Instrument_Attributes">
      <sch:assert test="count(child::*) > 0">
        <title>//vco:LIR_Instrument_Attributes/Rule</title>
        lir_child_check: vco:LIR_Instrument_Attributes must have at least 1 attribute specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//vco:Observation_Information">
      <sch:assert test="count(child::*) > 0">
        <title>//vco:Observation_Information/Rule</title>
        obs_child_check: vco:Observation_Information must have at least 1 attribute specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//vco:Orbit_Information">
      <sch:assert test="count(child::*) > 0">
        <title>//vco:Orbit_Information/Rule</title>
        orb_child_check: vco:Orbit_Information must have at least 1 attribute specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//vco:UVI_Instrument_Attributes">
      <sch:assert test="count(child::*) > 0">
        <title>//vco:UVI_Instrument_Attributes/Rule</title>
        uvi_child_check: vco:UVI_Instrument_Attributes must have at least 1 attribute specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_brightness">
      <sch:assert test=". = ('Bright', 'Dark')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_brightness/vco:ir1_calibration_lamp_brightness</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_brightness must be equal to one of the following values 'Bright', 'Dark'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_permission">
      <sch:assert test=". = ('Disabled', 'Enabled')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_permission/vco:ir1_calibration_lamp_permission</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_permission must be equal to one of the following values 'Disabled', 'Enabled'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_status">
      <sch:assert test=". = ('Off', 'On')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_status/vco:ir1_calibration_lamp_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_calibration_lamp_status must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_filter_wheel_rotation_status">
      <sch:assert test=". = ('Not Rotating', 'Rotating')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_filter_wheel_rotation_status/vco:ir1_filter_wheel_rotation_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_filter_wheel_rotation_status must be equal to one of the following values 'Not Rotating', 'Rotating'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_led_power_status">
      <sch:assert test=". = ('Off', 'On')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_led_power_status/vco:ir1_led_power_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_led_power_status must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_photodiode1_status">
      <sch:assert test=". = ('Ack', 'No Ack')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_photodiode1_status/vco:ir1_photodiode1_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_photodiode1_status must be equal to one of the following values 'Ack', 'No Ack'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_photodiode2_status">
      <sch:assert test=". = ('Ack', 'No Ack')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_photodiode2_status/vco:ir1_photodiode2_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_photodiode2_status must be equal to one of the following values 'Ack', 'No Ack'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_heater_status">
      <sch:assert test=". = ('Cooling', 'Heating')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_heater_status/vco:ir1_thermoelectric_cooler_heater_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_heater_status must be equal to one of the following values 'Cooling', 'Heating'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_power_status">
      <sch:assert test=". = ('Off', 'On')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_power_status/vco:ir1_thermoelectric_cooler_power_status</title>
        The attribute vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_power_status must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_set_value">
      <sch:assert test="@unit = ('A', 'mA')">
        <title>vco:IR1_Instrument_Attributes/vco:ir1_thermoelectric_cooler_set_value/vco:ir1_thermoelectric_cooler_set_value</title>
        The attribute @unit must be equal to one of the following values 'A', 'mA'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Interquadrant_Sensitivity_Correction/vco:quadrant">
      <sch:assert test=". = ('Bottom Left', 'Bottom Right', 'Top Left', 'Top Right')">
        <title>vco:IR1_Interquadrant_Sensitivity_Correction/vco:quadrant/vco:quadrant</title>
        The attribute vco:IR1_Interquadrant_Sensitivity_Correction/vco:quadrant must be equal to one of the following values 'Bottom Left', 'Bottom Right', 'Top Left', 'Top Right'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Interquadrant_Sensitivity_Correction_Status/vco:interquadrant_sensitivity_correction_status">
      <sch:assert test=". = ('Applied', 'Not Applied')">
        <title>vco:IR1_Interquadrant_Sensitivity_Correction_Status/vco:interquadrant_sensitivity_correction_status/vco:interquadrant_sensitivity_correction_status</title>
        The attribute vco:IR1_Interquadrant_Sensitivity_Correction_Status/vco:interquadrant_sensitivity_correction_status must be equal to one of the following values 'Applied', 'Not Applied'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Interquadrant_Sensitivity_Correction_Status/vco:quadrant">
      <sch:assert test=". = ('Bottom Left', 'Bottom Right', 'Top Left', 'Top Right')">
        <title>vco:IR1_Interquadrant_Sensitivity_Correction_Status/vco:quadrant/vco:quadrant</title>
        The attribute vco:IR1_Interquadrant_Sensitivity_Correction_Status/vco:quadrant must be equal to one of the following values 'Bottom Left', 'Bottom Right', 'Top Left', 'Top Right'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR1_Smear_Correction_Quadrant/vco:quadrant">
      <sch:assert test=". = ('Bottom Left', 'Bottom Right', 'Top Left', 'Top Right')">
        <title>vco:IR1_Smear_Correction_Quadrant/vco:quadrant/vco:quadrant</title>
        The attribute vco:IR1_Smear_Correction_Quadrant/vco:quadrant must be equal to one of the following values 'Bottom Left', 'Bottom Right', 'Top Left', 'Top Right'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_brightness">
      <sch:assert test=". = ('Bright', 'Dark')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_brightness/vco:ir2_calibration_lamp_brightness</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_brightness must be equal to one of the following values 'Bright', 'Dark'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_permission">
      <sch:assert test=". = ('Disabled', 'Enabled')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_permission/vco:ir2_calibration_lamp_permission</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_permission must be equal to one of the following values 'Disabled', 'Enabled'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_power_status">
      <sch:assert test=". = ('Off', 'On')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_power_status/vco:ir2_calibration_lamp_power_status</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_calibration_lamp_power_status must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_filter_wheel_rotation_status">
      <sch:assert test=". = ('Not Rotating', 'Rotating')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_filter_wheel_rotation_status/vco:ir2_filter_wheel_rotation_status</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_filter_wheel_rotation_status must be equal to one of the following values 'Not Rotating', 'Rotating'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_gain">
      <sch:assert test=". = ('High', 'Low')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_gain/vco:ir2_gain</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_gain must be equal to one of the following values 'High', 'Low'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_heater_permission">
      <sch:assert test=". = ('Disabled', 'Enabled')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_heater_permission/vco:ir2_heater_permission</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_heater_permission must be equal to one of the following values 'Disabled', 'Enabled'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_heater_power_status">
      <sch:assert test=". = ('Off', 'On')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_heater_power_status/vco:ir2_heater_power_status</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_heater_power_status must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_led_power_status">
      <sch:assert test=". = ('Off', 'On')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_led_power_status/vco:ir2_led_power_status</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_led_power_status must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_photodiode1_status">
      <sch:assert test=". = ('Ack', 'No Ack')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_photodiode1_status/vco:ir2_photodiode1_status</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_photodiode1_status must be equal to one of the following values 'Ack', 'No Ack'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Instrument_Attributes/vco:ir2_photodiode2_status">
      <sch:assert test=". = ('Ack', 'No Ack')">
        <title>vco:IR2_Instrument_Attributes/vco:ir2_photodiode2_status/vco:ir2_photodiode2_status</title>
        The attribute vco:IR2_Instrument_Attributes/vco:ir2_photodiode2_status must be equal to one of the following values 'Ack', 'No Ack'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:IR2_Temperature_Correction/vco:quadrant">
      <sch:assert test=". = ('Bottom Left', 'Bottom Right', 'Top Left', 'Top Right')">
        <title>vco:IR2_Temperature_Correction/vco:quadrant/vco:quadrant</title>
        The attribute vco:IR2_Temperature_Correction/vco:quadrant must be equal to one of the following values 'Bottom Left', 'Bottom Right', 'Top Left', 'Top Right'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes">
      <sch:assert test="if (vco:lir_wrong_bit_operation_flag) then vco:lir_wrong_bit_operation_flag = ('true', 'false') else true()">
        <title>vco:LIR_Instrument_Attributes/vco:lir_wrong_bit_operation_flag</title>
        The attribute vco:lir_wrong_bit_operation_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_calibration_reference_baffle_temperature">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_calibration_reference_baffle_temperature/vco:lir_calibration_reference_baffle_temperature</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_calibration_reference_shutter_temperature">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_calibration_reference_shutter_temperature/vco:lir_calibration_reference_shutter_temperature</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_after_observation_time_delta">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_after_observation_time_delta/vco:lir_housekeeping_packet_after_observation_time_delta</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_before_observation_time_delta">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_before_observation_time_delta/vco:lir_housekeeping_packet_before_observation_time_delta</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_interpolation_flag">
      <sch:assert test=". = ('No data', 'Within 120 seconds', 'Within 2048 seconds')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_interpolation_flag/vco:lir_housekeeping_packet_interpolation_flag</title>
        The attribute vco:LIR_Instrument_Attributes/vco:lir_housekeeping_packet_interpolation_flag must be equal to one of the following values 'No data', 'Within 120 seconds', 'Within 2048 seconds'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_image_type">
      <sch:assert test=". = ('OPN', 'PIC', 'SHT')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_image_type/vco:lir_image_type</title>
        The attribute vco:LIR_Instrument_Attributes/vco:lir_image_type must be equal to one of the following values 'OPN', 'PIC', 'SHT'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_l2d_reference_shutter_temperature">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_l2d_reference_shutter_temperature/vco:lir_l2d_reference_shutter_temperature</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:lir_operating_time">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>vco:LIR_Instrument_Attributes/vco:lir_operating_time/vco:lir_operating_time</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:number_of_first_accumulation">
      <sch:assert test=". = ('1', '128', '16', '2', '32', '4', '64', '8')">
        <title>vco:LIR_Instrument_Attributes/vco:number_of_first_accumulation/vco:number_of_first_accumulation</title>
        The attribute vco:LIR_Instrument_Attributes/vco:number_of_first_accumulation must be equal to one of the following values '1', '128', '16', '2', '32', '4', '64', '8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:number_of_second_accumulation">
      <sch:assert test=". = ('1', '128', '16', '2', '32', '4', '64', '8')">
        <title>vco:LIR_Instrument_Attributes/vco:number_of_second_accumulation/vco:number_of_second_accumulation</title>
        The attribute vco:LIR_Instrument_Attributes/vco:number_of_second_accumulation must be equal to one of the following values '1', '128', '16', '2', '32', '4', '64', '8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:LIR_Instrument_Attributes/vco:peltier_target_temperature">
      <sch:assert test=". = ('10', '40')">
        <title>vco:LIR_Instrument_Attributes/vco:peltier_target_temperature/vco:peltier_target_temperature</title>
        The attribute vco:LIR_Instrument_Attributes/vco:peltier_target_temperature must be equal to one of the following values '10', '40'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Observation_Information">
      <sch:assert test="if (vco:instrument_status_guess_flag) then vco:instrument_status_guess_flag = ('true', 'false') else true()">
        <title>vco:Observation_Information/vco:instrument_status_guess_flag</title>
        The attribute vco:instrument_status_guess_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Observation_Information/vco:image_processing_return_status">
      <sch:assert test=". = ('Buffer Size Error', 'Data Recorder Write Fail', 'Data Recorder Write Fail/Image Size Error', 'Normal End', 'Operation Interruption', 'Overflow', 'Overflow and Underflow', 'Parameter Error', 'Underflow', 'Zero Division')">
        <title>vco:Observation_Information/vco:image_processing_return_status/vco:image_processing_return_status</title>
        The attribute vco:Observation_Information/vco:image_processing_return_status must be equal to one of the following values 'Buffer Size Error', 'Data Recorder Write Fail', 'Data Recorder Write Fail/Image Size Error', 'Normal End', 'Operation Interruption', 'Overflow', 'Overflow and Underflow', 'Parameter Error', 'Underflow', 'Zero Division'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Observation_Information/vco:target_apparent_diameter">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>vco:Observation_Information/vco:target_apparent_diameter/vco:target_apparent_diameter</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information">
      <sch:assert test="if (vco:orbit_number > 0) then
                       exists(vco:periapsis_passage_date_time) and
                       exists(vco:subspacecraft_latitude_at_periapsis_passage) and
                       exists(vco:subspacecraft_longitude_at_periapsis_passage) and
                       exists(vco:subspacecraft_altitude_at_periapsis_passage) and
                       exists(vco:orbit_inclination_angle) and
                       exists(vco:orbit_eccentricity) and
                       exists(vco:orbit_longitude) and
                       exists(vco:orbit_argument) and
                       exists(vco:orbit_semimajor_axis_length)
                       else true()
            ">
        <title>vco:Orbit_Information/Rule</title>
        orb_child_existance_check: vco:Orbit_Information must have vco:periapsis_passage_date_time, vco:subspacecraft_latitude_at_periapsis_passage, vco:subspacecraft_longitude_at_periapsis_passage, vco:subspacecraft_altitude_at_periapsis_passage, vco:orbit_inclination_angle, vco:orbit_eccentricity, vco:orbit_longitude, vco:orbit_argument, and vco:orbit_semimajor_axis_length if vco:orbit_number > 0.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:orbit_argument">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>vco:Orbit_Information/vco:orbit_argument/vco:orbit_argument</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:orbit_inclination_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>vco:Orbit_Information/vco:orbit_inclination_angle/vco:orbit_inclination_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:orbit_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>vco:Orbit_Information/vco:orbit_longitude/vco:orbit_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:orbit_semimajor_axis_length">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>vco:Orbit_Information/vco:orbit_semimajor_axis_length/vco:orbit_semimajor_axis_length</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:subspacecraft_altitude_at_periapsis_passage">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>vco:Orbit_Information/vco:subspacecraft_altitude_at_periapsis_passage/vco:subspacecraft_altitude_at_periapsis_passage</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:subspacecraft_latitude_at_periapsis_passage">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>vco:Orbit_Information/vco:subspacecraft_latitude_at_periapsis_passage/vco:subspacecraft_latitude_at_periapsis_passage</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:Orbit_Information/vco:subspacecraft_longitude_at_periapsis_passage">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>vco:Orbit_Information/vco:subspacecraft_longitude_at_periapsis_passage/vco:subspacecraft_longitude_at_periapsis_passage</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:RS_Experiment_Attributes/vco:circular_polarization">
      <sch:assert test=". = ('Left-handed_Circulary_Polarized', 'Right-handed_Circulary_Polarized')">
        <title>vco:RS_Experiment_Attributes/vco:circular_polarization/vco:circular_polarization</title>
        The attribute vco:RS_Experiment_Attributes/vco:circular_polarization must be equal to one of the following values 'Left-handed_Circulary_Polarized', 'Right-handed_Circulary_Polarized'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:RS_Experiment_Attributes/vco:downconversion_frequency">
      <sch:assert test="@unit = ('GHz', 'Hz', 'MHz', 'THz', 'kHz', 'mHz')">
        <title>vco:RS_Experiment_Attributes/vco:downconversion_frequency/vco:downconversion_frequency</title>
        The attribute @unit must be equal to one of the following values 'GHz', 'Hz', 'MHz', 'THz', 'kHz', 'mHz'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:RS_Experiment_Attributes/vco:last_zero_padding_size">
      <sch:assert test="@unit = ('byte')">
        <title>vco:RS_Experiment_Attributes/vco:last_zero_padding_size/vco:last_zero_padding_size</title>
        The attribute @unit must be equal to one of the following values 'byte'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:UVI_Instrument_Attributes/vco:uvi_filter_wheel_position_flag">
      <sch:assert test=". = ('Bad', 'Good')">
        <title>vco:UVI_Instrument_Attributes/vco:uvi_filter_wheel_position_flag/vco:uvi_filter_wheel_position_flag</title>
        The attribute vco:UVI_Instrument_Attributes/vco:uvi_filter_wheel_position_flag must be equal to one of the following values 'Bad', 'Good'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="vco:UVI_Instrument_Attributes/vco:uvi_optical_black_area_mode">
      <sch:assert test=". = ('Left OB', 'Normal', 'Right OB', 'Top OB')">
        <title>vco:UVI_Instrument_Attributes/vco:uvi_optical_black_area_mode/vco:uvi_optical_black_area_mode</title>
        The attribute vco:UVI_Instrument_Attributes/vco:uvi_optical_black_area_mode must be equal to one of the following values 'Left OB', 'Normal', 'Right OB', 'Top OB'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
