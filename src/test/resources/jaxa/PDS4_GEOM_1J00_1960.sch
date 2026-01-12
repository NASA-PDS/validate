<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:geom  Version:1.9.6.0 - Thu Oct 20 21:51:24 UTC 2022 -->
  <!-- Generated from the PDS4 Information Model Version 1.19.0.0 - System Build 13.0 -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/geom/v1" prefix="geom"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="//geom:Body_Identification_Base/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        <title>//geom:Body_Identification_Base/pds:Internal_Reference/Rule</title>
        For Internal_Reference in Body_Identification_Base, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Central_Body_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Central_Body_Identification/Rule</title>
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Central_Body_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        <title>//geom:Central_Body_Identification/pds:Internal_Reference/Rule</title>
        For Internal_Reference in Central_Body_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Coordinate_Space_Reference/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'to_reference_coordinate_space'
            ">
        <title>//geom:Coordinate_Space_Reference/pds:Local_Internal_Reference/Rule</title>
        For Local_Internal_Reference in geom:Coordinate_Space_Reference, local_reference_type must equal 'to_reference_coordinate_space'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Coordinate_System_Origin_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Coordinate_System_Origin_Identification/Rule</title>
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Coordinate_System_Origin_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        <title>//geom:Coordinate_System_Origin_Identification/pds:Internal_Reference/Rule</title>
        For Internal_Reference in Coordinate_System_Origin_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Frame_Identification_Base/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        <title>//geom:Frame_Identification_Base/pds:Internal_Reference/Rule</title>
        For Internal_Reference in geom:Frame_Identification_Base, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Geometry_Target_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Geometry_Target_Identification/Rule</title>
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Geometry_Target_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        <title>//geom:Geometry_Target_Identification/pds:Internal_Reference/Rule</title>
        For Internal_Reference in geom:Geometry_Target_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Illumination_Geometry/geom:Illumination_Specific">
      <sch:assert test="if (not(geom:reference_location) and not (geom:reference_pixel_location) and not (geom:Reference_Pixel))  then false() else true()">
        <title>//geom:Illumination_Geometry/geom:Illumination_Specific/Rule</title>
        At least one of the following must be present: geom:reference_location, geom:reference_pixel_location, geom:Reference_Pixel.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Observer_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Observer_Identification/Rule</title>
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Observer_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        <title>//geom:Observer_Identification/pds:Internal_Reference/Rule</title>
        For Internal_Reference in geom:Observer_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Reference_Frame_Identification">
      <sch:assert test="if (not(geom:frame_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Reference_Frame_Identification/Rule</title>
        At least one of the following must be present: geom:frame_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Reference_Frame_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        <title>//geom:Reference_Frame_Identification/pds:Internal_Reference/Rule</title>
        For Internal_Reference in geom:Reference_Frame_Identification, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_From">
      <sch:assert test="if (not(geom:frame_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Rotate_From/Rule</title>
        At least one of the following must be present: geom:frame_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_From/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        <title>//geom:Rotate_From/pds:Internal_Reference/Rule</title>
        For Internal_Reference in geom:Rotate_From, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_To">
      <sch:assert test="if (not(geom:frame_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        <title>//geom:Rotate_To/Rule</title>
        At least one of the following must be present: geom:frame_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_To/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        <title>//geom:Rotate_To/pds:Internal_Reference/Rule</title>
        For Internal_Reference in geom:Rotate_To, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:CAHVORE_Model/geom:cahvore_model_type">
      <sch:assert test=". = ('1', '2', '3')">
        <title>geom:CAHVORE_Model/geom:cahvore_model_type/geom:cahvore_model_type</title>
        The attribute geom:CAHVORE_Model/geom:cahvore_model_type must be equal to one of the following values '1', '2', '3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Camera_Model_Parameters/geom:model_type">
      <sch:assert test=". = ('CAHV', 'CAHVOR', 'CAHVORE', 'PSPH')">
        <title>geom:Camera_Model_Parameters/geom:model_type/geom:model_type</title>
        The attribute geom:Camera_Model_Parameters/geom:model_type must be equal to one of the following values 'CAHV', 'CAHVOR', 'CAHVORE', 'PSPH'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Camera_Model_Parameters/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_camera_model'
            ">
        <title>geom:Camera_Model_Parameters/pds:Internal_Reference/Rule</title>
        For Internal_Reference in Camera_Model_Parameters, reference_type must equal 'geometry_to_camera_model'.
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Commanded_Geometry/geom:command_type">
      <sch:assert test=". = ('Angle_Absolute', 'Angle_Relative', 'Joint_Absolute', 'Joint_Relative', 'No_Motion', 'No_Motion_No_Arb', 'None', 'XYZ')">
        <title>geom:Commanded_Geometry/geom:command_type/geom:command_type</title>
        The attribute geom:Commanded_Geometry/geom:command_type must be equal to one of the following values 'Angle_Absolute', 'Angle_Relative', 'Joint_Absolute', 'Joint_Relative', 'No_Motion', 'No_Motion_No_Arb', 'None', 'XYZ'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Definition/geom:positive_azimuth_direction">
      <sch:assert test=". = ('CCW', 'CW', 'Clockwise', 'Counterclockwise')">
        <title>geom:Coordinate_Space_Definition/geom:positive_azimuth_direction/geom:positive_azimuth_direction</title>
        The attribute geom:Coordinate_Space_Definition/geom:positive_azimuth_direction must be equal to one of the following values 'CCW', 'CW', 'Clockwise', 'Counterclockwise'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Definition/geom:positive_elevation_direction">
      <sch:assert test=". = ('Down', 'Nadir', 'Up', 'Zenith')">
        <title>geom:Coordinate_Space_Definition/geom:positive_elevation_direction/geom:positive_elevation_direction</title>
        The attribute geom:Coordinate_Space_Definition/geom:positive_elevation_direction must be equal to one of the following values 'Down', 'Nadir', 'Up', 'Zenith'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Definition/geom:quaternion_measurement_method">
      <sch:assert test=". = ('Bundle_Adjustment', 'Coarse', 'Fine', 'Sun_Find', 'Tilt_Only', 'Unknown')">
        <title>geom:Coordinate_Space_Definition/geom:quaternion_measurement_method/geom:quaternion_measurement_method</title>
        The attribute geom:Coordinate_Space_Definition/geom:quaternion_measurement_method must be equal to one of the following values 'Bundle_Adjustment', 'Coarse', 'Fine', 'Sun_Find', 'Tilt_Only', 'Unknown'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Indexed/geom:coordinate_space_frame_type">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('AEGIS_1', 'AEGIS_2', 'AEGIS_3', 'AEGIS_4', 'AEGIS_5', 'APXS_Frame', 'APXS_Frame', 'ARM_CUSTOM_TCP_FRAME', 'ARM_DOCKING_POST_FRAME', 'ARM_DRILL_FRAME', 'ARM_DRT_FRAME', 'ARM_FCS_FRAME', 'ARM_FRAME', 'ARM_GDRT_FRAME', 'ARM_MAHLI_FRAME', 'ARM_PIXL_FRAME', 'ARM_PORTION_FRAME', 'ARM_SCOOP_TCP_FRAME', 'ARM_SCOOP_TIP_FRAME', 'ARM_SHERLOC_FRAME', 'ARM_TURRET_FRAME', 'ARM_WATSON_FRAME', 'Arm_Custom_TCP_Frame', 'Arm_DRT_Frame', 'Arm_Docking_Post_Frame', 'Arm_Drill_Frame', 'Arm_FCS_Frame', 'Arm_Frame', 'Arm_GDRT_Frame', 'Arm_MAHLI_Frame', 'Arm_PIXL_Frame', 'Arm_Portion_Frame', 'Arm_SHERLOC_Frame', 'Arm_Scoop_TCP_Frame', 'Arm_Scoop_TIP_Frame', 'Arm_Turret_Frame', 'Arm_WATSON_Frame', 'CINT_FRAME', 'CINT_Frame', 'DRILL_BIT_TIP', 'HELI_G_FRAME', 'HELI_M_FRAME', 'HELI_S1_FRAME', 'HELI_S2_FRAME', 'Heli_G_Frame', 'Heli_M_Frame', 'Heli_S1_Frame', 'Heli_S2_Frame', 'LANDER_FRAME', 'LOCAL_LEVEL_FRAME', 'Lander_Frame', 'Local_Level_Frame', 'MB_Frame', 'MCMF_FRAME', 'MCMF_Frame', 'MCZ_CAL_PRIMARY', 'MEDA_RDS', 'MI_Frame', 'Mast_Frame', 'ORBITAL', 'Orbital', 'PIXL_BASE_FRAME', 'PIXL_Base_Frame', 'PIXL_SENSOR_FRAME', 'PIXL_Sensor_Frame', 'PIXL_TOOL', 'PIXL_Tool', 'ROVER_FRAME', 'ROVER_MECH_FRAME', 'ROVER_NAV_FRAME', 'RSM_FRAME', 'RSM_Frame', 'RSM_HEAD_FRAME', 'RSM_Head_Frame', 'Rat_Frame', 'Rover_Frame', 'Rover_Mech_Frame', 'Rover_Nav_Frame', 'SITE_FRAME', 'SUN', 'Site_Frame', 'TOOL_FRAME', 'TURRET_FRAME', 'Tool_Frame', 'Turret_Frame', 'WHEEL_LF', 'WHEEL_LM', 'WHEEL_LR', 'WHEEL_RF', 'WHEEL_RM', 'WHEEL_RR', 'Wheel_LF', 'Wheel_LM', 'Wheel_LR', 'Wheel_RF', 'Wheel_RM', 'Wheel_RR')))) then false() else true()">
        <title>geom:Coordinate_Space_Indexed/geom:coordinate_space_frame_type/geom:coordinate_space_frame_type</title>
        The attribute geom:Coordinate_Space_Indexed/geom:coordinate_space_frame_type must be nulled or equal to one of the following values 'AEGIS_1', 'AEGIS_2', 'AEGIS_3', 'AEGIS_4', 'AEGIS_5', 'APXS_Frame', 'APXS_Frame', 'ARM_CUSTOM_TCP_FRAME', 'ARM_DOCKING_POST_FRAME', 'ARM_DRILL_FRAME', 'ARM_DRT_FRAME', 'ARM_FCS_FRAME', 'ARM_FRAME', 'ARM_GDRT_FRAME', 'ARM_MAHLI_FRAME', 'ARM_PIXL_FRAME', 'ARM_PORTION_FRAME', 'ARM_SCOOP_TCP_FRAME', 'ARM_SCOOP_TIP_FRAME', 'ARM_SHERLOC_FRAME', 'ARM_TURRET_FRAME', 'ARM_WATSON_FRAME', 'Arm_Custom_TCP_Frame', 'Arm_DRT_Frame', 'Arm_Docking_Post_Frame', 'Arm_Drill_Frame', 'Arm_FCS_Frame', 'Arm_Frame', 'Arm_GDRT_Frame', 'Arm_MAHLI_Frame', 'Arm_PIXL_Frame', 'Arm_Portion_Frame', 'Arm_SHERLOC_Frame', 'Arm_Scoop_TCP_Frame', 'Arm_Scoop_TIP_Frame', 'Arm_Turret_Frame', 'Arm_WATSON_Frame', 'CINT_FRAME', 'CINT_Frame', 'DRILL_BIT_TIP', 'HELI_G_FRAME', 'HELI_M_FRAME', 'HELI_S1_FRAME', 'HELI_S2_FRAME', 'Heli_G_Frame', 'Heli_M_Frame', 'Heli_S1_Frame', 'Heli_S2_Frame', 'LANDER_FRAME', 'LOCAL_LEVEL_FRAME', 'Lander_Frame', 'Local_Level_Frame', 'MB_Frame', 'MCMF_FRAME', 'MCMF_Frame', 'MCZ_CAL_PRIMARY', 'MEDA_RDS', 'MI_Frame', 'Mast_Frame', 'ORBITAL', 'Orbital', 'PIXL_BASE_FRAME', 'PIXL_Base_Frame', 'PIXL_SENSOR_FRAME', 'PIXL_Sensor_Frame', 'PIXL_TOOL', 'PIXL_Tool', 'ROVER_FRAME', 'ROVER_MECH_FRAME', 'ROVER_NAV_FRAME', 'RSM_FRAME', 'RSM_Frame', 'RSM_HEAD_FRAME', 'RSM_Head_Frame', 'Rat_Frame', 'Rover_Frame', 'Rover_Mech_Frame', 'Rover_Nav_Frame', 'SITE_FRAME', 'SUN', 'Site_Frame', 'TOOL_FRAME', 'TURRET_FRAME', 'Tool_Frame', 'Turret_Frame', 'WHEEL_LF', 'WHEEL_LM', 'WHEEL_LR', 'WHEEL_RF', 'WHEEL_RM', 'WHEEL_RR', 'Wheel_LF', 'Wheel_LM', 'Wheel_LR', 'Wheel_RF', 'Wheel_RM', 'Wheel_RR'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Quality/geom:attitude_propagation_duration">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>geom:Coordinate_Space_Quality/geom:attitude_propagation_duration/geom:attitude_propagation_duration</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Quality/geom:quaternion_measurement_method">
      <sch:assert test=". = ('Bundle_Adjustment', 'Coarse', 'Fine', 'Sun_Find', 'Tilt_Only', 'Unknown')">
        <title>geom:Coordinate_Space_Quality/geom:quaternion_measurement_method/geom:quaternion_measurement_method</title>
        The attribute geom:Coordinate_Space_Quality/geom:quaternion_measurement_method must be equal to one of the following values 'Bundle_Adjustment', 'Coarse', 'Fine', 'Sun_Find', 'Tilt_Only', 'Unknown'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_System_Identification/geom:coordinate_system_type">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('Azimuth-Elevation', 'Cartesian', 'Planetocentric', 'Planetodetic', 'Planetographic', 'Spherical')))) then false() else true()">
        <title>geom:Coordinate_System_Identification/geom:coordinate_system_type/geom:coordinate_system_type</title>
        The attribute geom:Coordinate_System_Identification/geom:coordinate_system_type must be nulled or equal to one of the following values 'Azimuth-Elevation', 'Cartesian', 'Planetocentric', 'Planetodetic', 'Planetographic', 'Spherical'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:emission_angle/geom:emission_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:incidence_angle/geom:incidence_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:instrument_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:instrument_azimuth/geom:instrument_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:instrument_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:instrument_elevation/geom:instrument_elevation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:phase_angle/geom:phase_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:solar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:solar_azimuth/geom:solar_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:solar_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:solar_elevation/geom:solar_elevation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:solar_image_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:solar_image_clock_angle/geom:solar_image_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:start_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:start_azimuth/geom:start_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:stop_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Derived_Geometry/geom:stop_azimuth/geom:stop_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Derived_Geometry/geom:target_heliocentric_distance/geom:target_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Display_Direction/geom:horizontal_display_direction">
      <sch:assert test=". = ('Left to Right', 'Right to Left')">
        <title>geom:Display_Direction/geom:horizontal_display_direction/geom:horizontal_display_direction</title>
        The attribute geom:Display_Direction/geom:horizontal_display_direction must be equal to one of the following values 'Left to Right', 'Right to Left'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Display_Direction/geom:vertical_display_direction">
      <sch:assert test=". = ('Bottom to Top', 'Top to Bottom')">
        <title>geom:Display_Direction/geom:vertical_display_direction/geom:vertical_display_direction</title>
        The attribute geom:Display_Direction/geom:vertical_display_direction must be equal to one of the following values 'Bottom to Top', 'Top to Bottom'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distance_Generic/geom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distance_Generic/geom:distance/geom:distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max">
      <sch:assert test="if ((geom:minimum_spacecraft_geocentric_distance) and not (geom:maximum_spacecraft_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_geocentric_distance and geom:maximum_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_geocentric_distance) and not (geom:minimum_spacecraft_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_geocentric_distance and geom:maximum_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_heliocentric_distance) and not (geom:maximum_spacecraft_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_heliocentric_distance and geom:maximum_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_heliocentric_distance) and not (geom:minimum_spacecraft_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_heliocentric_distance and geom:maximum_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_central_body_distance) and not (geom:maximum_spacecraft_central_body_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_central_body_distance and geom:maximum_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_central_body_distance) and not (geom:minimum_spacecraft_central_body_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_central_body_distance and geom:maximum_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_target_center_distance) and not (geom:maximum_spacecraft_target_center_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_target_center_distance and geom:maximum_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_target_center_distance) and not (geom:minimum_spacecraft_target_center_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_target_center_distance and geom:maximum_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_target_boresight_intercept_distance) and not (geom:maximum_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_target_boresight_intercept_distance and geom:maximum_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_target_boresight_intercept_distance) and not (geom:minimum_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_target_boresight_intercept_distance and geom:maximum_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_target_subspacecraft_distance) and not (geom:maximum_spacecraft_target_subspacecraft_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_target_subspacecraft_distance and geom:maximum_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_target_subspacecraft_distance) and not (geom:minimum_spacecraft_target_subspacecraft_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_spacecraft_target_subspacecraft_distance and geom:maximum_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_target_geocentric_distance) and not (geom:maximum_target_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_target_geocentric_distance and geom:maximum_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_target_geocentric_distance) and not (geom:minimum_target_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_target_geocentric_distance and geom:maximum_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_target_heliocentric_distance) and not (geom:maximum_target_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_target_heliocentric_distance and geom:maximum_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_target_heliocentric_distance) and not (geom:minimum_target_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_target_heliocentric_distance and geom:maximum_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_target_ssb_distance) and not (geom:maximum_target_ssb_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_target_ssb_distance and geom:maximum_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_target_ssb_distance) and not (geom:minimum_target_ssb_distance))  then false() else true()">
        <title>geom:Distances_Min_Max/Rule</title>
        geom:minimum_target_ssb_distance and geom:maximum_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_spacecraft_central_body_distance/geom:maximum_spacecraft_central_body_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_spacecraft_geocentric_distance/geom:maximum_spacecraft_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_spacecraft_heliocentric_distance/geom:maximum_spacecraft_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_spacecraft_target_boresight_intercept_distance/geom:maximum_spacecraft_target_boresight_intercept_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_spacecraft_target_center_distance/geom:maximum_spacecraft_target_center_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_spacecraft_target_subspacecraft_distance/geom:maximum_spacecraft_target_subspacecraft_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_target_geocentric_distance/geom:maximum_target_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_target_heliocentric_distance/geom:maximum_target_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:maximum_target_ssb_distance/geom:maximum_target_ssb_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_spacecraft_central_body_distance/geom:minimum_spacecraft_central_body_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_spacecraft_geocentric_distance/geom:minimum_spacecraft_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_spacecraft_heliocentric_distance/geom:minimum_spacecraft_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_spacecraft_target_boresight_intercept_distance/geom:minimum_spacecraft_target_boresight_intercept_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_spacecraft_target_center_distance/geom:minimum_spacecraft_target_center_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_spacecraft_target_subspacecraft_distance/geom:minimum_spacecraft_target_subspacecraft_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_target_geocentric_distance/geom:minimum_target_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_target_heliocentric_distance/geom:minimum_target_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Min_Max/geom:minimum_target_ssb_distance/geom:minimum_target_ssb_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:spacecraft_central_body_distance/geom:spacecraft_central_body_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:spacecraft_geocentric_distance/geom:spacecraft_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:spacecraft_heliocentric_distance/geom:spacecraft_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:spacecraft_target_boresight_intercept_distance/geom:spacecraft_target_boresight_intercept_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:spacecraft_target_center_distance/geom:spacecraft_target_center_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:spacecraft_target_subspacecraft_distance/geom:spacecraft_target_subspacecraft_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:target_geocentric_distance/geom:target_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:target_heliocentric_distance/geom:target_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Specific/geom:target_ssb_distance/geom:target_ssb_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop">
      <sch:assert test="ancestor::geom:Geometry_Orbiter/geom:geometry_start_time_utc and ancestor::geom:Geometry_Orbiter/geom:geometry_stop_time_utc">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:If you use the Distances_Start_Stop class, you must give values for Geometry_Orbiter/geometry_start_time_utc and Geometry_Orbiter/geometry_stop_time_utc.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_geocentric_distance) and not (geom:stop_spacecraft_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_geocentric_distance and geom:stop_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_geocentric_distance) and not (geom:start_spacecraft_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_geocentric_distance and geom:stop_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_heliocentric_distance) and not (geom:stop_spacecraft_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_heliocentric_distance and geom:stop_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_heliocentric_distance) and not (geom:start_spacecraft_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_heliocentric_distance and geom:stop_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_central_body_distance) and not (geom:stop_spacecraft_central_body_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_central_body_distance and geom:stop_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_central_body_distance) and not (geom:start_spacecraft_central_body_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_central_body_distance and geom:stop_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_target_center_distance) and not (geom:stop_spacecraft_target_center_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_target_center_distance and geom:stop_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_target_center_distance) and not (geom:start_spacecraft_target_center_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_target_center_distance and geom:stop_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_target_boresight_intercept_distance) and not (geom:stop_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_target_boresight_intercept_distance and geom:stop_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_target_boresight_intercept_distance) and not (geom:start_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_target_boresight_intercept_distance and geom:stop_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_target_subspacecraft_distance) and not (geom:stop_spacecraft_target_subspacecraft_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_target_subspacecraft_distance and geom:stop_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_target_subspacecraft_distance) and not (geom:start_spacecraft_target_subspacecraft_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_spacecraft_target_subspacecraft_distance and geom:stop_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_target_geocentric_distance) and not (geom:stop_target_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_target_geocentric_distance and geom:stop_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_target_geocentric_distance) and not (geom:start_target_geocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_target_geocentric_distance and geom:stop_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_target_heliocentric_distance) and not (geom:stop_target_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_target_heliocentric_distance and geom:stop_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_target_heliocentric_distance) and not (geom:start_target_heliocentric_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_target_heliocentric_distance and geom:stop_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_target_ssb_distance) and not (geom:stop_target_ssb_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_target_ssb_distance and geom:stop_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_target_ssb_distance) and not (geom:start_target_ssb_distance))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_target_ssb_distance and geom:stop_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_time) and not (geom:stop_time))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_time and geom:stop_time, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_time) and not (geom:start_time))  then false() else true()">
        <title>geom:Distances_Start_Stop/Rule</title>
        geom:start_time and geom:stop_time, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_spacecraft_central_body_distance/geom:start_spacecraft_central_body_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_spacecraft_geocentric_distance/geom:start_spacecraft_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_spacecraft_heliocentric_distance/geom:start_spacecraft_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_spacecraft_target_boresight_intercept_distance/geom:start_spacecraft_target_boresight_intercept_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_spacecraft_target_center_distance/geom:start_spacecraft_target_center_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_spacecraft_target_subspacecraft_distance/geom:start_spacecraft_target_subspacecraft_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_target_geocentric_distance/geom:start_target_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_target_heliocentric_distance/geom:start_target_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:start_target_ssb_distance/geom:start_target_ssb_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_spacecraft_central_body_distance/geom:stop_spacecraft_central_body_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_spacecraft_geocentric_distance/geom:stop_spacecraft_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_spacecraft_heliocentric_distance/geom:stop_spacecraft_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_spacecraft_target_boresight_intercept_distance/geom:stop_spacecraft_target_boresight_intercept_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_spacecraft_target_center_distance/geom:stop_spacecraft_target_center_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_spacecraft_target_subspacecraft_distance/geom:stop_spacecraft_target_subspacecraft_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_target_geocentric_distance/geom:stop_target_geocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_target_heliocentric_distance/geom:stop_target_heliocentric_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Distances_Start_Stop/geom:stop_target_ssb_distance/geom:stop_target_ssb_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Expanded_Geometry/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_expanded_geometry'
            ">
        <title>geom:Expanded_Geometry/pds:Internal_Reference/Rule</title>
        For Internal_Reference in Expanded_Geometry, reference_type must equal 'geometry_to_expanded_geometry'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Expanded_Geometry/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'to_expanded_geometry'
            ">
        <title>geom:Expanded_Geometry/pds:Local_Internal_Reference/Rule</title>
        For Local_Internal_Reference in geom:Expanded_Geometry, local_reference_type must equal 'to_expanded_geometry'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Geometry_Orbiter">
      <sch:assert test="if ((geom:geometry_start_time_utc) and not (geom:geometry_stop_time_utc))  then false() else true()">
        <title>geom:Geometry_Orbiter/Rule</title>
        geom:geometry_start_time_utc and geom:geometry_stop_time_utc, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:geometry_stop_time_utc) and not (geom:geometry_start_time_utc))  then false() else true()">
        <title>geom:Geometry_Orbiter/Rule</title>
        geom:geometry_start_time_utc and geom:geometry_stop_time_utc, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Geometry_Orbiter/geom:geometry_reference_time_tdb">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>geom:Geometry_Orbiter/geom:geometry_reference_time_tdb/geom:geometry_reference_time_tdb</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max">
      <sch:assert test="if ((geom:minimum_emission_angle) and not (geom:maximum_emission_angle))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_emission_angle and geom:maximum_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_emission_angle) and not (geom:minimum_emission_angle))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_emission_angle and geom:maximum_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_incidence_angle) and not (geom:maximum_incidence_angle))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_incidence_angle and geom:maximum_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_incidence_angle) and not (geom:minimum_incidence_angle))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_incidence_angle and geom:maximum_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_phase_angle) and not (geom:maximum_phase_angle))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_phase_angle and geom:maximum_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_phase_angle) and not (geom:minimum_phase_angle))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_phase_angle and geom:maximum_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_solar_elongation) and not (geom:maximum_solar_elongation))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_solar_elongation and geom:maximum_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_solar_elongation) and not (geom:minimum_solar_elongation))  then false() else true()">
        <title>geom:Illumination_Min_Max/Rule</title>
        geom:minimum_solar_elongation and geom:maximum_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:maximum_emission_angle/geom:maximum_emission_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:maximum_incidence_angle/geom:maximum_incidence_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:maximum_phase_angle/geom:maximum_phase_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:maximum_solar_elongation/geom:maximum_solar_elongation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:minimum_emission_angle/geom:minimum_emission_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:minimum_incidence_angle/geom:minimum_incidence_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:minimum_phase_angle/geom:minimum_phase_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Min_Max/geom:minimum_solar_elongation/geom:minimum_solar_elongation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Specific/geom:emission_angle/geom:emission_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Specific/geom:incidence_angle/geom:incidence_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Specific/geom:phase_angle/geom:phase_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:reference_location">
      <sch:assert test=". = ('Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center')">
        <title>geom:Illumination_Specific/geom:reference_location/geom:reference_location</title>
        The attribute geom:Illumination_Specific/geom:reference_location must be equal to one of the following values 'Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        <title>geom:Illumination_Specific/geom:reference_pixel_location/geom:reference_pixel_location</title>
        The attribute geom:Illumination_Specific/geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Specific/geom:solar_elongation/geom:solar_elongation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop">
      <sch:assert test="ancestor::geom:Geometry_Orbiter/geom:geometry_start_time_utc and ancestor::geom:Geometry_Orbiter/geom:geometry_stop_time_utc">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:If you use the Illumination_Start_Stop class, you must give values for Geometry_Orbiter/geometry_start_time_utc and Geometry_Orbiter/geometry_stop_time_utc.</sch:assert>
      <sch:assert test="if ((geom:start_emission_angle) and not (geom:stop_emission_angle))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_emission_angle and geom:stop_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_emission_angle) and not (geom:start_emission_angle))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_emission_angle and geom:stop_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_incidence_angle) and not (geom:stop_incidence_angle))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_incidence_angle and geom:stop_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_incidence_angle) and not (geom:start_incidence_angle))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_incidence_angle and geom:stop_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_phase_angle) and not (geom:stop_phase_angle))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_phase_angle and geom:stop_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_phase_angle) and not (geom:start_phase_angle))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_phase_angle and geom:stop_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_solar_elongation) and not (geom:stop_solar_elongation))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_solar_elongation and geom:stop_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_solar_elongation) and not (geom:start_solar_elongation))  then false() else true()">
        <title>geom:Illumination_Start_Stop/Rule</title>
        geom:start_solar_elongation and geom:stop_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:start_emission_angle/geom:start_emission_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:start_incidence_angle/geom:start_incidence_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:start_phase_angle/geom:start_phase_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:start_solar_elongation/geom:start_solar_elongation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:stop_emission_angle/geom:stop_emission_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:stop_incidence_angle/geom:stop_incidence_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:stop_phase_angle/geom:stop_phase_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Illumination_Start_Stop/geom:stop_solar_elongation/geom:stop_solar_elongation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Image_Display_Geometry/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'display_to_data_object'
            ">
        <title>geom:Image_Display_Geometry/pds:Local_Internal_Reference/Rule</title>
        For Local_Internal_Reference in geom:Image_Display_Geometry, local_reference_type must equal 'display_to_data_object'.
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Angle/geom:index_value_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:List_Index_Angle/geom:index_value_angle/geom:index_value_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Length/geom:index_value_length">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:List_Index_Length/geom:index_value_length/geom:index_value_length</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Temperature/geom:index_value_temperature">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>geom:List_Index_Temperature/geom:index_value_temperature/geom:index_value_temperature</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:celestial_east_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:celestial_east_clock_angle/geom:celestial_east_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:celestial_north_clock_angle/geom:celestial_north_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:central_body_north_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:central_body_north_pole_clock_angle/geom:central_body_north_pole_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:central_body_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:central_body_positive_pole_clock_angle/geom:central_body_positive_pole_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:ecliptic_east_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:ecliptic_east_clock_angle/geom:ecliptic_east_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:ecliptic_north_clock_angle/geom:ecliptic_north_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:sun_direction_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:sun_direction_clock_angle/geom:sun_direction_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:target_north_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:target_north_pole_clock_angle/geom:target_north_pole_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:target_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_Clock_Angles/geom:target_positive_pole_clock_angle/geom:target_positive_pole_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_North_East/geom:east_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_North_East/geom:east_azimuth/geom:east_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_North_East/geom:north_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_North_East/geom:north_azimuth/geom:north_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_RA_Dec/geom:celestial_north_clock_angle/geom:celestial_north_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:declination_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_RA_Dec/geom:declination_angle/geom:declination_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_RA_Dec/geom:ecliptic_north_clock_angle/geom:ecliptic_north_clock_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        <title>geom:Object_Orientation_RA_Dec/geom:reference_pixel_location/geom:reference_pixel_location</title>
        The attribute geom:Object_Orientation_RA_Dec/geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:right_ascension_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Object_Orientation_RA_Dec/geom:right_ascension_angle/geom:right_ascension_angle</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:right_ascension_hour_angle">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>geom:Object_Orientation_RA_Dec/geom:right_ascension_hour_angle/geom:right_ascension_hour_angle</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:horizontal_pixel_field_of_view">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Pixel_Dimensions/geom:horizontal_pixel_field_of_view/geom:horizontal_pixel_field_of_view</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:pixel_field_of_view_method">
      <sch:assert test=". = ('Average', 'Central Pixel', 'Constant')">
        <title>geom:Pixel_Dimensions/geom:pixel_field_of_view_method/geom:pixel_field_of_view_method</title>
        The attribute geom:Pixel_Dimensions/geom:pixel_field_of_view_method must be equal to one of the following values 'Average', 'Central Pixel', 'Constant'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:vertical_pixel_field_of_view">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Pixel_Dimensions/geom:vertical_pixel_field_of_view/geom:vertical_pixel_field_of_view</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept">
      <sch:assert test="if (not(geom:reference_pixel_location) and not (geom:Reference_Pixel))  then false() else true()">
        <title>geom:Pixel_Intercept/Rule</title>
        At least one of the following must be present: geom:reference_pixel_location, geom:Reference_Pixel.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:pixel_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Pixel_Intercept/geom:pixel_latitude/geom:pixel_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:pixel_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Pixel_Intercept/geom:pixel_longitude/geom:pixel_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        <title>geom:Pixel_Intercept/geom:reference_pixel_location/geom:reference_pixel_location</title>
        The attribute geom:Pixel_Intercept/geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Pixel_Size_Projected/geom:distance/geom:distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:horizontal_pixel_footprint">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Pixel_Size_Projected/geom:horizontal_pixel_footprint/geom:horizontal_pixel_footprint</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:reference_location">
      <sch:assert test=". = ('Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center')">
        <title>geom:Pixel_Size_Projected/geom:reference_location/geom:reference_location</title>
        The attribute geom:Pixel_Size_Projected/geom:reference_location must be equal to one of the following values 'Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:vertical_pixel_footprint">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Pixel_Size_Projected/geom:vertical_pixel_footprint/geom:vertical_pixel_footprint</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Quaternion_Plus_Direction/geom:rotation_direction">
      <sch:assert test=". = ('Forward', 'From Base', 'Present to Reference', 'Reference to Present', 'Reverse', 'Toward Base')">
        <title>geom:Quaternion_Plus_Direction/geom:rotation_direction/geom:rotation_direction</title>
        The attribute geom:Quaternion_Plus_Direction/geom:rotation_direction must be equal to one of the following values 'Forward', 'From Base', 'Present to Reference', 'Reference to Present', 'Reverse', 'Toward Base'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Reference_Pixel/geom:horizontal_coordinate_pixel">
      <sch:assert test="@unit = ('DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel')">
        <title>geom:Reference_Pixel/geom:horizontal_coordinate_pixel/geom:horizontal_coordinate_pixel</title>
        The attribute @unit must be equal to one of the following values 'DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Reference_Pixel/geom:vertical_coordinate_pixel">
      <sch:assert test="@unit = ('DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel')">
        <title>geom:Reference_Pixel/geom:vertical_coordinate_pixel/geom:vertical_coordinate_pixel</title>
        The attribute @unit must be equal to one of the following values 'DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/geom:kernel_provenance">
      <sch:assert test=". = ('Mixed', 'Predicted', 'Provenance Not Applicable', 'Reconstructed')">
        <title>geom:SPICE_Kernel_Identification/geom:kernel_provenance/geom:kernel_provenance</title>
        The attribute geom:SPICE_Kernel_Identification/geom:kernel_provenance must be equal to one of the following values 'Mixed', 'Predicted', 'Provenance Not Applicable', 'Reconstructed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_SPICE_kernel'
            ">
        <title>geom:SPICE_Kernel_Identification/pds:Internal_Reference/Rule</title>
        For Internal_Reference in SPICE_Kernel_Identification, reference_type must equal 'geometry_to_SPICE_kernel'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/pds:kernel_type">
      <sch:assert test=". = ('CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK')">
        <title>geom:SPICE_Kernel_Identification/pds:kernel_type/pds:kernel_type</title>
        The attribute geom:SPICE_Kernel_Identification/pds:kernel_type must be equal to one of the following values 'CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max">
      <sch:assert test="if ((geom:minimum_latitude) and not (geom:maximum_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_latitude and geom:maximum_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_latitude) and not (geom:minimum_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_latitude and geom:maximum_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_longitude) and not (geom:maximum_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_longitude and geom:maximum_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_longitude) and not (geom:minimum_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_longitude and geom:maximum_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subsolar_azimuth) and not (geom:maximum_subsolar_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subsolar_azimuth and geom:maximum_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subsolar_azimuth) and not (geom:minimum_subsolar_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subsolar_azimuth and geom:maximum_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subsolar_latitude) and not (geom:maximum_subsolar_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subsolar_latitude and geom:maximum_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subsolar_latitude) and not (geom:minimum_subsolar_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subsolar_latitude and geom:maximum_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subsolar_longitude) and not (geom:maximum_subsolar_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subsolar_longitude and geom:maximum_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subsolar_longitude) and not (geom:minimum_subsolar_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subsolar_longitude and geom:maximum_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subspacecraft_azimuth) and not (geom:maximum_subspacecraft_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subspacecraft_azimuth and geom:maximum_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subspacecraft_azimuth) and not (geom:minimum_subspacecraft_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subspacecraft_azimuth and geom:maximum_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subspacecraft_latitude) and not (geom:maximum_subspacecraft_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subspacecraft_latitude and geom:maximum_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subspacecraft_latitude) and not (geom:minimum_subspacecraft_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subspacecraft_latitude and geom:maximum_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subspacecraft_longitude) and not (geom:maximum_subspacecraft_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subspacecraft_longitude and geom:maximum_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subspacecraft_longitude) and not (geom:minimum_subspacecraft_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Min_Max/Rule</title>
        geom:minimum_subspacecraft_longitude and geom:maximum_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_latitude/geom:maximum_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_longitude/geom:maximum_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_azimuth/geom:maximum_subsolar_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_latitude/geom:maximum_subsolar_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_longitude/geom:maximum_subsolar_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_azimuth/geom:maximum_subspacecraft_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_latitude/geom:maximum_subspacecraft_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_longitude/geom:maximum_subspacecraft_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_latitude/geom:minimum_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_longitude/geom:minimum_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_azimuth/geom:minimum_subsolar_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_latitude/geom:minimum_subsolar_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_longitude/geom:minimum_subsolar_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_azimuth/geom:minimum_subspacecraft_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_latitude/geom:minimum_subspacecraft_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_longitude/geom:minimum_subspacecraft_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Specific/geom:subsolar_azimuth/geom:subsolar_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Specific/geom:subsolar_latitude/geom:subsolar_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Specific/geom:subsolar_longitude/geom:subsolar_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Specific/geom:subspacecraft_azimuth/geom:subspacecraft_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Specific/geom:subspacecraft_latitude/geom:subspacecraft_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Specific/geom:subspacecraft_longitude/geom:subspacecraft_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop">
      <sch:assert test="if ((geom:start_latitude) and not (geom:lat_long_method))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        If you give geom:start_latitude and geom:stop_latitude, then a value for lat_long_method must be given.</sch:assert>
      <sch:assert test="ancestor::geom:Geometry_Orbiter/geom:geometry_start_time_utc and ancestor::geom:Geometry_Orbiter/geom:geometry_stop_time_utc">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:If you use the Surface_Geometry_Start_Stop class, you must give values for Geometry_Orbiter/geometry_start_time_utc and Geometry_Orbiter/geometry_stop_time_utc.</sch:assert>
      <sch:assert test="if ((geom:start_latitude) and not (geom:stop_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_latitude and geom:stop_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_latitude) and not (geom:start_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_latitude and geom:stop_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_longitude) and not (geom:stop_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_longitude and geom:stop_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_longitude) and not (geom:start_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_longitude and geom:stop_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subsolar_azimuth) and not (geom:stop_subsolar_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subsolar_azimuth and geom:stop_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subsolar_azimuth) and not (geom:start_subsolar_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subsolar_azimuth and geom:stop_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subsolar_latitude) and not (geom:stop_subsolar_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subsolar_latitude and geom:stop_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subsolar_latitude) and not (geom:start_subsolar_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subsolar_latitude and geom:stop_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subsolar_longitude) and not (geom:stop_subsolar_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subsolar_longitude and geom:stop_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subsolar_longitude) and not (geom:start_subsolar_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subsolar_longitude and geom:stop_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subspacecraft_azimuth) and not (geom:stop_subspacecraft_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subspacecraft_azimuth and geom:stop_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subspacecraft_azimuth) and not (geom:start_subspacecraft_azimuth))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subspacecraft_azimuth and geom:stop_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subspacecraft_latitude) and not (geom:stop_subspacecraft_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subspacecraft_latitude and geom:stop_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subspacecraft_latitude) and not (geom:start_subspacecraft_latitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subspacecraft_latitude and geom:stop_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subspacecraft_longitude) and not (geom:stop_subspacecraft_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subspacecraft_longitude and geom:stop_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subspacecraft_longitude) and not (geom:start_subspacecraft_longitude))  then false() else true()">
        <title>geom:Surface_Geometry_Start_Stop/Rule</title>
        geom:start_subspacecraft_longitude and geom:stop_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:lat_long_method">
      <sch:assert test=". = ('Center', 'Mean', 'Median')">
        <title>geom:Surface_Geometry_Start_Stop/geom:lat_long_method/geom:lat_long_method</title>
        The attribute geom:Surface_Geometry_Start_Stop/geom:lat_long_method must be equal to one of the following values 'Center', 'Mean', 'Median'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_latitude/geom:start_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_longitude/geom:start_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_subsolar_azimuth/geom:start_subsolar_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_subsolar_latitude/geom:start_subsolar_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_subsolar_longitude/geom:start_subsolar_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_azimuth/geom:start_subspacecraft_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_latitude/geom:start_subspacecraft_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_longitude/geom:start_subspacecraft_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_latitude/geom:stop_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_longitude/geom:stop_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_azimuth/geom:stop_subsolar_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_latitude/geom:stop_subsolar_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_longitude/geom:stop_subsolar_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_azimuth/geom:stop_subspacecraft_azimuth</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_latitude/geom:stop_subspacecraft_latitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_longitude/geom:stop_subspacecraft_longitude</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Base/geom:x_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        <title>geom:Vector_Cartesian_Acceleration_Base/geom:x_acceleration/geom:x_acceleration</title>
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Base/geom:y_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        <title>geom:Vector_Cartesian_Acceleration_Base/geom:y_acceleration/geom:y_acceleration</title>
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Base/geom:z_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        <title>geom:Vector_Cartesian_Acceleration_Base/geom:z_acceleration/geom:z_acceleration</title>
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Acceleration_Extended_Base/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Acceleration_Extended_Base/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Generic/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Acceleration_Generic/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Acceleration_Generic/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Base/geom:x_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Vector_Cartesian_Position_Base/geom:x_position/geom:x_position</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Base/geom:y_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Vector_Cartesian_Position_Base/geom:y_position/geom:y_position</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Base/geom:z_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Vector_Cartesian_Position_Base/geom:z_position/geom:z_position</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Central_Body_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Central_Body_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Central_Body_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Earth_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Earth_To_Central_Body/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Earth_To_Central_Body/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Earth_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Earth_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Earth_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Earth_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Earth_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Earth_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Extended_Base/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Extended_Base/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Generic/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Generic/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Generic/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_SSB_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_SSB_To_Central_Body/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_SSB_To_Central_Body/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_SSB_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_SSB_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_SSB_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_SSB_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_SSB_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_SSB_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Spacecraft_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Spacecraft_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Spacecraft_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Sun_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Sun_To_Central_Body/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Sun_To_Central_Body/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Sun_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Sun_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Sun_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Sun_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Position_Sun_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Position_Sun_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Base/geom:x_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>geom:Vector_Cartesian_Velocity_Base/geom:x_velocity/geom:x_velocity</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Base/geom:y_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>geom:Vector_Cartesian_Velocity_Base/geom:y_velocity/geom:y_velocity</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Base/geom:z_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>geom:Vector_Cartesian_Velocity_Base/geom:z_velocity/geom:z_velocity</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Extended_Base/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Extended_Base/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Generic/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Generic/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Generic/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Central_Body/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Central_Body/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Earth/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Earth/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Earth/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_SSB/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_SSB/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_SSB/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Sun/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Sun/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Sun/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Earth/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Target_Relative_To_Earth/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Target_Relative_To_Earth/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_SSB/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Target_Relative_To_SSB/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Target_Relative_To_SSB/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Sun/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Cartesian_Velocity_Target_Relative_To_Sun/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Cartesian_Velocity_Target_Relative_To_Sun/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Base/geom:latitude_position">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Vector_Planetocentric_Position_Base/geom:latitude_position/geom:latitude_position</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Base/geom:longitude_position">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>geom:Vector_Planetocentric_Position_Base/geom:longitude_position/geom:longitude_position</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Base/geom:radius_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>geom:Vector_Planetocentric_Position_Base/geom:radius_position/geom:radius_position</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Central_Body_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Position_Central_Body_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Position_Central_Body_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Position_Extended_Base/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Position_Extended_Base/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Generic/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Position_Generic/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Position_Generic/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Spacecraft_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Position_Spacecraft_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Position_Spacecraft_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Base/geom:latitude_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        <title>geom:Vector_Planetocentric_Velocity_Base/geom:latitude_velocity/geom:latitude_velocity</title>
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Base/geom:longitude_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        <title>geom:Vector_Planetocentric_Velocity_Base/geom:longitude_velocity/geom:longitude_velocity</title>
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Base/geom:radial_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        <title>geom:Vector_Planetocentric_Velocity_Base/geom:radial_velocity/geom:radial_velocity</title>
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Velocity_Extended_Base/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Velocity_Extended_Base/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Generic/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Velocity_Generic/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Velocity_Generic/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test="if (not(@xsi:nil eq 'true') and (not(. = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')))) then false() else true()">
        <title>geom:Vector_Planetocentric_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied/geom:light_time_correction_applied</title>
        The attribute geom:Vector_Planetocentric_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied must be nulled or equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
