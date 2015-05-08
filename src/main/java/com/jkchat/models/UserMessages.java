package com.jkchat.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "usermessages", schema = "test")
public class UserMessages {
	@Id
	@GeneratedValue
	private int id;

	@Column(name = "uName")
	private String uName;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "cm")
	private ChatMessage cm;

	public String getuName() {
		return uName;
	}

	public void setuName(String uName) {
		this.uName = uName;
	}

	public ChatMessage getCm() {
		return cm;
	}

	public void setCm(ChatMessage cm) {
		this.cm = cm;
	}
}
