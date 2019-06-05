package tqs.cloudit.controllers;

import java.security.Principal;
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
        messageService.getAllMessagesFromConversation(user.getName(), message.getDestination());
    }
    
    @MessageMapping("/contacts")
    public void contacts(Principal user) throws Exception {
        messageService.getContactsFromUser(user.getName());
    }

}