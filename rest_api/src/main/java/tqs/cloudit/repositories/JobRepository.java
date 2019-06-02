package tqs.cloudit.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tqs.cloudit.domain.persistance.JobOffer;

public interface JobRepository extends CrudRepository<JobOffer, Long>{
    
    @Query(value= "select * from joboffer where id=?1", nativeQuery=true)
    public JobOffer getJobOffer(Long id);
    
    @Query(value="select * from joboffer where title like concat('%',?1,'%') or description like concat('%',?2,'%')", nativeQuery = true)
    public List<JobOffer> getJobOffersFromTextOnly(String title, String description);
    
    @Query(value="select * from joboffer where (title like concat('%',?1,'%') or description like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4", nativeQuery = true)
    public List<JobOffer> getJobOffersFromTextAndAmount(String title, String description, double fromAmount, double toAmount);
    
    @Query(value="select * from joboffer where (title like '%?1%'concat('%',?1,'%') or description like concat('%',?2,'%')) and date <= ?3 and date >= ?4", nativeQuery = true)
    public List<JobOffer> getJobOffersFromTextAndDate(String title, String description, String fromDate, String toDate);
    
    @Query(value="select * from joboffer where (title like concat('%',?1,'%') or description like concat('%',?1,'%') or area like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and date >= ?5 and date <= ?6", nativeQuery = true)
    public List<JobOffer> getJobOffersFromTextAmountAndDate(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate);
    
    @Query(value="select * from joboffer where (title like concat('%',?1,'%') or description like concat('%',?2,'%')) and amount >= ?3 and amount <= ?4 and date >= ?5 and date <= ?6", nativeQuery = true)
    public List<JobOffer> getJobOffersFromTextAmountAndDateOnlyTitle(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate);
    
    @Query(value="select * from joboffer where area like concat('%',?1,'%') and amount >= ?2 and amount <= ?3 and date >= ?4 and date <= ?5", nativeQuery = true)
    public List<JobOffer> getJobOffersFromTextAmountAndDateOnlyArea(String area, double fromAmount, double toAmount, String fromDate, String toDate);

}