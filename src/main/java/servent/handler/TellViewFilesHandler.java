package servent.handler;

import app.AppConfig;
import app.MyFile;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TellViewFilesHandler implements MessageHandler {
    private Message clientMessage;

    public TellViewFilesHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_VIEW_FILES) {
            List<MyFile> fileList = ((TellViewFilesMessage) clientMessage).getMyFileList();
            if(fileList != null){
                AppConfig.timestampedStandardPrint("Files from node " + clientMessage.getSender());
                fileList.forEach(file -> AppConfig.timestampedStandardPrint(file.toString()));
            }
        } else {
            AppConfig.timestampedErrorPrint("TELL_VIEW_FILES Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
