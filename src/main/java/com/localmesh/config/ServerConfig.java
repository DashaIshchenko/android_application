package com.localmesh.config;

import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {
	private final int port;
	private final int maxConnections;
	private final boolean broadcast;

	public ServerConfig() {
		Properties p = new Properties();
		try (InputStream is = ServerConfig.class.getResourceAsStream("/config.properties")) {
			if (is != null) p.load(is);
		} catch (Exception e) {
			System.err.println("Failed to load config.properties: " + e.getMessage());
		}
		this.port = Integer.parseInt(p.getProperty("server.port", "8887"));
		this.maxConnections = Integer.parseInt(p.getProperty("server.maxConnections", "100"));
		this.broadcast = Boolean.parseBoolean(p.getProperty("server.broadcast", "true"));
	}

	public int getPort() { return port; }
	public int getMaxConnections() { return maxConnections; }
	public boolean isBroadcast() { return broadcast; }
}