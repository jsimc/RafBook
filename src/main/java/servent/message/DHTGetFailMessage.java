package servent.message;

import app.ServentInfo;

public class DHTGetFailMessage extends BasicMessage {
    public DHTGetFailMessage(ServentInfo sender, ServentInfo receiver, int key) {
        super(MessageType.FAIL_GET, sender, receiver, String.valueOf(key));
    }
}
