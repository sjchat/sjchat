package sjchat.restapi;

import org.apache.catalina.authenticator.jaspic.AuthConfigFactoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import javax.security.auth.message.config.AuthConfigFactory;

@SpringBootApplication(exclude = {MongoAutoConfiguration.class, MongoDataAutoConfiguration.class})
public class Application {

  public static void main(String[] args) {
    if (AuthConfigFactory.getFactory() == null) {
      AuthConfigFactory.setFactory(new AuthConfigFactoryImpl());
    }
    SpringApplication.run(Application.class, args);
  }
}