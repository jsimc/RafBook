package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class DHTGetFailHandler implements MessageHandler {

    private Message clientMessage;

    public DHTGetFailHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.FAIL_GET) {
            try {
                int key = Integer.parseInt(clientMessage.getMessageText());
                AppConfig.timestampedErrorPrint("Couldn't find value with key: " + key);
            } catch (NumberFormatException e) {
                AppConfig.timestampedErrorPrint("Bad key format: " + clientMessage.getMessageText());
            }
        } else {
            AppConfig.timestampedErrorPrint("FAIL_GET Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
