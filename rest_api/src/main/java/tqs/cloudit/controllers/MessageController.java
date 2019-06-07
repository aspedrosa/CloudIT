package tqs.cloudit.controllers;

import java.security.Principal;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.services.MessageService;

@Controller
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @MessageMapping("/message")
    public void message(Message message, Principal user) throws Exception {
        messageService.writeMessageByUsername(message, user.getName());
    }
    
    @MessageMapping("/allMessages")
    public void getAll(Message message, Principal user) throws Exception {
        messageService.getAll(message, user.getName());
    }
    
    @MessageMapping("/contacts")
    public void contacts(Principal user) throws Exception {
        messageService.contacts(user.getName());
    }
    
    @MessageMapping("/updateMessage")
    public void update(JSONArray input, Principal user) throws Exception {
        messageService.update(input, user.getName());
    }

}