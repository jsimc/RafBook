package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.CheckResult;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellCheckNodeMessage;

public class TellCheckNodeHandler implements MessageHandler{
    private Message clientMessage;

    public TellCheckNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_CHECK_NODE) {
            CheckResult checkResult = ((TellCheckNodeMessage)clientMessage).getCheckResult();
            ServentInfo nodeToCheck = ((TellCheckNodeMessage)clientMessage).getNode();
            if(checkResult.equals(CheckResult.FAIL)) {
                // ako je fail odmah ga izbacuj
                if(AppConfig.routingTable.contains(nodeToCheck.getHashId())) {
                    AppConfig.routingTable.delete(nodeToCheck);
                    AppConfig.isAlive.remove(nodeToCheck);
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("TELL_CHECK_NODE Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
