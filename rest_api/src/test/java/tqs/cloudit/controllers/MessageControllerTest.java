package tqs.cloudit.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.TimeoutException;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.domain.rest.User;

/**
 *
 * @author fp
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MessageControllerTest {
    
    private static User user1, user2;
    private static Message msg1;
    static {
        user1 = new User();
        user1.setUsername("joao");
        user1.setPassword("123");
        user1.setName("Joao");
        user1.setEmail("emaidojoao@mail.com");
        user1.setType("Freelancer");
        
        user2 = new User();
        user2.setUsername("filipe");
        user2.setPassword("123");
        user2.setName("Filipe");
        user2.setEmail("emaidofilipe@mail.com");
        user2.setType("Employer");
        
        msg1 = new Message();
        msg1.setDate(System.currentTimeMillis());
        msg1.setDestination(user2.getUsername());
        msg1.setId(1L);
        msg1.setMessage("test message");
        msg1.setOrigin(user1.getUsername());
    }
    
    private CompletableFuture<Message> completableFuture;

    /**
     * Test of message method, of class MessageController.
     */
    @Test
    public void testMessage() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("message");
        /*
        Mockito.when(service.writeMessageByUsername(msg1, user1.getUsername()));
        mvc.perform(get("/message")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
        */
        
        
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect("ws://localhost:8080/secured/messageCenter", new StompSessionHandlerAdapter() {
        }).get(1, SECONDS);
        
        JSONObject jo = new JSONObject();
        jo.put("message", "testMessage");
        jo.put("to", "teste");

        stompSession.subscribe("/secured/queue/teste", new CreateStompFrameHandler());
        stompSession.send("/secured/message", jo.toJSONString());

        Message msg = completableFuture.get(10, SECONDS);

        assertNotNull(msg);
        
    }

    /**
     * Test of getAll method, of class MessageController.
     */
    @Test
    public void testGetAll() {
        System.out.println("getAll");
        
    }

    /**
     * Test of contacts method, of class MessageController.
     */
    @Test
    public void testContacts() {
        System.out.println("contacts");
        
    }

    /**
     * Test of update method, of class MessageController.
     */
    @Test
    public void testUpdate() {
        System.out.println("update");
        
    }
    
    private class CreateStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return Message.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((Message) o);
        }
    }
    
    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }
    
}