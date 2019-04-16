package tqs.cloudit.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author aspedrosa
 */
@RestController
@RequestMapping("/freelancer")
public class FreelancerController {

    @RequestMapping(method=RequestMethod.POST)
    public ResponseEntity register(@RequestParam(value="name", defaultValue="World") String name) {
        return new ResponseEntity("Freelancer registered", HttpStatus.OK);
    }
}
