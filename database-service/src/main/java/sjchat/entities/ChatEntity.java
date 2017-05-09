package sjchat.entities;

import javax.persistence.*;
import java.util.List;

/**
 * Created by jovi on 2017-05-09.
 */
@Entity
@Table(name="chats")
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    @Column(name="title")
    private String title;
    @Column(name="participants")
    private List<String> participantIds;
    @Column(name="messages")
    private List<String> messages;

    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public List<String> getParticipants(){
        return participantIds;
    }
    public List<String> getMessages(){
        return messages;
    }


}
