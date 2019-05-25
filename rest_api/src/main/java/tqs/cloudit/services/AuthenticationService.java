/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.services;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.Area;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.repositories.AreaRepository;
import tqs.cloudit.repositories.UserRepository;

/**
 *
 * @author joaoalegria
 */
@Service
public class AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AreaRepository areaRepository;
    
    private BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
    private Logger l = LoggerFactory.getLogger(AuthenticationService.class);
    
    public ResponseEntity register(User user) {
        if(!user.allDefined()){
            JSONObject response = new JSONObject();
            response.put("status", 1);
            response.put("message", "Registration invalid. When registering you need to provide username, password, name, email, type of user and optionally interest areas.");
            return ResponseEntity.badRequest().body(response);
        }
        if(userRepository.getUsernames().contains(user.getUsername())){
            JSONObject response = new JSONObject();
            response.put("status", 1);
            response.put("message", "Registration invalid. Username must be unique. This username already exists.");
            return ResponseEntity.badRequest().body(response);
        }
        if(userRepository.getMails().contains(user.getEmail())){
            JSONObject response = new JSONObject();
            response.put("status", 1);
            response.put("message", "Registration invalid. Your email must be unique. This email is already registered in the platform.");
            return ResponseEntity.badRequest().body(response);
        }
        
        tqs.cloudit.domain.persistance.User pUser=new tqs.cloudit.domain.persistance.User(user);
        if(pUser.getInterestedAreas()!=null){
            for(Area a : pUser.getInterestedAreas()){
                if(this.areaRepository.existsByArea(a.getArea())==0)
                    this.areaRepository.save(a);
            }
        }
        pUser.setPassword(this.bcpe.encode(pUser.getPassword()));
        this.userRepository.save(pUser);
        
        JSONObject response = new JSONObject();
        response.put("status", 0);
        response.put("message", "Registered with success");
        return ResponseEntity.ok(response);
    }
}
