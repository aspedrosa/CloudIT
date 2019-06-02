package tqs.cloudit.controllers;

import java.security.Principal;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.services.UserService;

/**
 * All request paths associated with users (employers and freelancers)
 *
 * @author fp
 */
@RestController
@CrossOrigin
public class UserController {
    
    @Autowired
    public UserService userService;
    
    /*
        User Profile
    */
    
    /**
     * Return the profile data of the user identified by the id
     * 
     * @param id ID of the user to whom the profile information should be returned
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and the profile information requested
     */
    @GetMapping("/profile")
    public ResponseEntity getProfile(Principal principal) {
        return userService.getUserInfoFromUsername(principal.getName(), false);
    }
    
    /**
     * Update the profile data of the user identified by the id
     * @param user information about the user required for the update
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good
     */
    @PutMapping(path="/profile", consumes="application/json", produces="application/json")
    public ResponseEntity updateProfile(@RequestBody User user) {
        return this.userService.updateUserInfo(user);
    }
    
    /*
        Freelancer
    */
    
    /**
     * Returns all freelancers registered
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with the information
     *  of the existing freelancers
     */
    @GetMapping("/freelancer")
    public ResponseEntity getAllFreelancers() {
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
    @GetMapping("/freelancer/id/{id}")
    public ResponseEntity getFreelancerById(@PathVariable long id) {
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
    @GetMapping("/freelancer/area/{area}")
    public ResponseEntity getFreelancersByArea(@PathVariable String area) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    /**
     *  THIS ENDPOINT DOES NOT EXIST. REPACE THIS TEST ENDPOINT WITH A /profile one, [get, put, delete].
     */
    @GetMapping("/freelancer/info")
    public ResponseEntity getFreelancerInfo(Principal p) {
        return ResponseEntity.ok(userService.getUserInfoFromUsername(p.getName(), false));
        //throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    /*
        Employer
    */
    
    /**
     * Returns all employers registered
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with the information
     *  of the existing employers
     */
    @GetMapping("/employer")
    public ResponseEntity getAllEmployers() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Return the employer associated with the received id
     * 
     * @param id ID of the employer that the user wants to know more
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and the information of the
     *  employer
     */
    @GetMapping("/employer/id/{id}")
    public ResponseEntity getEmployerById(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
