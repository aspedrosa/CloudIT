package tqs.cloudit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin
public class WebSiteController {
    
    @GetMapping(path = "/home")
    public String home() {
        return "login.html";
    }
    
    @GetMapping(path = "/welcome")
    public String welcome() {
        return "welcome.html";
    }
}
