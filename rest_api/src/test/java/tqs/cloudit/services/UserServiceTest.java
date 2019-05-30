package tqs.cloudit.services;

import java.util.HashSet;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.repositories.AreaRepository;
import tqs.cloudit.repositories.UserRepository;

/**
 *
 * @author fp
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {
    
    @TestConfiguration
    static class UserServiceTestContextConfiguration {
  
        @Bean
        public UserService userService() {
            return new UserService();
        }
    }
    
    private static User user1, user2;
    static {        
        user1 = new User();
        user1.setUsername("joao");
        user1.setPassword("123");
        user1.setName("Joao");
        user1.setEmail("emaidojoao@mail.com");
        user1.setType("Freelancer");
        
        user2 = new User();
        user2.setUsername("filipe");
        user2.setPassword("123");
        user2.setName("Filipe");
        user2.setEmail("emaidofilipe@mail.com");
        user2.setType("Freelancer");
    }
    
    private BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
    
    @InjectMocks
    UserService service;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private AreaRepository areaRepository;

    /**
     * Test of getUserInfoFromUsername method, of class UserService, with an existing user.
     */
    @Test
    public void testGetUserInfoFromExistingUsername() {
        System.out.println("getUserInfoFromExistingUsername");
        
        tqs.cloudit.domain.persistance.User user01 = new tqs.cloudit.domain.persistance.User(user1);
                
        Mockito.when(userRepository.getInfo(user1.getUsername())).thenReturn(user01);
        ResponseEntity result = service.getUserInfoFromUsername(user1.getUsername(), false);
        
        JSONObject response = new JSONObject();
        response.put("message", "User information found.");
        response.put("data",user01);
        ResponseEntity goodResponse = new ResponseEntity(response, HttpStatus.OK);
        assertEquals(goodResponse, result);
    }

    /**
     * Test of updateUserInfo method, of class UserService.
     */
    @Test
    public void testUpdateUserInfoSuccessfully() {
        System.out.println("updateUserInfoSuccessfully");
        
        tqs.cloudit.domain.persistance.User user01 = new tqs.cloudit.domain.persistance.User(user1);
        user01.setPassword(this.bcpe.encode(user01.getPassword()));
        
        user1.setEmail("emainovodojoao@mail.com");
        user1.setPassword("123");
        user1.setNewPassword("321");
        user1.setName("Joao Feijao");
        user1.setInterestedAreas(new HashSet<String>(){{add("Security");}});
        
        Mockito.when(userRepository.getInfo(user1.getUsername())).thenReturn(user01);
        Mockito.when(userRepository.emailExists(user1.getEmail())).thenReturn(0);
        Mockito.when(areaRepository.existsByArea("Security")).thenReturn(0l);
        
        ResponseEntity result = service.updateUserInfo(user1);
        
        JSONObject response = new JSONObject();
        response.put("message", "User update successful.");
        ResponseEntity goodResponse = new ResponseEntity(response, HttpStatus.OK);
        assertEquals(goodResponse, result);
    }
    
    /**
     * Test of updateUserInfo method, of class UserService, without any changes.
     */
    @Test
    public void testUpdateUserInfoNoUpdate() {
        System.out.println("updateUserInfoNoUpdate");
        
        user1.setNewPassword(null);
        
        tqs.cloudit.domain.persistance.User user01 = new tqs.cloudit.domain.persistance.User(user1);
        user01.setPassword(this.bcpe.encode(user01.getPassword()));
        
        Mockito.when(userRepository.getInfo(user1.getUsername())).thenReturn(user01);
        
        ResponseEntity result = service.updateUserInfo(user1);
        
        JSONObject response = new JSONObject();
        response.put("message", "No changes to the current user information were detected.");
        ResponseEntity badResponse = new ResponseEntity(response, HttpStatus.OK);
        assertEquals(badResponse, result);
    }
    
    /**
     * Test of updateUserInfo method, of class UserService, with an already registered email.
     */
    @Test
    public void testUpdateUserInfoRepeatedEmail() {
        System.out.println("updateUserInfoRepeatedEmail");
        
        tqs.cloudit.domain.persistance.User user01 = new tqs.cloudit.domain.persistance.User(user1);
        user01.setPassword(this.bcpe.encode(user01.getPassword()));
        tqs.cloudit.domain.persistance.User user02 = new tqs.cloudit.domain.persistance.User(user2);
        user01.setPassword(this.bcpe.encode(user02.getPassword()));
        
        user1.setEmail("emaildofilipe@mail.com");
        
        Mockito.when(userRepository.getInfo(user1.getUsername())).thenReturn(user01);
        Mockito.when(userRepository.emailExists(user1.getEmail())).thenReturn(1);
        
        ResponseEntity result = service.updateUserInfo(user1);
        
        JSONObject response = new JSONObject();
        response.put("message", "Unable to update profile. Your email must be unique. This email is already registered in the platform.");
        ResponseEntity badResponse = new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
        assertEquals(badResponse, result);
    }
    
    /**
     * Test of updateUserInfo method, of class UserService, with invalid password.
     */
    @Test
    public void testUpdateUserInfoInvalidPassword() {
        System.out.println("updateUserInfoInvalidPassword");
        
        tqs.cloudit.domain.persistance.User user01 = new tqs.cloudit.domain.persistance.User(user1);
        user01.setPassword(this.bcpe.encode(user01.getPassword()));
        
        user1.setPassword("1234");
        user1.setNewPassword("321");
        
        Mockito.when(userRepository.getInfo(user1.getUsername())).thenReturn(user01);
        
        ResponseEntity result = service.updateUserInfo(user1);
        
        JSONObject response = new JSONObject();
        response.put("message", "Unable to update profile. In order to change password you need to type in the current one correctly.");
        ResponseEntity badResponse = new ResponseEntity(response, HttpStatus.NOT_ACCEPTABLE);
        assertEquals(badResponse, result);
    }
    
}
