<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:mer  Version:1.0.0.0 - Tue Aug 27 10:43:56 CDT 2019 -->
  <!-- Generated from the PDS4 Information Model Version 1.11.0.0 - System Build 9a -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/mission/mer/v1" prefix="mer"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="mer:Angular_Distance_Index/mer:angular_distance">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:Angular_Distance_Index/mer:angular_distance_name">
      <sch:assert test=". = ('Dwell Operation', 'Grind Completion')">
        The attribute mer:angular_distance_name must be equal to one of the following values 'Dwell Operation', 'Grind Completion'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MER_Parameters/mer:magnet_id">
      <sch:assert test=". = ('Capture', 'Filter', 'Null', 'RAT', 'Sweep')">
        The attribute mer:magnet_id must be equal to one of the following values 'Capture', 'Filter', 'Null', 'RAT', 'Sweep'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MER_Parameters/mer:mission_phase_name">
      <sch:assert test=". = ('Extended Mission', 'Primary Mission')">
        The attribute mer:mission_phase_name must be equal to one of the following values 'Extended Mission', 'Primary Mission'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Command_Parameters/mer:center_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Command_Parameters/mer:center_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Command_Parameters/mer:horizontal_space">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Command_Parameters/mer:phase_algorithm_name">
      <sch:assert test=". = ('Mertz', 'None', 'RSS')">
        The attribute mer:phase_algorithm_name must be equal to one of the following values 'Mertz', 'None', 'RSS'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Command_Parameters/mer:vertical_space">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:field_of_view">
      <sch:assert test=". = ('20', '8')">
        The attribute mer:field_of_view must be equal to one of the following values '20', '8'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:gain_state">
      <sch:assert test=". = ('High', 'Low')">
        The attribute mer:gain_state must be equal to one of the following values 'High', 'Low'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:laser_1_status_flag">
      <sch:assert test=". = ('Off', 'On')">
        The attribute mer:laser_1_status_flag must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:laser_2_status_flag">
      <sch:assert test=". = ('Off', 'On')">
        The attribute mer:laser_2_status_flag must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:laser_heater_status_flag">
      <sch:assert test=". = ('Off', 'On')">
        The attribute mer:laser_heater_status_flag must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:linear_motor_status_flag">
      <sch:assert test=". = ('Off', 'On')">
        The attribute mer:linear_motor_status_flag must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:optical_switch_state">
      <sch:assert test=". = ('Primary', 'Redundant')">
        The attribute mer:optical_switch_state must be equal to one of the following values 'Primary', 'Redundant'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:MiniTES_Parameters/mer:spare_bit_flag">
      <sch:assert test=". = ('Off', 'On')">
        The attribute mer:spare_bit_flag must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Brush_Request_Parameters/mer:angular_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Brush_Request_Parameters/mer:rotation_voltage">
      <sch:assert test="@unit = ('V', 'mV')">
        The attribute @unit must be equal to one of the following values 'V', 'mV'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Brush_Request_Parameters/mer:z_axis_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Diagnostics_Cal_Request_Parameters/mer:angular_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Diagnostics_Cal_Request_Parameters/mer:rotation_voltage">
      <sch:assert test="@unit = ('V', 'mV')">
        The attribute @unit must be equal to one of the following values 'V', 'mV'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Grind_Request_Parameters/mer:angular_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Grind_Request_Parameters/mer:clearance_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Grind_Request_Parameters/mer:maximum_angular_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Grind_Request_Parameters/mer:rotation_voltage">
      <sch:assert test="@unit = ('V', 'mV')">
        The attribute @unit must be equal to one of the following values 'V', 'mV'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Grind_Request_Parameters/mer:timeout_parameter">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr')">
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Grind_Request_Parameters/mer:z_axis_step_size">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Request_Parameters/mer:error_condition">
      <sch:assert test=". = ('Both', 'Contact1', 'Contact2', 'None')">
        The attribute mer:error_condition must be equal to one of the following values 'Both', 'Contact1', 'Contact2', 'None'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Request_Parameters/mer:maximum_travel_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Request_Parameters/mer:z_axis_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Seek_Scan_Request_Parameters/mer:angular_distance">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Seek_Scan_Request_Parameters/mer:maximum_angular_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:RAT_Seek_Scan_Request_Parameters/mer:z_axis_step_size">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:Rotation_Voltage_Index/mer:rotation_voltage">
      <sch:assert test="@unit = ('V', 'mV')">
        The attribute @unit must be equal to one of the following values 'V', 'mV'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:Rotation_Voltage_Index/mer:rotation_voltage_name">
      <sch:assert test=". = ('Scan', 'Seek')">
        The attribute mer:rotation_voltage_name must be equal to one of the following values 'Scan', 'Seek'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:Torque_Gain_Index/mer:torque_gain_name">
      <sch:assert test=". = ('Derivative', 'Integral', 'Proportional')">
        The attribute mer:torque_gain_name must be equal to one of the following values 'Derivative', 'Integral', 'Proportional'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="mer:Z_Axis_Velocity_Index/mer:z_axis_velocity_name">
      <sch:assert test=". = ('Scan', 'Seek')">
        The attribute mer:z_axis_velocity_name must be equal to one of the following values 'Scan', 'Seek'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="/pds:Product_Observational/pds:Observation_Area/pds:Mission_Area/mer:MER_Parameters">
      <sch:assert test="
            if ( 
               (mer:spacecraft_clock_count_partition) 
               or
               ( (contains(mer:spacecraft_clock_start_count, '/')) 
                  and
                 (contains(mer:spacecraft_clock_stop_count, '/'))
               )
               or
               ( (not (mer:spacecraft_clock_count_partition))
                  and
                 (not (mer:spacecraft_clock_start_count))
                  and
                 (not (mer:spacecraft_clock_stop_count))
                )
                ) then true() else false()
        ">
        
            mer:error:sclk_rule_0: If spacecraft_clock_start_count and spacecraft_clock_stop_count
            are present, they must begin with a partition number followed by a forward slash,
            OR the partition number must be given by spacecraft_clock_count_partition.
        </sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
