function run {
    trap 'docker-compose rm -f db 2>/dev/null' EXIT
    $print $run docker-compose up
}

function -run-help {
    cat <<EOH
Runs Docker image.
EOH
}