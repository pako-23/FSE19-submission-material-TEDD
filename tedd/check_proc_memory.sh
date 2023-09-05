#!/bin/sh
PROCESS="java"

while true; do
    PIDS=$(pgrep "$PROCESS")
    for PID in $PIDS; do
        if [ ! -z "$PID" ]; then
            MEM_USAGE=$(ps -o rss= -p $PID)
            echo "$(date): PID $PID - MEM_USAGE $MEM_USAGE kB"
        else
            echo "$(date): $PROCESS not running."
        fi
    done

    sleep 5
done
