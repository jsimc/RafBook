package servent.message;

import app.ServentInfo;

import java.io.Serializable;

public interface Message extends Serializable {
    int getSenderPort();
    int getReceiverPort();
    String getReceiverIpAddress();
    MessageType getMessageType();
    String getMessageText();
    int getMessageId();

    ServentInfo getReceiver();
    ServentInfo getSender();
}
