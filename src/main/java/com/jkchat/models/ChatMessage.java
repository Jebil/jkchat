package com.jkchat.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ChatMessage", schema = "test")
public class ChatMessage {
	@Id
	@GeneratedValue
	private int id;
	@Column
	private String fromUser;
	@Column
	private String message;

	public String getFrom() {
		return fromUser;
	}

	public String getMessage() {
		return message;
	}

	public void setFrom(String from) {
		this.fromUser = from;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
