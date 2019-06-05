package tqs.cloudit.controllers;

import java.security.Principal;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.repositories.MessageRepository;

@Controller
public class MessageController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private MessageRepository messageRepository;
    
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

}