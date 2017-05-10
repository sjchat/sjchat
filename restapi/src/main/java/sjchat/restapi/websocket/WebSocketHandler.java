package sjchat.restapi.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import sjchat.restapi.websocket.controllers.WebSocketController;
import sjchat.restapi.websocket.models.AbstractAction;
import sjchat.restapi.websocket.models.AbstractResponse;
import sjchat.restapi.websocket.models.EnrollUserAction;
import sjchat.restapi.websocket.models.ErrorResponse;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
  private static final Map<String, Class> actionMapping;

  static {
    Map<String, Class> map = new HashMap<>();
    map.put("enrollUser", EnrollUserAction.class);
    actionMapping = Collections.unmodifiableMap(map);
  }

  private WebSocketController webSocketController;

  public WebSocketHandler() throws Exception {
    webSocketController = new WebSocketController();
  }

  private static String parseAction(String jsonString, ObjectMapper objectMapper) throws Exception {
    JsonNode rootObject = objectMapper.readTree(jsonString);
    if (rootObject.has("action")) {
      JsonNode actionNode = rootObject.get("action");
      if (actionNode.isValueNode()) {
        return actionNode.textValue();
      }
    }
    return null;
  }

  @Override
  public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
    System.out.println("error occurred at sender " + session);
    System.out.println("Exception " + throwable);
  }

  @Override
  public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    webSocketController.sessionClosed(session);
    System.out.println(String.format("Session %s closed because of %s", session.getId(), status.getReason()));
  }

  @Override
  public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    System.out.println("Connected  " + session.getId());
  }

  @Override
  protected void handleTextMessage(WebSocketSession session, TextMessage jsonTextMessage) throws Exception {
    System.out.println("message received: " + jsonTextMessage.getPayload());
    String jsonString = jsonTextMessage.getPayload();
    ObjectMapper mapper = new ObjectMapper();
    String action = parseAction(jsonString, mapper);

    String jsonResponse;
    if (action == null || !actionMapping.containsKey(action)) {
      ErrorResponse response = new ErrorResponse("Bad or missing action");
      jsonResponse = mapper.writeValueAsString(response);
    } else {
      Class<AbstractAction> actionClass = actionMapping.get(action);
      AbstractAction actionInstance = mapper.readValue(jsonString, actionClass);
      Method method = WebSocketController.class.getDeclaredMethod(action, actionClass, WebSocketSession.class);
      AbstractResponse response = (AbstractResponse) method.invoke(webSocketController, actionInstance, session);
      jsonResponse = mapper.writeValueAsString(response);
    }

    sendMessageTextMessage(jsonResponse, session);
  }

  public static void sendMessageTextMessage(String message, WebSocketSession session) throws IOException {
    TextMessage msg = new TextMessage(message);
    session.sendMessage(msg);
  }
}