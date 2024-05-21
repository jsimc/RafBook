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
// TODO
//  treba ponoviti replikaciju kad god udje neki novi cvor.
// TODO na kraju uradi scheduled ping !
    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.PUT) {
            DHTPutMessage dhtPutMessage = (DHTPutMessage) clientMessage;
            int key = dhtPutMessage.getKey();
            String value = dhtPutMessage.getValue();

            if(AppConfig.routingTable.containsValue(key)) {
                AppConfig.timestampedErrorPrint("Already have key: " + key);
                return;
            }

            for(ServentInfo serventInfo : AppConfig.routingTable.findClosest(key).getNodes()) {
                if(AppConfig.myServentInfo.getHashId() == serventInfo.getHashId()) {
                    AppConfig.routingTable.putValue(key, value);
                    continue;
                }

                DHTPutMessage dhtPutMessage1 = new DHTPutMessage(AppConfig.myServentInfo.getListenerPort(), serventInfo.getListenerPort(), key, value);
                MessageUtil.sendMessage(dhtPutMessage1);
            }
        }
    }
}
