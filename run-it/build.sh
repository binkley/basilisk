function build {
    $print $run docker build --compress --tag $name . \
        --build-arg APP_JAR=basilisk-service/build/libs/$name-$version.jar \
        --build-arg GRADLE_VERSION=5.2.1 \
        --build-arg JAVA_VERSION=11.0.2
}

function -build-help {
    cat <<EOH
Builds program and Docker image.
EOH
}
