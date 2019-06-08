package tqs.cloudit.domain.persistance;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author joaoalegria
 */
@Entity
@Table(name="job")
public class Job {
    /**
     * Internal Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Job title
     */
    private String title;
    
    /**
     * Job description
     */
    private String description;
    
    /**
     * Job area
     */
    private String area;
    
    /**
     * Job amount
     */
    private Integer amount;
    
    /**
     * Job deliver date
     */
    private String date;
    
    private boolean finished;
    
    /**
     * Job type
     */
    private String type;
    
    /**
     * Job creator
     */
    @ManyToOne
    @JoinColumn(name = "creator", nullable=false)
    @JsonIgnore
    private User creator;
    
    /**
     * Job worker
     */
    @ManyToOne
    @JoinColumn(name = "worker", nullable=true)
    @JsonIgnore
    private User worker;

    @ManyToMany(mappedBy="favouriteJobs")
    @JsonIgnore
    private Set<User> favouritedUsers = new HashSet<>();

    public Job(tqs.cloudit.domain.rest.Job jobOffer) {
        this.title = jobOffer.getTitle();
        this.description = jobOffer.getDescription();
        this.area = jobOffer.getArea();
        this.amount = jobOffer.getAmount();
        this.date = jobOffer.getDate();
        this.type = jobOffer.getType();
    }
    
    public Job(String title, String description, String area, int amount, String date, String type, User creator) {
        this.title = title;
        this.description = description;
        this.area = area;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.creator = creator;
    }

    public Job() {
    }
    
    @JsonGetter("creator")
    public JSONObject getTheName() {
        JSONObject owner = new JSONObject();
        owner.put("name", creator.getName());
        owner.put("username", creator.getUsername());
        owner.put("email", creator.getEmail());
        return owner;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
    
    
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }
    
    public Set<User> getFavouritedUsers() {
        return favouritedUsers;
    }

    public void setFavouritedUsers(Set<User> favouritedUsers) {
        this.favouritedUsers = favouritedUsers;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.id ^ (this.id >>> 32));
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
        final Job other = (Job) obj;

        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Job{" + "id=" + id + ", title=" + title + ", description=" + description + ", area=" + area + ", amount=" + amount + ", date=" + date + ", type=" + type + ", creator=" + creator + ", worker=" + worker + '}';
    }

    public void removeWorker() {
        this.worker=null;
    }
    
}
