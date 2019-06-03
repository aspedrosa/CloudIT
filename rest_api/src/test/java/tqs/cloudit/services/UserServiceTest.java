package tqs.cloudit.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    /**
     * Since the repository is in charge of filtering the user by the received name
     *  and user type, it is the responsibility of the User service to filter
     *  user by their interested areas, what is tested on this test
     */
    @Test
    public void testSearchProfile() {
        Set<String> user1Areas = new HashSet<>();
        user1Areas.add("Area1");
        user1Areas.add("Area2");
        user1Areas.add("Area3");

        Set<String> user2Areas = new HashSet<>();
        user2Areas.add("Area1");
        user2Areas.add("Area2");

        user1.setInterestedAreas(user1Areas);
        user2.setInterestedAreas(user2Areas);

        List<tqs.cloudit.domain.persistance.User> users = new ArrayList<>();
        users.add(new tqs.cloudit.domain.persistance.User(user1));
        users.add(new tqs.cloudit.domain.persistance.User(user2));

        Mockito.when(userRepository.userSearch(null, null)).thenReturn(users);

        Set<String> requestedAreas = new HashSet<>();
        requestedAreas.add("Area1");
        requestedAreas.add("Area3");
        List<tqs.cloudit.domain.responses.User> response = service.searchUser(null, requestedAreas, null);

        //only Joao has Area1 and Area3 as interested areas
        assertEquals(1, response.size());
        assertEquals("Joao", response.get(0).getName());

        requestedAreas.remove("Area3");
        response = service.searchUser(null, requestedAreas, null);
        //both have Area1 as interested area
        assertEquals(2, response.size());
        if (response.get(0).getName().equals("Joao")) {
            assertEquals("Filipe", response.get(1).getName());
        }
        else {
            assertEquals("Joao", response.get(1).getName());
        }

        requestedAreas.remove("Area1");
        response = service.searchUser(null, null, null);
        //no areas should give all users
        assertEquals(2, response.size());
        if (response.get(0).getName().equals("Joao")) {
            assertEquals("Filipe", response.get(1).getName());
        }
        else {
            assertEquals("Joao", response.get(1).getName());
        }

        response = service.searchUser(null, null, null);
        //null areas should give all users
        assertEquals(2, response.size());
        if (response.get(0).getName().equals("Joao")) {
            assertEquals("Filipe", response.get(1).getName());
        }
        else {
            assertEquals("Joao", response.get(1).getName());
        }

        //reset interested areas so conflict doesn't exist with other tests
        user1.setInterestedAreas(null);
        user2.setInterestedAreas(null);
    }
}
