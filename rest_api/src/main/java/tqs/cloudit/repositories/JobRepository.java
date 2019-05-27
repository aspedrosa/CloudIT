/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tqs.cloudit.domain.persistance.JobOffer;

/**
 *
 * @author joaoalegria
 */
public interface JobRepository extends CrudRepository<JobOffer, Long>{
    
    @Query(value= "select * from joboffer where id=?1", nativeQuery=true)
    public JobOffer getJobOffer(Long id);
    
    @Query(value= "select * from joboffer where creator=?1", nativeQuery=true)
    public List<JobOffer> getUserOffers(String name);

    public List<JobOffer> getUserOffersAccepted(String name);
}
