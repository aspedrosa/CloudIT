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

    @PostMapping("/login")
    public ResponseEntity login() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @PostMapping("/logout")
    public ResponseEntity logout() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @PostMapping("/register")
    public ResponseEntity register() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
