function clean {
    $print $run docker rm $name 2>/dev/null || true
}

function -clean-help {
    cat <<EOH
Removes Docker image.
EOH
}
