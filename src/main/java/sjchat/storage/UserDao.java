package sjchat.storage;

import sjchat.restapi.entities.User;

/**
 * Created by Johan Vikström on 2017-04-01.
 */
public interface UserDao extends Dao{
    public User findByUsername(String username);
    public User findById(long id);
}
