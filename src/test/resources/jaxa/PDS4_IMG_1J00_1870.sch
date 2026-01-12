<?xml version="1.0" encoding="UTF-8"?>
  <!-- PDS4 Schematron for Name Space Id:img  Version:1.8.7.0 - Thu Oct 20 21:51:50 UTC 2022 -->
  <!-- Generated from the PDS4 Information Model Version 1.19.0.0 - System Build 13.0 -->
  <!-- *** This PDS4 schematron file is an operational deliverable. *** -->
<sch:schema xmlns:sch="http://purl.oclc.org/dsdl/schematron" queryBinding="xslt2">

  <sch:title>Schematron using XPath 2.0</sch:title>

  <sch:ns uri="http://www.w3.org/2001/XMLSchema-instance" prefix="xsi"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/pds/v1" prefix="pds"/>
  <sch:ns uri="http://pds.nasa.gov/pds4/img/v1" prefix="img"/>

		   <!-- ================================================ -->
		   <!-- NOTE:  There are two types of schematron rules.  -->
		   <!--        One type includes rules written for       -->
		   <!--        specific situations. The other type are   -->
		   <!--        generated to validate enumerated value    -->
		   <!--        lists. These two types of rules have been -->
		   <!--        merged together in the rules below.       -->
		   <!-- ================================================ -->
  <sch:pattern>
    <sch:rule context="//img:Color_Filter_Array">
      <sch:assert test="img:color_filter_array_state = ('Encoded', 'Decoded', 'No CFA')">
        <title>//img:Color_Filter_Array/Rule</title>
        IMG:error:img:color_filter_array_state_check: img:color_filter_array_state must be equal to one of the following values: 
        'Encoded', 'Decoded', 'No CFA'.
      </sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Color_Processing">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Color_Processing/Rule</title>
        IMG:error:color_processing_child_check: img:color must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Commanded_Parameters">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Commanded_Parameters/Rule</title>
        IMG:error:commanded_params_child_check: img:Commanded_Parameters must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Detector">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Detector/Rule</title>
        IMG:error:detector_params_child_check: img:Detector must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Downsampling">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Downsampling/Rule</title>
        IMG:error:downsampling_child_check: img:Downsampling must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Exposure">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Exposure/Rule</title>
        IMG:error:exposure_child_check: img:Exposure must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Focus">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Focus/Rule</title>
        IMG:error:focus_child_check: img:Focus must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Focus_Stack">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Focus_Stack/Rule</title>
        IMG:error:focus_stack_child_check: img:Focus_Stack must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Frame">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Frame/Rule</title>
        IMG:error:frame_child_check: img:Frame must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:ICER_Parameters">
      <sch:assert test="(count(img:Image_Compression_Segment) = img:segment_count) or (count(img:Image_Compression_Segment) = 0)">
        <title>//img:ICER_Parameters/Rule</title>
        IMG:error:icer_comprs_segment_check: img:ICER_Parameters/img:segment_count must match the number of img:Image_Compression_Segment classes.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Imaging">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Imaging/Rule</title>
        IMG:error:imaging_child_check: img:Imaging class must contain at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Imaging/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'imaging_parameters_to_image_object'">
        <title>//img:Imaging/pds:Local_Internal_Reference/Rule</title>
        In img:Imaging, Local_Internal_Reference.local_reference_type must be equal to 'imaging_parameters_to_image_object'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Instrument_State">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Instrument_State/Rule</title>
        IMG:error:inst_state_child_check: img:Instrument_State must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:JPEG_Parameters">
      <sch:assert test="count(child::*) > 0">
        <title>//img:JPEG_Parameters/Rule</title>
        IMG:error:jpeg_params_child_check: img:JPEG_Parameters must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:LOCO_Parameters">
      <sch:assert test="count(child::*) > 0">
        <title>//img:LOCO_Parameters/Rule</title>
        IMG:error:loco_params_child_check: img:LOCO_Parameters must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Onboard_Compression">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Onboard_Compression/Rule</title>
        IMG:error:onboard_compression_child_check: img:Onboard_Compression must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Optical_Filter">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Optical_Filter/Rule</title>
        IMG:error:optical_filter_child_check: img:Optical_Filter must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Optical_Filter/pds:Local_Internal_Reference">
      <sch:assert test="pds:local_reference_type = 'data_to_optical_filter'">
        <title>//img:Optical_Filter/pds:Local_Internal_Reference/Rule</title>
        In img:Optical_Filter, Local_Internal_Reference.local_reference_type must be equal to 'data_to_optical_filter'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Sampling">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Sampling/Rule</title>
        IMG:error:sampling_child_check: img:Sampling must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Shutter_Subtraction">
      <sch:assert test="if (exists(img:shutter_subtraction_mode)) then (img:shutter_subtraction_mode != 'Conditional') or (img:shutter_subtraction_mode = 'Conditional' and exists(img:exposure_duration_threshold_count)) else true()">
        <title>//img:Shutter_Subtraction/Rule</title>
        IMG:error:shutter_subtraction_check: if img:shutter_subtraction_mode = 'Conditional', then img:exposure_duration_threshold_count must exist.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:StarPixel_Flexible_Parameters">
      <sch:assert test="count(child::*) > 0">
        <title>//img:StarPixel_Flexible_Parameters/Rule</title>
        IMG:error:starpixel_flexible_params_child_check: img:StarPixel_Flexible_Parameters must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:StarPixel_Lossless_Parameters">
      <sch:assert test="count(child::*) > 0">
        <title>//img:StarPixel_Lossless_Parameters/Rule</title>
        IMG:error:starpixel_lossless_params_child_check: img:StarPixel_Lossless_Parameters must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Subframe">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Subframe/Rule</title>
        IMG:error:subframe_child_check: img:Subframe must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="//img:Video">
      <sch:assert test="count(child::*) > 0">
        <title>//img:Video/Rule</title>
        IMG:error:video_child_check: img:Video must have at least 1 attribute or class specified.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Brightness_Correction_Image/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'data_to_brightness_correction'">
        <title>img:Brightness_Correction_Image/pds:Internal_Reference/Rule</title>
        In img:Brightness_Correction_Image, Internal_Reference.reference_type must be equal to 'data_to_brightness_correction'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Color_Filter_Array">
      <sch:assert test="if (img:active_flag) then img:active_flag = ('true', 'false') else true()">
        <title>img:Color_Filter_Array/img:active_flag</title>
        The attribute img:active_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Color_Filter_Array/img:color_filter_array_state">
      <sch:assert test=". = ('Decoded', 'Encoded', 'No CFA')">
        <title>img:Color_Filter_Array/img:color_filter_array_state/img:color_filter_array_state</title>
        The attribute img:Color_Filter_Array/img:color_filter_array_state must be equal to one of the following values 'Decoded', 'Encoded', 'No CFA'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Color_Filter_Array/img:color_filter_array_type">
      <sch:assert test=". = ('Bayer RGGB', 'None')">
        <title>img:Color_Filter_Array/img:color_filter_array_type/img:color_filter_array_type</title>
        The attribute img:Color_Filter_Array/img:color_filter_array_type must be equal to one of the following values 'Bayer RGGB', 'None'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Companding">
      <sch:assert test="if (img:active_flag) then img:active_flag = ('true', 'false') else true()">
        <title>img:Companding/img:active_flag</title>
        The attribute img:active_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (img:early_scaling) then img:early_scaling = ('true', 'false') else true()">
        <title>img:Companding/img:early_scaling</title>
        The attribute img:early_scaling must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Companding/img:companding_state">
      <sch:assert test=". = ('Companded', 'Expanded', 'None')">
        <title>img:Companding/img:companding_state/img:companding_state</title>
        The attribute img:Companding/img:companding_state must be equal to one of the following values 'Companded', 'Expanded', 'None'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Detector">
      <sch:assert test="if (img:bad_pixel_replacement_flag) then img:bad_pixel_replacement_flag = ('true', 'false') else true()">
        <title>img:Detector/img:bad_pixel_replacement_flag</title>
        The attribute img:bad_pixel_replacement_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
      <sch:assert test="if (img:early_image_return) then img:early_image_return = ('true', 'false') else true()">
        <title>img:Detector/img:early_image_return</title>
        The attribute img:early_image_return must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Detector/img:detector_to_image_flip">
      <sch:assert test=". = ('Horizontal', 'None', 'Vertical')">
        <title>img:Detector/img:detector_to_image_flip/img:detector_to_image_flip</title>
        The attribute img:Detector/img:detector_to_image_flip must be equal to one of the following values 'Horizontal', 'None', 'Vertical'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Detector/img:detector_to_image_rotation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>img:Detector/img:detector_to_image_rotation/img:detector_to_image_rotation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Detector/img:instrument_idle_timeout">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>img:Detector/img:instrument_idle_timeout/img:instrument_idle_timeout</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Detector/img:readout_rate">
      <sch:assert test="@unit = ('GHz', 'Hz', 'MHz', 'THz', 'kHz', 'mHz')">
        <title>img:Detector/img:readout_rate/img:readout_rate</title>
        The attribute @unit must be equal to one of the following values 'GHz', 'Hz', 'MHz', 'THz', 'kHz', 'mHz'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Detector/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'data_to_raw_source_product'">
        <title>img:Detector/pds:Internal_Reference/Rule</title>
        In img:Detector, Internal_Reference.reference_type must be equal to 'data_to_raw_source_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Device_Current/img:current_value">
      <sch:assert test="@unit = ('A', 'mA')">
        <title>img:Device_Current/img:current_value/img:current_value</title>
        The attribute @unit must be equal to one of the following values 'A', 'mA'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Device_Temperature/img:temperature_value">
      <sch:assert test="@unit = ('K', 'degC')">
        <title>img:Device_Temperature/img:temperature_value/img:temperature_value</title>
        The attribute @unit must be equal to one of the following values 'K', 'degC'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Device_Voltage/img:voltage_value">
      <sch:assert test="@unit = ('V', 'mV')">
        <title>img:Device_Voltage/img:voltage_value/img:voltage_value</title>
        The attribute @unit must be equal to one of the following values 'V', 'mV'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Exposure/img:exposure_duration">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>img:Exposure/img:exposure_duration/img:exposure_duration</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Exposure/img:exposure_type">
      <sch:assert test=". = ('Auto', 'Auto Last', 'Manual', 'Manual Last', 'None', 'Test')">
        <title>img:Exposure/img:exposure_type/img:exposure_type</title>
        The attribute img:Exposure/img:exposure_type must be equal to one of the following values 'Auto', 'Auto Last', 'Manual', 'Manual Last', 'None', 'Test'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Focus/img:best_focus_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Focus/img:best_focus_distance/img:best_focus_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Focus/img:focus_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Focus/img:focus_distance/img:focus_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Focus/img:maximum_focus_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Focus/img:maximum_focus_distance/img:maximum_focus_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Focus/img:minimum_focus_distance">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Focus/img:minimum_focus_distance/img:minimum_focus_distance</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Frame">
      <sch:assert test="if (img:product_flag) then img:product_flag = ('true', 'false') else true()">
        <title>img:Frame/img:product_flag</title>
        The attribute img:product_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Frame/img:frame_id">
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Frame/img:frame_type_name">
      <sch:assert test=". = ('Mono', 'Stereo')">
        <title>img:Frame/img:frame_type_name/img:frame_type_name</title>
        The attribute img:Frame/img:frame_type_name must be equal to one of the following values 'Mono', 'Stereo'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:High_Dynamic_Range">
      <sch:assert test="if (img:active_flag) then img:active_flag = ('true', 'false') else true()">
        <title>img:High_Dynamic_Range/img:active_flag</title>
        The attribute img:active_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:High_Dynamic_Range/img:hdr_acquisition_mode">
      <sch:assert test=". = ('Multiframe', 'None', 'Piecewise', 'Single')">
        <title>img:High_Dynamic_Range/img:hdr_acquisition_mode/img:hdr_acquisition_mode</title>
        The attribute img:High_Dynamic_Range/img:hdr_acquisition_mode must be equal to one of the following values 'Multiframe', 'None', 'Piecewise', 'Single'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:High_Dynamic_Range_Exposure/img:exposure_duration">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>img:High_Dynamic_Range_Exposure/img:exposure_duration/img:exposure_duration</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:High_Dynamic_Range_Exposure/img:exposure_time_delta">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>img:High_Dynamic_Range_Exposure/img:exposure_time_delta/img:exposure_time_delta</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:High_Dynamic_Range_Exposure/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'data_to_raw_source_product'">
        <title>img:High_Dynamic_Range_Exposure/pds:Internal_Reference/Rule</title>
        In img:High_Dynamic_Range_Exposure, Internal_Reference.reference_type must be equal to 'data_to_raw_source_product'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Image_Mask/img:horizon_mask_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>img:Image_Mask/img:horizon_mask_elevation/img:horizon_mask_elevation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Image_Mask_File/img:horizon_mask_elevation">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>img:Image_Mask_File/img:horizon_mask_elevation/img:horizon_mask_elevation</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Image_Mask_File/img:mask_type">
      <sch:assert test=". = ('description', 'image')">
        <title>img:Image_Mask_File/img:mask_type/img:mask_type</title>
        The attribute img:Image_Mask_File/img:mask_type must be equal to one of the following values 'description', 'image'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:JPEG_Parameters/img:color_subsampling_mode">
      <sch:assert test=". = ('4:2:2', '4:4:4', 'Grayscale')">
        <title>img:JPEG_Parameters/img:color_subsampling_mode/img:color_subsampling_mode</title>
        The attribute img:JPEG_Parameters/img:color_subsampling_mode must be equal to one of the following values '4:2:2', '4:4:4', 'Grayscale'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:JPEG_Progressive_Parameters/img:color_subsampling_mode">
      <sch:assert test=". = ('4:2:2', '4:4:4', 'Grayscale')">
        <title>img:JPEG_Progressive_Parameters/img:color_subsampling_mode/img:color_subsampling_mode</title>
        The attribute img:JPEG_Progressive_Parameters/img:color_subsampling_mode must be equal to one of the following values '4:2:2', '4:4:4', 'Grayscale'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:LED_Illumination_Source/img:illumination_state">
      <sch:assert test=". = ('Off', 'On')">
        <title>img:LED_Illumination_Source/img:illumination_state/img:illumination_state</title>
        The attribute img:LED_Illumination_Source/img:illumination_state must be equal to one of the following values 'Off', 'On'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:LED_Illumination_Source/img:illumination_wavelength">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:LED_Illumination_Source/img:illumination_wavelength/img:illumination_wavelength</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Nonlinear_Pixel/img:threshold_value">
      <sch:assert test="@unit = ('W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'W/m**2/sr/Hz', 'W/m**2/sr/nm', 'W/m**2/sr/μm', 'W/m**3/sr', 'uW*cm**-2*sr**-1*um**-1', 'μW/cm**2/sr/μm')">
        <title>img:Nonlinear_Pixel/img:threshold_value/img:threshold_value</title>
        The attribute @unit must be equal to one of the following values 'W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'W/m**2/sr/Hz', 'W/m**2/sr/nm', 'W/m**2/sr/μm', 'W/m**3/sr', 'uW*cm**-2*sr**-1*um**-1', 'μW/cm**2/sr/μm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Nonlinear_Pixel/img:threshold_value" role="warning">
      <sch:assert test="@unit != 'W*m**-2*sr**-1*Hz**-1'">
        <title>img:Nonlinear_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-2*sr**-1*Hz**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'W*m**-2*sr**-1*nm**-1'">
        <title>img:Nonlinear_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-2*sr**-1*nm**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'W*m**-2*sr**-1*um**-1'">
        <title>img:Nonlinear_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-2*sr**-1*um**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'W*m**-3*sr**-1'">
        <title>img:Nonlinear_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-3*sr**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'uW*cm**-2*sr**-1*um**-1'">
        <title>img:Nonlinear_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value uW*cm**-2*sr**-1*um**-1 is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Onboard_Compression">
      <sch:assert test="if (img:deferred_flag) then img:deferred_flag = ('true', 'false') else true()">
        <title>img:Onboard_Compression/img:deferred_flag</title>
        The attribute img:deferred_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Onboard_Compression/img:onboard_compression_class">
      <sch:assert test=". = ('Lossless', 'Lossy', 'Uncompressed')">
        <title>img:Onboard_Compression/img:onboard_compression_class/img:onboard_compression_class</title>
        The attribute img:Onboard_Compression/img:onboard_compression_class must be equal to one of the following values 'Lossless', 'Lossy', 'Uncompressed'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Onboard_Compression/img:onboard_compression_type">
      <sch:assert test=". = ('GZIP', 'H.264 Frame', 'ICER', 'ICT', 'JPEG', 'JPEG Progressive', 'LOCO', 'LZO', 'Lossless', 'MSSS Lossless', 'None', 'StarPixel Flexible', 'StarPixel Lossless')">
        <title>img:Onboard_Compression/img:onboard_compression_type/img:onboard_compression_type</title>
        The attribute img:Onboard_Compression/img:onboard_compression_type must be equal to one of the following values 'GZIP', 'H.264 Frame', 'ICER', 'ICT', 'JPEG', 'JPEG Progressive', 'LOCO', 'LZO', 'Lossless', 'MSSS Lossless', 'None', 'StarPixel Flexible', 'StarPixel Lossless'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Onboard_Compression/img:onboard_compression_venue">
      <sch:assert test=". = ('Hardware', 'Software')">
        <title>img:Onboard_Compression/img:onboard_compression_venue/img:onboard_compression_venue</title>
        The attribute img:Onboard_Compression/img:onboard_compression_venue must be equal to one of the following values 'Hardware', 'Software'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Optical_Filter/img:bandwidth">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Optical_Filter/img:bandwidth/img:bandwidth</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Optical_Filter/img:center_filter_wavelength">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Optical_Filter/img:center_filter_wavelength/img:center_filter_wavelength</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Optical_Properties/img:f_number">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Optical_Properties/img:f_number/img:f_number</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Optical_Properties/img:focal_length">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Optical_Properties/img:focal_length/img:focal_length</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Pixel_Averaging_Dimensions/img:height_pixels">
      <sch:assert test="@unit = ('DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel')">
        <title>img:Pixel_Averaging_Dimensions/img:height_pixels/img:height_pixels</title>
        The attribute @unit must be equal to one of the following values 'DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Pixel_Averaging_Dimensions/img:width_pixels">
      <sch:assert test="@unit = ('DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel')">
        <title>img:Pixel_Averaging_Dimensions/img:width_pixels/img:width_pixels</title>
        The attribute @unit must be equal to one of the following values 'DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Radial_Flat_Field_Function/img:x_center">
      <sch:assert test="@unit = ('DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel')">
        <title>img:Radial_Flat_Field_Function/img:x_center/img:x_center</title>
        The attribute @unit must be equal to one of the following values 'DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Radial_Flat_Field_Function/img:y_center">
      <sch:assert test="@unit = ('DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel')">
        <title>img:Radial_Flat_Field_Function/img:y_center/img:y_center</title>
        The attribute @unit must be equal to one of the following values 'DN', 'W/m**2/sr/nm/(DN/s)', 'electron/DN', 'pixel'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Radiometric_Correction">
      <sch:assert test="if (img:active_flag) then img:active_flag = ('true', 'false') else true()">
        <title>img:Radiometric_Correction/img:active_flag</title>
        The attribute img:active_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Radiometric_Correction/img:effective_wavelength">
      <sch:assert test="@unit = ('AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm')">
        <title>img:Radiometric_Correction/img:effective_wavelength/img:effective_wavelength</title>
        The attribute @unit must be equal to one of the following values 'AU', 'Angstrom', 'cm', 'km', 'm', 'micrometer', 'mm', 'nm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Radiometric_Correction/img:radiometric_type">
      <sch:assert test=". = ('No CFA', 'Radiance Factor', 'Scaled Spectral Radiance', 'Spectral Radiance')">
        <title>img:Radiometric_Correction/img:radiometric_type/img:radiometric_type</title>
        The attribute img:Radiometric_Correction/img:radiometric_type must be equal to one of the following values 'No CFA', 'Radiance Factor', 'Scaled Spectral Radiance', 'Spectral Radiance'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Saturated_Pixel/img:threshold_value">
      <sch:assert test="@unit = ('W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'W/m**2/sr/Hz', 'W/m**2/sr/nm', 'W/m**2/sr/μm', 'W/m**3/sr', 'uW*cm**-2*sr**-1*um**-1', 'μW/cm**2/sr/μm')">
        <title>img:Saturated_Pixel/img:threshold_value/img:threshold_value</title>
        The attribute @unit must be equal to one of the following values 'W*m**-2*sr**-1*Hz**-1', 'W*m**-2*sr**-1*nm**-1', 'W*m**-2*sr**-1*um**-1', 'W*m**-3*sr**-1', 'W/m**2/sr/Hz', 'W/m**2/sr/nm', 'W/m**2/sr/μm', 'W/m**3/sr', 'uW*cm**-2*sr**-1*um**-1', 'μW/cm**2/sr/μm'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Saturated_Pixel/img:threshold_value" role="warning">
      <sch:assert test="@unit != 'W*m**-2*sr**-1*Hz**-1'">
        <title>img:Saturated_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-2*sr**-1*Hz**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'W*m**-2*sr**-1*nm**-1'">
        <title>img:Saturated_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-2*sr**-1*nm**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'W*m**-2*sr**-1*um**-1'">
        <title>img:Saturated_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-2*sr**-1*um**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'W*m**-3*sr**-1'">
        <title>img:Saturated_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value W*m**-3*sr**-1 is deprecated and should not be used.</sch:assert>
      <sch:assert test="@unit != 'uW*cm**-2*sr**-1*um**-1'">
        <title>img:Saturated_Pixel/img:threshold_value role="warning"/img:threshold_value</title>
        The unit value uW*cm**-2*sr**-1*um**-1 is deprecated and should not be used.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Shutter_Subtraction">
      <sch:assert test="if (img:active_flag) then img:active_flag = ('true', 'false') else true()">
        <title>img:Shutter_Subtraction/img:active_flag</title>
        The attribute img:active_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Shutter_Subtraction/img:shutter_subtraction_mode">
      <sch:assert test=". = ('Always', 'Conditional', 'None', 'True')">
        <title>img:Shutter_Subtraction/img:shutter_subtraction_mode/img:shutter_subtraction_mode</title>
        The attribute img:Shutter_Subtraction/img:shutter_subtraction_mode must be equal to one of the following values 'Always', 'Conditional', 'None', 'True'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Subframe/img:line_fov">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>img:Subframe/img:line_fov/img:line_fov</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Subframe/img:sample_fov">
      <sch:assert test="@unit = ('arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad')">
        <title>img:Subframe/img:sample_fov/img:sample_fov</title>
        The attribute @unit must be equal to one of the following values 'arcmin', 'arcsec', 'deg', 'hr', 'mrad', 'rad'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Subframe/img:subframe_type">
      <sch:assert test=". = ('Hardware Compatible', 'Hardware Else Software', 'None', 'Software Only', 'Subframe Around Sun', 'Sun Subframe Or Full')">
        <title>img:Subframe/img:subframe_type/img:subframe_type</title>
        The attribute img:Subframe/img:subframe_type must be equal to one of the following values 'Hardware Compatible', 'Hardware Else Software', 'None', 'Software Only', 'Subframe Around Sun', 'Sun Subframe Or Full'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Thumbnail">
      <sch:assert test="if (img:product_flag) then img:product_flag = ('true', 'false') else true()">
        <title>img:Thumbnail/img:product_flag</title>
        The attribute img:product_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Thumbnail/img:frame_id">
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Thumbnail/img:frame_type_name">
      <sch:assert test=". = ('Mono', 'Stereo')">
        <title>img:Thumbnail/img:frame_type_name/img:frame_type_name</title>
        The attribute img:Thumbnail/img:frame_type_name must be equal to one of the following values 'Mono', 'Stereo'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Tile/img:tile_upsample_method">
      <sch:assert test=". = ('Bilinear', 'None', 'Replication')">
        <title>img:Tile/img:tile_upsample_method/img:tile_upsample_method</title>
        The attribute img:Tile/img:tile_upsample_method must be equal to one of the following values 'Bilinear', 'None', 'Replication'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Tile/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'data_to_tile_source_data'">
        <title>img:Tile/pds:Internal_Reference/Rule</title>
        In img:Tile, Internal_Reference.reference_type must be equal to 'data_to_tile_source_data'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Tiling/img:tile_type">
      <sch:assert test=". = ('Irregular', 'Regular')">
        <title>img:Tiling/img:tile_type/img:tile_type</title>
        The attribute img:Tiling/img:tile_type must be equal to one of the following values 'Irregular', 'Regular'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Tiling/img:tile_venue">
      <sch:assert test=". = ('Ground', 'Onboard')">
        <title>img:Tiling/img:tile_venue/img:tile_venue</title>
        The attribute img:Tiling/img:tile_venue must be equal to one of the following values 'Ground', 'Onboard'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Video">
      <sch:assert test="if (img:video_flag) then img:video_flag = ('true', 'false') else true()">
        <title>img:Video/img:video_flag</title>
        The attribute img:video_flag must be equal to one of the following values 'true', 'false'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Video/img:frame_interval">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>img:Video/img:frame_interval/TBD_attrNameSpaceNC:TBD_AttrTitle</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Video/img:frame_rate">
      <sch:assert test="@unit = ('frames/s')">
        <title>img:Video/img:frame_rate/img:frame_rate</title>
        The attribute @unit must be equal to one of the following values 'frames/s'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Video/img:interframe_delay">
      <sch:assert test="@unit = ('day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr')">
        <title>img:Video/img:interframe_delay/TBD_attrNameSpaceNC:TBD_AttrTitle</title>
        The attribute @unit must be equal to one of the following values 'day', 'hr', 'julian day', 'microseconds', 'min', 'ms', 'ns', 's', 'yr'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Video/pds:External_Reference">
      <sch:assert test="pds:reference_type = 'video_frame_to_video'">
        <title>img:Video/pds:External_Reference/Rule</title>
        In img:Video, External_Reference.reference_type must be equal to 'video_frame_to_video'.</sch:assert>
    </sch:rule>
  </sch:pattern>
  <sch:pattern>
    <sch:rule context="img:Video/pds:Internal_Reference">
      <sch:assert test="pds:reference_type = 'video_frame_to_video'">
        <title>img:Video/pds:Internal_Reference/Rule</title>
        In img:Video, Internal_Reference.reference_type must be equal to 'video_frame_to_video'.</sch:assert>
    </sch:rule>
  </sch:pattern>
</sch:schema>
