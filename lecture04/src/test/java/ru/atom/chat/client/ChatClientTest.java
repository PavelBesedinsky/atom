package ru.atom.chat.client;

import okhttp3.Response;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.atom.ChatApplication;

import java.io.IOException;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ChatApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatClientTest {
    private static String MY_NAME_IN_CHAT = "I_AM_STUPID";
    private static String MY_MESSAGE_TO_CHAT = "SOMEONE_KILL_ME";

    @Test
    public void login() throws IOException {
        Response response = ChatClient.login(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        String body = Objects.requireNonNull(response.body()).string();
        System.out.println();
        Assert.assertTrue(response.code() == 200 || body.equals("Already logged in:("));
    }

    @Test
    public void viewChat() throws IOException {
        Response response = ChatClient.viewChat();
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(200, response.code());
    }


    @Test
    public void viewOnline() throws IOException {
        Response response = ChatClient.viewOnline();
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void say() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void sayFail() throws IOException {
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(400, response.code());
    }

    @Test
    public void logout() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.logout(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void ban() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.ban(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(200, response.code());
        ChatClient.unban(MY_NAME_IN_CHAT);
    }

    @Test
    public void banAndSayFail() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        ChatClient.ban(MY_NAME_IN_CHAT);
        Response response = ChatClient.say(MY_NAME_IN_CHAT, MY_MESSAGE_TO_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(400, response.code());
    }

    @Test
    public void unban() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        ChatClient.ban(MY_NAME_IN_CHAT);
        Response response = ChatClient.unban(MY_NAME_IN_CHAT);
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(200, response.code());
    }

    @Test
    public void clear() throws IOException {
        ChatClient.login(MY_NAME_IN_CHAT);
        Response response = ChatClient.clear();
        System.out.println("[" + response + "]");
        System.out.println(Objects.requireNonNull(response.body()).string());
        Assert.assertEquals(200, response.code());
    }
}
