package sjchat.storage;

/**
 * Created by Johan Vikström on 2017-04-01.
 */
public interface Dao {
    public void create(Object o);
    public void remove(Object o);
    public <T> T update(T o);
    public <T> T find(Class<T> aClass, Object id);
}
