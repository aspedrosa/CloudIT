package tqs.cloudit.domain;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

/**
 * Representation of an employer
 *
 * @author aspedrosa
 */
@Entity
public class Employer {
    /**
     * Internal Id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    /**
     * Full name
     */
    private String name;

    /**
     * Personal email
     */
    private String email;

    /**
     * Password to hash
     */
    private String password;
    
    /**
     * Set of interested areas
     */
    private Set<String> interestAreas;

    /**
     * Set of companies that an employer is associated with (works or owns)
     */
    @ManyToMany
    @JoinColumn(name="company_id", nullable=false)
    private Set<Company> companies;
}
