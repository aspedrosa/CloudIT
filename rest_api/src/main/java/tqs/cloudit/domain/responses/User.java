package tqs.cloudit.domain.responses;

import tqs.cloudit.domain.persistance.Area;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private String userType;
    private List<String> interestedAreas;

    public User(tqs.cloudit.domain.persistance.User user) {
        name = user.getName();
        email = user.getEmail();
        userType = user.getType();
        interestedAreas = new ArrayList<>();
        for (Area area : user.getInterestedAreas()) {
            interestedAreas.add(area.getArea());
        }
    }

    public User() {}

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUserType() {
        return userType;
    }

    public List<String> getInterestedAreas() {
        return interestedAreas;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setInterestedAreas(List<String> interestedAreas) {
        this.interestedAreas = interestedAreas;
    }
}
