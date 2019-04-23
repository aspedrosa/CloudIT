package tqs.cloudit.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Paths related to the build in message system between
 *  freelancers and employers
 *
 * @author aspedrosa
 */
@RestController
@RequestMapping("/messages")
public class MessageController {
    /**
     * Returns all messages sent to the user
     * 
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful massage if all went good and a list with all the messages 
     * sent to the user
     */
    @GetMapping
    public ResponseEntity getAll() {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    /**
     * Return the messages sent to the user by the user identified by the id
     * 
     * @param id ID of the user with whom the message exchange should be returned
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and all the messages
     */
    @GetMapping("/id/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}
