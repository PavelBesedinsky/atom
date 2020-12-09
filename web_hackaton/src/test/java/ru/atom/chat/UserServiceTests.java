package ru.atom.chat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.service.message.MessageService;
import ru.atom.chat.service.user.UserService;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class UserServiceTests {

    @Autowired
    private UserService userService;
    final User user = new User("name","password", true);

    @Before
    public void createUser() {
        userService.createUser(user);
    }

    @Test
    public void testGetUserByIdFromDb() {
        final Optional<User> user = userService.findById(this.user.getId());
        assertTrue(user.isPresent());
        user.ifPresent(value -> assertEquals(value.getId(), this.user.getId()));
        user.ifPresent(value -> System.out.println("User: " + user.get().getName()));
    }
}
