package tqs.cloudit.services;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
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
import tqs.cloudit.utils.ResponseBuilder;

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
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "User information found.", user);
    }
    public ResponseEntity updateUserInfo(User user) {
        tqs.cloudit.domain.persistance.User old_user = this.userRepository.getInfo(user.getUsername());
        boolean changes = false;
        
        if(user.getName() != null && !user.getName().equals(old_user.getName())) {
            old_user.setName(user.getName());
            changes = true;
        }
        if(user.getEmail() != null && !user.getEmail().equals(old_user.getEmail())) {
            if(userRepository.emailExists(user.getEmail()) > 0){
                return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Unable to update profile. Your email must be unique. This email is already registered in the platform.");
            }
            old_user.setEmail(user.getEmail());
            changes = true;
        }
        //System.out.println("Passwords: user.getPassword()='"+user.getPassword()+"', user.getNewPassword()='"+user.getNewPassword()+"', old_user.getPassword()='"+old_user.getPassword()+"'");
        if(user.getPassword() != null && user.getNewPassword() != null && !bcpe.matches(user.getNewPassword(), old_user.getPassword())) {
            if(!bcpe.matches(user.getPassword(), old_user.getPassword())) {
                return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Unable to update profile. In order to change password you need to type in the current one correctly.");
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

        String message;
        if(changes) {
            this.userRepository.save(old_user);
            message = "User update successful.";
        } else {
            message = "No changes to the current user information were detected.";
        }
        
        return ResponseBuilder.buildWithMessage(HttpStatus.OK, message);

    }

    /**
     * Searches for all users that respect the given arguments
     *
     * @param name the user must have the given string on the name to be a match
     * @param interestedAreas the user must have all areas received to be a match
     * @param userType the user must be the type specified
     * @return All the users that match the arguments
     */
    public List<tqs.cloudit.domain.responses.User> searchUser(String name,
                                                              Set<String> interestedAreas,
                                                              String userType) {
        //transform string areas into persistence areas
        Set<Area> interestedAreasPersist = new HashSet<>();
        if (interestedAreas != null) {
            for (String area : interestedAreas) {
                interestedAreasPersist.add(new Area(area));
            }
        }

        Iterable<tqs.cloudit.domain.persistance.User> users = userRepository.userSearch(name, userType);
        Iterator<tqs.cloudit.domain.persistance.User> it = users.iterator();

        List<tqs.cloudit.domain.responses.User> matchedUsers = new ArrayList<>();

        while (it.hasNext()) {
            tqs.cloudit.domain.persistance.User possibleMatchUser = it.next();

            if (possibleMatchUser.getInterestedAreas().containsAll(interestedAreasPersist)) {
                matchedUsers.add(new tqs.cloudit.domain.responses.User(possibleMatchUser));
            }
        }

        return matchedUsers;
    }
}
