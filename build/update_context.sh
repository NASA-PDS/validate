#!/bin/bash
#
# Hacky script to build validate, generate new context products, and push to git
#

set -x
outdir=/tmp
rm -fr $outdir/validate-*
mvn clean package -DskipTests
tar -xvzf target/validate-*-bin.tar.gz -C $outdir
$outdir/validate-*/bin/validate -u
cp $outdir/validate-*/resources/registered_context_products.json src/main/resources/util/
set +x

