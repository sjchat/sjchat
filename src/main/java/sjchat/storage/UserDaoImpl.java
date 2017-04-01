package sjchat.storage;

import sjchat.restapi.entities.User;

import javax.persistence.Query;
import java.util.List;


/**
 * Created by Johan Vikström on 2017-04-01.
 */
public class UserDaoImpl extends DaoImpl implements UserDao{
    public User findByUsername(String username){
        Query q = em.createQuery("select u from User where u.username = :username", User.class);
        q.setParameter("username", username);
        List<User> users = q.getResultList();
        if(users == null || users.isEmpty()){
            return null;
        }else{
            return users.get(0);
        }
    }

    public User findById(long id){
        return em.find(User.class, id);
    }
}
