package ru.atom.chat.entity.message;

import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import ru.atom.chat.entity.user.User;

import javax.persistence.*;
import java.time.Instant;

import java.util.UUID;

@Entity
@Table(name="Messages")
public class Message {
    @Id
    @GeneratedValue
    private UUID id;


    private String message;
    private Instant instant;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Message() {
    }

    public Message(String message, Instant date) {
        this.message = message;
        this.instant = date;
    }

    public Message(String message, Instant date, User user) {
        this.message = message;
        this.instant = date;
        this.user = user;
    }

    public Message(UUID id, String message, Instant date, User user) {
        this.id = id;
        this.message = message;
        this.instant = date;
        this.user = user;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
