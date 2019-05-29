package tqs.cloudit.services;

import java.io.IOException;
import java.util.Collections;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Ignore;
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
import tqs.cloudit.domain.persistance.User;
import tqs.cloudit.domain.rest.JobOffer;
import tqs.cloudit.repositories.JobRepository;
import tqs.cloudit.repositories.UserRepository;

/**
 *
 * @author joaoalegria
 */
@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {
    
    @TestConfiguration
    static class Config {
  
        @Bean
        public JobService jobService() {
            return new JobService();
        }
    }
    
    
    private static ResponseEntity emptyResponse, incompleteResponse, completeResponse, fullResponse;
    private static JobOffer jo;
    private static User user1;
    
    static{
        jo = new JobOffer();
        jo.setTitle("titulo");
        jo.setDescription("descript");
        jo.setAmount(10);
        jo.setDate("hoje");
        jo.setArea("Web");
        
        JSONObject response1 = new JSONObject();
        response1.put("message", "Information fetched with success.");
        response1.put("data", Collections.EMPTY_LIST);
        emptyResponse = new ResponseEntity(response1, HttpStatus.OK);
        
        JSONObject response4 = new JSONObject();
        response4.put("message", "Information fetched with success.");
        JSONArray aux = new JSONArray();
        aux.add(new tqs.cloudit.domain.persistance.JobOffer(jo));
        response4.put("data", aux);
        fullResponse = new ResponseEntity(response4, HttpStatus.OK);
        
        JSONObject response2 = new JSONObject();
        response2.put("message", "Registration invalid. When registering you need to provide the offer title, description, working area, amount and date(to be delivered).");
        incompleteResponse = new ResponseEntity(response2, HttpStatus.NOT_ACCEPTABLE);
        
        JSONObject response3 = new JSONObject();
        response3.put("message", "Job offer registered with success.");
        completeResponse = new ResponseEntity(response3, HttpStatus.OK);
        
        user1 = new User();
        user1.setUsername("joao");
        user1.setPassword("123");
        user1.setName("Joao");
        user1.setEmail("emaidojoao@mail.com");
        user1.setType("Freelancer");
        user1.addNewOffer( new tqs.cloudit.domain.persistance.JobOffer(jo));
    }
    
    
    @InjectMocks
    JobService service;
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private JobRepository jobRepository;
    
    /**
     * Test of getOffers method, of class JobService.
     */
    @Test
    public void testGetOffersEmpty() {
        System.out.println("getOffers");
        Mockito.when(jobRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        ResponseEntity result = service.getOffers();
        assertEquals(emptyResponse, result);
        Mockito.verify(jobRepository).findAll();
    }

    /**
     * Test of registerOffer method, of class JobService.
     */
    @Test
    public void testRegisterOfferComplete() {
        System.out.println("registerOfferComplete");
        Mockito.when(userRepository.getInfo("teste")).thenReturn(user1);
        ResponseEntity result = service.registerOffer("teste", jo);
        assertEquals(completeResponse, result);
        Mockito.verify(userRepository).getInfo("teste");
    }
    
    /**
     * Test of registerOffer method, of class JobService.
     */
    @Test
    public void testRegisterOfferIncomplete() {
        System.out.println("registerOfferIncomplete");
        ResponseEntity result = service.registerOffer("teste", new JobOffer());
        assertEquals(incompleteResponse, result);
    }

    /**
     * Test of getSpecificOffer method, of class JobService.
     */
    @Test
    public void testGetSpecificOfferExists() {
        System.out.println("getSpecificOfferExists");
        tqs.cloudit.domain.persistance.JobOffer myJo = new tqs.cloudit.domain.persistance.JobOffer(jo);
        Mockito.when(jobRepository.getJobOffer(5l)).thenReturn(myJo);
        ResponseEntity result = service.getSpecificOffer(5l);
        
        JSONObject response = new JSONObject();
        response.put("message", "Job offer found with success.");
        response.put("data", myJo);
        
        assertEquals(new ResponseEntity(response,HttpStatus.OK), result);
        Mockito.verify(jobRepository).getJobOffer(5l);
    }
    
    /**
     * Test of getSpecificOffer method, of class JobService.
     */
    @Test
    public void testGetSpecificOfferNoExists() {
        System.out.println("getSpecificOfferNoExists");
        Mockito.when(jobRepository.getJobOffer(5l)).thenReturn(null);
        ResponseEntity result = service.getSpecificOffer(5l);
        JSONObject response = new JSONObject();
        response.put("message", "No job found with that id.");
        assertEquals(new ResponseEntity(response,HttpStatus.NOT_FOUND), result);
        Mockito.verify(jobRepository).getJobOffer(5l);
    }

    /**
     * Test of getUserOffers method, of class JobService.
     */
    @Test
    public void testGetUserOffers() throws IOException {
        System.out.println("getUserOffers");
        Mockito.when(userRepository.getInfo("teste")).thenReturn(user1);
        ResponseEntity result = service.getUserOffers("teste");
        assertEquals(fullResponse.getStatusCode(), result.getStatusCode());
        assertEquals(fullResponse.getBody().toString(), result.getBody().toString());
        Mockito.verify(userRepository).getInfo("teste");
    }

    /**
     * Test of deleteSpecificOffer method, of class JobService.
     */
    @Test
    public void testDeleteSpecificOfferExist() {
        System.out.println("deleteSpecificOfferExist");
        Mockito.when(jobRepository.existsById(5l)).thenReturn(true);
        ResponseEntity result = service.deleteSpecificOffer(5l);
        JSONObject response = new JSONObject();
        response.put("message", "Job removed with success.");
        assertEquals(new ResponseEntity(response,HttpStatus.OK), result);
        Mockito.verify(jobRepository).existsById(5l);
    }
    
    /**
     * Test of deleteSpecificOffer method, of class JobService.
     */
    @Test
    public void testDeleteSpecificOfferDontExist() {
        System.out.println("deleteSpecificOfferDontExist");
        Mockito.when(jobRepository.existsById(5l)).thenReturn(false);
        ResponseEntity result = service.deleteSpecificOffer(5l);
        JSONObject response = new JSONObject();
        response.put("message", "Job id doesn't exist.");
        assertEquals(new ResponseEntity(response,HttpStatus.NOT_FOUND), result);
        Mockito.verify(jobRepository).existsById(5l);
    }

    /**
     * Test of getUserOffersAccepted method, of class JobService.
     */
    @Test
    @Ignore
    public void testGetUserOffersAccepted() {
        System.out.println("getUserOffersAccepted");
        fail("Not Implemented yet.");
    }
    
}
