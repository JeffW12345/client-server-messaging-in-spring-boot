package com.example.messagingservice;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint(value = "/webSocket")
public class ServerMessagingService {
    private final static Set<Session> SESSIONS = new CopyOnWriteArraySet<>();

    @OnOpen
    public synchronized void onOpen(Session session) {
        SESSIONS.add(session);
        log.info(String.format("New server session. Number of sessions: %s", SESSIONS.size()));
    }

    @OnMessage
    public synchronized void onMessage(String message) {
        sendReplyToAllClients("Message from a client: " + message);
        String clientNumber = message.split("Client ")[1];
        sendReplyToAllClients("Hi, Client " + clientNumber);
    }
    @OnClose
    public synchronized void onClose(Session session) {
        SESSIONS.remove(session);
        log.warn(String.format("Session disconnected. Total connected sessions: %s", SESSIONS.size()));
    }
    @OnError
    public synchronized void onError(Session session, Throwable throwable) {
        log.error(throwable.getMessage());
    }

    public static void sendReplyToAllClients(String message) {
        for (Session session : SESSIONS) {
            synchronized (session){
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    log.error("Caught exception while sending message to Session Id: " + session.getId(), e.getMessage(), e);
                }
            }
        }
    }
}

