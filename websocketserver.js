const listenOnPort = 9999;
console.log("Websocket server started on port %d", listenOnPort);
console.log("Websocket server will echo back requests in UPPERCASE");

var WebSocketServer = require('ws').Server;
var wss = new WebSocketServer({port: listenOnPort});

wss.on('connection', function (ws) {
    ws.on('message', function (message) {
        console.log('Received from client: %s', message);
        ws.send(message.toUpperCase());
    });
});