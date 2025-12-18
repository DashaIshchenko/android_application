package com.localmesh.model;

public class Message {
	public enum Type { TEXT, LOCATION_UPDATE, SOS, FILE, ACK }

	private String id;
	private String fromUserId;
	private String fromUserName;
	private String text;
	private long timestamp;
	private Type type = Type.TEXT;
	private Double latitude;
	private Double longitude;
	private String fileType;    
	private String fileName;    
	private String fileData;    
	private boolean ackRequired = false;
	private String ackForMessageId;
	private Boolean encrypted = false;

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
	public Double getLatitude() { return latitude; }
	public void setLatitude(Double latitude) { this.latitude = latitude; }
	public Double getLongitude() { return longitude; }
	public void setLongitude(Double longitude) { this.longitude = longitude; }

	public String getFileType() { return fileType; }
	public void setFileType(String fileType) { this.fileType = fileType; }
	public String getFileName() { return fileName; }
	public void setFileName(String fileName) { this.fileName = fileName; }
	public String getFileData() { return fileData; }
	public void setFileData(String fileData) { this.fileData = fileData; }
	public Boolean isEncrypted() { return encrypted; }
	public void setEncrypted(Boolean encrypted) { this.encrypted = encrypted; }
	
	public boolean isAckRequired() { return ackRequired; }
	public void setAckRequired(boolean ackRequired) { this.ackRequired = ackRequired; }
	public String getAckForMessageId() { return ackForMessageId; }
	public void setAckForMessageId(String ackForMessageId) { this.ackForMessageId = ackForMessageId; }
}
