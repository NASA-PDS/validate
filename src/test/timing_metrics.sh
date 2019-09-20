#!/bin/bash

ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $ROOT

rm -rf temp
mkdir temp
cd temp

curl -O https://starbase.jpl.nasa.gov/pds4/1900/dph_examples_v1900.zip

unzip dph_examples_v1900.zip

cd ../../..

mvn clean
mvn package -DskipTests

cd target
tar zxf validate-*-bin.tar.gz

cd ${ROOT}

echo "RUNNING FULL CONTENT VALIDATION ON A ~4.8GB FILE."
echo "  (As of 2019-09-20:  should take 9-10 minutes to complete)"
time ../../target/validate-*/bin/validate ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml
exit 0
#
# SMALL
#
time ../../target/validate-*/bin/validate ./temp/V1900/dph_example_archive/data_tnmap/thermal_neutron_map.xml
exit 0
#
# LARGE --no-data-check
#
time ../../target/validate-*/bin/validate --no-data-check ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml

time ../../target/validate-*/bin/validate --spot-check-data 10000 ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml

time ../../target/validate-*/bin/validate --spot-check-data 1000 ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml
time ../../target/validate-*/bin/validate --spot-check-data 500 ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml
time ../../target/validate-*/bin/validate --spot-check-data 250 ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml
time ../../target/validate-*/bin/validate --spot-check-data 125 ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml

#
# LARGE (with data check)
#
time ../../target/validate-*/bin/validate ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml
exit 0
#
# Cleanup temp dir
#
rm -rf ./temp

