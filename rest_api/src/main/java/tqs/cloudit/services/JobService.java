/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.rest.JobOffer;
import tqs.cloudit.repositories.JobRepository;

/**
 *
 * @author joaoalegria
 */
@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;
    
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
            response.put("message", "Registration invalid. When registering you need to provide the offer name, area, amount and date(to be delivered).");
            return new ResponseEntity(response,HttpStatus.NOT_ACCEPTABLE);
        }
        
        tqs.cloudit.domain.persistance.JobOffer jo =  new tqs.cloudit.domain.persistance.JobOffer(jobOffer);
        jo.setCreator(creator);
        jobRepository.save(jo);
        
        JSONObject response = new JSONObject();
        response.put("message", "Job offer registered with success.");
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
        List<tqs.cloudit.domain.persistance.JobOffer> offers = jobRepository.getUserOffers(name);
        
        JSONObject response = new JSONObject();
        response.put("message", "Information fetched with success.");
        response.put("data", offers);
        return new ResponseEntity(response,HttpStatus.OK);
    }
}
