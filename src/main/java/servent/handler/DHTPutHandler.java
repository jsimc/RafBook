package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.DHTPutMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

public class DHTPutHandler implements MessageHandler {

    private Message clientMessage;

    public DHTPutHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.PUT) {
            DHTPutMessage dhtPutMessage = (DHTPutMessage) clientMessage;
            String value = dhtPutMessage.getValue();
            int key = AppConfig.valueHash(value);

            if(AppConfig.routingTable.containsValue(key)) {
                AppConfig.timestampedErrorPrint("Already have key: " + key);
                return;
            }

            AppConfig.routingTable.putValue(key, value);
        }  else {
            AppConfig.timestampedErrorPrint("PUT Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
