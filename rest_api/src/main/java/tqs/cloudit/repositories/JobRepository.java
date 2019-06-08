package tqs.cloudit.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tqs.cloudit.domain.persistance.Job;

public interface JobRepository extends CrudRepository<Job, Long> {

    @Query(value = "SELECT * FROM job WHERE id = ?1 AND NOT finished AND worker IS NULL", nativeQuery = true)
    public Job getJobById(Long id);

    @Query(value= "select * from job where id=?1 and not finished and worker is null", nativeQuery=true)
    public Job getJobOffer(Long id);
    
    @Query(value= "select * from job where type='Offer' and not finished and worker is null", nativeQuery=true)
    public List<Job> getOffers();
    
    @Query(value= "select * from job where type='Proposal' and not finished and worker is null", nativeQuery=true)
    public List<Job> getProposals();
    
    @Query(value="select * from job where title like concat('%',?1,'%') or description like concat('%',?2,'%') and type='Offer' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobOffersFromTextOnly(String title, String description);
    
    @Query(value="select * from job where (title like concat('%',?1,'%') or description like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and type='Offer' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobOffersFromTextAndAmount(String title, String description, double fromAmount, double toAmount);
    
    @Query(value="select * from job where (title like '%?1%'concat('%',?1,'%') or description like concat('%',?2,'%')) and date <= ?3 and date >= ?4 and type='Offer' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobOffersFromTextAndDate(String title, String description, String fromDate, String toDate);
    
    @Query(value="select * from job where (title like concat('%',?1,'%') or description like concat('%',?1,'%') or area like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and date >= ?5 and date <= ?6 and type='Offer' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobOffersFromTextAmountAndDate(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate);
    
    @Query(value="select * from job where (title like concat('%',?1,'%') or description like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and date >= ?5 and date <= ?6 and type='Offer' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobOffersFromTextAmountAndDateOnlyTitle(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate);
    
    @Query(value="select * from job where area like concat('%',?1,'%') and amount >= ?2 and amount <= ?3 and date >= ?4 and date <= ?5 and type='Offer' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobOffersFromTextAmountAndDateOnlyArea(String area, double fromAmount, double toAmount, String fromDate, String toDate);

    @Query(value="SELECT * FROM job WHERE amount >= ?1 AND amount <= ?2 AND date >= ?3 AND date <= ?4 AND type='Offer' AND NOT finished AND worker IS NULL", nativeQuery = true)
    List<Job> getJobOffersFromAmountAndDate(double fromAmount, double toAmount, String fromDate, String toDate);

    @Query(value="select * from job where (title like concat('%',?1,'%') or description like concat('%',?1,'%') or area like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and date >= ?5 and date <= ?6 and type='Proposal' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobProposalFromTextAmountAndDate(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate);
    
    @Query(value="select * from job where (title like concat('%',?1,'%') or description like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and date >= ?5 and date <= ?6 and type='Proposal' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobProposalFromTextAmountAndDateOnlyTitle(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate);
    
    @Query(value="select * from job where area like concat('%',?1,'%') and amount >= ?2 and amount <= ?3 and date >= ?4 and date <= ?5 and type='Proposal' and not finished and worker is null", nativeQuery = true)
    public List<Job> getJobProposalFromTextAmountAndDateOnlyArea(String area, double fromAmount, double toAmount, String fromDate, String toDate);

    @Query(value= "select * from job where id=?1 and not finished", nativeQuery=true)
    public Job getAcceptedOffer(long id);

    @Query(value="SELECT * FROM job WHERE amount >= ?1 AND amount <= ?2 AND date >= ?3 AND date <= ?4 AND type='Proposal' AND NOT finished AND worker IS NULL", nativeQuery = true)
    List<Job> getJobProposalFromAmountAndDate(double fromAmount, double toAmount, String fromDate, String toDate);
}
