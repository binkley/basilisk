function run {
    $print $run docker-compose up
}

function -run-help {
    cat <<EOH
Runs Docker image.
EOH
}
