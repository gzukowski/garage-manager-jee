package garagemanager.chat.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class SessionManager {

    private static final Logger LOG = Logger.getLogger(SessionManager.class.getName());

    private final Map<String, Set<Session>> sessionsByUser = new ConcurrentHashMap<>();

    private final Map<String, String> userBySession = new ConcurrentHashMap<>();

    public void addSession(String username, Session session) {
        sessionsByUser.computeIfAbsent(username, k -> Collections.newSetFromMap(new ConcurrentHashMap<>()))
                .add(session);
        userBySession.put(session.getId(), username);
        LOG.info(() -> "WebSocket opened for user: " + username + " (session=" + session.getId() + ")");
    }

    public void removeSession(Session session) {
        var sid = session.getId();
        var username = userBySession.remove(sid);
        if (username != null) {
            var set = sessionsByUser.get(username);
            if (set != null) {
                set.remove(session);
                if (set.isEmpty()) sessionsByUser.remove(username);
            }
            LOG.info(() -> "WebSocket closed for user: " + username + " (session=" + sid + ")");
        }
    }

    public void sendToAll(String json) {
        sessionsByUser.keySet().forEach(u -> sendToUser(u, json));
    }

    public void sendToUser(String username, String json) {
        var set = sessionsByUser.get(username);
        if (set == null) return;
        for (Session s : set) {
            if (s.isOpen()) {
                try {
                    s.getBasicRemote().sendText(json);
                } catch (IOException e) {
                    LOG.log(Level.WARNING, "Failed to send message to " + username, e);
                }
            }
        }
    }

}
