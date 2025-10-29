package com.localmesh.network;

import com.localmesh.config.ServerConfig;
import com.localmesh.model.Message;
import com.localmesh.model.User;
import com.localmesh.utils.JsonUtil;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class WebSocketChatServer extends WebSocketServer {
	private final Map<WebSocket, User> clients = new ConcurrentHashMap<>();
	private final ServerConfig cfg;
	private final AtomicInteger connections = new AtomicInteger(0);

	public WebSocketChatServer(ServerConfig cfg) {
		super(new InetSocketAddress(cfg.getPort()));
		this.cfg = cfg;
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		if (connections.incrementAndGet() > cfg.getMaxConnections()) {
			conn.close(1001, "Server full");
			connections.decrementAndGet();
			return;
		}
		// ПОЛЬЗОВАТЕЛЕЙ ПОКА ЧТО НЕТ
		User u = new User("anonymous");
		clients.put(conn, u);
		System.out.println("New connection: " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		User u = clients.remove(conn);
		connections.decrementAndGet();
		System.out.println("Connection closed: " + (u != null ? u.getId() : "unknown") + " reason=" + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
			Map incoming = JsonUtil.fromJson(message, Map.class);
			String action = (String) incoming.get("action");
			if ("register".equals(action)) { 
				String name = (String) incoming.getOrDefault("name", "anonymous");
				User u = clients.get(conn);
				if (u != null) u.setName(name);
				conn.send(JsonUtil.toJson(Collections.singletonMap("type", "registered")));
				broadcastUserList();
				return;
			}
			if ("message".equals(action)) {
				Message msg = JsonUtil.fromJson(message, Message.class);
				msg.setTimestamp(System.currentTimeMillis());
				broadcast(JsonUtil.toJson(msg));
				return;
			}
			conn.send(JsonUtil.toJson(Collections.singletonMap("error", "unknown action")));
		} catch (Exception e) {
			conn.send(JsonUtil.toJson(Collections.singletonMap("error", e.getMessage())));
		}
	}
	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.err.println("Server error: " + ex.getMessage());
	}

	@Override
	public void onStart() {
		System.out.println("WebSocket server started on port " + cfg.getPort());
	}

	private void broadcastUserList() {
		Map<String, Object> payload = Collections.singletonMap("type", "users");
		broadcast(JsonUtil.toJson(payload));
	}
}