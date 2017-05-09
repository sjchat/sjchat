package sjchat.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by jovi on 2017-05-09.
 */
@Entity
@Table(name="messages")
public class MessageEntity {
    @Id
    private String id;
    @Column(name="chatid")
    private String chatId;
    @Column(name="message")
    private String message;
    @Column(name="sender")
    private String sender;

    public String getMessage(){
        return message;
    }
    public String getSender(){
        return sender;
    }
    public String getChatId(){
        return chatId;
    }
    public String getId(){
        return id;
    }
}
