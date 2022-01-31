package sokol.messagingapp;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

public class ChatSocketHandler {

    @MessageMapping("/hello")
    @SendTo("/messagesWS/greetings")
    public String greeting(String message) throws Exception {
        System.out.println(message);
        Thread.sleep(1000); // simulated delay
        return "Pong";
    }

}
