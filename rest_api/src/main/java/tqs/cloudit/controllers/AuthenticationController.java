package tqs.cloudit.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.domain.persistance.Area;
import tqs.cloudit.domain.rest.Credentials;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.repositories.AreaRepository;
import tqs.cloudit.repositories.UserRepository;
import tqs.cloudit.services.AuthenticationService;

/**
 * Paths related to authentication of users for using the API
 *
 * @author aspedrosa
 */
@RestController
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
     * @param cred
     * @return 
     */
    @GetMapping("/login")
    public ResponseEntity login() {
        return ResponseEntity.ok("firmeza");
    }

    /**
     * Deauthenticates a user by removing session cookies
     */
    @PostMapping("/logout")
    public ResponseEntity logout() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody User user) {
        return this.authServ.register(user);
    }
    
    @PostMapping("/company")
    public ResponseEntity associateCompany() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    @PostMapping("/hire/id/{id}")
    public ResponseEntity hire(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}