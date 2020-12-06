package ru.atom.chat.modelDto;

public class UserDto {
    private final String name;
    private final boolean online;

    public UserDto(String name, boolean online) {
        this.name = name;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public boolean isOnline() {
        return online;
    }
}
