#!/bin/bash

ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $ROOT

rm -rf temp
mkdir -p temp/insight_cameras/data/0001
cd temp

curl -O https://starbase.jpl.nasa.gov/pds4/1900/dph_examples_v1900.zip
unzip dph_examples_v1900.zip

scp -r ghollins@pds-dev-el7:/data/home/pds4/insight_cameras/data/0001/ ./insight_cameras/data/0001

cd ../../..

echo "----------------------------------"
pwd
echo "----------------------------------"

mvn clean
mvn package -DskipTests

cd target
tar zxf validate-*-bin.tar.gz

cd ${ROOT}

echo "RUNNING AGAINST A DIRECTORY WITH 80 FILES..."
time ../../target/validate-*/bin/validate  temp//insight_cameras/data/0001/0001/mipl/rdr/idc

#echo "RUNNING SINGLE XML FILE"
#time ../../target/validate-*/bin/validate ${ROOT}/C000M0010_597420115CNF_C0000_0461M3.xml

echo "RUNNING FULL CONTENT VALIDATION ON A ~4.8GB FILE."
echo "  (As of 2019-11-20:  should take 9:21 to complete)"
time ../../target/validate-*/bin/validate ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml

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

