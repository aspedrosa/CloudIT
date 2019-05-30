package tqs.cloudit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.Matchers.is;
import org.json.simple.JSONObject;
import org.junit.Ignore; 
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.services.AuthenticationService;
import tqs.cloudit.services.UserService;

/**
 *
 * @author fp
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserControllerTest {
    
    private ObjectMapper mapper = new ObjectMapper();
    
    private static User user1, user2;
    private static ResponseEntity goodResponse0, goodResponse1, badResponse0, badResponse1, badResponse2, badResponse3;
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
        
        JSONObject response01 = new JSONObject();
        response01.put("message", "User information found.");
        response01.put("data", user1);
        goodResponse0 = new ResponseEntity(response01, HttpStatus.OK);
        
        JSONObject response1 = new JSONObject();
        response1.put("message", "User update successful.");
        goodResponse1 = new ResponseEntity(response1, HttpStatus.OK);
        
        JSONObject response02 = new JSONObject();
        response02.put("message", "Unable to retrieve information. User does not exist.");
        badResponse0 = new ResponseEntity(response02, HttpStatus.NOT_ACCEPTABLE);
        
        JSONObject response2 = new JSONObject();
        response2.put("message", "No changes to the current user information were detected.");
        badResponse1 = new ResponseEntity(response2, HttpStatus.OK);
        
        JSONObject response3 = new JSONObject();
        response3.put("message", "Unable to update profile. Your email must be unique. This email is already registered in the platform.");
        badResponse2 = new ResponseEntity(response3, HttpStatus.NOT_ACCEPTABLE);
        
        JSONObject response4 = new JSONObject();
        response4.put("message", "Unable to update profile. In order to change password you need to type in the current one correctly.");
        badResponse3 = new ResponseEntity(response4, HttpStatus.NOT_ACCEPTABLE);
    }
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private UserService service;
    
    @MockBean
    private AuthenticationService authServ;
    
    /*
        User Profile Tests
    */

    /**
     * Test of getProfile method, of class UserController, with an existing user.
     */
    @Test
    public void testGetExistingProfile() throws Exception {
        System.out.println("getExistingProfile");
        
        authServ.register(user1);
        
        Mockito.when(service.getUserInfoFromUsername(user1.getUsername(), false)).thenReturn(goodResponse0);
        mvc.perform(get("/profile").with(user("joao").password("123"))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User information found.")));
    }
    
    /**
     * Test of getProfile method, of class UserController, with a non existing user.
     */
    @Test
    public void testGetNonExistingProfile() throws Exception {
        System.out.println("getNonExistingProfile");
        
        authServ.register(user1);
        
        mvc.perform(get("/profile")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    /**
     * Test of updateProfile method, of class UserController, with correct input.
     */
    @Test
    public void testUpdateProfileSuccessfully() throws Exception {
        System.out.println("updateProfile");
        
        Set<String> ia = new HashSet<>();
        ia.add("software engineering");
        
        authServ.register(user1);
        
        user1.setName("Joao Updated");
        user1.setEmail("emaidojoao_updated@mail.com");
        user1.setInterestedAreas(ia);
        user1.setPassword("123");
        user1.setNewPassword("321");
        
        Mockito.when(service.updateUserInfo(Mockito.any())).thenReturn(goodResponse1);
        mvc.perform(put("/profile").with(user("joao").password("123"))
                .content(toJson(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User update successful.")));
    }
    
    /**
     * Test of updateProfile method, of class UserController, without any changes.
     */
    @Test
    public void testUpdateProfileNoUpdate() throws Exception {
        System.out.println("updateProfileNoUpdate");
        
        authServ.register(user1);
        
        Mockito.when(service.updateUserInfo(Mockito.any())).thenReturn(badResponse1);
        mvc.perform(put("/profile").with(user("joao").password("123"))
                .content(toJson(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("No changes to the current user information were detected.")));
    }
    
    /**
     * Test of updateProfile method, of class UserController, with an already registered email.
     */
    @Test
    public void testUpdateProfileRepeatedEmail() throws Exception {
        System.out.println("updateProfileRepeatedEmail");
        
        authServ.register(user1);
        authServ.register(user2);
        
        user1.setEmail("emaidofilipe@mail.com");
        
        Mockito.when(service.updateUserInfo(Mockito.any())).thenReturn(badResponse2);
        mvc.perform(put("/profile").with(user("joao").password("123"))
                .content(toJson(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message", is("Unable to update profile. Your email must be unique. This email is already registered in the platform.")));
        
    }
    
    /**
     * Test of updateProfile method, of class UserController, with invalid password.
     */
    @Test
    public void testUpdateProfileInvalidPassword() throws Exception {
        System.out.println("updateProfileInvalidPassword");
        
        authServ.register(user1);
        
        user1.setPassword("000");
        user1.setNewPassword("321");
        
        Mockito.when(service.updateUserInfo(Mockito.any())).thenReturn(badResponse3);
        mvc.perform(put("/profile").with(user("joao").password("123"))
                .content(toJson(user1))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable())
                .andExpect(jsonPath("$.message", is("Unable to update profile. In order to change password you need to type in the current one correctly.")));
    }
    
    /*
        To Do ...
    */
    
    /*
        Freelancer Tests
    */

    /**
     * Test of getAllFreelancers method, of class UserController.
     */
    @Test
    @Ignore
    public void testGetAllFreelancers() {
        System.out.println("getAllFreelancers");
        
    }

    /**
     * Test of getFreelancerById method, of class UserController.
     */
    @Test
    @Ignore
    public void testGetFreelancerById() {
        System.out.println("getFreelancerById");
        
    }

    /**
     * Test of getFreelancersByArea method, of class UserController.
     */
    @Test
    @Ignore
    public void testGetFreelancersByArea() {
        System.out.println("getFreelancersByArea");
        
    }

    /**
     * Test of getFreelancerInfo method, of class UserController.
     */
    @Test
    @Ignore
    public void testGetFreelancerInfo() {
        System.out.println("getFreelancerInfo");
        
    }
    
    /*
        Employer Tests
    */

    /**
     * Test of getAllEmployers method, of class UserController.
     */
    @Test
    @Ignore
    public void testGetAllEmployers() {
        System.out.println("getAllEmployers");
        
    }

    /**
     * Test of getEmployerById method, of class UserController.
     */
    @Test
    @Ignore
    public void testGetEmployerById() {
        System.out.println("getEmployerById");
        
    }
    
    /*
        Aux Methods
    */
    
    private byte[] toJson(Object object) throws Exception {
        return this.mapper
            .writeValueAsString(object).getBytes();
    } 
    
}
