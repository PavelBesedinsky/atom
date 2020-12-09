package ru.atom.chat.modelDto;

import java.util.UUID;

public class UserDto {
    private final UUID id;
    private final String name;
    private final boolean online;

    public UserDto(UUID id, String name, boolean online) {
        this.id = id;
        this.name = name;
        this.online = online;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }
}
