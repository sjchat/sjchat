package sjchat.users;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import sjchat.daos.UserDao;
import sjchat.entities.UserEntity;
import sjchat.general.testutils.BasicStreamObserverImpl;

/**
 * Created by jovi on 2017-07-02.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
  @Mock
  private UserDao dao;

  private UserService userService;
  @Before
  public void setUp() throws Exception {
    userService = new UserService(dao);
    when(dao.findByUsername("user1")).thenReturn(new UserEntity("user1-id", "user1"));
    when(dao.find("user1-id")).thenReturn(new UserEntity("user1-id", "user1"));
  }

  @Test
  public void cantCreateAlreadyExistingUser() throws Exception{
    BasicStreamObserverImpl<CreateUserResponse> streamObserver = new BasicStreamObserverImpl<CreateUserResponse>();
    userService.createUser(CreateUserRequest.newBuilder().setUsername("user1").build(), streamObserver);
    assertNull(streamObserver.getLastEntity());
    assertEquals("User already exists", streamObserver.getLastThrowable().getMessage());
  }

  @Test
  public void getByUsername() throws Exception {
    BasicStreamObserverImpl<GetByUsernameResponse> streamObserver = new BasicStreamObserverImpl<GetByUsernameResponse>();
    userService.getByUsername(GetByUsernameRequest.newBuilder().setUsername("user1").build(), streamObserver);
    assertNull(streamObserver.getLastThrowable());
    assertEquals("user1", streamObserver.getLastEntity().getUser().getUsername());
    assertEquals("user1-id", streamObserver.getLastEntity().getUser().getId());
  }

  @Test
  public void getNonExistingByUsername() throws Exception{
    BasicStreamObserverImpl<GetByUsernameResponse> streamObserver = new BasicStreamObserverImpl<GetByUsernameResponse>();
    userService.getByUsername(GetByUsernameRequest.newBuilder().setUsername("user2").build(), streamObserver);
    assertNull(streamObserver.getLastEntity());
    assertEquals("User does not exist", streamObserver.getLastThrowable().getMessage());
  }

  @Test
  public void getUser() throws Exception {
    BasicStreamObserverImpl<GetUserResponse> streamObserver = new BasicStreamObserverImpl<GetUserResponse>();
    userService.getUser(GetUserRequest.newBuilder().setId("user1-id").build(), streamObserver);
    assertNull(streamObserver.getLastThrowable());
    assertEquals("user1-id", streamObserver.getLastEntity().getUser().getId());
    assertEquals("user1", streamObserver.getLastEntity().getUser().getUsername());
  }

  @Test
  public void getNonExistentUser() throws Exception{
    BasicStreamObserverImpl<GetUserResponse> streamObserver = new BasicStreamObserverImpl<GetUserResponse>();
    userService.getUser(GetUserRequest.newBuilder().setId("user2-id").build(), streamObserver);
    assertNull(streamObserver.getLastEntity());
    assertEquals("User does not exist", streamObserver.getLastThrowable().getMessage());
  }
}