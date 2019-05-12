package tqs.cloudit.domain.rest;

import java.util.Set;
import tqs.cloudit.domain.persistance.Area;

/**
 *
 * @author aspedrosa
 */
public class User {

    private String username;

    private String password;

    private String name;

    private String email;

    private String type;

    private Set<String> interestedAreas;

    public User(String username, String password, String name, String email, String type, Set<String> interestedAreas) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.type = type;
        this.interestedAreas = interestedAreas;
    }

    public User() {
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

    public Set<String> getInterestedAreas() {
        return interestedAreas;
    }

    public void setInterestedAreas(Set<String> interestedAreas) {
        this.interestedAreas = interestedAreas;
    }

}
