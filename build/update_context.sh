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

set +x
git config --local user.email "$1"
git config --local user.name "$2"
git config --local github.token $3

set -x
git add src/main/resources/util/
git commit -m "Update config with latest context product metadata"

git push origin master
set +x
