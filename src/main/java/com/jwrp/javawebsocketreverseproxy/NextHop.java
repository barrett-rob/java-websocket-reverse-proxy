package com.jwrp.javawebsocketreverseproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * Represents a 'hop' in the proxying chain, establishes a 'client' to
 * communicate with the next server, with a {@link WebSocketProxyClientHandler}
 * to copy data from the 'client' to the supplied 'server' session.
 */
public class NextHop {

    private final WebSocketSession webSocketClientSession;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public NextHop(WebSocketSession webSocketServerSession, WebSocketProxyClientHandler.ClientOfflineListener listener) {
        webSocketClientSession = createWebSocketClientSession(webSocketServerSession, listener);
    }

    private WebSocketHttpHeaders getWebSocketHttpHeaders(final WebSocketSession userAgentSession) {
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        Principal principal = userAgentSession.getPrincipal();
        if (principal != null && OAuth2Authentication.class.isAssignableFrom(principal.getClass())) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) principal;
            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) oAuth2Authentication.getDetails();
            String accessToken = details.getTokenValue();
            headers.put(HttpHeaders.AUTHORIZATION, Collections.singletonList("Bearer " + accessToken));
            if(logger.isDebugEnabled()) {
                logger.debug("Added Oauth2 bearer token authentication header for user " +
                        principal.getName() + " to web sockets http headers");
            }
        }
        else {
            if(logger.isDebugEnabled()) {
                logger.debug("Skipped adding basic authentication header since user session principal is null");
            }
        }
        return headers;
    }

    private WebSocketSession createWebSocketClientSession(WebSocketSession webSocketServerSession, WebSocketProxyClientHandler.ClientOfflineListener listener) {
        try {
            WebSocketHttpHeaders headers = getWebSocketHttpHeaders(webSocketServerSession);
            return new StandardWebSocketClient()
                    .doHandshake(new WebSocketProxyClientHandler(webSocketServerSession, listener), headers, new URI("ws://localhost:9999"))
                    .get(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessageToNextHop(WebSocketMessage<?> webSocketMessage) throws IOException {
        webSocketClientSession.sendMessage(webSocketMessage);
    }

    /**
     * Triggering client offline.
     */
    public void close() throws IOException {
        webSocketClientSession.close();
    }
}
