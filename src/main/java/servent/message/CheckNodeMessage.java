package servent.message;

import app.ServentInfo;

public class CheckNodeMessage extends BasicMessage {
    private static final long serialVersionUID = -71866183555507085L;

    public CheckNodeMessage(ServentInfo senderPort, ServentInfo receiverPort) {
        super(MessageType.CHECK_NODE, senderPort, receiverPort, "PONG");
    }
}
