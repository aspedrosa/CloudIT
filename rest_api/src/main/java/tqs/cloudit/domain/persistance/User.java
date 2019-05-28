package tqs.cloudit.domain.persistance;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author aspedrosa
 */
@Entity
@Table(name="user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String username;

    private String password;

    private String name;

    private String email;
    
    private String type;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
        name = "UserArea", 
        joinColumns = @JoinColumn(name = "userId"), 
        inverseJoinColumns = @JoinColumn(name = "areaId"))
    private Set<Area> interestedAreas;

    public User(tqs.cloudit.domain.rest.User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.name = user.getName();
        this.email = user.getEmail();
        this.type = user.getType();
        this.interestedAreas = new HashSet<Area>();
        if(user.getInterestedAreas()!=null){
            for(String area : user.getInterestedAreas()){
                this.interestedAreas.add(new Area(area));
            }
        }
        
    }

    public User() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<Area> getInterestedAreas() {
        return interestedAreas;
    }
    
    public void setInterestedAreas(Set<String> interestedAreas) {
        this.interestedAreas.clear();
        if(interestedAreas!=null){
            for(String area : interestedAreas){
                this.interestedAreas.add(new Area(area));
            }
        }
    }

}
