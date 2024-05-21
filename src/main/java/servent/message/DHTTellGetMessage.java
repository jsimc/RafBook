package servent.message;

public class DHTTellGetMessage extends BasicMessage {

    private final int key;
    private final String value;

    public DHTTellGetMessage(int senderPort, int receiverPort, int key, String value) {
        super(MessageType.TELL_GET, senderPort, receiverPort);
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
