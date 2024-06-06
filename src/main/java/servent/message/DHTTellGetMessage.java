package servent.message;

import app.MyFile;
import app.ServentInfo;

public class DHTTellGetMessage extends BasicMessage {

    private final int key;
    private final MyFile value;

    public DHTTellGetMessage(ServentInfo sender, ServentInfo receiver, int key, MyFile value) {
        super(MessageType.TELL_GET, sender, receiver);
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public MyFile getValue() {
        return value;
    }
}
