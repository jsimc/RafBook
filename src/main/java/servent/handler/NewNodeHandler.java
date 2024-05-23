package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

public class NewNodeHandler implements MessageHandler {

    private Message clientMessage;

    public NewNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.NEW_NODE) {
            NewNodeMessage newNodeMessage = (NewNodeMessage) clientMessage;
            ServentInfo newNodeInfo = newNodeMessage.getNewServentInfo();
            int newNodeId = newNodeInfo.getHashId();

            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(newNodeId);

            int add = AppConfig.routingTable.update(newNodeInfo);
            if(add == 0) {
                AppConfig.timestampedStandardPrint("Novi u routingTable: " + newNodeId);
            } else if (add == -1){
                AppConfig.timestampedStandardPrint("Collision: " + newNodeId + " already exists.");
                return;
            }

            TellNewNodeMessage tellNewNodeMessage = new TellNewNodeMessage(AppConfig.myServentInfo, newNodeInfo, findNodeAnswer);
            if(newNodeMessage.isInitializer()) {
                tellNewNodeMessage.setInit(true);
            }
            MessageUtil.sendMessage(tellNewNodeMessage);
        } else {
            AppConfig.timestampedErrorPrint("NEW_NODE handler got something that is not new node message.");
        }
    }
}
