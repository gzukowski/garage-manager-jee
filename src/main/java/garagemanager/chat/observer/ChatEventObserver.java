package garagemanager.chat.observer;

import garagemanager.chat.dto.ChatMessage;
import garagemanager.chat.websocket.SessionManager;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import java.util.logging.Logger;

@ApplicationScoped
public class ChatEventObserver {

    private static final Logger LOG = Logger.getLogger(ChatEventObserver.class.getName());

    @Inject
    private SessionManager sessionManager;

    public void onChat(@Observes ChatMessage msg) {
        if (msg == null) return;
        String json = msg.toJson();
        LOG.info(() -> "Chat event observed - sender=" + msg.getSender() + " recipient=" + msg.getRecipient());
        if (msg.getRecipient() == null || "all".equalsIgnoreCase(msg.getRecipient())) {
            sessionManager.sendToAll(json);
        } else {
            sessionManager.sendToUser(msg.getRecipient(), json);
            // also send to sender to reflect message in sender's chat (optional)
            if (msg.getSender() != null && !msg.getSender().equals(msg.getRecipient())) {
                sessionManager.sendToUser(msg.getSender(), json);
            }
        }
    }

}
