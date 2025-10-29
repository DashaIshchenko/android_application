package com.localmesh.model;

public class Message {
	public enum Type { TEXT, LOCATION_UPDATE, SOS }

	private String id;
	private String fromUserId;
	private String fromUserName;
	private String text;
	private long timestamp;
	private Type type = Type.TEXT;

	public Message() {}

	public String getId() { return id; }
	public void setId(String id) { this.id = id; }
	public String getFromUserId() { return fromUserId; }
	public void setFromUserId(String fromUserId) { this.fromUserId = fromUserId; }
	public String getFromUserName() { return fromUserName; }
	public void setFromUserName(String fromUserName) { this.fromUserName = fromUserName; }
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }
	public long getTimestamp() { return timestamp; }
	public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
	public Type getType() { return type; }
	public void setType(Type type) { this.type = type; }
}