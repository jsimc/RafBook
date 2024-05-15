package servent.message;

public class PoisonMessage extends BasicMessage {
    private static final long serialVersionUID = -5625132784318034900L;
    public PoisonMessage(int senderPort, int receiverPort) {
        super(MessageType.POISON, senderPort, receiverPort);
    }
}
