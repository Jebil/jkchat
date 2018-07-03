package com.jkchat.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Jebil Kuruvila
 */
@Entity
@Table(name = "ChatMessage", schema = "test")
@DynamicUpdate
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

    public void setFrom(String from) {
        this.fromUser = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
