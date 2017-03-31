package sjchat.backend.restapi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import sjchat.backend.messages.MessageServiceGrpc;
import sjchat.backend.restapi.entities.Chat;
import sjchat.backend.restapi.entities.ChatRequest;
import sjchat.backend.restapi.entities.Message;
import sjchat.backend.restapi.entities.User;

@RestController
public class ChatController {

  @RequestMapping(
          value = "/chat",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<List<Chat>> getChatList() {
    //TODO: Fetch chats from message service
    //TODO: Parse and response from message service

    User user1 = new User();
    user1.setId(3);
    user1.setUsername("first_user");

    User user2 = new User();
    user2.setId(4);
    user2.setUsername("another_user");

    User user3 = new User();
    user3.setId(6);
    user3.setUsername("third_user");

    ArrayList<Chat> chatList = new ArrayList<>();

    Chat chat1 = new Chat();
    chat1.setId(1);
    chat1.setTitle("First chat");
    chat1.addUser(user1);
    chat1.addUser(user2);
    chat1.addUser(user3);

    Chat chat2 = new Chat();
    chat2.setId(1);
    chat2.setTitle("Another chat");
    chat2.addUser(user1);
    chat2.addUser(user3);

    return new ResponseEntity<>(chatList, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat",
          method = RequestMethod.POST,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> createChat(@RequestBody ChatRequest chatRequest) {
    //TODO: Send request to message service to create chat
    //TODO: Parse response from message service

    Chat chat = new Chat();
    chat.setTitle(chatRequest.getTitle());
    for (long userId : chatRequest.getUsers()) {
      User user = new User();
      user.setId(userId);
      chat.addUser(user);
    }

    return new ResponseEntity<>(chat, HttpStatus.CREATED);
  }

  @RequestMapping(
          value = "/chat/{chatId}",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> getChat(@PathVariable long chatId) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = buildMessageServiceStub();

    sjchat.backend.messages.ChatResponse response = blockingStub.getChat(sjchat.backend.messages.ChatRequest.newBuilder().setId(chatId).build());
    sjchat.backend.messages.Chat responseChat = response.getChat();

    Chat chat = new Chat();
    chat.setId(responseChat.getId());
    chat.setTitle(responseChat.getTitle());
    List<sjchat.backend.messages.User> chatUsers = responseChat.getUsersList();
    for (sjchat.backend.messages.User chatUser : chatUsers) {
      User user = new User();
      user.setId(chatUser.getId());
      user.setUsername(chatUser.getUsername());
      chat.addUser(user);
    }

    return new ResponseEntity<>(chat, HttpStatus.OK);
  }

  private static MessageServiceGrpc.MessageServiceBlockingStub buildMessageServiceStub() {
    Channel channel = ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext(true).build(); //TODO: Put port in config file
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(channel);
    return blockingStub;
  }

  @RequestMapping(
          value = "/chat/{chatId}",
          method = RequestMethod.PUT,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> updateChat(@PathVariable long chatId, @RequestBody ChatRequest chatRequest) {
    //TODO: Send request to message service to update chat
    //TODO: Parse response from message service

    Chat chat = new Chat();
    chat.setId(chatId);
    chat.setTitle(chatRequest.getTitle());
    for (long userId : chatRequest.getUsers()) {
      User user = new User();
      user.setId(userId);
      chat.addUser(user);
    }

    return new ResponseEntity<>(chat, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}/message",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<List<Message>> getMessagesList(@PathVariable long chatId) {
    //TODO: Fetch messages from message service
    //TODO: Parse and response from message service

    Message message1 = new Message(1, "First message", 3);
    Message message2 = new Message(2, "Another message", 5);
    ArrayList<Message> messageList = new ArrayList<>();
    messageList.add(message1);
    messageList.add(message2);


    return new ResponseEntity<>(messageList, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}/message",
          method = RequestMethod.POST,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Message> createMessage(@PathVariable long chatId, @RequestBody Message messageRequest) {
    //TODO: Send request to message service to create message
    //TODO: Parse response from message service

    return new ResponseEntity<>(messageRequest, HttpStatus.OK);
  }
}