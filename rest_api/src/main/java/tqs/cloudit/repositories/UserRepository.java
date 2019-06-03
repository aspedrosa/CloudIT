package tqs.cloudit.repositories;

import java.util.List;
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
    
    @Query(value= "select username from user", nativeQuery=true)
    public List<String> getUsernames();
    
    @Query(value= "select email from user", nativeQuery=true)
    public List<String> getMails();
    
    @Query(value= "select * from user where username=?1", nativeQuery=true)
    public User getInfo(String username);
    
    @Query(value= "select count(*) from user where username = ?1", nativeQuery=true)
    public int usernameExists(String username);

    @Query(value= "select count(*) from user where email = ?1", nativeQuery=true)
    public int emailExists(String email);

    @Query(value= "select type from user where username = ?1", nativeQuery=true)
    public String getType(String name);
}
