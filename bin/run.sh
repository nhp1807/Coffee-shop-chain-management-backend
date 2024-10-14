#!/usr/bin/env bash
# shellcheck disable=SC2046
BASE_DIR=$(dirname $(readlink -e $0))/..

LOG_LEVEL=info

MAIN_JAR=target/coffee-shop-chain-management.jar

echo $BASE_DIR
echo $MAIN_JAR

while (true)
do
java -Dlogging.level.root=$LOG_LEVEL \
     -Dlog4j2.formatMsgNoLookups=true \
     -XX:ParallelGCThreads=20 -XX:-UseGCOverheadLimit -XX:GCTimeRatio=5 \
     -jar $MAIN_JAR
echo "sleeping 2 seconds before continuous...."
sleep 2s
done