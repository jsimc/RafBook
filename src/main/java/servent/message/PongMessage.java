package servent.message;

import app.ServentInfo;

public class PongMessage extends BasicMessage{
    private static final long serialVersionUID = -71866183898007085L;

    public PongMessage(int senderPort, int receiverPort) {
        super(MessageType.PONG, senderPort, receiverPort, "PONG");
    }
}
