#!/bin/sh -eu

ALGORITHMS='
    ex-linear|org.mb.tedd.main.ExLinear
    big-table|org.mb.tedd.main.BigTable
'
TEST_SUITES="$(ls .. | grep testsuite- | cut -d '-' -f2)"
TEST_SUITE=''
ALGORITHM=''


clean_vars() {
    local vars="$1"

    echo "$vars" | sed -E 's/^\s*//; s/\s*$//' | sed '/^$/d' | cut -d '|' -f1
}

join_lines() {
    local lines="$(clean_vars  "$1")"
    local sep="$2"

    echo "$lines"  | awk -v d="$sep" '{s=(NR==1?s:s d)$0}END{print s}'
}

print_help() {
    cat <<EOT
Usage: $(basename "$0") [-ahp]

where:
    -a [--algorithm]    Set the algorithm to use to find test dependencies
                        (supported values: $(join_lines "$ALGORITHMS" ', '))
    -h [--help]         Print the help page.
    -t [--test-suite]   Set the test suite on which the algorithm should be run.
                        (supported values: $(join_lines "$TEST_SUITES" ', '))
EOT
}

validate_option() {
    local options="$(clean_vars "$1")"
    local option="$(clean_vars "$2")"

    if [ -z "$option" ]; then
        return 0
    fi

    for o in $options; do
        if [ "$o" = "$option" ]; then
            return 1
        fi
    done

    return 0
}

get_algorithm() {
    local algorithm="$(clean_vars "$1")"
    echo "$ALGORITHMS" | grep "$algorithm" | cut -d '|' -f2
}

set_application() {
    local app="$(clean_vars "$1")"
    local props="$(find src -name 'app.properties')"
    cd "../testsuite-$TEST_SUITE"
    mvn compile
    local app_cp="$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q)"
    cd -

    sed -i "s/project_name=.*/project_name=$app/g" $props
    sed -i "s/testsuite-[^\/]*/testsuite-$app/g" $props
    sed -i "s|project_classpath=.*|project_classpath=$app_cp|g" $props
}

while [ $# -gt 0 ]; do
    case $1 in
        -a|--algorithm)
            ALGORITHM="$2"
            shift
            shift
            ;;
        -h|--help)
            print_help
            exit 0
            ;;
        -t|--test-suite)
            TEST_SUITE="$2"
            shift
            shift
            ;;
        *)
            print_help
            exit 1
            ;;
    esac
done

if validate_option "$ALGORITHMS" "$ALGORITHM"; then
    print_help
    exit 1
fi

if validate_option "$TEST_SUITES" "$TEST_SUITE"; then
    print_help
    exit 1
fi


mvn compile

classpath="$(mvn dependency:build-classpath -Dmdep.outputFile=/dev/stdout -q):./target/classes"
set_application "$TEST_SUITE"

cat <<EOF
=====================================================================================
================================[ Execution ]========================================
=====================================================================================
EOF

container="$(cd "../testsuite-$TEST_SUITE"; ./run-docker.sh -p yes -n "$TEST_SUITE" | tail -1)"

sleep 2s

java -cp $classpath "$(get_algorithm "$ALGORITHM")" "$TEST_SUITE"

docker stop "$container"
docker rm "$container"
