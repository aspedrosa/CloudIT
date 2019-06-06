package tqs.cloudit.controllers;

import java.security.Principal;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tqs.cloudit.domain.persistance.JobOffer;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.repositories.JobRepository;
import tqs.cloudit.repositories.MessageRepository;
import tqs.cloudit.repositories.UserRepository;

@Controller
public class MessageController {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private JobRepository jobRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @MessageMapping("/message")
    public void message(Message message, Principal user) throws Exception {
        String destinationUser =  message.getDestination();
        message.setOrigin(user.getName());
        message.setDate(System.currentTimeMillis());
        
        messageRepository.save(message);
        
        message.setDestination(null);
        simpMessagingTemplate.convertAndSend("/secured/queue/"+destinationUser, message);
    }
    
    @MessageMapping("/allMessages")
    public void getAll(Message message, Principal user) throws Exception {
        JSONObject output = new JSONObject();
        output.put("messages", messageRepository.getMessages(user.getName(), message.getDestination()));
        simpMessagingTemplate.convertAndSend("/secured/queue/"+user.getName(), output);
    }
    
    @MessageMapping("/contacts")
    public void contacts(Principal user) throws Exception {
        JSONObject output = new JSONObject();
        output.put("contacts", messageRepository.getContacts(user.getName()));
        simpMessagingTemplate.convertAndSend("/secured/queue/"+user.getName(), output);
    }
    
    @MessageMapping("/update")
    public void update(JSONArray input, Principal user) throws Exception {
        if(((String)input.get(2)).equals("accept")){
            JobOffer jo = jobRepository.getJobOffer((Long)input.get(3));
            jo.setWorker(userRepository.getInfo((String)input.get(4)));
        }
        messageRepository.updateMessage((Long)input.get(0), (String)input.get(1));
    }

}