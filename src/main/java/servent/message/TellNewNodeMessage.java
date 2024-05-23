package servent.message;

import app.ServentInfo;
import app.kademlia.FindNodeAnswer;

public class TellNewNodeMessage extends BasicMessage {
    private static final long serialVersionUID = 3899837285542127636L;

    private final FindNodeAnswer findNodeAnswer;

    private boolean init = false;
    public TellNewNodeMessage(ServentInfo senderPort, ServentInfo receiverPort, FindNodeAnswer findNodeAnswer) {
        super(MessageType.TELL_NEW_NODE, senderPort, receiverPort);
        this.findNodeAnswer = findNodeAnswer;
    }

    public FindNodeAnswer getFindNodeAnswer() {
        return findNodeAnswer;
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }
}
