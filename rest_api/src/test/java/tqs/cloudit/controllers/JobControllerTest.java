package tqs.cloudit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import static org.hamcrest.Matchers.is;
import org.json.simple.JSONObject;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import tqs.cloudit.domain.rest.AdvancedSearch;
import tqs.cloudit.domain.rest.Job;
import tqs.cloudit.services.JobService;
import tqs.cloudit.utils.Constants;

/**
 *
 * @author joaoalegria
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
/**
 * This test class only serves to guarantee that the rest mapping is working, since all the logic is in the services.
 */
public class JobControllerTest {
    
    private ObjectMapper mapper = new ObjectMapper();

    private static ResponseEntity emptyResponse;
    static{
        JSONObject response1 = new JSONObject();
        response1.put("message", "Information passes with success.");
        emptyResponse = new ResponseEntity(response1, HttpStatus.OK);
    }
    
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private JobService service;
    
    /**
     * Test of getAll method, of class JobController.
     */
    @Test
    public void testGetAll() throws Exception {
        System.out.println("getAll");
        Mockito.when(service.getOffers()).thenReturn(emptyResponse);
        mvc.perform(get("/joboffer")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }

    /**
     * Test of register method, of class JobController.
     */
    @Test
    public void testRegister() throws Exception {
        System.out.println("register");
        Mockito.when(service.registerOffer(Mockito.eq("joao"),Mockito.any())).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer").content(toJson(new Job())).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }

    /**
     * Test of getById method, of class JobController.
     */
    @Test
    public void testGetById() throws Exception {
        System.out.println("getById");
        Mockito.when(service.getSpecificOffer(2)).thenReturn(emptyResponse);
        mvc.perform(get("/joboffer/id/2").with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }

    /**
     * Test of deleteById method, of class JobController.
     */
    @Test
    public void testDeleteById() throws Exception {
        System.out.println("deleteById");
        Mockito.when(service.deleteSpecificOffer(5)).thenReturn(emptyResponse);
        mvc.perform(delete("/joboffer/id/5").with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }

    /**
     * Test of getMyOffers method, of class JobController.
     */
    @Test
    public void testGetMyOffers() throws Exception {
        System.out.println("getMyOffers");
        Mockito.when(service.getUserOffers("joao")).thenReturn(emptyResponse);
        mvc.perform(get("/joboffer/self").with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }

    /**
     * Test of getMyOffersAccepted method, of class JobController.
     */
    @Test
    public void testGetMyOffersAccepted() throws Exception {
        System.out.println("getMyOffersAccepted");
        Mockito.when(service.getUserOffersAccepted("joao")).thenReturn(emptyResponse);
        mvc.perform(get("/joboffer/accepted").with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }
    
    
    private byte[] toJson(Object object) throws Exception {
        return this.mapper
            .writeValueAsString(object).getBytes();
    } 

    /**
     * Test of editById method, of class JobController.
     */
    @Test
    public void testEditById() throws Exception {
        System.out.println("editByIdSuccessful");
        Long id = 1L;
        Job jo = new Job("title test","descr test","area test",100,"1111-11-11", "Offer");
        Mockito.when(service.editOffer(Mockito.eq(id),Mockito.any())).thenReturn(emptyResponse);
        mvc.perform(put("/joboffer/edit/" + id).content(toJson(jo)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            //.andExpect(cookie().exists("JSESSIONID"))
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
    }
        
    @Test
    public void testSearchOffers1() throws Exception {
        System.out.println("searchOffer1");
        AdvancedSearch as = new AdvancedSearch("AI", true, true, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("AI"), 
                                                               Mockito.eq("AI"), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(""), 
                                                               Mockito.eq(""))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearch").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchOffers2() throws Exception {
        System.out.println("searchOffer2");
        AdvancedSearch as = new AdvancedSearch("AI", false, true, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("Cybersecutiry"), 
                                                               Mockito.eq("Cybersecutiry"), 
                                                               Mockito.eq(1.0), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(""), 
                                                               Mockito.eq(""))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearch").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchOffers3() throws Exception {
        System.out.println("searchOffer3");
        AdvancedSearch as = new AdvancedSearch("AI", true, false, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("AI"), 
                                                               Mockito.eq("AI"), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(1.0), 
                                                               Mockito.eq(""), 
                                                               Mockito.eq(""))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearch").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchOffers4() throws Exception {
        System.out.println("searchOffer4");
        AdvancedSearch as = new AdvancedSearch("AI", false, false, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("Databases"), 
                                                               Mockito.eq("Databases"), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq("2019-09-01"), 
                                                               Mockito.eq("2020-09-01"))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearch").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchProposals1() throws Exception {
        System.out.println("searchProposal1");
        AdvancedSearch as = new AdvancedSearch("AI", true, true, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("AI"), 
                                                               Mockito.eq("AI"), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(""), 
                                                               Mockito.eq(""))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearchProposal").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchProposals2() throws Exception {
        System.out.println("searchProposal2");
        AdvancedSearch as = new AdvancedSearch("AI", false, true, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("Cybersecutiry"), 
                                                               Mockito.eq("Cybersecutiry"), 
                                                               Mockito.eq(1.0), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(""), 
                                                               Mockito.eq(""))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearchProposal").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchProposals3() throws Exception {
        System.out.println("searchProposal3");
        AdvancedSearch as = new AdvancedSearch("AI", true, false, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("AI"), 
                                                               Mockito.eq("AI"), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(1.0), 
                                                               Mockito.eq(""), 
                                                               Mockito.eq(""))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearchProposal").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    
    @Test
    public void testSearchProposals4() throws Exception {
        System.out.println("searchProposal4");
        AdvancedSearch as = new AdvancedSearch("AI", false, false, -1.0, -1.0, "", "");
        Mockito.when(service.getJobOffersFromTextAmountAndDate(Mockito.eq("Databases"), 
                                                               Mockito.eq("Databases"), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq(-1.0), 
                                                               Mockito.eq("2019-09-01"), 
                                                               Mockito.eq("2020-09-01"))).thenReturn(emptyResponse);
        mvc.perform(post("/joboffer/advancedSearchProposal").content(toJson(as)).with(user("joao").password("1235"))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
