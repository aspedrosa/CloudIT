/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tqs.cloudit.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.domain.persistance.User;
import tqs.cloudit.repositories.JobRepository;
import tqs.cloudit.repositories.MessageRepository;
import tqs.cloudit.repositories.UserRepository;

/**
 *
 * @author joaoalegria
 */
@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    private static final String BASE_PATH = "/secured/queue/";
    
    public void writeMessageByUsername(Message message, String user) {
        String destinationUser =  message.getDestination();
        message.setOrigin(user);
        message.setDate(System.currentTimeMillis());
        
        messageRepository.save(message);
        
        message.setDestination(null);
        simpMessagingTemplate.convertAndSend(BASE_PATH+destinationUser, message);
    }

    public void getAll(Message message, String user) {
        JSONObject output = new JSONObject();
        output.put("messages", messageRepository.getMessages(user, message.getDestination()));
        simpMessagingTemplate.convertAndSend(BASE_PATH+user, output);
    }
    
    public void contacts(String user) {
        JSONObject output = new JSONObject();
        output.put("contacts", messageRepository.getContacts(user));
        simpMessagingTemplate.convertAndSend(BASE_PATH+user, output);
    }
    
    public void update(JSONArray input, String worker) {
        if(((String)input.get(2)).equals("accept")){
            tqs.cloudit.domain.persistance.Job jo = jobRepository.getJobOffer(new Long((int)input.get(3)));
            User user  = userRepository.getInfo(worker);
            jo.setWorker(user);
            user.addAcceptedOffer(jo);
            jobRepository.save(jo);
            userRepository.save(user);
        }
        Message m = messageRepository.getMessage(new Long((int)input.get(0)));
        m.setMessage((String)input.get(1));
        messageRepository.save(m);
    }
    
    
}
