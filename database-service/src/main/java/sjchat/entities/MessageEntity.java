package sjchat.entities;

import javax.persistence.*;

/**
 * Created by jovi on 2017-05-09.
 */
@Entity
@Table(name="messages")
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(name="chatid")
    private String chatid;
    @Column(name="message")
    private String message;
    @Column(name="sender")
    private String sender;

    public MessageEntity(){}
    public MessageEntity(String id, String chatid, String message, String sender){
        this.id = id;
        this.chatid = chatid;
        this.message = message;
        this.sender = sender;
    }

    public String getMessage(){
        return message;
    }
    public String getSender(){
        return sender;
    }
    public String getChatid(){
        return chatid;
    }
    public String getId(){
        return id;
    }
}
