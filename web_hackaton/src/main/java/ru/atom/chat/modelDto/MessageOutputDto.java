package ru.atom.chat.modelDto;

import java.time.Instant;

public class MessageOutputDto {
    private final String name;
    private final String message;
    private final Instant instant;

    public MessageOutputDto(String name, String message, Instant instant) {
        this.name = name;
        this.message = message;
        this.instant = instant;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Instant getInstant() {
        return instant;
    }
}
