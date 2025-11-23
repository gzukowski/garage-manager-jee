package garagemanager.chat.dto;

import java.time.Instant;

/**
 * Simple DTO representing a chat message.
 */
public class ChatMessage {

    private String sender;

    private String recipient; // null or "all" means broadcast

    private String text;

    private Instant timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String sender, String recipient, String text) {
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.timestamp = Instant.now();
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String toJson() {
        // very small JSON serializer to avoid additional dependencies
        return "{" +
                "\"sender\":\"" + escape(sender) + "\"," +
                "\"recipient\":\"" + escape(recipient) + "\"," +
                "\"text\":\"" + escape(text) + "\"," +
                "\"timestamp\":\"" + (timestamp != null ? timestamp.toString() : "") + "\"" +
                "}";
    }

    private String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n");
    }
}
