package tqs.cloudit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.SQLException;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import org.json.simple.JSONObject;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.services.AuthenticationService;
/**
 *
 * @author joaoalegria
 */
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private static User user1, user2, user3, user4;
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
        user4 = new User();
        user4.setUsername("joana");
        user4.setPassword("123");
        user4.setName("Joana");
        user4.setEmail("emaidojoana@mail.com");
        user4.setType("freelancer");
    }
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private AuthenticationService service;
    

    @BeforeEach
    public void setUp() throws SQLException {
        when(service.register(Mockito.refEq(user1))).thenReturn(goodResponse);
        
        when(service.register(Mockito.refEq(user2))).thenReturn(badResponse1);
        
        when(service.register(Mockito.refEq(user3))).thenReturn(badResponse2);
        
        when(service.register(Mockito.refEq(user4))).thenReturn(badResponse3);
    }
    
    
    /**
     * Test of login method, of class AuthenticationController. With Credentials.
     */
    @Test
    public void testLoginWithCredentials() throws Exception {
        System.out.println("loginWithCredentials");
        mvc.perform(get("/login").with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.status", is(0)))
            .andExpect(jsonPath("$.message", is("Logged in with success")));
            
    }
    
    /**
     * Test of login method, of class AuthenticationController. Without Credentials.
     */
    @Test
    public void testLoginWithoutCredentials() throws Exception {
        System.out.println("loginWithoutCredentials");
        //dataSource.getConnection().createStatement().executeUpdate("insert into user(id,username, password) values (1,'joao','123')");
        mvc.perform(get("/login")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    /**
     * Test of logout method, of class AuthenticationController.
     */
    @Test
    public void testLogout() {
        fail("The test case is a prototype.");
    }

    /**
     * Test of register method, of class AuthenticationController. Form information correct.
     */
    @Test
    public void testRegisterCorrect() throws Exception {
        System.out.println("registerAllCorrect");
        mvc.perform(post("/register")
            .content(toJson(user1))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status", is(0)))
            .andExpect(jsonPath("$.message", is("Registered with success")));
    }
    
    /**
     * Test of register method, of class AuthenticationController. Not all necessary fields sent.
     */
    @Test
    public void testRegisterNotPassingAllTheRequiredFields() throws Exception {
        System.out.println("registerNotPassingAllTheRequiredFields");
        mvc.perform(post("/register")
            .content(toJson(user2))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.status", is(1)))
            .andExpect(jsonPath("$.message", containsString("Registration invalid.")));
    }

    /**
     * Test of register method, of class AuthenticationController. Not all necessary fields sent.
     */
    @Test
    public void testRegisterNameOrMailAlreadyExisting() throws Exception {
        System.out.println("registerNameOrMailAlreadyExisting");
        mvc.perform(post("/register")
            .content(toJson(user3))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.status", is(1)))
            .andExpect(jsonPath("$.message", containsString("Registration invalid. Username must be unique.")));
        
        mvc.perform(post("/register")
            .content(toJson(user4))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError())
            .andExpect(jsonPath("$.status", is(1)))
            .andExpect(jsonPath("$.message", containsString("Registration invalid. Your email must be unique.")));
    }

    
    /**
     * Test of associateCompany method, of class AuthenticationController.
     */
    @Test
    public void testAssociateCompany() {
        System.out.println("associateCompany");
        fail("The test case is a prototype.");
    }

    /**
     * Test of hire method, of class AuthenticationController.
     */
    @Test
    public void testHire() {
        System.out.println("hire");
        fail("The test case is a prototype.");
    }

    private byte[] toJson(Object object) throws Exception {
        return this.mapper
            .writeValueAsString(object).getBytes();
    } 
    
}
