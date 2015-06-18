package com.jkchat.models;

public class MessageDTO {
	private String from, to, message;

	public String getFrom() {
		return from;
	}

	public String getMessage() {
		return message;
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setTo(String to) {
		this.to = to;
	}

}
