package tqs.cloudit.services;

import java.util.Collections;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
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
    
    
    private static ResponseEntity emptyResponse;
    static{
        JSONObject response1 = new JSONObject();
        response1.put("message", "Information fetched with success.");
        response1.put("data", Collections.EMPTY_LIST);
        emptyResponse = new ResponseEntity(response1, HttpStatus.OK);
        
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
    public void testGetOffers() {
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
        Mockito.when(jobRepository.findAll()).thenReturn(Collections.EMPTY_LIST);
        ResponseEntity result = service.getOffers();
        assertEquals(emptyResponse, result);
        Mockito.verify(jobRepository).findAll();
    }
    
    /**
     * Test of registerOffer method, of class JobService.
     */
    @Test
    public void testRegisterOfferIncomplete() {
        System.out.println("registerOfferIncomplete");
        String creator = "";
        JobOffer jobOffer = null;
        JobService instance = new JobService();
        ResponseEntity expResult = null;
        ResponseEntity result = instance.registerOffer(creator, jobOffer);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getSpecificOffer method, of class JobService.
     */
    @Test
    public void testGetSpecificOffer() {
        System.out.println("getSpecificOffer");
        long id = 0L;
        JobService instance = new JobService();
        ResponseEntity expResult = null;
        ResponseEntity result = instance.getSpecificOffer(id);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserOffers method, of class JobService.
     */
    @Test
    public void testGetUserOffers() {
        System.out.println("getUserOffers");
        String name = "";
        JobService instance = new JobService();
        ResponseEntity expResult = null;
        ResponseEntity result = instance.getUserOffers(name);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteSpecificOffer method, of class JobService.
     */
    @Test
    public void testDeleteSpecificOffer() {
        System.out.println("deleteSpecificOffer");
        long id = 0L;
        JobService instance = new JobService();
        ResponseEntity expResult = null;
        ResponseEntity result = instance.deleteSpecificOffer(id);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserOffersAccepted method, of class JobService.
     */
    @Test
    public void testGetUserOffersAccepted() {
        System.out.println("getUserOffersAccepted");
        String name = "";
        JobService instance = new JobService();
        ResponseEntity expResult = null;
        ResponseEntity result = instance.getUserOffersAccepted(name);
        assertEquals(expResult, result);
        fail("The test case is a prototype.");
    }
    
}
