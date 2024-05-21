package servent.message;

public class DHTPutMessage extends BasicMessage {
    private static final long serialVersionUID = -1934709147043909222L;
    private final int key;
    private final String value;

    public DHTPutMessage(int senderPort, int receiverPort, int key, String value) {
        super(MessageType.PUT, senderPort, receiverPort);
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
