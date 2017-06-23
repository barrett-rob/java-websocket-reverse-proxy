package com.jwrp.javawebsocketreverseproxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class JavaWebsocketReverseProxyApplication {

    public static void main(String[] args) {
        System.out.println("Proxy server started on port 7777");
        System.out.println("Proxy server will forward requests to port 9999");
        SpringApplication.run(JavaWebsocketReverseProxyApplication.class, args);
    }
}
