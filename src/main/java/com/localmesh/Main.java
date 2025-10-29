package com.localmesh;

import com.localmesh.config.ServerConfig;
import com.localmesh.network.WebSocketChatServer;

public class Main {
	public static void main(String[] args) {
		ServerConfig cfg = new ServerConfig();
		WebSocketChatServer server = new WebSocketChatServer(cfg);
		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				server.stop();
				System.out.println("Server stopped");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));

		System.out.println("LocalMeshServer is running (press Ctrl+C to stop)");
	}
}