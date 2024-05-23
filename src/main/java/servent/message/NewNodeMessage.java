package servent.message;

import app.ServentInfo;

public class NewNodeMessage extends BasicMessage {
    private static final long serialVersionUID = 3899837286642127636L;

    private final ServentInfo newServentInfo;
    private boolean initializer = false;

    public NewNodeMessage(ServentInfo senderPort, ServentInfo receiverPort, ServentInfo newServentInfo) {
        super(MessageType.NEW_NODE, senderPort, receiverPort);
        this.newServentInfo = newServentInfo;
    }

    public ServentInfo getNewServentInfo() {
        return newServentInfo;
    }

    public boolean isInitializer() {
        return initializer;
    }

    public void setInitializer(boolean initializer) {
        this.initializer = initializer;
    }
}
