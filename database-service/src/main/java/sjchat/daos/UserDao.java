package sjchat.daos;

import sjchat.entities.UserEntity;

/**
 * Created by Johan Vikström on 2017-04-01.
 */
public interface UserDao extends Dao{
    public UserEntity findByUsername(String username);
    public UserEntity findById(String id);
}
