package tqs.cloudit.services;

import java.util.TreeSet;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import tqs.cloudit.domain.rest.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AuthenticationServiceTest {
    
    @Autowired
    private AuthenticationService authenticationService;    
    
    public AuthenticationServiceTest() {
    }
    
    @Test
    public void testRegisterAndLogin() {
        authenticationService.register(new User("Test Username", "Test Password", "Test Name", "Test Email", "Test Type", new TreeSet<>()));
        assertEquals(ResponseEntity.ok().build(), authenticationService.login(new Credentials("Test username", "Test Password")));
    }
    
    @Test
    public void testWrongLogin() {
        assertEquals(ResponseEntity.notFound().build(), authenticationService.login(new Credentials("Test username", "Test Password")));
    }
}