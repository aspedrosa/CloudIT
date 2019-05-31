package tqs.cloudit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class WebSiteController {
    
    @GetMapping("/loginPage")
    public String login() {
        return "login.html";
    }
    
    @GetMapping("/welcomePage")
    public String welcome() {
        return "welcome.html";
    }
    
    @GetMapping("/aboutPage")
    public String about() {
        return "about.html";
    }
    
    @GetMapping("/messagesPage")
    public String messages() {
        return "messageCenter.html";
    }
    
    @GetMapping("/searchPage")
    public String search() {
        return "search.html";
    }
    
    @GetMapping("/profilePage")
    public String profile() {
        return "profile.html";
    }
    
    @GetMapping("/jobsPage")
    public String jobs() {
        return "jobs.html";
    }
}
