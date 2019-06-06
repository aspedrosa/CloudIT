package tqs.cloudit.domain.rest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    private String newPassword;

    /**
     * Common assignments on both constructors
     */
    private void baseAssignments(String username, String password, String name, String email, String type, List<String> interestedAreas) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.type = type;
        this.interestedAreas = new HashSet<>(interestedAreas);
    }

    public User(String username, String password, String name, String email, String type, List<String> interestedAreas, String newPassword) {
        baseAssignments(username, password, name, email, type, interestedAreas);
        this.newPassword = newPassword;
    }
    
    public User(String username, String password, String name, String email, String type, List<String> interestedAreas) {
        baseAssignments(username, password, name, email, type, interestedAreas);
        this.newPassword = null;
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

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
    
    public boolean allDefined(){
        Object[] tmp = new Object[]{this.username, this.password, this.name, this.email, this.type};
        for(Object aux : tmp){
            if(aux==null){
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", password=" + password + ", name=" + name + ", email=" + email + ", type=" + type + ", interestedAreas=" + interestedAreas + ", newPassword=" + newPassword + '}';
    }
    

}
