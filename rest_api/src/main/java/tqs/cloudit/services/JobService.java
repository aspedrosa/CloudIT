package tqs.cloudit.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.User;
import tqs.cloudit.domain.rest.JobOffer;
import tqs.cloudit.repositories.JobRepository;
import tqs.cloudit.repositories.UserRepository;
import tqs.cloudit.utils.ResponseBuilder;

/**
 *
 * @author joaoalegria
 */
@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public ResponseEntity getOffers() {
        Iterator offersIter = jobRepository.findAll().iterator();
        List<tqs.cloudit.domain.persistance.JobOffer> offers = new ArrayList();
        while(offersIter.hasNext()){
            tqs.cloudit.domain.persistance.JobOffer jo = (tqs.cloudit.domain.persistance.JobOffer) offersIter.next();
            offers.add(jo);
        }
        
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Information fetched with success.", offers);
    }

    public ResponseEntity registerOffer(String creator, JobOffer jobOffer) {
        if(!jobOffer.allDefined()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Registration invalid. When registering you need to provide the offer title, description, working area, amount and date(to be delivered).");
        }
        tqs.cloudit.domain.persistance.JobOffer jo =  new tqs.cloudit.domain.persistance.JobOffer(jobOffer);
        User aux = userRepository.getInfo(creator);
        jo.setCreator(aux);
        aux.addNewOffer(jo);
        jobRepository.save(jo);
        
        return ResponseBuilder.buildWithMessage(HttpStatus.OK, "Job offer registered with success.");
    }
    
    public ResponseEntity editOffer(Long id, JobOffer jobOffer) {
        if(!jobOffer.allDefined()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_ACCEPTABLE, "Update invalid. When updating a job offer you need to provide the offer title, description, working area, amount and date(to be delivered).");
        }
        
        tqs.cloudit.domain.persistance.JobOffer old_jobOffer = this.jobRepository.getJobOffer(id);
        
        User aux = old_jobOffer.getCreator();
        aux.removeOffer(old_jobOffer);
        
        old_jobOffer.setTitle(jobOffer.getTitle());
        old_jobOffer.setDescription(jobOffer.getDescription());
        old_jobOffer.setArea(jobOffer.getArea());
        old_jobOffer.setAmount(jobOffer.getAmount());
        old_jobOffer.setDate(jobOffer.getDate());
        
        aux.addNewOffer(old_jobOffer);
        jobRepository.save(old_jobOffer);
        
        return ResponseBuilder.buildWithMessage(HttpStatus.OK, "Job offer edited with success.");
    }

    public ResponseEntity getJobOffersFromTextAmountAndDate(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate) {
        List<tqs.cloudit.domain.persistance.JobOffer> jobs = jobRepository.getJobOffersFromTextAmountAndDate(title, area, fromAmount, toAmount, fromDate, toDate);
        if(jobs.isEmpty()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, "No job found with that id.");
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Job offer found with success.", jobs);
    }
    
    public ResponseEntity getJobOffersFromTextAmountAndDateOnlyTitle(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate) {
        List<tqs.cloudit.domain.persistance.JobOffer> jobs = jobRepository.getJobOffersFromTextAmountAndDateOnlyTitle(title, area, fromAmount, toAmount, fromDate, toDate);
        if(jobs.isEmpty()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, "No job found with that id.");
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Job offer found with success.", jobs);
    }
    
    public ResponseEntity getJobOffersFromTextAmountAndDateOnlyArea(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate) {
        List<tqs.cloudit.domain.persistance.JobOffer> jobs = jobRepository.getJobOffersFromTextAmountAndDateOnlyArea(area, fromAmount, toAmount, fromDate, toDate);
        if(jobs.isEmpty()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, "No job found with that id.");
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Job offer found with success.", jobs);
    }
    
    public ResponseEntity getSpecificOffer(long id) {
        tqs.cloudit.domain.persistance.JobOffer jo = jobRepository.getJobOffer(id);
        if(jo==null){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, "No job found with that id.");
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Job offer found with success.", jo);
    }

    public ResponseEntity getUserOffers(String name) {
        Set<tqs.cloudit.domain.persistance.JobOffer> offers = userRepository.getInfo(name).getMyOffers();

        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Information fetched with success.", offers);
    }

    public ResponseEntity deleteSpecificOffer(long id) {
        if(jobRepository.existsById(id)){
            jobRepository.deleteById(id);
            
            return ResponseBuilder.buildWithMessage(HttpStatus.OK, "Job removed with success.");
        }
        
        return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, "Job id doesn't exist.");

    }
    
    public ResponseEntity getUserOffersAccepted(String name) {
        Set<tqs.cloudit.domain.persistance.JobOffer> offers = userRepository.getInfo(name).getAcceptedOffers();
        
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Information fetched with success.", offers);
    }

    public ResponseEntity finishOffer(long id) {
        tqs.cloudit.domain.persistance.JobOffer jo = jobRepository.getJobOffer(id);
        jo.setFinished(true);
        
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, "Offer finished with success.", jo);
    }
}
