#!/bin/bash

# install node modules
npm install ws && npm install wscat && npm install http-proxy

# start the websocket server
node websocketserver.js &
node websocketproxy.js &
sleep 1

# use wscat to issue data
(echo -n; sleep 1; echo orville; sleep 1; echo wilbur; sleep 1; ) | ./node_modules/wscat/bin/wscat --connect ws://localhost:8888

# kill the websocket server
pkill -f websocketserver.js
pkill -f websocketproxy.js

