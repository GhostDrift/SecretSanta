package Common;

import java.io.Serializable;

public class Message implements Serializable {
    public User user;
    public String message;
    public Message(User user, String message) {
        this.user = user;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user=" + user +
                ", message='" + message + '\'' +
                '}';
    }
}
