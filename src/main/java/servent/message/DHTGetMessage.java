package servent.message;

import java.util.concurrent.atomic.AtomicInteger;

public class DHTGetMessage extends BasicMessage {
    private static final long serialVersionUID = -1934907147043909222L;
    private final int key;
    private final int originalSenderPort;

    private AtomicInteger counter;
    public DHTGetMessage(int senderPort, int receiverPort, int key, int originalSenderPort, AtomicInteger counter) {
        super(MessageType.ASK_GET, senderPort, receiverPort);
        this.key = key;
        this.originalSenderPort = originalSenderPort;
        this.counter = counter;
    }
    public int getKey() {
        return key;
    }
    public int getOriginalSenderPort() {
        return originalSenderPort;
    }

    public AtomicInteger getCounter() {
        return counter;
    }
}
