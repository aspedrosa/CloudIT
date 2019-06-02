package tqs.cloudit.controllers;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class MessageWebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    private static final Logger log = LoggerFactory.getLogger(MessageWebSocketController.class);

    /**
     * Example of sending message to specific user using 'convertAndSendToUser()' and '/queue'
     */
    @MessageMapping("/secured/messageCenter")
    public void sendSpecific(@Payload Message msg, Principal user, @Header("simpSessionId") String sessionId) throws Exception {
        OutputMessage out = new OutputMessage(msg.getFrom(), msg.getText(), new SimpleDateFormat("HH:mm").format(new Date()));
        System.out.println(msg);
        simpMessagingTemplate.convertAndSendToUser(msg.getTo(), "/secured/user/queue/specific-user", out.time);
    }
    
    public class Message {

        private String from;
        private String to;
        private String text;

        public String getFrom() {
            return from;
        }
        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }
        public void setTo(String to) {
            this.to = to;
        }
        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }
    
    public class OutputMessage extends Message {

        private String time;

        public OutputMessage(final String from, final String text, final String time) {
            setFrom(from);
            setText(text);
            this.time = time;
        }

        public String getTime() {
            return time;
        }
        public void setTime(String time) { this.time = time; }
    }
}