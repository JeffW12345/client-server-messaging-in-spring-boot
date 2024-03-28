package com.example.messagingservice;

import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;

@ClientEndpoint
@Slf4j
public class ClientMessagingService {
    private final int clientNumber;
    private Session session;
    public ClientMessagingService(int clientNumber){
        this.clientNumber = clientNumber;
    }
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        log.info("Client " + clientNumber + " open");
    }
    @OnMessage
    public void onMessage(String message) {
        log.info("Client " + clientNumber + ": Server says: " + message);
    }
    public void sendMessage(String message) {
        if (session != null && session.isOpen()) {
            session.getAsyncRemote().sendText(message);
        }
    }
    public void setUpAndSendMessageToServer(){
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, new URI("ws://localhost:8080/webSocket"));

            this.sendMessage("Hello, Server, from Client " + clientNumber);

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}