package tqs.cloudit.services;

import java.util.Arrays;
import java.util.List;
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

    public ResponseEntity register(User user) {
        if(!user.allDefined()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Registration invalid. When registering you need to provide username, password, name, email, type of user and optionally interest areas.");
        }
        if(userRepository.usernameExists(user.getUsername()) > 0){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Registration invalid. Username must be unique. This username already exists.");
        }
        if(userRepository.emailExists(user.getEmail()) > 0){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Registration invalid. Your email must be unique. This email is already registered in the platform.");
        }
        List<String> supportedTypes = Arrays.asList("Freelancer", "Employer");

        if(!supportedTypes.contains(user.getType())){
           return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Registration invalid. User type must be Freelancer or Employer.");
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
        
        return ResponseBuilder.buildWithMessage(HttpStatus.OK, "Registered with success");
    }

    public ResponseEntity login(String name) {
        String type = userRepository.getType(name);

        JSONObject aux = new JSONObject();
        aux.put("type", type);

        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Logged in with success", aux);
    }
}
