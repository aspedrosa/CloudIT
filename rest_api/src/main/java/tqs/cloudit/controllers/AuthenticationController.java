package tqs.cloudit.controllers;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.services.AuthenticationService;
import tqs.cloudit.utils.Constants;


/**
 * Paths related to authentication of users for using the API
 *
 * @author aspedrosa
 */
@RestController
@CrossOrigin
public class AuthenticationController {
    
    @Autowired
    private AuthenticationService authServ;

    /**
     * Authenticates user
     * Has to:
     * <ul>
     *    <li>check the database</li>
     *    <li>see if user exists</li>
     *    <li>hash password</li>
     *    <li>compare passwords</li>
     * </ul>
     *
     * @return
     */
    @GetMapping("/login")
    public ResponseEntity login(Principal principal) {
        return authServ.login(principal.getName());
    }

    /**
     * Deauthenticates a user by removing session cookies
     */
    @PostMapping(path = "/logout", consumes = "application/json", produces = "application/json")
    public ResponseEntity logout() {
        throw new UnsupportedOperationException(Constants.NOT_IMPLEMENTED_YET);
    }

    @PostMapping(path = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity register(@RequestBody User user) {
        return this.authServ.register(user);
    }
    
    @PostMapping(path = "/company", consumes = "application/json", produces = "application/json")
    public ResponseEntity associateCompany() {
        throw new UnsupportedOperationException(Constants.NOT_IMPLEMENTED_YET);
    }
    
    @PostMapping(path = "/hire/id/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity hire(@PathVariable long id) {
        throw new UnsupportedOperationException(Constants.NOT_IMPLEMENTED_YET);
    }
    
}