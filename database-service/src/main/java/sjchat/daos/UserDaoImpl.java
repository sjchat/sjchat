package sjchat.daos;

import sjchat.entities.UserEntity;

import javax.persistence.Query;
import java.util.List;


/**
 * Created by Johan Vikström on 2017-04-01.
 */
public class UserDaoImpl extends DaoImpl implements UserDao {

  public UserEntity findByUsername(String username) {
    Query q = em
        .createQuery("select u from UserEntity u where u.username = :username", UserEntity.class);
    q.setParameter("username", username);
    List<UserEntity> users = q.getResultList();
    if (users == null || users.isEmpty()) {
      return null;
    }
    return users.get(0);
  }

  public UserEntity findById(String id) {
    return em.find(UserEntity.class, id);
  }

  public UserEntity find(String id){
    return super.find(UserEntity.class, id);
  }
}
