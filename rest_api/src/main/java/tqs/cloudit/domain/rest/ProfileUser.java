package tqs.cloudit.domain.rest;

import java.util.Set;
import tqs.cloudit.domain.persistance.Area;

/**
 *
 * @author aspedrosa
 */
public class ProfileUser {

    private String username;

    private String password;

    private String newPassword;

    private String name;

    private String email;

    private String type;

    private Set<String> interestedAreas;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String newPassword) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
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
