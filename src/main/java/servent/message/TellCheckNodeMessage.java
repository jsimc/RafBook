package servent.message;

import app.ServentInfo;

public class TellCheckNodeMessage  extends BasicMessage {
    private static final long serialVersionUID = -71866183555507085L;

    private final ServentInfo node;
    private final CheckResult checkResult;

    public TellCheckNodeMessage(ServentInfo senderPort, ServentInfo receiverPort, ServentInfo node, CheckResult checkResult) {
        super(MessageType.TELL_CHECK_NODE, senderPort, receiverPort, "TELL_CHECK_NODE_"+node.getHashId());
        this.node = node;
        this.checkResult = checkResult;
    }

    public ServentInfo getNode() {
        return node;
    }

    public CheckResult getCheckResult() {
        return checkResult;
    }
}
