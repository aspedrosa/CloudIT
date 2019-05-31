package tqs.cloudit.controllers;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.domain.rest.UserSearch;
import tqs.cloudit.services.UserService;
import tqs.cloudit.utils.ResponseBuilder;

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


        if (user.getInterestedAreas() != null) {
            for (String area : user.getInterestedAreas()) {
                if (area.length() > 30) {
                    return new ResponseEntity(
                        ResponseBuilder.buildWithMessage("Areas must not have more than 30 characters"),
                        HttpStatus.BAD_REQUEST
                    );
                }
            }
        }

        return this.userService.updateUserInfo(user);
    }

    /**
     * Get all users on the platform filtered by parameters
     *
     * @param userSearch where the parameters to filter the user are sent
     * @return users that matched the filters
     */
    @PostMapping(path="/profile/search", produces="application/json", consumes="application/json")
    public ResponseEntity searchProfile(@RequestBody UserSearch userSearch) {
        String name = userSearch.getName();
        String userType = userSearch.getUserType();
        Set<String> areas = userSearch.getAreas();

        if (userType != null) {
            userType = userType.trim().toLowerCase();
            if (!userType.equals("freelancer") && !userType.equals("employer")) {
                return new ResponseEntity(
                    ResponseBuilder.buildWithMessage("userType field can only be \"freelancer\" or \"employer\""),
                    HttpStatus.BAD_REQUEST
                );
            }
        }

        if (areas != null) {
            if (areas.size() == 0) {
                areas = null;
            }
            else {
                for (String area : areas) {
                    if (area.length() > 30) {
                        return new ResponseEntity(
                            ResponseBuilder.buildWithMessage("Areas must have less than 30 characters"),
                            HttpStatus.BAD_REQUEST
                        );
                    }
                }
            }
        }

        if (name != null) {
            name = name.trim();
            if (name.length() == 0) {
                name = null;
            }
        }

        List<tqs.cloudit.domain.responses.User> matchedUsers = this.userService.searchUser(name, areas, userType);

        if (matchedUsers.size() > 0) {
            return new ResponseEntity(
                ResponseBuilder.buildWithMessageAndData("Users found", matchedUsers),
                HttpStatus.ACCEPTED
            );
        }

        return new ResponseEntity(
            ResponseBuilder.buildWithMessage("No users found for the given parameters"),
            HttpStatus.NOT_FOUND
        );
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
