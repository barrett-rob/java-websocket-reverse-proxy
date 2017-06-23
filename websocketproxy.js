var httpProxy = require('http-proxy');
var proxy = new httpProxy.createProxyServer({
    target: {
        port: 9999
    }
});

var http = require('http');
var server = http.createServer(function (req, res) {
    proxy.web(req, res);
});

server.on('upgrade', function (req, socket, head) {
    proxy.ws(req, socket, head);
});

const listenOnPort = 8888;
console.log("Proxy server started on port %d", listenOnPort);
console.log("Proxy server will forward requests to port 9999");

server.listen(listenOnPort);