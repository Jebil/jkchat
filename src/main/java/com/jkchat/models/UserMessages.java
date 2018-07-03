package com.jkchat.models;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * @author Jebil Kuruvila
 */
@Entity
@Table(name = "usermessages", schema = "test")
@DynamicUpdate
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
