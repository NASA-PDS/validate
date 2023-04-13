KPL/MK

   This meta-kernel lists the M2020 SPICE kernels providing coverage
   for the whole mission. All the kernels listed below are archived
   in the PDS M2020 SPICE kernel archive. This set of files and the
   order in which they are listed were picked to provide the best
   available data and the most complete coverage based on the information
   about the kernels available at the time this meta-kernel was made.
   For detailed information about the kernels listed below refer to the
   internal comments included in the kernels and the documentation
   accompanying the M2020 SPICE kernel archive.

   It is recommended that users make a local copy of this file and
   modify the value of the PATH_VALUES keyword to point to the actual
   location of the M2020 SPICE kernel archives' ``spice_kernels''
   directory on their system. Replacing ``/'' with ``\'' and converting
   line terminators to the format native to the user's system may also
   be required if this meta-kernel is to be used on a non-UNIX
   workstation.

   This file was created on March 11, 2022 by Marc Costa Sitja, NAIF/JPL.
   The original name of this file was m2020_v03.tm.

   \begindata

      PATH_VALUES     = ( '..'      )

      PATH_SYMBOLS    = ( 'KERNELS' )

      KERNELS_TO_LOAD = (

                          '$KERNELS/lsk/naif0012.tls'

                          '$KERNELS/pck/pck00010.tpc'

                          '$KERNELS/fk/m2020_v04.tf'

                          '$KERNELS/ik/m2020_struct_v00.ti'

                          '$KERNELS/sclk/m2020_lmst_ops210303_v1.tsc'

                          '$KERNELS/sclk/m2020_168_sclkscet_refit_v03.tsc'

                          '$KERNELS/spk/de438s.bsp'
                          '$KERNELS/spk/mar097.bsp'

                          '$KERNELS/spk/m2020_struct_v01.bsp'

                          '$KERNELS/spk/m2020_cruise_od138_v1.bsp'

                          '$KERNELS/spk/m2020_edl_v01.bsp'

                          '$KERNELS/spk/m2020_ls_ops210303_iau2000_v1.bsp'

                          '$KERNELS/spk/m2020_surf_rover_loc_0000_0089_v1.bsp'
                          '$KERNELS/spk/m2020_surf_rover_loc_0089_0179_v1.bsp'
                          '$KERNELS/spk/m2020_surf_rover_loc_0179_0299_v1.bsp'

                          '$KERNELS/ck/m2020_turret_tools_v1.bc'

                          '$KERNELS/ck/m2020_cruise_recon_rawrt_v1.bc'
                          '$KERNELS/ck/m2020_cruise_recon_raweng_v1.bc'

                          '$KERNELS/ck/m2020_edl_v01.bc'

                          '$KERNELS/ck/m2020_surf_ra_tlmenc_0000_0089_v1.bc'
                          '$KERNELS/ck/m2020_surf_ra_tlmenc_0089_0179_v1.bc'
                          '$KERNELS/ck/m2020_surf_ra_tlmenc_0179_0299_v1.bc'

                          '$KERNELS/ck/m2020_surf_ra_tlmres_0000_0089_v1.bc'
                          '$KERNELS/ck/m2020_surf_ra_tlmres_0089_0179_v1.bc'
                          '$KERNELS/ck/m2020_surf_ra_tlmres_0179_0299_v1.bc'

                          '$KERNELS/ck/m2020_surf_rsm_tlmenc_0000_0089_v1.bc'
                          '$KERNELS/ck/m2020_surf_rsm_tlmenc_0089_0179_v1.bc'
                          '$KERNELS/ck/m2020_surf_rsm_tlmenc_0179_0299_v1.bc'

                          '$KERNELS/ck/m2020_surf_rsm_tlmres_0000_0089_v1.bc'
                          '$KERNELS/ck/m2020_surf_rsm_tlmres_0089_0179_v1.bc'
                          '$KERNELS/ck/m2020_surf_rsm_tlmres_0179_0299_v1.bc'

                          '$KERNELS/ck/m2020_surf_rover_tlm_0000_0089_v1.bc'
                          '$KERNELS/ck/m2020_surf_rover_tlm_0089_0179_v1.bc'
                          '$KERNELS/ck/m2020_surf_rover_tlm_0179_0299_v1.bc'

                        )

   \begintext

End of MK file.
