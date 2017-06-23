package com.jwrp.javawebsocketreverseproxy;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * Copies data from the client to the server session.
 */
public class WebSocketProxyClientHandler extends AbstractWebSocketHandler {

    private final WebSocketSession webSocketServerSession;

    public WebSocketProxyClientHandler(WebSocketSession webSocketServerSession) {
        this.webSocketServerSession = webSocketServerSession;
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> webSocketMessage) throws Exception {
        webSocketServerSession.sendMessage(webSocketMessage);
    }
}
