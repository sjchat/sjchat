package sjchat.users.entities;

public class User {
    
    public final String username;
    public String password;
    public String jws;
    public long id;

    public User(String username, String password, String jws, long id) {
        this.username = username;
        this.password = password;
        this.jws = jws;
        this.id = id;
    }


    public User(String username, String password, long id) {
        this.username = username;
        this.password = password;
        this.id = id;
    }

    public User(String username, long id) {
        this.username = username;
        this.id = id;
    }

    public User(String username) {
        this.username = username;
    }

}

