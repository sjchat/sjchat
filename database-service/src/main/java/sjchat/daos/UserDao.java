package sjchat.daos;

import sjchat.entities.UserEntity;

/**
 * Created by Johan Vikström on 2017-04-01.
 */
public interface UserDao extends Dao {

  UserEntity findByUsername(String username);

  UserEntity findById(String id);

  UserEntity find(String id);
}
