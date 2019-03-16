#!/bin/bash

set -e
set -o pipefail
set -u

name=basilisk-service
version=0

run=''
case $(uname) in
    CYGWIN* | MINGW* ) run=winpty ;;
esac

$run docker rm $name 2>/dev/null || true
$run docker build --compress --tag $name . \
    --build-arg APP_JAR=basilisk-service/build/libs/$name-$version.jar \
    --build-arg GRADLE_VERSION=5.2.1 \
    --build-arg JAVA_VERSION=11.0.2
$run docker run --publish 8080:8080/tcp -it $name
