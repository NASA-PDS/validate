<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:geom  Version:1.7.1.0 - Mon May 06 10:24:19 CDT 2019 -->
  <!-- Generated from the PDS4 Information Model Version 1.11.1.0 - System Build 9b -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

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
    <sch:rule context="//geom:Central_Body_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Central_Body_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        For Internal_Reference in Central_Body_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Coordinate_System_Origin_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Coordinate_System_Origin_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        For Internal_Reference in Coordinate_System_Origin_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max">
      <sch:assert test="if ((geom:minimum_spacecraft_geocentric_distance) and not (geom:maximum_spacecraft_geocentric_distance))  then false() else true()">
        geom:minimum_spacecraft_geocentric_distance and geom:maximum_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_geocentric_distance) and not (geom:minimum_spacecraft_geocentric_distance))  then false() else true()">
        geom:minimum_spacecraft_geocentric_distance and geom:maximum_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_heliocentric_distance) and not (geom:maximum_spacecraft_heliocentric_distance))  then false() else true()">
        geom:minimum_spacecraft_heliocentric_distance and geom:maximum_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_heliocentric_distance) and not (geom:minimum_spacecraft_heliocentric_distance))  then false() else true()">
        geom:minimum_spacecraft_heliocentric_distance and geom:maximum_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_central_body_distance) and not (geom:maximum_spacecraft_central_body_distance))  then false() else true()">
        geom:minimum_spacecraft_central_body_distance and geom:maximum_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_central_body_distance) and not (geom:minimum_spacecraft_central_body_distance))  then false() else true()">
        geom:minimum_spacecraft_central_body_distance and geom:maximum_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_target_center_distance) and not (geom:maximum_spacecraft_target_center_distance))  then false() else true()">
        geom:minimum_spacecraft_target_center_distance and geom:maximum_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_target_center_distance) and not (geom:minimum_spacecraft_target_center_distance))  then false() else true()">
        geom:minimum_spacecraft_target_center_distance and geom:maximum_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_target_boresight_intercept_distance) and not (geom:maximum_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        geom:minimum_spacecraft_target_boresight_intercept_distance and geom:maximum_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_target_boresight_intercept_distance) and not (geom:minimum_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        geom:minimum_spacecraft_target_boresight_intercept_distance and geom:maximum_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_spacecraft_target_subspacecraft_distance) and not (geom:maximum_spacecraft_target_subspacecraft_distance))  then false() else true()">
        geom:minimum_spacecraft_target_subspacecraft_distance and geom:maximum_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_spacecraft_target_subspacecraft_distance) and not (geom:minimum_spacecraft_target_subspacecraft_distance))  then false() else true()">
        geom:minimum_spacecraft_target_subspacecraft_distance and geom:maximum_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_target_geocentric_distance) and not (geom:maximum_target_geocentric_distance))  then false() else true()">
        geom:minimum_target_geocentric_distance and geom:maximum_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_target_geocentric_distance) and not (geom:minimum_target_geocentric_distance))  then false() else true()">
        geom:minimum_target_geocentric_distance and geom:maximum_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_target_heliocentric_distance) and not (geom:maximum_target_heliocentric_distance))  then false() else true()">
        geom:minimum_target_heliocentric_distance and geom:maximum_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_target_heliocentric_distance) and not (geom:minimum_target_heliocentric_distance))  then false() else true()">
        geom:minimum_target_heliocentric_distance and geom:maximum_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_target_ssb_distance) and not (geom:maximum_target_ssb_distance))  then false() else true()">
        geom:minimum_target_ssb_distance and geom:maximum_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_target_ssb_distance) and not (geom:minimum_target_ssb_distance))  then false() else true()">
        geom:minimum_target_ssb_distance and geom:maximum_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop">
      <sch:assert test="ancestor::geom:Geometry_Orbiter/geom:geometry_start_time_utc and ancestor::geom:Geometry_Orbiter/geom:geometry_stop_time_utc">
        geom:If you use the Distances_Start_Stop class, you must give values for Geometry_Orbiter/geometry_start_time_utc and Geometry_Orbiter/geometry_stop_time_utc.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_geocentric_distance) and not (geom:stop_spacecraft_geocentric_distance))  then false() else true()">
        geom:start_spacecraft_geocentric_distance and geom:stop_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_geocentric_distance) and not (geom:start_spacecraft_geocentric_distance))  then false() else true()">
        geom:start_spacecraft_geocentric_distance and geom:stop_spacecraft_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_heliocentric_distance) and not (geom:stop_spacecraft_heliocentric_distance))  then false() else true()">
        geom:start_spacecraft_heliocentric_distance and geom:stop_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_heliocentric_distance) and not (geom:start_spacecraft_heliocentric_distance))  then false() else true()">
        geom:start_spacecraft_heliocentric_distance and geom:stop_spacecraft_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_central_body_distance) and not (geom:stop_spacecraft_central_body_distance))  then false() else true()">
        geom:start_spacecraft_central_body_distance and geom:stop_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_central_body_distance) and not (geom:start_spacecraft_central_body_distance))  then false() else true()">
        geom:start_spacecraft_central_body_distance and geom:stop_spacecraft_central_body_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_target_center_distance) and not (geom:stop_spacecraft_target_center_distance))  then false() else true()">
        geom:start_spacecraft_target_center_distance and geom:stop_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_target_center_distance) and not (geom:start_spacecraft_target_center_distance))  then false() else true()">
        geom:start_spacecraft_target_center_distance and geom:stop_spacecraft_target_center_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_target_boresight_intercept_distance) and not (geom:stop_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        geom:start_spacecraft_target_boresight_intercept_distance and geom:stop_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_target_boresight_intercept_distance) and not (geom:start_spacecraft_target_boresight_intercept_distance))  then false() else true()">
        geom:start_spacecraft_target_boresight_intercept_distance and geom:stop_spacecraft_target_boresight_intercept_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_spacecraft_target_subspacecraft_distance) and not (geom:stop_spacecraft_target_subspacecraft_distance))  then false() else true()">
        geom:start_spacecraft_target_subspacecraft_distance and geom:stop_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_spacecraft_target_subspacecraft_distance) and not (geom:start_spacecraft_target_subspacecraft_distance))  then false() else true()">
        geom:start_spacecraft_target_subspacecraft_distance and geom:stop_spacecraft_target_subspacecraft_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_target_geocentric_distance) and not (geom:stop_target_geocentric_distance))  then false() else true()">
        geom:start_target_geocentric_distance and geom:stop_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_target_geocentric_distance) and not (geom:start_target_geocentric_distance))  then false() else true()">
        geom:start_target_geocentric_distance and geom:stop_target_geocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_target_heliocentric_distance) and not (geom:stop_target_heliocentric_distance))  then false() else true()">
        geom:start_target_heliocentric_distance and geom:stop_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_target_heliocentric_distance) and not (geom:start_target_heliocentric_distance))  then false() else true()">
        geom:start_target_heliocentric_distance and geom:stop_target_heliocentric_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_target_ssb_distance) and not (geom:stop_target_ssb_distance))  then false() else true()">
        geom:start_target_ssb_distance and geom:stop_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_target_ssb_distance) and not (geom:start_target_ssb_distance))  then false() else true()">
        geom:start_target_ssb_distance and geom:stop_target_ssb_distance, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_time) and not (geom:stop_time))  then false() else true()">
        geom:start_time and geom:stop_time, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_time) and not (geom:start_time))  then false() else true()">
        geom:start_time and geom:stop_time, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Expanded_Geometry/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_expanded_geometry'
            ">
        For Internal_Reference in Expanded_Geometry, reference_type must equal 'geometry_to_expanded_geometry'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Expanded_Geometry/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'to_expanded_geometry'
            ">
        For Local_Internal_Reference in geom:Expanded_Geometry, local_reference_type must equal 'to_expanded_geometry'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Geometry_Orbiter">
      <sch:assert test="if ((geom:geometry_start_time_utc) and not (geom:geometry_stop_time_utc))  then false() else true()">
        geom:geometry_start_time_utc and geom:geometry_stop_time_utc, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:geometry_stop_time_utc) and not (geom:geometry_start_time_utc))  then false() else true()">
        geom:geometry_start_time_utc and geom:geometry_stop_time_utc, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Geometry_Target_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Geometry_Target_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        For Internal_Reference in geom:Geometry_Target_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max">
      <sch:assert test="if ((geom:minimum_emission_angle) and not (geom:maximum_emission_angle))  then false() else true()">
        geom:minimum_emission_angle and geom:maximum_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_emission_angle) and not (geom:minimum_emission_angle))  then false() else true()">
        geom:minimum_emission_angle and geom:maximum_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_incidence_angle) and not (geom:maximum_incidence_angle))  then false() else true()">
        geom:minimum_incidence_angle and geom:maximum_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_incidence_angle) and not (geom:minimum_incidence_angle))  then false() else true()">
        geom:minimum_incidence_angle and geom:maximum_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_phase_angle) and not (geom:maximum_phase_angle))  then false() else true()">
        geom:minimum_phase_angle and geom:maximum_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_phase_angle) and not (geom:minimum_phase_angle))  then false() else true()">
        geom:minimum_phase_angle and geom:maximum_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_solar_elongation) and not (geom:maximum_solar_elongation))  then false() else true()">
        geom:minimum_solar_elongation and geom:maximum_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_solar_elongation) and not (geom:minimum_solar_elongation))  then false() else true()">
        geom:minimum_solar_elongation and geom:maximum_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop">
      <sch:assert test="ancestor::geom:Geometry_Orbiter/geom:geometry_start_time_utc and ancestor::geom:Geometry_Orbiter/geom:geometry_stop_time_utc">
        geom:If you use the Illumination_Start_Stop class, you must give values for Geometry_Orbiter/geometry_start_time_utc and Geometry_Orbiter/geometry_stop_time_utc.</sch:assert>
      <sch:assert test="if ((geom:start_emission_angle) and not (geom:stop_emission_angle))  then false() else true()">
        geom:start_emission_angle and geom:stop_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_emission_angle) and not (geom:start_emission_angle))  then false() else true()">
        geom:start_emission_angle and geom:stop_emission_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_incidence_angle) and not (geom:stop_incidence_angle))  then false() else true()">
        geom:start_incidence_angle and geom:stop_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_incidence_angle) and not (geom:start_incidence_angle))  then false() else true()">
        geom:start_incidence_angle and geom:stop_incidence_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_phase_angle) and not (geom:stop_phase_angle))  then false() else true()">
        geom:start_phase_angle and geom:stop_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_phase_angle) and not (geom:start_phase_angle))  then false() else true()">
        geom:start_phase_angle and geom:stop_phase_angle, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_solar_elongation) and not (geom:stop_solar_elongation))  then false() else true()">
        geom:start_solar_elongation and geom:stop_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_solar_elongation) and not (geom:start_solar_elongation))  then false() else true()">
        geom:start_solar_elongation and geom:stop_solar_elongation, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Illumination_Geometry/geom:Illumination_Specific">
      <sch:assert test="if (not(geom:reference_location) and not (geom:reference_pixel_location) and not (geom:Reference_Pixel))  then false() else true()">
        At least one of the following must be present: geom:reference_location, geom:reference_pixel_location, geom:Reference_Pixel.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Image_Display_Geometry/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'display_to_data_object'
            ">
        For Local_Internal_Reference in geom:Image_Display_Geometry, local_reference_type must equal 'display_to_data_object'.
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Observer_Identification">
      <sch:assert test="if (not(geom:body_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:body_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Observer_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_body'
            ">
        For Internal_Reference in geom:Observer_Identification, reference_type must equal 'geometry_to_body'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept">
      <sch:assert test="if (not(geom:reference_pixel_location) and not (geom:Reference_Pixel))  then false() else true()">
        At least one of the following must be present: geom:reference_pixel_location, geom:Reference_Pixel.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Reference_Frame_Identification">
      <sch:assert test="if (not(geom:frame_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:frame_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Reference_Frame_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        For Internal_Reference in geom:Reference_Frame_Identification, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_From">
      <sch:assert test="if (not(geom:frame_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:frame_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_From/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        For Internal_Reference in geom:Rotate_From, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_To">
      <sch:assert test="if (not(geom:frame_spice_name) and not (geom:name) and not (pds:Internal_Reference))  then false() else true()">
        At least one of the following must be present: geom:frame_spice_name, geom:name, pds:Internal_Reference.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Rotate_To/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_reference_frame'
            ">
        For Internal_Reference in geom:Rotate_To, reference_type must equal 'geometry_to_reference_frame'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max">
      <sch:assert test="if ((geom:minimum_latitude) and not (geom:maximum_latitude))  then false() else true()">
        geom:minimum_latitude and geom:maximum_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_latitude) and not (geom:minimum_latitude))  then false() else true()">
        geom:minimum_latitude and geom:maximum_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_longitude) and not (geom:maximum_longitude))  then false() else true()">
        geom:minimum_longitude and geom:maximum_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_longitude) and not (geom:minimum_longitude))  then false() else true()">
        geom:minimum_longitude and geom:maximum_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subsolar_azimuth) and not (geom:maximum_subsolar_azimuth))  then false() else true()">
        geom:minimum_subsolar_azimuth and geom:maximum_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subsolar_azimuth) and not (geom:minimum_subsolar_azimuth))  then false() else true()">
        geom:minimum_subsolar_azimuth and geom:maximum_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subsolar_latitude) and not (geom:maximum_subsolar_latitude))  then false() else true()">
        geom:minimum_subsolar_latitude and geom:maximum_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subsolar_latitude) and not (geom:minimum_subsolar_latitude))  then false() else true()">
        geom:minimum_subsolar_latitude and geom:maximum_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subsolar_longitude) and not (geom:maximum_subsolar_longitude))  then false() else true()">
        geom:minimum_subsolar_longitude and geom:maximum_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subsolar_longitude) and not (geom:minimum_subsolar_longitude))  then false() else true()">
        geom:minimum_subsolar_longitude and geom:maximum_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subspacecraft_azimuth) and not (geom:maximum_subspacecraft_azimuth))  then false() else true()">
        geom:minimum_subspacecraft_azimuth and geom:maximum_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subspacecraft_azimuth) and not (geom:minimum_subspacecraft_azimuth))  then false() else true()">
        geom:minimum_subspacecraft_azimuth and geom:maximum_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subspacecraft_latitude) and not (geom:maximum_subspacecraft_latitude))  then false() else true()">
        geom:minimum_subspacecraft_latitude and geom:maximum_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subspacecraft_latitude) and not (geom:minimum_subspacecraft_latitude))  then false() else true()">
        geom:minimum_subspacecraft_latitude and geom:maximum_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:minimum_subspacecraft_longitude) and not (geom:maximum_subspacecraft_longitude))  then false() else true()">
        geom:minimum_subspacecraft_longitude and geom:maximum_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:maximum_subspacecraft_longitude) and not (geom:minimum_subspacecraft_longitude))  then false() else true()">
        geom:minimum_subspacecraft_longitude and geom:maximum_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop">
      <sch:assert test="if ((geom:start_latitude) and not (geom:lat_long_method))  then false() else true()">
        If you give geom:start_latitude and geom:stop_latitude, then a value for lat_long_method must be given.</sch:assert>
      <sch:assert test="ancestor::geom:Geometry_Orbiter/geom:geometry_start_time_utc and ancestor::geom:Geometry_Orbiter/geom:geometry_stop_time_utc">
        geom:If you use the Surface_Geometry_Start_Stop class, you must give values for Geometry_Orbiter/geometry_start_time_utc and Geometry_Orbiter/geometry_stop_time_utc.</sch:assert>
      <sch:assert test="if ((geom:start_latitude) and not (geom:stop_latitude))  then false() else true()">
        geom:start_latitude and geom:stop_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_latitude) and not (geom:start_latitude))  then false() else true()">
        geom:start_latitude and geom:stop_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_longitude) and not (geom:stop_longitude))  then false() else true()">
        geom:start_longitude and geom:stop_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_longitude) and not (geom:start_longitude))  then false() else true()">
        geom:start_longitude and geom:stop_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subsolar_azimuth) and not (geom:stop_subsolar_azimuth))  then false() else true()">
        geom:start_subsolar_azimuth and geom:stop_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subsolar_azimuth) and not (geom:start_subsolar_azimuth))  then false() else true()">
        geom:start_subsolar_azimuth and geom:stop_subsolar_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subsolar_latitude) and not (geom:stop_subsolar_latitude))  then false() else true()">
        geom:start_subsolar_latitude and geom:stop_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subsolar_latitude) and not (geom:start_subsolar_latitude))  then false() else true()">
        geom:start_subsolar_latitude and geom:stop_subsolar_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subsolar_longitude) and not (geom:stop_subsolar_longitude))  then false() else true()">
        geom:start_subsolar_longitude and geom:stop_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subsolar_longitude) and not (geom:start_subsolar_longitude))  then false() else true()">
        geom:start_subsolar_longitude and geom:stop_subsolar_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subspacecraft_azimuth) and not (geom:stop_subspacecraft_azimuth))  then false() else true()">
        geom:start_subspacecraft_azimuth and geom:stop_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subspacecraft_azimuth) and not (geom:start_subspacecraft_azimuth))  then false() else true()">
        geom:start_subspacecraft_azimuth and geom:stop_subspacecraft_azimuth, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subspacecraft_latitude) and not (geom:stop_subspacecraft_latitude))  then false() else true()">
        geom:start_subspacecraft_latitude and geom:stop_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subspacecraft_latitude) and not (geom:start_subspacecraft_latitude))  then false() else true()">
        geom:start_subspacecraft_latitude and geom:stop_subspacecraft_latitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:start_subspacecraft_longitude) and not (geom:stop_subspacecraft_longitude))  then false() else true()">
        geom:start_subspacecraft_longitude and geom:stop_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
      <sch:assert test="if ((geom:stop_subspacecraft_longitude) and not (geom:start_subspacecraft_longitude))  then false() else true()">
        geom:start_subspacecraft_longitude and geom:stop_subspacecraft_longitude, are a pair; if you use one, you must use both.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Camera_Model_Parameters/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_camera_model'
            ">
        For Internal_Reference in Camera_Model_Parameters, reference_type must equal 'geometry_to_camera_model'.
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//geom:Coordinate_Space_Reference/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'to_reference_coordinate_space'
            ">
        For Local_Internal_Reference in geom:Coordinate_Space_Reference, local_reference_type must equal 'to_reference_coordinate_space'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:CAHVORE_Model/geom:cahvore_model_type">
      <sch:assert test=". = ('1', '2', '3')">
        The attribute geom:cahvore_model_type must be equal to one of the following values '1', '2', '3'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Definition/geom:positive_azimuth_direction">
      <sch:assert test=". = ('CCW', 'CW', 'Clockwise', 'Counterclockwise')">
        The attribute geom:positive_azimuth_direction must be equal to one of the following values 'CCW', 'CW', 'Clockwise', 'Counterclockwise'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Definition/geom:positive_elevation_direction">
      <sch:assert test=". = ('Down', 'Nadir', 'Up', 'Zenith')">
        The attribute geom:positive_elevation_direction must be equal to one of the following values 'Down', 'Nadir', 'Up', 'Zenith'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_Space_Definition/geom:quaternion_measurement_method">
      <sch:assert test=". = ('Coarse', 'Fine', 'Tilt_Only', 'Unknown')">
        The attribute geom:quaternion_measurement_method must be equal to one of the following values 'Coarse', 'Fine', 'Tilt_Only', 'Unknown'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Coordinate_System_Identification/geom:coordinate_system_type">
      <sch:assert test=". = ('Azimuth-Elevation', 'Cartesian', 'Planetocentric', 'Planetodetic', 'Planetographic', 'Spherical')">
        The attribute geom:coordinate_system_type must be equal to one of the following values 'Azimuth-Elevation', 'Cartesian', 'Planetocentric', 'Planetodetic', 'Planetographic', 'Spherical'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:instrument_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:instrument_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:solar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:solar_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:start_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Derived_Geometry/geom:stop_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Display_Direction/geom:horizontal_display_direction">
      <sch:assert test=". = ('Left to Right', 'Right to Left')">
        The attribute geom:horizontal_display_direction must be equal to one of the following values 'Left to Right', 'Right to Left'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Display_Direction/geom:vertical_display_direction">
      <sch:assert test=". = ('Bottom to Top', 'Top to Bottom')">
        The attribute geom:vertical_display_direction must be equal to one of the following values 'Bottom to Top', 'Top to Bottom'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distance_Generic/geom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:maximum_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Min_Max/geom:minimum_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Specific/geom:target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:start_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_central_body_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_target_boresight_intercept_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_target_center_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_spacecraft_target_subspacecraft_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_target_geocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_target_heliocentric_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Distances_Start_Stop/geom:stop_target_ssb_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Geometry_Orbiter/geom:geometry_reference_time_tdb">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr')">
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:maximum_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Min_Max/geom:minimum_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:reference_location">
      <sch:assert test=". = ('Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center')">
        The attribute geom:reference_location must be equal to one of the following values 'Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        The attribute geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Specific/geom:solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:start_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_emission_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_incidence_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_phase_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Illumination_Start_Stop/geom:stop_solar_elongation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Angle/geom:index_value_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Length/geom:index_value_length">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:List_Index_Temperature/geom:index_value_temperature">
      <sch:assert test="@unit = ('K', 'degC')">
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:celestial_east_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:central_body_north_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:central_body_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:ecliptic_east_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:sun_direction_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:target_north_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_Clock_Angles/geom:target_positive_pole_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_North_East/geom:east_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_North_East/geom:north_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:celestial_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:declination_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:ecliptic_north_clock_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        The attribute geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:right_ascension_angle">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Object_Orientation_RA_Dec/geom:right_ascension_hour_angle">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr')">
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:horizontal_pixel_field_of_view">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:pixel_field_of_view_method">
      <sch:assert test=". = ('Average', 'Central Pixel', 'Constant')">
        The attribute geom:pixel_field_of_view_method must be equal to one of the following values 'Average', 'Central Pixel', 'Constant'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Dimensions/geom:vertical_pixel_field_of_view">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:pixel_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:pixel_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Intercept/geom:reference_pixel_location">
      <sch:assert test=". = ('Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner')">
        The attribute geom:reference_pixel_location must be equal to one of the following values 'Center', 'Lower Left Corner', 'Lower Right Corner', 'Upper Left Corner', 'Upper Right Corner'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:horizontal_pixel_footprint">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:reference_location">
      <sch:assert test=". = ('Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center')">
        The attribute geom:reference_location must be equal to one of the following values 'Boresight Intercept Point', 'Constant', 'Subspacecraft Point', 'Target Center'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Pixel_Size_Projected/geom:vertical_pixel_footprint">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Quaternion_Plus_Direction/geom:rotation_direction">
      <sch:assert test=". = ('Forward', 'From Base', 'Present to Reference', 'Reference to Present', 'Reverse', 'Toward Base')">
        The attribute geom:rotation_direction must be equal to one of the following values 'Forward', 'From Base', 'Present to Reference', 'Reference to Present', 'Reverse', 'Toward Base'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Reference_Pixel/geom:horizontal_coordinate_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Reference_Pixel/geom:vertical_coordinate_pixel">
      <sch:assert test="@unit = ('DN', 'electron/DN', 'pixel')">
        The attribute @unit must be equal to one of the following values 'DN', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/geom:kernel_provenance">
      <sch:assert test=". = ('Mixed', 'Predicted', 'Provenance Not Applicable', 'Reconstructed')">
        The attribute geom:kernel_provenance must be equal to one of the following values 'Mixed', 'Predicted', 'Provenance Not Applicable', 'Reconstructed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/pds:kernel_type">
      <sch:assert test=". = ('CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK')">
        The attribute pds:kernel_type must be equal to one of the following values 'CK', 'DBK', 'DSK', 'EK', 'FK', 'IK', 'LSK', 'MK', 'PCK', 'SCLK', 'SPK'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:maximum_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Min_Max/geom:minimum_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Specific/geom:subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:lat_long_method">
      <sch:assert test=". = ('Center', 'Mean', 'Median')">
        The attribute geom:lat_long_method must be equal to one of the following values 'Center', 'Mean', 'Median'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:start_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subsolar_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_azimuth">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_latitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Surface_Geometry_Start_Stop/geom:stop_subspacecraft_longitude">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Base/geom:x_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Base/geom:y_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Base/geom:z_acceleration">
      <sch:assert test="@unit = ('cm/s**2', 'km/s**2', 'm/s**2')">
        The attribute @unit must be equal to one of the following values 'cm/s**2', 'km/s**2', 'm/s**2'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Acceleration_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Base/geom:x_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Base/geom:y_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Base/geom:z_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Central_Body_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Earth_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Earth_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Earth_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_SSB_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_SSB_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_SSB_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Spacecraft_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Sun_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Sun_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Position_Sun_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Base/geom:x_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Base/geom:y_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Base/geom:z_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Earth/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_SSB/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Sun/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Earth/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_SSB/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Cartesian_Velocity_Target_Relative_To_Sun/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Base/geom:latitude_position">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Base/geom:longitude_position">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Base/geom:radius_position">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Central_Body_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Central_Body_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Position_Spacecraft_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Base/geom:latitude_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Base/geom:longitude_velocity">
      <sch:assert test="@unit = ('deg/day', 'deg/s', 'rad/s')">
        The attribute @unit must be equal to one of the following values 'deg/day', 'deg/s', 'rad/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Base/geom:radial_velocity">
      <sch:assert test="@unit = ('cm/s', 'km/s', 'm/s')">
        The attribute @unit must be equal to one of the following values 'cm/s', 'km/s', 'm/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Extended_Base/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Generic/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Spacecraft_Relative_To_Target/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Target_Relative_To_Central_Body/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:Vector_Planetocentric_Velocity_Target_Relative_To_Spacecraft/geom:light_time_correction_applied">
      <sch:assert test=". = ('None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb')">
        The attribute geom:light_time_correction_applied must be equal to one of the following values 'None', 'Received_Light_Time', 'Received_Light_Time_Stellar_Abb', 'Transmitted_Light_Time', 'Transmitted_Light_Time_Stellar_Abb'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="geom:SPICE_Kernel_Identification/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'geometry_to_SPICE_kernel'
            ">
        For Internal_Reference in SPICE_Kernel_Identification, reference_type must equal 'geometry_to_SPICE_kernel'
            </sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
