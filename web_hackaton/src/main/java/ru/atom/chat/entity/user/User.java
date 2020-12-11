package ru.atom.chat.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ru.atom.chat.entity.message.Message;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.UUID;
import java.util.List;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String password;
    private boolean online;
    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "user",
            cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Message> messages;

    public User() {
    }

    public User(String name, String password, boolean online) {
        this.name = name;
        this.password = password;
        this.online = online;
    }

    public User(String name, String password, boolean online, List<Message> messages) {
        this.name = name;
        this.password = password;
        this.online = online;
        this.messages = messages;
    }

    public User(UUID id, String name, String password, boolean online) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.online = online;
    }

    public User(UUID id, String name, String password, boolean online, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.online = online;
        this.messages = messages;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
