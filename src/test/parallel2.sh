#!/bin/bash

NUM=$1
SRC_DIR=$2
VALIDATE_BIN=$3

ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $ROOT

time find ${SRC_DIR} -name "*.xml" | head -$NUM | parallel ${VALIDATE_BIN}/validate

