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
import sjchat.backend.messages.ChatDataRequest;
import sjchat.backend.messages.MessageServiceGrpc;
import sjchat.backend.messages.NewMessageRequest;
import sjchat.backend.restapi.entities.Chat;
import sjchat.backend.restapi.entities.ChatRequest;
import sjchat.backend.restapi.entities.Message;
import sjchat.backend.restapi.entities.User;
import sjchat.backend.users.UserDataRequest;
import sjchat.backend.users.UserServiceGrpc;

@RestController
public class ChatController {
  private Channel messageServiceChannel;
  private Channel userServiceChannel;

  public ChatController() {
    messageServiceChannel = buildMessageServiceChannel();
    userServiceChannel = buildUserServiceChannel();
  }

  private static Channel buildMessageServiceChannel() {
    return ManagedChannelBuilder.forAddress("localhost", 50052).usePlaintext(true).build(); //TODO: Put port in config file
  }

  private static Channel buildUserServiceChannel() {
    return ManagedChannelBuilder.forAddress("localhost", 50051).usePlaintext(true).build(); //TODO: Put port in config file
  }

  private static Chat buildChatFromResponse(sjchat.backend.messages.Chat responseChat) {
    Chat chat = new Chat();
    chat.setId(responseChat.getId());
    chat.setTitle(responseChat.getTitle());

    List<sjchat.backend.users.User> chatUsers = responseChat.getUsersList();
    for (sjchat.backend.users.User chatUser : chatUsers) {
      User user = buildUserFromResponse(chatUser);
      chat.addUser(user);
    }

    return chat;
  }

  private static Message buildMessageFromResponse(sjchat.backend.messages.Message responseMessage) {
    Message message = new Message();
    message.setId(responseMessage.getId());
    message.setMessage(responseMessage.getMessage());
    message.setUser(responseMessage.getUser());

    return message;
  }

  private static User buildUserFromResponse(sjchat.backend.users.User responseUser) {
    User user = new User();
    user.setId(responseUser.getId());
    user.setUsername(responseUser.getUsername());

    return user;
  }

  @RequestMapping(
          value = "/chat",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<List<Chat>> getChatList() {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    ArrayList<Chat> chatList = new ArrayList<>();

    sjchat.backend.messages.ChatListResponse response = blockingStub.getChatList(sjchat.backend.messages.ChatListRequest.newBuilder().build());
    for (sjchat.backend.messages.Chat responseChat : response.getChatsList()) {
      Chat chat = buildChatFromResponse(responseChat);
      chatList.add(chat);
    }

    return new ResponseEntity<>(chatList, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat",
          method = RequestMethod.POST,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> createChat(@RequestBody ChatRequest chatRequest) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    ChatDataRequest.Builder chatDataRequestBuilder = ChatDataRequest.newBuilder();
    chatDataRequestBuilder.setTitle(chatRequest.getTitle());
    for (long userId : chatRequest.getUsers()) {
      chatDataRequestBuilder.addUsers(userId);
    }

    sjchat.backend.messages.ChatResponse response = blockingStub.createChat(chatDataRequestBuilder.build());
    sjchat.backend.messages.Chat responseChat = response.getChat();
    Chat chat = buildChatFromResponse(responseChat);

    return new ResponseEntity<>(chat, HttpStatus.CREATED);
  }

  @RequestMapping(
          value = "/chat/{chatId}",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> getChat(@PathVariable long chatId) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    sjchat.backend.messages.ChatResponse response = blockingStub.getChat(sjchat.backend.messages.ChatRequest.newBuilder().setId(chatId).build());
    sjchat.backend.messages.Chat responseChat = response.getChat();
    Chat chat = buildChatFromResponse(responseChat);
    return new ResponseEntity<>(chat, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}",
          method = RequestMethod.PUT,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> updateChat(@PathVariable long chatId, @RequestBody ChatRequest chatRequest) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    ChatDataRequest.Builder chatDataRequestBuilder = ChatDataRequest.newBuilder();
    chatDataRequestBuilder.setId(chatId);
    chatDataRequestBuilder.setTitle(chatRequest.getTitle());
    for (long userId : chatRequest.getUsers()) {
      chatDataRequestBuilder.addUsers(userId);
    }

    sjchat.backend.messages.ChatResponse response = blockingStub.updateChat(chatDataRequestBuilder.build());
    sjchat.backend.messages.Chat responseChat = response.getChat();
    Chat chat = buildChatFromResponse(responseChat);

    return new ResponseEntity<>(chat, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}/message",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<List<Message>> getMessagesList(@PathVariable long chatId) {
   final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    ArrayList<Message> messageList = new ArrayList<>();

    sjchat.backend.messages.MessageListResponse response = blockingStub.getMessages(sjchat.backend.messages.MessageListRequest.newBuilder().setChatId(chatId).build());
    for (sjchat.backend.messages.Message responseMessage : response.getMessagesList()) {
      Message message = buildMessageFromResponse(responseMessage);
      messageList.add(message);
    }

    return new ResponseEntity<>(messageList, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}/message",
          method = RequestMethod.POST,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Message> createMessage(@PathVariable long chatId, @RequestBody Message messageRequest) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    NewMessageRequest.Builder messageRequestBuilder = NewMessageRequest.newBuilder();
    messageRequestBuilder.setMessage(messageRequest.getMessage());

    sjchat.backend.messages.MessageResponse response = blockingStub.sendMessage(messageRequestBuilder.build());
    sjchat.backend.messages.Message responseMessage = response.getMessage();
    Message message = buildMessageFromResponse(responseMessage);

    return new ResponseEntity<>(message, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/user",
          method = RequestMethod.POST,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<User> createUser(@RequestBody User userRequest) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);

    UserDataRequest.Builder userDataRequestBuilder = UserDataRequest.newBuilder();
    userDataRequestBuilder.setUsername(userRequest.getUsername());

    sjchat.backend.users.UserResponse response = blockingStub.createUser(userDataRequestBuilder.build());
    sjchat.backend.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @RequestMapping(
          value = "/user/{userId}",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<User> getUser(@PathVariable long userId) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);

    sjchat.backend.users.UserResponse response = blockingStub.getUser(sjchat.backend.users.UserRequest.newBuilder().setId(userId).build());
    sjchat.backend.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/user/{userId}",
          method = RequestMethod.PUT,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<User> updateUser(@PathVariable long userId, @RequestBody User userRequest) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);

    UserDataRequest.Builder userDataRequestBuilder = UserDataRequest.newBuilder();
    userDataRequestBuilder.setId(userId);
    userDataRequestBuilder.setUsername(userRequest.getUsername());

    sjchat.backend.users.UserResponse response = blockingStub.createUser(userDataRequestBuilder.build());
    sjchat.backend.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}