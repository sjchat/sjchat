package sjchat.daos;

import sjchat.entities.MessageEntity;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by jovi on 2017-05-09.
 */
public class MessageDaoImpl extends DaoImpl implements MessageDao {
    public List<MessageEntity> getInChat(String chatId){
        Query q = em.createQuery("select m from MessageEntity m where m.chatid = :chatid", MessageEntity.class);
        q.setParameter("chatid", chatId);
        return q.getResultList();
    }
    public MessageEntity find(String id){
        return super.find(MessageEntity.class, id);
    }
}
