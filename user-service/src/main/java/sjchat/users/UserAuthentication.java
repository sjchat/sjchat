package sjchat.users;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.security.Key;
import sjchat.users.AuthenticationResult;
import java.util.Date;

class UserAuthentication {

    public static final String issuser = "Sjchat";
    public static final long long expiration_time = 10000000;
    public static final byte[] secret_key = [1, 7, 8, 7, 5, 6, 9];
    
    
    String tokenize(String username) {
   
        return Jwts.builder()
            .setSubject(username)
            .setExpiration(new Date(System.currentTimeMillis() + expiration_time))
            .setIssuer(issuser)
            .signWith(SignatureAlgorithm.HS512, key).compact();
    }

    
     AuthenticationResult authenticate(String token, String username) {
       
       
        try {
            Jwts.parser()
                .requireIssuer(issuser)
                .setSigningKey(secret_key)
                .requireSubject(username)
                .parse(token);

            return new AuthenticationResult(true, null);  
      
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            
            return new AuthenticationResult(false, e.getMessage());
    
        }
    

}
