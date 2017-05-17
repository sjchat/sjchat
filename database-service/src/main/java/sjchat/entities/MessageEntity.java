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
    @Column(name="created_at")
    private long createdAt;

    public MessageEntity(){}
    public MessageEntity(String id, String chatid, String message, String sender, long createdAt){
        this.id = id;
        this.chatid = chatid;
        this.message = message;
        this.sender = sender;
        this.createdAt = createdAt;
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
    public long getCreatedAt(){
        return createdAt;
    }
}
