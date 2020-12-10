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
import ru.atom.chat.entity.user.User;
import ru.atom.chat.repository.user.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(initializers = {UserRepositoryTests.Initializer.class})
@SpringBootTest
public class UserRepositoryTests {

    @ClassRule
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres:11");

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveNewUser() {
        userRepository.deleteAll();
        User user = userRepository.save(new User("user_1", "password", true));

        assertThat(user)
                .matches(u -> u.getId() != null && u.getName().equals("user_1")
                        && u.getPassword().equals("password")
                        && u.isOnline());
        assertThat(userRepository.findAll().size() == 1);
    }

    @Test
    public void testFindAllUsers() {
        userRepository.deleteAll();
        User user1 = userRepository.save(new User("user_1", "password", true));
        User user2 = userRepository.save(new User("user_2", "password", true));

        assertThat(userRepository.findAll().size() == 2);
    }

    @Test
    public void testFindUserById() {
        userRepository.deleteAll();
        User user = userRepository.save(new User("user_1", "password", true));

        assertThat(user)
                .matches(u -> u.getId() != null && u.getName().equals("user_1")
                        && u.getPassword().equals("password")
                        && u.isOnline());

        assertThat(userRepository.findById(user.getId()));
    }

    @Test
    public void testFindUserByName() {
        userRepository.deleteAll();
        User user = userRepository.save(new User("user_1", "password", true));

        assertThat(user)
                .matches(u -> u.getId() != null && u.getName().equals("user_1")
                        && u.getPassword().equals("password")
                        && u.isOnline());

        assertThat(userRepository.findUserByName(user.getName()));
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
