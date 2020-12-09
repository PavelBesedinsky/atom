package ru.atom.chat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.repository.user.UserRepository;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@Ignore
public class UserRepositoryTests {
    @Autowired
    private UserRepository userRepository;

    final User user = new User("name","password", true);

    @Before
    public void init() {
        userRepository.save(user);
    }

    @Test
    public void testGet() {

        final Optional<User> user = userRepository.findById(this.user.getId());

        assertTrue(user.isPresent());
        user.ifPresent(usr -> assertEquals(usr.getId(), this.user.getId()));
    }

}
