#!/bin/sh

# Copyright 2022, California Institute of Technology ("Caltech").
# U.S. Government sponsorship acknowledged.
#
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are met:
#
# * Redistributions of source code must retain the above copyright notice,
# this list of conditions and the following disclaimer.
# * Redistributions must reproduce the above copyright notice, this list of
# conditions and the following disclaimer in the documentation and/or other
# materials provided with the distribution.
# * Neither the name of Caltech nor its operating division, the Jet Propulsion
# Laboratory, nor the names of its contributors may be used to endorse or
# promote products derived from this software without specific prior written
# permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
# AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
# IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
# ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
# LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
# CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
# SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
# INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
# CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
# ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
# POSSIBILITY OF SUCH DAMAGE.

# This is an example shell script called run.sh provided in the docker directory of Validate repository to make it
# easier to execute the dockerized Validate Tool validate a PDS bundle with default arguments. It is possible to make copies
# of this run.sh file, modify the default arguments and execute those scripts as a simplified shell script based option.

# Update following environment variable to match with your environment

# PDS bundle directory location in the host machine, which is used to execute the dockerized Validate Tool
PDS_BUNDLE_PATH=${HOME}/urn-nasa-pds-insight_rad

# Checksum file name provided for the PDS bundle
CHECKSUM_MANIFEST_FILE_NAME=urn-nasa-pds-insight_rad.md5

# Validate report directory path
VALIDATE_REPORT_PATH=${HOME}

# Validate report file name
VALIDATE_REPORT_FILE_NAME=validate-report.txt

# Default arguments provided as an array. This can be updated based on the requirements.
DEFAULT_ARGS=(/tmp/pds -M /tmp/pds/${CHECKSUM_MANIFEST_FILE_NAME} -r /tmp/validate-report/${VALIDATE_REPORT_FILE_NAME} -R pds4.bundle)

echo "Executing: validate ${DEFAULT_ARGS[*]}"

# Execute validate docker container with default arguments
docker container run --name validate \
                 --rm \
                 --volume "${PDS_BUNDLE_PATH}":/tmp/pds \
                 --volume "${PDS_BUNDLE_PATH}"/"${CHECKSUM_MANIFEST_FILE_NAME}":/tmp/pds/"${CHECKSUM_MANIFEST_FILE_NAME}" \
                 --volume "${VALIDATE_REPORT_PATH}":/tmp/validate-report \
                 nasapds/validate "${DEFAULT_ARGS[@]}"

echo "The validate report is available at: ${VALIDATE_REPORT_PATH}/${VALIDATE_REPORT_FILE_NAME}"
