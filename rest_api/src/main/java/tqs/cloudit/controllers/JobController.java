package tqs.cloudit.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.domain.rest.JobOffer;
import tqs.cloudit.services.JobService;

/**
 * Paths related to the build in message system between
 *  freelancers and employers
 *
 * @author aspedrosa
 */
@RestController
@CrossOrigin
@RequestMapping("/joboffer")
public class JobController {
    
    @Autowired
    private JobService jobService;
    
    /**
     * Returns all job offers registered in the platform
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with all the jobs 
     *  available in the platform
     */
    @GetMapping
    public ResponseEntity getAll() {
        return jobService.getOffers();
    }
    
    /**
     * Registers a job offer in the platform
     * 
     * @param jobOffer Information relative to the job
     * @return HTTP response with a descriptive message of what went wrong or
     *  a successful massage if all went good
     */
    @PostMapping
    public ResponseEntity register(@RequestBody JobOffer jobOffer, Principal principal) {
        return jobService.registerOffer(principal.getName(), jobOffer);
    }
    
    /**
     * Return the description of the job identified by the id
     * 
     * @param id ID of the job
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and the detailed description of the job
     */
    @GetMapping("/id/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        return jobService.getSpecificOffer(id);
    }
    
    /**
     * Return the description of the jobs of the user.
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and the detailed description of the job
     */
    @GetMapping("/self")
    public ResponseEntity getMyOffers(Principal principal) {
        return jobService.getUserOffers(principal.getName());
    }
}
