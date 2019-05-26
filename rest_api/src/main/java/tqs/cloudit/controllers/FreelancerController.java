package tqs.cloudit.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.services.UserService;

/**
 * All request paths associated with freelancers
 *
 * @author aspedrosa
 */
@RestController
@CrossOrigin
@RequestMapping("/freelancer")
public class FreelancerController {
    
    @Autowired
    public UserService userService;

    /**
     * Returns all freelancers registered
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with the information
     *  of the existing freelancers
     */
    @GetMapping
    public ResponseEntity getAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Return the freelancer associated with the received id
     * 
     * @param id ID of the Freelancer that the user wants to know more
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and the information of the
     *  freelancer
     */
    @GetMapping("/id/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Return all freelancers of a specific area  
     *
     * @param area name of the area that should filter the results
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with the information
     *  of the freelancers of the specific area
     */
    @GetMapping("/area/{area}")
    public ResponseEntity getByArea(@PathVariable String area) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    
    /**
     *  THIS ENDPOINT DOES NOT EXIST. REPACE THIS TEST ENDPOINT WITH A /profile one, [get, put, delete].
     */
    @GetMapping("/info")
    public ResponseEntity getInfo(Principal p) {
        return ResponseEntity.ok(userService.getUserInfoFromUsername(p.getName(), false));
        //throw new UnsupportedOperationException("Not implemented yet!");
    }
}
