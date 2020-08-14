#!/bin/bash
#
# Hacky script to build validate, generate new context products, and push to git
#

set -x
rm -fr validate-*/
mvn clean package -DskipTests
tar -xvzf target/validate-*-bin.tar.gz
validate-*/bin/validate -u
cp validate-*/resources/registered_context_products.json src/main/resources/util/
git add src/main/resources/util/
git commit -m "Update context products for release"

git push origin master

set +x
