package com.jwrp.javawebsocketreverseproxy;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handles establishment and tracking of next 'hop', and
 * copies data from the current session to the next hop.
 */
@Component
public class WebSocketProxyServerHandler extends AbstractWebSocketHandler {

    private final Map<String, NextHop> nextHops = new ConcurrentHashMap<>();

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage) throws Exception {
        getNextHop(webSocketSession).sendMessageToNextHop(webSocketMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus status) throws Exception {
        super.afterConnectionClosed(webSocketSession, status);
        NextHop nextHop = nextHops.get(webSocketSession.getId());
        if (nextHop != null) {
            nextHop.close();
        }
    }

    private NextHop getNextHop(WebSocketSession webSocketSession) {
        NextHop nextHop = nextHops.get(webSocketSession.getId());
        if (nextHop == null) {
            // registering offline listener to avoid nextHop leaks.
            nextHop = new NextHop(webSocketSession, () -> nextHops.remove(webSocketSession.getId()));
            nextHops.put(webSocketSession.getId(), nextHop);
        }
        return nextHop;
    }

}
