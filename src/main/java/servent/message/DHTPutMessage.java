package servent.message;

import app.ServentInfo;

public class DHTPutMessage extends BasicMessage {
    private static final long serialVersionUID = -1934709147043909222L;
    private final int key;
    private final String value;

    public DHTPutMessage(ServentInfo sender, ServentInfo receiver, int key, String value) {
        super(MessageType.PUT, sender, receiver);
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
