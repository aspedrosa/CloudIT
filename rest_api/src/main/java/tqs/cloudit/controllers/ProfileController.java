/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Paths related to the user profiles
 *
 * @author fp
 */
@RestController
@RequestMapping("/user")
public class ProfileController {
    
    /**
     * Return the profile data of the user identified by the id
     * 
     * @param id ID of the user to whom the profile information should be returned
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good and the profile information requested
     */
    @GetMapping("/get/{id}")
    public ResponseEntity getById(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
    
    /**
     * Update the profile data of the user identified by the id
     * @param id ID of the user whose profile information should be updated
     * @return HTTP response with a descriptive message of what went wrong OR
     *  a successful message if all went good
     */
    @GetMapping("/update/{id}")
    public ResponseEntity updateById(@PathVariable long id) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }
}