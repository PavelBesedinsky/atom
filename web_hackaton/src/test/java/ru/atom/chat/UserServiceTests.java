package ru.atom.chat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.repository.user.UserRepository;
import ru.atom.chat.service.user.UserService;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@SpringBootTest
public class UserServiceTests {
    @Mock
    private UserRepository mockUserRepository;

    private UserService userServiceUnderTest;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        userServiceUnderTest = new UserService(mockUserRepository);

        User testUser = new User("user_1", "password", true);


        Mockito.when(mockUserRepository.save(any()))
                .thenReturn(testUser);

        Mockito.when(mockUserRepository.findUserByName(anyString()))
                .thenReturn(java.util.Optional.of(testUser));
    }

    @Test
    public void testFindUserByName() {
        final String userName = "user_1";
        final Optional<User> result = userServiceUnderTest.findByName("user_1");
        // Verify the results

        if (result.isPresent()) {
            assertEquals(userName, result.get().getName());
        } else {
            fail();
        }
    }

    @Test
    public void testCreateUser() {
        User result = userServiceUnderTest.createUser(new User("user_1", "password", true));
        // Verify the results
        assertEquals("user_1", result.getName());
    }
}
