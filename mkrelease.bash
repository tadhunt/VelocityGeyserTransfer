#!/bin/bash -x

set -o nounset
set -o errexit

rm -rf tmp
rm -rf release
mkdir tmp
mkdir -p release

./gradlew build

cd tmp
unzip ../build/libs/VelocityGeyserTransfer-0.0.1.jar
cp ../velocity-plugin.json .
zip -r ../release/VelocityGeyserTransfer-0.0.1.jar *
