package ru.atom.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.atom.chat.entity.message.Message;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.modelDto.LoginDto;
import ru.atom.chat.modelDto.MessageDto;
import ru.atom.chat.modelDto.MessageOutputDto;
import ru.atom.chat.modelDto.UserDto;
import ru.atom.chat.service.message.MessageService;
import ru.atom.chat.service.user.UserService;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto) {
        final Optional<User> optionalUser = userService.findById(messageDto.getId());
        if (optionalUser.isPresent()) {
            Message message = new Message(messageDto.getMessage(), Instant.now(), optionalUser.get());
            messageService.createMessage(message);
            return new ResponseEntity<>(message.getId().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/messages")
    public ResponseEntity<List<MessageOutputDto>> getMessages() {
        List<MessageOutputDto> messages = messageService.getAllMessages()
                .stream().map(message ->
                        new MessageOutputDto(
                                message.getUser().getName(),
                                message.getMessage(),
                                message.getInstant()
                        )).collect(Collectors.toList());
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<Iterable<UserDto>> getUsers() {
        Iterable<UserDto> users = ((List<User>) userService.findAll()).stream().map(
                (user) -> new UserDto(user.getId(), user.getName(), user.isOnline())).collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        final Optional<User> optionalUser = userService.findByName(loginDto.getName());
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            if (user.getPassword().equals(loginDto.getPassword())) {
                final User onlineUser = new User(user.getId(), user.getName(), user.getPassword(), true);
                userService.updateUser(onlineUser);
                return new ResponseEntity<>(user.getId().toString(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Неправильный пароль", HttpStatus.FORBIDDEN);
            }
        } else {
            final User createdUser = new User(loginDto.getName(), loginDto.getPassword(), true);
            userService.createUser(createdUser);
            return new ResponseEntity<String>(createdUser.getId().toString(), HttpStatus.OK);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String userName) {
        final Optional<User> optionalUser = userService.findByName(userName);
        if (optionalUser.isPresent()) {
            final User user = optionalUser.get();
            final User offlineUser = new User(user.getId(), user.getName(), user.getPassword(), false);
            userService.updateUser(offlineUser);
            return new ResponseEntity<>("Пользователь " + userName + " разлогинился", HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Пользователь не найден", HttpStatus.NOT_FOUND);
        }
    }
}