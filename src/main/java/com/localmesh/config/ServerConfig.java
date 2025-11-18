package com.localmesh.config;

import java.io.InputStream;
import java.util.Properties;

public class ServerConfig {
	private static final int DEFAULT_PORT = 8887;
	private static final int DEFAULT_MAX_CONNECTIONS = 100;
	private static final boolean DEFAULT_BROADCAST = true;
	
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
		this.port = Integer.parseInt(p.getProperty("server.port", String.valueOf(DEFAULT_PORT)));
		this.maxConnections = Integer.parseInt(p.getProperty("server.maxConnections", String.valueOf(DEFAULT_MAX_CONNECTIONS)));
		this.broadcast = Boolean.parseBoolean(p.getProperty("server.broadcast", String.valueOf(DEFAULT_BROADCAST)));
	}

	public int getPort() { return port; }
	public int getMaxConnections() { return maxConnections; }
	public boolean isBroadcast() { return broadcast; }
}