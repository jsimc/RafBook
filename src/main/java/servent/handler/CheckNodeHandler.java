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
            ServentInfo nodeToCheck = ((CheckNodeMessage)clientMessage).getNodeToCheck();

            synchronized (AppConfig.checkNodeLock) {
                if(AppConfig.myServentInfo.equals(nodeToCheck)) {
                    TellCheckNodeMessage tellCheckNodeMessage = new TellCheckNodeMessage(AppConfig.myServentInfo, clientMessage.getSender(), nodeToCheck, CheckResult.SUCCESS);
                    MessageUtil.sendMessage(tellCheckNodeMessage);
                    return;
                }
                PingMessage pingMessage = new PingMessage(AppConfig.myServentInfo, nodeToCheck);
                MessageUtil.sendMessage(pingMessage);
                AppConfig.isAlive.put(nodeToCheck, false);
                Thread waitForPong = new Thread(() -> {
                    try {
                        Thread.sleep(AppConfig.HARD_RESET_MS);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    CheckResult checkResult;
                    System.out.println("NODE TO CHECK: " + nodeToCheck);
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

            }
        } else {
            AppConfig.timestampedErrorPrint("CHECK_NODE Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
