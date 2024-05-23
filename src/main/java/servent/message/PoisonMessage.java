package servent.message;

import app.ServentInfo;

public class PoisonMessage extends BasicMessage {
    private static final long serialVersionUID = -5625132784318034900L;
    public PoisonMessage(ServentInfo senderPort, ServentInfo receiverPort) {
        super(MessageType.POISON, senderPort, receiverPort);
    }
}
