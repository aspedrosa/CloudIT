package tqs.cloudit.domain.responses;


import tqs.cloudit.domain.persistance.Job;

import java.util.ArrayList;
import java.util.List;
import tqs.cloudit.domain.persistance.Area;

public class User {
    private String name;
    private String username;
    private String email;
    private String userType;
    private List<String> interestedAreas;
    private List<Job> jobOffers;

    public User(tqs.cloudit.domain.persistance.User user) {
        name = user.getName();
        username = user.getUsername();
        email = user.getEmail();
        userType = user.getType();
        interestedAreas = new ArrayList<>();
        for (Area area : user.getInterestedAreas()) {
            interestedAreas.add(area.getArea());
        }
        jobOffers = new ArrayList<>(user.getMyOffers());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    
    
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

    public List<Job> getJobOffers() {
        return jobOffers;
    }

}
