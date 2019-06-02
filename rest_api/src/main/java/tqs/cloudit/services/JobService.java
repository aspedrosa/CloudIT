/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.User;
import tqs.cloudit.domain.rest.JobOffer;
import tqs.cloudit.repositories.JobRepository;
import tqs.cloudit.repositories.UserRepository;

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
        
        JSONObject response = new JSONObject();
        response.put("message", "Information fetched with success.");
        response.put("data", offers);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity registerOffer(String creator, JobOffer jobOffer) {
        if(!jobOffer.allDefined()){
            JSONObject response = new JSONObject();
            response.put("message", "Registration invalid. When registering you need to provide the offer title, description, working area, amount and date(to be delivered).");
            return new ResponseEntity(response,HttpStatus.NOT_ACCEPTABLE);
        }
        
        tqs.cloudit.domain.persistance.JobOffer jo =  new tqs.cloudit.domain.persistance.JobOffer(jobOffer);
        User aux = userRepository.getInfo(creator);
        jo.setCreator(aux);
        aux.addNewOffer(jo);
        jobRepository.save(jo);
        
        JSONObject response = new JSONObject();
        response.put("message", "Job offer registered with success.");
        return new ResponseEntity(response,HttpStatus.OK);
    }
    
    public ResponseEntity editOffer(Long id, JobOffer jobOffer) {
        if(!jobOffer.allDefined()){
            JSONObject response = new JSONObject();
            response.put("message", "Update invalid. When updating a job offer you need to provide the offer title, description, working area, amount and date(to be delivered).");
            return new ResponseEntity(response,HttpStatus.NOT_ACCEPTABLE);
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
        
        JSONObject response = new JSONObject();
        response.put("message", "Job offer edited with success.");
        return new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity getSpecificOffer(long id) {
        tqs.cloudit.domain.persistance.JobOffer jo = jobRepository.getJobOffer(id);
        if(jo==null){
            JSONObject response = new JSONObject();
            response.put("message", "No job found with that id.");
            return new ResponseEntity(response,HttpStatus.NOT_FOUND);
        }
        JSONObject response = new JSONObject();
        response.put("message", "Job offer found with success.");
        response.put("data", jo);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity getUserOffers(String name) {
        Set<tqs.cloudit.domain.persistance.JobOffer> offers = userRepository.getInfo(name).getMyOffers();
        JSONObject response = new JSONObject();
        response.put("message", "Information fetched with success.");
        response.put("data", offers);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    public ResponseEntity deleteSpecificOffer(long id) {
        if(jobRepository.existsById(id)){
            jobRepository.deleteById(id);
            
            JSONObject response = new JSONObject();
            response.put("message", "Job removed with success.");
            return new ResponseEntity(response,HttpStatus.OK);
        }
        
        JSONObject response = new JSONObject();
        response.put("message", "Job id doesn't exist.");
        return new ResponseEntity(response,HttpStatus.NOT_FOUND);
        
    }
    
    public ResponseEntity getUserOffersAccepted(String name) {
        Set<tqs.cloudit.domain.persistance.JobOffer> offers = userRepository.getInfo(name).getAcceptedOffers();
        
        JSONObject response = new JSONObject();
        response.put("message", "Information fetched with success.");
        response.put("data", offers);
        return new ResponseEntity(response,HttpStatus.OK);
    }
}
