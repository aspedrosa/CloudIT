package tqs.cloudit.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * All request paths associated with employers
 *
 * @author aspedrosa
 */
@RestController
@CrossOrigin
@RequestMapping("/employer")
public class EmployerController {
    /**
     * Returns all freelancers registered
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with the information
     *  of the existing employers
     */
    @GetMapping
    public ResponseEntity getAll() {
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
    @GetMapping("/id/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
