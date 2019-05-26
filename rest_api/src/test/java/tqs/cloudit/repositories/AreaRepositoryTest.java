package tqs.cloudit.repositories;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import tqs.cloudit.domain.persistance.Area;

/**
 *
 * @author joaoalegria
 */
@DataJpaTest
@RunWith(SpringRunner.class)
public class AreaRepositoryTest {
    
    @Autowired
    private TestEntityManager entityManager;
 
    @Autowired
    private AreaRepository areaRepository;

    /**
     * Test of existsByArea method, of class AreaRepository.
     */
    @Test
    public void testExistsByArea() {
        System.out.println("existsByArea");
        String areaName = "Virtualization";
        assertEquals(0,this.areaRepository.existsByArea(areaName));
        Area area = new Area(areaName);
        this.entityManager.persist(area);
        this.entityManager.flush();
        assertEquals(1,this.areaRepository.existsByArea(areaName));
    }

}
