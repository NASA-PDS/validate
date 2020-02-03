#!/bin/bash

#
# Script that assists with splitting up PDS-4 products into multiple
# groups, so that they can be run in parallel by the validate program.
#
# Author  :  Galen Hollins
# Updated :  2020-02-02
#

usage()
{
    echo
    echo "USAGE:"
    echo
    echo "  validate-parallel [-d directory] [-n num-groups]"
    echo
    echo "  If no directory is specified, the current directory is used."
    echo
    echo "  If num-groups is specified, then the inputs are split up into"
    echo "  num-groups groups.  Otherwise the inputs are split up into the"
    echo "  same amount of groups as there are processing cores on this"
    echo "  system."
}

##### Main

PRODUCT_DIR=.
NUM_GROUPS=

while [ "$1" != "" ]; do
    case $1 in
        -d | --directory )      shift
                                PRODUCT_DIR=$1
                                ;;
        -n | --num-groups )     shift
                                NUM_GROUPS=$1
                                ;;
        -h | --help )           usage
                                exit
                                ;;
        * )                     usage
                                exit 1
    esac
    shift
done

#
# Check to make sure validate is on the path
#
VALIDATE_ON_PATH=`which validate | wc -l`
if [[ "${VALIDATE_ON_PATH}" -eq "1" ]]; then
  echo "  validate binary is available (`which validate`) [OK]"
else
  echo
  echo "  ERROR: No 'validate' found on your path."
  echo "         Please ensure this is on your environment PATH, before running this script."
  echo "         NOTE: this script requires that the name of the binary is 'validate'."
  echo
  exit 1
fi

#
# Check to make sure GNU parallel is on the path
#
PARALLEL_ON_PATH=`which parallel | wc -l`
if [[ "${PARALLEL_ON_PATH}" -eq "1" ]]; then
  echo "  GNU parallel binary is available (`which parallel`) [OK]"
else
  echo
  echo "  ERROR: No GNU parallel found on your path."
  echo "         Please ensure this is on your environment PATH, before running this script."
  echo "         See also:  https://www.gnu.org/software/parallel"
  echo
  exit 1
fi

NUM_CORES=`getconf _NPROCESSORS_ONLN`
if [[ "${NUM_GROUPS}" -eq "" ]]; then
  echo "  Splitting up into groups, based on number of cores this machine has..."
  NUM_GROUPS=`echo $NUM_CORES`
fi

echo "  Directory to process is : ${PRODUCT_DIR}"
echo "  Number of effective cores on this system: ${NUM_CORES}"

find  ${PRODUCT_DIR} -name "*.xml" > validate_all_files.txt

TOTAL_PRODUCTS=`wc -l < validate_all_files.txt`

if [[ "${TOTAL_PRODUCTS}" -eq "0" ]]; then
  echo "  No products found under directory (${PRODUCT_DIR}) to validate."
  exit 1
else
  echo "  Total number of products found to validate: $TOTAL_PRODUCTS"
fi

echo "  Cleaning up files from previous runs..."
rm -rf validate_set_*

FILES_PER_VALIDATE=$(((`wc -l < validate_all_files.txt`/${NUM_GROUPS})+1))
echo "  Splitting up inputs into ${NUM_GROUPS} groups..."
echo "    ($FILES_PER_VALIDATE files per group)"
echo "  +--------------------+----------------+"
echo "  |       Group        |   # in Group   |"
echo "  +--------------------+----------------+"
split -l${FILES_PER_VALIDATE} validate_all_files.txt validate_set_
for line in $(find . -name 'validate_set*'); do 
     echo "  | $line  |  " `cat $line | wc -l`
done
echo "  +--------------------+-----------------"

rm validate_all_files.txt

echo
echo "Running all $NUM_GROUPS groups in parallel now..."
echo
find . -name "validate_set_*" | parallel --max-args 1  echo validate -R pds4.label --target-manifest {}
echo
echo "Each validate command will output to an individual log."
time find . -name "validate_set_*" | parallel --max-args 1  "validate -R pds4.label --target-manifest {} > {}.log"
echo

for line in $(find . -name 'validate_set*.log'); do
     echo $line " :"
     echo "   " `grep "error(s)" $line`
     echo "   " `grep "warning(s)" $line`
     echo
done

