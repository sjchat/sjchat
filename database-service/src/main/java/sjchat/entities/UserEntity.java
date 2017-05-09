package sjchat.entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "users")
public class UserEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private String id;
  @Column(name = "username")
  private String username;
  @Column(name = "password")
  private String password;
  @Column(name = "last_forced_logout")
  private Date lastForecdLogout;

  public UserEntity() {
  }

  public UserEntity(String id, String username) {
    this.id = id;
    this.username = username;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Date getLastForcedLogout() {
    return lastForecdLogout;
  }

  public void setLastForcedLogout(Date newDate) {
    this.lastForecdLogout = newDate;
  }

}
