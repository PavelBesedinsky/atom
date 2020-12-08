package ru.atom.chat.modelDto;

import java.util.UUID;

public class MessageDto {
    private UUID id;
    private String message;

    public MessageDto(UUID id, String message) {
        this.id = id;
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "\nMessageDto{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
