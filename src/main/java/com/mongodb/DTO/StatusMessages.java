package com.mongodb.DTO;

public class StatusMessages {
	
	public StatusMessages(RequestStatus statusType, String message) {
		super();
		this.statusType = statusType;
		this.message = message;
	}
	private RequestStatus statusType;
	private String message;
	public RequestStatus getStatusType() {
		return statusType;
	}
	public void setStatusType(RequestStatus statusType) {
		this.statusType = statusType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
