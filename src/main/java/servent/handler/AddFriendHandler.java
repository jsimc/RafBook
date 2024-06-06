package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.Set;

public class AddFriendHandler implements MessageHandler{
    private Message clientMessage;

    public AddFriendHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.ADD_FRIEND) {
            int friendHashId = ((AddFriendMessage) clientMessage).getFriendHashId();
            ServentInfo originalSender = ((AddFriendMessage) clientMessage).getOriginalSender();

            if(AppConfig.myServentInfo.getHashId() == friendHashId) {
                AppConfig.myServentInfo.addFriend(originalSender); // dodaj ga u svoje prijatelje
                TellFriendMessage tellFriendMessage = new TellFriendMessage(AppConfig.myServentInfo, originalSender, AppConfig.myServentInfo);
                MessageUtil.sendMessage(tellFriendMessage);
                return;
            }

            Set<ServentInfo> serventInfoSet = ((AddFriendMessage) clientMessage).getServentInfos();
            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(friendHashId);
            if(AppConfig.isSame(serventInfoSet, findNodeAnswer.getNodes())) {
                if(serventInfoSet.stream().anyMatch(serventInfo -> serventInfo.getHashId() == friendHashId)) return;
                TellFriendMessage tellFriendMessage = new TellFriendMessage(AppConfig.myServentInfo, originalSender, null);
                MessageUtil.sendMessage(tellFriendMessage);
                return;
            }
            Set<ServentInfo> newSet = new HashSet<>(serventInfoSet);
            newSet.addAll(findNodeAnswer.getNodes());
            findNodeAnswer.getNodes().forEach(serventInfo -> {
                if(serventInfo.equals(originalSender) || serventInfo.equals(AppConfig.myServentInfo) || serventInfoSet.contains(serventInfo)) return; // preskoci sebe i original sendera.
                AddFriendMessage addFriendMessage = new AddFriendMessage(AppConfig.myServentInfo, serventInfo, originalSender, friendHashId, newSet);
                MessageUtil.sendMessage(addFriendMessage);
            });
        } else {
            AppConfig.timestampedErrorPrint("ADD_FRIEND Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
