package tqs.cloudit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebSiteController {
    
    @GetMapping(path = "/login")
    public String login() {
        return "login.html";
    }
    
    @GetMapping(path = "/welcome")
    public String welcome() {
        return "welcome.html";
    }
    
    @GetMapping(path = "/about")
    public String about() {
        return "about.html";
    }
    
    @GetMapping(path = "/messages")
    public String messages() {
        return "messageCenter.html";
    }
    
    @GetMapping(path = "/search")
    public String search() {
        return "search.html";
    }
    
    @GetMapping(path = "/profile")
    public String profile() {
        return "profile.html";
    }
}
