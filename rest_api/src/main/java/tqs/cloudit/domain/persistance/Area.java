package tqs.cloudit.domain.persistance;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

/**
 *
 * @author aspedrosa
 */
@Entity
public class Area {
    @Id
    private String area;
    
    @ManyToMany(mappedBy="interestedAreas")
    private Set<User> users;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
