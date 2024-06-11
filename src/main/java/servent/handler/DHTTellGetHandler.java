package servent.handler;

import app.AppConfig;
import app.MyFile;
import servent.message.DHTTellGetMessage;
import servent.message.Message;
import servent.message.MessageType;

public class DHTTellGetHandler implements MessageHandler {

    private Message clientMessage;

    public DHTTellGetHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_GET) {
            // znaci da smo nasli vrednost sa tim kljucem.
            DHTTellGetMessage dhtTellGetMessage = (DHTTellGetMessage) clientMessage;
            int key = dhtTellGetMessage.getKey();
            MyFile value = dhtTellGetMessage.getValue();

            if(AppConfig.routingTable.containsValue(key)) return; // dobili smo je od nekog drugog vec.

            AppConfig.mutex.unlock();
            // mogli bismo iz predostroznosti da je sacuvamo kod nas
            AppConfig.routingTable.putValue(key, value);

            AppConfig.timestampedStandardPrint("Found! Key: " + key + ", value: " + value);
        } else {
            AppConfig.timestampedErrorPrint("TELL_GET Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
