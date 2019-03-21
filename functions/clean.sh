function clean {
    $print $run docker-compose rm -f basilisk-db basilisk-service \
        2>/dev/null || true
}

function -clean-help {
    cat <<EOH
Removes Docker images for program.
EOH
}
