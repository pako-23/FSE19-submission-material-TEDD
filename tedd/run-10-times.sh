#!/bin/bash
declare -a algorithms=("ex-linear" "big-table" "pradet")
declare -a testsuites=("addressbook" "claroline" "mantisbt" "mrb" "ppma")

for algo in "${algorithms[@]}"; do
    for ts in "${testsuites[@]}"; do
        for i in {1..10}; do
            TIMESTAMP=$(date "+%Y%m%d_%H%M%S")
            ./run-app.sh -a ${algo} -t ppm > "results/${algo}_${ts}_output_${TIMESTAMP}.log" 2>&1
        done
    done
done
