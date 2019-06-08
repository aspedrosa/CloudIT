package tqs.cloudit.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.Area;
import tqs.cloudit.domain.persistance.Job;
import tqs.cloudit.domain.rest.User;
import tqs.cloudit.repositories.AreaRepository;
import tqs.cloudit.repositories.JobRepository;
import tqs.cloudit.repositories.UserRepository;
import tqs.cloudit.utils.ResponseBuilder;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AreaRepository areaRepository;
    
    private BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
    
    public ResponseEntity getUserInfoFromUsername(String username, boolean withPassword) {
        tqs.cloudit.domain.persistance.User user = this.userRepository.getInfo(username);
        if (!withPassword) {
            user.setPassword("");
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "User information found.", user);
    }
    public ResponseEntity updateUserInfo(User user) {
        tqs.cloudit.domain.persistance.User oldUser = this.userRepository.getInfo(user.getUsername());
        boolean changes = false;
        
        if(user.getName() != null && !user.getName().equals(oldUser.getName())) {
            oldUser.setName(user.getName());
            changes = true;
        }
        if(user.getEmail() != null && !user.getEmail().equals(oldUser.getEmail())) {
            if(userRepository.emailExists(user.getEmail()) > 0){
                return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Unable to update profile. Your email must be unique. This email is already registered in the platform.");
            }
            oldUser.setEmail(user.getEmail());
            changes = true;
        }
        if(user.getPassword() != null && user.getNewPassword() != null && !bcpe.matches(user.getNewPassword(), oldUser.getPassword())) {
            if(!bcpe.matches(user.getPassword(), oldUser.getPassword())) {
                return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Unable to update profile. In order to change password you need to type in the current one correctly.");
            }
            oldUser.setPassword(this.bcpe.encode(user.getNewPassword()));
            changes = true;
        }
        Set<String> interestedAreasStr = user.getInterestedAreas();
        if(interestedAreasStr != null) {
            Set<Area> interestedAreasSet = new HashSet<>();
            for(String s: interestedAreasStr){
                interestedAreasSet.add(new Area(s));
                if(this.areaRepository.existsByArea(s)==0) {
                    this.areaRepository.save(new Area(s));
                }
            }
            if(!interestedAreasSet.equals(oldUser.getInterestedAreas())) {
                oldUser.setInterestedAreas(interestedAreasStr);
                changes = true;
            }
        }

        String message;
        if(changes) {
            this.userRepository.save(oldUser);
            message = "User update successful.";
        } else {
            message = "No changes to the current user information were detected.";
        }
        
        return ResponseBuilder.buildWithMessage(HttpStatus.OK, message);

    }

    /**
     * Searches for all users that respect the given arguments
     *
     * @param name the user must have the given string on the name to be a match
     * @param interestedAreas the user must have all areas received to be a match
     * @param userType the user must be the type specified
     * @return All the users that match the arguments
     */
    public List<tqs.cloudit.domain.responses.User> searchUser(String name,
                                                              Set<String> interestedAreas,
                                                              String userType) {
        //transform string areas into persistence areas
        Set<Area> interestedAreasPersist = new HashSet<>();
        if (interestedAreas != null) {
            for (String area : interestedAreas) {
                interestedAreasPersist.add(new Area(area));
            }
        }

        Iterable<tqs.cloudit.domain.persistance.User> users = userRepository.userSearch(name, userType);
        Iterator<tqs.cloudit.domain.persistance.User> it = users.iterator();

        List<tqs.cloudit.domain.responses.User> matchedUsers = new ArrayList<>();

        while (it.hasNext()) {
            tqs.cloudit.domain.persistance.User possibleMatchUser = it.next();

            if (possibleMatchUser.getInterestedAreas().containsAll(interestedAreasPersist)) {
                matchedUsers.add(new tqs.cloudit.domain.responses.User(possibleMatchUser));
            }
        }

        return matchedUsers;
    }

    public ResponseEntity searchUserByUsername(String username) {
        tqs.cloudit.domain.persistance.User user = userRepository.getInfo(username);
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "User found with success", user);
    }

    @Autowired
    JobRepository jobRepository;

    /**
     * Used to reduce code duplication.
     * Merges insertFavourite and removeFavourite in one method
     *
     * @param isAdd true if the user wants to add a job to his favourites
     *                 false to remove
     * @return true if the operation was completed with success, false if there's no
     *  job with the given id
     */
    private boolean updateFavourites(String username, long id, boolean isAdd) {
        Job job = jobRepository.getJobById(id);
        if (job == null) {
            return false;
        }

        tqs.cloudit.domain.persistance.User user = userRepository.getInfo(username);

        boolean wasUserOperationDone, wasJobOperationDone;
        if (isAdd) {
            wasUserOperationDone = job.getFavouritedUsers().add(user);
            wasJobOperationDone = user.getFavouriteJobs().add(job);
        }
        else {
            wasUserOperationDone = job.getFavouritedUsers().remove(user);
            wasJobOperationDone = user.getFavouriteJobs().remove(job);
        }

        if (wasUserOperationDone || wasJobOperationDone) {
            jobRepository.save(job);
            userRepository.save(user);
        }

        return true;
    }

    /**
     * Adds a job to user's favourites
     *
     * @param username of the user to remove a job from his favourites
     * @param id of the job to add to the favourites
     * @return true if job was removed from the favourites,
     *  false if there's no job with the given id
     */
    public boolean addFavourite(String username, long id) {
        return updateFavourites(username, id, true);
    }

    /**
     * Removes a job from user's favourites
     *
     * @param username of the user to remove a job from his favourites
     * @param id of the job
     * @return true if job was removed from the favourites,
     *  false if there's no job with the given id
     */
    public boolean deleteFavourite(String username, long id) {
        return updateFavourites(username, id, false);
    }

    /**
     * Gets all favourite jobs
     *
     * @param username of which user to retrieve favourite jobs
     * @return favourites jobs of user
     */
    public List<Job> getFavourites(String username) {
        tqs.cloudit.domain.persistance.User user = userRepository.getInfo(username);
        return new ArrayList<>(user.getFavouriteJobs());
    }
}
