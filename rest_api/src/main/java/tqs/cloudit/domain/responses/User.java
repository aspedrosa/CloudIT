package tqs.cloudit.domain.responses;

import tqs.cloudit.domain.persistance.Area;
import tqs.cloudit.domain.persistance.JobOffer;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String email;
    private String userType;
    private List<String> interestedAreas;
    private List<JobOffer> jobOffers;

    public User(tqs.cloudit.domain.persistance.User user) {
        name = user.getName();
        email = user.getEmail();
        userType = user.getType();
        interestedAreas = new ArrayList<>();
        for (Area area : user.getInterestedAreas()) {
            interestedAreas.add(area.getArea());
        }
        jobOffers = new ArrayList<>(user.getMyOffers());
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

    public List<JobOffer> getJobOffers() {
        return jobOffers;
    }

}
