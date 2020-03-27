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
mvn clean package -DskipTests
tar -xvzf target/validate-*-bin.tar.gz
validate-*/bin/validate -u
cp validate-*/resources/registered_context_products.json src/main/resources/util/
git add src/main/resources/util/
git commit -m "Update context products for release"

mvn versions:set-property -Dproperty=pds4-jparser.version -DnewVersion=$version

set +x