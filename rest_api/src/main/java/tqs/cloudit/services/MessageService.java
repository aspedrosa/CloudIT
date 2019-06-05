package tqs.cloudit.services;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.repositories.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    public void getAllMessagesFromConversation(String username, String destination) {
        JSONObject output = new JSONObject();
        output.put("messages", messageRepository.getMessages(username, destination));
        simpMessagingTemplate.convertAndSend("/secured/queue/"+username, output);
    }
    
    public void getContactsFromUser(String username) {
        JSONObject output = new JSONObject();
        output.put("contacts", messageRepository.getContacts(username));
        simpMessagingTemplate.convertAndSend("/secured/queue/"+username, output);
    }
    
    public void writeMessageByUsername(Message message, String username) {
        String destinationUser =  message.getDestination();
        message.setOrigin(username);
        message.setDate(System.currentTimeMillis());
        
        messageRepository.save(message);
        
        message.setDestination(null);
        simpMessagingTemplate.convertAndSend("/secured/queue/"+destinationUser, message);
    }
    
    public void updateMessage(JSONArray input) {
        messageRepository.updateMessage((Long)input.get(0), (String)input.get(1));
    }
}
