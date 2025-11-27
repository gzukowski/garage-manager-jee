package garagemanager.chat.controller.rest;

import garagemanager.chat.dto.ChatMessage;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;

@Path("/chat")
public class RestChatController {

    @Context
    private HttpServletRequest request;

    private final Event<ChatMessage> event;

    @Inject
    public RestChatController(Event<ChatMessage> event) {
        this.event = event;
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    public void send(ChatMessage incoming) {
        if (incoming == null || incoming.getText() == null) return;
        var principal = request.getUserPrincipal();
        String sender = principal != null ? principal.getName() : "anonymous";
        incoming.setSender(sender);
        if (incoming.getRecipient() == null) incoming.setRecipient("all");
        event.fire(incoming);
    }

}
