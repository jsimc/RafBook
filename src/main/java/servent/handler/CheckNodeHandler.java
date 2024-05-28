package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.*;
import servent.message.util.MessageUtil;

public class CheckNodeHandler implements MessageHandler{

    private Message clientMessage;

    public CheckNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.CHECK_NODE) {
            // ovde treba da pingujemo taj node koji nam je poslat u poruci
            ServentInfo nodeToCheck = ((CheckNodeMessage)clientMessage).getNodeToCheck();
            PingMessage pingMessage = new PingMessage(AppConfig.myServentInfo, nodeToCheck);
            MessageUtil.sendMessage(pingMessage);
            AppConfig.isAlive.put(nodeToCheck, false);
            Thread waitForPong = new Thread(() -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                CheckResult checkResult;
                if(!AppConfig.isAlive.get(nodeToCheck)) {
                    checkResult = CheckResult.FAIL;
                    AppConfig.timestampedErrorPrint("Hard reset for node: " + nodeToCheck);
                    if(AppConfig.routingTable.contains(nodeToCheck.getHashId())) { // izbacujemo ga i kod nas ako smo ga imali u routingTable.
                        AppConfig.routingTable.delete(nodeToCheck);
                    }
                } else {
                    checkResult = CheckResult.SUCCESS;
                }
                TellCheckNodeMessage tellCheckNodeMessage = new TellCheckNodeMessage(AppConfig.myServentInfo, clientMessage.getSender(), nodeToCheck, checkResult);
                MessageUtil.sendMessage(tellCheckNodeMessage);
            }); // ne smem da koristim pong checker jer ce da udje u vrzino kolo.
            waitForPong.start();
        } else {
            AppConfig.timestampedErrorPrint("CHECK_NODE Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
