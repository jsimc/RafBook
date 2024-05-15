package servent.message;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicMessage implements Message {

    private static final long serialVersionUID = -9075856313609777945L;

    private final MessageType type;
    private final int senderPort;
    private final int receiverPort;
    private final String messageText;
    private static AtomicInteger messageCounter = new AtomicInteger(0);
    private final int messageId;

    public BasicMessage(MessageType type, int senderPort, int receiverPort) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = "";

        this.messageId = messageCounter.getAndIncrement();
    }

    public BasicMessage(MessageType type, int senderPort, int receiverPort, String messageText) {
        this.type = type;
        this.senderPort = senderPort;
        this.receiverPort = receiverPort;
        this.messageText = messageText;

        this.messageId = messageCounter.getAndIncrement();
    }

    @Override
    public int getSenderPort() {
        return this.senderPort;
    }

    @Override
    public int getReceiverPort() {
        return this.receiverPort;
    }

    @Override
    public String getReceiverIpAddress() {
        return "localhost";
    }

    @Override
    public MessageType getMessageType() {
        return this.type;
    }

    @Override
    public String getMessageText() {
        return this.messageText;
    }

    @Override
    public int getMessageId() {
        return this.messageId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasicMessage that = (BasicMessage) o;
        return senderPort == that.senderPort && messageId == that.messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(senderPort, messageId);
    }

    @Override
    public String toString() {
        return "[" + getSenderPort() + "|"
                + getMessageId() + "|"
                + getMessageText() + "|"
                + getMessageType() + "|"
                + getReceiverPort() + "]";
    }
}
