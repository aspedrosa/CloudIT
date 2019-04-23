package tqs.cloudit.repositories;

import org.springframework.data.repository.CrudRepository;

import tqs.cloudit.domain.persistance.User;

/**
 *
 * @author aspedrosa
 */
public interface UserRepository extends CrudRepository<User, Long> {
    
}
