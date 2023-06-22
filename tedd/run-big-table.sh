#!/bin/sh -eu

mvn compile

classpath="$(find ~/.m2/repository/ -name '*.jar' | paste -sd ':' -):./target/classes"

cat <<EOF
=====================================================================================
===========[ Executing BigTable ]====================================================
=====================================================================================
EOF

java -cp $classpath org.mb.tedd.main.BigTable
