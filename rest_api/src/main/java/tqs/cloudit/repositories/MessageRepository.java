package tqs.cloudit.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tqs.cloudit.domain.persistance.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
    
    @Query(value= "select * from message where origin=?1 and destination=?2 sort date limit 20", nativeQuery=true)
    public long getMessages(String origin, String destination);
    
}
