package tqs.cloudit.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
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
