package tqs.cloudit.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

/**
 * Representation of a company
 *
 * @author aspedrosa
 */
@Entity
public class Company {
    /**
     * Internal Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Brand name
     */
    private String name;

    /**
     * Owner of the company
     */
    @ManyToOne
    @JoinColumn(name="employer_id", nullable=false)
    private Employer owner;

    /**
     * Workers of the company
     */
    @ManyToMany
    @JoinColumn(name="employer_id", nullable=false)
    private Set<Employer> workers;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Employer getOwner() {
        return owner;
    }

    public void setOwner(Employer owner) {
        this.owner = owner;
    }

    public Set<Employer> getWorkers() {
        return workers;
    }

    public void setWorkers(Set<Employer> workers) {
        this.workers = workers;
    }

    
}
