#!/bin/bash
classpath=$(mvn dependency:build-classpath -Dmdep.outputFilterFile=true | grep classpath=)
mvn clean compile
application_name=mrbs
current_date=$(date '+%d-%m-%Y_%H-%M')
./run-docker.sh -p yes -n $application_name-$current_date
sleep 15
java -cp $classpath:./target/classes main.CheckSuiteFlakiness
docker stop $application_name-$current_date
docker rm $application_name-$current_date
