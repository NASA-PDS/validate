#!/bin/bash
#
# Hacky script to build validate, generate new context products, and push to git
#

usage() {
    echo "$(basename $0) <new-pds4-jparser-version>"
    echo
    exit 1
}

if [ $# -ne 1 ]; then
    usage
fi

version=$1

set -x
rm -fr validate-*/

mvn versions:set-property -Dproperty=pds4-jparser.version -DnewVersion=$version
git add pom.xml
git commit -m "Upgrade pds4-jparser"

git push origin master

set +x
