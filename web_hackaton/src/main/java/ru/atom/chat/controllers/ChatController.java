package ru.atom.chat.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.atom.chat.entity.message.Message;
import ru.atom.chat.entity.user.User;
import ru.atom.chat.modelDto.LoginDto;
import ru.atom.chat.modelDto.MessageDto;
import ru.atom.chat.modelDto.MessageOutputDto;
import ru.atom.chat.modelDto.UserDto;
import ru.atom.chat.service.message.MessageService;
import ru.atom.chat.service.user.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
//    private Queue<String> messages = new ConcurrentLinkedQueue<>();
//    private Map<String, String> registeredUsers = new ConcurrentHashMap<>();
//    private Map<String, String> usersOnline = new ConcurrentHashMap<>();
//    private String adminPassword = getSha256("admin");

//    ChatController() {
//        registeredUsers.put("admin", adminPassword);
//    }

    @Autowired
    private UserService userService;

    @Autowired
    private MessageService messageService;

    @PostMapping("/message")
    public ResponseEntity<String> sendMessage(@RequestBody MessageDto messageDto) {
        final Optional<User> optionalUser = userService.findById(messageDto.getId());
        if(optionalUser.isPresent()) {
            Message message = new Message(messageDto.getMessage(), Instant.now(), optionalUser.get());
            messageService.createMessage(message);
            return new ResponseEntity<String>(message.getId().toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<String>("Пользователь не найден", HttpStatus.NOT_FOUND);
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
        Iterable<UserDto> users = userService.findAll();
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
            }
            else {
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
//    /**
//     * curl -X POST -i localhost:8080/chat/signup -d "name=NAME&password=PASSWORD"
//     */
//    @RequestMapping(
//            path = "signup",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<String> signup(@RequestParam("name") String name,
//                                         @RequestParam("password") String password) {
//        if (name.length() < 1) {
//            return ResponseEntity.badRequest().body("Too short name.");
//        }
//
//        if (name.length() > 20) {
//            return ResponseEntity.badRequest().body("Too long name.");
//        }
//
//        if (password.length() < 8) {
//            return ResponseEntity.badRequest().body("Too short password.");
//        }
//
//        if (password.length() > 16) {
//            return ResponseEntity.badRequest().body("Too long password.");
//        }
//
//        if (registeredUsers.containsKey(name)) {
//            return ResponseEntity.badRequest().body("This user already exists");
//        }
//
//        if (!registeredUsers.containsKey("admin")) {
//            registeredUsers.put("admin", getSha256(adminPassword));
//        }
//
//        registeredUsers.put(name, getSha256(password));
//        messages.add("[" + name + "] registered! Welcome to our chat!");
//
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * curl -X POST -i localhost:8080/chat/login -d "name=NAME&password=PASSWORD"
//     */
//    @RequestMapping(
//            path = "login",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<String> login(@RequestParam("name") String name,
//                                        @RequestParam("password") String password) {
//        if (!registeredUsers.containsKey(name)) {
//            return ResponseEntity.badRequest().body("This user doesn't exists");
//        }
//
//        if (usersOnline.containsKey(name)) {
//            return ResponseEntity.badRequest().body("Already logged in.");
//        }
//
//        if ((name.equals("admin")) && (!getSha256(password).equals(adminPassword))) {
//            return ResponseEntity.badRequest().body("Incorrect Admin password.");
//        }
//
//        usersOnline.put(name, getSha256(password));
//        messages.add("[" + name + "] logged in");
//
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * curl -i localhost:8080/chat/online
//     */
//    @RequestMapping(
//            path = "online",
//            method = RequestMethod.GET,
//            produces = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity online() {
//        String responseBody = String.join("\n", usersOnline.keySet().stream()
//                .sorted().collect(Collectors.toList()));
//
//        return ResponseEntity.ok(responseBody);
//    }
//
//    /**
//     * curl -i localhost:8080/chat/allusers
//     */
//    @RequestMapping(
//            path = "allusers",
//            method = RequestMethod.GET,
//            produces = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity allUsers() {
//        String responseBody = String.join("\n", registeredUsers.keySet().stream()
//                .sorted().collect(Collectors.toList()));
//
//        return ResponseEntity.ok(responseBody);
//    }
//
//    /**
//     * curl -X POST -i localhost:8080/chat/logout -d "name=NAME&password=PASSWORD"
//     */
//    @RequestMapping(
//            path = "logout",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<String> logout(@RequestParam("name") String name,
//                                         @RequestParam("password") String password) {
//        if (!usersOnline.containsKey(name)) {
//            return ResponseEntity.badRequest().body("Already logged out.");
//        }
//
//        if (getSha256(password).equals(usersOnline.get(name))) {
//            usersOnline.remove(name);
//            messages.add("[" + name + "] logged out.");
//        } else {
//            return ResponseEntity.badRequest().body("Wrong password.");
//        }
//
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * curl -X POST -i localhost:8080/chat/say -d "name=NAME&password=PASSWORD&msg=MESSAGE"
//     */
//    @RequestMapping(
//            path = "say",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<String> say(@RequestParam("name") String name,
//                                      @RequestParam("password") String password,
//                                      @RequestParam("msg") String msg) {
//        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//
//        if (msg == null || msg == "") {
//            return ResponseEntity.badRequest().body("Message is empty!");
//        }
//
//        if (!usersOnline.containsKey(name)) {
//            return ResponseEntity.badRequest().body("User logged out.");
//        }
//
//        if (getSha256(password).equals(usersOnline.get(name))) {
//            messages.add("[" + name + " - " + df.format(date) + "]: " + msg);
//        } else {
//            return ResponseEntity.badRequest().body("Wrong password.");
//        }
//
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * curl -i localhost:8080/chat/chat
//     */
//    @RequestMapping(
//            path = "chat",
//            method = RequestMethod.GET,
//            produces = MediaType.TEXT_PLAIN_VALUE)
//    public ResponseEntity chat() {
//        String responseBody = String.join("\n", messages);
//
//        return ResponseEntity.ok(responseBody);
//    }
//
//    /**
//     * curl -X POST -i localhost:8080/chat/admin/kick -d "adminname=ADMINNAME&password=PASSWORD&kickname=KICKNAME"
//     */
//    @RequestMapping(
//            path = "admin/kick",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity adminKick(@RequestParam("adminname") String adminName,
//                                    @RequestParam("password") String password,
//                                    @RequestParam("kickname") String kickName) {
//        if ((!adminName.equals("admin")) || (!getSha256(password).equals(adminPassword))) {
//            return ResponseEntity.badRequest().body("Incorrect Admin password.");
//        }
//
//        if (!usersOnline.containsKey(kickName)) {
//            return ResponseEntity.badRequest().body("User logged out.");
//        }
//
//        usersOnline.remove(kickName);
//        messages.add("[" + kickName + "] was kicked by Admin.");
//
//        return ResponseEntity.ok().build();
//    }
//
//    /**
//     * curl -X POST -i localhost:8080/chat/admin/ban -d "adminname=ADMINNAME&password=PASSWORD&banname=BANNAME"
//     */
//    @RequestMapping(
//            path = "admin/ban",
//            method = RequestMethod.POST,
//            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public ResponseEntity adminBan(@RequestParam("adminname") String adminName,
//                                   @RequestParam("password") String password,
//                                   @RequestParam("banname") String banName) {
//        if ((!adminName.equals("admin")) || (!getSha256(password).equals(adminPassword))) {
//            return ResponseEntity.badRequest().body("Incorrect Admin password.");
//        }
//
//        usersOnline.remove(banName);
//
//        if (!registeredUsers.containsKey(banName)) {
//            return ResponseEntity.badRequest().body("[" + banName + "] doesn't exists");
//        }
//
//        registeredUsers.remove(banName);
//        messages.add("[" + banName + "] was banned by Admin. Bye-bye!");
//
//        return ResponseEntity.ok().build();
//    }

    private String getSha256(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] messageDigest = md.digest(str.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);

            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }

            return hashText;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}