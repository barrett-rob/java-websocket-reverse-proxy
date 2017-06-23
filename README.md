# java-websocket-reverse-proxy

Java implementation of a websocket reverse proxy. A similar method to the one described in 
[https://www.nginx.com/blog/websocket-nginx/](https://www.nginx.com/blog/websocket-nginx/),
but implemented in Java. This should be useful in Java application servers, e.g. Spring Boot.

There are a few nodejs scripts at the root level that can be used to mimic some of the 
surrounding functionality required to verify the proxy server.

To establish a direct connection to a websocket server, run 
[test_websocketserver_direct.sh](./test_websocketserver_direct.sh) 





*(developed on macOS, ymmv on other platforms)*