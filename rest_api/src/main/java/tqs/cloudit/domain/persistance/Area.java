package tqs.cloudit.domain.persistance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tqs.cloudit.utils.AreaBeautifier;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
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
    
    @Column(unique=true)
    private String area;
    
    @ManyToMany(mappedBy="interestedAreas")
    @JsonIgnore
    private Set<User> users;

    public Area() {
    }
    
    public Area(String area) {
        this.area = AreaBeautifier.beautify(area);
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = AreaBeautifier.beautify(area);
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.area);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Area other = (Area) obj;

        return this.area.equalsIgnoreCase(other.area);
    }

    

}
