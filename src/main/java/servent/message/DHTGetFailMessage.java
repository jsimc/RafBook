package servent.message;

public class DHTGetFailMessage extends BasicMessage {
    public DHTGetFailMessage(int senderPort, int receiverPort, int key) {
        super(MessageType.FAIL_GET, senderPort, receiverPort, String.valueOf(key));
    }
}
