package tqs.cloudit.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.domain.rest.AdvancedSearch;
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
     *  a successful message if all went good and a list with all the jobs 
     *  available in the platform
     */
    @GetMapping
    public ResponseEntity getAll() {
        return jobService.getOffers();
    }
    
    @PostMapping("/advancedSearch")
    public ResponseEntity advancedSearch(@RequestBody AdvancedSearch advancedSearch) {
        if (advancedSearch.getFromAmount() < 0.0)
            advancedSearch.setFromAmount(0.0);
        if (advancedSearch.getToAmount() < 0.0)
            advancedSearch.setToAmount(Double.MAX_VALUE);
        if (advancedSearch.getFromDate().equals(""))
            advancedSearch.setFromDate("0000-00-00");
        if (advancedSearch.getToDate().equals(""))
            advancedSearch.setToDate("9999-99-99");
        if (advancedSearch.isTitle() && advancedSearch.isArea())
            return jobService.getJobOffersFromTextAmountAndDate(advancedSearch.getQuery(), 
                                                                advancedSearch.getQuery(), 
                                                                advancedSearch.getFromAmount(),
                                                                advancedSearch.getToAmount(), 
                                                                advancedSearch.getFromDate(),
                                                                advancedSearch.getToDate());
        if (advancedSearch.isTitle())
            return jobService.getJobOffersFromTextAmountAndDateOnlyTitle(advancedSearch.getQuery(), 
                                                                         advancedSearch.getQuery(), 
                                                                         advancedSearch.getFromAmount(),
                                                                         advancedSearch.getToAmount(), 
                                                                         advancedSearch.getFromDate(),
                                                                         advancedSearch.getToDate());
        if (advancedSearch.isArea())
            return jobService.getJobOffersFromTextAmountAndDateOnlyArea(advancedSearch.getQuery(), 
                                                                        advancedSearch.getQuery(), 
                                                                        advancedSearch.getFromAmount(),
                                                                        advancedSearch.getToAmount(), 
                                                                        advancedSearch.getFromDate(),
                                                                        advancedSearch.getToDate());
        return jobService.getJobOffersFromTextAmountAndDate("", 
                                                            "", 
                                                            advancedSearch.getFromAmount(),
                                                            advancedSearch.getToAmount(), 
                                                            advancedSearch.getFromDate(),
                                                            advancedSearch.getToDate());
        
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
     * Edits an existing job offer in the platform of a given user.
     * 
     * @param id identification of the user whose job offer is to be edited
     * @param jobOffer Information relative to the job
     * @return HTTP response with a descriptive message of what went wrong or
     *  a successful message if all went good
     */
    @PutMapping("/edit/{id}")
    public ResponseEntity editById(@PathVariable long id, @RequestBody JobOffer jobOffer,Principal principal) {
        return jobService.editOffer(id, jobOffer);
    }
    
    /**
     * Deletes the job identified by the id
     * 
     * @param id ID of the job
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and the detailed description of the job
     */
    @DeleteMapping("/id/{id}")
    public ResponseEntity deleteById(@PathVariable long id) {
        return jobService.deleteSpecificOffer(id);
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
    
    /**
     * Return the description of the jobs accepted by the user.
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and the detailed description of the job
     */
    @GetMapping("/accepted")
    public ResponseEntity getMyOffersAccepted(Principal principal) {
        return jobService.getUserOffersAccepted(principal.getName());
    }
    
    
    @GetMapping("/finish/{id}")
    public ResponseEntity finishOffer(@PathVariable long id,Principal principal) {
        return jobService.finishOffer(id);
    }
}
