package tqs.cloudit.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.User;
import tqs.cloudit.repositories.UserRepository;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public ResponseEntity getUserInfoFromUsername(String username, boolean withPassword) {
        User user = this.userRepository.getInfo(username);
        if (!withPassword)
            user.setPassword("");
        return new ResponseEntity(user, HttpStatus.OK);
    }
}
