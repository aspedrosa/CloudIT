/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.Area;
import tqs.cloudit.domain.rest.Credentials;
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
        tqs.cloudit.domain.persistance.User pUser=new tqs.cloudit.domain.persistance.User(user);
        for(Area a : pUser.getInterestedAreas()){
            if(this.areaRepository.existsByArea(a.getArea())==0)
                this.areaRepository.save(a);
        }
        pUser.setPassword(this.bcpe.encode(pUser.getPassword()));
        this.userRepository.save(pUser);
        return ResponseEntity.ok("firmeza");
    }
    
    public ResponseEntity login(Credentials cred) {
        if(this.bcpe.matches(cred.getPassword(),this.userRepository.getPass(cred.getUsername()))){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
