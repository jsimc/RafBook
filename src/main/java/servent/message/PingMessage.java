package servent.message;

import app.ServentInfo;

public class PingMessage  extends BasicMessage {

    private static final long serialVersionUID = -1934709147043909111L;

    public PingMessage(int senderPort, int receiverPort) {
        super(MessageType.PING, senderPort, receiverPort, "PING");
    }

}
