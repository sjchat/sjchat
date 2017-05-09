package sjchat.daos;

import sjchat.entities.MessageEntity;

import java.util.List;

/**
 * Created by jovi on 2017-05-09.
 */
public interface MessageDao extends Dao{
    MessageEntity find(String id);
    List<MessageEntity> getInChat(String chatId);
}
