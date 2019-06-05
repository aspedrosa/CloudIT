package tqs.cloudit.repositories;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import tqs.cloudit.domain.persistance.Job;
import tqs.cloudit.domain.persistance.User;

/**
 *
 * @author joaoalegria
 */
@DataJpaTest
@RunWith(SpringRunner.class)
public class JobRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    /**
     * Test of getJobOffer method, of class JobRepository.
     */
    @Test
    public void testGetJobOffer() {
        System.out.println("getJobOffer");
        User user = new User();
        user.setUsername("joao");
        user.setPassword("123");
        this.entityManager.persist(user);
        this.entityManager.flush();
        Job jo = new Job("teste", "desc", "myArea", 2, "tomorrow", "Offer", user);
        assertEquals(null,this.jobRepository.getJobOffer(jo.getId()));
        this.entityManager.persist(jo);
        this.entityManager.flush();
        assertEquals(jo,this.jobRepository.getJobOffer(jo.getId()));
    }
    
}
