package garagemanager.chat.websocket;

import jakarta.enterprise.inject.spi.CDI;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import java.security.Principal;

@ServerEndpoint("/ws/chat")
public class ChatEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        SessionManager manager = CDI.current().select(SessionManager.class).get();
        Principal p = session.getUserPrincipal();
        String username = p != null ? p.getName() : session.getId();
        manager.addSession(username, session);
    }

    @OnClose
    public void onClose(Session session) {
        SessionManager manager = CDI.current().select(SessionManager.class).get();
        manager.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable t) {
        SessionManager manager = CDI.current().select(SessionManager.class).get();
        manager.removeSession(session);
    }

}
