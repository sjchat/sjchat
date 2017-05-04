package sjchat.daos;

import sjchat.exceptions.NoEntityExistsException;

/**
 * Created by Johan Vikström on 2017-04-01.
 */
public interface Dao {

  public void create(Object o);

  public void remove(Object o);

  public <T> T update(T o) throws NoEntityExistsException;

  public <T> T find(Class<T> aClass, Object id);
}
