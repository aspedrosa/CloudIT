package tqs.cloudit.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tqs.cloudit.domain.rest.ProfileUser;
import tqs.cloudit.domain.rest.User;

@RestController
@RequestMapping ("/profile")
public class ProfileController {


    @PutMapping
    public void updateProfile(ProfileUser user) {

    }

    @GetMapping
    public void getProfile() {

    }
}
