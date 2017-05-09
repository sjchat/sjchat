package sjchat.restapi.controllers;

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
import sjchat.messages.CreateChatRequest;
import sjchat.messages.CreateChatResponse;
import sjchat.messages.GetChatListRequest;
import sjchat.messages.GetChatListResponse;
import sjchat.messages.GetChatRequest;
import sjchat.messages.GetChatResponse;
import sjchat.messages.GetMessagesRequest;
import sjchat.messages.GetMessagesResponse;
import sjchat.messages.MessageServiceGrpc;
import sjchat.messages.SendMessageRequest;
import sjchat.messages.SendMessageResponse;
import sjchat.messages.UpdateChatRequest;
import sjchat.messages.UpdateChatResponse;
import sjchat.restapi.entities.Chat;
import sjchat.restapi.entities.ChatRequest;
import sjchat.restapi.entities.Message;
import sjchat.restapi.entities.User;
import sjchat.users.CreateUserRequest;
import sjchat.users.CreateUserResponse;
import sjchat.users.GetUserRequest;
import sjchat.users.GetUserResponse;
import sjchat.users.UpdateUserRequest;
import sjchat.users.UpdateUserResponse;
import sjchat.users.UserServiceGrpc;

@RestController
public class ChatController {
  private Channel messageServiceChannel;
  private Channel userServiceChannel;

  public ChatController() {
    messageServiceChannel = buildMessageServiceChannel();
    userServiceChannel = buildUserServiceChannel();
  }

  private static Channel buildMessageServiceChannel() {
    String host = System.getenv("MESSAGE_SERVICE_HOST");
    host = (host == null) ? "localhost" : host;

    return ManagedChannelBuilder.forAddress(host, 50052).usePlaintext(true).build(); //TODO: Put port in config file
  }

  private static Channel buildUserServiceChannel() {
    String host = System.getenv("USER_SERVICE_HOST");
    host = (host == null) ? "localhost" : host;

    return ManagedChannelBuilder.forAddress(host, 50051).usePlaintext(true).build(); //TODO: Put port in config file
  }

  private static Chat buildChatFromResponse(sjchat.messages.Chat responseChat) {
    Chat chat = new Chat();
    chat.setId(responseChat.getId());
    chat.setTitle(responseChat.getTitle());

    List<sjchat.users.User> chatUsers = responseChat.getParticipantsList();
    for (sjchat.users.User chatUser : chatUsers) {
      User user = buildUserFromResponse(chatUser);
      chat.addParticipant(user);
    }

    return chat;
  }

  private static Message buildMessageFromResponse(sjchat.messages.Message responseMessage) {
    Message message = new Message();
    message.setId(responseMessage.getId());
    message.setMessage(responseMessage.getMessage());
    message.setUser(responseMessage.getSender());

    return message;
  }

  private static User buildUserFromResponse(sjchat.users.User responseUser) {
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

    List<Chat> chatList = new ArrayList<>();

    GetChatListResponse response = blockingStub.getChatList(GetChatListRequest.newBuilder().build());
    for (sjchat.messages.Chat responseChat : response.getChatsList()) {
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

    CreateChatRequest.Builder chatDataRequestBuilder = CreateChatRequest.newBuilder();
    chatDataRequestBuilder.setTitle(chatRequest.getTitle());
    for (String userId : chatRequest.getParticipants()) {
      chatDataRequestBuilder.addParticipants(userId);
    }

    CreateChatResponse response = blockingStub.createChat(chatDataRequestBuilder.build());
    sjchat.messages.Chat responseChat = response.getChat();
    Chat chat = buildChatFromResponse(responseChat);

    return new ResponseEntity<>(chat, HttpStatus.CREATED);
  }

  @RequestMapping(
          value = "/chat/{chatId}",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> getChat(@PathVariable String chatId) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    GetChatResponse response = blockingStub.getChat(GetChatRequest.newBuilder().setId(chatId).build());
    sjchat.messages.Chat responseChat = response.getChat();
    Chat chat = buildChatFromResponse(responseChat);
    return new ResponseEntity<>(chat, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}",
          method = RequestMethod.PUT,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<Chat> updateChat(@PathVariable String chatId, @RequestBody ChatRequest chatRequest) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    UpdateChatRequest.Builder chatDataRequestBuilder = UpdateChatRequest.newBuilder();
    chatDataRequestBuilder.setId(chatId);
    chatDataRequestBuilder.setTitle(chatRequest.getTitle());
    for (String userId : chatRequest.getParticipants()) {
      chatDataRequestBuilder.addParticipants(userId);
    }

    UpdateChatResponse response = blockingStub.updateChat(chatDataRequestBuilder.build());
    sjchat.messages.Chat responseChat = response.getChat();
    Chat chat = buildChatFromResponse(responseChat);

    return new ResponseEntity<>(chat, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/chat/{chatId}/message",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<List<Message>> getMessagesList(@PathVariable String chatId) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    ArrayList<Message> messageList = new ArrayList<>();

    GetMessagesResponse response = blockingStub.getMessages(GetMessagesRequest.newBuilder().setChatId(chatId).build());
    for (sjchat.messages.Message responseMessage : response.getMessagesList()) {
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
  public ResponseEntity<Message> createMessage(@PathVariable String chatId, @RequestBody Message messageRequest) {
    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    SendMessageRequest.Builder messageRequestBuilder = SendMessageRequest.newBuilder();
    messageRequestBuilder.setMessage(messageRequest.getMessage()).setChatId(chatId).setSender(messageRequest.getSender());

    SendMessageResponse response = blockingStub.sendMessage(messageRequestBuilder.build());
    sjchat.messages.Message responseMessage = response.getMessage();
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

    CreateUserRequest.Builder userDataRequestBuilder = CreateUserRequest.newBuilder();
    userDataRequestBuilder.setUsername(userRequest.getUsername());

    CreateUserResponse response = blockingStub.createUser(userDataRequestBuilder.build());
    sjchat.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @RequestMapping(
          value = "/user/{userId}",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<User> getUser(@PathVariable String userId) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);

    GetUserResponse response = blockingStub.getUser(GetUserRequest.newBuilder().setId(userId).build());
    sjchat.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @RequestMapping(
          value = "/user/{userId}",
          method = RequestMethod.PUT,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<User> updateUser(@PathVariable String userId, @RequestBody User userRequest) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(userServiceChannel);

    UpdateUserRequest.Builder userDataRequestBuilder = UpdateUserRequest.newBuilder();
    userDataRequestBuilder.setId(userId);
    userDataRequestBuilder.setUsername(userRequest.getUsername());

    UpdateUserResponse response = blockingStub.updateUser(userDataRequestBuilder.build());
    sjchat.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
