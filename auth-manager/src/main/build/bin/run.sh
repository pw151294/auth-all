#!/bin/sh
BASE_DIR=`cd $(dirname $0); pwd`

if [ -f "$BASE_DIR/VERSION" ]; then
  SERVER_JAR=`cat $BASE_DIR/VERSION`
else
  echo "no VERSION file found"
  exit 1
fi


JAVA_OPT="-server -Xms128m -Xmx128m -Xmn64m -XX:MetaspaceSize=64m -XX:MaxMetaspaceSize=128m"
JAVA_OPT="${JAVA_OPT} -XX:-OmitStackTraceInFastThrow -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=${BASE_DIR}/logs/heapdump.hprof"
JAVA_OPT="${JAVA_OPT} -Xloggc:${BASE_DIR}/logs/gc.log -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=3 -XX:GCLogFileSize=1M"


if [ "$1" = "start" ]; then
  java ${JAVA_OPT} -jar ${BASE_DIR}/${SERVER_JAR} 2>&1 &
  echo " auth-manager server is starting"
elif [ "$1" = "stop" ]; then
  pid=`ps -ef | grep ${SERVER_JAR} | grep java | grep -v grep | awk '{print $1}'`
  if [ -z "$pid" ] ; then
    echo "no auth-manager server running."
    exit 1;
  fi
  kill ${pid}
  echo "shut down auth-manager server(${pid}) "
else
  echo "please use (sh run.sh start) or (sh run.sh stop)"
fi
