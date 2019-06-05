package tqs.cloudit.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tqs.cloudit.domain.persistance.Message;

public interface MessageRepository extends CrudRepository<Message, Long> {
    
    @Query(value= "(select distinct * from message where origin=?1 and destination=?2) union all (select * from message where origin=?2 and destination=?1) order by date", nativeQuery=true)
    public List<Message> getMessages(String origin, String destination);
    
    @Query(value= "select distinct destination, name from message join user on destination=username where origin=?1", nativeQuery=true)
    public List<List<String>> getContacts(String user);

    @Query(value= "update message set message=?2 where id=?1", nativeQuery=true)
    public void updateMessage(Long get, String get0);
    
}
