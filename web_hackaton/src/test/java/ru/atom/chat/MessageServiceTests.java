package ru.atom.chat;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.entity.message.Message;
import ru.atom.chat.service.message.MessageService;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class MessageServiceTests {
    @Autowired
    private MessageService messageService;

    final Message message = new Message(
            "message",
            Instant.now()
    );

    @Test
    public void testGetAllFromDb() {
        messageService.createMessage(message);
        List<Message> messages = messageService.getAllMessages();
        assertNotEquals(messages.size(), 0);
    }
}
