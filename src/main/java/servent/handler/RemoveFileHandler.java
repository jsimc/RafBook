package servent.handler;

import app.AppConfig;
import app.MyFile;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.RemoveFileMessage;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.Set;

public class RemoveFileHandler implements MessageHandler{

    private Message clientMessage;

    public RemoveFileHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.REMOVE_FILE) {
            RemoveFileMessage removeFileMessage = (RemoveFileMessage) clientMessage;
            int fileKey = removeFileMessage.getFileKey();
            Set<ServentInfo> serventInfoSet = removeFileMessage.getServentInfoSet();
            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(fileKey);

            MyFile myFile = AppConfig.routingTable.removeValue(fileKey);
            if(myFile != null) AppConfig.timestampedStandardPrint("Removing file: " + myFile);

            if(!AppConfig.isSame(serventInfoSet, findNodeAnswer.getNodes())) {
                Set<ServentInfo> newSet = new HashSet<>(serventInfoSet);
                newSet.addAll(findNodeAnswer.getNodes());
                findNodeAnswer.getNodes().forEach(si -> {
                    if(si.equals(removeFileMessage.getSender()) || si.equals(AppConfig.myServentInfo) || serventInfoSet.contains(si)) return;
                    RemoveFileMessage removeFileMessage1 = new RemoveFileMessage(AppConfig.myServentInfo, si, fileKey, newSet);
                    MessageUtil.sendMessage(removeFileMessage1);
                });
            }
        } else {
            AppConfig.timestampedErrorPrint("REMOVE_FILE Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
