package tqs.cloudit.controllers;

import tqs.cloudit.domain.persistance.JobOffer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Paths related to the build in message system between
 *  freelancers and employers
 *
 * @author aspedrosa
 */
@RestController
@RequestMapping("/joboffer")
public class JobController {
    /**
     * Returns all job offers registered in the platform
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with all the jobs 
     *  available in the platform
     */
    @GetMapping
    public ResponseEntity getAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    /**
     * Registers a job offer in the platform
     * 
     * @param joboffer Information relative to the job
     * @return HTTP response with a descriptive message of what went wrong or
     *  a successful massage if all went good
     */
    @PostMapping
    public ResponseEntity register(@RequestBody JobOffer joboffer) {
        throw new UnsupportedOperationException("Not implemented yet!");
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
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
