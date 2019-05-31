package tqs.cloudit.services;

import java.util.HashSet;
import java.util.Set;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.Area;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.repositories.AreaRepository;
import tqs.cloudit.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AreaRepository areaRepository;
    
    private BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
    
    public ResponseEntity getUserInfoFromUsername(String username, boolean withPassword) {
        JSONObject response = new JSONObject();
        tqs.cloudit.domain.persistance.User user = this.userRepository.getInfo(username);
        if (!withPassword) {
            user.setPassword("");
        }
        response.put("message", "User information found.");
        response.put("data", user);
        return new ResponseEntity(response, HttpStatus.OK);
    }
    public ResponseEntity updateUserInfo(User user) {
        JSONObject response = new JSONObject();
        tqs.cloudit.domain.persistance.User old_user = this.userRepository.getInfo(user.getUsername());
        boolean changes = false;
        
        if(user.getName() != null && !user.getName().equals(old_user.getName())) {
            old_user.setName(user.getName());
            changes = true;
        }
        if(user.getEmail() != null && !user.getEmail().equals(old_user.getEmail())) {
            if(userRepository.emailExists(user.getEmail()) > 0){
                response.put("message", "Unable to update profile. Your email must be unique. This email is already registered in the platform.");
                return new ResponseEntity(response,HttpStatus.NOT_ACCEPTABLE);
            }
            old_user.setEmail(user.getEmail());
            changes = true;
        }
        //System.out.println("Passwords: user.getPassword()='"+user.getPassword()+"', user.getNewPassword()='"+user.getNewPassword()+"', old_user.getPassword()='"+old_user.getPassword()+"'");
        if(user.getPassword() != null && user.getNewPassword() != null && !bcpe.matches(user.getNewPassword(), old_user.getPassword())) {
            if(!bcpe.matches(user.getPassword(), old_user.getPassword())) {
                response.put("message", "Unable to update profile. In order to change password you need to type in the current one correctly.");
                return new ResponseEntity(response,HttpStatus.NOT_ACCEPTABLE);
            }
            old_user.setPassword(this.bcpe.encode(user.getNewPassword()));
            changes = true;
        }
        Set<String> interestedAreasStr = user.getInterestedAreas();
        if(interestedAreasStr != null) {
            Set<Area> interestedAreasSet = new HashSet<>();
            for(String s: interestedAreasStr){
                interestedAreasSet.add(new Area(s));
                if(this.areaRepository.existsByArea(s)==0) {
                    this.areaRepository.save(new Area(s));
                }
            }
            if(!interestedAreasSet.equals(old_user.getInterestedAreas())) {
                old_user.setInterestedAreas(interestedAreasStr);
                changes = true;
            }
        }
        
        if(changes) {
            this.userRepository.save(old_user);
            response.put("message", "User update successful.");
        } else {
            response.put("message", "No changes to the current user information were detected.");
        }
        
        return new ResponseEntity(response,HttpStatus.OK);
        
    }
}
