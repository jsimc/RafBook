package servent.message;

import app.ServentInfo;

public class CheckNodeMessage extends BasicMessage {
    private static final long serialVersionUID = -71866183555507085L;

    private final ServentInfo nodeToCheck;

    public CheckNodeMessage(ServentInfo senderPort, ServentInfo receiverPort, ServentInfo nodeToCheck) {
        super(MessageType.CHECK_NODE, senderPort, receiverPort, "CHECK_NODE_"+nodeToCheck.getHashId());
        this.nodeToCheck = nodeToCheck;
    }

    public ServentInfo getNodeToCheck() {
        return nodeToCheck;
    }
}
