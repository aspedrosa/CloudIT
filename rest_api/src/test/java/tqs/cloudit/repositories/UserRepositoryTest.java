package tqs.cloudit.repositories;

import java.util.Collections;
import java.util.List;
import org.assertj.core.util.Lists;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import tqs.cloudit.domain.persistance.User;

/**
 *
 * @author joaoalegria
 */
@DataJpaTest
@RunWith(SpringRunner.class)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Test of getPass method, of class UserRepository.
     */
    @Test
    public void testGetPass() {
        System.out.println("getPass");
        String username = "joao";
        String expResult = "123";
        String result = userRepository.getPass(username);
        assertEquals(null, result);
        User user =  new User();
        user.setId(0);
        user.setUsername("joao");
        user.setPassword("123");
        this.entityManager.persist(user);
        this.entityManager.flush();
        result = userRepository.getPass(username);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNames method, of class UserRepository.
     */
    @Test
    public void testGetNames() {
        System.out.println("getNames");
        List<String> result = userRepository.getUsernames();
        assertEquals(Collections.EMPTY_LIST, result);
        User user =  new User();
        user.setId(0);
        user.setUsername("joao");
        user.setPassword("123");
        this.entityManager.persist(user);

        User user1 =  new User();
        user1.setId(0);
        user1.setUsername("joana");
        user1.setPassword("123");
        this.entityManager.persist(user1);
        this.entityManager.flush();
        
        result = userRepository.getUsernames();
        assertEquals(Lists.list("joao","joana"), result);
    }

    /**
     * Test of getMails method, of class UserRepository.
     */
    @Test
    public void testGetMails() {
        System.out.println("getMails");
        List<String> result = userRepository.getMails();
        assertEquals(Collections.EMPTY_LIST, result);
        User user =  new User();
        user.setId(0);
        user.setUsername("joao");
        user.setPassword("123");
        user.setEmail("emaildojoao@mail.com");
        this.entityManager.persist(user);
        
        User user1 =  new User();
        user1.setId(0);
        user1.setUsername("joana");
        user1.setPassword("123");
        user1.setEmail("emaildajoana@mail.com");
        this.entityManager.persist(user1);
        this.entityManager.flush();
        
        result = userRepository.getMails();
        assertEquals(Lists.list("emaildojoao@mail.com","emaildajoana@mail.com"), result);
    }
    
}
