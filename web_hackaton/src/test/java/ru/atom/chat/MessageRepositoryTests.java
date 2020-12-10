package ru.atom.chat;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.atom.chat.entity.message.Message;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.repository.message.MessageRepository;
import ru.atom.chat.repository.user.UserRepository;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {MessageRepositoryTests.Initializer.class})
@SpringBootTest
public class MessageRepositoryTests {
    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11");

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testGetAllMessages() {
        messageRepository.deleteAll();
        Message message1 = messageRepository.save(
                new Message("message1", Instant.now())
        );
        Message message2 = messageRepository.save(
                new Message("message2", Instant.now())
        );
        Message message3 = messageRepository.save(
                new Message("message3", Instant.now())
        );

        assertThat(messageRepository.findAll().size() == 3);
    }

    @Test
    public void testSaveNewMessageWithoutUser() {
        messageRepository.deleteAll();
        Message message = messageRepository.save(
                new Message("message", Instant.now())
        );

        assertThat(message)
                .matches(m -> m.getId() != null && m.getMessage().equals("message")
                        && m.getUser() == null);

        assertThat(messageRepository.findAll().size() == 1);
    }

    @Test
    public void testSaveNewMessageWithUser() {
        messageRepository.deleteAll();
        userRepository.deleteAll();
        User user = userRepository.save(
                new User("user_1", "password", true)
        );
        Message message = messageRepository.save(
                new Message("message", Instant.now(), user)
        );

        assertThat(user)
                .matches(u -> u.getId() != null && u.getName().equals("user_1")
                        && u.getPassword().equals("password")
                        && u.isOnline());

        assertThat(message)
                .matches(m -> m.getId() != null && m.getMessage().equals("message")
                        && m.getUser().getId() == user.getId());

        assertThat(messageRepository.findAll().size() == 1);
    }


    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + postgreSQLContainer.getUsername(),
                    "spring.datasource.password=" + postgreSQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
