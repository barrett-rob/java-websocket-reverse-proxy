# java-websocket-reverse-proxy

Most of the java websocket examples I've found have been based on, or included a messaging
protocol like STOMP. This example is the result of looking at how to proxy any message content, 
without worrying about the messaging protocol.

Java implementation of a websocket reverse proxy. A similar method to the one described in 
[https://www.nginx.com/blog/websocket-nginx/](https://www.nginx.com/blog/websocket-nginx/),
but implemented in Java. This could be useful in Java application servers, e.g. Spring Boot.

There are nodejs scripts at the root level that can be used to mimic functionality 
required to verify the proxy server.

The common one is the [websocketserver.js](./websocketserver.js) script, which listens 
on port `9999` and echoes back any input after uppercasing it. To test the echoing behaviour 
by establishing a direct connection to a websocket server, run 
[test_websocketserver_direct.sh](./test_websocketserver_direct.sh) 

*(requires [node & npm](https://nodejs.org/en/) to be installed)*

There is also a very simple [websocketproxy.js](./websocketproxy.js) script which will proxy
the websocketserver. It listens on port `8888` and will relay all requests to port `9999`.
It can be tested by running [test_websocketserver_nodeproxy.sh](./test_websocketserver_nodeproxy.sh)

A simple java implementation to match this proxying behaviour is contained in the classes defined
in the [src](./src) folder. It is based on [Spring Boot](https://projects.spring.io/spring-boot/).
You can build it using [Gradle](https://gradle.org/) and run it up manually,
or use the [test_websocketserver_javaproxy.sh](./test_websocketserver_javaproxy.sh) script.

The java websocket reverse proxy will listen on port `7777` and relay all requests to port `9999`.

*(developed on macOS, ymmv on other platforms)*

