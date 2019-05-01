/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.repositories;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.cloudit.domain.persistance.Area;

/**
 *
 * @author joaoalegria
 */
@DataJpaTest
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
