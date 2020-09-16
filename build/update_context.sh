#!/bin/bash
#
# Hacky script to build validate, generate new context products, and push to git
#

set -x
outdir=/tmp/validate
rm -fr $outdir
mvn clean package -DskipTests
tar -xvzf target/validate-*-bin.tar.gz $outdir
$outdir/bin/validate -u
cp $outdir/validate/resources/registered_context_products.json src/main/resources/util/
set +x

