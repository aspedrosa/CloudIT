package tqs.cloudit.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tqs.cloudit.domain.persistance.User;

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
    
    @Query(value ="SELECT * FROM user WHERE " +
                  "(?2 IS NULL OR (?2 IS NOT NULL AND type = ?2)) " +
                  "AND (?1 IS NULL OR (?1 IS NOT NULL AND name LIKE CONCAT('%', ?1, '%'))) ", nativeQuery = true)
    Iterable<User> userSearch(String name, String userType);

}
