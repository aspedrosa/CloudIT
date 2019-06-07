package tqs.cloudit.controllers;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import static java.util.concurrent.TimeUnit.SECONDS;
import java.util.concurrent.TimeoutException;
import org.json.simple.JSONObject;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import tqs.cloudit.domain.persistance.Message;
import tqs.cloudit.domain.persistance.User;
import tqs.cloudit.repositories.UserRepository;

/**
 *
 * @author fp
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MessageControllerTest {
    
    @Value("${local.server.port}")
    private int port;
    
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
    
    @Autowired
    UserRepository userRepository;

    /**
     * Test of message method, of class MessageController.
     */
    @Test
    @Ignore
    public void testMessage() throws InterruptedException, ExecutionException, TimeoutException {
        System.out.println("message");
        userRepository.save(user1);
        /*
        Mockito.when(service.writeMessageByUsername(msg1, user1.getUsername()));
        mvc.perform(get("/message")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message", is("Information passes with success.")));
        */
        
        String plainCredentials="joao:123";
        String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());

        final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Basic " + base64Credentials);
        
        
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        StompSession stompSession = stompClient.connect("ws://localhost:"+port+"/secured/messageCenter", headers, new StompSessionHandlerAdapter(){}).get(1, SECONDS);
        
        JSONObject jo = new JSONObject();
        jo.put("message", "testMessage");
        jo.put("to", "teste");

        stompSession.subscribe("/secured/queue/teste", new CreateStompFrameHandler());
        stompSession.send("/secured/message", jo);

        Message msg = completableFuture.get(10, SECONDS);

        assertNotNull(msg);
        
    }

    /**
     * Test of getAll method, of class MessageController.
     */
    @Test
    @Ignore
    public void testGetAll() {
        System.out.println("getAll");
        fail("not implemented");
        
    }

    /**
     * Test of contacts method, of class MessageController.
     */
    @Test
    @Ignore
    public void testContacts() {
        System.out.println("contacts");
        fail("not implemented");
        
    }

    /**
     * Test of update method, of class MessageController.
     */
    @Test
    @Ignore
    public void testUpdate() {
        System.out.println("update");
        fail("not implemented");
        
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