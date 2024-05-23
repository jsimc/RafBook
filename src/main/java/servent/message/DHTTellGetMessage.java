package servent.message;

import app.ServentInfo;

public class DHTTellGetMessage extends BasicMessage {

    private final int key;
    private final String value;

    public DHTTellGetMessage(ServentInfo sender, ServentInfo receiver, int key, String value) {
        super(MessageType.TELL_GET, sender, receiver);
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
