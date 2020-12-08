package ru.atom.chat.entity.message;

import ru.atom.chat.entity.user.User;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Messages")
public class Message {
    @Id
    @GeneratedValue
    private UUID id;

    private String message;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Message() {
    }

    public Message(String message, User user) {
        this.message = message;
        this.user = user;
    }

    public Message(UUID id, String message, User user) {
        this.id = id;
        this.message = message;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
