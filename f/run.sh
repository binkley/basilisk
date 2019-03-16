function run {
    $print $run docker run --publish 8080:8080/tcp -it $name
}

function -run-help {
    cat <<EOH
Runs Docker image.
EOH
}
