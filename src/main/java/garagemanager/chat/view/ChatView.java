package garagemanager.chat.view;

import garagemanager.chat.dto.ChatMessage;
import jakarta.enterprise.event.Event;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.security.Principal;

@Named
@ViewScoped
public class ChatView implements Serializable {

    private static final long serialVersionUID = 1L;

    private String recipient = "all";

    private String message;

    @Inject
    private Event<ChatMessage> event;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void send() {
        if (message == null || message.trim().isEmpty()) return;
        Principal p = FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        String sender = p != null ? p.getName() : null;
        ChatMessage msg = new ChatMessage(sender, recipient == null ? "all" : recipient, message);
        event.fire(msg);
        // clear message so JSF-render will update textarea
        this.message = "";
    }

    // f:ajax will pass AjaxBehaviorEvent when listener attribute is used
    public void send(AjaxBehaviorEvent event) {
        send();
    }

}
