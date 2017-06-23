package com.jwrp.javawebsocketreverseproxy;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Represents a 'hop' in the proxying chain, establishes a 'client' to
 * communicate with the next server, with a {@link WebSocketProxyClientHandler}
 * to copy data from the 'client' to the supplied 'server' session.
 */
public class NextHop {

    private final WebSocketSession webSocketClientSession;

    public NextHop(WebSocketSession webSocketServerSession) {
        webSocketClientSession = createWebSocketClientSession(webSocketServerSession);
    }

    private WebSocketSession createWebSocketClientSession(WebSocketSession webSocketServerSession) {
        try {
            return new StandardWebSocketClient()
                    .doHandshake(new WebSocketProxyClientHandler(webSocketServerSession), "ws://localhost:9999")
                    .get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToNextHop(WebSocketMessage<?> webSocketMessage) throws IOException {
        webSocketClientSession.sendMessage(webSocketMessage);
    }
}
