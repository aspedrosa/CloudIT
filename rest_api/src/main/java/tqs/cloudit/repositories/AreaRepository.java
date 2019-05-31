package tqs.cloudit.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tqs.cloudit.domain.persistance.Area;

/**
 *
 * @author aspedrosa
 */
public interface AreaRepository extends CrudRepository<Area, Long> {
    
    @Query(value= "select count(*) from area where area=?1", nativeQuery=true)
    public long existsByArea(String area);
    
}
