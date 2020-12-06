package ru.atom.chat.entity.user;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name="Users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String password;
    private boolean online;

    public User() {
    }

    public User(String name, String password, boolean online) {
        this.name = name;
        this.password = password;
        this.online = online;
    }

    public User(UUID id, String name, String password, boolean online) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.online = online;
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

    public boolean getOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
}
