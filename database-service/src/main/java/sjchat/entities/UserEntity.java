package sjchat.entities;

import javax.persistence.*;

@Entity
@Table(name="users")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @Column(name="username")
  private String username;
  private String password;

  public UserEntity() {
  }

  public UserEntity(long id, String username) {
    this.id = id;
    this.username = username;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /*public static UserEntity buildUser(sjchat.users.UserEntity usr){
    return new UserEntity(usr.getId(), usr.getUsername());
  }*/
}
