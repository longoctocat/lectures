#!/bin/bash
APP_HOME='.'
java -Xmx128m -Xms50m \
	-XX:+UseG1GC -XX:G1HeapRegionSize=1 \
	-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="$APP_HOME/dumps"\
	-Xloggc:"$APP_HOME/logs/`date +%F_%H-%M-%S`-gc.log" -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCCause\
	-Dlog4j.configurationFile="$APP_HOME/config/log4j2.xml"\
	-XX:+UnlockCommercialFeatures -XX:+FlightRecorder\
	-jar "$APP_HOME/target/tooling-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
