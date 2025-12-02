package com.localmesh.model;

import java.util.UUID;

public class User {
	public enum Status { ONLINE, OFFLINE }
	
	private final String id;
	private String name;
	
	private double latitude = 0.0;
	private double longitude = 0.0;
	
	private Status status = Status.OFFLINE;
	
	private long lastUpdate = 0L;
	private long lastSeen = 0L;

	public User(String name) {
		this.id = UUID.randomUUID().toString();
		this.name = name != null ? name : "anonymous";
	}

	public String getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public double getLatitude() { return latitude; }
	public void setLatitude(double latitude) { this.latitude = latitude; }
	public double getLongitude() { return longitude; }
	public void setLongitude(double longitude) { this.longitude = longitude; }
	
	public long getLastUpdate() { return lastUpdate; }
	public void setLastUpdate(long lastUpdate) { this.lastUpdate = lastUpdate; }
	
	public Status getStatus() { return status; }
	public void setStatus(Status status) { this.status = status; }

	public long getLastSeen() { return lastSeen; }
	public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
}