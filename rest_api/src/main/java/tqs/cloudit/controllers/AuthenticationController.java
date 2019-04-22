package tqs.cloudit.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Paths related to authentication of users for using the API
 *
 * @author aspedrosa
 */
@RestController
public class AuthenticationController {

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
    @PostMapping("/login")
    public ResponseEntity login() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    /**
     * Deauthenticates a user by removing session cokkies
     */
    @PostMapping("/logout")
    public ResponseEntity logout() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @PostMapping("/register")
    public ResponseEntity register() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
