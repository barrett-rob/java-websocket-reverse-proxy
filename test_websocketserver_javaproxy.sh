#!/bin/bash

# install node modules
npm install ws && npm install wscat && npm install http-proxy

# build java proxy
gradle clean build

# start the node websocket server and java websocket proxy
node websocketserver.js &
java -jar ./build/libs/java-websocket-reverse-proxy-0.0.1-SNAPSHOT.jar &
sleep 4

# use wscat to issue data
(echo -n; sleep 1; echo orville; sleep 1; echo wilbur; sleep 1; ) | ./node_modules/wscat/bin/wscat --connect ws://localhost:7777

# kill the servers
pkill -f websocketserver.js
pkill -f java-websocket-reverse-proxy
