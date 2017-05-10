package sjchat.restapi.websocket.controllers;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import sjchat.messages.GetChatRequest;
import sjchat.messages.GetChatResponse;
import sjchat.messages.Message;
import sjchat.messages.MessageServiceGrpc;
import sjchat.queue.MessageQueue;
import sjchat.queue.QueueException;
import sjchat.queue.consumer.MessageConsumer;
import sjchat.queue.consumer.MessageConsumerCallback;
import sjchat.restapi.entities.Chat;
import sjchat.restapi.websocket.WebSocketHandler;
import sjchat.restapi.websocket.models.AbstractResponse;
import sjchat.restapi.websocket.models.EnrollUserAction;
import sjchat.restapi.websocket.models.SuccessResponse;
import sjchat.users.User;

public class WebSocketController implements MessageConsumerCallback {
  private Channel messageServiceChannel;
  private final Map<String, WebSocketSession> userSessionMap;

  public WebSocketController() throws Exception {
    messageServiceChannel = buildMessageServiceChannel();

    MessageQueue messageQueue = new MessageQueue();
    try {
      messageQueue.initialize();
    } catch (QueueException exception) {
      System.out.println("Could not initialize message queue");
    }

    MessageConsumer consumer = new MessageConsumer(messageQueue);
    try {
      messageQueue.addConsumer(consumer);
    } catch (QueueException exception) {
      System.out.println("Could not add message consumer to queue");
    }
    consumer.setCallback(this);

    userSessionMap = new HashMap<>();
  }

  private static Channel buildMessageServiceChannel() {
    String host = System.getenv("MESSAGE_SERVICE_HOST");
    host = (host == null) ? "localhost" : host;

    return ManagedChannelBuilder.forAddress(host, 50052).usePlaintext(true).build(); //TODO: Put port in config file
  }

  public AbstractResponse enrollUser(EnrollUserAction action, WebSocketSession session) throws Exception {
    userSessionMap.put(action.getUserId(), session);
    return new SuccessResponse();
  }

  public void sessionClosed(WebSocketSession closedSession) {
    for (Map.Entry<String, WebSocketSession> entry : userSessionMap.entrySet()) {
      if (entry.getValue().getId().equals(closedSession.getId())) {
        userSessionMap.remove(entry.getKey());
        return;
      }
    }
  }

  public void consumeMessage(Message message) {
    String messageString = message.getMessage();
    System.out.println("New message" + messageString);

    final MessageServiceGrpc.MessageServiceBlockingStub blockingStub = MessageServiceGrpc.newBlockingStub(messageServiceChannel);

    String chatId = message.getChat();
    GetChatResponse response = blockingStub.getChat(GetChatRequest.newBuilder().setId(chatId).build());
    sjchat.messages.Chat responseChat = response.getChat();

    List<User> participants = responseChat.getParticipantsList();
    for (User user : participants) {
      String userId = user.getId();
      if (userSessionMap.containsKey(userId)) {
        WebSocketSession session = userSessionMap.get(userId);
        try {
          WebSocketHandler.sendMessageTextMessage(messageString, session);
        } catch (IOException exception) {
          System.out.println("Failed to send message to socket:" +  exception.getMessage());
        }
      }
    }
  }
}
