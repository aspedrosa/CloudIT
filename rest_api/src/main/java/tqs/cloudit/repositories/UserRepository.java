package tqs.cloudit.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import tqs.cloudit.domain.persistance.User;

/**
 *
 * @author aspedrosa
 */
public interface UserRepository extends CrudRepository<User, Long> {
    
    @Query(value= "select password from user where username=?1", nativeQuery=true)
    public String getPass(String username);
    
    @Query(value= "select * from user where username=?1", nativeQuery=true)
    public User getInfo(String username);
}
