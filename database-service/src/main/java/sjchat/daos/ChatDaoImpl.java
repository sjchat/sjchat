package sjchat.daos;

import sjchat.entities.ChatEntity;

import javax.persistence.Query;

/**
 * Created by jovi on 2017-05-09.
 */
public class ChatDaoImpl extends DaoImpl implements ChatDao{
    public ChatEntity find(String id){
        return super.find(ChatEntity.class, id);
    }

}
