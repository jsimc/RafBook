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
            String value = dhtPutMessage.getValue();
            int key = AppConfig.valueHash(value);

            if(AppConfig.routingTable.containsValue(key)) {
                AppConfig.timestampedErrorPrint("Already have key: " + key);
                return;
            }

            for(ServentInfo serventInfo : AppConfig.routingTable.findClosest(key).getNodes()) {
                if(AppConfig.myServentInfo.getHashId() == serventInfo.getHashId()) {
                    AppConfig.routingTable.putValue(key, value);
                    AppConfig.timestampedErrorPrint("Ubacila sebi: " + key);
                    continue;
                }
                if(serventInfo.getHashId() == dhtPutMessage.getSender().getHashId()) {
                    AppConfig.timestampedErrorPrint("Not going to send it to our sender: " + dhtPutMessage.getSender().getHashId());
                    continue;
                }

                DHTPutMessage dhtPutMessage1 = new DHTPutMessage(AppConfig.myServentInfo, serventInfo, value);
                MessageUtil.sendMessage(dhtPutMessage1);
            }
        }  else {
            AppConfig.timestampedErrorPrint("PUT Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
