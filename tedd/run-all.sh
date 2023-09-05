#!/bin/bash
./run-app.sh -a pradet -t claroline &&
./run-app.sh -a pradet -t mantisbt &&
./run-app.sh -a pradet -t mrbs &&
# ./run-app.sh -a ex-linear -t claroline &&
# ./run-app.sh -a ex-linear -t mantisbt &&
# ./run-app.sh -a ex-linear -t mrbs &&
# ./run-app.sh -a big-table -t addressbook &&
# ./run-app.sh -a big-table -t claroline &&
./run-app.sh -a big-table -t mantisbt &&
./run-app.sh -a big-table -t mrbs