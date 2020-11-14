package ru.atom.chat.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Controller
@RequestMapping("chat")
public class ChatController {
    private final Queue<String> messages = new ConcurrentLinkedQueue<>();
    private final Map<String, String> usersOnline = new ConcurrentHashMap<>();
    private final Map<String, String> usersBanned = new ConcurrentHashMap<>();

    /**
     * curl -X POST -i localhost:8080/api/chat/login -d "name=I_AM_STUPID"
     */
    @RequestMapping(
            path = "login",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> login(@RequestParam("name") String name) {
        if (name.length() < 1) {
            return ResponseEntity.badRequest().body("Too short name, sorry :(");
        }
        if (name.length() > 20) {
            return ResponseEntity.badRequest().body("Too long name, sorry :(");
        }
        if (usersOnline.containsKey(name)) {
            return ResponseEntity.badRequest().body("Already logged in:(");
        }
        if (usersBanned.containsKey(name)) {
            String message = "user [" + name + "] got ban";
            return ResponseEntity.badRequest().body(message);
        }
        usersOnline.put(name, name);
        messages.add("[" + name + "] logged in");
        return ResponseEntity.ok().build();
    }

    /**
     * curl -i localhost:8080/api/chat/online
     */
    @RequestMapping(
            path = "online",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> online() {
        String responseBody = usersOnline.keySet().stream().sorted().collect(Collectors.joining("\n"));
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/api/chat/logout -d "name=I_AM_STUPID"
     */
    @PostMapping(
            path = "logout",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> logout(@RequestParam("name") String name) {
        if (usersOnline.containsKey(name)) {
            String responseBody = usersOnline.remove(name);
            messages.add("[" + name + "] logged out");
            return ResponseEntity.ok(responseBody);
        } else {
            String message = "user [" + name + "] not found";
            messages.add(message);
            return ResponseEntity.badRequest().body(message);
        }
    }

    /**
     * curl -X POST -i localhost:8080/api/chat/say -d "name=I_AM_STUPID&msg=Hello everyone in this chat"
     */
    @PostMapping(
            path = "say",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    public ResponseEntity<String> say(@RequestParam("name") String name, @RequestParam("msg") String msg) {
        if (usersBanned.containsKey(name)) {
            String message = "user [" + name + "] got ban";
            return ResponseEntity.badRequest().body(message);
        } else if (!usersOnline.containsKey(name)) {
            String message = "user [" + name + "] not found";
            return ResponseEntity.badRequest().body(message);
        } else {
            messages.add("[" + name + "]: " + msg);
            return ResponseEntity.ok().body("[" + name + "]: " + msg);
        }
    }


    /**
     * curl -i localhost:8080/api/chat/chat
     */
    @GetMapping(
            path = "chat",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> chat() {
        String responseBody = String.join("\n", messages);
        return ResponseEntity.ok(responseBody);
    }

    /**
     * curl -X POST -i localhost:8080/api/chat/ban -d "name=I_AM_STUPID"
     */
    @PostMapping(
            path = "ban",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> ban(@RequestParam("name") String name) {
        if (usersBanned.containsKey(name)) {
            String message = "user [" + name + "] already banned";
            return ResponseEntity.badRequest().body(message);
        } else if (!usersOnline.containsKey(name)) {
            String message = "user [" + name + "] not found";
            messages.add(message);
            return ResponseEntity.badRequest().body(message);
        } else {
            usersBanned.put(name, name);
            usersOnline.remove(name, name);
            messages.add(name + " was banned");
            return ResponseEntity.ok().build();
        }
    }

    /**
     * curl -X POST -i localhost:8080/api/chat/ban -d "name=I_AM_STUPID"
     */
    @PostMapping(
            path = "unban",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> unban(@RequestParam("name") String name) {
        if (!usersBanned.containsKey(name)) {
            String message = "user [" + name + "] wasn't banned";
            return ResponseEntity.badRequest().body(message);
        } else {
            usersBanned.remove(name);
            messages.add(name + " was unbanned");
            return ResponseEntity.ok().build();
        }
    }

    /**
     * curl -i localhost:8080/api/chat/chat
     */
    @GetMapping(
            path = "clear",
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> clear() {
        messages.clear();
        String responseBody = "Чат пуст";
        return ResponseEntity.ok(responseBody);
    }
}
