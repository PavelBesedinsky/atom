package ru.atom.chat.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.repository.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByName(String name) {
        return userRepository.findUserByName(name);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }
}
