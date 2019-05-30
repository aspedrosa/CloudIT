package tqs.cloudit.domain.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author aspedrosa
 */
@Entity
@Table(name="area")
public class Area {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
    private String area;
    
    @ManyToMany(mappedBy="interestedAreas")
    @JsonIgnore
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

    public Area(String area) {
        this.area = area;
    }

}
