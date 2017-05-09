package sjchat.daos;

import sjchat.entities.ChatEntity;

import java.util.List;

/**
 * Created by jovi on 2017-05-09.
 */
public interface ChatDao extends Dao {
    ChatEntity find(String id);
    List<ChatEntity> findAll();
}
