package ru.atom.chat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ru.atom.chat.entity.message.Message;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.repository.message.MessageRepository;
import ru.atom.chat.service.message.MessageService;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class MessageServiceTests {
    @Mock
    private MessageRepository mockMessageRepository;

    private MessageService messageServiceServiceUnderTest;

    private final User testUser1 = new User("user_1", "password", true);
    private final Message testMessage1 = new Message("message_1", Instant.now(), testUser1);

    private final User testUser2 = new User("user_2", "password", true);
    private final Message testMessage2 = new Message("message_2", Instant.now(), testUser2);

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        messageServiceServiceUnderTest = new MessageService(mockMessageRepository);

        Mockito.when(mockMessageRepository.save(any()))
                .thenReturn(testMessage1);
        Mockito.when(mockMessageRepository.findAll())
                .thenReturn(Arrays.asList(testMessage1, testMessage2));
    }

    @Test
    public void testFindAllMessages() {
        final List<Message> result = messageServiceServiceUnderTest.getAllMessages();
        // Verify the results
        assertThat(result.size() == 2);
    }

    @Test
    public void testSaveMessage() {
        Message result = messageServiceServiceUnderTest.createMessage(testMessage1);
        // Verify the results
        assertEquals(testMessage1.getMessage(), result.getMessage());
    }
}
