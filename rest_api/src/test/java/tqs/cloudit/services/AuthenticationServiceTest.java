package tqs.cloudit.services;

import java.util.Collections;
import java.util.HashSet;
import org.assertj.core.util.Lists;
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
import org.springframework.http.ResponseEntity;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.repositories.AreaRepository;
import tqs.cloudit.repositories.UserRepository;

/**
 *
 * @author joaoalegria
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {
    @TestConfiguration
    static class WeatherServiceTestContextConfiguration {
  
        @Bean
        public AuthenticationService authenticationService() {
            return new AuthenticationService();
        }
    }
    
    
    private static User user1, user2, user3;
    private static ResponseEntity goodResponse, badResponse1, badResponse2, badResponse3;
    static{
        JSONObject response1 = new JSONObject();
        response1.put("status", 0);
        response1.put("message", "Registered with success");
        goodResponse = ResponseEntity.ok(response1);
        
        JSONObject response2 = new JSONObject();
        response2.put("status", 1);
        response2.put("message", "Registration invalid. When registering you need to provide username, password, name, email, type of user and optionally interest areas.");
        badResponse1 = ResponseEntity.badRequest().body(response2);
        
        JSONObject response3 = new JSONObject();
        response3.put("status", 1);
        response3.put("message", "Registration invalid. Username must be unique. This username already exists.");
        badResponse2 = ResponseEntity.badRequest().body(response3);
        
        JSONObject response4 = new JSONObject();
        response4.put("status", 1);
        response4.put("message", "Registration invalid. Your email must be unique. This email is already registered in the platform.");
        badResponse3 = ResponseEntity.badRequest().body(response4);
        
        user1 = new User();
        user1.setUsername("joao");
        user1.setPassword("123");
        user1.setName("Joao");
        user1.setEmail("emaidojoao@mail.com");
        user1.setType("freelancer");
        user2 = new User();
        user2.setPassword("123");
        user2.setName("Joao");
        user2.setEmail("emaidojoao@mail.com");
        user2.setType("freelancer");
        user3 = new User();
        user3.setUsername("joaquim");
        user3.setPassword("123");
        user3.setName("Joaquim");
        user3.setEmail("emaidojoaquim@mail.com");
        user3.setType("freelancer");
        user3.setInterestedAreas(new HashSet<String>(){{add("Security");}});
    }
    
    @InjectMocks
    AuthenticationService service;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private AreaRepository areaRepository;
    
    /**
     * Test of register method, of class AuthenticationService. RegisterAll.
     */
    @Test
    public void testRegisterAll() {
        System.out.println("RegisterAll");
        Mockito.when(userRepository.getUsernames()).thenReturn(Collections.EMPTY_LIST);
        Mockito.when(userRepository.getMails()).thenReturn(Collections.EMPTY_LIST);
        Mockito.when(areaRepository.existsByArea("Security")).thenReturn(0l);
        ResponseEntity result = service.register(user3);
        assertEquals(goodResponse, result);
        Mockito.verify(userRepository).getUsernames();
        Mockito.verify(userRepository).getMails();
        Mockito.verify(areaRepository).existsByArea("Security");
    }
    
    /**
     * Test of register method, of class AuthenticationService. RegisterAllMinusAreas.
     */
    @Test
    public void testRegisterAllMinusAreas() {
        System.out.println("RegisterAllMinusAreas");
        Mockito.when(userRepository.getUsernames()).thenReturn(Collections.EMPTY_LIST);
        Mockito.when(userRepository.getMails()).thenReturn(Collections.EMPTY_LIST);
        ResponseEntity result = service.register(user1);
        assertEquals(goodResponse, result);
        Mockito.verify(userRepository).getUsernames();
        Mockito.verify(userRepository).getMails();
    }
    
    /**
     * Test of register method, of class AuthenticationService. RegisterNotPassingNecessaryInfo.
     */
    @Test
    public void testRegisterNotPassingNecessaryInfo() {
        System.out.println("RegisterNotPassingNecessaryInfo");
        ResponseEntity result = service.register(user2);
        assertEquals(badResponse1, result);
    }
    
    /**
     * Test of register method, of class AuthenticationService. RegisterNameAlreadyExisting.
     */
    @Test
    public void testRegisterNameAlreadyExisting() {
        System.out.println("RegisterNameAlreadyExisting");
        Mockito.when(userRepository.getUsernames()).thenReturn(Lists.list("joao"));
        ResponseEntity result = service.register(user1);
        assertEquals(badResponse2, result);
    }
    
    /**
     * Test of register method, of class AuthenticationService. RegisterEmailAlreadyExisting.
     */
    @Test
    public void testRegisterEmailAlreadyExisting() {
        System.out.println("RegisterEmailAlreadyExisting");
        Mockito.when(userRepository.getUsernames()).thenReturn(Collections.EMPTY_LIST);
        Mockito.when(userRepository.getMails()).thenReturn(Lists.list("emaidojoao@mail.com"));
        ResponseEntity result = service.register(user1);
        assertEquals(badResponse3, result);
        Mockito.verify(userRepository).getUsernames();
        Mockito.verify(userRepository).getMails();
    }
    
}
