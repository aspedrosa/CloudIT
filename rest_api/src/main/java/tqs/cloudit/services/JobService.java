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
import tqs.cloudit.utils.Constants;
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
        List<tqs.cloudit.domain.persistance.JobOffer> offers = new ArrayList<>();
        while(offersIter.hasNext()){
            tqs.cloudit.domain.persistance.JobOffer jo = (tqs.cloudit.domain.persistance.JobOffer) offersIter.next();
            offers.add(jo);
        }
        
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.INFO_FETCHED_SUCCESS, offers);
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
        
        tqs.cloudit.domain.persistance.JobOffer oldJobOffer = this.jobRepository.getJobOffer(id);
        
        User aux = oldJobOffer.getCreator();
        aux.removeOffer(oldJobOffer);
        
        oldJobOffer.setTitle(jobOffer.getTitle());
        oldJobOffer.setDescription(jobOffer.getDescription());
        oldJobOffer.setArea(jobOffer.getArea());
        oldJobOffer.setAmount(jobOffer.getAmount());
        oldJobOffer.setDate(jobOffer.getDate());
        
        aux.addNewOffer(oldJobOffer);
        jobRepository.save(oldJobOffer);
        
        return ResponseBuilder.buildWithMessage(HttpStatus.OK, "Job offer edited with success.");
    }

    public ResponseEntity getJobOffersFromTextAmountAndDate(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate) {
        List<tqs.cloudit.domain.persistance.JobOffer> jobs = jobRepository.getJobOffersFromTextAmountAndDate(title, area, fromAmount, toAmount, fromDate, toDate);
        if(jobs.isEmpty()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, Constants.NO_JOB_FOR_ID);
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.JOB_FOUND_SUCCESS, jobs);
    }
    
    public ResponseEntity getJobOffersFromTextAmountAndDateOnlyTitle(String title, String area, double fromAmount, double toAmount, String fromDate, String toDate) {
        List<tqs.cloudit.domain.persistance.JobOffer> jobs = jobRepository.getJobOffersFromTextAmountAndDateOnlyTitle(title, area, fromAmount, toAmount, fromDate, toDate);
        if(jobs.isEmpty()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, Constants.NO_JOB_FOR_ID);
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.JOB_FOUND_SUCCESS, jobs);
    }
    
    public ResponseEntity getJobOffersFromTextAmountAndDateOnlyArea(String area, double fromAmount, double toAmount, String fromDate, String toDate) {
        List<tqs.cloudit.domain.persistance.JobOffer> jobs = jobRepository.getJobOffersFromTextAmountAndDateOnlyArea(area, fromAmount, toAmount, fromDate, toDate);
        if(jobs.isEmpty()){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, Constants.NO_JOB_FOR_ID);
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.JOB_FOUND_SUCCESS, jobs);
    }
    
    public ResponseEntity getSpecificOffer(long id) {
        tqs.cloudit.domain.persistance.JobOffer jo = jobRepository.getJobOffer(id);
        if(jo==null){
            return ResponseBuilder.buildWithMessage(HttpStatus.NOT_FOUND, Constants.NO_JOB_FOR_ID);
        }
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.JOB_FOUND_SUCCESS, jo);
    }

    public ResponseEntity getUserOffers(String name) {
        Set<tqs.cloudit.domain.persistance.JobOffer> offers = userRepository.getInfo(name).getMyOffers();

        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.INFO_FETCHED_SUCCESS, offers);
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
        
        return ResponseBuilder.buildWithMessageAndData(HttpStatus.OK, Constants.INFO_FETCHED_SUCCESS, offers);
    }
}
