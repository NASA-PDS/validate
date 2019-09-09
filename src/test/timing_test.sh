#!/bin/bash

ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $ROOT

rm -rf temp
mkdir temp
cd temp

curl -O https://starbase.jpl.nasa.gov/pds4/1900/dph_examples_v1900.zip

unzip dph_examples_v1900.zip

cd ../../../

mvn clean
mvn package -DskipTests

cd target
tar zxvf validate-*-bin.tar.gz

cd ${ROOT}

#
# SMALL
#
time ../../target/validate-*/bin/validate ./temp/V1900/dph_example_archive/data_tnmap/thermal_neutron_map.xml

#
# LARGE --no-data-check
#
time ../../target/validate-*/bin/validate --no-data-check ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml

#
# LARGE
#
time ../../target/validate-*/bin/validate ./temp/V1900/dph_example_archive/data_imagecube/virs/H01/virs_cube_64ppd_h01np.xml




