package tqs.cloudit.services;

import java.util.TreeSet;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import tqs.cloudit.domain.rest.*;

@SpringBootTest
public class AuthenticationServiceTest {
    
    @Autowired
    private AuthenticationService authenticationService;    
    
    public AuthenticationServiceTest() {
    }
    
    @Before
    public static void setUpClass() {
    }
    
    @After
    public static void tearDownClass() {
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
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }
}