package ru.atom.chat.service.user;//package ru.atom.chat.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.modelDto.UserDto;
import ru.atom.chat.repository.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Iterable<UserDto> findAll() {
        final List<User> users = (List<User>) userRepository.findAll();
        return users.stream().map((user) -> new UserDto(user.getName(), user.isOnline())).collect(Collectors.toList());
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
