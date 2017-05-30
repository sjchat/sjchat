package sjchat.queue.workitem;

import sjchat.messages.Message;

public class MessageWorkItem extends WorkItem {
  private final static String MESSAGE_ID_KEY = "message_id";
  private final static String MESSAGE_CONTENT_KEY = "message_content";
  private final static String MESSAGE_SENDER_KEY = "message_sender";
  private final static String MESSAGE_CHAT_KEY = "message_chat";

  public Message getMessage() {
    Message.Builder messageBuilder = Message.newBuilder();
    messageBuilder.setId((String) getDataValue(MESSAGE_ID_KEY, String.class));
    messageBuilder.setMessage((String) getDataValue(MESSAGE_CONTENT_KEY, String.class));
    messageBuilder.setSender((String) getDataValue(MESSAGE_SENDER_KEY, String.class));
    messageBuilder.setChat((String) getDataValue(MESSAGE_CHAT_KEY, String.class));

    return messageBuilder.build();
  }

  public void setMessage(Message message) {
    getDataObject().put(MESSAGE_ID_KEY, message.getId());
    getDataObject().put(MESSAGE_CONTENT_KEY, message.getMessage());
    getDataObject().put(MESSAGE_SENDER_KEY, message.getSender());
    getDataObject().put(MESSAGE_CHAT_KEY, message.getChat());
  }
}
