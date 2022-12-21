#!/bin/sh

# Update following environment variable to match with your environment
export PDS_BUNDLE_PATH= #<ADD PDS BUNDLE PATH>
export CHECKSUM_MANIFEST_FILE_NAME=urn-nasa-pds-insight_rad.md5
export VALIDATE_REPORT_PATH=/tmp/

export DEFAULT_ARGS="/tmp/pds -M /tmp/pds/"${CHECKSUM_MANIFEST_FILE_NAME}" -r /tmp/validate-report/validate-report.txt -R pds4.bundle"

docker container run --name validate \
                 --rm \
                 --volume "${PDS_BUNDLE_PATH}":/tmp/pds \
                 --volume "${PDS_BUNDLE_PATH}"/"${CHECKSUM_MANIFEST_FILE_NAME}":/tmp/pds/"${CHECKSUM_MANIFEST_FILE_NAME}" \
                 --volume "${VALIDATE_REPORT_PATH}":/tmp/validate-report/ \
                 --env DEFAULT_ARGS="${DEFAULT_ARGS}" \
                 nasapds/validate           
