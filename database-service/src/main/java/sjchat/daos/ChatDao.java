package sjchat.daos;

import sjchat.entities.ChatEntity;

/**
 * Created by jovi on 2017-05-09.
 */
public interface ChatDao extends Dao {
    public ChatEntity find(String id);
}
