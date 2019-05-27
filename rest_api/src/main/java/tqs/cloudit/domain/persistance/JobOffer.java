package tqs.cloudit.domain.persistance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author joaoalegria
 */
@Entity
@Table(name="joboffer")
public class JobOffer {
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
    
    /**
     * Job creator
     */
    @ManyToOne
    @JoinColumn(name="id", nullable=false)
    private User creator;
    
    /**
     * Job worker
     */
    @ManyToOne
    @JoinColumn(name="id", nullable=false)
    private User worker;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    
    public JobOffer(tqs.cloudit.domain.rest.JobOffer jobOffer) {
        this.title = jobOffer.getTitle();
        this.description = jobOffer.getDescription();
        this.area = jobOffer.getArea();
        this.amount = jobOffer.getAmount();
        this.date = jobOffer.getDate();   
    }

    public JobOffer() {
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
    
    public User getWorker() {
        return worker;
    }

    public void setWorker(User worker) {
        this.worker = worker;
    }
    
}
