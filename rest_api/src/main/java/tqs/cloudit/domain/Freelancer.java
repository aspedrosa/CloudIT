package tqs.cloudit.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Representation of a freelancer
 *
 * @author aspedrosa
 */
@Entity
public class Freelancer {

    /**
     * Internal Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Full name
     */
    private String name;

    /**
     * Personal email
     */
    private String email;

    /**
     * Password to hash
     */
    private String password;

    /**
     * Set of areas that this freelancer is able to work
     */
    private Set<String> areas;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getAreas() {
        return areas;
    }

    public void setAreas(Set<String> areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "Freelancer{" +
                "id=" + id +
                ", name=" + name +
                ", email=" + email +
                ", password=" + password +
                ", areas=" + areas +
                '}';
    }
}
