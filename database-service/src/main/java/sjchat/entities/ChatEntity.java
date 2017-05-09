package sjchat.entities;

import javax.persistence.*;
import java.util.ArrayList;
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

    public ChatEntity(){}
    public ChatEntity(String id, String title, List<String> participantIds){
        this.id = id;
        this.title = title;
        this.participantIds = participantIds;
    }

    public static class Builder{
        private String id;
        private String title;
        private List<String> participantIds = new ArrayList<String>();
        public Builder setId(String id){
            this.id = id;
            return this;
        }
        public Builder setTitle(String title){
            this.title = title;
            return this;
        }
        public Builder addParticipant(String id){
            participantIds.add(id);
            return this;
        }
        public Builder setParticipants(List<String> ids){
            participantIds = ids;
            return this;
        }
        public ChatEntity build(){
            return new ChatEntity(id, title, participantIds);
        }
    }

    public String getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public List<String> getParticipants(){
        return participantIds;
    }
}
