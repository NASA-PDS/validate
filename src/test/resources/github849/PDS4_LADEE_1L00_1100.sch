<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:ladee  Version:1.1.0.0 - Fri Feb 23 06:43:53 PST 2024 -->
  <!-- Generated from the PDS4 Information Model Version 1.21.0.0 - System Build 14.0 -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/mission/ladee/v1" prefix="ladee"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes">
      <sch:assert test="if (ladee:tec_enabled) then ladee:tec_enabled = ('true', 'false') else true()">
        <title>ladee:UVS_Specific_Attributes/ladee:tec_enabled</title>
        The attribute ladee:tec_enabled must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (ladee:valid_checksum) then ladee:valid_checksum = ('true', 'false') else true()">
        <title>ladee:UVS_Specific_Attributes/ladee:valid_checksum</title>
        The attribute ladee:valid_checksum must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:activity_type">
      <sch:assert test=". = ('AlmostLimb', 'BackwardLimbWithNod', 'BackwardLimbWithoutNod', 'DarkCal', 'ForwardLimbWithNod', 'ForwardLimbWithoutNod', 'NorthLimbWithNod', 'NorthLimbWithoutNod', 'Occultation', 'RamOnly', 'SodiumTail', 'SolarBoresightCal', 'SolarCal', 'SouthLimbWithNod', 'SouthLimbWithoutNod', 'StarCal', 'SurfaceCal', 'TelescopeBoresightCal', 'Unknown')">
        <title>ladee:UVS_Specific_Attributes/ladee:activity_type/ladee:activity_type</title>
        The attribute ladee:UVS_Specific_Attributes/ladee:activity_type must be equal to one of the following values 'AlmostLimb', 'BackwardLimbWithNod', 'BackwardLimbWithoutNod', 'DarkCal', 'ForwardLimbWithNod', 'ForwardLimbWithoutNod', 'NorthLimbWithNod', 'NorthLimbWithoutNod', 'Occultation', 'RamOnly', 'SodiumTail', 'SolarBoresightCal', 'SolarCal', 'SouthLimbWithNod', 'SouthLimbWithoutNod', 'StarCal', 'SurfaceCal', 'TelescopeBoresightCal', 'Unknown'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:altitude">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:altitude/ladee:altitude</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:altitude_above_terrain">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:altitude_above_terrain/ladee:altitude_above_terrain</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:drive_current">
      <sch:assert test="@unit = ('A', 'mA')">
        <title>ladee:UVS_Specific_Attributes/ladee:drive_current/ladee:drive_current</title>
        The attribute @unit must be equal to one of the following values 'A', 'mA'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:integration_time">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>ladee:UVS_Specific_Attributes/ladee:integration_time/ladee:integration_time</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:moon_fixed_vx">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>ladee:UVS_Specific_Attributes/ladee:moon_fixed_vx/ladee:moon_fixed_vx</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:moon_fixed_vy">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>ladee:UVS_Specific_Attributes/ladee:moon_fixed_vy/ladee:moon_fixed_vy</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:moon_fixed_vz">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>ladee:UVS_Specific_Attributes/ladee:moon_fixed_vz/ladee:moon_fixed_vz</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:moon_fixed_x">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:moon_fixed_x/ladee:moon_fixed_x</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:moon_fixed_y">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:moon_fixed_y/ladee:moon_fixed_y</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:moon_fixed_z">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:moon_fixed_z/ladee:moon_fixed_z</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:solar_viewer_target">
      <sch:assert test=". = ('DarkSky', 'Earth', 'Limb', 'LitMoon', 'Sun', 'Unknown', 'UnlitMoon')">
        <title>ladee:UVS_Specific_Attributes/ladee:solar_viewer_target/ladee:solar_viewer_target</title>
        The attribute ladee:UVS_Specific_Attributes/ladee:solar_viewer_target must be equal to one of the following values 'DarkSky', 'Earth', 'Limb', 'LitMoon', 'Sun', 'Unknown', 'UnlitMoon'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:tec_cold">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>ladee:UVS_Specific_Attributes/ladee:tec_cold/ladee:tec_cold</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:tec_hot">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>ladee:UVS_Specific_Attributes/ladee:tec_hot/ladee:tec_hot</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:tec_setpoint">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>ladee:UVS_Specific_Attributes/ladee:tec_setpoint/ladee:tec_setpoint</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:telescope_fov_graze_altitude_above_terrain">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:telescope_fov_graze_altitude_above_terrain/ladee:telescope_fov_graze_altitude_above_terrain</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:telescope_graze_altitude">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:telescope_graze_altitude/ladee:telescope_graze_altitude</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:telescope_graze_altitude_above_terrain">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>ladee:UVS_Specific_Attributes/ladee:telescope_graze_altitude_above_terrain/ladee:telescope_graze_altitude_above_terrain</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="ladee:UVS_Specific_Attributes/ladee:telescope_target">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('DarkSky', 'Earth', 'Limb', 'LitMoon', 'Sun', 'Unknown', 'UnlitMoon')))) then false() else true()">
        <title>ladee:UVS_Specific_Attributes/ladee:telescope_target/ladee:telescope_target</title>
        The attribute ladee:UVS_Specific_Attributes/ladee:telescope_target must be nulled or equal to one of the following values 'DarkSky', 'Earth', 'Limb', 'LitMoon', 'Sun', 'Unknown', 'UnlitMoon'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
