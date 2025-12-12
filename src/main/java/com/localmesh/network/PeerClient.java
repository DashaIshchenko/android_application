package com.localmesh.network;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;


 //Клиент для подключения к другим узлам (Peer-to-Peer) в mesh-сети

public class PeerClient extends WebSocketClient {
    
    private PeerMessageListener listener;
    
    public PeerClient(URI serverUri, PeerMessageListener listener) {
        super(serverUri);
        this.listener = listener;
    }
    
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("[PeerClient] Connected to peer: " + getURI());
        
        // Отправляем приветственное сообщение о своём подключении
        String greeting = "{\"type\":\"peer_connect\",\"address\":\"" + 
                         getLocalSocketAddress() + "\"}";
        send(greeting);
    }
    
    @Override
    public void onMessage(String message) {
        System.out.println("[PeerClient] Message from peer " + getURI() + ": " + message.substring(0, Math.min(100, message.length())) + "...");
        
        // Передаём сообщение слушателю
        if (listener != null) {
            listener.onPeerMessage(message, getURI().toString());
        }
    }
    
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("[PeerClient] Disconnected from peer: " + getURI() + 
                          ", code: " + code + ", reason: " + reason);
    }
    
    @Override
    public void onError(Exception ex) {
        System.err.println("[PeerClient] Error with peer " + getURI() + ":");
        ex.printStackTrace();
    }
    
    public interface PeerMessageListener {
        void onPeerMessage(String message, String peerAddress);
    }
}
