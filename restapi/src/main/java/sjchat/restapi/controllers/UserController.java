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
import sjchat.messages.ChatDataRequest;
import sjchat.messages.ChatListRequest;
import sjchat.messages.ChatListResponse;
import sjchat.messages.ChatResponse;
import sjchat.messages.MessageListRequest;
import sjchat.messages.MessageListResponse;
import sjchat.messages.MessageResponse;
import sjchat.messages.MessageServiceGrpc;
import sjchat.messages.NewMessageRequest;
import sjchat.restapi.ChannelManager;
import sjchat.restapi.entities.Chat;
import sjchat.restapi.entities.ChatRequest;
import sjchat.restapi.entities.Message;
import sjchat.restapi.entities.User;
import sjchat.users.UserDataRequest;
import sjchat.users.UserRequest;
import sjchat.users.UserResponse;
import sjchat.users.UserServiceGrpc;

@RestController
public class UserController {
  @RequestMapping(
          value = "/user",
          method = RequestMethod.POST,
          produces = "application/json",
          consumes = "application/json")
  @ResponseBody
  public ResponseEntity<User> createUser(@RequestBody User userRequest) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(ChannelManager.getInstance().getUserServiceChannel());

    UserDataRequest.Builder userDataRequestBuilder = UserDataRequest.newBuilder();
    userDataRequestBuilder.setUsername(userRequest.getUsername());

    UserResponse response = blockingStub.createUser(userDataRequestBuilder.build());
    sjchat.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @RequestMapping(
          value = "/user/{userId}",
          method = RequestMethod.GET,
          produces = "application/json")
  @ResponseBody
  public ResponseEntity<User> getUser(@PathVariable long userId) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(ChannelManager.getInstance().getUserServiceChannel());

    UserResponse response = blockingStub.getUser(UserRequest.newBuilder().setId(userId).build());
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
  public ResponseEntity<User> updateUser(@PathVariable long userId, @RequestBody User userRequest) {
    final UserServiceGrpc.UserServiceBlockingStub blockingStub = UserServiceGrpc.newBlockingStub(ChannelManager.getInstance().getUserServiceChannel());

    UserDataRequest.Builder userDataRequestBuilder = UserDataRequest.newBuilder();
    userDataRequestBuilder.setId(userId);
    userDataRequestBuilder.setUsername(userRequest.getUsername());

    UserResponse response = blockingStub.createUser(userDataRequestBuilder.build());
    sjchat.users.User responseUser = response.getUser();
    User user = buildUserFromResponse(responseUser);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }
}
