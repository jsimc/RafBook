package servent.message;

import app.ServentInfo;

import java.util.concurrent.atomic.AtomicInteger;

public class DHTGetMessage extends BasicMessage {
    private static final long serialVersionUID = -1934907147043909222L;
    private final int key;
    private final ServentInfo originalSender;
    private final int counter;

    public DHTGetMessage(ServentInfo sender, ServentInfo receiver, int key, ServentInfo originalSender, int counter) {
        super(MessageType.ASK_GET, sender, receiver, String.valueOf(key));
        this.key = key;
        this.originalSender = originalSender;
        this.counter = counter;
    }
    public int getKey() {
        return key;
    }

    public ServentInfo getOriginalSender() {
        return originalSender;
    }

    public int getCounter() {
        return counter;
    }
}
