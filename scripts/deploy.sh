#!/usr/bin/env bash

mvn clean package

echo 'Copy files...'

scp -i ~/.ssh/id_rsa target/thoughts-0.0.1-SNAPSHOT.jar adsima@192.168.0.101:~/thoughts-0.0.1-SNAPSHOT.jar

echo 'Restart server...'

ssh -i ~/.ssh/id_rsa adsima@192.168.0.101 << EOF

pgrep java | xargs kill -9
nohup java -jar ~/thoughts-0.0.1-SNAPSHOT.jar > log.txt &

EOF

echo 'Bye'
