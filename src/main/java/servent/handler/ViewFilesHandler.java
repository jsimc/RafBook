package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.Set;

public class ViewFilesHandler implements MessageHandler{
    private Message clientMessage;

    public ViewFilesHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.VIEW_FILES) {
            ViewFilesMessage viewFilesMessage = (ViewFilesMessage) clientMessage;
            ServentInfo originalSender = viewFilesMessage.getOriginalSender();
            int hashId = viewFilesMessage.getHashId();
            if(AppConfig.myServentInfo.getHashId() == hashId) {
                TellViewFilesMessage tellViewFilesMessage = new TellViewFilesMessage(
                        AppConfig.myServentInfo,
                        viewFilesMessage.getOriginalSender(),
                        AppConfig.myServentInfo.getFriends().contains(originalSender) ? AppConfig.routingTable.getAllMyFiles() : AppConfig.routingTable.getPublicMyFiles()
                );
                MessageUtil.sendMessage(tellViewFilesMessage);
                return;
            }

            Set<ServentInfo> serventInfoSet = ((AddFriendMessage) clientMessage).getServentInfos();
            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(hashId);
            if(AppConfig.isSame(serventInfoSet, findNodeAnswer.getNodes())) {
                TellViewFilesMessage tellFriendMessage = new TellViewFilesMessage(AppConfig.myServentInfo, originalSender, null);
                MessageUtil.sendMessage(tellFriendMessage);
                return;
            }

            Set<ServentInfo> newSet = new HashSet<>(serventInfoSet);
            newSet.addAll(findNodeAnswer.getNodes());
            findNodeAnswer.getNodes().forEach(serventInfo -> {
                if(serventInfo.equals(originalSender) || serventInfo.equals(AppConfig.myServentInfo) || serventInfoSet.contains(serventInfo)) return; // preskoci sebe i original sendera.
                ViewFilesMessage viewFilesMessage1 = new ViewFilesMessage(AppConfig.myServentInfo, serventInfo, originalSender, hashId, newSet);
                MessageUtil.sendMessage(viewFilesMessage1);
            });
        } else {
            AppConfig.timestampedErrorPrint("VIEW_FILES Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
