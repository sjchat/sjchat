package sjchat.daos;

import sjchat.exceptions.NoEntityExistsException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

/**
 * Created by Johan Vikström on 2017-04-01.
 */
public class DaoImpl implements Dao{
    private final String PU = "mongo_pu";
    protected EntityManagerFactory emf;
    protected EntityManager em;

    public DaoImpl(){
        emf = Persistence.createEntityManagerFactory(PU);
        em = emf.createEntityManager();
    }

    public void create(Object o){
        em.persist(o);
    }
    public void remove(Object o){
        em.remove(o);
    }
    public <T> T update(T o) throws NoEntityExistsException{
        //TODO: Implement
        return em.merge(o);
    }
    public <T> T find(Class<T> aClass, Object id){
        return em.find(aClass, id);
    }
}
