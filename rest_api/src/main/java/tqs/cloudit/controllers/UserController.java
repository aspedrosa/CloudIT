package tqs.cloudit.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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
                    return ResponseBuilder.buildWithMessage(
                        HttpStatus.BAD_REQUEST,
                        "Areas must not have more than 30 characters"
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
        Object username = userSearch.getUsername();
        if(username != null){
            return userService.searchUserByUsername((String)username);
        }
        
        
        String name = userSearch.getName();
        String userType = userSearch.getUserType();
        Set<String> areas = userSearch.getAreas();

        if (userType != null && !userType.equalsIgnoreCase("freelancer") && !userType.equalsIgnoreCase("employer")) {
            return ResponseBuilder.buildWithMessage(
                HttpStatus.BAD_REQUEST,
                "userType field can only be \"freelancer\" or \"employer\""
            );
        }

        if (areas != null) {
            if (areas.isEmpty()) {
                areas = null;
            }
            else {
                for (String area : areas) {
                    if (area.length() > 30) {
                        return ResponseBuilder.buildWithMessage(
                            HttpStatus.BAD_REQUEST,
                            "Areas must have less than 30 characters"
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

        if (!matchedUsers.isEmpty()) {
            return ResponseBuilder.buildWithMessageAndData(
                HttpStatus.ACCEPTED,
                "Users found",
                matchedUsers
            );
        }

        return ResponseBuilder.buildWithMessage(
            HttpStatus.NOT_FOUND,
            "No users found for the given parameters"
        );
    }
    
}
