package servent.handler;

import app.AppConfig;
import app.MyFile;
import app.ServentInfo;
import servent.message.DHTPutMessage;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.util.MessageUtil;
import servent.message.util.RepublishValue;

public class DHTPutHandler implements MessageHandler {

    private Message clientMessage;

    public DHTPutHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.PUT) {
            DHTPutMessage dhtPutMessage = (DHTPutMessage) clientMessage;
            MyFile value = dhtPutMessage.getValue();
            int key = value.getKey();

            if(AppConfig.routingTable.containsValue(key)) {
                AppConfig.timestampedErrorPrint("Already have key: " + key);
                return;
            }

            AppConfig.routingTable.putValue(key, value);

            // TODO uncomment
//            RepublishValue republishValue = new RepublishValue(value);
//            Thread thread = new Thread(republishValue);
//            thread.start();
        }  else {
            AppConfig.timestampedErrorPrint("PUT Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
