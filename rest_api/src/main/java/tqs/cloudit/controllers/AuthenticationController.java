package tqs.cloudit.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aspedrosa
 */
@RestController
public class AuthenticationController {
    @GetMapping
    public ResponseEntity login() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
